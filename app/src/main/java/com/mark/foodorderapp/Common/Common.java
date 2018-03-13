package com.mark.foodorderapp.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.mark.foodorderapp.Model.User;

/**
 * Created by Mark on 23/02/2018.
 */

public class Common {
    public static User currentUser;

    public static  final String DELETE = "Delete";
    public static  final String USER_KEY = "User";
    public static  final String PWD_KEY = "Password";

    //check users connection to internet
    public static boolean checkConnection (Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager != null)
        {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if(info != null)
            {
                for(int i=0;i<info.length;i++)
                {
                    if(info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }
}
