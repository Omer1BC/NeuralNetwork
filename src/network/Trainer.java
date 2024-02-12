package network;


import java.util.ArrayList;

import java.util.List;
import java.util.Observable;
import mnist.MNISTImage;

// Assembles all the classes to create a trainable neural network
public class Trainer extends Observable {
    private NeuralNetwork network;
    private Gradient gradient;
    // Represents how many images we process before averaging the cost function
    // and gradients (stochastic gradient descent)
    private int batchSize;
    // How many times we repeat this process
    private int cycles;

    public Trainer(Gradient pGradient, int pBatchSize, int pCycles) {
        gradient = pGradient;
        network = gradient.getNetwork();
        batchSize = pBatchSize;
        cycles = pCycles;

    }


    public Gradient getGradient() {
        return gradient;
    }


    /**
     * Turn into a 2d array, and throw dimensional exception if it's
     * inconsistent with cycles and batch size
     */
    public void train(MNISTImage[] dataset) {

        // The the final loop that runs our program
        // When the outermost loop finishes, this is an epoch; I read that we
        // have to feed the data again
        for (int i = 0; i < cycles; i++) {
            NetworkGUI.updateCycle(i);
            Tracker[] trackers = createLayerTrackers();
            Tracker cost = new Tracker(null, null);
            // As per stochastic descent, we divide the images into batches and
            // then average out the gradients (and cost to get a sense of our
            // accuracy)
            // These averages approximate the gradient and are then used to
            // update the network
            for (int j = 0; j < batchSize; j++) {
                try {
                    // feed the image
                    network.processImage(dataset[j].normalize(), dataset[j]
                        .getLabel()); // --Update--//
                }
                catch (DimensionalException de) {
                    de.printStackTrace();
                }
                double ithCost = gradient.ithCost();
                setChanged(); // Change
                notifyObservers((Double)ithCost);
                cost.addTrial(ithCost); // --Update--//
                NetworkGUI.updateCost(gradient);
                for (int k = 1; k < network.getDimensions().length; k++) {
                    // Computes the gradient for each layer
                    ArrayList<double[]> gradWeight = gradient.ithDWeight(k);
                    double[] gradBias = gradient.ithDBias(k);

                    trackers[k - 1].addTrial(gradWeight);
                    trackers[k - 1].addTrial(gradBias);

                }
                double avgCost = cost.averageCost(); // --Update -- //
                // Batch is done, so it's time to optimize the parameters
                sleep(); // Change

            }
            // Then we leverage our tracker objects to average the gradients
            for (int layer = 0; layer < trackers.length; layer++) {

                int trueLayer = layer + 1;
                ArrayList<double[]> completeGradient = trackers[layer]
                    .averageDWeights();
                double[] completeGradient2 = trackers[layer].averageDBiases();
                for (int neuronIndex = 0; neuronIndex < network
                    .getDimensions()[trueLayer]; neuronIndex++) {
                    // We update the neurons accordingly
                    Neuron currNeuron = network.get(trueLayer, neuronIndex);
                    currNeuron.updateWeights(completeGradient.get(neuronIndex)); // --Update--//
                    currNeuron.updateBias(completeGradient2[neuronIndex]);

                }

            }
            // We then force an update on the network to recalculate the
            // activations following the new weights and biases
            network.updateNetwork(); // --Update--//

        }
    }


    public void trainInCycle(MNISTImage[][] data) throws DimensionalException {
        if (data.length != cycles || data[0].length != batchSize) {
            throw new DimensionalException(
                "Check that the cycles and batch size declared in the constructor matches the dataset");
        }
        for (int i = 0; i < cycles; i++) {
            Tracker[] trackers = createLayerTrackers();
            Tracker cost = new Tracker(null, null);
             System.out.println("\nCycle " + (i+1) + "\n");

            // As per stochastic descent, we divide the images into batches and
            // then average out the gradients (and cost to get a sense of our
            // accuracy)
            // These averages approximate the gradient and are then used to
            // update the network
            for (int j = 0; j < batchSize; j++) {
                try {
                    // feed the image
                    network.processImage(data[i][j].normalize(), data[i][j]
                        .getLabel()); // --Update--//
                }
                catch (DimensionalException de) {
                    de.printStackTrace();
                }
                double ithCost = gradient.ithCost();
                cost.addTrial(ithCost); // --Update--//
                // NetworkGUI.updateCost(gradient);
                for (int k = 1; k < network.getDimensions().length; k++) {
                    // Computes the gradient for each layer
                    ArrayList<double[]> gradWeight = gradient.ithDWeight(k);
                    double[] gradBias = gradient.ithDBias(k);

                    trackers[k - 1].addTrial(gradWeight);
                    trackers[k - 1].addTrial(gradBias);

                }

                // --Update -- //
                // Batch is done, so it's time to optimize the parameters
                // Change

            }

            // Then we leverage our tracker objects to average the gradients
            for (int layer = 0; layer < trackers.length; layer++) {

                int trueLayer = layer + 1;
                ArrayList<double[]> completeGradient = trackers[layer]
                    .averageDWeights();
                double[] completeGradient2 = trackers[layer].averageDBiases();
                for (int neuronIndex = 0; neuronIndex < network
                    .getDimensions()[trueLayer]; neuronIndex++) {
                    // We update the neurons accordingly
                    Neuron currNeuron = network.get(trueLayer, neuronIndex);
                    currNeuron.updateWeights(completeGradient.get(neuronIndex)); // --Update--//
                    currNeuron.updateBias(completeGradient2[neuronIndex]);

                }

            }
            // We then force an update on the network to recalculate the
            // activations following the new weights and biases
            network.updateNetwork(); // --Update--//
            double avgCost = cost.averageCost(); // major change
            System.out.println("The average cost is " + avgCost);
            setChanged(); //
            notifyObservers((Double)avgCost);
            // sleep();

        }
    }


    private void sleep() { // Change
        try {
            Thread.sleep(40);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public int testImage(MNISTImage data) {
        try {
            network.processImage(data.normalize(), data.getLabel());
            System.out.println("You passed a " + data.getLabel());
            System.out.println("The computer predicts that it's a " + network
                .indexOfHighestConfidence());
        }
        catch (DimensionalException e) {
            e.printStackTrace();
        }
        return 0;
    }


    private Tracker[] createLayerTrackers() {
        int applicableLayers = network.getDimensions().length - 1;
        Tracker[] trackers = new Tracker[applicableLayers];
        for (int i = 0; i < applicableLayers; i++) {
            trackers[i] = new Tracker(new ArrayList<ArrayList<double[]>>(),
                new ArrayList<double[]>());
        }
        return trackers;
    }

}
