package network;


// Thrown when there isn't a weight associated with each child of a neuron
public class MismatchException extends Exception {

    public MismatchException(String message) {
        super(message);
    }

}
