package com.ttyrovou.snake.panels;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.ttyrovou.snake.MainThread;
import com.ttyrovou.snake.R;
import com.ttyrovou.snake.sprites.Background;
import com.ttyrovou.snake.sprites.SnakeBoard;

public class SnakePanel extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread thread;
    private Background background;
    private SnakeBoard snakeBoard;

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
        snakeBoard.draw(canvas);
        Drawable ladder;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ladder = getResources().getDrawable(R.drawable.ic_ladder, null);
        } else
            ladder = getResources().getDrawable(R.drawable.ic_ladder);
        ladder.setBounds(new Rect(0, 0, 100, 100));
        ladder.draw(canvas);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        initGraphics();

        thread.setRunning(true);
        thread.start();
    }

    public void initGraphics() {
        background = new Background(getWidth(), getHeight(), Color.rgb(255, 183, 81));
        snakeBoard = new SnakeBoard(10, 10, (int) (getWidth() * 0.9), (int) (getHeight() * 0.6), (int) (getWidth() * 0.05),
                (int) (getHeight() * 0.1));
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
