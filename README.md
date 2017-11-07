# RecordScreenDemo
### 一、参考网上的安卓录屏
###     1.ScreenRecorder.java  录制屏幕操作类
###     2.ScreenRecorderService.java    录制屏幕Service
### 
### 二、使用方法参考下面的类：
###     RecordScreenVoiceActivity.java
###
### 切记：AndroidManifest.xml文件需要注册Service
     <service android:name=".ScreenRecorderService"></service>
### 
### 需要的权限：
     <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
     <uses-permission android:name="android.permission.RECORD_AUDIO" />
     <uses-permission android:name="android.permission.READ_PHONE_STATE" />
###
###
###在Application的继承类的onCreate方法加入如下代码
        // 每次打开APP
     ShareedPreferenceUtils.setIsRecording(getApplicationContext(), false);
