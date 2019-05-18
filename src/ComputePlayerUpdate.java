import java.util.LinkedList;

public class ComputePlayerUpdate implements Runnable {
    public ComputePlayerUpdate(Player p, LinkedList<Obstacle> obs){
        player = p;
        obstacles = obs;
    }

    Player player;
    LinkedList<Obstacle> obstacles;

    @Override
    public void run() {
        //get action
        player.think(obstacles);

        //move
        player.getDino().move();

        //update
        player.checkAndUpdate(obstacles);
    }
}
