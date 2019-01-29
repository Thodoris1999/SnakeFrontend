package com.ttyrovou.snake.sprites;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import com.ttyrovou.snake.AndroidUtils;
import com.ttyrovou.snake.R;

public class GameOverScreen extends BaseSprite {

    private Rect rect;
    private Paint windowPaint, buttonPaint;
    private StaticLayout textLayout;
    private int textXpos, textYpos;
    private Rect retryContainer, mainMenuContainer;
    private Drawable retryDrawable, mainMenuDrawable;

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

        int buttonIconSize = (int) AndroidUtils.convertDpToPixel(24);
        int buttonSize = (int) AndroidUtils.convertDpToPixel(36);

        buttonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        buttonPaint.setColor(Color.GRAY);
        retryContainer = new Rect(x + width / 3 - buttonSize / 2, y + height - buttonSize / 2,
                x + width / 3 - buttonSize / 2 + buttonSize,
                y + height - buttonSize / 2 + buttonSize);
        mainMenuContainer = new Rect(x + 2 * width / 3 - buttonSize / 2,
                y + height - buttonSize / 2,
                x + 2 * width / 3 - buttonSize / 2 + buttonSize,
                y + height - buttonSize / 2 + buttonSize);

        retryDrawable = context.getResources().getDrawable(R.drawable.ic_refresh_black_24dp);
        mainMenuDrawable = context.getResources().getDrawable(R.drawable.ic_menu_black_24dp);

        retryDrawable.setBounds(x + width / 3 - buttonIconSize / 2, y + height - buttonIconSize / 2,
                x + width / 3 - buttonIconSize / 2 + buttonIconSize,
                y + height - buttonIconSize / 2 + buttonIconSize);
        mainMenuDrawable.setBounds(x + 2 * width / 3 - buttonIconSize / 2,
                y + height - buttonIconSize / 2,
                x + 2 * width / 3 - buttonIconSize / 2 + buttonIconSize,
                y + height - buttonIconSize / 2 + buttonIconSize);
    }

    public boolean isRetryClciked(float x, float y) {
        return retryContainer.contains((int) x, (int) y);
    }

    public boolean isMainMenuClicked(float x, float y) {
        return mainMenuContainer.contains((int) x, (int) y);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(rect, windowPaint);
        canvas.translate(textXpos, textYpos);
        textLayout.draw(canvas);
        canvas.translate(-textXpos, -textYpos);
        canvas.drawRect(retryContainer, buttonPaint);
        canvas.drawRect(mainMenuContainer, buttonPaint);
        retryDrawable.draw(canvas);
        mainMenuDrawable.draw(canvas);
    }
}
