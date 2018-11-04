package com.ttyrovou.snake.panels;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.ttyrovou.snake.MainThread;
import com.ttyrovou.snake.sprites.Background;

public class SnakePanel extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;
    Background background;

    public SnakePanel(Context context) {
        super(context);

        thread = new MainThread(getHolder(), this);

        getHolder().addCallback(this);
        setFocusable(true);
        setWillNotDraw(false);
    }

    public void update() {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        background.draw(canvas);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        background = new Background(getWidth(), getHeight(), Color.rgb(255, 153, 51));

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Toast.makeText(getContext(), "x: " + event.getX() + ", y: " + event.getY(), Toast.LENGTH_SHORT).show();
        }
        return super.onTouchEvent(event);
    }
}
