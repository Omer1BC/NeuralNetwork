package network;


import java.io.IOException;
import mnist.MNISTImage;
import mnist.MNISTReader;

public class Test2 {
    public static void main(String[] args) {
        //Need 784 in the last in 10 in first
        int[] dimensions = { 784, 10, 10 }; // Change
        NeuralNetwork network = new NeuralNetwork(dimensions);
        
       int cycles = 700;
       int batch = 10;
       MNISTImage[][] data = new MNISTImage[cycles][batch];

        MNISTReader reader = new MNISTReader(
            "assets/mnist/train-images-idx3-ubyte",
            "assets/mnist/train-labels-idx1-ubyte");
        try {
            for (int i = 0; i < cycles; i++ )
            {
                data[i] = reader.readImages(batch);            //Make read image pick up where it left off   
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(data.length);

        Gradient grad = new Gradient(network);
        Trainer trainer = new Trainer(grad, batch, cycles);
       //  NetworkGUI gui = new NetworkGUI(trainer, image); 
       //  Listener listen = new Listener(trainer, data,1); 
        try {
            trainer.trainInCycle(data);
        }
        catch (DimensionalException e) {
            e.printStackTrace();
        }
     
        
        network.printLastLayer();
        for (int i = 0; i < 5;i++) {
        MNISTImage testImage = data[0][i];
        trainer.testImage(testImage);
        network.printLastLayer();
        }
        
    }
}
