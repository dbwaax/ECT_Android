package com.example.administrator.beta;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.*;

public class DataStorage {

    private String folder_name = null;
    private String value = null;
    private String file_name = null;
    private Context context = null;
    private String file_path = null;
    public DataStorage(Context context,String folder_name,String file_name,String value){
        this.context = context;
        this.folder_name = folder_name;
        this.file_name = file_name+".txt";
        this.value = value;
    }
    private static boolean Check_SD_Status(){
        return  Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
    private static String get_SD_Path(){
        if(Check_SD_Status()){
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }else{
            return null;
        }
    }
    private boolean Create_Files(){
        File folder = new File(this.context.getFilesDir(),this.folder_name);
        File file = new File(folder.getPath()+"/"+this.file_name);
        try{
            if(!folder.exists()){
                folder.mkdir();
            }
            if(!file.exists()){
                file.createNewFile();
                file_path = file.getPath();
                return true;
            }
            file_path = file.getPath();
            return true;

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public boolean Write_File(){
            if(Create_Files()){
                try
                {
                    File file = new File(file_path);
                    final FileOutputStream outStream = new FileOutputStream(file);
                    outStream.write(this.value.getBytes());
                    outStream.close();
                    return true;
                }catch (Exception e){
                    e.printStackTrace();
                }
        }
        return false;
    }
    public String Read_File(){
        BufferedReader br = null;
        String temp = null;
        if(Create_Files()){
            try{
                br = new BufferedReader(new FileReader(file_path));
                temp = br.readLine();
                if(temp==null){
                    return "Empty";
                }else{
                    String res[] = new String[2];
                    String res1 = null;
                    if((res1 = br.readLine())==null){
                        return temp;
                    }else{
                        res[0] = temp;
                        res[1] = res1;
                        return res[0]+"\n"+res[1];
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                return  "Empty";
            }
        }
        return  "Empty";
    }

}
