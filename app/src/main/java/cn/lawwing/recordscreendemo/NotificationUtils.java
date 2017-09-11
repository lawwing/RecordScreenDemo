package cn.lawwing.recordscreendemo;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * author lawwing time 2017/9/8 17:52 describe
 **/
public class NotificationUtils
{
    private Context context;
    
    public NotificationUtils(Context context)
    {
        this.context = context;
    }
    
    public void setNotification()
    {
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(NOTIFICATION_SERVICE);
        
        Notification notification = new Notification(R.mipmap.ic_launcher,
                context.getString(R.string.app_name),
                System.currentTimeMillis());
        
        Intent intent = new Intent(context, MainActivity.class);
        notification.flags = Notification.FLAG_ONGOING_EVENT; // 设置常驻 Flag
        PendingIntent contextIntent = PendingIntent.getActivity(context,
                0,
                intent,
                0);
        // notification.setLatestEventInfo(context,
        // context.getString(R.string.app_name),
        // "点击查看",
        // contextIntent);
        notificationManager.notify(R.string.app_name, notification);
    }
    
    // 取消通知
    public void cancelNotification()
    {
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(R.string.app_name);
    }
}
