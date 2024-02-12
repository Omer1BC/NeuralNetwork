package network;

 
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.*;  
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
 
import mnist.MNISTImage;

/**
 * Class that will visualize the neural network
 */
public class NetworkGUI extends Frame {
 
    private Button train;
 
    public static final int WIDTH_FACTOR = 400;
    public static final int HEIGHT_FACTOR = 90;
    public static final int CIRCLE_WIDTH = 80;
    public static final int CIRCLE_HEIGHT = 80;


    private List<List<Neuron>> neuron;
    private static Trainer trainer;
    public static List<List<TextField>> texts;
    private static TextArea cost;
    private static TextField cycleDisplay;
    private static int numCycles;
    /**
     * Constructor for the GUI that draws the neural network
     */
    public NetworkGUI(Trainer trainer, MNISTImage[] data) {
 
        this.trainer = trainer;
 
        this.neuron = trainer.getGradient().getNetwork().getNeurons();
 
        texts = new ArrayList<List<TextField>>();
 
        Double dCost = new Double(trainer.getGradient().ithCost());
        cost = new TextArea(dCost.toString());
        cost.setBounds(1300, 110, 300, 400);
        add(cost);
 
        cycleDisplay = new TextField("Cycle: " + numCycles);
        cycleDisplay.setBounds(550 - cycleDisplay.getText().length() * 2, 40, 50, 20);
        add(cycleDisplay);
 
        train = new Button("Train Neural Network");
        train.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                train.disable();
                try {
                    trainer.train(data);
                    System.out.println("Now Training Neural Network");
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
        train.setBounds(1300, 500, 150, 150);
        add(train);
 
        for (int i = 0; i < neuron.size(); i++) {
            List<TextField> currText = new ArrayList<TextField>();
            if (i == 0) {
                for (int j = 0; j < 3; j++) {
                    int x = 100 + i * WIDTH_FACTOR;
                    int y = 50 + j * HEIGHT_FACTOR;
                    Neuron currNeuron = neuron.get(i).get(j);
                    Double activation = new Double(currNeuron.getActivation());
                    TextField text = new TextField(activation.toString());
                    text.setBounds(x + 45 - text.getText().length(), y + 50, 40 + text.getText().length(), 20);
                    currText.add(text);
                    add(text);
                }
            }
            else {
                for (int j = 0; j < neuron.get(i).size(); j++) {
                    int x = 100 + i * WIDTH_FACTOR;
                    int y = 50 + j * HEIGHT_FACTOR;
                    Neuron currNeuron = neuron.get(i).get(j);
                    Double activation = new Double(currNeuron.getActivation());
                    TextField text = new TextField(activation.toString());
                    text.setBounds(x + 45 - text.getText().length(), y + 50, 40 + text.getText().length(), 20);
                    currText.add(text);
                    add(text);
                }
            }
            this.texts.add(currText);
        }
 
        CustomComponent comp = new CustomComponent(neuron);
        add(comp);
 
        // Closes the external application via the "x" button
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
 
        setExtendedState(MAXIMIZED_BOTH);
        setVisible(true);
    }
 
    public static void updateActivations(Neuron neuron, int row, int col) {
        Double activation = new Double(neuron.getActivation());
        TextField currText = texts.get(row).get(col);
        currText.setText(activation.toString());
        currText.setSize(45 + activation.toString().length(), 20);
    }
 
    public static void updateCost(Gradient gradient) {
        Double dCost = new Double(gradient.ithCost());
        cost.append("\n" + dCost.toString());
    }
 
    public static void updateCycle(int cycle) {
        Integer strCyc = new Integer(cycle);
        cycleDisplay.setText("Cycle: " + strCyc.toString());
    }
 
    /**
     * Class responsible for drawing out the neural network
     */
    static class CustomComponent extends Component {
 
        private List<List<Neuron>> neurons;
       
        /*
         * Constructor for the component to be added in the frame
         */
        public CustomComponent(List<List<Neuron>> neurons) {
            this.neurons = neurons;
        }
 
 
        /**
         * Responsible for painting the neural network visualization
         */
        public void paint(Graphics g) {
            super.paint(g);
 
            Graphics2D g2d = (Graphics2D)g;
 
            List<List<Integer>> xCoords = new ArrayList<List<Integer>>(neurons
                .size());
            List<List<Integer>> yCoords = new ArrayList<List<Integer>>(neurons
                .size());
 
            // Goes through each neuron in each layer and draws it out
            // Keeps track of x and y coordinates of each neuron in each layer
            // to draw out lines in the future
            for (int i = 0; i < neurons.size(); i++) {
                ArrayList<Integer> xCoordArray = new ArrayList<Integer>();
                ArrayList<Integer> yCoordArray = new ArrayList<Integer>();
                if (i == 0) {
                    for (int j = 0; j < 3; j++) {
                        int x = 100 + i * WIDTH_FACTOR;
                        int y = 50 + j * HEIGHT_FACTOR;
                        xCoordArray.add(x);
                        yCoordArray.add(y);
                        g.drawOval(x, y, CIRCLE_WIDTH, CIRCLE_WIDTH);
                    }
                }
                else {
                    for (int j = 0; j < neurons.get(i).size(); j++) {
                        int x = 100 + i * WIDTH_FACTOR;
                        int y = 50 + j * HEIGHT_FACTOR;
                        xCoordArray.add(x);
                        yCoordArray.add(y);
                        g.drawOval(x, y, CIRCLE_WIDTH, CIRCLE_WIDTH);
                    }
                }
                xCoords.add(xCoordArray);
                yCoords.add(yCoordArray);
            }
 
            /**
             * Takes the two Array lists filled wtih x and y coordinates of the
             * neurons, and connects lines between each
             */
            for (int i = 0; i < xCoords.size(); i++) {
                for (int j = 0; j < xCoords.get(i).size(); j++) {
                    if (i + 1 < xCoords.size()) {
                        for (int b = 0; b < xCoords.get(i + 1).size(); b++) {
                            g.drawLine(xCoords.get(i).get(j) + CIRCLE_WIDTH, yCoords.get(
                                i).get(j) + CIRCLE_WIDTH / 2, xCoords.get(i + 1).get(b),
                                yCoords.get(i + 1).get(b) + CIRCLE_WIDTH / 2);
                        }
                    }
                }
            }
        }
    }
}
 


