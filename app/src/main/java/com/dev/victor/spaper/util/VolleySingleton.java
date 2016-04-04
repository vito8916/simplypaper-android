package com.dev.victor.spaper.util;

/**
 * Created by Victor on 18/09/2015.
 */
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.dev.victor.spaper.MainActivity;


public class VolleySingleton {
    private static VolleySingleton mInstance = null;
    private RequestQueue mRQ;
    private ImageLoader mIL;

    private VolleySingleton()
    {
        mRQ = Volley.newRequestQueue(MainActivity.getAppContext());
        mIL = new ImageLoader(this.mRQ,new ImageLoader.ImageCache(){
            private final LruCache<String , Bitmap> mCache = new LruCache<String, Bitmap>(10);
            public void putBitmap(String url, Bitmap bitmap)
            {
                mCache.put(url,bitmap);
            }
            public Bitmap getBitmap(String url)
            {
                return mCache.get(url);
            }
        });
    }

    public static VolleySingleton getInstance()
    {
        if(mInstance == null)
        {
            mInstance = new VolleySingleton();
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue()
    {
        return mRQ;
    }

    public ImageLoader getImageLoader()
    {
        return mIL;
    }

}
