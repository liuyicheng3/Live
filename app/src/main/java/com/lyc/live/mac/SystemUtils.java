package com.lyc.live.mac;

import com.lyc.live.utils.MLog;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class SystemUtils {

    /**
     * get Mac-Address
     * 之前 wifiInfo.getMacAddress() 的方式在 6.0 以上的手机会有问题。
     * 详见 http://robinhenniges.com/en/android6-get-mac-address-programmatically。
     */
    public static String getWlan0Address() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            if (all == null || all.size() == 0) {
                return "02:00:00:00:00:00";
            }
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
                    res1.append(Integer.toHexString(b & 0xFF)).append(":");
                }
                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            MLog.d("Exception", ex);
        }
        return "02:00:00:00:00:00";
    }
    public static String getEth0Address() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            if (all == null || all.size() == 0) {
                return "02:00:00:00:00:00";
            }
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("eth0")) {
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
                    res1.append(Integer.toHexString(b & 0xFF)).append(":");
                }
                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            MLog.d("Exception", ex);
        }
        return "02:00:00:00:00:00";
    }

}
