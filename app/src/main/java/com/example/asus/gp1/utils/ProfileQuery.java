package com.example.asus.gp1.utils;

import android.provider.ContactsContract;

/**
 * Created by Qzhu on 2018/3/29.
 */

public class ProfileQuery {
    public static String[] PROJECTION = {
            ContactsContract.CommonDataKinds.Email.ADDRESS,
            ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
    };

    public static int ADDRESS = 0;
    public static int IS_PRIMARY = 1;
}
