package com.shield.resident.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Commons {


    public static String getFolderDirectory(){
        final String dir = android.os.Environment.getExternalStorageDirectory() + "/Shield";
        return dir;
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    public static void showKeyBoard(Activity mActivity) {
        try {
            View view = mActivity.getCurrentFocus();
            if (view != null) {
                InputMethodManager keyboard = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInputFromInputMethod(view.getWindowToken(), InputMethodManager.SHOW_FORCED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
