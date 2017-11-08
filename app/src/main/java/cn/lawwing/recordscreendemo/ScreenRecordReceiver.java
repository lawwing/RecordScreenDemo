package cn.lawwing.recordscreendemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.util.Log;

public class ScreenRecordReceiver extends BroadcastReceiver
{
    
    private Intent service;
    
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent != null)
        {
            if (intent.getAction().equals("com.lawwing.record.start"))
            {
                Log.v("ScreenRecorderService", "开始了");
                if (service == null)
                {
                    service = new Intent(context, ScreenRecorderService.class);
                    service.putExtra("code", intent.getIntExtra("code", 0));
                    service.putExtra("data", intent.getParcelableExtra("data"));
                    service.putExtra("audio",
                            intent.getBooleanExtra("audio", true));
                    service.putExtra("width", intent.getIntExtra("width", 720));
                    service.putExtra("height",
                            intent.getIntExtra("height", 1280));
                    service.putExtra("density",
                            intent.getIntExtra("density", 0));
                    service.putExtra("quality",
                            intent.getBooleanExtra("quality", true));
                    service.putExtra("fileName",
                            intent.getStringExtra("fileName"));
                    service.putExtra("filePath",
                            intent.getStringExtra("filePath"));
                    context.startService(service);
                }
                else
                {
                    // context.stopService(service);
                    // service = null;
                }
                // Intent service = new Intent(context,
                // ScreenRecorderService.class);
                // context.startService(service);
            }
        }
    }
    
}
