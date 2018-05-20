package tv.hooq.sampleapp.commons;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import tv.hooq.sampleapp.SampleApplication;

public class Utils {

    public static String getDeviceManufacture() {
        String manufacturer = Build.MANUFACTURER;
        return manufacturer;
    }

    public static String getDeviceModel() {
        String model = Build.MODEL;
        if (model.startsWith(getDeviceManufacture())) {
            return model.replace(getDeviceManufacture(), "");
        }
        return model;
    }

    public static String getDeviceName() {
        return getDeviceManufacture() + " " + getDeviceModel();
    }

    public static String getDeviceId() {
        String androidId = Settings.Secure.getString(SampleApplication.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }

    public static String getIpAddress() {
        return "0.0.0.0";
    }

    public static String getCountryCode() {
        return "XX";
    }

    public static String readFile(String filename) {
        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard, filename);

        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString();
    }

    public static String getConnectionTypeString(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null || !info.isConnected())
            return "-"; //not connected
        if (info.getType() == ConnectivityManager.TYPE_WIFI)
            return "WIFI";
        if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int networkType = info.getSubtype();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                    return "2G";
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                    return "3G";
                case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                    return "4G";
                default:
                    return "?";
            }
        }
        return "?";
    }

    public static String getConvivaDebugKey() {
        return readFile("convivaDebugKey.txt");
    }

    public static String getConvivaDebugGatewayUrl() {
        return readFile("convivaDebugGatewayUrl.txt");
    }
}
