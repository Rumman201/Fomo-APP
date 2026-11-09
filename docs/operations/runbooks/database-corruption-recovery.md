# Operational Runbook: SQLite/Room Database Corruption & State Recovery

**Severity**: P1  
**Target System**: Fomo Local Persistence Layer  
**Database**: SQLite (`fomo.db`) via AndroidX Room v3

---

## 1. Overview
SQLite database corruption can occur due to sudden device power loss during disk write cycles, storage hardware failures, or corrupted schema migration sequences. This runbook establishes exact protocols for diagnostic detection, automated recovery, and manual recovery tooling.

---

## 2. Integrity Verification
Android SQLite provides a built-in integrity verification pragma:
```sql
PRAGMA quick_check;
PRAGMA integrity_check;
```
If corruption exists, SQLite returns specific error codes such as `SQLITE_CORRUPT` (code 11) or `SQLITE_IOERR` (code 10).

---

## 3. Automated Application Recovery Mechanisms
In `FomoDatabase.java`:
1. **Migration Safety**:
   - `MIGRATION_1_2`: Creates `saved_cities` table with primary key `cityName`.
   - `MIGRATION_2_3`: Creates performance indices `idx_saved_cities_favorite`, `idx_automation_trigger_time`, and `idx_automation_timestamp`.
2. **Safe Migration Fallback**:
   - In catastrophic scenarios where disk corruption prevents execution of `MIGRATION_X_Y`, Room's `fallbackToDestructiveMigration()` allows recreation of clean tables rather than permanently bricking the application on launch.
3. **Transaction Isolation**:
   - Room writes are wrapped in atomic transactions (`runInTransaction`), preventing partial writes from leaving table state corrupted.

---

## 4. Manual Diagnostic & Recovery Tooling

### Step 1: Extract Database via ADB (Debug/Staging builds)
```bash
# Pull database and WAL journal files
adb exec-out run-as com.example.fomo cat databases/fomo.db > fomo_dump.db
adb exec-out run-as com.example.fomo cat databases/fomo.db-wal > fomo_dump.db-wal 2>/dev/null || true
adb exec-out run-as com.example.fomo cat databases/fomo.db-shm > fomo_dump.db-shm 2>/dev/null || true

# Run integrity check using sqlite3 CLI
sqlite3 fomo_dump.db "PRAGMA integrity_check;"
```

### Step 2: Emergency SQLite Repair
If database file has salvageable rows:
```bash
# Dump SQL records from damaged database and restore into new file
sqlite3 fomo_dump.db ".recover" | sqlite3 fomo_repaired.db

# Push repaired database back to test device
adb push fomo_repaired.db /data/local/tmp/fomo.db
adb shell run-as com.example.fomo cp /data/local/tmp/fomo.db databases/fomo.db
```

### Step 3: Clean State Reset (Field Support Procedure)
For field support helping impacted users without ADB:
1. Direct user to **Android Settings** -> **Apps** -> **Fomo**.
2. Tap **Storage & Cache** -> **Clear Storage**.
3. Application will recreate fresh SQLite schemas on next launch with default coordinates.

