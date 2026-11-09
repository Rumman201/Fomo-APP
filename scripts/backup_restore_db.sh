#!/usr/bin/env bash
# ==============================================================================
# Fomo Android - Database Backup & Restoration Diagnostic Tool (Bash)
# ==============================================================================

set -euo pipefail

PACKAGE_NAME="${2:-com.example.fomo}"
BACKUP_PATH="${3:-./fomo_backup.db}"

assert_adb() {
    if ! adb devices | grep -q "device$"; then
        echo "[!] Error: No Android device or emulator detected via ADB." >&2
        exit 1
    fi
}

case "${1:-}" in
    backup)
        assert_adb
        echo "[+] Backing up database from ${PACKAGE_NAME} to ${BACKUP_PATH}..."
        adb exec-out run-as "${PACKAGE_NAME}" cat databases/fomo.db > "${BACKUP_PATH}"
        echo "[OK] Backup completed ($(wc -c < "${BACKUP_PATH}") bytes)."
        ;;
    restore)
        assert_adb
        if [ ! -f "${BACKUP_PATH}" ]; then
            echo "[!] Error: Backup file ${BACKUP_PATH} does not exist." >&2
            exit 1
        fi
        echo "[+] Restoring database from ${BACKUP_PATH} to ${PACKAGE_NAME}..."
        adb push "${BACKUP_PATH}" /data/local/tmp/fomo.db
        adb shell run-as "${PACKAGE_NAME}" cp /data/local/tmp/fomo.db databases/fomo.db
        adb shell rm /data/local/tmp/fomo.db
        echo "[OK] Restored successfully. Restarting ${PACKAGE_NAME}..."
        adb shell am force-stop "${PACKAGE_NAME}"
        adb shell am start -n "${PACKAGE_NAME}/.ui.main.MainActivity"
        ;;
    check)
        assert_adb
        echo "[+] Checking database integrity..."
        adb exec-out run-as "${PACKAGE_NAME}" cat databases/fomo.db > /tmp/fomo_check.db
        if command -v sqlite3 >/dev/null 2>&1; then
            sqlite3 /tmp/fomo_check.db "PRAGMA integrity_check;"
        else
            echo "[i] sqlite3 not found locally; file extracted to /tmp/fomo_check.db ($(wc -c < /tmp/fomo_check.db) bytes)."
        fi
        rm -f /tmp/fomo_check.db
        ;;
    clean)
        assert_adb
        echo "[+] Clearing application data..."
        adb shell pm clear "${PACKAGE_NAME}"
        echo "[OK] Data cleared."
        ;;
    *)
        echo "Usage: $0 {backup|restore|check|clean} [package_name] [backup_path]"
        exit 1
        ;;
esac

