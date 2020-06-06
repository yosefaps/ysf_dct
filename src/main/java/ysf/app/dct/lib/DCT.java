package ysf.app.dct.lib;

import boofcv.io.image.ConvertBufferedImage;
import boofcv.struct.image.*;

import javax.imageio.ImageIO;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.*;
import java.util.Arrays;
import java.util.Random;

public class DCT {

    public static final double PI = 3.1415926535897931;

    private static final float[][] DCTbasis3x3 =
            {
                    {
                            0.5773502588272094726562500000000000000000f,
                            0.5773502588272094726562500000000000000000f,
                            0.5773502588272094726562500000000000000000f,
                    },
                    {
                            0.7071067690849304199218750000000000000000f,
                            0.0000000000000000000000000000000000000000f,
                            -0.7071067690849304199218750000000000000000f,
                    },
                    {
                            0.4082483053207397460937500000000000000000f,
                            -0.8164966106414794921875000000000000000000f,
                            0.4082483053207397460937500000000000000000f
                    }
            };

    /* ------------------------------------------------------------------ */

    private void Print4DArray(float[][][][] arr, int p1, int p2, int p3, int p4) {

        System.out.println("Params: " + Arrays.toString(arr));

//        for (int a = 0; a < p1; a++) {
//            for (int b = 0; b < p2; b++) {
//                for (int d = 0; d < p3; d++) {
//                    for (int e = 0; e < p4; e++) {
//                        System.out.printf("%f ", arr[a][b][d][e]);
//                    }
//                    System.out.print("\n");
//                }
//                System.out.print(" ~~ ");
//            }
//            System.out.print(" !! ");
//        }
    }

    public void funcTest() {
        int num_patches = 2;
        int channel = 2;
        int height_p = 5;
        int width_p = 5;

        float[][][][] patches = new float[num_patches][][][];
        for (int p = 0; p < num_patches; p++) {
            patches[p] = new float[channel][][];
            for (int c = 0; c < channel; c++) {
                patches[p][c] = new float[height_p][];
                for (int h = 0; h < height_p; h++) {
                    patches[p][c][h] = new float[width_p];
                }
            }
        }

//        patches[counter_patch][kp][jp][ip] = im[kp*size1 + (j+jp)*width + i + ip];

        int xx = 0;
        for (int a = 0; a < num_patches; a++) {
            for (int b = 0; b < channel; b++) {
                for (int d = 0; d < height_p; d++) {
                    for (int e = 0; e < width_p; e++) {
                        patches[a][b][d][e] = xx;
                        xx++;
                    }
                }
            }
        }

        System.out.println("Patches: " + Arrays.toString(patches));
        Print4DArray(patches, num_patches, channel, height_p, width_p);
    }

    public void boofTest(BufferedImage in) throws IOException {

        Planar<GrayF32> convertedImg = new Planar<>(GrayF32.class, in.getWidth(), in.getHeight(), 3);
        ConvertBufferedImage.convertFrom(in, convertedImg, true);

        Random rand = new Random();
        for( int i = 0; i < convertedImg.getNumBands(); i++ ) {
            for (int y = 0; y < convertedImg.getHeight(); y++) {
                for (int x = 0; x < convertedImg.getWidth(); x++) {
//                    System.out.println("Original "+i+" = "+rgb.getBand(i).get(x,y));
                    float xx = convertedImg.getBand(i).get(x,y);
//                    int rand_int1 = rand.nextInt(25);
                    xx += 100;
                    convertedImg.getBand(i).set(x, y, xx);
                }
            }
        }

        BufferedImage output = new BufferedImage(in.getWidth(), in.getHeight(), in.getType());
        ConvertBufferedImage.convertTo(convertedImg, output,true);

        File pwd = new File(".");
        File outfile = File.createTempFile("aaaaa", ".png", pwd);
        ImageIO.write(output, "PNG", outfile);
        System.out.println("doneee: " + outfile.getAbsolutePath());
    }

    public void testColorTransform(BufferedImage in, BufferedImage out, int width, int height) throws IOException {
        int bands = 3;
//        int[] bandOffsets = {0, 1, 2, 3}; // length == bands, 0 == R, 1 == G, 2 == B and 3 == A
        float[] outsrc = new float[width*height*bands];
        float[] outsrc2 = new float[width*height*4];

        int size1 = width * height;
        System.out.println("w: "+ width+", h:"+height);

        int imagetype = in.getType();
        Raster xx = in.getData();
        DataBuffer yy = xx.getDataBuffer();
        System.out.println("data buffer size: " + yy.getSize());
        for (int asd = 0; asd < yy.getSize(); asd++) {
//            System.out.print(yy.getElemFloat(asd)+" ");
            outsrc2[asd] = yy.getElemFloat(asd);
        }
        System.out.println("end \n");
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {

//                System.out.println("try get rgb: "+j+","+i+".");
                Color c = new Color(in.getRGB(i, j));
                int r = c.getRed();
                int g = c.getGreen();
                int b = c.getBlue();
//                int a = c.getAlpha();
//                System.out.println("getrgb: "+j+","+i+"("+r+","+g+","+b+")");

                float or =
                        (  r * DCTbasis3x3[0][0]
                                + g * DCTbasis3x3[0][1]
                                + b * DCTbasis3x3[0][2] );

                float og =
                        (  r * DCTbasis3x3[1][0]
                                + g * DCTbasis3x3[1][1]
                                + b * DCTbasis3x3[1][2] );

                float ob =
                        (  r * DCTbasis3x3[2][0]
                                + g * DCTbasis3x3[2][1]
                                + b * DCTbasis3x3[2][2] );

                // fill the buffer
                int idx_pixel0 = j * width + i;
                int idx_pixel1 = 1 * size1 + j * width + i;
                int idx_pixel2 = 2 * size1 + j * width + i;
//                outsrc[idx_pixel0] = or;
//                outsrc[idx_pixel1] = og;
//                outsrc[idx_pixel2] = ob;
                outsrc[idx_pixel0] = r;
                outsrc[idx_pixel1] = g;
                outsrc[idx_pixel2] = b;
            }
        }

        for(int aa = 0; aa < width * height * 4; aa++){
//            System.out.print(outsrc2[aa] + " ");
        }
        System.out.println("end \n");

        out = new BufferedImage(width, height, imagetype);
        WritableRaster raster = out.getRaster();
        raster.setPixels(raster.getMinX(), raster.getMinY(), raster.getWidth(), raster.getHeight(), outsrc2);

        File pwd = new File(".");
        File outfile = File.createTempFile("aaaaa", ".png", pwd);
        ImageIO.write(out, "PNG", outfile);
        System.out.println("doneee: " + outfile.getAbsolutePath());
    }

    public void testCreateImage() throws IOException {
        int w = 300;
        int h = 300;

        Color[] colors = new Color[] { Color.red, Color.green, Color.blue };

        BufferedImage img = new BufferedImage(w, h, 1);

        int dx = w / colors.length;

        for (int i = 0; i < colors.length; i++) {
            for (int x = i *dx; (x < (i + 1) * dx) && (x < w) ; x++) {
                for (int y = 0; y < h; y++) {
                    img.setRGB(x, y, colors[i].getRGB());
                }
            }
        }

        File pwd = new File(".");
        File out = File.createTempFile("rgba_", ".png", pwd);
        System.out.println("Create file: " + out.getAbsolutePath());
        ImageIO.write(img, "PNG", out);
//        return ImageIO.createImageInputStream(out);
    }

    /* ------------------------------------------------------------------ */

    public void ColorTransform(BufferedImage in, BufferedImage out) throws IOException {
        System.out.println("image input type: " + in.getType());

        int w = in.getWidth();
        int h = in.getHeight();

        Planar<GrayF32> preparedImg = new Planar<>(GrayF32.class, w, h, 3);
        ConvertBufferedImage.convertFrom(in, preparedImg, true);
        Planar<GrayF32> decorrelated = new Planar<>(GrayF32.class, w, h, 3);

        for (int r = 0; r < preparedImg.getHeight(); r++) {
            for (int c = 0; c < preparedImg.getWidth(); c++) {
                float temp_r = preparedImg.getBand(0).get(c, r);
                float temp_g = preparedImg.getBand(1).get(c, r);
                float temp_b = preparedImg.getBand(2).get(c, r);
                float temp_dr = (temp_r * DCTbasis3x3[0][0]) + (temp_g * DCTbasis3x3[0][1]) + (temp_b * DCTbasis3x3[0][2]);
                float temp_dg = (temp_g * DCTbasis3x3[1][0]) + (temp_g * DCTbasis3x3[1][1]) + (temp_b * DCTbasis3x3[1][2]);
                float temp_db = (temp_b * DCTbasis3x3[2][0]) + (temp_g * DCTbasis3x3[2][1]) + (temp_b * DCTbasis3x3[2][2]);
                decorrelated.getBand(0).set(c, r, temp_dr);
                decorrelated.getBand(1).set(c, r, temp_dg);
                decorrelated.getBand(2).set(c, r, temp_db);
            }
        }

        out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        ConvertBufferedImage.convertTo(decorrelated, out,true);

        File pwd = new File("./output");
        File outfile = File.createTempFile("out_", ".png", pwd);
        ImageIO.write(out, "PNG", outfile);
        System.out.println("done: " + outfile.getAbsolutePath());
    }

    private void Image2Patches() {
        System.out.println("Image2Patches");
    }

    private void Patches2Image() {
        System.out.println("Patches2Image");
    }

    public void DCTdenoising(byte[] img) throws IOException {

        int dct_size_flag = 0;
        float sigma = 30;
        int channel = 3;

        // DCT window size
        int width_p, height_p;
        if (dct_size_flag == 0) {
            width_p = 16;
            height_p = 16;
        } else {
            width_p = 8;
            height_p = 8;
        }

        // Get a BufferedImage object from a byte array
        InputStream in = new ByteArrayInputStream(img);
        BufferedImage originalImage = ImageIO.read(in);

        // Get image dimensions
        int height = originalImage.getHeight();
        int width = originalImage.getWidth();

        int num_patches = (width - width_p + 1) * (height - height_p + 1);

        float[][][][] patches = new float[num_patches][][][];
        for (int p = 0; p < num_patches; p++) {
            patches[p] = new float[channel][][];
            for (int c = 0; c < channel; c++) {
                patches[p][c] = new float[height_p][];
                for (int h = 0; h < height_p; h++) {
                    patches[p][c][h] = new float[width_p];
                }
            }
        }
        System.out.println(Arrays.toString(patches));

        if (channel == 3) {

        }


        double[][] dct = new double[height][width];
        double ci, cj, dct1, sum;

//        this.ColorTransform();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color c = new Color(originalImage.getRGB(j, i));
                int r = c.getRed();
                int g = c.getGreen();
                int b = c.getBlue();
                int a = c.getAlpha();
//                System.out.printf("(j, i): (%d, %d), r: %d, g: %d, b: %d, a: %d \n", j,i,r,g,b,a);
            }
        }


        // Get a BufferedImage object from a byte array
//        InputStream in = new ByteArrayInputStream(image);
//        BufferedImage originalImage = ImageIO.read(in);
//
//        // Get image dimensions
//        int height = originalImage.getHeight();
//        int width = originalImage.getWidth();
//
//        // The image is already a square
//        if (height == width) {
//            return originalImage;
//        }
//
//        // Compute the size of the square
//        int squareSize = (height > width ? width : height);
//
//        // Coordinates of the image's middle
//        int xc = width / 2;
//        int yc = height / 2;
//
//        // Crop
//        BufferedImage croppedImage = originalImage.getSubimage(
//                xc - (squareSize / 2), // x coordinate of the upper-left corner
//                yc - (squareSize / 2), // y coordinate of the upper-left corner
//                squareSize,            // widht
//                squareSize             // height
//        );
//
//        return croppedImage;
    }
}
