package network;


import java.util.List;

import java.util.ArrayList;
//Gurantee that key is not null
public class NeuralNetwork {
    /**
     * Combines the neurons into a network following an array of dimensions
     * 
     * {4, 3, 2} is a network of 4 layers with 4 neurons in the first layer, 3
     * in the second, and 2 in the last
     */
    
    
    private int[] dimensions;

    /**
     * Stores all the layers of neurons
     */
    private List<List<Neuron>> neurons;
    public List<Double> randomWeights = new ArrayList<Double>();
    public List<Double> randomBias = new ArrayList<Double>();
    // The expected confidence values for each run
    private double[] key;
    //The digit we hope the computer predicts
    private int currentAnswer;

    /**
     * At max, 3 neurons per layer
     * 
     * @throws DimensionalException
     *             If layer requirement isn't met
     */

    /*
     * I'm going to disable the exception that ensures the layer cap to make
     * adjusting the GUI less annoying, but there will be errors in the gradient
     * when trying to train the network. If you're above the layer cap, DO NOT
     * call the train() method of a trainer object
     */
    //---------------------------------------------------------------------------
    public NeuralNetwork(int[] dimensionsP)
        {
        dimensions = dimensionsP;
        key = new double[dimensions[dimensions.length - 1]];
        currentAnswer = -1;
        
        /*
         * if (dimensions.length > 3)
         * {
         * throw new
         * DimensionalException("Missing gradients for a network with " +
         * dimensions.length + " layers");
         * }
         */
        
        
        neurons = new ArrayList<List<Neuron>>();
        generateNetwork();


    }


    /**
     * Assembles the neurons following the array of dimensions passed
     * 
     */
    public void generateNetwork() {
        List<Neuron> prev = null;
        for (int i = 0; i < dimensions.length; i++) {
            List<Neuron> layer_n_nuerons = new ArrayList<Neuron>();
            for (int j = 0; j < dimensions[i]; j++) {
                if (i == 0) {
                    layer_n_nuerons.add(new Neuron(0, i, j));
                }
                else {
                    createRandomWeights(prev.size());
                    Neuron nuero = new Neuron(0, Math.random(), randomWeights, i, j);  //change
                    nuero.setChildren(prev);
                    layer_n_nuerons.add(nuero);
                }

            }
            prev = layer_n_nuerons;
            neurons.add(layer_n_nuerons);
        }
        
        updateNetwork();
        
    }


    /**
     * Retrieves a particular neuron in the network
     * 
     * @param index
     *            Location in the format column, row
     * @return
     *         That neuron
     */

    public Neuron get(int column, int row) {
        return neurons.get(column).get(row);
    }


    public int[] getDimensions() {
        return dimensions;
    }


    public List<List<Neuron>> getNeurons() {
        return neurons;
    }


    public void updateNetwork() {
        for (int i = 1; i < dimensions.length; i++) {
            for (int j = 0; j < dimensions[i]; j++) {
                try {
                    neurons.get(i).get(j).update();
                }
                catch (MismatchException e) {
                    e.printStackTrace();
                }
                if (NetworkGUI.texts != null) {
                    NetworkGUI.updateActivations(neurons.get(i).get(j), i, j);
                }
            }
        }
    }


    private void createRandomWeights(int n) {
        randomWeights = new ArrayList<Double>();
        for (int i = 0; i < n; i++) {

            randomWeights.add(Math.random() );  //change
        }
    }


    /**
     * Updates the activation of the sensor neurons when a new image is being
     * processed
     * @throws DimensionalException 
     * When there's an insufficient sensor neurons for the image size
     * 
     *
     */
    
    //---------------------------------------------------------
    public void processImage(double[] pixValues, int label) throws DimensionalException
         {

        if (pixValues == null) {
            throw new NullPointerException("Null array of pixel values passed");
        }
        if (pixValues.length != dimensions[0]) {
            throw new DimensionalException(
                "Dimensions of the image do not match the dimensions of the sensor neurons"
                    + "\nCheck that the passed array is completely filled");
        } 

        for (int i = 0; i < dimensions[0]; i++) {

            double pixelValue = pixValues[i];
            get(0, i).setActivation(pixelValue);
        }
        assertDigit(label);    
        updateNetwork();
        


    }


    // Creates the answer key for each training sample
    public void assertDigit(int label) {  //Change to public
        key = new double[dimensions[dimensions.length - 1]];
        key[label] = 1;
        currentAnswer = label;

    }
    
    public double[] getKey()
    {
        return key;
    }
    
    public int indexOfHighestConfidence()
    {
        // updateNetwork();
        double min = -1;
        int index = -1;
        double[] finalActivations = new double[dimensions[dimensions.length - 1]];
        for (int i = 0; i < dimensions[dimensions.length -1 ];i++)
        {
            Neuron curr = get(dimensions.length -1 , i);
            finalActivations[i] = curr.getActivation();    
        }


        
        for (int i = 0; i < finalActivations.length;i++ )
        {
            if (finalActivations[i] > min)
            {
                min = finalActivations[i];
                index = i;
            }
        }
        
        return index;
        
    }
    
    public void printLastLayer()
    {
        for (int i = 0; i < dimensions[dimensions.length -1];i++)
        {
           System.out.print( neurons.get(2).get(i).getActivation() + " ");
        }
        System.out.println("");
    }
    
    public int getCurrentAnswer()
    {
        return currentAnswer;
    }

}
