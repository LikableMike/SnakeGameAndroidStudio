package com.example.snakefinal;

public class duration {
    private long durationLength;
    private long endTime;
    private boolean active;


    duration(){

    }
    duration(long Duration){
        durationLength = Duration;
        endTime = 0;
        active = false;


    }
    public void activate(long millisPerSecond) {
        endTime = System.currentTimeMillis() + durationLength * millisPerSecond;
        active = true;
    }

    public void checkDuration(){
        if(active){
            if(System.currentTimeMillis() > this.endTime){
                this.active = false;
            }
        }
    }

    public boolean getActive(){
        return this.active;
    }
}
