# ==============================================================================
# Fomo Android - Database Backup & Restoration Diagnostic Tool (PowerShell)
# Facilitates nonproduction testing, disaster recovery verification, and state triage
# ==============================================================================

param (
    [Parameter(Mandatory=$true)]
    [ValidateSet("backup", "restore", "check", "clean")]
    [string]$Action,

    [string]$PackageName = "com.example.fomo",
    [string]$BackupPath = ".\fomo_backup.db"
)

$ErrorActionPreference = "Stop"

function Assert-AdbConnected {
    $devices = adb devices | Select-String "device$"
    if (-not $devices) {
        Write-Error "No connected Android devices found via ADB. Please connect a physical device or start an emulator."
    }
}

switch ($Action) {
    "backup" {
        Assert-AdbConnected
        Write-Host "[+] Backing up database from $PackageName to $BackupPath..." -ForegroundColor Cyan
        adb exec-out run-as $PackageName cat databases/fomo.db > $BackupPath
        if (Test-Path $BackupPath) {
            $size = (Get-Item $BackupPath).Length
            Write-Host "[OK] Backup completed successfully ($size bytes written)." -ForegroundColor Green
        } else {
            Write-Error "Backup failed: destination file not found."
        }
    }

    "restore" {
        Assert-AdbConnected
        if (-not (Test-Path $BackupPath)) {
            Write-Error "Backup file $BackupPath does not exist."
        }
        Write-Host "[+] Restoring database from $BackupPath to $PackageName..." -ForegroundColor Cyan
        adb push $BackupPath /data/local/tmp/fomo.db
        adb shell run-as $PackageName cp /data/local/tmp/fomo.db databases/fomo.db
        adb shell rm /data/local/tmp/fomo.db
        Write-Host "[OK] Database restored successfully. Restarting application..." -ForegroundColor Green
        adb shell am force-stop $PackageName
        adb shell am start -n "$PackageName/.ui.main.MainActivity"
    }

    "check" {
        Assert-AdbConnected
        Write-Host "[+] Running SQLite integrity check on device..." -ForegroundColor Cyan
        adb exec-out run-as $PackageName cat databases/fomo.db > ".\temp_check.db"
        if (Get-Command sqlite3 -ErrorAction SilentlyContinue) {
            $result = sqlite3 ".\temp_check.db" "PRAGMA integrity_check;"
            Write-Host "[+] Integrity Check Result: $result" -ForegroundColor Yellow
        } else {
            Write-Host "[!] sqlite3 CLI not available locally. File size: $((Get-Item '.\temp_check.db').Length) bytes."
        }
        Remove-Item ".\temp_check.db" -Force -ErrorAction SilentlyContinue
    }

    "clean" {
        Assert-AdbConnected
        Write-Host "[+] Wiping application state and database..." -ForegroundColor Yellow
        adb shell pm clear $PackageName
        Write-Host "[OK] Application data wiped successfully." -ForegroundColor Green
    }
}

