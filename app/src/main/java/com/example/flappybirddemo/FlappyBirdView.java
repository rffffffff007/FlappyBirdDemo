package com.example.flappybirddemo;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class FlappyBirdView extends View {
    private static final String LOG_TAG = "FlappyBirdView";

    public FlappyBirdView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlappyBirdView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initDrawables() {
        mLastTime = SystemClock.uptimeMillis();
        final int height = getHeight();
        final int width = getWidth();

        ShapeDrawable bird = new ShapeDrawable(new OvalShape());
        bird.getPaint().setColor(0xffED213C);
        bird.getPaint().setAntiAlias(true);
        bird.getPaint().setStyle(Paint.Style.FILL);
        bird.setBounds(width / 2 - BIRD_RADIUS, height / 2 - BIRD_RADIUS,
                width / 2 + BIRD_RADIUS, height / 2 + BIRD_RADIUS);
        mBird = bird;

        mWalls = new ArrayList<Drawable>();
        for (int i = 0; i < WALL_COUNT; ++i) {
            ShapeDrawable wall = new ShapeDrawable(new RectShape());
            wall.getPaint().setColor(0xff9E9697);
            wall.getPaint().setAntiAlias(true);
            wall.getPaint().setStyle(Paint.Style.FILL);
            int wallLeft = width / (WALL_COUNT - 1) * i;
            int wallHeight = height / WALL_COUNT * i;
            wall.setBounds(wallLeft, 0, wallLeft + WALL_WIDTH, wallHeight);
            mWalls.add(wall);
        }
    }

    private void moveWalls(int dx) {
        for (Drawable wall : mWalls) {
            Rect rect = wall.getBounds();
            rect.offset(dx, 0);
            if (rect.right <= 0) {
                // if this rect is moved outside of left bound, move it to right bound.
                rect.offset(getWidth() + WALL_WIDTH - rect.right, 0);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBird == null) {
            initDrawables();
        }
        canvas.drawColor(0xff112233);
        final long curTime = SystemClock.uptimeMillis();
        for (Drawable wall : mWalls) {
            wall.draw(canvas);
        }
        mBird.draw(canvas);

        int wallOffset = (int) ((curTime - mLastTime) * WALL_SPEED);
        moveWalls(wallOffset);
        mLastTime = curTime;

        postInvalidate();
    }

    static final float WALL_SPEED = -0.2f;
    static final int WALL_COUNT = 5;
    static final int WALL_WIDTH = 50;
    static final int BIRD_RADIUS = 20;

    private List<Drawable> mWalls;
    private Drawable mBird;
    private long mLastTime;
}