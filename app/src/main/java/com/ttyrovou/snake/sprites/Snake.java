package com.ttyrovou.snake.sprites;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import com.ttyrovou.snake.R;

public class Snake extends BaseSprite {

    private Bitmap snakeBitmap;
    private Rect bitmapPosition;

    public Snake(Context context, Rect tail, Rect head) {
        // calculate bitmap length and rotation
        double snakeHeight = (int) Math.hypot(head.left - tail.left, head.top - tail.top);
        double snakeWidth = (int) ((tail.right - tail.left) * 0.5);
        double snakeRotation = 0;
        if (tail.left - head.left != 0) {
            snakeRotation += Math.atan((head.left - tail.left) / (double) (tail.top - head.top));
        }

        Bitmap sourceSnake = BitmapFactory.decodeResource(context.getResources(), R.drawable.snake);
        Matrix matrix = new Matrix();
        matrix.postRotate((float) Math.toDegrees(snakeRotation));
        Bitmap scaledSnake = Bitmap.createScaledBitmap(sourceSnake, (int) snakeWidth, (int) snakeHeight, true);
        snakeBitmap = Bitmap.createBitmap(scaledSnake, 0, 0, scaledSnake.getWidth(),
                scaledSnake.getHeight(), matrix, true);

        int horizontalOffset = (int) ((head.right - head.left - snakeWidth * Math.cos(snakeRotation)) / 2);
        int verticalOffset = (int) ((head.bottom - head.top - snakeWidth * Math.sin(snakeRotation)) / 2);
        if (snakeRotation >=0) {
            bitmapPosition = new Rect(head.left - (int) (snakeHeight * Math.sin(snakeRotation)) + horizontalOffset,
                    head.top + verticalOffset,
                    head.left - (int) (snakeHeight * Math.sin(snakeRotation)) + horizontalOffset + snakeBitmap.getWidth(),
                    head.top + verticalOffset + snakeBitmap.getHeight());
        } else {
            bitmapPosition = new Rect(head.left + horizontalOffset,
                    head.top + (int) (snakeWidth * Math.sin(snakeRotation)) + verticalOffset,
                    head.left + snakeBitmap.getWidth() + horizontalOffset,
                    head.top + (int) (snakeWidth * Math.sin(snakeRotation)) + snakeBitmap.getHeight() + verticalOffset);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(snakeBitmap, null, bitmapPosition, null);
    }
}
