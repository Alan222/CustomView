package charview.com.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;


public class MyWaveView extends View {

    private Paint mPaint;
    private Path mPath;
    int width;
    int height;
    private Runnable nextFrameAction;
    private int w;
    private Paint mPaint2;
    private Path mPath2;
    private int w2;

    float recycle = 0.8f;

    public MyWaveView(Context context) {
        this(context, null);
    }

    public MyWaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyWaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(0x22ffffff);
        mPaint.setAntiAlias(true);
        mPath = new Path();

        mPaint2 = new Paint();
        mPaint2.setColor(0x22ffffff);
        mPaint2.setAntiAlias(true);
        mPath2 = new Path();
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
        canvas.drawPath(mPath2, mPaint2);
        postDelayed(nextFrameAction, 0);
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
            mPath2.reset();
            w += 10;
            w2 -= -16;
            int rand = 4;
            mPath.moveTo(0, (float) ((height / rand) * Math.sin((Math.PI*(w)) / (recycle*width))+height/(2)));
            mPath2.moveTo(0, (float) ((height / (rand+2)) * Math.sin((Math.PI*(w2)) / (recycle*width)+Math.PI/2)+height/(2)));
            float pro = 8;
            for (float i = 0; i < width; i = i + 0) {
                if (i + pro < width) {
                    i = i + pro;
                    double sin = (height / rand) * Math.sin((Math.PI * (i + w)) / (recycle * width)) + height / (2);
                    mPath.lineTo(i, (float) sin);
                    double sin2 = (height / (rand + 2)) * Math.sin((Math.PI * (i + w2)) / (recycle * width) + Math.PI / 2) + height / (2);
                    mPath2.lineTo(i, (float) sin2);
                } else {
                    i = width;
                    double sin = (height / rand) * Math.sin((Math.PI * (i + w)) / (recycle * width)) + height / (2);
                    mPath.lineTo(i, (float) sin);
                    double sin2 = (height / (rand + 2)) * Math.sin((Math.PI * (i + w2)) / (recycle * width) + Math.PI / 2) + height / (2);
                    mPath2.lineTo(i, (float) sin2);
                }
            }

            mPath.lineTo(width, height);
            mPath.lineTo(0, height);
            mPath.close();

            mPath2.lineTo(width, height);
            mPath2.lineTo(0, height);
            mPath2.close();
            invalidate();
        }
    }
}