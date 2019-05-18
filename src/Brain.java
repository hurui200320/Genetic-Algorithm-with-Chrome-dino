import java.util.LinkedList;
import java.util.Random;

public class Brain {
    private Node[] output = new Node[3];// is Jump, big or not, is duck
    
    public static Random random = new Random();

    private InputNode[] inputLayer = new InputNode[11];
    //next obstacle posX
    //posY
    //sizeX
    //sizeY
    //ground speed
    //player's posY
    //next two obstacle posX
    //posY
    //sizeX
    //sizeY
    //bias

    private Node[] middles = new Node[5];
    private double bias;

    public Brain(){
        for(int i = 0; i < inputLayer.length; i++){
            inputLayer[i] = new InputNode();
        }
        for(int i = 0; i < middles.length; i++){
            middles[i] = new Node();
            middles[i].setInput(inputLayer);
        }
        for(int i = 0; i < output.length; i++){
            output[i] = new Node();
            output[i].setInput(middles);
        }
    }

    public double nextDouble(){
        random.setSeed(System.currentTimeMillis());
        if(random.nextBoolean())
            return random.nextDouble()*random.nextInt(10);
        else
            return -random.nextDouble()*random.nextInt(10);
    }

    public void randomWeight(){
        for(Node out: output)
            for(int i = 0; i < out.weights.length; i++)
                out.weights[i] = nextDouble();
        for(Node middle: middles)
            for(int i = 0; i < middle.weights.length; i++)
                middle.weights[i] = nextDouble();

        bias = nextDouble();
    }

    public void lookAround(LinkedList<Obstacle> obstacles, Dino player){//set inputLayer
        if(obstacles.isEmpty()){
            //next obstacle posX
            inputLayer[0].setInput(1);
            //posY
            inputLayer[1].setInput(0);
            //sizeX
            inputLayer[2].setInput(0);
            //sizeY
            inputLayer[3].setInput(0);
        }else {
            //next obstacle posX
            inputLayer[0].setInput(obstacles.get(0).getPos().x / Main.getPApplet().width);
            //posY
            inputLayer[1].setInput(obstacles.get(0).getPos().y / Main.getPApplet().height);
            //sizeX
            inputLayer[2].setInput(obstacles.get(0).getSize().x / Main.getPApplet().width);
            //sizeY
            inputLayer[3].setInput(obstacles.get(0).getSize().y / Main.getPApplet().height);
        }

        //ground speed
        inputLayer[4].setInput(Game.getInstance().getSpeed());

        //player's posY
        inputLayer[5].setInput(player.getPos().y);

        if(obstacles.size() >= 2){
            //next obstacle posX
            inputLayer[0].setInput(obstacles.get(1).getPos().x / Main.getPApplet().width);
            //posY
            inputLayer[1].setInput(obstacles.get(1).getPos().y / Main.getPApplet().height);
            //sizeX
            inputLayer[2].setInput(obstacles.get(1).getSize().x / Main.getPApplet().width);
            //sizeY
            inputLayer[3].setInput(obstacles.get(1).getSize().y / Main.getPApplet().width);
        }else {
            //next two obstacle posX
            inputLayer[6].setInput(1);
            //posY
            inputLayer[7].setInput(0);
            //sizeX
            inputLayer[8].setInput(0);
            //sizeY
            inputLayer[9].setInput(0);
        }
        //bias(as input)
        inputLayer[10].setInput(bias);
    }

    public boolean[] computeAction(){
        boolean[] answer = new boolean[3];
        for(int i = 0; i < 3; i++){
            answer[i] = output[i].getOutput() >= 0.8;
        }
        return answer;
    }

    public Brain clone(){
        Brain b = new Brain();

        for(int i = 0; i < output.length; i++){
            for(int j = 0; j < b.output[i].weights.length; j++){
                b.output[i].weights[j] = this.output[i].weights[j];
            }
        }

        for(int i = 0; i < middles.length; i++){
            for(int j = 0; j < b.middles[i].weights.length; j++){
                b.middles[i].weights[j] = this.middles[i].weights[j];
            }
        }

        b.bias = this.bias;

        return b;
    }

    public void mutate(double rate){
        for(Node out: output) {
            for (int i = 0; i < out.weights.length; i++) {
                if (Main.getPApplet().random(1) < rate) {
                    out.weights[i] = nextDouble();
                }
            }
        }
        for(Node middle: middles) {
            for (int i = 0; i < middle.weights.length; i++) {
                if (Main.getPApplet().random(1) < rate) {
                    middle.weights[i] = nextDouble();
                }
            }

        }

        if (Main.getPApplet().random(1) < rate) {
            bias = nextDouble();
        }

    }

    @Override
    public String toString(){
        String result = "";
        for(Node out: output)
            for(int i = 0; i < out.weights.length; i++)
                result += out.weights[i] + " ";
        result += "\n";

        for(Node middle: middles)
            for(int i = 0; i < middle.weights.length; i++)
                result += middle.weights[i] + " ";

        result += "\n" + bias;
        return result;
    }
}
