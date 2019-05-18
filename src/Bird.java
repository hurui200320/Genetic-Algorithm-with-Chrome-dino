import processing.core.PVector;

import java.util.Random;

public class Bird extends Obstacle {
    private static PVector size = new PVector(92,80);
    private PVector pos;
    private float posX, posY;
    private static float[] posYs = new float[]{40f, 80f, 200f};

    public Bird(int index){
        posX = Main.getPApplet().width + size.x / 2;
        posY = posYs[index % posYs.length];
    }

    public Bird(){
        posX = Main.getPApplet().width + size.x / 2;
        posY = posYs[Math.abs(new Random().nextInt()) % posYs.length];
    }

    public PVector getSize() {
        return size;
    }

    public PVector getPos(){
        return new PVector(posX, posY);
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
            float playerUp = playerDown + dino.getSize().y / 2;
            float thisUp = posY + size.y / 2;
            float thisDown = posY - size.y / 2;
            if (playerDown <= thisUp && playerUp >= thisDown) {
                return true;
            }
        }
        return false;
    }
}
