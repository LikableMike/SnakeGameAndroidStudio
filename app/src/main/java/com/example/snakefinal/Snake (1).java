package com.example.snakefinal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;

import java.util.ArrayList;

class Snake {

    private float Speed;

    int lives;
    // The location in the grid of all the segments
    private ArrayList<Point> segmentLocations;

    // How big is each segment of the snake?
    private int mSegmentSize;

    // How big is the entire grid
    private Point mMoveRange;

    // Where is the centre of the screen
    // horizontally in pixels?
    private int halfWayPoint;

    // For tracking movement Heading
    private enum Heading {
        UP, RIGHT, DOWN, LEFT
    }

    // Start by heading to the right
    private Heading heading = Heading.RIGHT;
    private Heading nextHeading = Heading.RIGHT;

    // A bitmap for each direction the head can face
    private Bitmap mBitmapHeadRight;
    private Bitmap mBitmapHeadLeft;
    private Bitmap mBitmapHeadUp;
    private Bitmap mBitmapHeadDown;

    // A bitmap for the body
    private Bitmap mBitmapBody;
    private Bitmap mBitmapBody2;


    Snake(Context context, Point mr, int ss, int NUM_BLOCKS_WIDE, int mNumBlocksHigh) {



        // Initialize our ArrayList
        segmentLocations = new ArrayList<>();

        // Initialize the segment size and movement
        // range from the passed in parameters
        mSegmentSize = ss;
        mMoveRange = mr;
        this.Speed = ss / 5f;
        this.lives = 3;
        // Create and scale the bitmaps
        mBitmapHeadRight = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.head);

        // Create 3 more versions of the head for different headings
        mBitmapHeadLeft = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.head);

        mBitmapHeadUp = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.head);

        mBitmapHeadDown = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.head);

        // Modify the bitmaps to face the snake head
        // in the correct direction
        mBitmapHeadRight = Bitmap
                .createScaledBitmap(mBitmapHeadRight,
                        ss, ss, false);

        // A matrix for scaling
        Matrix matrix = new Matrix();
        matrix.preScale(-1, 1);

        mBitmapHeadLeft = Bitmap
                .createBitmap(mBitmapHeadRight,
                        0, 0, ss, ss, matrix, true);

        // A matrix for rotating
        matrix.preRotate(-90);
        mBitmapHeadUp = Bitmap
                .createBitmap(mBitmapHeadRight,
                        0, 0, ss, ss, matrix, true);

        // Matrix operations are cumulative
        // so rotate by 180 to face down
        matrix.preRotate(180);
        mBitmapHeadDown = Bitmap
                .createBitmap(mBitmapHeadRight,
                        0, 0, ss, ss, matrix, true);

        // Create and scale the body
        mBitmapBody = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.body);
        mBitmapBody2 = BitmapFactory
                .decodeResource(context.getResources(),
                        R.drawable.body2);

        mBitmapBody = Bitmap
                .createScaledBitmap(mBitmapBody,
                        ss, ss, false);
        mBitmapBody2 = Bitmap
                .createScaledBitmap(mBitmapBody2,
                        ss, ss, false);

        // The halfway point across the screen in pixels
        // Used to detect which side of screen was pressed
        halfWayPoint = mr.x / 2;
    }

    // Get the snake ready for a new game
    void reset(int w, int h) {

        // Reset the heading
        heading = Heading.RIGHT;
        nextHeading = heading;
        lives = 3;
        // Delete the old contents of the ArrayList
        segmentLocations.clear();

        // Start with a single snake segment
        segmentLocations.add(new Point(mSegmentSize, mSegmentSize));
        Speed = mSegmentSize / 8f;
    }


    void move() {
        // Move the body
        // Start at the back and move it
        // to the position of the segment in front of it
        for (int i = segmentLocations.size() - 1; i > 0; i--) {
            segmentLocations.get(i).x = segmentLocations.get(i - 1).x;
            segmentLocations.get(i).y = segmentLocations.get(i - 1).y;
        }

        // Move the head in the appropriate heading
        // Get the existing head position
        Point p = segmentLocations.get(0);


        // Move it appropriately
        switch (heading) {
            case UP:
                if(p.y%mSegmentSize <= Speed && nextHeading != heading){
                    p.y-= p.y%mSegmentSize;
                    heading = nextHeading;

                }else{
                    p.y-= Speed;

                }
                break;

            case RIGHT:
                if(p.x%mSegmentSize>=mSegmentSize - Speed && nextHeading != heading){
                    p.x+=mSegmentSize - p.x%mSegmentSize;
                    heading = nextHeading;

                }else{
                    p.x+=Speed;
                }
                break;

            case DOWN:
                if(p.y%mSegmentSize>=mSegmentSize - Speed && nextHeading != heading){
                    p.y+=mSegmentSize - p.y%mSegmentSize;
                    heading = nextHeading;

                }else{
                    p.y+=Speed;

                }
                break;

            case LEFT:
                if(p.x%mSegmentSize<=Speed && nextHeading != heading){
                    p.x-=p.x%mSegmentSize;
                    heading = nextHeading;

                }else{
                    p.x-=Speed;

                }
                break;
        }

    }

    boolean detectDeath() {
        // Has the snake died?
        boolean dead = false;

        if(this.lives <= 0){
            dead =  true;}

        // Hit any of the screen edges
        if (segmentLocations.get(0).x <= mSegmentSize - 1 ||
                segmentLocations.get(0).x > mMoveRange.x - mSegmentSize * 2 ||
                segmentLocations.get(0).y <= mSegmentSize - 1 ||
                segmentLocations.get(0).y > mMoveRange.y - mSegmentSize * 2) {

            dead = true;
        }

        // Eaten itself?
        if(segmentLocations.size() > 9) {
            for (int i = segmentLocations.size() - 1;  i > 8; i--) {
                // Have any of the sections collided with the head
                if (Math.abs(segmentLocations.get(0).x - segmentLocations.get(i).x) <= Speed / 2 &&
                        Math.abs(segmentLocations.get(0).y - segmentLocations.get(i).y) <= Speed / 2) {

                    dead = true;
                }
            }
        }
        return dead;
    }

    boolean checkDinner(Point l, Food food) {
        //if (snakeXs[0] == l.x && snakeYs[0] == l.y) {
        if (Math.abs(segmentLocations.get(0).x - l.x) <= mSegmentSize / 2 &&
                Math.abs(segmentLocations.get(0).y - l.y) <= mSegmentSize / 2 ) {

            // Add a new Point to the list
            // located off-screen.
            // This is OK because on the next call to
            // move it will take the position of
            // the segment in front of it

            //New: switch segmentLocations.add with food.applyMod
            for(int i = 0; i < food.getValue(); i++) {
                segmentLocations.add(new Point(-100, -100));
            }
            return true;
        }
        return false;
    }

    void draw(Canvas canvas, Paint paint) {

        int colorLength = 0;
        for (int i = 1; i < segmentLocations.size(); i++) {
            if(colorLength < 4){
                canvas.drawBitmap(mBitmapBody, segmentLocations.get(i).x, segmentLocations.get(i).y, paint);
            }else {
                canvas.drawBitmap(mBitmapBody2, segmentLocations.get(i).x, segmentLocations.get(i).y, paint);
            }
            colorLength++;
            if(colorLength > 7){
                colorLength = 0;
            }


        }

        // Don't run this code if ArrayList has nothing in it
        if (!segmentLocations.isEmpty()) {
            // All the code from this method goes here
            // Draw the head
            switch (heading) {
                case RIGHT:
                    canvas.drawBitmap(mBitmapHeadRight,
                            segmentLocations.get(0).x
                                    ,
                            segmentLocations.get(0).y
                                    , paint);
                    break;

                case LEFT:
                    canvas.drawBitmap(mBitmapHeadLeft,
                            segmentLocations.get(0).x
                                    ,
                            segmentLocations.get(0).y
                                    , paint);
                    break;

                case UP:
                    canvas.drawBitmap(mBitmapHeadUp,
                            segmentLocations.get(0).x
                                    ,
                            segmentLocations.get(0).y
                                    , paint);
                    break;

                case DOWN:
                    canvas.drawBitmap(mBitmapHeadDown,
                            segmentLocations.get(0).x
                                    ,
                            segmentLocations.get(0).y
                                   , paint);
                    break;
            }

        }
    }


    // Handle changing direction
    void switchHeading(MotionEvent motionEvent) {

        // Is the tap on the right hand side?
        if (motionEvent.getX() >= halfWayPoint) {
            switch (heading) {
                // Rotate right
                case UP:
                    nextHeading = Heading.RIGHT;
                    break;
                case RIGHT:
                    nextHeading = Heading.DOWN;
                    break;
                case DOWN:
                    nextHeading = Heading.LEFT;
                    break;
                case LEFT:
                    nextHeading = Heading.UP;
                    break;

            }
        } else {
            // Rotate left
            switch (heading) {
                case UP:
                    nextHeading = Heading.LEFT;
                    break;
                case LEFT:
                    nextHeading = Heading.DOWN;
                    break;
                case DOWN:
                    nextHeading = Heading.RIGHT;
                    break;
                case RIGHT:
                    nextHeading = Heading.UP;
                    break;
            }
        }
    }

    public void setSpeed(float speed){
        this.Speed = speed;
    }

    public float getSpeed(){
        return this.Speed;
    }

    public int getSegmentSize(){
        return this.mSegmentSize;
    }

    public void Die(){

    }

    public int getLives(){
        return this.lives;
    }

    public void setLives(int add){
        this.lives = add;
    }
}
