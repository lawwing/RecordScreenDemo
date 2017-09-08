package cn.lawwing.recordscreendemo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * author lawwing time 2017/9/8 14:00 describe
 **/
public class ScreenRecorderService extends Service
{
    private static final String TAG = "ScreenRecorderService";
    
    private int mScreenWidth;
    
    private int mScreenHeight;
    
    private int mScreenDensity;
    
    private int mResultCode;
    
    private Intent mResultData;
    
    private boolean isVideoSd = true;
    
    private boolean isAudio = true;
    
    private MediaProjection mediaProjection;
    
    private MediaRecorder mediaRecorder;
    
    private VirtualDisplay virtualDisplay;
    
    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.e(TAG, "服务onCreate方法...");
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.e(TAG, "服务onStartCommand方法...");
        mResultCode = intent.getIntExtra("code", -1);
        mResultData = intent.getParcelableExtra("data");
        
        mScreenHeight = intent.getIntExtra("height", 1280);
        mScreenWidth = intent.getIntExtra("width", 720);
        mScreenDensity = intent.getIntExtra("density", 1);
        
        isAudio = intent.getBooleanExtra("audio", true);
        isVideoSd = intent.getBooleanExtra("quality", true);
        
        mediaProjection = createMediaProjection();
        mediaRecorder = createMediaRecorder();
        virtualDisplay = createVirtualDisplay();
        mediaRecorder.start();
        
        return Service.START_NOT_STICKY;
    }
    
    private MediaProjection createMediaProjection()
    {
        return ((MediaProjectionManager) getSystemService(
                Context.MEDIA_PROJECTION_SERVICE))
                        .getMediaProjection(mResultCode, mResultData);
    }
    
    private MediaRecorder createMediaRecorder()
    {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd-HH-mm-ss");
        Date curDate = new Date(System.currentTimeMillis());
        String curTime = formatter.format(curDate).replace(" ", "");
        String videoQuality = "HD";
        
        if (isVideoSd)
        {
            videoQuality = "SD";
        }
        
        MediaRecorder mediaRecorder = new MediaRecorder();
        if (isAudio)
        {
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        }
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setOutputFile(Environment.getExternalStorageDirectory()
                + "/" + curTime + ".mp4");
        mediaRecorder.setVideoSize(mScreenWidth, mScreenHeight);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        if (isAudio)
        {
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        }
        int bitRate;
        if (isVideoSd)
        {
            mediaRecorder.setVideoEncodingBitRate(mScreenHeight * mScreenWidth);
            mediaRecorder.setVideoFrameRate(18);
            bitRate = mScreenWidth * mScreenHeight / 1000;
        }
        else
        {
            mediaRecorder
                    .setVideoEncodingBitRate(5 * mScreenHeight * mScreenWidth);
            mediaRecorder.setVideoFrameRate(18);
            bitRate = 5 * mScreenWidth * mScreenHeight / 1000;
        }
        try
        {
            mediaRecorder.prepare();
        }
        catch (IllegalStateException | IOException e)
        {
            e.printStackTrace();
        }
        return mediaRecorder;
    }
    
    private VirtualDisplay createVirtualDisplay()
    {
        return mediaProjection.createVirtualDisplay(TAG,
                mScreenWidth,
                mScreenHeight,
                mScreenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mediaRecorder.getSurface(),
                null,
                null);
    }
    
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
        if (virtualDisplay != null)
        {
            virtualDisplay.release();
            virtualDisplay = null;
        }
        if (mediaRecorder != null)
        {
            mediaRecorder.setOnErrorListener(null);
            mediaProjection.stop();
            mediaRecorder.release();
        }
        if (mediaProjection != null)
        {
            mediaProjection.stop();
            mediaProjection = null;
        }
    }
    
    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}
