package com.music.pinkorange.vibrato;

/** LIVE MUSIC https://www.dreamincode.net/forums/topic/303235-visualizing-sound-from-the-microphone/ **/
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class PerceptualVisualizer extends View {

    private byte[] mFFTbytes;
    private float[] mPoints1;
    private float[] mPoints2;
    private Rect mRect = new Rect();
    private Paint mPaint = new Paint();
    private Paint mPaint2 = new Paint();

    private int amp;

    private static final int LINE_WIDTH = 20; //width of visualier lines
    private static final int LINE_SCALE = 5; //scales visualier lines

    public PerceptualVisualizer(Context context) {
        super(context);
        init();
    }

    public PerceptualVisualizer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PerceptualVisualizer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mFFTbytes = null;
        mPaint.setStrokeWidth(1f);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.rgb(0, 128, 255));
        mPaint2.setStrokeWidth(5f);
        mPaint2.setAntiAlias(true);
        mPaint2.setColor(Color.rgb(0, 128, 255));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        setBackgroundColor(Color.BLACK);

        if (mFFTbytes == null) {
            return;
        }

        int screenWidth = getWidth();
        int radiusMultiplier = screenWidth/4;

        int captureSize = mFFTbytes.length;

        float[] magnitudes = new float[captureSize/2 + 1];
        float[] phases = new float[captureSize / 2 + 1];

        magnitudes[0] = (float)Math.abs(mFFTbytes[0]);
        phases[0] = phases[captureSize / 2] = 0;

        // Nyquist
        magnitudes[captureSize/2] = (float)Math.abs(mFFTbytes[1]);

        if (mPoints1 == null || mPoints1.length < mFFTbytes.length * 4) {
            mPoints1 = new float[mFFTbytes.length * 4];
        }

        if (mPoints2 == null || mPoints2.length < mFFTbytes.length * 4) {
            mPoints2 = new float[mFFTbytes.length * 4];
        }

        mRect.set(0, 0, getWidth(), getHeight());


        cycleColor();
        mPaint.setStrokeWidth(LINE_WIDTH);

        // Use 'angle' to keep it rotating

        double angle = 0;

        for (int i = 0; i < 360; i++, angle++) {
            mPoints1[i * 4] = (float) (getWidth() / 2
                    + Math.abs(mFFTbytes[i * 2])
                    * radiusMultiplier
                    * Math.cos(Math.toRadians(angle)));
            mPoints1[i * 4 +  1] = (float) (getHeight() / 2
                    + Math.abs(mFFTbytes[i * 2])
                    * radiusMultiplier
                    * Math.sin(Math.toRadians(angle)));

            mPoints1[i * 4 + 2] = (float) (getWidth() / 2
                    + Math.abs(mFFTbytes[i * 2 + 1])
                    * radiusMultiplier
                    * Math.cos(Math.toRadians(angle + 1)));

            mPoints1[i * 4 + 3] = (float) (getHeight() / 2
                    + Math.abs(mFFTbytes[i * 2 + 1])
                    * radiusMultiplier
                    * Math.sin(Math.toRadians(angle + 1)));
        }
        canvas.drawLines(mPoints1, mPaint);

        for (int i = 0; i < mFFTbytes.length - 1; i++) {
            float[] cartPoint = {
                    (float)i / (mFFTbytes.length - 1),
                    getHeight() / 2 + ((byte) (mFFTbytes[i] + 128)) * ( getHeight()  / 2) / 128
            };

            float[] polarPoint = toPolar(cartPoint, mRect);
            mPoints2[i * 4] = polarPoint[0];
            mPoints2[i * 4 + 1] = polarPoint[1];

            float[] cartPoint2 = {
                    (float)(i + 1) / (mFFTbytes.length - 1),
                    getHeight()  / 2 + ((byte) (mFFTbytes[i + 1] + 128)) * ( getHeight() / 2) / 128
            };

            float[] polarPoint2 = toPolar(cartPoint2, mRect);
            mPoints2[i * 4 + 2] = polarPoint2[0];
            mPoints2[i * 4 + 3] = polarPoint2[1];
        }
        canvas.drawLines(mPoints2, mPaint2);


        // Controls the pulsing rate
        modulation += 0.0;

        for (int k = 1; k < captureSize / 2; k++) {

            int i = k * 2;

            magnitudes[k] = (float)Math.hypot(mFFTbytes[i], mFFTbytes[i + 1]);
            phases[k] = (float)Math.atan2(mFFTbytes[i + 1], mFFTbytes[i]);

            canvas.drawCircle((float) Math.cos(phases[k]), (float) Math.sin(phases[k]), 5, mPaint);

            if (magnitudes[k] < 1.5) {
                amp = 0;
            } else {
                amp = 128;
            }

        }
    }

    public void updateVisualizerFFT(byte[] bytes) {
        mFFTbytes = bytes;
        invalidate();
    }

    private float colorCounter = 0;
    private void cycleColor()
    {
        int r = (int)Math.floor(128*(Math.sin(colorCounter) + 1));
        int g = (int)Math.floor(128*(Math.sin(colorCounter + 2) + 1));
        int b = (int)Math.floor(128*(Math.sin(colorCounter + 4) + 1));
        mPaint.setColor(Color.argb(128, r, g, b));
        mPaint2.setColor(Color.argb(128, b/5, r, g));
        colorCounter += 0.03;
    }

    float modulation = 0;
    float aggresive = 0.5f;
    private float[] toPolar(float[] cartesian, Rect rect)
    {
        double cX = rect.width()/2;
        double cY = rect.height()/2;
        double angle = (cartesian[0]) * 2 * Math.PI;
        double radius = ((rect.width()/2) * (1 - aggresive) + aggresive * cartesian[1]/2) * (1.2 + Math.sin(modulation))/2.2;
        float[] out =  {
                (float)(cX + radius * Math.sin(angle)),
                (float)(cY + radius * Math.cos(angle))
        };
        return out;
    }

}