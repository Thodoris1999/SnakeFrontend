package com.ttyrovou.snake.sprites;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import com.ttyrovou.snake.R;

import timber.log.Timber;

/**
 * Draws ladders, if they are not broken
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
public class Ladder extends BaseSprite {

    private Bitmap ladderBitmap;
    private Rect bitmapPosition;
    private boolean broken = false;

    public Ladder(Context context, Rect lowStep, Rect highStep) {
        // calculate bitmap length and rotation
        double ladderHeight = (int) Math.hypot(highStep.left - lowStep.left, highStep.top - lowStep.top);
        double ladderWidth = (int) ((lowStep.right - lowStep.left) * 0.5);
        double ladderRotation = 0;
        if (lowStep.left - highStep.left != 0) {
            ladderRotation += Math.atan((highStep.left - lowStep.left) / (double) (lowStep.top - highStep.top));
        }

        Bitmap sourceLadder = BitmapFactory.decodeResource(context.getResources(), R.drawable.medium_ladder);
        Matrix matrix = new Matrix();
        matrix.postRotate((float) Math.toDegrees(ladderRotation));
        Bitmap scaledLadder = Bitmap.createScaledBitmap(sourceLadder, (int) ladderWidth, (int) ladderHeight, true);
        ladderBitmap = Bitmap.createBitmap(scaledLadder, 0, 0, scaledLadder.getWidth(),
                scaledLadder.getHeight(), matrix, true);

        int horizontalOffset = (int) ((highStep.right - highStep.left - ladderWidth * Math.cos(ladderRotation)) / 2);
        int verticalOffset = (int) ((highStep.bottom - highStep.top - ladderWidth * Math.sin(ladderRotation)) / 2);
        if (ladderRotation >= 0) {
            bitmapPosition = new Rect(highStep.left - (int) (ladderHeight * Math.sin(ladderRotation)) + horizontalOffset,
                    highStep.top + verticalOffset,
                    highStep.left - (int) (ladderHeight * Math.sin(ladderRotation)) + horizontalOffset + ladderBitmap.getWidth(),
                    highStep.top + verticalOffset + ladderBitmap.getHeight());
        } else {
            bitmapPosition = new Rect(highStep.left + horizontalOffset,
                    highStep.top + (int) (ladderWidth * Math.sin(ladderRotation)) + verticalOffset,
                    highStep.left + ladderBitmap.getWidth() + horizontalOffset,
                    highStep.top + (int) (ladderWidth * Math.sin(ladderRotation)) + ladderBitmap.getHeight() + verticalOffset);
        }
    }

    public void update(boolean broken) {
        this.broken = broken;
    }

    @Override
    public void draw(Canvas canvas) {
        if (!broken)
            canvas.drawBitmap(ladderBitmap, null, bitmapPosition, null);
    }
}
