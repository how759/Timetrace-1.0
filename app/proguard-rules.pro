# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Program Files (x86)\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-libraryjars ./libs/universal-image-loader-1.9.3-with-sources.jar
-libraryjars ./libs/Android_Location_V1.3.0.jar
-libraryjars ./libs/achartengine-1.1.0.jar

-keep class org.achartengine.** {*;}
-keep class com.amap.api.location.** {*;}
-keep class com.aps.** {*;}
-keep class com.nostra13.universalimageloader.** {*;}
