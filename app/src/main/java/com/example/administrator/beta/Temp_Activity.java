package com.example.administrator.beta;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.BubbleValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;


public class Temp_Activity extends AppCompatActivity {
    Bundle bundle = new Bundle();
    DBu dBu = new DBu();
    ArrayList<Usr_Data> usr_data = new ArrayList<Usr_Data>();
    private String test;
    private LineChartView lineChartView;
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisValues = new ArrayList<AxisValue>();
    PointValue pointValue;
    private boolean flag = false;
    private int count = 0;
    private  int temp1 = 0; //for thread

    String [] data1 = {"0","5","10","15","20","25"};
    float score[] = new float[6]; //= {1,1,1,1,1,1};

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_);
        Intent intent = getIntent();
        bundle = intent.getBundleExtra("DBdata");
        test = bundle.getString("Temperature");
        Float r = Float.valueOf(test);
        lineChartView = (LineChartView)findViewById(R.id.Liner_Chart);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(100);
                        usr_data = dBu.getChartData(bundle.getString("UID"),bundle.getString("PSW"));
                        try{
                            for (int i = 5; i >= 0; i--) {
                                score[temp1] = usr_data.get(i).getTemperature();
                                temp1++;
                            }
                            temp1 = 0;
                            if (count % 10 == 0) {
                                RepaintChart();
                            }
                            if (count >= 30000) {
                                count = 0;
                            }
                            count++;
                            flag = true;
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        TextView button = (TextView)findViewById(R.id.t1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                while(true){
                    if(flag)
                    {
                        RepaintChart();
                        break;
                    }
                }
            }
        });
    }
    private void getAxisXLables(){
        for(int i = 0; i < data1.length; i++){
            mAxisValues.add(new AxisValue(i).setLabel(data1[i]));
        }
    }
    private void getAxisPoints(){
        for(int i = 0;i < score.length; i++){
            pointValue = new PointValue(i,score[i]);
            pointValue.setLabel(score[i]+"℃");
            mPointValues.add(pointValue);

        }
    }
    private void initLineChart(){
        Line line = new Line(mPointValues).setColor(Color.parseColor("#FFCD41"));
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);
        line.setCubic(true);
        line.setFilled(false);
        line.setHasLabels(true);
        line.setHasLines(true);
        line.setHasPoints(true);
        LineChartData data = new LineChartData();
        lines.add(line);

        data.setLines(lines);

        Axis axisX = new Axis();
        axisX.setHasTiltedLabels(false);
        axisX.setTextColor(Color.GRAY);
        axisX.setTextSize(10);
        axisX.setName("Relative Time/min");
        axisX.setMaxLabelChars(3);
        axisX.setValues(mAxisValues);//data.setAxisXTop();
        axisX.setHasLines(true);
        data.setAxisXBottom(axisX);

        Axis axisY = new Axis().setHasLines(true);
        axisY.setMaxLabelChars(0);//max label length, for example 60
        data.setAxisYLeft(axisY);
        lineChartView.setInteractive(true);
        lineChartView.setZoomType(ZoomType.HORIZONTAL);
        lineChartView.setMaxZoom((float)2);
        lineChartView.setContainerScrollEnabled(true,ContainerScrollType.HORIZONTAL);
        lineChartView.setLineChartData(data);
        lineChartView.setVisibility(View.VISIBLE);
         Viewport v = new Viewport(lineChartView.getMaximumViewport());
         v.top = 65;
        v.bottom = 0;
        lineChartView.setMaximumViewport(v);
        lineChartView.setCurrentViewport(v);
    }
    public void RepaintChart()
    {
        mPointValues.clear();
        mAxisValues.clear();
        getAxisXLables();
        getAxisPoints();
        initLineChart();

    }
}
