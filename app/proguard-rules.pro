-dontobfuscate

-keep,allowoptimization,allowshrinking class ru.kima.**
-keep,allowoptimization,allowshrinking class androidx.**
-keep,allowoptimization,allowshrinking class kotlin.**

-dontwarn javax.script.Bindings
-dontwarn javax.script.ScriptContext
-dontwarn javax.script.ScriptEngine
-dontwarn javax.script.ScriptEngineManager
-dontwarn org.slf4j.impl.StaticLoggerBinder