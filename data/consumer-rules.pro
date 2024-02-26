##--------------- proguard configuration for retrofit2  ----------
## https://github.com/square/retrofit/blob/trunk/retrofit/src/main/resources/META-INF/proguard/retrofit2.pro

-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepattributes AnnotationDefault

-keepclassmembers,allowshrinking,allowoptimization interface * {
    @retrofit2.http.* <methods>;
}

-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn javax.annotation.**
-dontwarn kotlin.Unit
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

-if interface * { @retrofit2.http.* <methods>; }
-keep,allowoptimization interface <1>

-if interface * { @retrofit2.http.* <methods>; }
-keep,allowoptimization interface * extends <1>
-keep,allowoptimization,allowshrinking class kotlin.coroutines.Continuation

-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowoptimization class <3>

-keep,allowoptimization,allowshrinking class retrofit2.Response
