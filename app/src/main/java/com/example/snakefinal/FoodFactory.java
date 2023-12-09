package com.example.snakefinal;
import android.content.Context;
import android.graphics.Point;
import java.util.ArrayList;
import java.util.Random;

public class FoodFactory {
    private final int foodTypes;

    FoodFactory(int foodTypes){
        this.foodTypes = foodTypes;
    }

    public void createFood(ArrayList<Food> foodList, int score, Context context, Point point, int blockSize, Snake snake){
        for(int i = foodList.size(); i < score / 50 + 1; i++) {
            if(i > 5){
                break;
            }
            Random rand = new Random();
            int food = rand.nextInt(6) + 1;
            switch (food) {
                case 1:
                    foodList.add(new Apple(context, findSpawn(foodList, point.x, point.y, blockSize,snake), blockSize));
                    break;
                case 2:
                    foodList.add(new Banana(context, findSpawn(foodList, point.x, point.y, blockSize,snake), blockSize));
                    break;
                case 3:
                    foodList.add(new Raspberry(context, findSpawn(foodList, point.x, point.y, blockSize,snake), blockSize));
                    break;
                case 4:
                    foodList.add(new GoldenApple(context, findSpawn(foodList, point.x, point.y, blockSize,snake), blockSize));
                    break;
                case 5:
                    foodList.add(new badapple(context, findSpawn(foodList, point.x, point.y, blockSize,snake), blockSize));
                    break;
                case 6:
                    if(score > 100)
                        foodList.add(new Bomb(context, findSpawn(foodList, point.x, point.y, blockSize, snake), blockSize));
                    createFood(foodList, score, context, point, blockSize ,snake);
                    break;

                    default:
                    break;
            }
        }
    }
    public Point findSpawn(ArrayList<Food> foodList, int x, int y, int blockSize ,Snake snake){
        Point spawn = new Point();
        Random random = new Random();
        spawn.x = (random.nextInt(x - 2) + 2) * blockSize;
        spawn.y = (random.nextInt(y - 2) + 2) * blockSize;

        ArrayList<Point> takenSpawns = new ArrayList<>();
        for(int i = 0; i < foodList.size(); i++) {
            takenSpawns.add(new Point(foodList.get(i).getLocation().x, foodList.get(i).getLocation().y));
        }

        takenSpawns.addAll(snake.getSegmentLocations());

        for(int i = 0; i < takenSpawns.size(); i++){
            if((Math.abs(spawn.x - takenSpawns.get(i).x) <= blockSize / 2) &&
                    (Math.abs(spawn.y - takenSpawns.get(i).y) <= blockSize / 2)){
                return findSpawn(foodList, x, y, blockSize, snake);
            }
        }
        return spawn;
    }
}
