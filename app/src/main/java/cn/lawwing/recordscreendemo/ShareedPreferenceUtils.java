package cn.lawwing.recordscreendemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * <pre>
 * 项目名称：surfond
 * 类描述：
 * 创建人：David
 * 创建时间：2017/1/22 15:25
 * 邮箱：zb@clearcom.com.cn
 * </pre>
 */

public class ShareedPreferenceUtils
{
    
    public static final String IS_RECORDING = "appToken";
    
    public static final String LOGIN_ACCOUNT = "login_account";
    
    public static SharedPreferences getSharedPreferences(Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
    
    public static void setIsRecording(Context context, boolean isOpen)
    {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putBoolean(IS_RECORDING, isOpen).apply();
    }
    
    public static boolean isRecording(Context context)
    {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getBoolean(IS_RECORDING, false);
    }
    
    public static void setLoginAccount(Context context, String acount)
    {
        SharedPreferences sp = getSharedPreferences(context);
        sp.edit().putString(LOGIN_ACCOUNT, acount).apply();
    }
    
    public static String getLoginAccount(Context context)
    {
        SharedPreferences sp = ShareedPreferenceUtils
                .getSharedPreferences(context);
        return sp.getString(LOGIN_ACCOUNT, "");
    }
}
