package network;


// Thrown when the structure can't accommodate a specific action (insufficient neurons, uncalculated gradients)  
public class DimensionalException extends Exception {

    public DimensionalException(String message) {
        super(message);
    }
}
