package com.ttyrovou.snake;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import com.ttyrovou.snake.panels.SnakePanel;

/**
 * A thread that runs every frame
 *
 * @author Τυροβούζης Θεόδωρος
 * AEM 9369
 * phone number 6955253435
 * email ttyrovou@ece.auth.gr
 *
 * @author Τσιμρόγλου Στυλιανός
 * AEM 9468
 * phone number 6977030504
 * email stsimrog@ece.auth.gr
 */
public class MainThread extends Thread {

    private final static int MAX_FPS = 25;
    private final static int MAX_FRAME_SKIPS = 5;
    private final static int FRAME_PERIOD = 1000 / MAX_FPS;

    private boolean running;
    private final SurfaceHolder surfaceHolder;
    private SnakePanel snakePanel;

    public MainThread(SurfaceHolder surfaceHolder, SnakePanel snakePanel) {
        this.surfaceHolder = surfaceHolder;
        this.snakePanel = snakePanel;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * runs {@link SnakePanel#update()} and {@link SnakePanel#draw(Canvas)} {@link MainThread#MAX_FPS}
     * times a second
     */
    @Override
    public void run() {
        Canvas canvas = null;
        while (running) {
            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    if (!running) break;
                    long beginTime = System.currentTimeMillis();
                    int framesSkipped = 0;

                    snakePanel.update();
                    snakePanel.draw(canvas);

                    long refreshTime = System.currentTimeMillis() - beginTime;
                    int sleepTime = (int) (FRAME_PERIOD - refreshTime);

                    if (sleepTime > 0) {
                        try {
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException ignored) { }
                    }

                    while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
                        snakePanel.update();
                        sleepTime += FRAME_PERIOD;
                        framesSkipped++;
                    }
                }
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
