import processing.core.PVector;

import java.util.Random;

public class Obstacle {
    private static PVector[] obstacles = new PVector[]{
            new PVector(40,80),
            new PVector(60,120),
            new PVector(120,80),
            new PVector(180,120)
    };
    private PVector size;
    private float posX;

    public Obstacle(int index){
        size = obstacles[index % obstacles.length];
        posX = Main.getPApplet().width + size.x / 2;
    }

    public Obstacle(){
        size = obstacles[Math.abs(new Random().nextInt()) % obstacles.length];
        posX = Main.getPApplet().width + size.x / 2;
    }

    public PVector getSize() {
        return size;
    }

    public PVector getPos(){
        return new PVector(posX, 0);
    }

    public void move(float speed){
        posX -= speed;
    }

    public boolean collided(Dino dino){
        float playerLeft = dino.getPos().x - dino.getSize().x/2;
        float playerRight = dino.getPos().x + dino.getSize().x/2;
        float thisLeft = posX - size.x / 2 ;
        float thisRight = posX + size.x / 2;

        if ((playerLeft<= thisRight && playerRight >= thisLeft ) || (thisLeft <= playerRight && thisRight >= playerLeft)) {
            float playerDown = dino.getPos().y;
            float thisUp = size.y;
            if (playerDown <= thisUp) {
                return true;
            }
        }
        return false;
    }

}
