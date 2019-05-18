import processing.core.PApplet;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Game {
    //Game
    public static final float groundHeight = 50f;
    public static final float groundBaseSpeed = 10f; // min 8.7
    public static final float deltaT = 1.1547f;// according to current setting(g = -1.5f), deltaT^2 <= 4/3, max 1.1547
    public static final float obstaclesDistance = Dino.dinoWidth + 180f + 550f;
    public static final float minDistance = Dino.dinoWidth + 180f + 310f;
    public static final float gaussSigma = 100f; // 3sigma in gauss distribution
    public static final int nThread = 64;
    private int gen = 1;

    public static ExecutorService executorService;

    private static Game ourInstance = new Game();

    public static Game getInstance() {
        return ourInstance;
    }

    private Game() {}

    private static PApplet parent = Main.getPApplet();
    public Player[] players;
    private LinkedList<Obstacle> obstacles = new LinkedList<>();
    private float speedFix = 0f;

    public float getSpeed(){
        return groundBaseSpeed + speedFix;
    }

    public void init(int population){
        players = new Player[population];
        for(int i = 0; i < population; i++){
            players[i] = new Player();
        }
    }

    public void show(){
        //draw dino
        float maxScore = Float.MIN_VALUE;
        for(Player player : players){
            if(player.isGameOver())
                continue;
            if(player.getScore() > maxScore)
                maxScore = player.getScore();
            parent.fill(0);
            parent.rectMode(parent.CENTER);
            parent.rect(player.getDino().getPos().x, parent.height - groundHeight - player.getDino().getSize().y / 2 - player.getDino().getPos().y, player.getDino().getSize().x,player.getDino().getSize().y);
        }

        parent.fill(0);
        parent.textSize(24);
        parent.text("Score: " + maxScore, 260,30);
        parent.text("Gen: " + gen, 260,60);
//
//        parent.fill(0);
//        parent.rectMode(parent.CENTER);
//        parent.rect(players[0].getDino().getPos().x, parent.height - Share.groundHeight - players[0].getDino().getSize().y / 2 - players[0].getDino().getPos().y, players[0].getDino().getSize().x,players[0].getDino().getSize().y);


        //draw obstacles
        for(Obstacle obstacle : obstacles){
            parent.fill(parent.color(255,0,0));
            parent.rectMode(parent.CENTER);
            parent.rect(obstacle.getPos().x, parent.height - groundHeight - obstacle.getSize().y / 2 - obstacle.getPos().y, obstacle.getSize().x, obstacle.getSize().y);

        }

    }

    private float next = (float) (gaussSigma * new Random().nextGaussian() + obstaclesDistance + obstaclesDistance/ groundBaseSpeed*speedFix);
    public void update() {
        //update map
        //move
        LinkedList<Obstacle> discardObstacle = new LinkedList<>();
        for(Obstacle obstacle : obstacles){
            obstacle.move((groundBaseSpeed + speedFix) * deltaT);
            if(obstacle.getPos().x + obstacle.getSize().x / 2 <= Dino.posX + Dino.dinoDownWidth - minDistance )
                discardObstacle.add(obstacle);
        }
        obstacles.removeAll(discardObstacle);

        //add new
        Obstacle obstacle = obstacles.peekLast();
        Brain.random.setSeed(System.currentTimeMillis());
        if(obstacle != null){
            float prev = obstacle.getPos().x + obstacle.getSize().x / 2;
            if(prev + next <= parent.width){
                if (speedFix >= 1 && Brain.random.nextDouble() <= 0.32) { // 32% of the time add a bird
                    obstacles.add(new Bird());
                } else {//otherwise add a cactus
                    obstacles.add(new Obstacle());
                }
                next = Math.max(minDistance + obstaclesDistance/ groundBaseSpeed*speedFix, (float) (gaussSigma * new Random().nextGaussian() + obstaclesDistance + obstaclesDistance/ groundBaseSpeed*speedFix));
            }
        }else {
            if (speedFix >= 1 && Brain.random.nextDouble() <= 0.32) { // 32% of the time add a bird
                obstacles.add(new Bird());
            } else {//otherwise add a cactus
                obstacles.add(new Obstacle());
            }
        }

        //update player
        executorService = Executors.newFixedThreadPool(nThread);
        for(Player player : players){
            if(player.isGameOver())
                continue;

            executorService.execute(new ComputePlayerUpdate(player, obstacles));
        }
        executorService.shutdown();

        while(!executorService.isTerminated());

        if(speedFix <= 1.3 * groundBaseSpeed)//speed fix limit
            speedFix += 0.002;
    }

    public boolean isGameOver(){
        for(Player player : players){
            if(!player.isGameOver())
                return false;
        }
        return true;
    }

    public Player selectParent(){
        float fitnessSum = 0;
        for(int i = 0; i < players.length; i++){
            fitnessSum += players[i].getScore();
        }
        float rand = parent.random(fitnessSum);

        float runningSum = 0;

        for (int i = 0; i< players.length; i++) {
            runningSum += players[i].getScore();
            if (runningSum > rand) {
                return players[i];
            }
        }

        //should never get to this point

        return players[0];
    }

    public float naturalSelection(){
        Player[] newPlayers = new Player[players.length];//next gen

        //find best one
        float max = players[0].getScore();
        for (int i = 1; i< players.length; i++) {
            if (players[i].getScore() > max) {
                max = players[i].getScore();
            }
        }
        int i = 0;

        for(int j = 0; j < players.length; j++){
            if(players[j].getScore() == max)
                newPlayers[i++] = players[j].clone();
        }



        //System.out.println(mvp.brain);

        for(; i < players.length; i++) {
            //select parent based on fitness
            Player parent = selectParent();

            //get baby from them
            newPlayers[i] = parent.clone();
        }

        players = newPlayers;

        System.out.println(gen + ", " + max);

        return max;
    }

    public void mutateAndPrepareNextGeneration(double rate){
        for(int i = 0; i < players.length; i++) {
            players[i].mutate(rate);
        }
        gen++;
        obstacles.clear();
        speedFix = 0;
    }
}
