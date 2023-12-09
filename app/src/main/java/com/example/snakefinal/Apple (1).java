package com.example.snakefinal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import java.util.Random;

class Apple extends Food implements Powerup{

    /// Set up the apple in the constructor
    Apple(Context context, Point sr, int s){

        // Make a note of the passed in spawn range
        this.mSpawnRange = sr;
        this.mSize = s;
        // Hide the apple off-screen until the game starts
        this.location = new Point();
        this.location.x = -10;
        this.hasDuration = false;
        this.value = 10;

        this.mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.apple);
        this.mBitmap = Bitmap.createScaledBitmap(this.mBitmap, s, s, false);
        this.spawn();
    }


    @Override
    public void applyMod(SnakeGame game, Snake snake){

        game.increaseScore(value);
        this.spawn();
    }

}