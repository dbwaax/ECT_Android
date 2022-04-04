package com.example.administrator.beta;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.Formatter;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.NumberPicker.OnScrollListener;
import android.widget.Toast;

public class NumberPick_Activity extends AppCompatActivity implements OnValueChangeListener, Formatter, OnScrollListener {

    private Bundle bundle = new Bundle();
    private DataStorage dataStorage = null;
    private String targetFile = "User_Data";
    private String targetFolder = "Air";
    private Intent intent = null;
    private mNumberPicker numberPicker1;
    private mNumberPicker numberPicker2;
    private mNumberPicker numberPicker3;
    private int DeviceX = 0;
    private int DeviceY = 0;
    public int Cube;
    private long mExitTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - mExitTime > 2000) {
                Object mH;
                Toast.makeText(this, "再次点击退出APP", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_pick_);
        intent = getIntent();
        bundle = intent.getBundleExtra("usr_inf");
        getDeviceSize();
        NumberPick_Init();
        Button button_confirm = (Button) findViewById(R.id.confirm);
        Button button_funny = (Button) findViewById(R.id.funny);
        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Cube>0) {
                    dataStorage = new DataStorage(NumberPick_Activity.this,targetFolder,targetFile,String.valueOf(Cube));
                    if(dataStorage.Write_File()){
                        //Toast.makeText(NumberPick_Activity.this,dataStorage.Read_File(),Toast.LENGTH_SHORT).show();
                        Toast.makeText(NumberPick_Activity.this,"数据保存成功！φ(≧ω≦*)♪",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(NumberPick_Activity.this,"写入失败,请检查您的权限",Toast.LENGTH_SHORT).show();
                    }
                    intent = new Intent(NumberPick_Activity.this, SecondActivity.class);
                    bundle.putString("targetFile",targetFile);
                    bundle.putString("targetFolder",targetFolder);
                    intent.putExtra("usr_inf", bundle);
                    NumberPick_Activity.this.startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(NumberPick_Activity.this,"输入值不能为0，请重新输入",Toast.LENGTH_SHORT).show();
                }
            }
        });
        button_funny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(NumberPick_Activity.this,"┗|｀O′|┛ 嗷~~",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getDeviceSize(){
        Point p = new Point();
        WindowManager wm = (WindowManager) NumberPick_Activity.this.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getSize(p);
        DeviceX = p.x;
        DeviceY = p.y;
    }

    private void NumberPick_Init(){
        numberPicker1 = (mNumberPicker)findViewById(R.id.NumberPicker1);
        numberPicker2 = (mNumberPicker)findViewById(R.id.NumberPicker2);
        numberPicker3 = (mNumberPicker)findViewById(R.id.NumberPicker3);
        numberPicker1.updateView(numberPicker1);
        numberPicker2.updateView(numberPicker2);
        numberPicker3.updateView(numberPicker3);
        numberPicker1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker2.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker3.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker1.setLayoutParams(new LinearLayout.LayoutParams(DeviceX / 3, LinearLayout.LayoutParams.WRAP_CONTENT));
        numberPicker2.setLayoutParams(new LinearLayout.LayoutParams(DeviceX / 3, LinearLayout.LayoutParams.WRAP_CONTENT));
        numberPicker3.setLayoutParams(new LinearLayout.LayoutParams(DeviceX / 3, LinearLayout.LayoutParams.WRAP_CONTENT));
        numberPicker1.setOnValueChangedListener(this);
        numberPicker1.setOnScrollListener(this);
        numberPicker1.setFormatter(this);
        numberPicker2.setOnValueChangedListener(this);
        numberPicker2.setOnScrollListener(this);
        numberPicker2.setFormatter(this);
        numberPicker3.setOnValueChangedListener(this);
        numberPicker3.setOnScrollListener(this);
        numberPicker3.setFormatter(this);
        numberPicker1.setMaxValue(9);
        numberPicker2.setMaxValue(9);
        numberPicker3.setMaxValue(9);
        numberPicker1.setMinValue(0);
        numberPicker2.setMinValue(0);
        numberPicker3.setMinValue(0);
        numberPicker1.setValue(0);
        numberPicker2.setValue(0);
        numberPicker3.setValue(0);
    }

    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        Cube = getCubes();
    }

    public String format(int value) {
        String tmpStr = String.valueOf(value);
        return tmpStr;
    }
    public void onScrollStateChange(NumberPicker view, int scrollState) {

    }

    public void updateView(View view) {
        if (view instanceof EditText) {
            //这里修改字体的属性
            ((EditText) view).setTextColor(Color.parseColor("#000000"));
            ((EditText) view).setTextSize(20);
        }
    }

    public int getCubes(){
        Cube = (numberPicker1.getValue()*100+numberPicker2.getValue()*10+numberPicker3.getValue())*3;
        return Cube;
    }
}
