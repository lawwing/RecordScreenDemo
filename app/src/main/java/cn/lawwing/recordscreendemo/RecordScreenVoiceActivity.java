package cn.lawwing.recordscreendemo;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

public class RecordScreenVoiceActivity extends AppCompatActivity
{
    private static final String RECORD_STATUS = "record_status";
    
    private static final int REQUEST_CODE = 1000;
    
    private int mScreenWidth;
    
    private int mScreenHeight;
    
    private int mScreenDensity;
    
    private boolean isStarted = false;
    
    private boolean isVideoSd = true;
    
    private boolean isAudio = true;
    
    private Button recordBtn;
    
    private CheckBox voiceCheckBox;
    
    private RadioGroup radioGroup;
    
    private ListView listView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_screen_voice);
        
        if (savedInstanceState != null)
        {
            isStarted = savedInstanceState.getBoolean(RECORD_STATUS);
        }
        getView();
        getScreenBaseInfo();
    }
    
    private void getScreenBaseInfo()
    {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenDensity = metrics.densityDpi;
        mScreenHeight = metrics.heightPixels;
        mScreenWidth = metrics.widthPixels;
    }
    
    private void getView()
    {
        recordBtn = (Button) findViewById(R.id.recordBtn);
        voiceCheckBox = (CheckBox) findViewById(R.id.cb_voice);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        listView = (ListView) findViewById(R.id.listView);
        
        voiceCheckBox.setChecked(true);
        voiceCheckBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener()
                {
                    
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                            boolean isChecked)
                    {
                        // TODO Auto-generated method stub
                        isAudio = isChecked;
                    }
                });
        
        radioGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener()
                {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup,
                            int checkedId)
                    {
                        switch (checkedId)
                        {
                            case R.id.sd_button:
                                isVideoSd = true;
                                break;
                            case R.id.hd_button:
                                isVideoSd = false;
                                break;
                            
                            default:
                                break;
                            
                        }
                    }
                });
        
        if (isStarted)
        {
            statusIsStart();
        }
        else
        {
            statusIsStop();
        }
        
        recordBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (isStarted)
                {
                    stopRecording();
                    statusIsStop();
                }
                else
                {
                    startRecord();
                    statusIsStart();
                }
            }
        });
        
        setListView();
    }
    
    private void setListView()
    {
    }
    
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startRecord()
    {
        MediaProjectionManager manager = (MediaProjectionManager) getSystemService(
                Context.MEDIA_PROJECTION_SERVICE);
        Intent permissionIntent = manager.createScreenCaptureIntent();
        startActivityForResult(permissionIntent, REQUEST_CODE);
    }
    
    private void stopRecording()
    {
        Intent service = new Intent(this, ScreenRecorderService.class);
        stopService(service);
        isStarted = !isStarted;
    }
    
    private void statusIsStop()
    {
        recordBtn.setText("开始录屏");
    }
    
    private void statusIsStart()
    {
        recordBtn.setText("结束录屏");
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putBoolean(RECORD_STATUS, isStarted);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
            Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                // 获得权限，启动service开始录制
                Intent service = new Intent(this, ScreenRecorderService.class);
                service.putExtra("code", resultCode);
                service.putExtra("data", data);
                service.putExtra("audio", isAudio);
                service.putExtra("width", mScreenWidth);
                service.putExtra("height", mScreenHeight);
                service.putExtra("density", mScreenDensity);
                service.putExtra("quality", isVideoSd);
                this.startService(service);
                isStarted = !isStarted;
                statusIsStart();
                simulateHome();// 返回到首页,关闭当前页
            }
            else
            {
                Toast.makeText(this, "用户取消", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    /**
     * 模拟HOME键返回桌面的功能
     */
    private void simulateHome()
    {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        this.startActivity(intent);
    }
    
}
