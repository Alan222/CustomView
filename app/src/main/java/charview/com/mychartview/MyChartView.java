package charview.com.mychartview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by lin on 2017/4/16.
 */

public class MyChartView extends View {
    private ArrayList<Integer> itemData = new ArrayList<>();
    private int[] shadeColors;
    private Paint mShawerPaint;
    private Path mShowerPath;
    private Paint mCirclePaint;
    private int yMax;
    private int yMin;


    private int mLineColor;
    private int mTextSize;
    private int mWidth;//控件的宽
    private int mHeight;//控件的高
    private int mPaintColor;
    private Paint mPaint;
    private Paint mLinePaint;
    private Path mPath;
    private int xPoint; //原点X轴坐标
    private int yPoint; //原点Y轴坐标
    private int yTopPoint;//y轴顶点坐标
    private int dp3;
    private int dp6;
    private int yItemDistance;
    private int xItemDistance;
    private String xTitle = "近七日";
    private String yTitle = "新增/个";
    private ArrayList<String> xData = new ArrayList<>();
    private ArrayList<Integer> yData = new ArrayList<>();

    private int itemMin;
    private int itemMax;
    private float mProgress;


    public MyChartView(Context context) {
        this(context, null);
    }

    public MyChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        float textSize = getContext().getResources().getDimension(R.dimen.textSize);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyChartView);
        mPaintColor = typedArray.getColor(R.styleable.MyChartView_paintColor,Color.RED);
        mLineColor = typedArray.getColor(R.styleable.MyChartView_chartLineColor, Color.RED);
        mTextSize = (int) typedArray.getDimension(R.styleable.MyChartView_textSize, textSize);
        typedArray.recycle();
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            mWidth = getWidth();
            mHeight = getHeight();

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        initData();
        drawAxes(canvas);        //  画坐标轴
        drawText(canvas);//  画文字
        drawLine(canvas);//  画折线
    }

    private void initData() {
        //初始化画笔
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);//去锯齿
        mPaint.setColor(mPaintColor);//颜色
        mPaint.setTextSize(mTextSize);
        //折线画笔
        mLinePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setAntiAlias(true);//去锯齿
        mLinePaint.setColor(mLineColor);//颜色
        mLinePaint.setTextSize(mTextSize);

        mCirclePaint = new Paint();
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setAntiAlias(true);//去锯齿
        mCirclePaint.setColor(mLineColor);//颜色


        //阴影画笔
        mShawerPaint = new Paint();
        mShawerPaint.setAntiAlias(true);
        mShawerPaint.setStrokeWidth(2f);

        //  初始化折线路径
        mPath = new Path();
        mShowerPath = new Path();


        xPoint = (int) getContext().getResources().getDimension(R.dimen.dp20);
        yTopPoint = (int) getContext().getResources().getDimension(R.dimen.dp20);
        yPoint = mHeight - yTopPoint;

        dp3 = (int) getContext().getResources().getDimension(R.dimen.dp3);  //箭头的偏移量
        dp6 = (int) getContext().getResources().getDimension(R.dimen.dp6);  //箭头的偏移量
        //间距=(终点坐标-起始坐标)/线的数量
        if (xData != null && xData.size() != 0) {
            xItemDistance = (mWidth - xPoint - xPoint) / xData.size();
        }

        if (yData != null && yData.size() != 0) {
            yItemDistance = (yPoint - (yTopPoint + mTextSize)) / yData.size();//y轴字段坐标的间距 5代表除起始坐标外有5条线
        }

        shadeColors = new int[]{
                Color.argb(80, Color.red(mPaintColor), Color.green(mPaintColor), Color.blue(mPaintColor)),
                Color.argb(20, Color.red(mPaintColor), Color.green(mPaintColor), Color.blue(mPaintColor)),
                Color.argb(0, Color.red(mPaintColor), Color.green(mPaintColor), Color.blue(mPaintColor))};

        if (yData.size() > 0) {
            yMin = yData.get(0);
            yMax = yData.get(0);
        }
        for (int i = 0; i < yData.size(); i++) {
            if (yData.get(i) > yMax) {
                yMax = yData.get(i);
            } else if (yData.get(i) < yMin) {
                yMin = yData.get(i);
            }
        }
    }

    private void drawAxes(Canvas canvas) {
        canvas.drawLine(xPoint, yPoint, mWidth - xPoint, yPoint, mPaint);   //x轴起始坐标线
        canvas.drawLine(mWidth - xPoint, yPoint, mWidth - xPoint - dp6, yPoint + dp3, mPaint);  //向右箭头
        canvas.drawLine(mWidth - xPoint, yPoint, mWidth - xPoint - dp6, yPoint - dp3, mPaint);

        int xTextPoint; //x轴字段的坐标
        if (xData != null) {
            for (int i = 1; i < xData.size(); i++) {
                xTextPoint = xPoint + xItemDistance * i;
                canvas.drawLine(xTextPoint, yPoint, xTextPoint, yTopPoint +
                        mTextSize, mPaint);   //y轴线这里的线比起始Y轴短,留出位置画title
            }
        }

        canvas.drawLine(xPoint, yPoint, xPoint, yTopPoint, mPaint);   //y起始轴线
        canvas.drawLine(xPoint, yTopPoint, xPoint - dp3, yTopPoint + dp6, mPaint);  //向右箭头
        canvas.drawLine(xPoint, yTopPoint, xPoint + dp3, yTopPoint + dp6, mPaint);

        int yTextPoint; //y轴字段的坐标
        if (yData != null) {
            for (int i = 0; i < yData.size(); i++) {
                yTextPoint = yTopPoint + mTextSize + yItemDistance * i;
                canvas.drawLine(xPoint, yTextPoint, mWidth - xPoint, yTextPoint, mPaint);   //x轴线
            }
        }
    }

    private void drawText(Canvas canvas) {
        //画x轴的title
        int xTitleWidth = measureTextWidth(xTitle);
        canvas.drawText(xTitle, mWidth - xPoint - xTitleWidth / 2, yPoint + dp3 + mTextSize, mPaint);
        //画y轴的title
        canvas.drawText(yTitle, xPoint + dp6, yTopPoint + mTextSize / 2, mPaint);

        for (int i = 0; i < xData.size(); i++) {
            int dataWidth = measureTextWidth(xData.get(i));
            canvas.drawText(xData.get(i), xPoint + xItemDistance * i - dataWidth / 2, yPoint + dp3 + mTextSize, mPaint);
        }
        for (int i = 0; i < yData.size(); i++) {
            int dataWidth = measureTextWidth(yData.get(i) + "");
            canvas.drawText(yData.get(i) + "", xPoint - dataWidth - dp3, yPoint - yItemDistance * (i + 1), mPaint);
        }

    }

    private void drawLine(Canvas canvas) {

        for (int i = 0; i < itemData.size(); i++) {
            int integer = itemData.get(i);
            int itemLength = yMax - yMin;
            int yLength = yPoint - (yTopPoint + mTextSize) - yItemDistance;
            int itemY = 0;
            if (itemLength != 0) {
                itemY = (int) (((integer * 1.0-yMin) / itemLength) * yLength);
            }
            canvas.drawCircle((xPoint + xItemDistance * i), yPoint - yItemDistance - itemY, 3, mCirclePaint);//画小圆点

            if (i == 0) {
                mPath.moveTo(xPoint, yPoint - yItemDistance - itemY);
                mShowerPath.moveTo(xPoint, yPoint - yItemDistance - itemY);
            } else {
                mPath.lineTo(xPoint + xItemDistance * i, yPoint - yItemDistance - itemY);
                mShowerPath.lineTo(xPoint + xItemDistance * i, yPoint - yItemDistance - itemY);
                if (i == itemData.size() - 1) {
                    mShowerPath.lineTo(xPoint + xItemDistance * i, yPoint - yItemDistance);
                    mShowerPath.lineTo(xPoint, yPoint - yItemDistance);
                    mShowerPath.close();
                }
            }
        }
        Shader mShader = new LinearGradient(0, 0, 0, getHeight(), shadeColors, null, Shader.TileMode.CLAMP);
        mShawerPaint.setShader(mShader);
        canvas.drawPath(mPath, mLinePaint);         //画线
        canvas.drawPath(mShowerPath, mShawerPaint);     //画阴影
    }

    public void setxTitle(String xTitle) {
        this.xTitle = xTitle;
    }

    public void setyTitle(String yTitle) {
        this.yTitle = yTitle;
    }

    public void setItemData(@NonNull ArrayList<Integer> itemData) {
        this.itemData = itemData;

        if (itemData.size() == 0) {
            return;
        }

        itemMin = itemData.get(0);
        itemMax = itemData.get(0);

        for (int i = 0; i < itemData.size(); i++) {
            if (itemData.get(i) > itemMax) {
                itemMax = itemData.get(i);
            } else if (itemData.get(i) < itemMin) {
                itemMin = itemData.get(i);
            }
        }
        int yMin = itemMin / 10 *10;
        if (yMin < 0) {
            yMin = yMin - 10;
        }
        int yMax = (itemMax / 10) *10 + 10;
        if (itemMax%10==0) {
            yMax = itemMax / 10 * 10;
        }



        yData.clear();
        for (int i = 0; i <= 5; i++) {
            if ((yMax - yMin) / 5 <= 10) {
                yData.add(yMin + (yMax - yMin) / 5 * i);
            } else {
                yData.add(yMin + ((yMax - yMin) / 50 + 1) * 10 * i);
            }
        }
        Date date = new Date();
        long time = date.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("M-d");
        xData.clear();
        for (long i = itemData.size(); i > 0; i--) {
            long xDataTime = time - 24 * 60 * 60 * 1000 * i;
            xData.add(dateFormat.format(xDataTime));
        }

    }

    public int measureTextWidth(String text) {
        return (int) mPaint.measureText(text);
    }

    public void setxData(ArrayList<String> xData)  {
        this.xData = xData;
        if (xData.size() != itemData.size()) {
            try {
                throw new Exception("XData Count Unmatched Exception");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
/*    //  计算动画进度
    public void setPercentage(float percentage) {
        if (percentage < 0.0f || percentage > 1.0f) {
            throw new IllegalArgumentException(
                    "setPercentage not between 0.0f and 1.0f");
        }
        mProgress = percentage;
        invalidate();
    }
    public void startAnim(MyChartView lineChartView, long duration) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(lineChartView, "percentage", 0.0f, 1.0f);
        anim.setDuration(duration);
        anim.setInterpolator(new LinearInterpolator());
        anim.start();
    }
    private void setAnim(Canvas canvas) {
        PathMeasure measure = new PathMeasure(mPath, false);
        float pathLength = measure.getLength();
        PathEffect effect = new DashPathEffect(new float[]{pathLength,
                pathLength}, pathLength - pathLength * mProgress);
        mLinePaint.setPathEffect(effect);
        canvas.drawPath(mPath, mLinePaint);
    }*/
}
