package com.example.administrator.beta;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import in.shadowfax.proswipebutton.ProSwipeButton;

public class httpPost {
    /**
     * 往服务器上上传文本  比如log日志
     * @param urlstr        请求的url
     * @param uploadFile    log日志的路径
     *                                    /mnt/shell/emulated/0/LOG/LOG.log
     * @param newName        log日志的名字 LOG.log
     * @return
     */
    public static boolean httpPost(Activity activity, String urlstr, String uploadFile, String newName){
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";//边界标识
        int TIME_OUT = 10*1000;   //超时时间
        HttpURLConnection con = null;
        DataOutputStream ds = null;
        InputStream is = null;
        try {
            URL url = new URL(urlstr);
            con = (HttpURLConnection) url.openConnection();
            con.setReadTimeout(TIME_OUT);
            con.setConnectTimeout(TIME_OUT);
            /* 允许Input、Output，不使用Cache */
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);

            // 设置http连接属性
            con.setRequestMethod("POST");//请求方式
            con.setRequestProperty("Connection", "Keep-Alive");//在一次TCP连接中可以持续发送多份数据而不会断开连接
            con.setRequestProperty("Charset", "UTF-8");//设置编码
            con.setRequestProperty("Content-Type",//multipart/form-data能上传文件的编码格式
                    "multipart/form-data;boundary=" + boundary);

            ds = new DataOutputStream(con.getOutputStream());
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; "
                    + "name=\"csv\";filename=\"" + newName + "\"" + end);
            ds.writeBytes(end);

            // 取得文件的FileInputStream
            FileInputStream fStream = new FileInputStream(uploadFile);
            /* 设置每次写入1024bytes */
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = -1;
            /* 从文件读取数据至缓冲区 */
            while ((length = fStream.read(buffer)) != -1) {
                /* 将资料写入DataOutputStream中 */
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);//结束

            fStream.close();
            ds.flush();
            /* 取得Response内容 */
            is = con.getInputStream();
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            /* 将Response显示于Dialog */
            if(b.toString().trim().equals("success")){
                showDialog(activity,true,uploadFile,"上传成功");
                return true;
            }else if(b.toString().trim().equals("WrongType")){
                showDialog(activity,false,uploadFile,"仅限上传CSV文件" );
                return false;
            }else if(b.toString().trim().equals("oversize")){
                showDialog(activity,false,uploadFile,"上传文件不能大于10M");
                return false;
            }else{
                showDialog(activity,false,uploadFile,"无效操作" );
                return false;
            }
        } catch (Exception e) {
            showDialog(activity,false,uploadFile,"上传失败\n" + e);
            return false;
        }finally {
            /* 关闭DataOutputStream */
            if(ds!=null){
                try {
                    ds.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                con.disconnect();
            }
        }
    }
    /* 显示Dialog的method */
    private static void showDialog(final Activity activity, final Boolean isSuccess, final String uploadFile, final String mess) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(activity).setTitle("通知")
                        .setMessage(mess)
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
            }
        });

    }
}
