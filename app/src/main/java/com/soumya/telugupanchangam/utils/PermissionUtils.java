package com.soumya.telugupanchangam.utils;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

public class PermissionUtils {
    @RequiresApi(api = Build.VERSION_CODES.S)
    public static boolean checkPermissions(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ((ContextCompat.checkSelfPermission(context,Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED));
        }
        else {
            return ((ContextCompat.checkSelfPermission(context,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                    (ContextCompat.checkSelfPermission(context,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED));
        }
    }

    public static void showCustomDialog(Context context, String title, String message,
                                        String positiveBtn, DialogInterface.OnClickListener positiveListener,
                                        String negativeBtn, DialogInterface.OnClickListener negativeListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(positiveBtn,positiveListener)
                .setNegativeButton(negativeBtn,negativeListener);
        builder.create().show();
    }
}
