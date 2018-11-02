package com.lyc.live.mac;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lyc.live.common.base.BaseActivity;
import com.lyc.live.livelove.R;
import com.lyc.live.mac.netunit.MacNetUnit;
import com.lyc.live.utils.MLog;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

public class MacActivity extends BaseActivity {

    private TextView tv_ip_wlan_online, tv_spy_wlan_online, tv_current_wlan_online,
            tv_ip_eth_online, tv_spy_eth_online,
            tv_current_eth_online, tv_wifimanager_mac_online;
    private TextView tv_ip_wlan_offline, tv_spy_wlan_offline, tv_current_wlan_offline,
            tv_ip_eth_offline, tv_spy_eth_offline,
            tv_current_eth_offline, tv_wifimanager_mac_offline;

    private TextView tv_brand,tv_android_sdk,tv_android_mac;

    private CheckBox cb_current_net;
    private Button btn_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.live__activity_mac);
        initView();
        initData();
        SharedPreferences sp =  PreferenceManager.getDefaultSharedPreferences(mContext);
        int upload = sp.getInt("upload_2",0);
        String data = packData();
        if (upload == 0){
            new MacNetUnit(mActivity).sendData(data);
        }else {
            MLog.e("已经上传了");
        }
    }

    public static String getMacAddress2(boolean isEth) {
        String str = "";
        String macSerial = "";
        try {
            Process pp = Runtime.getRuntime().exec(
                    isEth ? "cat /sys/class/net/eth0/address" : "cat /sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (Exception ex) {
            Log.e("----->" + "NetInfoManager", "getMacAddress:" + ex.toString());
        }
        return macSerial;
    }


    public String getLocalMacAddress(boolean isEth) {

        String wlan = null;
        if (isEth) {
            try {
                String path = "sys/class/net/eth0/address";
                FileInputStream fis_name = new FileInputStream(path);
                byte[] buffer_name = new byte[8192];
                int byteCount_name = fis_name.read(buffer_name);
                if (byteCount_name > 0) {
                    wlan = new String(buffer_name, 0, byteCount_name, "utf-8");
                }

                if (wlan == null) {
                    fis_name.close();
                    return "";
                }
                fis_name.close();
            } catch (Exception io) {
                MLog.e(io.getMessage());
            }
        } else {
            try {
                String path = "sys/class/net/wlan0/address";
                FileInputStream fis_name;
                try {
                    fis_name = new FileInputStream(path);
                    byte[] buffer_name = new byte[8192];
                    int byteCount_name = fis_name.read(buffer_name);
                    if (byteCount_name > 0) {
                        wlan = new String(buffer_name, 0, byteCount_name, "utf-8");
                    }

                    if (wlan == null) {
                        fis_name.close();
                        return "";
                    }
                    fis_name.close();

                } catch (Exception io) {
                    // TODO Auto-generated catch block
                    MLog.e(io.getMessage());
                }
            } catch (Exception e) {
                MLog.e(e.getMessage());
            }

        }
        return wlan;

    }

    /**
     * get Mac-Address
     * 参考文章
     * https://blog.csdn.net/u014134180/article/details/78125494?locationNum=7&fps=1
     * http://robinhenniges.com/en/android6-get-mac-address-programmatically
     * https://blog.csdn.net/andoop/article/details/54633077
     */
    public static String getMacAddress(Context context) {
        // 6.0 以下直接使用WifiManager获取（扫描端口方法在无网络条件有问题）
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo winfo = wifi.getConnectionInfo();
            if (isMacValid(winfo.getMacAddress())){
                return winfo.getMacAddress().toLowerCase();
            }
        }
        // 扫描端口方法获取
        List<NetworkInterface> all = null;
        try {
            all = Collections.list(NetworkInterface.getNetworkInterfaces());
        } catch (Exception e) {
            MLog.e(e.getMessage());
        }
        if (all != null) {
            try {
                for (NetworkInterface nif : all) {
                    if (!nif.getName().equalsIgnoreCase("wlan0")) {
                        continue;
                    }
                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return "";
                    }
                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        if (b >= 0 && b < 16) {
                            res1.append("0");
                        }
                        res1.append(Integer.toHexString(b & 0xFF) + ":");
                    }
                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    if (isMacValid(res1.toString())){
                        return res1.toString().toLowerCase();
                    }
                }
            } catch (Exception ex) {
                MLog.d("Exception", ex);
            }
        }
        long timeStart = System.currentTimeMillis();
        // 测试发现vivo（6.0）出现了一个通过扫描端口仍然不能获取，但是可以cat一个节点获取
        String[] commandArr = new String[]{"cat /sys/class/net/wlan0/address" ,
                "cat /sys/class/net/eth0/address"};
        InputStreamReader ir = null;
        for (int i = 0; i < commandArr.length; i++) {
            try {

                Process pp = Runtime.getRuntime().exec(commandArr[i]);
                ir = new InputStreamReader(pp.getInputStream());
                LineNumberReader input = new LineNumberReader(ir);
                String str = "";
                for (; null != str; ) {
                    str = input.readLine();
                    if (str != null) {
                        if (isMacValid(str.trim())) {
                            return str.trim().toLowerCase();
                        }
                        break;
                    }
                }
            } catch (Exception ex) {
                Log.e("----->" + "NetInfoManager", "getMacAddress:" + ex.toString());
            }finally {
                if (ir!=null){
                    try {
                        ir.close();
                    } catch (IOException e) {
                        MLog.d("Exception", e);
                    }
                }
            }
        }
        MLog.e("time:" + (System.currentTimeMillis() - timeStart));
        return "02:00:00:00:00:00";
    }


    private static boolean isMacValid(String macAddress) {
        if (!TextUtils.isEmpty(macAddress) && !"02:00:00:00:00:00".equalsIgnoreCase(macAddress)) {
            return true;
        } else {
            return false;
        }
    }



    private static InetAddress getLocalInetAddress(boolean isEth) {
        InetAddress ip = null;
        try {
            //列举
            Enumeration<NetworkInterface> en_netInterface = NetworkInterface.getNetworkInterfaces();
            while (en_netInterface.hasMoreElements()) {//是否还有元素
                NetworkInterface ni = (NetworkInterface) en_netInterface.nextElement();//得到下一个元素
                if (!ni.getName().equalsIgnoreCase(isEth ? "eth0" : "wlan0")) {
                    continue;
                }
                Enumeration<InetAddress> en_ip = ni.getInetAddresses();//得到一个ip地址的列举
                while (en_ip.hasMoreElements()) {
                    ip = en_ip.nextElement();
                    if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1)
                        break;
                    else
                        ip = null;
                }

                if (ip != null) {
                    break;
                }
            }
        } catch (SocketException e) {

            e.printStackTrace();
        }
        return ip;
    }


    /**
     * 根据IP地址获取MAC地址
     *
     * @return
     */
    public static String getMacAddress(boolean isEth) {
        String strMacAddr = null;
        try {
            //获得IpD地址
            InetAddress ip = getLocalInetAddress(isEth);
            byte[] b = NetworkInterface.getByInetAddress(ip).getHardwareAddress();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
                if (i != 0) {
                    buffer.append(':');
                }
                String str = Integer.toHexString(b[i] & 0xFF);
                buffer.append(str.length() == 1 ? 0 + str : str);
            }
            strMacAddr = buffer.toString().toUpperCase();
        } catch (Exception e) {

        }

        return strMacAddr;
    }

    public String getWifiMangerMacAddress() {
        WifiManager wifi = (WifiManager) mActivity.getSystemService(Context.WIFI_SERVICE);
        WifiInfo winfo = wifi.getConnectionInfo();
        String mac = winfo.getMacAddress();
        return mac;
    }

    private void initView() {
        tv_ip_wlan_online = findViewById(R.id.tv_ip_wlan_online);
        tv_spy_wlan_online = findViewById(R.id.tv_spy_wlan_online);
        tv_current_wlan_online = findViewById(R.id.tv_current_wlan_online);
        tv_ip_eth_online = findViewById(R.id.tv_ip_eth_online);
        tv_spy_eth_online = findViewById(R.id.tv_spy_eth_online);
        tv_current_eth_online = findViewById(R.id.tv_current_eth_online);
        tv_wifimanager_mac_online = findViewById(R.id.tv_wifimanager_mac_online);

        tv_ip_wlan_offline = findViewById(R.id.tv_ip_wlan_offline);
        tv_spy_wlan_offline = findViewById(R.id.tv_spy_wlan_offline);
        tv_current_wlan_offline = findViewById(R.id.tv_current_wlan_offline);
        tv_ip_eth_offline = findViewById(R.id.tv_ip_eth_offline);
        tv_spy_eth_offline = findViewById(R.id.tv_spy_eth_offline);
        tv_current_eth_offline = findViewById(R.id.tv_current_eth_offline);
        tv_wifimanager_mac_offline = findViewById(R.id.tv_wifimanager_mac_offline);

        cb_current_net = findViewById(R.id.cb_current_net);
        btn_refresh = findViewById(R.id.btn_refresh);

        tv_brand = findViewById(R.id.tv_brand);
        tv_android_sdk = findViewById(R.id.tv_android_sdk);

        tv_android_mac = findViewById(R.id.tv_android_mac);

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (true) {
                    Toast.makeText(mActivity, "有网络连接", Toast.LENGTH_SHORT).show();
                    cb_current_net.setChecked(true);
                    tv_current_wlan_online.setText(SystemUtils.getWlan0Address());
                    tv_current_eth_online.setText(SystemUtils.getEth0Address());

                    tv_spy_wlan_online.setText(getMacAddress2(false));
                    tv_spy_eth_online.setText(getMacAddress2(true));
                    tv_wifimanager_mac_online.setText(getWifiMangerMacAddress());

                    tv_ip_wlan_online.setText(getMacAddress(false));
                    tv_ip_eth_online.setText(getMacAddress(true));
                } else {
                    cb_current_net.setChecked(false);
                    Toast.makeText(mActivity, "无网络连接", Toast.LENGTH_SHORT).show();
                    tv_current_wlan_offline.setText(SystemUtils.getWlan0Address());
                    tv_current_eth_offline.setText(SystemUtils.getEth0Address());

                    tv_spy_wlan_offline.setText(getMacAddress2(false));
                    tv_spy_eth_offline.setText(getMacAddress2(true));
                    tv_wifimanager_mac_offline.setText(getWifiMangerMacAddress());

                    tv_ip_wlan_offline.setText(getMacAddress(false));
                    tv_ip_eth_offline.setText(getMacAddress(true));
                }
            }
        });

    }

    private void initData(){
        Toast.makeText(mActivity, "有网络连接", Toast.LENGTH_SHORT).show();
        cb_current_net.setChecked(true);
        tv_current_wlan_online.setText(SystemUtils.getWlan0Address());
        tv_current_eth_online.setText(SystemUtils.getEth0Address());

        tv_spy_wlan_online.setText(getMacAddress2(false));
        tv_spy_eth_online.setText(getMacAddress2(true));
        tv_wifimanager_mac_online.setText(getWifiMangerMacAddress());

        tv_ip_wlan_online.setText(getMacAddress(false));
        tv_ip_eth_online.setText(getMacAddress(true));

        tv_brand.setText(Build.BRAND.toLowerCase());
        tv_android_sdk.setText(String.valueOf(Build.VERSION.SDK_INT));
        tv_android_mac.setText(getMacAddress(mContext));
    }

    private String packData(){
        HashMap<String,String> data= new HashMap<>();
        data.put("version",String.valueOf(2));

        data.put("timestamp",String.valueOf(System.currentTimeMillis()));
        data.put("brand",tv_brand.getText().toString().toLowerCase());
        data.put("android_sdk",tv_android_sdk.getText().toString().toLowerCase());

        data.put("plan_1",tv_wifimanager_mac_online.getText().toString().toLowerCase());

        data.put("plan_2_wlan",tv_spy_wlan_online.getText().toString().toLowerCase());
        data.put("plan_2_eth",tv_spy_eth_online.getText().toString().toLowerCase());

        data.put("plan_3_wlan",tv_ip_wlan_online.getText().toString().toLowerCase());
        data.put("plan_3_eth",tv_ip_eth_online.getText().toString().toLowerCase());

        data.put("plan_4_wlan",tv_current_wlan_online.getText().toString().toLowerCase());
        data.put("plan_4_eth",tv_current_eth_online.getText().toString().toLowerCase());

        data.put("plan_final",tv_android_mac.getText().toString().toLowerCase());
        return new Gson().toJson(data);
    }

}
