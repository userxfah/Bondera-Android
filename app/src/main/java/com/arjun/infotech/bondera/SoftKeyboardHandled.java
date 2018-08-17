package com.arjun.infotech.bondera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class SoftKeyboardHandled extends RelativeLayout {

    private boolean isKeyboardShown = false;
    private List<SoftKeyboardLsner> lsners=new ArrayList<SoftKeyboardLsner>();
    private float layoutMaxH = 0f; // max measured height is considered layout normal size
    private static final float DETECT_ON_SIZE_PERCENT = 0.8f;

    public SoftKeyboardHandled(Context context) {
        super(context);
    }

    public SoftKeyboardHandled(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressLint("NewApi")
    public SoftKeyboardHandled(Context context, AttributeSet attrs,
                                           int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int newH = MeasureSpec.getSize(heightMeasureSpec);
        if (newH > layoutMaxH) {
            layoutMaxH = newH;
        }
        if (layoutMaxH != 0f) {
            final float sizePercent = newH / layoutMaxH;
            if (!isKeyboardShown && sizePercent <= DETECT_ON_SIZE_PERCENT) {
                isKeyboardShown = true;
                for (final SoftKeyboardLsner lsner : lsners) {
                    lsner.onSoftKeyboardShow();
                }
            } else if (isKeyboardShown && sizePercent > DETECT_ON_SIZE_PERCENT) {
                isKeyboardShown = false;
                for (final SoftKeyboardLsner lsner : lsners) {
                    lsner.onSoftKeyboardHide();
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void addSoftKeyboardLsner(SoftKeyboardLsner lsner) {
        lsners.add(lsner);
    }

    public void removeSoftKeyboardLsner(SoftKeyboardLsner lsner) {
        lsners.remove(lsner);
    }

    // Callback
    public interface SoftKeyboardLsner {
        public void onSoftKeyboardShow();
        public void onSoftKeyboardHide();
    }

//    private boolean isKeyboardShown;
//    private SoftKeyboardVisibilityChangeListener listener;
//
//    public SoftKeyboardHandled(Context context) {
//        super(context);
//    }
//
//    public SoftKeyboardHandled(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    @SuppressLint("NewApi")
//    public SoftKeyboardHandled(Context context, AttributeSet attrs,
//                               int defStyle) {
//        super(context, attrs, defStyle);
//    }
//
//    @Override
//    public boolean dispatchKeyEventPreIme(KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//            // Keyboard is hidden <<< RIGHT
//            if (isKeyboardShown) {
//                isKeyboardShown = false;
//                listener.onSoftKeyboardHide();
//            }
//        }
//        return super.dispatchKeyEventPreIme(event);
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        final int proposedheight = MeasureSpec.getSize(heightMeasureSpec);
//        final int actualHeight = getHeight();
//        if (actualHeight > proposedheight) {
//            // Keyboard is shown
//            if (!isKeyboardShown) {
//                isKeyboardShown = true;
//                listener.onSoftKeyboardShow();
//            }
//        } else {
//            // Keyboard is hidden <<< this doesn't work sometimes, so I don't use it
//        }
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    }
//
//    public void setOnSoftKeyboardVisibilityChangeListener(SoftKeyboardVisibilityChangeListener listener) {
//        this.listener = listener;
//    }
//
//    // Callback
//    public interface SoftKeyboardVisibilityChangeListener {
//        public void onSoftKeyboardShow();
//
//        public void onSoftKeyboardHide();
//    }

}
