package com.dev.cda.WindAround;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by clementdaviller on 05/09/15.
 */
public class ImageResizer {
    static public Drawable resize(Drawable image, int width, int height, Resources res) {
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, width, height, false);
        return new BitmapDrawable(res, bitmapResized);
    }
}