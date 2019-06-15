package com.mobiweb.ibrahim.agenda.utils;

//import android.accounts.AccountManager;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

public class TelephonyHelper {


    //Variables
    //private static TelephonyManager telephonyManager;
    //private static AccountManager accountManager;
    //private static String networkOperator;
    //private static PackageInfo pInfo;

    //Constructor
    /*public TelephonyHelper(Context context) {
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //accountManager = AccountManager.get(context);
        networkOperator = telephonyManager.getNetworkOperator();
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }*/

    public TelephonyHelper() {
    }

    //mobile country code //415
    public String getMCC(Context context) {

        //return "415"; // Test
        //return "415"; // Default
        //return "420"; // Zain
        //return "426"; // Bahrain
        //return "420"; // Mobily
        //return "432"; // Iran MTN, MNC


        String mcc = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String networkOperator = telephonyManager.getNetworkOperator();
            if (networkOperator != null && !networkOperator.equals("")) {
                mcc = networkOperator.substring(0, 3);
                Log.d("mcc", "mcc : " + mcc);
            }
        } catch (Exception ex) {
            Log.d("getMNC", "networkOperator.substring(0, 3)");
        }
        return mcc; //telephonyManager.getNetworkOperator().substring(0, 3);

    }

    // mobile network code //01
    public String getMNC(Context context) {

        //return "03"; // Test
        //return "01"; // Default
        //return "04"; // Zain
        //return "02"; // Bahrain
        //return "03"; // Mobily
        //return "11"; // Iran MCI
        //return "35"; // Iran MTN

        String mnc = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String networkOperator = telephonyManager.getNetworkOperator();
            if (networkOperator != null && !networkOperator.equals("")) {
                mnc = networkOperator.substring(3);
                Log.d("mnc", "mnc : " + mnc);
            }
        } catch (Exception ex) {
            Log.d("getMNC", "networkOperator.substring(3)");
        }
        return mnc; //telephonyManager.getNetworkOperator().substring(3);

    }

    //get DeviceID
    public static String getDeviceID(Context context) {


        //355167052072061
        /* String deviceID = android.os.Build.SERIAL;
        String deviceID = telephonyManager.getDeviceId();*/
   /*     try {
        /*String deviceID = android.os.Build.SERIAL;
        String deviceID = telephonyManager.getDeviceId();*/
   /*         try {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                return telephonyManager.getDeviceId();
            } catch (Exception ex) {
                return "";
            }

        } catch (Exception ex) {
            Log.d("getDeviceID", "telephonyManager.getDeviceId()");
            return "";
        }

  */
         String android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
                return android_id;

    }

    //get DeviceType
    public String getDeviceModel() {
        return android.os.Build.MODEL.replace(" ", ""); //e.g device Model =  Samsung GT-I9100 -> GT-I9100
    }


    public String getPhoneName() {
        try {
            BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
            return myDevice.getName();// Device Name in phone
        } catch (Exception ex) {
            return null;
        }
    }

    // get Device Name
    public String getDeviceName() {
        String name = getPhoneName();
        if (name != null && !name.equals("")) {
            return name;
        } else {
            try {
                return android.os.Build.MANUFACTURER; // e.g Device name =  Samsung
            } catch (Exception e) {
                return "";
            }
        }
    }

    // get Device Version
    public String getDeviceVersion() {
        return android.os.Build.VERSION.RELEASE; // e.g. myVersion := "1.6"
    }

    /* // get SDK Version
    public int getSDKVersion() {
        return android.os.Build.VERSION.SDK_INT; // e.g. sdkVersion := 8;
    }*/


    /*public String getVersionName(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if (pInfo != null) {
                return pInfo.versionName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "";
    }*/

   /* public int getVersionCode(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if (pInfo != null) {
                return pInfo.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }*/

    /*
    //get System
    public static String getSystem() {
        return String.valueOf(android.os.Build.VERSION_CODES.BASE).replace(" ", "");
    }


    //get CountryCode
    public static String getCountryCode() {
        return telephonyManager.getSimCountryIso();
    }

    //get UserEmail
    public static String getEmail() {
        String email = null;
        Account account[] = accountManager.getAccounts();

        if (account.length > 0) {
            try {
                email = account[0].name;
                email = email.replace(" ", "");
            } catch (Exception e) {
                email = "null";
            }
        }

        return email;
    }*/
}
