package com.example.administrator.beta;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class NetWorkUtil {

    /**
     * 获得网络连接是否可用
     * @param context
     * @return
     */

    public static boolean hasNetwork(Context context) {
        ConnectivityManager con = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo workinfo = con.getActiveNetworkInfo();
        if (workinfo == null || !workinfo.isAvailable()) {
            return false;
        }
        return true;
    }

    /**
     * 判断是否是wifi连接
     */

    public static boolean checkWifiState(Context context) {
        boolean isWifiConnect = true;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
        for (int i = 0; i < networkInfos.length; i++) {
            if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED) {
                if (networkInfos[i].getType() == cm.TYPE_MOBILE) {
                    isWifiConnect = false;
                }
                if (networkInfos[i].getType() == cm.TYPE_WIFI) {
                    isWifiConnect = true;
                }
            }
        }
        return isWifiConnect;
    }

    /**
     * 获取手机连接的网络类型状态 WIFI/2G/3G/4G
     */

    public static int getNetWorkStatus(Context context) {

        int netWorkType = NetWorkStatusConstants.NETWORK_CLASS_UNKNOWN;

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            int type = networkInfo.getType();
            if (type == ConnectivityManager.TYPE_WIFI) {
                netWorkType = NetWorkStatusConstants.NETWORK_WIFI;
            } else if (type == ConnectivityManager.TYPE_MOBILE) {
                netWorkType = getNetWorkClass(context);
            }
        }
        return netWorkType;
    }

    /**
     * 获取手机连接的网络类型是2G/3G/4G
     */

    public static int getNetWorkClass(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        switch (telephonyManager.getNetworkType()) {

            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return NetWorkStatusConstants.NETWORK_CLASS_2_G;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return NetWorkStatusConstants.NETWORK_CLASS_3_G;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return NetWorkStatusConstants.NETWORK_CLASS_4_G;
            default:
                return NetWorkStatusConstants.NETWORK_CLASS_UNKNOWN;
        }
    }
    public class NetWorkStatusConstants {
        /**
         * Unknown network class
         */
        public static final int NETWORK_CLASS_UNKNOWN = 0;
        /**
         * wifi networks
         */
        public static final int NETWORK_WIFI          = 1;
        /**
         * "2G" networks
         */
        public static final int NETWORK_CLASS_2_G     = 2;
        /**
         * "3G" networks
         */
        public static final int NETWORK_CLASS_3_G     = 3;
        /**
         * "4G" networks
         */
        public static final int NETWORK_CLASS_4_G     = 4;
    }
}
