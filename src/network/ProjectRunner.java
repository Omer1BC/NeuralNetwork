package network;


import java.io.IOException;
import mnist.MNISTImage;
import mnist.MNISTReader;

public class ProjectRunner {
    public static void main(String[] args) {
        int[] dimensions = {784,9,10};
        NeuralNetwork network = new NeuralNetwork(dimensions);
        Gradient grad = new Gradient(network);
        Trainer trainer = new Trainer(grad,5,1);
        
        MNISTReader reader = new MNISTReader("assets/mnist/train-images-idx3-ubyte", "assets/mnist/train-labels-idx1-ubyte");
        MNISTImage[] data = new MNISTImage[0];
        try {
            data = reader.readImages(5);
            trainer.train(data);
            
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        
        

    }
}
