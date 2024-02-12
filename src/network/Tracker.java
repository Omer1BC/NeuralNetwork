package network;


import java.util.ArrayList;

// Responsible for averaging the weights and biases over a batch of trials per
// LAYER of neurons

// For the weights, the data structure stores trial number, neuron, and
// associated weight for each child (as there multiple weights for each neuron)
// For the bias, it's trial number and neuron (as there is only one bias per
// neuron)
public class Tracker {
    private ArrayList<ArrayList<double[]>> batchWeights;
    private ArrayList<double[]> batchBias;
    private ArrayList<Double> batchCost;

    public Tracker(
        ArrayList<ArrayList<double[]>> pWeights,
        ArrayList<double[]> pBias) {
        batchWeights = pWeights;
        batchBias = pBias;
        batchCost = new ArrayList<Double>();
    }
    
    public void addTrial(ArrayList<double[]> pWeight)
    {
        batchWeights.add(pWeight);
    }
    
    public void addTrial(double[] pBias)
    {
        batchBias.add(pBias);
    }
    
    public void addTrial(double pCost)
    {
        batchCost.add(pCost);
    }


    // Returns the final averaged gradient weights that should update the
    // current layer neurons
    public ArrayList<double[]> averageDWeights() {
        ArrayList<double[]> averaged = new ArrayList<double[]>();
        ArrayList<double[]> dimensions = batchWeights.get(0);

        for (int i = 0; i < dimensions.size(); i++) {
            averaged.add(new double[dimensions.get(0).length]);
        }

        for (int i = 0; i < dimensions.size(); i++) {
            for (int j = 0; j < dimensions.get(i).length; j++) {
                for (int k = 0; k < batchWeights.size(); k++) {
                    averaged.get(i)[j] += getDWeight(k, i, j);
                }
                averaged.get(i)[j] /= batchWeights.size();
            }
        }

        return averaged;

    }


    // Returns the final averaged gradient biases that should update the neurons
    public double[] averageDBiases()

    {
        double[] dimensions = batchBias.get(0);
        double[] averaged = new double[dimensions.length];
        for (int i = 0; i < dimensions.length; i++) {
            for (int j = 0; j < batchBias.size(); j++) {
                averaged[i] += getDBias(j, i);
            }
            averaged[i] /= batchBias.size();
        }
        return averaged;

    }
    
    public double averageCost()
    {
        double avg = 0;
        for (Double cost: batchCost)
        {
            avg += cost;
        }
        avg /= batchCost.size();
        return avg;
    }


    private double getDWeight(int trial, int neuron, int weight) {
        return batchWeights.get(trial).get(neuron)[weight];
    }


    private double getDBias(int trial, int neuron) {
        return batchBias.get(trial)[neuron];
    }

}
