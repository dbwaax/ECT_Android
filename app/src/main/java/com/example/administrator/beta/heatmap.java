package com.example.administrator.beta;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class heatmap extends AppCompatActivity {
    private int NetStatusUtil = 0;
    private LinearLayout linearLayout = null;
    private  WebSettings webSettings = null;
    private WebView mwebview = null;
    private ProgressBar progressBar = null;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent = new Intent(heatmap.this, SecondActivity.class);
        heatmap.this.startActivity(intent);
        finish();
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heatmap);
        linearLayout = (LinearLayout)findViewById(R.id.llayout3);

        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        progressBar = (ProgressBar)findViewById(R.id.progressbar2);
        mwebview = new WebView(this){                                            //新建WebView实例并禁止屏幕拖拽！！！！！！！
            @Override
            public void scrollTo(int x, int y) {
                super.scrollTo(0, 0);
            }
            @Override
            public boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY,
                                        int scrollRangeX, int scrollRangeY, int maxOverScrollX,
                                        int maxOverScrollY, boolean isTouchEvent) {
                return false;
            }
        };

        linearLayout.addView(mwebview,params);
        webSettings = mwebview.getSettings();                          //新建WebView设置
        webSettings.setJavaScriptEnabled(true);                                   //使能JS
        //webSettings.setPluginsEnabled(true)                                     支持插件
        webSettings.setUseWideViewPort(false);                                    //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true);                               //缩放至屏幕大小
        webSettings.setSupportZoom(true);                                        //支持缩放
        webSettings.setBuiltInZoomControls(false);                                //内置缩放控件
        webSettings.setDisplayZoomControls(false);                               //隐藏原生的缩放控件
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);            //关闭webview中缓存
        webSettings.setAllowFileAccess(true);                                     //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);               //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true);                            //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");                          //设置编码格式
        NetStatusUtil = NetWorkUtil.getNetWorkStatus(this);

        if (NetStatusUtil== NetWorkUtil.NetWorkStatusConstants.NETWORK_WIFI||NetStatusUtil== NetWorkUtil.NetWorkStatusConstants.NETWORK_CLASS_4_G) {
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//根据cache-control决定是否从网络上取数据。
        } else {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//没网，则从本地获取，即离线加载
        }
        //缓存模式如下：
        //LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        //LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
        //LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        //LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
        String APP_CACAHE_DIRNAME = "cache";
        webSettings.setDomStorageEnabled(true); // 开启 DOM storage API 功能
        webSettings.setDatabaseEnabled(true);   //开启 database storage API 功能
        webSettings.setAppCacheEnabled(true);//开启 Application Caches 功能
        String cacheDirPath = getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME;
        webSettings.setAppCachePath(cacheDirPath); //设置  Application Caches 缓存目录

        mwebview.loadUrl("http://zyhdbw.cn/RES/heatmap.html");//"http://zyhdbw.cn/RES/password_find_android.html");
        //步骤3. 复写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器， 而是在本WebView中显示
        mwebview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //Toast.makeText(registerActivity.this,"开始加载页面！",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Toast.makeText(heatmap.this,"页面加载完成！",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(view.GONE);
            }
        });
        //setWebChromeClient辅助 WebView 处理 Javascript 的对话框,网站图标,网站标题等等
        mwebview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress<90){
                    progressBar.setProgress(newProgress);
                    if(newProgress==88){
                        progressBar.setProgress(99);
                    }
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
            }

            //作用：支持javascript的确认框
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                new AlertDialog.Builder(heatmap.this)
                        .setTitle("zyhdbw.cn")
                        .setMessage(message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.cancel();
                            }
                        })
                        .setCancelable(false)
                        .show();
                return true;
                //return super.onJsAlert(view, url, message, result);
            }
            //作用：支持javascript输入框
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
                final EditText et = new EditText(heatmap.this);
                et.setText(defaultValue);
                new AlertDialog.Builder(heatmap.this)
                        .setTitle(message)
                        .setView(et)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm(et.getText().toString());
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.cancel();
                            }
                        })
                        .setCancelable(false)
                        .show();

                return true;
            }
        });

    }
}
