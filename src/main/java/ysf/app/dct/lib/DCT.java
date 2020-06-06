package ysf.app.dct.lib;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;

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

    private void ColorTransform() {
        System.out.println("Color Transform");

        int size1 = img.getWidth()*img.getHeight();
        if ( flag == 1 ) {
            for (int j = 0; j < img.getHeight(); j++) {
                for (int i = 0; i < img.getWidth(); i++) {
                    //get rgb color on each pixel
//                 Color c = new Color(img.getRGB(i, j));
                    int idx_pixel0 = j * img.getWidth() + i;
                    int idx_pixel1 = 1 * size1 + j * img.getWidth() + i;
                    int idx_pixel2 = 2 * size1 + j * img.getWidth() + i;
                    out.set(idx_pixel0, (float) (in.get(idx_pixel0) * DCTbasis3x3[0][0]
                            + in.get(idx_pixel1) * DCTbasis3x3[0][1]
                            + in.get(idx_pixel2) * DCTbasis3x3[0][2]));

                    out.set(idx_pixel1, (float) (in.get(idx_pixel0) * DCTbasis3x3[1][0]
                            + in.get(idx_pixel1) * DCTbasis3x3[1][1]
                            + in.get(idx_pixel2) * DCTbasis3x3[1][2]));

                    out.set(idx_pixel2, (float) (in.get(idx_pixel0) * DCTbasis3x3[2][0]
                            + in.get(idx_pixel1) * DCTbasis3x3[2][1]
                            + in.get(idx_pixel2) * DCTbasis3x3[2][2]));

                }
            }
        }
        else if (flag == -1) {
            for (int j = 0; j < img.getHeight(); j++) {
                for (int i = 0; i < img.getHeight(); i++) {
                    int idx_pixel0 = j * img.getWidth() + i;
                    int idx_pixel1 = 1 * size1 + j * img.getWidth() + i;
                    int idx_pixel2 = 2 * size1 + j * img.getWidth() + i;
                    out.set(idx_pixel0, (float) (in.get(idx_pixel0) * DCTbasis3x3[0][0]
                            + in.get(idx_pixel1) * DCTbasis3x3[0][1]
                            + in.get(idx_pixel2) * DCTbasis3x3[0][2]));

                    out.set(idx_pixel1, (float) (in.get(idx_pixel0) * DCTbasis3x3[1][0]
                            + in.get(idx_pixel1) * DCTbasis3x3[1][1]
                            + in.get(idx_pixel2) * DCTbasis3x3[1][2]));

                    out.set(idx_pixel2, (float) (in.get(idx_pixel0) * DCTbasis3x3[2][0]
                            + in.get(idx_pixel1) * DCTbasis3x3[2][1]
                            + in.get(idx_pixel2) * DCTbasis3x3[2][2]));
                }
            }
        } else {
            System.out.print("Error: ColorTransform flag should be 1 (forward) or");
        }
    }

    private void Image2Patches() {
        System.out.println("Image2Patches");
    }

    private void Patches2Image() {
        System.out.println("Patches2Image");
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

    public void testColorTransform(BufferedImage in, BufferedImage out, int width, int height) {
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                Color c = new Color(in.getRGB(j, i));
                int r = c.getRed();
                int g = c.getGreen();
                int b = c.getBlue();

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

            }
        }
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

        this.ColorTransform();

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
