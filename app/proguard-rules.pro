# ProGuard / R8 Rules for Fomo Android Application
# Enforces safe code shrinking, dead-code elimination, and obfuscation.

# Preserve debugging line numbers and stack traces
-keepattributes LineNumberTable,SourceFile
-renamesourcefileattribute SourceFile

# Preserve standard annotations and generic signatures
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

# Retrofit 2 Keep Rules
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# OkHttp 3 Keep Rules
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# Gson Serialization Keep Rules
-keepattributes *Annotation*
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-keep class com.example.fomo.data.remote.dto.** { *; }

# Room Database Keep Rules
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao interface * { *; }
-keep class * extends androidx.room.migration.Migration

# ViewBinding Keep Rules
-keep class com.example.fomo.databinding.** { *; }

# Security and Cryptography Managers
-keep class com.example.fomo.security.** { *; }
-keep class javax.crypto.** { *; }

# Core Performance and Concurrency Managers
-keep class com.example.fomo.core.** { *; }
