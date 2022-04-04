package com.example.administrator.beta;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.dd.CircularProgressButton;
import com.nostra13.universalimageloader.utils.L;

import android.view.animation.AccelerateDecelerateInterpolator;

import java.io.IOException;


public class Login_Activity extends AppCompatActivity {
    String UID = null;
    String PSW = null;
    DataStorage dataStorage = null;
    int i = 0;
    private MysurfaceView surfaceView = null;
    private MediaPlayer player = null;
    private SurfaceHolder holder;
    private Button reg = null;
    private Button p_find = null;
    private  CircularProgressButton btn = null;
    private  DataStorage dataStorage_for = null;
    private int record_flag = 0;
    private long mExitTime;
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(System.currentTimeMillis()-mExitTime>2000){
                Object mH;
                Toast.makeText(this,"再次点击退出APP",Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            }else {
                finish();
            }
            return  true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        Intent test = getIntent();
        if(test.hasExtra("bb"))
        {
            Toast.makeText(Login_Activity.this,test.getBundleExtra("bb").getString("info"),Toast.LENGTH_SHORT).show();
        }
        dataStorage_for = new DataStorage(Login_Activity.this,"code","Usr_Inf","1");
        dataStorage = new DataStorage(Login_Activity.this,"Air","User_Data","1");
        String res1 =  dataStorage_for.Read_File();
        if(res1.equals("Empty")){
            record_flag = 1;
        }else{
            try {
                String info[] = res1.split("\n");
                UID = info[0];
                PSW = info[1];
                final Handler handler1 = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message message) {
                        String res = null;
                        if (message.what == 1) {
                            res = "登录成功!╰(*°▽°*)╯";
                            Toast.makeText(Login_Activity.this, res, Toast.LENGTH_SHORT).show();
                            if (dataStorage.Read_File().equals("Empty")) {
                                Intent intent = new Intent(Login_Activity.this, NumberPick_Activity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("UID", UID);
                                bundle.putString("PSW", PSW);
                                intent.putExtra("usr_inf", bundle);
                                Login_Activity.this.startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(Login_Activity.this, SecondActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("UID", UID);
                                bundle.putString("PSW", PSW);
                                bundle.putString("targetFile", "User_Data");
                                bundle.putString("targetFolder", "Air");
                                intent.putExtra("usr_inf", bundle);
                                Login_Activity.this.startActivity(intent);
                                finish();
                            }
                        }
                        return false;
                    }
                });
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        String result = DBu.QuarySql(UID, PSW);
                        Message message = new Message();
                        if (result.equals("OK")) {
                            message.what = 1;
                            record_flag = 0;
                        } else {
                            record_flag = 1;
                        }
                        handler1.sendMessage(message);
                        Looper.loop();
                    }
                }).start();
            }catch (ArrayIndexOutOfBoundsException a){
                a.printStackTrace();
            }
        }
        reg = (Button)findViewById(R.id.register);
        p_find = (Button)findViewById(R.id.password_find);
        final Display display = this.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int screenWidth = size.x;
        final int screenHeight = size.y;
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Activity.this,registerActivity.class);
                Login_Activity.this.startActivity(intent);
            }
        });
        p_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_Activity.this,password_find_Activity.class);
                Login_Activity.this.startActivity(intent);
            }
        });
        surfaceView = (MysurfaceView) findViewById(R.id.video);
        player = new MediaPlayer();

        try{
            player.setDataSource(this,Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.bg));
            holder = surfaceView.getHolder();
            holder.addCallback(new MyCallBack());
            player.prepare();
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    int myVideoWidth = player.getVideoWidth();
                    int myVideoHeight = player.getVideoHeight();
                    float scale_width = screenWidth/myVideoWidth;
                    float scale_height = screenHeight/myVideoHeight;
                    if (scale_width>scale_height){
                        float mVideoWidth = myVideoWidth*scale_width;
                        float mVideoHeight = myVideoHeight*scale_height;
                        surfaceView.getHolder().setFixedSize((int)mVideoWidth,screenHeight);
                        surfaceView.setMeasure(mVideoWidth,screenHeight);
                        surfaceView.requestLayout();
                    }else{
                        float mVideoWidth = myVideoWidth*scale_height;
                        float mVideoHeight = myVideoHeight*scale_width;
                        surfaceView.getHolder().setFixedSize((int)mVideoWidth,(int)mVideoHeight);
                        surfaceView.setMeasure((int)mVideoWidth,(int)mVideoHeight);
                        surfaceView.requestLayout();
                    }
                    player.start();
                    player.setLooping(true);
                }
            });
        }catch (IOException e){
            e.printStackTrace();
        }

        final CircularProgressButton circularProgressButton = (CircularProgressButton) findViewById(R.id.button3);
        circularProgressButton.setIndeterminateProgressMode(true); // 进入不精准进度模式
        dataStorage = new DataStorage(Login_Activity.this,"Air","User_Data","1");
        final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.uid);
        final EditText editText = (EditText)findViewById(R.id.pass);
        final Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                String res = null;
                if(message.what==1)
                {
                    res = "登录成功!╰(*°▽°*)╯";
                    Toast.makeText(Login_Activity.this,res,Toast.LENGTH_SHORT).show();
                    dataStorage_for = new DataStorage(Login_Activity.this,"code","Usr_Inf",UID+"\n"+PSW);
                   dataStorage_for.Write_File();
                    if(dataStorage.Read_File().equals("Empty")){
                        Intent intent = new Intent(Login_Activity.this,NumberPick_Activity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("UID",UID);
                        bundle.putString("PSW",PSW);
                        intent.putExtra("usr_inf",bundle);
                        Login_Activity.this.startActivity(intent);
                        finish();
                    }else{
                        Intent intent = new Intent(Login_Activity.this,SecondActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("UID",UID);
                        bundle.putString("PSW",PSW);
                        bundle.putString("targetFile","User_Data");
                        bundle.putString("targetFolder","Air");
                        intent.putExtra("usr_inf",bundle);
                        Login_Activity.this.startActivity(intent);
                        finish();
                    }
                }else
                {
                    btn.setBackgroundColor(Color.RED);
                    btn.setProgress(-1);
                }
                return false;
            }
        });
        circularProgressButton.setErrorText("Enable");
        circularProgressButton.setCompleteText("OK");
        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn = (CircularProgressButton) view;
                int progress = btn.getProgress();
                if (progress == 0) { // 初始时progress = 0
                    btn.setProgress(50); // 点击后开始不精准进度，不精准进度的进度值一直为50
                } else if (progress == 100) { // 如果当前进度为100，即完成状态，那么重新回到未完成的状态
                    btn.setProgress(0);
                }else if(progress==-1){
                    btn.setProgress(0);
                }
                if(btn.getProgress()==50){
                UID = autoCompleteTextView.getText().toString();
                PSW = editText.getText().toString();
                if(UID.equals("")||PSW.equals(""))
                {
                    Toast.makeText(Login_Activity.this,"用户名和密码不能为空！",Toast.LENGTH_SHORT).show();
                    circularProgressButton.setProgress(0);
                }
                else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            String result = DBu.QuarySql(UID, PSW);
                            Message message = new Message();
                            if (result.equals("OK")) {
                                btn.setBackgroundColor(Color.GREEN);
                                btn.setProgress(100);

                                message.what = 1;
                            } else if (result.equals("Nok1"))//密码不正确
                            {
                                Toast.makeText(Login_Activity.this, "密码错误！", Toast.LENGTH_SHORT).show();
                                message.what = 2;
                            } else if (result.equals("Nok2"))//用户不存在
                            {
                                Toast.makeText(Login_Activity.this, "用户名不存在！", Toast.LENGTH_SHORT).show();
                                message.what = 3;
                            } else if (result.equals("Error")) {
                                //Toast.makeText(Login_Activity.this,"网络连接失败，请检查网络！(๑•̀ㅂ•́)و✧",Toast.LENGTH_SHORT).show();
                                Toast.makeText(Login_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                                message.what = 4;
                            } else {
                                Toast.makeText(Login_Activity.this, result, Toast.LENGTH_SHORT).show();
                                message.what = 5;

                            }
                            handler.sendMessage(message);
                            Looper.loop();
                        }
                    }).start();
                }
                }

            }
        });

    }
    private class MyCallBack implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            player.setDisplay(holder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }
}
