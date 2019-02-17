package com.ttyrovou.snake.sprites;

import android.graphics.Canvas;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import com.ttyrovou.snake.AndroidUtils;

public class ScreenText extends BaseSprite {

    private Layout textLayout;
    private int x, y;

    public ScreenText(CharSequence text, int x, int y, int maxWidth, float textSizeDP) {
        this.x = x;
        this.y = y;
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(AndroidUtils.convertDpToPixel(textSizeDP));
        textPaint.setColor(0xFF000000);

        textLayout = new StaticLayout(text, textPaint, maxWidth,
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.translate(x, y);
        textLayout.draw(canvas);
        canvas.translate(-x, -y);
    }

    public int getHeight() {
        return textLayout.getHeight();
    }

    public float getMeasuredWidth() {
        return textLayout.getPaint().measureText(textLayout.getText().toString());
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setText(CharSequence text) {
        if (!text.equals(textLayout.getText())) {
            textLayout = new StaticLayout(text, textLayout.getPaint(), textLayout.getWidth(),
                    Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
        }
    }
}
