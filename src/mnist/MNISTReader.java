package mnist;

import java.awt.image.BufferedImage;

import java.io.*;

import javax.imageio.ImageIO;

/**
 * Reads the given MNIST data set by parsing the given MNIST files.
 * The files should be decompressed, but NO other modifications should be made or
 * the images will appear corrupted.
 */
public class MNISTReader {
    
    private String imageDataset, labelDataset;
    private int offset; // indicates how many images have already been read.

    /**
     * The size of both files must match or else they will not be read properly. An exception will be thrown.
     * @param imageDataset
     * @param labelDataset
     */
    public MNISTReader(String imageDataset, String labelDataset) {
        this.imageDataset = imageDataset;
        this.labelDataset = labelDataset;
        this.offset = 0;
    }

    public MNISTImage[] readImages() throws IOException, IllegalArgumentException {
        return this.readImages(-1); // will read all images.
    }

    public MNISTImage[] readImages(int maxRead) throws IOException, IllegalArgumentException {
        if (maxRead < 0) 
            maxRead = Integer.MAX_VALUE;

        if (maxRead == 0) {
            throw new IllegalArgumentException("maxRead must not be 0. Use a negative number to read all images.");
        }

        // open the files.
        DataInputStream imageReader = new DataInputStream(new FileInputStream(new File(this.imageDataset)));
        DataInputStream labelReader = new DataInputStream(new FileInputStream(new File(this.labelDataset)));
        
        // skip the magic number on both files -- we don't care about this.
        imageReader.skipBytes(4);
        labelReader.skipBytes(4);

        // now read the sizes of each file to ensure they match.
        // the file is stored in Big-Endian format
        // int imageSize = readInt(imageReader);
        int numImages = imageReader.readInt();
        int numLabels = labelReader.readInt();

        if (numImages != numLabels) {
            throw new IllegalArgumentException("The number of images and labels do not match.");
        }

        // skip the integers that represent the size of the image files.
        imageReader.skipBytes(8); // 4 bytes per dimension
        
        // don't read more than requested.
        numImages = Math.min(maxRead, numImages);
        MNISTImage[] images = new MNISTImage[numImages];

        // skip the data that has already been read.
        labelReader.skipBytes(offset);
        imageReader.skipBytes(offset * 28 * 28);

     //   System.out.println("Now reading " + numImages + " MNIST images starting from the " + this.offset + "th image.");

        // now read the data.
        for (int i = 0; i < numImages; i++) {
            int label = labelReader.readByte();
            byte[] pixels = new byte[MNISTImage.SIZE * MNISTImage.SIZE];
            imageReader.read(pixels);
            images[i] = new MNISTImage(pixels, label);
        }

        // move the offset up.
        offset += numImages;

        imageReader.close();
        labelReader.close();

        return images;
    }

    public static void main(String[] args) {
        // Test this class.
        // Generates some images with their respective index and label written in the filename.
        // Stored in the folder "assets/mnist/sample"
        MNISTReader reader = new MNISTReader("assets/mnist/train-images-idx3-ubyte", "assets/mnist/train-labels-idx1-ubyte");
        MNISTImage[] images;
        try {
            images = reader.readImages(10);
            for (int i = 0; i < images.length; i++) {
                ImageIO.write((BufferedImage)images[i].generateImage(), "jpg", new File("assets/mnist/sample/test_" + i + "_" + images[i].getLabel() + ".jpg"));
            }
            System.out.println(images[0].getLabel());
            images = reader.readImages(10);
            for (int i = 0; i < images.length; i++) {
                ImageIO.write((BufferedImage)images[i].generateImage(), "jpg", new File("assets/mnist/sample/test_" + (i + 10) + "_" + images[i].getLabel() + ".jpg"));
            }
            System.out.println(images[0].getLabel());

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
