package com.ttyrovou.snake.sprites;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.ttyrovou.snake.AndroidUtils;
import com.ttyrovou.snake.R;

/**
 * Draws the game over screen
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
public class GameOverScreen extends BaseSprite {

    private Rect rect;
    private Paint windowPaint, buttonPaint;
    private ScreenText winnerText;
    private Rect retryContainer, mainMenuContainer;
    private Drawable retryDrawable, mainMenuDrawable;

    private int TEXT_MARGIN_LEFT = 10;
    private int TEXT_MARGIN_TOP = 10;

    public GameOverScreen(Context context, int x, int y, int width, int height, String winnerName) {
        rect = new Rect(x, y, x + width, y + height);
        windowPaint = new Paint();
        windowPaint.setColor(Color.LTGRAY);

        String text = winnerName + " has won!";
        int textXpos = x + TEXT_MARGIN_LEFT;
        int textYpos = y + TEXT_MARGIN_TOP;
        winnerText = new ScreenText(text, textXpos, textYpos, width - 2 * TEXT_MARGIN_LEFT,
                24, 255, 0, 0, 0);

        int buttonIconSize = (int) AndroidUtils.convertDpToPixel(36);
        int buttonSize = (int) AndroidUtils.convertDpToPixel(48);

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

    /**
     * Checks if the retry button was clicked
     * @param x the x position of the click event
     * @param y the y position of the click event
     * @return whether the button was clicked or not
     */
    public boolean isRetryClciked(float x, float y) {
        return retryContainer.contains((int) x, (int) y);
    }

    /**
     * Checks if the main menu button was clicked
     * @param x the x position of the click event
     * @param y the y position of the click event
     * @return whether the button was clicked or not
     */
    public boolean isMainMenuClicked(float x, float y) {
        return mainMenuContainer.contains((int) x, (int) y);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(rect, windowPaint);
        winnerText.draw(canvas);
        canvas.drawRect(retryContainer, buttonPaint);
        canvas.drawRect(mainMenuContainer, buttonPaint);
        retryDrawable.draw(canvas);
        mainMenuDrawable.draw(canvas);
    }
}
