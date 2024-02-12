package network;


import java.util.*;
import mnist.MNISTImage;

public class Listener implements Observer {
    private Trainer train;
    private NeuralNetwork net;

    public Listener(Trainer tr, MNISTImage[][] data, int epoch) {
        train = tr;
        train.addObserver(this);
        /*
         * net = train.getGradient().getNetwork();
         * for (int i = 0; i < net.getDimensions().length;i++)
         * {
         * for (int j = 0; j < net.getDimensions()[i];j++)
         * {
         * net.get(i, j).addObserver(this);
         * }
         * }
         */
        try {
            for (int i = 0; i < epoch; i++) {
                System.out.println("Epoch " + (i + 1) + "\n==========================");
                sleep();
                train.trainInCycle(data);
            }
        }
        catch (DimensionalException e) {
            e.printStackTrace();
        }

    }


    /*
     * @Override
     * public void update(Observable o, Object arg) {
     * if (String.class == arg.getClass())
     * {
     * String name = (String)arg;
     * System.out.print("Neuron " + name + "'s Z changed to ");
     * }
     * 
     * 
     * if (arg.getClass() == Double.class)
     * {
     * Double d = (Double)arg;
     * System.out.print( d + "\n");
     * }
     * 
     * }
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg.getClass() == Double.class) {
            Double d = (Double)arg;
            System.out.println("Average Cost: " + d);
        }
    }
    
    public void sleep()
    {
        try {
            Thread.sleep(300);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        
    }

}
