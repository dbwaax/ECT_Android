/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.administrator.beta;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.*;
import com.study.fileselectlibrary.AllFileActivity;
import com.study.fileselectlibrary.utils.PermissionCheckUtils;

import io.mattcarroll.hover.Content;

/**
 * A screen that is displayed in our Hello World Hover Menu.
 */
public class HoverMenuScreen implements Content {

    private final Context mContext;
    private final Context context1;
    private final String mPageTitle;
    private final View mWholeScreen;
    private final int mindex;
    public HoverMenuScreen(@NonNull Context context, @NonNull String pageTitle , @NonNull int index) {
        mContext = context.getApplicationContext();
        context1 = context;
        mPageTitle = pageTitle;
        mindex = index;
        mWholeScreen = createScreenView(mindex);
        mWholeScreen.bringToFront();

    }

    @NonNull
    private View createScreenView(int index) {
        float screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        LinearLayout linearLayout = null;
        LinearLayout.LayoutParams ll = null;

        linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(ll);

        if(index==1){
            TextView wholeScreen = new TextView(mContext);
            wholeScreen.setText(mPageTitle);
            wholeScreen.setTextColor(Color.WHITE);
            wholeScreen.setTextSize(30);
            ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ll.gravity =Gravity.CENTER;
            ll.topMargin = 50;
            linearLayout.addView(wholeScreen,ll);

            ImageView imageView = new ImageView(mContext);
            imageView.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            ll = new LinearLayout.LayoutParams((int)(screenWidth/1.5), 7);
            ll.gravity =Gravity.CENTER;
            ll.topMargin = 50;
            linearLayout.addView(imageView,ll);

            final CircularProgressButton circularProgressButton = new CircularProgressButton(mContext);
            circularProgressButton.setText("进行数据上传！");
            circularProgressButton.setIndeterminateProgressMode(true); // 进入不精准进度模式
            circularProgressButton.setErrorText("Enable");
            circularProgressButton.setCompleteText("OK");

            ll = new LinearLayout.LayoutParams((int)screenWidth/2,300);
            ll.gravity = Gravity.CENTER;
            ll.topMargin = 200;
            linearLayout.addView(circularProgressButton,ll);
            circularProgressButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CircularProgressButton btn = (CircularProgressButton) v;
                    int progress = btn.getProgress();

                    if (progress == 0) { // 初始时progress = 0
                        btn.setProgress(50); // 点击后开始不精准进度，不精准进度的进度值一直为50
                    } else if (progress == 100) { // 如果当前进度为100，即完成状态，那么重新回到未完成的状态
                        btn.setProgress(0);
                    } else if (progress == 50) { // 如果当前进度为50，那么点击后就显示完成的状态
                        btn.setProgress(100); // -1表示出错，显示出错的图片和背景，100表示完成，显示完成的图片和背景
                    }
                }
            });

        }else if(index == 2){

        }else  if(index == 3){

        }

        return linearLayout;
    }

    // Make sure that this method returns the SAME View.  It should NOT create a new View each time
    // that it is invoked.
    @NonNull
    @Override
    public View getView() {
        return mWholeScreen;
    }

    @Override
    public boolean isFullscreen() {
        return true;
    }

    @Override
    public void onShown() {
        // No-op.
    }

    @Override
    public void onHidden() {
        // No-op.
    }
}
