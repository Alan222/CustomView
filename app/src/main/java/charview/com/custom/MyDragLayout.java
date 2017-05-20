package charview.com.custom;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.nineoldandroids.view.ViewHelper;

/**
 * Created by lin on 2017/5/20.
 */

public class MyDragLayout extends FrameLayout {

    private ViewDragHelper mDragHelper;
    private ViewGroup mLeftMenu;
    private ViewGroup mMainView;
    private int mWidth;
    private int mHeight;
    private int mRange;


    public MyDragLayout(Context context) {
        this(context,null);
    }

    public MyDragLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyDragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDragHelper = ViewDragHelper.create(this,1,mCallBack);
    }

    private ViewDragHelper.Callback mCallBack=new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child == mMainView) {
                left = fitLeft(left);
            } else {

            }
            return left;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return mRange;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            if (changedView == mLeftMenu) {
                mLeftMenu.layout(0, 0, mWidth, mHeight);
                int newLeft =fitLeft(mMainView.getLeft() + dx);

                mMainView.layout(newLeft, 0, newLeft + mWidth, mHeight);
            }
            dispatchEvent();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if (Math.abs(xvel) <= 20 && mMainView.getLeft() > mRange * 0.5f) {
                open();
            } else if (xvel > 20) {
                open();
            } else {
                close();
            }
        }
    };

    private void dispatchEvent() {
        float percent = mMainView.getLeft() * 1.0f / mRange;
        ViewHelper.setScaleX(mLeftMenu,evaluate(percent,0.5f,1.0f));
        ViewHelper.setScaleY(mLeftMenu,evaluate(percent, 0.5f, 1.0f));
        ViewHelper.setTranslationX(mLeftMenu,evaluate(percent,-mWidth/2f,0));
        ViewHelper.setAlpha(mLeftMenu,evaluate(percent,0.5f,1.0f));

        ViewHelper.setScaleX(mMainView,evaluate(percent,1f,0.8f));
        ViewHelper.setScaleY(mMainView,evaluate(percent, 1f, 0.8f));
    }
    public Float evaluate(float fraction, Number startValue, Number endValue) {
        float startFloat = startValue.floatValue();
        return startFloat + fraction * (endValue.floatValue() - startFloat);
    }
    private void open() {
        open(true);
    }

    private void open(boolean smooth) {
        int finalLeft = mRange;
        if (smooth) {
            if (mDragHelper.smoothSlideViewTo(mMainView, finalLeft, 0)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            mMainView.layout(finalLeft, 0, mWidth + finalLeft, mHeight);
        }
    }

    private void close() {
        close(true);
    }
    private void close(boolean smooth) {
        int finalLeft = 0;
        if (smooth) {
            if (mDragHelper.smoothSlideViewTo(mMainView, finalLeft, 0)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            mMainView.layout(finalLeft, 0, mWidth + finalLeft, mHeight);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);

        }

    }

    private int fitLeft(int left) {
        if (left < 0) {
            left = 0;
        } else if (left > mRange) {
            left = mRange;
        }
        return left;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            mDragHelper.processTouchEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() < 2) {
            throw new IllegalStateException("your viewgroup must contains 2 child");
        }
        if (!(getChildAt(0) instanceof ViewGroup )|| !(getChildAt(1) instanceof ViewGroup)) {
            throw new IllegalStateException("your child must be instanceof ViewGroup");
        }
        mLeftMenu = (ViewGroup) getChildAt(0);
        mMainView = (ViewGroup) getChildAt(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mRange = (int) (mWidth * 0.6);
    }
}
