package com.example.administrator.beta;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DBu {
    private static  Connection getConnection(String DBName){
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String TargetIp = "47.95.238.162";
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + TargetIp + ":3306/" + DBName,
                    "root",
                    "123456");
        }catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return connection;
    }
    public static String QuarySql(String UID,String PSW){
        String DBName = "temp";
        String field = null;
        Connection connection = null ;
        try{
            connection = getConnection(DBName);
            Statement statement = connection.createStatement();
            Statement statement1 = connection.createStatement();
            String sql = "select password from usr_inf where id='"+UID+"'";
            ResultSet resultSet = statement.executeQuery(sql);
            String sql1 = "select password from usr_inf where email='"+UID+"'";
            ResultSet resultSet1 = statement1.executeQuery(sql1);
            if(!(resultSet.isBeforeFirst()||resultSet1.isBeforeFirst()))
            {
                return "Error!";
            }
            else {
                    if(resultSet1.isBeforeFirst()){
                        resultSet1.next();
                        field = resultSet1.getString(1);
                    }else if(resultSet.isBeforeFirst()){
                        resultSet.next();
                        field = resultSet.getString(1);
                    }
                    else
                    {
                        connection.close();
                        statement.close();
                        statement1.close();
                        resultSet.close();
                        resultSet1.close();
                        return "Error11";
                    }
                    if(!field.isEmpty())
                    {
                        String comfirm = MD5Utils.encrypt(PSW);
                        if (field.equals(comfirm)) {
                            connection.close();
                            statement.close();
                            statement1.close();
                            resultSet.close();
                            resultSet1.close();
                            return "OK";
                        }
                        else
                        {
                            connection.close();
                            statement.close();
                            statement1.close();
                            resultSet.close();
                            resultSet1.close();
                            return "Nok1";//"Nok1"; //密码错误
                        }
                    }else
                    {
                        connection.close();
                        statement.close();
                        statement1.close();
                        resultSet.close();
                        resultSet1.close();
                        return "Nok2"; //用户不存在
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();//用户不存在
        }

    }
    public  Bundle Updata(String UID, String PSW){
        String DBName = "temp";
        String field = null;
        Bundle bundle = new Bundle();
        Connection connection = null ;
        try{
            connection = getConnection(DBName);
            String sql = "select count(time) from "+UID;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if(resultSet==null)
            {
                bundle.putString("error","No result");
                return bundle;
            }
            else {
                int count1 = 0;
                resultSet.next();
                count1 = Integer.valueOf(resultSet.getString(1));
                if(count1>0){
                    String sql1 = "select * from"+" "+UID+" "+"limit "+count1;
                    resultSet = statement.executeQuery(sql1);
                    if(resultSet==null){
                        bundle.putString("ACK","0");
                        return bundle;
                    }
                    else{
                        while(resultSet.next()){
                            bundle.putString("Time",resultSet.getString(1));
                            bundle.putString("total",resultSet.getString(2));
                            bundle.putString("Humidity",resultSet.getString(3));
                            bundle.putString("Temperature",resultSet.getString(4));
                            bundle.putString("dust",resultSet.getString(5));
                            bundle.putString("co2",resultSet.getString(6));
                        }
                        connection.close();
                        statement.close();
                        resultSet.close();
                        bundle.putString("ACK","OK");
                        return bundle;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("");
            System.out.println(e);
            bundle.putString("error","failed!");
            return bundle;
        }
        bundle.putString("ACK","0");
        return bundle;
    }
    public ArrayList<Usr_Data> getChartData(String UID,String PSW){
        String DBName = "temp";
        Connection connection = null ;
        Usr_Data ud;
        ArrayList<Usr_Data> usr_data = new ArrayList<Usr_Data>();
        try
        {
            connection = getConnection(DBName);
            Statement statement = connection.createStatement();
            String sql = "select count(time) from "+UID;
            ResultSet resultSet = statement.executeQuery(sql);
            if(resultSet==null){
                return null;
            }
            else {
                int count1 = 0;
                resultSet.next();
                count1 = Integer.valueOf(resultSet.getString(1));
                if(count1>0){
                    String sql1 = "select * from"+" "+UID+" "+"limit "+(count1-50)+","+count1;
                    resultSet = statement.executeQuery(sql1);
                    if (resultSet == null){
                        return null;
                    }
                    else {
                        while (resultSet.next()){
                            float time = Float.valueOf(resultSet.getString(1));
                            float total = Float.valueOf(resultSet.getString(2));
                            float hum = Float.valueOf(resultSet.getString(3));
                            float temp = Float.valueOf(resultSet.getString(4));
                            float dust = Float.valueOf(resultSet.getString(5));
                            float co2 = Float.valueOf(resultSet.getString(6));
                            ud = new Usr_Data(temp,hum,co2,dust,time,total);
                            usr_data.add(ud);
                        }
                        connection.close();
                        statement.close();
                        resultSet.close();
                        return usr_data;
                        }
                }
                else{
                    return null;
                }

            }

        }catch (Exception e){
            e.printStackTrace();

            return null;
        }
    }
    public float[] getPieChartDate(){
        float share[] = new float[9];
        String DBName = "temp";
        Connection connection = null ;
        try{
            connection = getConnection(DBName);
            String sql = "select count(s1) from out_time";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            if(resultSet==null){
                return null;
            } else {
                int count1 = 0;
                resultSet.next();
                count1 = Integer.valueOf(resultSet.getString(1));
                if(count1>0) {
                    String sql1 = "select * from out_time limit" + " " + (count1 - 1) + "," + count1;
                    resultSet = statement.executeQuery(sql1);
                    if (resultSet == null) {
                        return null;
                    } else {
                        resultSet.next();
                        for (int i = 0; i < 9; i++) {
                            share[i] = Float.valueOf(resultSet.getString(i + 1));
                        }
                        connection.close();
                        statement.close();
                        resultSet.close();
                        return share;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
