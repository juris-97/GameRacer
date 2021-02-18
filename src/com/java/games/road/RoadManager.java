package com.java.games.road;

import com.java.games.GameObject;
import com.java.games.PlayerCar;
import com.java.games.RacerGame;
import com.javarush.engine.cell.Game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RoadManager {
    public static final int LEFT_BORDER = RacerGame.ROADSIDE_WIDTH;
    public static final int RIGHT_BORDER = RacerGame.WIDTH - LEFT_BORDER;

    private int passedCarsCount = 0;
    private static final int FIRST_LANE_POSITION = 16;
    private static final int FOURTH_LANE_POSITION = 44;
    private static final int PLAYER_CAR_DISTANCE = 12;
    private List<RoadObject> items = new ArrayList<>();

    private RoadObject createRoadObject(RoadObjectType type, int x, int y){
        if(type.equals(RoadObjectType.THORN)){
            return new Thorn(x, y);
        }else if(type.equals(RoadObjectType.DRUNK_CAR)){
            return new MovingCar(x, y);
        }else {
            return new Car(RoadObjectType.CAR, x,y);
        }
    }

    private void addRoadObject(RoadObjectType type, Game game){
        int x = game.getRandomNumber(FIRST_LANE_POSITION, FOURTH_LANE_POSITION);
        int y = -1 * RoadObject.getHeight(type);

        RoadObject obj = createRoadObject(type, x, y);
        if(obj != null && isRoadSpaceFree(obj)){
            items.add(obj);
        }
    }

    public void draw(Game game){
        for(RoadObject obj : items)
            obj.draw(game);
    }

    public void move(int boost){
        for(RoadObject obj : items)
            obj.move(boost + obj.speed, items);
        deletePassedItems();
    }

    private boolean isThornExists(){
        for (RoadObject obj : items){
            if(obj.type.equals(RoadObjectType.THORN))
                return true;
        }

        return false;
    }

    private void generateThorn(Game game){
        int rand = game.getRandomNumber(100);

        if(!isThornExists() && rand < 10){
            addRoadObject(RoadObjectType.THORN, game);
        }
    }

    public void generateNewRoadObjects(Game game){
        generateThorn(game);
        generateRegularCar(game);
        generateMovingCar(game);
    }

    private void deletePassedItems(){
        Iterator<RoadObject> iterator = items.iterator();

        while(iterator.hasNext()){
            RoadObject obj = iterator.next();
            if(obj.y >= RacerGame.HEIGHT){
                iterator.remove();
                if(!obj.type.equals(RoadObjectType.THORN))
                    passedCarsCount++;
            }
        }
    }

    public boolean checkCrush(PlayerCar playerCar){
        for (GameObject obj : items){
            if(obj.isCollision(playerCar)){
                return true;
            }
        }
        return false;
    }

    private void generateRegularCar(Game game){
        int rand = game.getRandomNumber(100);
        int carTypeNumber = game.getRandomNumber(4);

        if(rand < 30){
            addRoadObject(RoadObjectType.values()[carTypeNumber], game);
        }
    }

    private boolean isRoadSpaceFree(RoadObject object){
        for(RoadObject obj : items){
            if(obj.isCollisionWithDistance(object, PLAYER_CAR_DISTANCE)){
                return false;
            }
        }

        return true;
    }

    private boolean isMovingCarExists(){
        for (RoadObject obj : items){
            if(obj.type.equals(RoadObjectType.DRUNK_CAR))
                return true;
        }

        return false;
    }

    private void generateMovingCar(Game game){
        int rand = game.getRandomNumber(100);
        if(rand < 10 && !isMovingCarExists()){
            addRoadObject(RoadObjectType.DRUNK_CAR, game);
        }
    }

    public int getPassedCarsCount() {
        return passedCarsCount;
    }
}
