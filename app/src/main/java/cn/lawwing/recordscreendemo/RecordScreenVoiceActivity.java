package cn.lawwing.recordscreendemo;

import java.util.List;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import pub.devrel.easypermissions.EasyPermissions;

public class RecordScreenVoiceActivity extends AppCompatActivity
        implements EasyPermissions.PermissionCallbacks
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
        
        // if (savedInstanceState != null)
        // {
        // Log.e("test", "savedInstanceState != null");
        // isStarted = savedInstanceState.getBoolean(RECORD_STATUS);
        // }
        // else
        // {
        //
        // Log.e("test", "savedInstanceState == null");
        // }
        isStarted = ShareedPreferenceUtils.isRecording(this);
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
                }
            }
        });
        
        setListView();
    }
    
    private void setListView()
    {
    }
    
    /**
     * 开始录屏
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startRecord()
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
        {
            Toast.makeText(RecordScreenVoiceActivity.this,
                    "录制功能仅支持安卓5.0以及以上系统",
                    Toast.LENGTH_LONG).show();
            return;
        }
        
        String[] perms = new String[] {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO };
        if (!EasyPermissions.hasPermissions(RecordScreenVoiceActivity.this,
                perms))
        {
            EasyPermissions.requestPermissions(RecordScreenVoiceActivity.this,
                    "需要访问手机存储权限与摄像头权限，请在系统提醒时选择允许！",
                    10086,
                    perms);
        }
        else
        {
            showDialog();
        }
    }
    
    private void showDialog()
    {
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(
                RecordScreenVoiceActivity.this);
        normalDialog.setIcon(R.mipmap.ic_launcher);
        normalDialog.setTitle("提醒");
        normalDialog.setMessage("若提示\"将开始截取您的屏幕上显示的所有内容\"，请点击立即开始即可开始录制");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // ...To-do
                        MediaProjectionManager manager = (MediaProjectionManager) getSystemService(
                                Context.MEDIA_PROJECTION_SERVICE);
                        Intent permissionIntent = manager
                                .createScreenCaptureIntent();
                        startActivityForResult(permissionIntent, REQUEST_CODE);
                        statusIsStart();
                    }
                });
        
        // 显示
        normalDialog.show();
    }
    
    /**
     * 停止录屏
     */
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
    
    /**
     * 在回调里调用开始录制
     * 
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
            Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                doRegisterReceiver();
                Intent intent = new Intent(RecordScreenVoiceActivity.this,
                        ScreenRecordReceiver.class);
                intent.putExtra("code", resultCode);
                intent.putExtra("data", data);
                intent.putExtra("audio", isAudio);
                intent.putExtra("width", mScreenWidth);
                intent.putExtra("height", mScreenHeight);
                intent.putExtra("density", mScreenDensity);
                intent.putExtra("quality", isVideoSd);
                // Intent intent = new Intent();
                intent.setAction("com.lawwing.record.start");
                sendBroadcast(intent);
                
                // 获得权限，启动service开始录制
                // Intent service = new Intent(this,
                // ScreenRecorderService.class);
                // service.putExtra("code", resultCode);
                // service.putExtra("data", data);
                // service.putExtra("audio", isAudio);
                // service.putExtra("width", mScreenWidth);
                // service.putExtra("height", mScreenHeight);
                // service.putExtra("density", mScreenDensity);
                // service.putExtra("quality", isVideoSd);
                // this.startService(service);
                isStarted = !isStarted;
                statusIsStart();
                // simulateHome();// 返回到首页,关闭当前页
            }
            else
            {
                Toast.makeText(this, "取消录屏", Toast.LENGTH_SHORT).show();
                statusIsStop();
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
    
    private ScreenRecordReceiver recordReceiver;
    
    private void doRegisterReceiver()
    {
        recordReceiver = new ScreenRecordReceiver();
        IntentFilter filter = new IntentFilter("com.lawwing.record.start");
        registerReceiver(recordReceiver, filter);
    }
    
    @Override
    protected void onDestroy()
    {
        ShareedPreferenceUtils.setIsRecording(this, isStarted);
        super.onDestroy();
    }
    
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms)
    {
        // 成功
        if (requestCode == 10086)
        {
            Log.e("test", perms.size() + "success size");
            if (perms.size() == 4)
            {
                showDialog();
            }
        }
    }
    
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms)
    {
        
        // 失败
        if (requestCode == 10086)
        {
            Log.e("test", perms.size() + "faild size");
            Toast.makeText(RecordScreenVoiceActivity.this,
                    "请务必打开全部权限才能录制",
                    Toast.LENGTH_LONG).show();
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode,
            String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);
        
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults,
                this);
    }
    
}
