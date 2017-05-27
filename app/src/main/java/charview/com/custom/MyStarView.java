package charview.com.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

/**
 * Created by lin on 2017/5/26.
 */

public class MyStarView extends View {
    private int width;
    private int height;
    private Paint mPaint;
    private float mScaleFactor = 1;

    public MyStarView(Context context) {
        this(context, null);
    }

    public MyStarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyStarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(10);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.width = getWidth();
        this.height = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < 5; i++) {
            canvas.drawLine(width / 2 - 300, height / 2 - 100, width / 2 + 300, height / 2 - 100, mPaint);
            canvas.rotate(72, width / 2, height / 2);
        }
        setScaleX(mScaleFactor);
        setScaleY(mScaleFactor);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount() == 1) {
             mGestureDetector.onTouchEvent(event);
        } else {
            mScaleGestureDector.onTouchEvent(event);
        }
        return true;
    }

    private GestureDetector mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            scrollBy((int) distanceX,(int)distanceY);
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    });
    private ScaleGestureDetector mScaleGestureDector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener(){
        float currentSpan;
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            relativeScale(detector.getCurrentSpan() / currentSpan);
            return false;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            currentSpan = detector.getCurrentSpan();
            return true;
        }
    });

    public void relativeScale(float scaleFactor) {
        mScaleFactor *= scaleFactor;
        this.invalidate();
    }
    public void restore() {
        mScaleFactor = 1;
        this.invalidate();
    }

}