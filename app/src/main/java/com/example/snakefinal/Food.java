package com.example.snakefinal;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import java.util.Random;

public class Food{
    protected boolean hasDuration;
    protected Point location;
    protected Point mSpawnRange;
    protected int mSize;
    protected Bitmap mBitmap;
    protected int value;
    protected duration Duration;

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
    public void checkDuration(SnakeGame game){}
    public Bitmap getmBitmap(){
        return this.mBitmap;
    }
    public int getmSize(){
        return this.mSize;
    }
    public Point getmSpawnRange(){
        return this.mSpawnRange;
    }
    public Point getLocation(){
        return this.location;
    }
    public boolean findDuration(){
        return this.hasDuration;
    }
    public int getValue(){
        return this.value;
    }

    public void applyMod(SnakeGame game, Snake snake) {}
}
