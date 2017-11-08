package cn.lawwing.recordscreendemo;

import android.app.Application;
import android.util.Log;

import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.rtmp.TXLiveBase;
import com.tencent.rtmp.TXLiveConstants;

/**
 * author lawwing time 2017/9/11 14:13 describe
 **/
public class RecordApp extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        // 每次打开APP
        ShareedPreferenceUtils.setIsRecording(getApplicationContext(), false);
        ShareedPreferenceUtils.setLoginAccount(getApplicationContext(),
                "15622104962");
        String sdkver = TXLiveBase.getSDKVersionStr();
        Log.e("liteavsdk", "liteav sdk version is : " + sdkver);
        TXLiveBase.setConsoleEnabled(true);
        TXLiveBase.setLogLevel(TXLiveConstants.LOG_LEVEL_DEBUG);
        
        CrashReport
                .initCrashReport(getApplicationContext(), "9d5501966c", false);
    }
}
