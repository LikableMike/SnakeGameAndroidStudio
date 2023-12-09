package com.example.snakefinal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public class badapple extends Food{
    badapple(Context context, Point sr, int s){

        // Make a note of the passed in spawn range
        this.mSpawnRange = sr;
        this.mSize = s;
        // Hide the apple off-screen until the game starts
        this.location = new Point();
        this.location.x = -10;
        this.hasDuration = false;
        this.value = 0;

        this.mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.badapple);
        this.mBitmap = Bitmap.createScaledBitmap(this.mBitmap, s, s, false);
        this.spawn();
    }


    @Override
    public void applyMod(SnakeGame game, Snake snake){

        game.increaseScore(value);
        if(snake.getSpeed() > snake.getSegmentSize() / 12f) {
            snake.setSpeed(snake.getSpeed() * .9f);
        }
        this.spawn();
    }
}
