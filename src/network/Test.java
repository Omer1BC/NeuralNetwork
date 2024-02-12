package network;


import java.util.ArrayList;

import mnist.MNISTImage;
import mnist.MNISTReader;

public class Test {

    public static void main(String[] args)
        throws MismatchException,
        DimensionalException {
        // Now only restricted by layers, not rows (3 max)
        int[] dimensions = { 784, 2, 10 }; //Change
        NeuralNetwork network = new NeuralNetwork(dimensions);

        /*
         * Pass an instance of a trainer instead bc it combines all the classes
         * to update a network
         */
        MNISTImage[] image = new MNISTImage[0];
        MNISTReader reader = new MNISTReader(
            "assets/mnist/train-images-idx3-ubyte",
            "assets/mnist/train-labels-idx1-ubyte");
        try {
            image = reader.readImages(5);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(image.length);

        Gradient grad = new Gradient(network);
        Trainer trainer = new Trainer(grad, image.length, 1);
        NetworkGUI gui = new NetworkGUI(trainer, image);
        trainer.train(image);
        
    }

}
