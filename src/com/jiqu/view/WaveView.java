/*
 *  Copyright (C) 2015, gelitenight(gelitenight@gmail.com).
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.jiqu.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class WaveView extends View {
    /**
     * +------------------------+
     * |<--wave length->        |______
     * |   /\          |   /\   |  |
     * |  /  \         |  /  \  | amplitude
     * | /    \        | /    \ |  |
     * |/      \       |/      \|__|____
     * |        \      /        |  |
     * |         \    /         |  |
     * |          \  /          |  |
     * |           \/           | water level
     * |                        |  |
     * |                        |  |
     * +------------------------+__|____
     */
    private static final float DEFAULT_AMPLITUDE_RATIO = 0.05f;
    private static final float DEFAULT_WATER_LEVEL_RATIO = 0.5f;
    private static final float DEFAULT_WAVE_LENGTH_RATIO = 1.0f;
    private static final float DEFAULT_WAVE_SHIFT_RATIO = 0.0f;

    public static final int DEFAULT_BEHIND_WAVE_COLOR = Color.parseColor("#28FFFFFF");
    public static final int DEFAULT_FRONT_WAVE_COLOR = Color.parseColor("#35c3f5");
    private static final int DEFAULT_TEXT_COLOR = Color.parseColor("#35c3f5");
    
    private static final int DEFAULT_TEXT_SIZE = 40;
    public static final ShapeType DEFAULT_WAVE_SHAPE = ShapeType.CIRCLE;

    public enum ShapeType {
        CIRCLE,
        SQUARE
    }

    // if true, the shader will display the wave
    private boolean mShowWave;

    // shader containing repeated waves
    private BitmapShader mWaveShader;
    // shader matrix
    private Matrix mShaderMatrix;
    // paint to draw wave
    private Paint mViewPaint;
    // paint to draw border
    private Paint mBorderPaint;
    
    private Paint mPaintText;

    private float mDefaultAmplitude;
    private float mDefaultWaterLevel;
    private float mDefaultWaveLength;
    private double mDefaultAngularFrequency;
	private FontMetrics mFontMetrics;
	private float mTextHeight;
	private String mText;
	private boolean visible = true;

    private float mAmplitudeRatio = DEFAULT_AMPLITUDE_RATIO;
    private float mWaveLengthRatio = DEFAULT_WAVE_LENGTH_RATIO;
    private float mWaterLevelRatio = DEFAULT_WATER_LEVEL_RATIO;
    private float mWaveShiftRatio = DEFAULT_WAVE_SHIFT_RATIO;

//    private int mBehindWaveColor = DEFAULT_BEHIND_WAVE_COLOR;
    private int mFrontWaveColor = DEFAULT_FRONT_WAVE_COLOR;
    private ShapeType mShapeType = DEFAULT_WAVE_SHAPE;

    public WaveView(Context context) {
        super(context);
        init(context);
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mShaderMatrix = new Matrix();
        mViewPaint = new Paint();
        mViewPaint.setAntiAlias(true);
        
        mPaintText = new Paint();
        mPaintText.setAntiAlias(true);
        mPaintText.setTextSize(DEFAULT_TEXT_SIZE);
        mPaintText.setTextAlign(Align.CENTER);
        mPaintText.setColor(DEFAULT_TEXT_COLOR);
        mPaintText.setShadowLayer(2, 0, 0, 0xee666666);
//		mPaintText.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/lantinghei.ttf"));
		
		mFontMetrics = mPaintText.getFontMetrics();
		mTextHeight = mFontMetrics.bottom - mFontMetrics.top;
    }

    public float getWaveShiftRatio() {
        return mWaveShiftRatio;
    }
    
    public void setTextVisible(boolean visible){
    	this.visible = visible;
    	invalidate();
    }

    /**
     * Shift the wave horizontally according to <code>waveShiftRatio</code>.
     *
     * @param waveShiftRatio Should be 0 ~ 1. Default to be 0.
     *                       <br/>Result of waveShiftRatio multiples width of WaveView is the length to shift.
     */
    public void setWaveShiftRatio(float waveShiftRatio) {
        if (mWaveShiftRatio != waveShiftRatio) {
            mWaveShiftRatio = waveShiftRatio;
            invalidate();
        }
    }

    public float getWaterLevelRatio() {
        return mWaterLevelRatio;
    }

    /**
     * Set water level according to <code>waterLevelRatio</code>.
     *
     * @param waterLevelRatio Should be 0 ~ 1. Default to be 0.5.
     *                        <br/>Ratio of water level to WaveView height.
     */
    public void setWaterLevelRatio(float waterLevelRatio) {
        if (mWaterLevelRatio != waterLevelRatio) {
            mWaterLevelRatio = waterLevelRatio;
        }
        invalidate();
    }

    public float getAmplitudeRatio() {
        return mAmplitudeRatio;
    }

    /**
     * Set vertical size of wave according to <code>amplitudeRatio</code>
     *
     * @param amplitudeRatio Default to be 0.05. Result of amplitudeRatio + waterLevelRatio should be less than 1.
     *                       <br/>Ratio of amplitude to height of WaveView.
     */
    public void setAmplitudeRatio(float amplitudeRatio) {
        if (mAmplitudeRatio != amplitudeRatio) {
            mAmplitudeRatio = amplitudeRatio;
            invalidate();
        }
    }

    public float getWaveLengthRatio() {
        return mWaveLengthRatio;
    }

    /**
     * Set horizontal size of wave according to <code>waveLengthRatio</code>
     *
     * @param waveLengthRatio Default to be 1.
     *                        <br/>Ratio of wave length to width of WaveView.
     */
    public void setWaveLengthRatio(float waveLengthRatio) {
        mWaveLengthRatio = waveLengthRatio;
    }

    public boolean isShowWave() {
        return mShowWave;
    }

    public void setShowWave(boolean showWave) {
        mShowWave = showWave;
    }

    public void setBorder(int width, int color) {
        if (mBorderPaint == null) {
            mBorderPaint = new Paint();
            mBorderPaint.setAntiAlias(true);
            mBorderPaint.setStyle(Style.STROKE);
        }
        mBorderPaint.setColor(color);
        mBorderPaint.setStrokeWidth(width);

        invalidate();
    }

    public void setWaveColor(int behindWaveColor, int frontWaveColor) {
//        mBehindWaveColor = behindWaveColor;
        mFrontWaveColor = frontWaveColor;

        // need to recreate shader when color changed
        mWaveShader = null;
        createShader();
        invalidate();
    }
    
    public void setText(String text){
    	mText = text;
    	invalidate();
    }
    
    public void setWaveColor(int frontWaveColor) {
      mFrontWaveColor = frontWaveColor;

      // need to recreate shader when color changed
      mWaveShader = null;
      createShader();
      invalidate();
  }
    
    public void setTextSize(int size){
    	mPaintText.setTextSize(size);
    	mFontMetrics = mPaintText.getFontMetrics();
		mTextHeight = mFontMetrics.bottom - mFontMetrics.top;
		
		invalidate();
    }

    public void setShapeType(ShapeType shapeType) {
        mShapeType = shapeType;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        createShader();
    }

    /**
     * Create the shader with default waves which repeat horizontally, and clamp vertically
     */
    private void createShader() {
        mDefaultAngularFrequency = 2.0f * Math.PI / DEFAULT_WAVE_LENGTH_RATIO / getWidth();
        //(振幅)波浪垂直振动时偏离水面的最大距离
        mDefaultAmplitude = getHeight() * DEFAULT_AMPLITUDE_RATIO;
        //(水位)波浪静止时水面距离底部的高度
        mDefaultWaterLevel = getHeight() * DEFAULT_WATER_LEVEL_RATIO;
        //(波长)一个完整的波浪的水平长度
        mDefaultWaveLength = getWidth();

        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint wavePaint = new Paint();
        wavePaint.setStrokeWidth(2);
        wavePaint.setAntiAlias(true);

        // Draw default waves into the bitmap
        // y=Asin(ωx+φ)+h
        final int endX = getWidth() + 1;
        final int endY = getHeight() + 1;

        float[] waveY = new float[endX];

//        wavePaint.setColor(mBehindWaveColor);
//        for (int beginX = 0; beginX < endX; beginX++) {
//            double wx = beginX * mDefaultAngularFrequency;
//            float beginY = (float) (mDefaultWaterLevel + mDefaultAmplitude * Math.sin(wx));
//            canvas.drawLine(beginX, beginY, beginX, endY, wavePaint);
//
//            waveY[beginX] = beginY;
//        }

        wavePaint.setColor(mFrontWaveColor);
//        final int wave2Shift = (int) (mDefaultWaveLength / 4);
//        for (int beginX = 0; beginX < endX; beginX++) {
//            canvas.drawLine(beginX, waveY[(beginX + wave2Shift) % endX], beginX, endY, wavePaint);
//        }
        
        for (int beginX = 0; beginX < endX; beginX++) {
            double wx = beginX * mDefaultAngularFrequency;
            float beginY = (float) (mDefaultWaterLevel + mDefaultAmplitude * Math.sin(wx));
            canvas.drawLine(beginX, beginY, beginX, endY, wavePaint);

//            waveY[beginX] = beginY;
        }

        // use the bitamp to create the shader
        mWaveShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        mViewPaint.setShader(mWaveShader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // modify paint shader according to mShowWave state
        if (mShowWave && mWaveShader != null) {
            // first call after mShowWave, assign it to our paint
            if (mViewPaint.getShader() == null) {
                mViewPaint.setShader(mWaveShader);
            }

            // sacle shader according to mWaveLengthRatio and mAmplitudeRatio
            // this decides the size(mWaveLengthRatio for width, mAmplitudeRatio for height) of waves
            mShaderMatrix.setScale(
                mWaveLengthRatio / DEFAULT_WAVE_LENGTH_RATIO,
                mAmplitudeRatio / DEFAULT_AMPLITUDE_RATIO,
                0,
                mDefaultWaterLevel);
            // translate shader according to mWaveShiftRatio and mWaterLevelRatio
            // this decides the start position(mWaveShiftRatio for x, mWaterLevelRatio for y) of waves
            mShaderMatrix.postTranslate(
                mWaveShiftRatio * getWidth(),
                (DEFAULT_WATER_LEVEL_RATIO - mWaterLevelRatio) * getHeight());

            // assign matrix to invalidate the shader
            mWaveShader.setLocalMatrix(mShaderMatrix);

            float borderWidth = mBorderPaint == null ? 0f : mBorderPaint.getStrokeWidth();
            switch (mShapeType) {
                case CIRCLE:
                    if (borderWidth > 0) {
                        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f,
                            (getWidth() - borderWidth) / 2f - 1f, mBorderPaint);
                    }
                    
                    float radius = getWidth() / 2f - borderWidth;
                    canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, radius - 1f, mViewPaint);
                    if (visible) {
                    	canvas.drawText(mText, getWidth() / 2f,getHeight() - (getHeight() - mTextHeight) / 2 - mFontMetrics.bottom, mPaintText);
					}
                    
                    break;
                case SQUARE:
                    if (borderWidth > 0) {
                        canvas.drawRect(
                            borderWidth / 2f,
                            borderWidth / 2f,
                            getWidth() - borderWidth / 2f - 0.5f,
                            getHeight() - borderWidth / 2f - 0.5f,
                            mBorderPaint);
                    }
                    canvas.drawRect(borderWidth, borderWidth, getWidth() - borderWidth,
                        getHeight() - borderWidth, mViewPaint);
                    break;
            }
        } else {
            mViewPaint.setShader(null);
        }
    }
}
