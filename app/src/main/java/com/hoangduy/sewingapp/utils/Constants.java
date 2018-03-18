package com.hoangduy.sewingapp.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.hoangduy.sewingapp.dto.Customer;

import java.util.Random;

/**
 * Created by hoangduy on 2/13/18.
 */

public class Constants {
    public static String customerDetailsTemplate = "" +
            "Hạ eo :    \n" +
            "Dài :      \n" +
            "Nách :     \n" +
            "Ngực :     \n" +
            "Eo :       \n" +
            "Cổ :       \n" +
            "Ly :       \n" +
            "Tay :      \n" +
            "Bt :       \n" +
            "Kt :       \n" +
            "Quần :     \n" +
            "Lưng :     \n" +
            "Mông :     \n" +
            "Đáy :      \n" +
            "Ống :      \n" +
            "...        \n";

    @NonNull
    public static String getLastWordOfName(Customer customer) {
        String customerName = customer.getName().trim();
        return String.valueOf(customerName.charAt(0)).toUpperCase();

    }

    @NonNull
    public static String normalizeNumber(int num) {
        if (num < 10)
            return "0" + num;
        else if (num == 0)
            return "12";
        else
            return String.valueOf(num);
    }

    @NonNull
    public static String normalizeDate(String date) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(date.substring(6));
        stringBuilder.append("/");
        stringBuilder.append(date.substring(4, 6));
        stringBuilder.append("/");
        stringBuilder.append(date.substring(0, 4));

        return stringBuilder.toString();
    }

    public static int randomColor() {
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        return color;
    }

    public static boolean checkPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public enum MODE {
        CREATE,
        EDIT
    }
}
