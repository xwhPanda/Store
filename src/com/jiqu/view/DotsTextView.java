package com.jiqu.view;


import com.vr.store.R;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.TextView;

public class DotsTextView extends TextView {

    private int jumpHeight;
    private boolean autoPlay;
    private boolean isPlaying;
    private boolean isHide;
    private int period;


    private AnimatorSet mAnimatorSet = new AnimatorSet();

    public DotsTextView(Context context) {
        super(context);
        init(context, null);
    }

    public DotsTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DotsTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaitingDots);
            period = typedArray.getInt(R.styleable.WaitingDots_period, 6000);
            jumpHeight = typedArray.getInt(R.styleable.WaitingDots_jumpHeight, (int) (getTextSize() / 4));
            autoPlay = typedArray.getBoolean(R.styleable.WaitingDots_autoplay, true);
            typedArray.recycle();
        }
        String string = "Loading...";
        SpannableString spannable = new SpannableString(string);
        ObjectAnimator[] animatorsnimator = new ObjectAnimator[string.length()];
        JumpingSpan jumpingSpan = new JumpingSpan();
		spannable.setSpan(jumpingSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		animatorsnimator[0] = createDotJumpAnimator(jumpingSpan, 0);
        for(int i = 0;i<string.length();i++){
        	if (i != 0) {
        		JumpingSpan jumpingSpan1 = new JumpingSpan();
        		spannable.setSpan(jumpingSpan1, i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        		animatorsnimator[i] = createDotJumpAnimator(jumpingSpan1, period * i / string.length());
			}
        }
        	animatorsnimator[0].addUpdateListener(new AnimatorUpdateListener() {
        		
        		@Override
        		public void onAnimationUpdate(ValueAnimator valueAnimator) {
        			invalidate();
        		}
        	});
        setText(spannable,BufferType.SPANNABLE);
        mAnimatorSet.playTogether(animatorsnimator);
        
        isPlaying = autoPlay;
        if(autoPlay) {
            start();
        }
    }

    public void start() {
        isPlaying = true;
        setAllAnimationsRepeatCount(ValueAnimator.INFINITE);
        mAnimatorSet.start();
    }

    private ObjectAnimator createDotJumpAnimator(JumpingSpan jumpingSpan, long delay) {
        ObjectAnimator jumpAnimator = ObjectAnimator.ofFloat(jumpingSpan, "translationY", 0, -jumpHeight);
        jumpAnimator.setEvaluator(new TypeEvaluator<Number>() {

            @Override
            public Number evaluate(float fraction, Number from, Number to) {
                return Math.max(0, Math.sin(fraction * Math.PI * 2)) * (to.floatValue() - from.floatValue());
            }
        });
        jumpAnimator.setDuration(period);
        jumpAnimator.setStartDelay(delay);
        jumpAnimator.setRepeatCount(ValueAnimator.INFINITE);
        jumpAnimator.setRepeatMode(ValueAnimator.RESTART);
        return jumpAnimator;
    }

    public void stop() {
        isPlaying = false;
        setAllAnimationsRepeatCount(0);
    }

    private void setAllAnimationsRepeatCount(int repeatCount) {
        for (Animator animator : mAnimatorSet.getChildAnimations()) {
            if (animator instanceof ObjectAnimator) { 
                ((ObjectAnimator) animator).setRepeatCount(repeatCount);
            }
        }
    }


    public boolean isHide() {
        return isHide;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setJumpHeight(int jumpHeight) {
        this.jumpHeight = jumpHeight;
    }

    public void setPeriod(int period) {
        this.period = period;
    }
}
