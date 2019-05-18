public class Node {
    private Node[] inputs;
    double[] weights;

    public static double sigmoid(double x){
        //using serial expansion
        return (1 / (1 + Math.exp(-x)));
    }

    public void setInput(Node[] node){
        inputs = node;
        weights = new double[inputs.length];
    }

    public double getOutput(){
        double inputSum = 0;
        for(int i = 0; i < inputs.length; i++){
            inputSum += inputs[i].getOutput() * weights[i];
        }
        return sigmoid(inputSum);
    }
}
