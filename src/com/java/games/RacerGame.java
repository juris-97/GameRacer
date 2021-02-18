package com.java.games;

import com.java.games.road.RoadManager;
import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;
import com.javarush.engine.cell.Key;

public class RacerGame extends Game {
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    public static final int CENTER_X = WIDTH / 2;
    public static final int ROADSIDE_WIDTH = 14;
    private static final int RACE_GOAL_CARS_COUNT = 40;
    private int score;

    private ProgressBar progressBar;
    private boolean isGameStopped;
    private RoadMarking roadMarking;
    private PlayerCar player;
    private RoadManager roadManager;
    private FinishLine finishLine;

    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        showGrid(false);
        createGame();
    }

    private void createGame(){
        score = 3500;
        isGameStopped = false;
        player = new PlayerCar();
        roadMarking = new RoadMarking();
        roadManager = new RoadManager();
        finishLine = new FinishLine();
        progressBar = new ProgressBar(RACE_GOAL_CARS_COUNT);
        setTurnTimer(40);
        drawScene();
    }

    private void drawScene(){
        drawField();
        roadManager.draw(this);
        roadMarking.draw(this);
        player.draw(this);
        finishLine.draw(this);
        progressBar.draw(this);

    }

    private void drawField(){
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {

                if(x == CENTER_X)
                    setCellColor(x,y, Color.WHITE);
                else if(x >= ROADSIDE_WIDTH && x < WIDTH - ROADSIDE_WIDTH)
                    setCellColor(x, y, Color.DIMGREY);
                else
                    setCellColor(x,y, Color.GREEN);

            }
        }
    }

    private void moveAll(){
        roadMarking.move(player.speed);
        roadManager.move(player.speed);
        player.move();
        finishLine.move(player.speed);
        progressBar.move(roadManager.getPassedCarsCount());
    }

    @Override
    public void setCellColor(int x, int y, Color color) {
        if(x >= WIDTH || x < 0)
            return;
        if(y >= HEIGHT || y < 0)
            return;

        super.setCellColor(x, y, color);
    }

    @Override
    public void onTurn(int step) {

        if(roadManager.checkCrush(player)){
            gameOver();
            drawScene();
            return;
        }

        if(roadManager.getPassedCarsCount() >= RACE_GOAL_CARS_COUNT)
            finishLine.show();

        if(finishLine.isCrossed(player)){
            win();
            drawScene();
            return;
        }
        moveAll();
        score -= 5;
        setScore(score);
        roadManager.generateNewRoadObjects(this);
        drawScene();
    }

    @Override
    public void onKeyPress(Key key) {
        if(key == Key.LEFT){
            player.setDirection(Direction.LEFT);
        }else if(key == Key.RIGHT){
            player.setDirection(Direction.RIGHT);
        }else if(key == Key.UP){
            player.speed = 2;
        }else if(key == Key.SPACE && isGameStopped){
            createGame();
        }
    }

    @Override
    public void onKeyReleased(Key key) {
        if(key.equals(Key.RIGHT) && player.getDirection().equals(Direction.RIGHT)){
            player.setDirection(Direction.NONE);
        }else if(key.equals(Key.LEFT) && player.getDirection().equals(Direction.LEFT)){
            player.setDirection(Direction.NONE);
        }else if(key.equals(Key.UP)){
            player.speed = 1;
        }
    }

    private void gameOver(){
        isGameStopped = true;
        showMessageDialog(Color.BLACK, "GAME OVER!" ,Color.RED, 50);
        stopTurnTimer();
        player.stop();
    }

    private void win(){
        isGameStopped = true;
        showMessageDialog(Color.ALICEBLUE, "YOU WIN THE GAME", Color.BLACK, 50);
        stopTurnTimer();
    }
}
