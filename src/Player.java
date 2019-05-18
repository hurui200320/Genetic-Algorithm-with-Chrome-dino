import java.util.LinkedList;

public class Player {
    private Dino dino = new Dino();
    private boolean gameOver = false;
    private float score = 0;
    private Brain brain;

    public Player(){
        brain = new Brain();
        brain.randomWeight();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public float getScore() {
        return score;
    }

    public Dino getDino(){
        return dino;
    }

    public void think(LinkedList<Obstacle> obstacles){
        brain.lookAround(obstacles, dino);
        boolean[] actions = brain.computeAction();
        if(actions[0] && actions[1])
            dino.bigJump();
        if(actions[0] && !actions[1])
            dino.smallJump();
        dino.setDown(actions[2]);
    }

    public void checkAndUpdate(LinkedList<Obstacle> obstacles){
        if(gameOver)
            return;
        for(Obstacle obstacle : obstacles){
            if(obstacle.collided(dino)){
                gameOver = true;
                return;
            }
        }
        score += 0.01;
    }

    @Override
    public Player clone(){
        Player p = new Player();
        p.brain = brain.clone();
        return p;
    }

    public void mutate(double rate){//chance that any vector in directions gets changed
        brain.mutate(rate);
    }
}
