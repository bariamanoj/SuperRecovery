package com.slbdeveloper.superrecovery.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.slbdeveloper.superrecovery.R;
import com.slbdeveloper.superrecovery.Utils.Util;

public class HoleView extends View {

    private static final String TAG = "HoleView";
    
    private Paint mPaint;

    private int mCircleCount;
    private int mCircleGap;
    private int mInmostRadius;

    private final int mRotate = 30;

    private int mGradientStartColor;
    private int[] mColorArrays;


    private Animation mRotateAnimation;

    public HoleView(Context context) {
        super(context);
    }

    public HoleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);


        mCircleCount = 7;
        mCircleGap = Util.dp2px(context, 10.f);
        mInmostRadius = Util.dp2px(context, 60.f);

        mGradientStartColor = Util.getColorFromRes(context, R.color.gradient_start);
        mColorArrays = new int[mCircleCount];
        mColorArrays[0] = Util.getColorFromRes(context, R.color.gradient_end_1);
        mColorArrays[1] = Util.getColorFromRes(context, R.color.gradient_end_2);
        mColorArrays[2] = Util.getColorFromRes(context, R.color.gradient_end_3);
        mColorArrays[3] = Util.getColorFromRes(context, R.color.gradient_end_4);
        mColorArrays[4] = Util.getColorFromRes(context, R.color.gradient_end_5);
        mColorArrays[5] = Util.getColorFromRes(context, R.color.gradient_end_6);
        mColorArrays[6] = Util.getColorFromRes(context, R.color.gradient_end_7);

        mRotateAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate_around_center);
        mRotateAnimation.setInterpolator(new LinearInterpolator());

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int centerH = getWidth() / 2;
        int centerV = getHeight() / 2;

        int radius = mInmostRadius;

        int[] colors = new int[2];
        for (int i = 0; i < this.mCircleCount; i++) {
            colors[0] = mGradientStartColor;
            colors[1] = this.mColorArrays[i];
            SweepGradient sweepGradient = new SweepGradient(centerH, centerV, colors, null);
            mPaint.setShader(sweepGradient);
            canvas.rotate(-mRotate, centerH, centerV);

            canvas.drawCircle(centerH, centerV, radius, mPaint);

            radius += mCircleGap;
            
        }
    }


    public void startAnimation(final long cleanTime) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
//                mIsActing = true;
//                mCleanTime = cleanTime;
//
//                setBackgroundColor(ViewUtils.getColorFromRes(mContext, R.color.blackhole_bg));
//
//                if (mIconList != null && mIconList.size() > 0) {
//                    startIconsAnimation();
//                }

                setVisibility(View.VISIBLE);
                if (mRotateAnimation != null) {
                    startAnimation(mRotateAnimation);
                }
            }
        }, 200L);
    }
}
