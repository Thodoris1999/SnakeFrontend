package com.ttyrovou.snake.sprites;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.ttyrovou.snake.R;

/**
 * Draws red and black apples
 *
 * @author Τυροβούζης Θεόδωρος
 * AEM 9369
 * phone number 6955253435
 * email ttyrovou@ece.auth.gr
 * @author Τσιμρόγλου Στυλιανός
 * AEM 9468
 * phone number 6977030504
 * email stsimrog@ece.auth.gr
 */
public class Apple extends BaseSprite {

    private Context context;
    private Drawable appleSprite;
    private int x, y, width, height;
    private int points;

    public Apple(Context context, int x, int y, int width, int height, int points) {
        this.context = context;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        update(points);
    }

    public Apple(Context context, Rect rect, int points) {
        this.context = context;
        this.x = rect.left;
        this.y = rect.top;
        this.width = rect.right - rect.left;
        this.height = rect.bottom - rect.top;

        update(points);
    }

    public void update(int points) {
        this.points = points;
        if (points > 0) {
            appleSprite = context.getResources().getDrawable(R.drawable.ic_apple);
            appleSprite.setBounds(x, y, x + width, y + height);
        } else if (points < 0) {
            // TODO: change to black apple
            appleSprite = context.getResources().getDrawable(R.drawable.ic_apple_black);
            appleSprite.setBounds(x, y, x + width, y + height);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (points != 0)
            appleSprite.draw(canvas);
    }
}
