package network;


import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Neuron extends Observable {
    /**
     * How many children a neuron has
     */
    private int numConnections;
    private double activation;
    private double bias;
    /**
     * The neurons in the previous layer that feed into a neuron
     */
    private List<Neuron> children;
    /**
     * Weights for each of its children
     */
    private List<Double> weights;
    private int column;
    /**
     * The last neuron in the next layer that has this neuron as one of its
     * children
     * 
     */
    private Neuron parent;
    private int row;
    /**
     * The next neuron down in the current layer
     */
    private Neuron sibling;

    private double z; 

    public Neuron(
        double pActivation,
        double pBias,
        List<Double> pWeight,
        int column,
        int row) {
        children = new ArrayList<Neuron>();
        activation = pActivation;
        bias = pBias;
        weights = pWeight;
        this.column = column;
        this.row = row;
        parent = null;
        sibling = null;
        z = 0;

    }


    // For the last (leftmost) layer, each neuron has no children, and its
    // activation is just the pixel value
    // These are called the sensor neurons
    public Neuron(double pActivation, int column, int row) {
        this(pActivation, 0, null, column, row);
    }


    // Could be necessary to have a certain amount of digits for activation
    private double truncate(double n) {
        n = n * Math.pow(10, 2);
        n = Math.floor(n);
        n = n / Math.pow(10, 2);
        return n;
    }


    // Calculates the weighted sum of its children (previous layer
    // neurons)
    public void update() throws MismatchException {
        if (weights.size() < children.size()) {
            throw new MismatchException(
                "A weight isn't asociated with each child of " + this
                    .toString());
        }
        activation = 0;
        for (int i = 0; i < children.size(); i++) {
            activation += children.get(i).getActivation() * weights.get(i);
        }
        activation += bias;
        z = activation;
        setChanged();                    //change
        notifyObservers((Double)z);
        
        activation = sig(z);

        // **Modification**// activation = sig(z);
    }

    // For testing, you can add a weight at a time


    public void addWeight(double n) {
        // Modification
        if (weights == null) {
            throw new NullPointerException(
                "Null parameter passed in the neuron constructor representing weights");
        }
        weights.add(n);
    }


    public double getBias() {
        return bias;
    }


    public void setBias(double n) {
        bias = n;
    }


    public double getActivation() {
        return activation;
    }


    public List<Double> getWeights() {
        return weights;

    }


    public void setActivation(double newActivation) {
        activation = newActivation;
    }


    public int getNumConnections() {
        return numConnections;
    }


    // You can add a single child at a time
    public void setChild(Neuron nuero) {
        children.add(nuero);
        nuero.setParent(this);
        numConnections++;

        if (children.size() > 1)

        {
            int prevIndex = children.size() - 2;
            Neuron prev = children.get(prevIndex);
            prev.setSibling(nuero);

        }
    }


    // Gets child at a particular index 0 -> n, useful for testing
    public Neuron getChild(int n) {
        return children.get(n);
    }


    public List<Neuron> getChildren() {
        return children;
    }

    // ALso possible to add children in bulk from a list


    public void setChildren(List<Neuron> list) {
        for (Neuron nuero : list) {
            setChild(nuero);
        }
    }


    public Neuron getParent() {
        return parent;
    }


    public boolean hasParent() {
        return parent != null;
    }


    private void setParent(Neuron nuero) {
        parent = nuero;
    }


    private boolean setSibling(Neuron nuero) {
        if (parent == null) {
            return false;
        }
        else {
            sibling = nuero;
            return true;

        }
    }


    public boolean hasSibling() {
        return sibling != null;
    }


    public Neuron getSibling() {
        return sibling;
    }


    public boolean hasChildren() {
        return children.size() > 0;
    }


    // Replaces weight at particular index
    public boolean replaceWeight(int index, double weight) {
        if (index < 0 || index >= weights.size()) {
            return false;
        }
        else {
            weights.set(index, weight);
            return true;
        }
    }


    // Gets weight at a particular index
    public double getWeight(int n) {
        return weights.get(n);
    }


    // Helper method
    private int getIndexOfChild(Neuron nuero) {
        return children.lastIndexOf(nuero);
    }


    @Override
    public String toString() {
        return "Neuron " + column + "_" + row;
    }


    public int getRow() {
        return row;
    }


    public int getColumn() {
        return column;
    }


    public boolean equals(Neuron nuero) {
        if (nuero == null)
            return false;
        if (nuero.getClass() == this.getClass()) {
            Neuron other = (Neuron)nuero;
            return row == other.getRow() && column == other.getColumn();

        }
        return false;
    }


    public static double sig(double n) {
        double denom = 1.0 + Math.exp(-n);  //Change
        return 1.0 /denom;
    }


    public static double dSig(double n) {
        return sig(n) * (1 - sig(n));
    }


    public double getZ() {
        return z;
    }


    public void updateBias(double dBias) {
        bias += dBias;
    }

    
    public void updateWeights(double[] dWeights)
    {
        for (int i = 0 ; i < weights.size();i++)
        {
            weights.set(i, weights.get(i) + dWeights[i]);
        }
    }

}
