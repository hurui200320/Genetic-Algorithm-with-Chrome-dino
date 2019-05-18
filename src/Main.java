import processing.core.PApplet;

public class Main extends PApplet {
    //Program
    public static final int sizeX = 1200;
    public static final int sizeY = 420;
    public static final float fps = 320.0f;

    private Main(){}
    private static PApplet pApplet = new Main();
    public static PApplet getPApplet(){
        return pApplet;
    }

    @Override
    public void settings() {
        size(sizeX, sizeY);
    }


    Game game;

    @Override
    public void setup(){
        frameRate(fps);
        game = Game.getInstance();
        game.init(2000);
    }

    @Override
    public void draw(){
        background(255);

        //draw ground
        fill(0);
        line(0,height - Game.groundHeight, width, height - Game.groundHeight);

        if(game.isGameOver()){
            game.naturalSelection();
            game.mutateAndPrepareNextGeneration(0.015);
        }else {
            game.update();
            game.show();
        }
    }

    public static void main(String[] args) {
        String[] processingArgs = {"Genetic Algorithm"};
        PApplet.runSketch(processingArgs, pApplet);
    }
}
