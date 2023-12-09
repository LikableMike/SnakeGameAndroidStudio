package com.example.snakefinal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
public class Raspberry extends Food{
    // An image to represent the apple
    Raspberry(Context context, Point sr, int s){
        // Make a note of the passed in spawn range
        this.location = new Point();
        this.mSpawnRange = sr;
        this.mSize = s;
        // Hide the apple off-screen until the game starts
        this.location.x = -10;
        this.value = 10;
        // Load the image to the bitmap
        // Resize the bitmap
        this.mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.raspberry);
        this.mBitmap = Bitmap.createScaledBitmap(this.mBitmap, s, s, false);
        this.spawn();
    }
    @Override
    public void applyMod(SnakeGame game, Snake snake)
    {
        if(snake.getSpeed() < 2 * snake.getSegmentSize()) {
            snake.setSpeed(snake.getSpeed() * 1.1f);
        }
        game.increaseScore(value);
        this.spawn();
    }
}
