package cn.lawwing.recordscreendemo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends Activity implements View.OnClickListener
{
    private static final int REQUEST_CODE = 1;
    
    private MediaProjectionManager mMediaProjectionManager;
    
    private ScreenRecorder mRecorder;
    
    private Button mButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(this);
        // noinspection ResourceType
        mMediaProjectionManager = (MediaProjectionManager) getSystemService(
                MEDIA_PROJECTION_SERVICE);
    }
    
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
            Intent data)
    {
        MediaProjection mediaProjection = mMediaProjectionManager
                .getMediaProjection(resultCode, data);
        if (mediaProjection == null)
        {
            Log.e("@@", "media projection is null");
            return;
        }
        // video size
        final int width = 1280;
        final int height = 720;
        File file = new File(Environment.getExternalStorageDirectory(),
                "record-" + width + "x" + height + "-"
                        + System.currentTimeMillis() + ".mp4");
        final int bitrate = 6000000;
        mRecorder = new ScreenRecorder(width, height, bitrate, 1,
                mediaProjection, file.getAbsolutePath());
        mRecorder.start();
        mButton.setText("Stop Recorder");
        Toast.makeText(this,
                "Screen recorder is running...",
                Toast.LENGTH_SHORT).show();
        moveTaskToBack(true);
    }
    
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v)
    {
        // if (mRecorder != null)
        // {
        // mRecorder.quit();
        // mRecorder = null;
        // mButton.setText("Restart recorder");
        // }
        // else
        // {
        // Intent captureIntent = mMediaProjectionManager
        // .createScreenCaptureIntent();
        // startActivityForResult(captureIntent, REQUEST_CODE);
        // }
        startActivity(
                new Intent(MainActivity.this, RecordScreenVoiceActivity.class));
    }
    
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (mRecorder != null)
        {
            mRecorder.quit();
            mRecorder = null;
        }
    }
}
