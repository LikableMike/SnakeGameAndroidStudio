package com.example.snakefinal;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.io.IOException;
import java.util.ArrayList;

class SnakeGame extends SurfaceView implements Runnable{

    // Objects for the game loop/thread
    private Thread mThread = null;
    // Control pausing between updates
    private long mNextFrameTime;
    // Is the game currently playing and or paused?
    private volatile boolean mPlaying = false;

    private volatile GAME_STATE CURRENT_STATE = GAME_STATE.START_SCREEN;

    private Bitmap startScreen;
    private Bitmap helpScreen;
    private Bitmap pauseScreen;
    private Bitmap gameOverScreen;
    private Bitmap backGround;
    private Bitmap border;

    // for playing sound effects
    //make soundn abstract class
    private SoundPool mSP;
    private int mEat_ID = -1;
    private int mCrashID = -1;
    private int mSongID = -1;

    // The size in segments of the playable area
    private final int NUM_BLOCKS_WIDE = 16;
    private int mNumBlocksHigh;
    private int mScore; // How many points does the player have
    private int highScore; // The highest score of this session
    private final SurfaceHolder mSurfaceHolder;
    private final Paint mPaint;
    private final Snake mSnake;
    private FoodFactory foodFactory;
    private ArrayList<Food> FOOD;
    private final int FOOD_TYPES= 5; // # of food types used for random spawning
    private final long TARGET_FPS = 30;
    final long MILLIS_PER_SECOND = 1000;
    private int blockSize;
    private final Point screenSize;
    private final Context gameContext;
    private PauseButton pauseButton;


    // This is the constructor method that gets called
    // from SnakeActivity
    public SnakeGame(Context context, Point size) {
        super(context);
        screenSize = size;
        gameContext = context;
        highScore = 0;

        // Work out how many pixels each block is
        findGameSize(size);
        loadBackground(context);
        loadScreens(context);
        initializeSoundPool(context);

        FOOD = new ArrayList<Food>();
        mSurfaceHolder = getHolder();
        mPaint = new Paint();
        mSnake = makeSnake(context);


    }

    // Called to start a new game
    public void newGame() {

        // reset the snake
        mSnake.reset();
        FOOD.clear();
        FOOD.add(new Apple(gameContext,
                new Point(NUM_BLOCKS_WIDE / 2 * blockSize, mNumBlocksHigh / 2 * blockSize),
                blockSize));

        foodFactory = new FoodFactory(FOOD_TYPES);

        if(mScore / 10 > highScore){
            highScore = mScore / 10;
        }
        // Reset the mScore
        mScore = 0;
        // Setup mNextFrameTime so an update can triggered
        mNextFrameTime = System.currentTimeMillis();
    }

    // Handles the game loop
    @Override
    public void run() {

        while (mPlaying) {
            if(CURRENT_STATE == GAME_STATE.PLAYING_SCREEN) {
                if (updateRequired()) {
                    update();
                }
            }
            draw();
        }
    }

    // Check to see if it is time for an update
    public boolean updateRequired() {
        // Are we due to update the frame
        if(mNextFrameTime <= System.currentTimeMillis()){
            // Setup when the next update will be triggered
            mNextFrameTime =System.currentTimeMillis()
                    + MILLIS_PER_SECOND / TARGET_FPS;

            // Return true so that the update and draw
            // methods are executed
            return true;
        }
        return false;
    }

    // Update all the game objects
    public void update() {
        // Move the snake
        mSnake.move();

        // Did the head of the snake eat the apple?
        for(int i = 0; i < FOOD.size(); i++) {
            if (mSnake.checkDinner(FOOD.get(i).getLocation(), FOOD.get(i))) {
                FOOD.get(i).applyMod(this, mSnake);
                FOOD.remove(FOOD.get(i));
                mSP.play(mEat_ID, 1, 1, 0, 0, 1);
                foodFactory.createFood(FOOD,getmScore(),getContext(), new Point(NUM_BLOCKS_WIDE-1, mNumBlocksHigh-1), blockSize, mSnake);
                // Play a sound
               // mSP.play(mEat_ID, 1, 1, 0, 0, 1);
            }
        }
        // Did the snake die?
        if (mSnake.detectDeath()) {
            // Pause the game ready to start again
            mSP.play(mCrashID, 1, 1, 0, 0, 1);
            CURRENT_STATE = GAME_STATE.GAME_OVER_SCREEN;
        }
    }



    // Do all the drawing
    public void draw() {
        // Get a lock on the mCanvas
        if (mSurfaceHolder.getSurface().isValid()) {
            mPaint.setTextAlign(Paint.Align.CENTER);
            // Objects for drawing
            Canvas mCanvas = mSurfaceHolder.lockCanvas();

            // Fill the screen with a color
            mCanvas.drawBitmap(backGround, null, new Rect(0, 0, mCanvas.getWidth() * 30 / NUM_BLOCKS_WIDE, (mCanvas.getHeight()- mNumBlocksHigh -16) * 17/mNumBlocksHigh), null);

            //Draw a grid for debugging
//            mPaint.setColor(Color.argb(255, 50, 240, 150));
//            for(int i = 0; i < screenSize.x - blockSize; i += blockSize){
//                mCanvas.drawLine(i, blockSize, i, screenSize.y - blockSize, mPaint);
//            }
//            for(int i = 0; i < screenSize.y - blockSize; i += blockSize){
//                mCanvas.drawLine(blockSize, i, screenSize.x - blockSize, i, mPaint);
//            }

            //Draw the game border
            mCanvas.drawBitmap(border, null, new Rect(0, 0, mCanvas.getWidth(),mCanvas.getHeight()), null);

            // Draw the score
            mPaint.setColor(Color.argb(255, 136, 255, 239));
            mPaint.setTextSize(blockSize);
            mCanvas.drawText("" + mScore / 10, mSnake.getSegmentSize() * 2, blockSize, mPaint);

            pauseButton.draw(mCanvas, mPaint);
            // Draw the apple and the snake
            for(int i = 0; i < FOOD.size(); i++) {
                FOOD.get(i).draw(mCanvas, mPaint);
            }
            mSnake.draw(mCanvas, mPaint);
            drawLives(mSnake, mCanvas);

            if(highScore > 0){
                mCanvas.drawText("High Score: " + highScore, screenSize.x / 2f, blockSize, mPaint);
            }

            switch(CURRENT_STATE){
                case START_SCREEN:
                    startScreen(mCanvas);
                    break;

                case TUTORIAL_SCREEN:
                    tutorialScreen(mCanvas);
                    break;

                case PAUSE_SCREEN:
                    pauseScreen(mCanvas);
                    break;

                case GAME_OVER_SCREEN:
                    deathScreen(mCanvas);
                    break;

            }
            // Unlock the mCanvas and reveal the graphics for this frame
            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                switch (CURRENT_STATE){
                    case START_SCREEN:
                        CURRENT_STATE = GAME_STATE.TUTORIAL_SCREEN;
                        return true;
                    case TUTORIAL_SCREEN:
                        CURRENT_STATE = GAME_STATE.PAUSE_SCREEN;
                        mSP.play(mSongID, .5f, .5f, 0, 500, 1);
                        newGame();
                        return true;
                    case GAME_OVER_SCREEN:
                        CURRENT_STATE = GAME_STATE.PAUSE_SCREEN;
                        newGame();
                        return true;
                    case PAUSE_SCREEN:
                        CURRENT_STATE = GAME_STATE.PLAYING_SCREEN;
                        return true;

                }

                if(motionEvent.getX() - pauseButton.getX() <= pauseButton.getButtonSize() &&
                        motionEvent.getY() - pauseButton.getY() <= pauseButton.getButtonSize()){
                    CURRENT_STATE = GAME_STATE.PAUSE_SCREEN;
                    return true;
                }
                // Let the Snake class handle the input

                mSnake.switchHeading(motionEvent);
                break;

            default:
                break;

        }
        return true;
    }


    // Stop the thread
    public void pause() {
        mPlaying = false;
        try {
            mThread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }


    // Start the thread
    public void resume() {
        mPlaying = true;
        mThread = new Thread(this);
        mThread.start();
    }

    public void deathScreen(Canvas mCanvas){
        mCanvas.drawBitmap(gameOverScreen, null, new Rect(0, 0, mCanvas.getWidth(),mCanvas.getHeight()), null);

    }

    public void pauseScreen(Canvas mCanvas){
        mCanvas.drawBitmap(pauseScreen, null, new Rect(0, 0, mCanvas.getWidth(),mCanvas.getHeight()), null);
    }

    public void startScreen(Canvas mCanvas){
        mCanvas.drawBitmap(startScreen, null, new Rect(0, 0, mCanvas.getWidth(),mCanvas.getHeight()), null);
        mCanvas.drawBitmap(border, null, new Rect(0, 0, mCanvas.getWidth(),mCanvas.getHeight()), null);
    }

    public void tutorialScreen(Canvas mCanvas){
        mCanvas.drawBitmap(helpScreen, null, new Rect(0, 0, mCanvas.getWidth(),mCanvas.getHeight()), null);
    }

    public void drawLives(Snake snake, Canvas canvas){
        Bitmap heart = BitmapFactory.decodeResource(gameContext.getResources(), R.drawable.heart);
        heart = Bitmap.createScaledBitmap(heart, snake.getSegmentSize(), snake.getSegmentSize(), false);
        for(int i = 1; i <= snake.getLives(); i++){
            canvas.drawBitmap(heart, canvas.getWidth() - snake.getSegmentSize() * i, 0, mPaint);

        }
    }

    public void findGameSize(Point size){
        blockSize = size.x / NUM_BLOCKS_WIDE;
        mNumBlocksHigh = size.y / blockSize;
    }

    public void loadBackground(Context context){
        backGround = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
        border = BitmapFactory.decodeResource(context.getResources(), R.drawable.border);
        pauseButton = new PauseButton(context, blockSize);

    }

    public void loadScreens(Context context){
        startScreen = BitmapFactory.decodeResource(context.getResources(), R.drawable.startscreen);
        helpScreen = BitmapFactory.decodeResource(context.getResources(), R.drawable.helpscreen);
        pauseScreen = BitmapFactory.decodeResource(context.getResources(), R.drawable.pausescreen);
        gameOverScreen = BitmapFactory.decodeResource(context.getResources(), R.drawable.gameoverscreen);
    }

    public void initializeSoundPool(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            mSP = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            mSP = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }
        try {
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            // Prepare the sounds in memory
            descriptor = assetManager.openFd("EatSound.ogg");
            mEat_ID = mSP.load(descriptor, 0);

            descriptor = assetManager.openFd("DeathSound.ogg");
            mCrashID = mSP.load(descriptor, 0);

            descriptor = assetManager.openFd("Music.ogg");
            mSongID = mSP.load(descriptor, 0);

        } catch (IOException e) {
            // Error
        }
    }

    public Snake makeSnake(Context context){
        return new Snake(context,
                new Point(screenSize.x, screenSize.y),
                blockSize);
    }

    public int getmScore(){
        return this.mScore;
    }

    public void increaseScore(int add){
        this.mScore += add;
    }

}
