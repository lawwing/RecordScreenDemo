# RecordScreenDemo
### 一、参考网上的安卓录屏
####     1.ScreenRecorder.java  录制屏幕操作类
####     2.ScreenRecorderService.java    录制屏幕Service
### 
### 二、使用方法参考下面的类：
####     RecordScreenVoiceActivity.java
####
### 切记：AndroidManifest.xml文件需要注册Service
     <service android:name=".ScreenRecorderService"></service>
### 也需要注册广播接收者
     <receiver android:name=".ScreenRecordReceiver"
            android:enabled="true"
            android:exported="true"></receiver>
### 
### 三、需要的权限：
     <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
     <uses-permission android:name="android.permission.RECORD_AUDIO" />
     <uses-permission android:name="android.permission.READ_PHONE_STATE" />
###
### 四、在Application的继承类的onCreate方法加入如下代码
     ShareedPreferenceUtils.setIsRecording(getApplicationContext(), false);
