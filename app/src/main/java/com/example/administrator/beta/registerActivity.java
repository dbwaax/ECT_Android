package com.example.administrator.beta;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.mysql.fabric.xmlrpc.base.Param;

import static android.view.View.OVER_SCROLL_NEVER;

public class registerActivity extends AppCompatActivity {

    private int NetStatusUtil = 0;
    private LinearLayout linearLayout = null;
    private  WebSettings webSettings = null;
    private WebView mwebview = null;
    private ProgressBar progressBar = null;
    @Override
    protected void onDestroy() {
        if(mwebview!=null){
            mwebview.loadDataWithBaseURL(null,"","text/html","utf-8",null);
            mwebview.clearHistory();
            ((ViewGroup)mwebview.getParent()).removeView(mwebview);
            mwebview.destroy();
            mwebview = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode == KeyEvent.KEYCODE_BACK && mwebview.canGoBack()){
//            mwebview.canGoBack();
//            return true;
//        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        linearLayout = (LinearLayout)findViewById(R.id.llayout1);

        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //WebView webView = (WebView)findViewById(R.id.webView1);
        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        mwebview = new WebView(this){                                            //??????WebView????????????????????????????????????????????????
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
        webSettings = mwebview.getSettings();                          //??????WebView??????
        webSettings.setJavaScriptEnabled(true);                                   //??????JS
        //webSettings.setPluginsEnabled(true)                                     ????????????
        webSettings.setUseWideViewPort(true);                                    //????????????????????????webview?????????
        webSettings.setLoadWithOverviewMode(true);                               //?????????????????????
        webSettings.setSupportZoom(false);                                        //????????????
        webSettings.setBuiltInZoomControls(false);                                //??????????????????
        webSettings.setDisplayZoomControls(false);                               //???????????????????????????
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);            //??????webview?????????
        webSettings.setAllowFileAccess(true);                                     //????????????????????????
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);               //????????????JS???????????????
        webSettings.setLoadsImagesAutomatically(true);                            //????????????????????????
        webSettings.setDefaultTextEncodingName("utf-8");                          //??????????????????
        NetStatusUtil = NetWorkUtil.getNetWorkStatus(this);

        if (NetStatusUtil== NetWorkUtil.NetWorkStatusConstants.NETWORK_WIFI||NetStatusUtil== NetWorkUtil.NetWorkStatusConstants.NETWORK_CLASS_4_G) {
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//??????cache-control????????????????????????????????????
        } else {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//?????????????????????????????????????????????
        }
        //?????????????????????
        //LOAD_CACHE_ONLY: ?????????????????????????????????????????????
        //LOAD_DEFAULT: ??????????????????cache-control????????????????????????????????????
        //LOAD_NO_CACHE: ??????????????????????????????????????????.
        //LOAD_CACHE_ELSE_NETWORK????????????????????????????????????????????????no-cache?????????????????????????????????
        String APP_CACAHE_DIRNAME = "cache";
        webSettings.setDomStorageEnabled(true); // ?????? DOM storage API ??????
        webSettings.setDatabaseEnabled(true);   //?????? database storage API ??????
        webSettings.setAppCacheEnabled(true);//?????? Application Caches ??????
        String cacheDirPath = getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME;
        webSettings.setAppCachePath(cacheDirPath); //??????  Application Caches ????????????

        mwebview.loadUrl("http://zyhdbw.cn/RES/register_android.html");//"http://zyhdbw.cn/RES/password_find_android.html");
        //??????3. ??????shouldOverrideUrlLoading()????????????????????????????????????????????????????????? ????????????WebView?????????
        mwebview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //Toast.makeText(registerActivity.this,"?????????????????????",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Toast.makeText(registerActivity.this,"?????????????????????",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(view.GONE);
            }
        });
        //setWebChromeClient?????? WebView ?????? Javascript ????????????,????????????,??????????????????
        mwebview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress<90){
                    progressBar.setProgress(newProgress);
                    if(newProgress==88){
                        progressBar.setProgress(99);
                    }
//                    String temp = newProgress + "%";
//                    text_Loading.setText(temp);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
               // web_title.setText(title);
            }

            //???????????????javascript????????????
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                new AlertDialog.Builder(registerActivity.this)
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
            //???????????????javascript?????????
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
                final EditText et = new EditText(registerActivity.this);
                et.setText(defaultValue);
                new AlertDialog.Builder(registerActivity.this)
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
