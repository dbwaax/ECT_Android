package com.example.administrator.beta;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.github.clans.fab.FloatingActionButton;
import com.study.fileselectlibrary.utils.PermissionCheckUtils;
import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import in.shadowfax.proswipebutton.ProSwipeButton;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ViewportChangeListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;
import lecho.lib.hellocharts.view.PreviewLineChartView;

import static android.app.Notification.VISIBILITY_SECRET;

public class SecondActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private com.github.clans.fab.FloatingActionButton floatingActionButton = null;
    private com.github.clans.fab.FloatingActionButton floatingActionButton1 = null;
    private FloatingActionButton floatingActionButton2 = null;
    private FrameLayout frameLayout = null;
    private PreviewLineChartView previewLineChartView = null;
    private LineChartView chart = null;
    private PieChartView pieChartView = null;
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisValues = new ArrayList<AxisValue>();
    private ArrayList<Usr_Data> usr_data = new ArrayList<Usr_Data>();
    private LineChartData data = null;
    private LineChartData previewData = null;
    private PointValue pointValue;
    private String[] data1 = {"0", "5", "10", "15", "20", "25"};
    private float score[] = new float[50];//{1,2,3,2,1,6,1,2,3,2,1,6,1,2,3,2,1,6};//new float[18];
    private float pie_score[] = new float[9];
    private Bundle bundle = new Bundle();
    private DBu dBu = new DBu();
    private boolean flag = false;
    private int count = 0;
    private int temp1 = 0; //for thread
    //第一次点击事件发生的时间
    private long mExitTime;
    private String UID;
    private String PSW;
    private String targetFile;
    private String targetFolder;
    private String uploadUrl = "http://zyhdbw.cn/RES/dataUpload.php?ak=a535a510c1e14ca345d8d8efeb90cc53";
    private ProSwipeButton proSwipeBtn = null;
    private ProSwipeButton proSwipeBtn_exec = null;
    private Handler handler_proSwipeBtn = null;
    private StringBuffer buffer  = new StringBuffer();;
    private boolean proSwipeBtn_exec_flag = false;
    private boolean isRepeat = false;
    private Execute_AsyncTask execute_asyncTask = null;
    private  boolean tv_flag = false;
    private  int color[] = {Color.rgb(128,0,0),
            Color.rgb(139,0,0),
            Color.rgb(178,34,34),
            Color.rgb(165,42,42),
            Color.rgb(	255,0,0),
            Color.rgb(	205,92,92),
            Color.rgb(188,143,143),
            Color.rgb(240,128,128),
            Color.rgb(255,228,225)};  //饼图颜色版
    private String pieLabel[] = {"<1s-10",
                              "10-20",
                              "20-30",
                              "30-40",
                              "40-50",
                              "50-60",
                              "60-180",
                              "180-360",
                              ">360"};
    /*
        饼图变量
    */
    private PieChartView pieChartViewchart;
    private PieChartData pieChartDatadata;

    private boolean hasLabels = true;//是否在薄片上显示label
    private boolean hasLabelsOutside = false;//是否在薄片外显示label
    private boolean hasCenterCircle = false;//是否中间掏空一个圈
    private boolean hasCenterText1 = true;//掏空圈是的title1
    private boolean hasCenterText2 = true;//掏空圈是的title2
    private boolean isExploded = false;//薄片是否分离
    private boolean hasLabelForSelected = false;
    private Handler handler;
    private TextView tvResult = null;
    private Bitmap LargeBitmap = null;
    private MQTT mqtt = new MQTT();
    public String getMqttrevbuff = null;
    private Handler handler_mqtt = null;
    private NotificationManager mNotificationManager = null;
    private Notification notification = null;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - mExitTime > 2000) {
                Object mH;
                Toast.makeText(this, "再次点击退出APP", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
//                System.exit(0);加此段可彻底退出APP 关闭所有后台线程
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    Bundle bundle1 = new Bundle();
    DataStorage dataStorage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        PermisionUtils.verifyStoragePermissions(this);

        NotificationManagerCompat notification = NotificationManagerCompat.from(SecondActivity.this);
        boolean isEnabled = notification.areNotificationsEnabled();
        if (!isEnabled) {
            //未打开通知
            AlertDialog alertDialog = new AlertDialog.Builder(SecondActivity.this)
                    .setTitle("提示")
                    .setMessage("请在“通知”中打开通知权限")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Intent intent = new Intent();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                                intent.putExtra("android.provider.extra.APP_PACKAGE", SecondActivity.this.getPackageName());
                            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {  //5.0
                                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                                intent.putExtra("app_package", SecondActivity.this.getPackageName());
                                intent.putExtra("app_uid", SecondActivity.this.getApplicationInfo().uid);
                                startActivity(intent);
                            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {  //4.4
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.addCategory(Intent.CATEGORY_DEFAULT);
                                intent.setData(Uri.parse("package:" + SecondActivity.this.getPackageName()));
                            } else if (Build.VERSION.SDK_INT >= 15) {
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                intent.setData(Uri.fromParts("package", SecondActivity.this.getPackageName(), null));
                            }
                            startActivity(intent);

                        }
                    })
                    .create();
            alertDialog.show();
            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        /*开启通知栏功能*/
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        /*开启订阅功能*/
        handler_mqtt = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                if (message.what == 12) {
//                    Toast.makeText(SecondActivity.this, getMqttrevbuff, Toast.LENGTH_SHORT).show();
                    playNotificationVibrate(SecondActivity.this);
                    playNotificationRing(SecondActivity.this);
                    String id = "channel_001";
                    String name = "name";
                    if(getMqttrevbuff.equals("mm1")){
                        try{
                            mNotificationManager.cancel(1);
                            mNotificationManager.deleteNotificationChannel(id);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        Notification notification = null;
                        Intent resultIntent = new Intent(SecondActivity.this, WayOutActivity.class);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//判断API
                            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT);
                            mChannel.enableVibration(true);
                            mChannel.setSound(null, null);
                            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400});
//                        mChannel.setSound();
                            mNotificationManager.createNotificationChannel(mChannel);
                            TaskStackBuilder stackBuilder = TaskStackBuilder.create(SecondActivity.this);
                            stackBuilder.addParentStack(MainActivity.class);
                            stackBuilder.addNextIntent(resultIntent);
                            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                            notification = new Notification.Builder(SecondActivity.this)
                                    .setChannelId(id)
                                    .setContentTitle("交通情况实时推送")
                                    .setContentText("您有新的路况信息推送！")
                                    .setContentIntent(resultPendingIntent)
                                    .setAutoCancel(true)
                                    .setSmallIcon(R.mipmap.ic_launcher_round).build();
                        }else{
                            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(SecondActivity.this)
                                    .setContentTitle("交通情况实时推送")
                                    .setContentText("getMqttrevbuff")
                                    .setSmallIcon(R.mipmap.ic_launcher_round)
                                    .setOngoing(true)
                                    .setAutoCancel(true)
                                    .setDefaults(Notification.DEFAULT_SOUND)    //设置系统的提示音
                                    .setChannelId(id);//无效
                            notification = notificationBuilder.build();

                        }
                        mNotificationManager.notify(1,notification);
                    }
                }
                return false;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
//                        Thread.sleep(5000);
                        mqtt.setHost("47.95.238.162", 1883);
                        BlockingConnection connection = mqtt.blockingConnection();
                        connection.connect();
                        Topic[] topics = {new Topic("test", QoS.AT_LEAST_ONCE)};
                        byte[] qoses = connection.subscribe(topics);
                        org.fusesource.mqtt.client.Message message = connection.receive();
                        System.out.println(message.getTopic());
                        byte[] payload = message.getPayload();
                        getMqttrevbuff = new String(payload);
                        message.ack();
                        connection.disconnect();
                        Message message_mqtt = new Message();                //message 确认MQTT消息获得
                        message_mqtt.what = 12;
                        handler_mqtt.sendMessage(message_mqtt);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();

//        mqttSubscriber = new MqttSubscriber();
//        try {
//            mqttSubscriber.listening();
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        /*获取屏幕宽高*/
        final Display display = this.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        final int screenWidth = size.x;
        final int screenHeight = size.y;


        /*获取布局FrameLayout*/
        frameLayout = (FrameLayout) findViewById(R.id.frame_main);

        Intent intent = getIntent();
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headView = navigationView.getHeaderView(0);
        TextView title = (TextView) headView.findViewById(R.id.title1);
        bundle = intent.getBundleExtra("usr_inf");
        try{
            UID = bundle.getString("UID");
            PSW = bundle.getString("PSW");
            targetFile = bundle.getString("targetFile");
            targetFolder = bundle.getString("targetFolder");
            dataStorage = new DataStorage(SecondActivity.this, targetFolder, targetFile, "1");
            title.setText(dataStorage.Read_File());
        }catch (Exception e){
            e.printStackTrace();
        }


        proSwipeBtn = (ProSwipeButton) findViewById(R.id.awesome_btn);
        handler_proSwipeBtn = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                if (message.what == 1) {
                    proSwipeBtn.showResultIcon(true, true);
                }
                if (message.what == 2) {
                    proSwipeBtn.showResultIcon(false, true);
                }
                if (message.what == 3) {
                    /* tvResult构造*/
                    tvResult = new TextView(SecondActivity.this);
                    tvResult.setVisibility(View.INVISIBLE);
                    tvResult.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            tvResult.setVisibility(View.INVISIBLE);
                            isRepeat = true;
                        }
                    });
                    tvResult.setVisibility(View.VISIBLE);
                    tvResult.setTextColor(Color.WHITE);
                    tvResult.setBackgroundColor(Color.BLACK);
                    tvResult.bringToFront();
                    tv_flag = true;
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams((int) ((screenWidth / 3) * 2), (int) screenHeight);
                    layoutParams.gravity = Gravity.LEFT;
                    frameLayout.addView(tvResult, layoutParams);
                }
                if(message.what == 4){
                    tvResult.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        proSwipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proSwipeBtn.showResultIcon(true,true);
            }
        });
        proSwipeBtn.setOnSwipeListener(new ProSwipeButton.OnSwipeListener() {
            @Override
            public void onSwipeConfirm() {
                // user has swiped the btn. Perform your async operation now
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            PermisionUtils.verifyStoragePermissions(SecondActivity.this);
                            jumpActivity();
                        }catch (Exception e){
                            Toast.makeText(SecondActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                        }

                    }
                }, 200);
            }
        });


        proSwipeBtn_exec = (ProSwipeButton) findViewById(R.id.awesome_btn_exec);
        proSwipeBtn_exec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (proSwipeBtn_exec_flag && isRepeat) {
                    isRepeat = false;
                    Message message = new Message();
                    message.what = 4;
                    handler_proSwipeBtn.sendMessage(message);
                }
            }
        });

        execute_asyncTask = new Execute_AsyncTask();
        proSwipeBtn_exec.setOnSwipeListener(new ProSwipeButton.OnSwipeListener() {
            @Override
            public void onSwipeConfirm() {
                // user has swiped the btn. P  erform your async operation now
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Message message1 = new Message();
                        message1.what = 3;
                        handler_proSwipeBtn.sendMessage(message1);
                    }
                }, 100);
                execute_asyncTask.execute();
            }
        });
        PermissionCheckUtils.setOnOnWantToOpenPermissionListener(new PermissionCheckUtils.OnWantToOpenPermissionListener() {
            @Override
            public void onWantToOpenPermission() {
                Toast.makeText(SecondActivity.this, "请去设置的应用管理中打开应用读取内存的权限", Toast.LENGTH_SHORT).show();
            }
        });

        /*
            建立Piechart
         */
          pieChartView = (PieChartView)findViewById(R.id.piechart);

        /*
            建立Previewchart
         */
        previewLineChartView = (PreviewLineChartView) findViewById(R.id.PL); //new PreviewLineChartView(this);
        chart = (LineChartView) findViewById(R.id.LC); //new LineChartView(this);
        chart.setLineChartData(data);
        chart.setZoomEnabled(true);
        chart.setScrollEnabled(true);

        generateDefaultData();
        previewLineChartView.setLineChartData(previewData);
        previewLineChartView.setViewportChangeListener(new SecondActivity.ViewportListener());
        previewX(true);

        //浮动菜单  激活需要消除layout中的注释
//        HoverView hoverView = (HoverView) findViewById(R.id.hovermenu1);
//        MultipleSectionsHoverMenuService mm1 = new MultipleSectionsHoverMenuService(this);
//        mm1.onHoverMenuLaunched(null, hoverView);

        /*
            主线程刷新PreViewChart的数据每10S一次
                                                  */
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(2000);
                        usr_data = dBu.getChartData("dbw123", "123123");
                        try {
                            for (int i = 49; i >= 0; i--) {
                                score[temp1] = usr_data.get(i).getTemperature() + 25;
                                temp1++;
                            }
                            temp1 = 0;
                            if (count % 10 == 0) {    //十秒刷新一次
                                pie_score = dBu.getPieChartDate();
                                generateData();
                                generateDefaultData();
                                chart.setLineChartData(data);
                                previewLineChartView.setLineChartData(previewData);
                                previewX(false);
                            }
                            if (count >= 30000) {
                                count = 0;
                            }
                            count++;
                            flag = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.second, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




    /*
    用于滑动框的点击事件
                         */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Temperature) {                                                                      //第一个跳转代表动态热力图
            Intent intent = new Intent(SecondActivity.this, heatmap.class);
            SecondActivity.this.startActivity(intent);

        } else if (id == R.id.Humidity) {                                                                  //第二个跳转代表出行方式分析图
            Intent intent = new Intent(SecondActivity.this, WayOutActivity.class);
            SecondActivity.this.startActivity(intent);

        } else if (id == R.id.CO2) {                                                                       //第三个跳转代表人口驻留分析图
            Intent intent = new Intent(SecondActivity.this, ResidencyActivity.class);
            SecondActivity.this.startActivity(intent);

        } else if (id == R.id.PM25) {                                                                       //第四个跳转代表异常驻留分析图
            Intent intent = new Intent(SecondActivity.this, TimeResidencyActivity.class);
            SecondActivity.this.startActivity(intent);
        } else if (id == R.id.Assessment) {
            Intent intent = new Intent(SecondActivity.this, NumberPick_Activity.class);
            bundle1.putString("ACK", "1");
            while (true) {
                if (!bundle1.getString("ACK").equals("1")) {
                    bundle1.putString("UID", UID);
                    bundle1.putString("PSW", PSW);
                    intent.putExtra("DBdata", bundle1);
                    SecondActivity.this.startActivity(intent);
                    break;
                }

            }

        } else if (id == R.id.Suggestion) {
            DataStorage dataStorage_for = new DataStorage(SecondActivity.this, "code", "Usr_Inf", "0" + "\n" + "0");
            dataStorage_for.Write_File();
            Intent intent = new Intent(SecondActivity.this, Login_Activity.class);
            bundle1.putString("info", "已退出账号,并删除当前帐号记录");
            intent.putExtra("bb", bundle1);
            SecondActivity.this.startActivity(intent);
            SecondActivity.this.finish();

        } else if (id == R.id.Reset) {
            Intent intent = new Intent(SecondActivity.this, NumberPick_Activity.class);
            intent.putExtra("usr_inf", bundle);
            SecondActivity.this.startActivity(intent);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /*
        获取饼图数据
    */
    public void generateData() {
        int size = 9;
        List<SliceValue> values = new ArrayList<SliceValue>();
        for (int i = 0; i < size; ++i)
        {
            SliceValue sliceValue = new SliceValue(
                    pie_score[i]+(float) Math.random() , color[i]);//ChartUtils.pickColor());
            sliceValue.setLabel(pieLabel[i]);//设置label
            values.add(sliceValue);
        }

        pieChartDatadata = new PieChartData(values);
        pieChartDatadata.setHasLabels(hasLabels);
        pieChartDatadata.setHasLabelsOnlyForSelected(hasLabelForSelected);
        pieChartDatadata.setHasLabelsOutside(hasLabelsOutside);
        pieChartDatadata.setHasCenterCircle(hasCenterCircle);

        if (isExploded)
        {
            pieChartDatadata.setSlicesSpacing(24);//设置分离距离
        }

        if (hasCenterText1)
        {
            pieChartDatadata.setCenterText1("Hello!");
            pieChartDatadata.setCenterText1FontSize(ChartUtils.px2sp(getResources()
                    .getDisplayMetrics().scaledDensity, (int) getResources()
                    .getDimension(R.dimen.nav_header_vertical_spacing)));
        }

        if (hasCenterText2)
        {
            pieChartDatadata.setCenterText2("Charts (Roboto Italic)");
            pieChartDatadata.setCenterText2FontSize(ChartUtils.px2sp(getResources()
                    .getDisplayMetrics().scaledDensity, (int) getResources()
                    .getDimension(R.dimen.nav_header_vertical_spacing)));
        }

        pieChartView.setPieChartData(pieChartDatadata);
        pieChartView.setCircleFillRatio(2.0f);//设置放大缩小范围
    }


    /*
    以下类用于绘制previewDataChart 与其对应的数据交互
                                                    */
    private void generateDefaultData() {
        int numValues = score.length;

        List<PointValue> values = new ArrayList<PointValue>();
        for (int i = 0; i < numValues; i++) {
            values.add(new PointValue(i, (float) score[i] + (float) Math.random()));//(float) Math.random() * 100f));//(float) Math.random() * 100f));
        }

        Line line = new Line(values);
        line.setColor(ChartUtils.COLOR_GREEN);
        line.setHasPoints(false);// too many values so don't draw points.

        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        data = new LineChartData(lines);
        data.setAxisXBottom(new Axis());
        data.setAxisYLeft(new Axis().setHasLines(true));

        previewData = new LineChartData(data);
        previewData.getLines().get(0).setColor(ChartUtils.DEFAULT_DARKEN_COLOR);
    }
    private void previewY() {
        Viewport tempViewport = new Viewport(chart.getMaximumViewport());
        float dy = tempViewport.height() / 4;
        tempViewport.inset(0, dy);
        previewLineChartView.setCurrentViewportWithAnimation(tempViewport);
        previewLineChartView.setZoomType(ZoomType.VERTICAL);
    }
    private void previewX(boolean animate) {
        Viewport tempViewport = new Viewport(chart.getMaximumViewport());
        float dx = tempViewport.width() / 4;
        tempViewport.inset(dx, 0);
        if (animate) {
            previewLineChartView.setCurrentViewportWithAnimation(tempViewport);
        } else {
            previewLineChartView.setCurrentViewport(tempViewport);
        }
        previewLineChartView.setZoomType(ZoomType.HORIZONTAL);
    }
    private void previewXY() {
        // Better to not modify viewport of any chart directly so create a copy.
        Viewport tempViewport = new Viewport(chart.getMaximumViewport());
        // Make temp viewport smaller.
        float dx = tempViewport.width() / 4;
        float dy = tempViewport.height() / 4;
        tempViewport.inset(dx, dy);
        previewLineChartView.setCurrentViewportWithAnimation(tempViewport);
    }
    private class ViewportListener implements ViewportChangeListener {

        @Override
        public void onViewportChanged(Viewport newViewport) {
            // don't use animation, it is unnecessary when using preview chart.
            chart.setCurrentViewport(newViewport);
        }

    }

    /*
    jumpActivity类
    jumpActivity类主要功能为 从当前Acitivity跳转到本地文件管理器
    并设置类型为excel类型文件，选择后进入onActivityResult得到文件信息
                                                                            */
    public void jumpActivity() {
        PermisionUtils.verifyStoragePermissions(SecondActivity.this);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/vnd.ms-excel");//设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 1);
    }


    /*
    重写onActivityResult类
    onActivityResult类主要功能为 获取本地文件管理器选择的文件路径
    返回String 字符串类型 并调用httpPost类 发送http请求并上传对应选择文件
                                                                            */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                if (uri != null) {
                    try {
                        String path = getPath(this, uri);
                        if (path != null) {
                            File file = new File(path);
                            if (file.exists()) {
                                final String upLoadFilePath = file.toString();
                                final String upLoadFileName = file.getName();
                                Toast.makeText(SecondActivity.this, upLoadFilePath + "1231", Toast.LENGTH_SHORT).show();

                                // Android 4.0 之后不能在主线程中请求HTTP请求
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        boolean result1 = httpPost.httpPost(SecondActivity.this, uploadUrl, upLoadFilePath, upLoadFileName);
                                        Message message = new Message();
                                        //Toast.makeText(SecondActivity.this,result1+"",Toast.LENGTH_SHORT).show();
                                        if (result1) {
                                            message.what = 1;
                                            handler_proSwipeBtn.sendMessage(message);

                                        } else {
                                            message.what = 2;
                                            handler_proSwipeBtn.sendMessage(message);
                                        }

                                    }
                                }).start();
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                  }
            }
        }
    }
    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        System.out.println(isKitKat+"    1 2312312313123123123");
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {//

                final int column_index = cursor.getColumnIndexOrThrow(column);

                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /*
    AsyncTask异步对长时间Http请求进行处理
    Execute_AsyncTask类主要功能为对于数据清洗操作进行Http请求
    并返回操作试试输出信息 已完成数据清洗可视化
                                        */
    public class Execute_AsyncTask extends AsyncTask<Boolean, Integer, Boolean> {
        // 方法1：onPreExecute（）
        // 作用：执行 线程任务前的操作
        // 注：根据需求复写
        @Override
        protected Boolean doInBackground(Boolean... booleans) {
            try {
                URL url = new URL("http://zyhdbw.cn/RES/command_active.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(reader);
                publishProgress(0);
                String temp = null;
                String res = null;
                int i = 0;
                while ((temp = bufferedReader.readLine()) != null) {
                    proSwipeBtn_exec_flag = true;
                    try {
                        res = temp.split("<br>")[1];
                    } catch (IndexOutOfBoundsException e) {
                        res = temp;
                    }
                    if (res.equals("<br>")) {

                    } else {
                        if (buffer.length() <= 2048) {
                            buffer.append(res + "\n");
                        } else {
                            publishProgress(0);
                            try{
                                Thread.sleep(100);
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                            buffer.delete(0, 500);
                            buffer.append(res + "\n");

                        }
                    }
                }
                bufferedReader.close();//记得关闭
                reader.close();
                inputStream.close();
            } catch (MalformedURLException e) {
                proSwipeBtn_exec.showResultIcon(false,true);
                e.printStackTrace();
            } catch (IOException e) {
                proSwipeBtn_exec.showResultIcon(false,true);
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            tv_flag = false;
            tvResult.setText("数据处理完毕");
            proSwipeBtn_exec.showResultIcon(true,true);
        }
        @Override
        protected void onCancelled(Boolean aBoolean) {
            super.onCancelled(aBoolean);
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            try{
                tvResult.setText(buffer.toString());
            }catch (NullPointerException e){
                e.printStackTrace();
            }

        }
    }
    /*  获取通知权限*/
    public void notificationPermission(Activity activity){
        NotificationManagerCompat notification = NotificationManagerCompat.from(activity);
        boolean isEnabled = notification.areNotificationsEnabled();
        if (!isEnabled) {
            //未打开通知
            AlertDialog alertDialog = new AlertDialog.Builder(activity)
                    .setTitle("提示")
                    .setMessage("请在“通知”中打开通知权限")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Intent intent = new Intent();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                                intent.putExtra("android.provider.extra.APP_PACKAGE", SecondActivity.this.getPackageName());
                            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {  //5.0
                                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                                intent.putExtra("app_package", SecondActivity.this.getPackageName());
                                intent.putExtra("app_uid", SecondActivity.this.getApplicationInfo().uid);
                                startActivity(intent);
                            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {  //4.4
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.addCategory(Intent.CATEGORY_DEFAULT);
                                intent.setData(Uri.parse("package:" + SecondActivity.this.getPackageName()));
                            } else if (Build.VERSION.SDK_INT >= 15) {
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                intent.setData(Uri.fromParts("package", SecondActivity.this.getPackageName(), null));
                            }
                            startActivity(intent);

                        }
                    })
                    .create();
            alertDialog.show();
            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        }
    }

    /*使得手机震动*/
    private static void playNotificationVibrate(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        long[] vibrationPattern = new long[]{0, 180, 80, 120};
        // 第一个参数为开关开关的时间，第二个参数是重复次数，振动需要添加权限
        vibrator.vibrate(vibrationPattern, -1);
    }
    /*使得手机振铃一下*/
    private static void playNotificationRing(Context context) {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone rt = RingtoneManager.getRingtone(context, uri);
        rt.play();
    }

}
