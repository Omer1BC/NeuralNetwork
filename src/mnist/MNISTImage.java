package mnist;

import java.awt.image.BufferedImage;
import network.Neuron;
import java.awt.Image;

public class MNISTImage {
    
    public static final int SIZE = 28;

    private int label;
    private byte[] pixels; // stored row by row

    public MNISTImage(byte[] pixels, int label) {
        if (pixels.length != SIZE * SIZE) {
            throw new IllegalArgumentException("pixels.length != SIZE * SIZE");
        }
        this.pixels = pixels;
        this.label = label;
    }

    public Image generateImage() {
        BufferedImage image = new BufferedImage(28, 28, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < pixels.length; i++) {
            int x = i % SIZE;
            int y = i / SIZE;
            // draw it gray scaled.
            image.setRGB(x, y, (int) (pixels[i]) << 16 | (int) (pixels[i]) << 8 | (int) (pixels[i]));
        }
        return image;
    }

    public int getLabel() {
        return this.label;
    }

    public byte[] getData() {
        return this.pixels;
    }
    
    public double[] normalize()
    {
        byte[] clone = pixels.clone();
        double[] values = new double[clone.length];
        for (int i = 0; i < clone.length;i++)
        {
            values[i] = Neuron.sig((double)clone[i]);
        }
        return values;
   
    }
}
