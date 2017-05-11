package charview.com.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;


public class WaveView extends View {
    private int width;
    private int height;
    private Paint mPaint;
    private Path mPath;
    private Path mPath1;
    int w = 0;
    int i = -10;
    boolean isAdd = true;

    private NextFrameAction nextFrameAction;
    private Paint mPaint1;
    private int z;

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(0x22ffffff);
        mPaint.setAntiAlias(true);
        mPath = new Path();

        mPaint1 = new Paint();
        mPaint1.setColor(0x33ffffff);
        mPaint1.setAntiAlias(true);
        mPath1 = new Path();
        nextFrameAction = new NextFrameAction();

    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
        canvas.drawPath(mPath1, mPaint1);
        postDelayed(nextFrameAction, 80);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        init();
    }

    protected class NextFrameAction implements Runnable {
        @Override
        public void run() {
            mPath.reset();
            mPath1.reset();
            w += 5;
            if (w >= width - 5) {
                w = 5;
            }
            z = width - 5;
            if (z <= 0) {
                z = width - 5;
            }
            double max = height / 80.0 * (i);
            if (isAdd) {
                i++;
                if (i == 10) {
                    isAdd = false;
                }
            } else {
                i--;
                if (i == -10) {
                    isAdd = true;
                }
            }

            mPath.moveTo(0, (float) (height / 4.0 + max * Math.sin((2.5 * Math.PI * w))));
            mPath1.moveTo(0, (float) (height / 4.0 + max * Math.cos((2.5 * Math.PI * z))));
            float pro = 3;
            for (float i = 0; i < width; i = i + pro) {
                double sin = max * Math.sin((2.5 * Math.PI * (i + w)) / (width)) + height / 4.0;
                double cos = max * Math.cos(((2.5 * Math.PI * (i + z)) / (width)) + Math.PI / 2) + height / 4.0;
                mPath.lineTo(i, (float) sin);
                mPath1.lineTo(i, (float) cos);

            }

            mPath.lineTo(width, height);
            mPath.lineTo(0, height);
            mPath.close();

            mPath1.lineTo(width, height);
            mPath1.lineTo(0, height);
            mPath1.close();
            invalidate();
        }
    }
}