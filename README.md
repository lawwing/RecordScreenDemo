# RecordScreenDemo
参考网上的安卓录屏
    ScreenRecorder.java  录制屏幕操作类
    ScreenRecorderService.java    录制屏幕Service

使用方法参考下面的类：
    RecordScreenVoiceActivity.java

切记：AndroidManifest.xml文件需要注册Service
    <service android:name=".ScreenRecorderService"></service>

需要的权限：
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.RECORD_AUDIO" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
