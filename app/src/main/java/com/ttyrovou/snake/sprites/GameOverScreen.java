package com.ttyrovou.snake.sprites;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

public class GameOverScreen extends BaseSprite {

    private Rect rect;
    private Paint windowPaint;
    private StaticLayout textLayout;
    private int textXpos, textYpos;

    private int TEXT_MARGIN_LEFT = 10;
    private int TEXT_MARGIN_TOP = 10;

    public GameOverScreen(Context context, int x, int y, int width, int height, String winnerName) {
        rect = new Rect(x, y, x + width, y + height);
        windowPaint = new Paint();
        windowPaint.setColor(Color.LTGRAY);

        String text = winnerName + " has won!";
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(24 * context.getResources().getDisplayMetrics().density);
        textPaint.setColor(0xFF000000);
        int textWidth = (int) textPaint.measureText(text);

        textLayout = new StaticLayout(text, textPaint, textWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);

        textXpos = x + TEXT_MARGIN_LEFT;
        textYpos = y + TEXT_MARGIN_TOP;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(rect, windowPaint);
        canvas.translate(textXpos, textYpos);
        textLayout.draw(canvas);
        canvas.translate(-textXpos, -textXpos);
    }
}
