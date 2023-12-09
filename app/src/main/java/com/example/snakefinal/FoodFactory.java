package com.example.snakefinal;
import android.content.Context;
import android.graphics.Point;
import java.util.ArrayList;
import java.util.Random;

public class FoodFactory {
    private int foodTypes;

    FoodFactory(int foodTypes){
        this.foodTypes = foodTypes;
    }

    public void createFood(ArrayList<Food> foodList, int score, Context context, Point point, int blockSize){
        for(int i = foodList.size(); i < score / 50 + 1; i++) {
            Random rand = new Random();
            int food = rand.nextInt(6) + 1;
            switch (food) {
                case 1:
                    foodList.add(new Apple(context, findSpawn(foodList, point.x, point.y), blockSize));
                    break;
                case 2:
                    foodList.add(new Banana(context, findSpawn(foodList, point.x, point.y), blockSize));
                    break;
                case 3:
                    foodList.add(new Raspberry(context, findSpawn(foodList, point.x, point.y), blockSize));
                    break;
                case 4:
                    foodList.add(new GoldenApple(context, findSpawn(foodList, point.x, point.y), blockSize));
                    break;
                case 5:
                    foodList.add(new badapple(context, findSpawn(foodList, point.x, point.y), blockSize));
                    break;
                case 6:
                    if(score > 50)
                        foodList.add(new Bomb(context, findSpawn(foodList, point.x, point.y), blockSize));
                    createFood(foodList, score, context, point, blockSize);
                    break;

                    default:
                    break;
            }
        }
    }
    public Point findSpawn(ArrayList<Food> foodList, int x, int y){
        Point spawn = new Point(x, y);
        for(int i = 0; i < foodList.size(); i++){
            if(spawn.x == foodList.get(i).getLocation().x &&
               spawn.y == foodList.get(i).getLocation().y){
                return findSpawn(foodList, x, y);
            }
        }
        return spawn;

    }
    public int getFoodTypes(){
        return this.foodTypes;
    }
}
