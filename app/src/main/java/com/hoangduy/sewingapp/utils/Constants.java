package com.hoangduy.sewingapp.utils;

import com.hoangduy.sewingapp.dto.Customer;

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

    public static String getLastWordOfName(Customer customer) {
        String customerName = customer.getName();
        int space_letter = customerName.lastIndexOf(" ");
        if (space_letter > 0)
            return String.valueOf(customerName.substring(customerName.lastIndexOf(" ") + 1).charAt(0)).toUpperCase();
        else
            return String.valueOf(customerName.charAt(0)).toUpperCase();

    }

    public enum MODE {
        CREATE,
        EDIT
    }
}
