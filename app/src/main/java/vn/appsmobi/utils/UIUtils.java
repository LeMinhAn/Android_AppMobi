/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package vn.appsmobi.utils;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import it.sephiroth.android.library.widget.HListView;
import vn.appsmobi.R;


/**
 * An assortment of UI helpers.
 */
public class UIUtils {
    private static final int[] RES_IDS_ACTION_BAR_SIZE = {R.attr.actionBarSize};

    /**
     * Calculates the Action Bar height in pixels.
     */
    public static int calculateActionBarSize(Context context) {
        if (context == null) {
            return 0;
        }

        Resources.Theme curTheme = context.getTheme();
        if (curTheme == null) {
            return 0;
        }

        TypedArray att = curTheme.obtainStyledAttributes(RES_IDS_ACTION_BAR_SIZE);
        if (att == null) {
            return 0;
        }

        float size = att.getDimension(0, 0);
        att.recycle();
        return (int) size;
    }

    public static void setAccessibilityIgnore(View view) {
        view.setClickable(false);
        view.setFocusable(false);
        view.setContentDescription("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
        }
    }

    /**
     * a view called action button when It call an asyntask in OnClick function.
     * So that: after click button, should disable onclick of the button in order to prevent user click
     * Solution: first: disable clickable, then after 500miliseconds enable this
     * GNAM
     */
    public static void tmpDisableClickOnView(final View v) {
        v.setClickable(false);
        Runnable restoreClickable = new Runnable() {
            @Override
            public void run() {
                v.setClickable(true);
            }
        };
        Handler process = new Handler();
        process.postDelayed(restoreClickable, 500);
    }


    public static void setBackground(View v, Drawable bg) {
        if (Build.VERSION.SDK_INT >= 16) {
            v.setBackground(bg);

        } else {

            v.setBackgroundDrawable(bg);
        }
    }

    //Height
    public static class HeightAnimation extends Animation {
        protected final int originalHeight;
        protected final View view;
        protected float perValue;

        public HeightAnimation(View view, int fromHeight, int toHeight) {
            this.view = view;
            this.originalHeight = fromHeight;
            this.perValue = (toHeight - fromHeight);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            view.getLayoutParams().height = (int) (originalHeight + perValue * interpolatedTime);
            view.requestLayout();
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }

    public static int getScreenHeight(Context mContext) {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        return height;
    }

    public static int getScreenWidth(Context mContext) {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width;
    }

    public static int getScreenWidthDB(Context mContext) {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        Resources resources = mContext.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = width / (metrics.densityDpi / 160f);
        return (int) dp;
    }

    public static void setImageViewWithScale(ImageView mImageView, int width, int height, String url) {
        ImageSize targetSize = new ImageSize(width, height); // result Bitmap will be fit to this size
        Bitmap bmp = ImageLoader.getInstance().loadImageSync(url, targetSize, Constants.options);
        mImageView.setImageBitmap(bmp);

    }

    public static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
        @Override
        public void onLoadingStarted(String imageUri, View view) {
            // spinner.setVisibility(View.VISIBLE);
        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            String message = null;
            switch (failReason.getType()) {
                case IO_ERROR:
                    message = "Input/Output error";
                    break;
                case DECODING_ERROR:
                    message = "Image can't be decoded";
                    break;
                case NETWORK_DENIED:
                    message = "Downloads are denied";
                    break;
                case OUT_OF_MEMORY:
                    message = "Out Of Memory error";
                    break;
                case UNKNOWN:
                    message = "Unknown error";
                    break;
            }
            Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();

            //spinner.setVisibility(View.GONE);
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            //spinner.setVisibility(View.GONE);
        }
    }

    public static Drawable scaleImageOf(int id, int width, int height, Context context) {
        Drawable image = context.getResources().getDrawable(id);
        if ((image == null) || !(image instanceof BitmapDrawable)) {
            return image;
        }

        Bitmap b = ((BitmapDrawable) image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, width, height, false);
        image = new BitmapDrawable(context.getResources(), bitmapResized);
        return image;
    }

    public static void setViewSize(View view, int width, int height) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        view.setLayoutParams(layoutParams);
    }

    public static void setViewSize(int width, int height, View view) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
        view.setLayoutParams(layoutParams);
    }

    //Hàm set số lượt rate để hiển thị số ngôi sao lên app
    public static void setRatingSize(View view, Context context, float scale) {
        int width = (int) (getScreenWidth(context) / scale);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        view.setLayoutParams(layoutParams);
    }

    public static void setImageSize(View view, int width) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, width);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        view.setLayoutParams(layoutParams);
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public static float convertPixelToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    public static String shortString(String inString, int maxLenght) {
        String outString = "";
        if (inString.length() > maxLenght) {
            outString = inString.substring(0, maxLenght) + "...";
        } else {
            outString = inString;
        }
        return outString;
    }

    public static void loadImageLoader(DisplayImageOptions options, String url, final ImageView imageView) {
        //ImageLoaderConfiguration config = new ImageLoaderConfiguration(context);
        //config.memoryCache(new WeakMemoryCache());
        ImageLoader.getInstance().displayImage(url, imageView, options, new SimpleImageLoadingListener() {
            final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

            @Override
            public void onLoadingStarted(String imageUri, View view) {
                //spinner.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                String message = null;
                switch (failReason.getType()) {
                    case IO_ERROR:
                        message = "Check internet";
                        break;
                    case DECODING_ERROR:
                        message = "Image can't be decoded";
                        break;
                    case NETWORK_DENIED:
                        message = "Downloads are denied";
                        break;
                    case OUT_OF_MEMORY:
                        message = "Out Of Memory error";
                        break;
                    case UNKNOWN:
                        message = "Unknown error";
                        break;
                }
                Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (loadedImage != null) {
                    ImageView imageView = (ImageView) view;
                    boolean firstDisplay = !displayedImages.contains(imageUri);
                    if (firstDisplay) {
                        FadeInBitmapDisplayer.animate(imageView, 300);
                        displayedImages.add(imageUri);
                    }
                }
                imageView.setImageBitmap(loadedImage);
            }
        });
    }

    public static void showToast(String message, Context context) {
        Toast t = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + listView.getDividerHeight() * 4;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static void setHListViewHeightBasedOnChildren(HListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        View listItem = listAdapter.getView(0, null, listView);
        listItem.measure(0, 0);
        totalHeight += listItem.getMeasuredHeight();


        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = (int) (totalHeight * 1.05);
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static void setGridViewHeightBasedOnChildren(GridView listView, Context context) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = (totalHeight / 3) + calculateActionBarSize(context);
        //params.height = (totalHeight/3);
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static void setHeightRVSuggestApp(RecyclerView rv) {
        View chView = rv.getChildAt(0);
        chView.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        //totalHeight += Utils.dpToPx(8+15);
        LinearLayout.LayoutParams listParams = (LinearLayout.LayoutParams) rv.getLayoutParams();
        listParams.height = chView.getMeasuredHeight();
        rv.setLayoutParams(listParams);
    }

    public static int getToolbarHeight(Context context) {
        int toolbarHeight = 0;
        return toolbarHeight;
    }

    public static void showDialog(Context context, String content) {
        new MaterialDialog.Builder(context)
                .title("THÔNG BÁO")
                .content(content)
                .positiveText("MỞ")
                .negativeText("THOÁT")
                .positiveColor(context.getResources().getColor(R.color.main_color))
                .negativeColor(context.getResources().getColor(R.color.main_color))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .show();
    }


}
