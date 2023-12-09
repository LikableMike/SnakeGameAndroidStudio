package com.example.snakefinal;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

public class Food{
    protected Point location;
    protected Point mSpawnRange;
    protected int mSize;
    protected Bitmap mBitmap;
    protected int value;

    public void spawn(){
        // Choose two random values and place the apple
        //is now done in food factory
//        Random random = new Random();
        location.x = mSpawnRange.x;
        location.y = mSpawnRange.y;
    }

    //Getters and setters
    public void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(mBitmap,
                location.x , location.y, paint);

    }
    public Point getLocation(){
        return this.location;
    }
    public int getValue(){
        return this.value;
    }

    public void applyMod(SnakeGame game, Snake snake) {}
}
