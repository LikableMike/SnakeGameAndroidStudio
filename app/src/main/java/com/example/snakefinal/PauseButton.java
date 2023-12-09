package com.example.snakefinal;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;


public class PauseButton {
    private final Point location;
    private final int buttonSize;
    private Bitmap bitmap;

    PauseButton(Context context, int size){
        this.buttonSize = size / 5 * 3;
        this.location = new Point(size/4, size/4);
        this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pause);
        this.bitmap = Bitmap.createScaledBitmap(this.bitmap, this.buttonSize, this.buttonSize, false);
    }

    public void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(this.bitmap, this.location.x, this.location.y, paint);
    }

    public int getX(){
        return this.location.x;
    }

    public int getY(){
        return this.location.y;
    }

    public int getButtonSize(){
        return this.buttonSize;
    }
}
