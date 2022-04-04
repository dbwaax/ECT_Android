package com.example.administrator.beta;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private int count = 0;
    private boolean flag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.Ads_L);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = false;
                count = 0;
                Intent intent = new Intent(MainActivity.this,Login_Activity.class);//Login_Activity.class);
            MainActivity.this.startActivity(intent);
            finish();
        }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (flag) {
                    try {
                        Thread.sleep(1000);
                        count++;
                        if(count==4){
                            flag = false;
                            count = 0;
                            Intent intent = new Intent(MainActivity.this,Login_Activity.class);//Login_Activity.class);
                            MainActivity.this.startActivity(intent);
                            finish();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
