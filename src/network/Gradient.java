package network;


import java.util.*;
// Calculates the necessary changes to the neural network's parameters
// Only computed for a 3 layer network (so far)





public class Gradient {
    private int[] dimensions;
    private List<List<Neuron>> neurons;
    private NeuralNetwork network;
    // Learning rate coefficient
    public static final double n = .1; 

    public Gradient(NeuralNetwork pNetwork) {
        network = pNetwork;
        dimensions = network.getDimensions();
        neurons = network.getNeurons();

    }


    public NeuralNetwork getNetwork() {
        return network;
    }


    // Programmed for a 3 layer network with layer 0 being the sensor neurons
    // (indexing begins at 0)
    /**
     * Computes the gradient of the weights
     * (This is only the ith gradient per batch size; the true gradient is the
     * average of this over the batch size)
     * 
     * @param layer
     *            layer of the neurons we wish to modify
     * @return
     *         A list of the necessary changes to the weights of each neuron in
     *         a selected layer
     */
    public ArrayList<double[]> ithDWeight(int layer) {
        ArrayList<double[]> dWeights = new ArrayList<double[]>();
        double gradient = 0;

        for (int j = 0; j < dimensions[layer]; j++) {
            List<Neuron> currLayer = neurons.get(layer);
            double[] currDWeight = new double[currLayer.get(j)
                .getNumConnections()];
            for (int k = 0; k < currLayer.get(j).getNumConnections(); k++) {
                Neuron currNeuron = currLayer.get(j);
                /*
                 * A dummy value is placed--replace later
                 * with the expected activation of the jth neuron in the
                 * last layer
                 */
                double result = 10;
                if (layer == 2) {
                    gradient = 2 * (currNeuron.getActivation() - network.getKey()[j])
                        * Neuron.dSig(currNeuron.getZ()) * currNeuron.getChild(
                            k).getActivation();
                }
                else if (layer == 1) {
                    double prevActiv = dPrevActivation(j);                    
                    gradient = prevActiv * Neuron.dSig(currNeuron.getZ())*currNeuron.getChild(k).getActivation();
                }
                currDWeight[k] = gradient * n * -1;
            }
            dWeights.add(currDWeight);
        }

        return dWeights;

    }


    /**
     * Computes the gradient of the necessary changes to the bias
     * (ith)
     * 
     * 
     * @param layer
     *            layer whose neurons we wish to modify
     * @return
     *         An array of the exact tweaks to the bias
     */
    public double[] ithDBias(int layer) {
        int numNeurons = dimensions[layer];
        double[] dBias = new double[numNeurons];
        List<Neuron> currLayer = neurons.get(layer);
        double gradient = 0;
      
            for (int j = 0; j < numNeurons; j++) {
                Neuron currNeuron = currLayer.get(j);
                /*
                 * A dummy value is placed--replace later
                 * with the expected activation of the jth neuron in the
                 * last layer
                 */
                 //  double result = 10;
                if (layer == 2) {
                    gradient = 2 * (currNeuron.getActivation() - network.getKey()[j]) * Neuron
                        .dSig(currNeuron.getZ()); 
                }
                else if (layer == 1)
                    
                {
                    double prevActiv = dPrevActivation(j);
                    gradient = prevActiv * Neuron.dSig(currNeuron.getZ());
                }

                dBias[j] = gradient * n * -1;

            }
        
        return dBias;

    }


    /**
     * Computes the cost per image
     * (ith)
     * 
     * @return
     */
    public double ithCost() {
        int lastLayer = dimensions.length - 1;
        double cost = 0;
        for (int i = 0; i < dimensions[lastLayer]; i++) {
            Neuron currNeuron = network.get(lastLayer, i);
            cost += Math.pow((currNeuron.getActivation() - network.getKey()[i]), 2); 

        }
        return cost;
    }


    private double dPrevActivation(int k) {
        double result = 0;
        for (int j = 0; j < dimensions[dimensions.length - 1]; j++) {
            Neuron Lneuron = network.get(dimensions.length - 1, j);
            double weight = Lneuron.getWeight(k);
            double dSig = Neuron.dSig(Lneuron.getZ());
            result += weight * dSig * 2 * (Lneuron.getActivation() - network.getKey()[j]);

        }
        return result;
    }

}
