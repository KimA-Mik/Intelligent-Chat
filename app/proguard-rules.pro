-dontobfuscate

-keep,allowoptimization class ru.kima.**

-keep,allowoptimization class androidx.preference.** { public protected *; }
-keep,allowoptimization class androidx.room.** { public protected *; }
-keep,allowoptimization class kotlin.** { public protected *; }
-keep,allowoptimization class kotlinx.coroutines.** { public protected *; }
-keep,allowoptimization class kotlinx.serialization.** { public protected *; }
-keep,allowoptimization class kotlin.time.** { public protected *; }
-keep,allowoptimization class coil3.** { public protected *; }
-keep,allowoptimization class com.mikepenz.** { public protected *; }