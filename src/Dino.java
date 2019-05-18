import processing.core.PVector;

public class Dino {

    //Dino
    public static final float dinoHeight = 112f;
    public static final float dinoWidth = 96f;
    public static final float dinoDownHeight = 68f;
    public static final float dinoDownWidth = 136f;

    public final static float posX = 150f;
    private float posY = 0f; //reference to the ground
    private float velY = 0f; //v y
    private float gravity = -0.78f;

    private float smallJumpVel = (float) Math.sqrt(298);//max 160
    private float bigJumpVel = (float)(10.0 * Math.sqrt(3.85));//max 200
    private boolean isDown = false;

    public PVector getPos() {
        return new PVector(posX, posY);
    }

    public PVector getSize(){
        if(isDown && posY == 0 && velY == 0){
            return new PVector(dinoDownWidth, dinoDownHeight);
        }else {
            return new PVector(dinoWidth, dinoHeight);
        }
    }

    public void move(){
//        if(posY != 0 || isDown)
//            System.out.println("Y: " + posY + " V: " + velY + " D: " + isDown);
        if(isDown)
            return;
        //posY == 0 means on the ground, then velY > 0 means wants to jump
        //or, posY > 0 means in the air
        if((posY == 0 && velY > 0) || posY > 0){//needs to update
            posY += velY * Game.deltaT + 0.5f * gravity * Game.deltaT * Game.deltaT;
            //y = v0*t + (a*t*t)/2 + y0
            velY += gravity * Game.deltaT;
            //vy = v0 + a*t
        }

        if(posY < 0){ // hit the ground
            posY = 0;
            velY = 0;
        }

    }

    public void smallJump(){
        if(posY == 0 && velY == 0 && !isDown){//only jump when on the ground
            velY = smallJumpVel;
        }
    }
    public void bigJump(){
        if(posY == 0 && velY == 0 && !isDown){//only jump when on the ground
            velY = bigJumpVel;
        }
    }

    public void setDown(boolean status){
        isDown = status && posY == 0 && velY == 0;
    }

    @Override
    public String toString() {
        return "Dino{" +
                "posY=" + posY +
                ", velY=" + velY +
                ", gravity=" + gravity +
                ", smallJumpVel=" + smallJumpVel +
                ", bigJumpVel=" + bigJumpVel +
                ", isDown=" + isDown +
                '}';
    }
}
