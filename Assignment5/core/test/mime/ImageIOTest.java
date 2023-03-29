package mime;

import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import javax.imageio.ImageIO;

import mime.model.MoreImageProcessor;
import mime.model.MoreImageProcessorImpl;

import static org.junit.Assert.assertTrue;

public class ImageIOTest {

  @Test
  public void testReadingToBufferImage() {
    try {
      BufferedImage image = ImageIO.read(new File("res/test.png"));
      int width = image.getWidth();
      int height = image.getHeight();
      System.out.println("Width: " + width + " Height: " + height);

      int[] dataBuffInt = image.getRGB(0, 0, width, height, null, 0, width);
      System.out.println(dataBuffInt.length);
      for (int rgb : dataBuffInt) {
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = (rgb) & 0xFF;
        System.out.println("RGB: " + red + " " + green + " " + blue);
      }

    } catch (IOException e) {

    }
  }

  @Test
  public void testInvokeGenericFunction() throws IOException {

    int[][][] ppmImage = readPPM("res/cat-blue.ppm");

    for (int row = 0; row < 160; row++) {
      for (int col = 0; col < 240; col++) {
        System.out.println(ppmImage[row][col][0] + " " + ppmImage[row][col][1] + " " + ppmImage[row][col][2]);
      }
    }

  }

  private int[][][] readPPM(String path) throws FileNotFoundException {
    Scanner sc = new Scanner(new FileInputStream(path));

    if (!sc.nextLine().equals("P3")) {
      throw new IllegalStateException("Invalid PPM file: plain RAW file should begin with P3");
    }

    String s = sc.nextLine();
    if (s.charAt(0) == '#') {
      s = sc.nextLine();
    }
    //Dimension
    String[] splited = s.split(" ");
    int width = Integer.parseInt(splited[0]);
    int height = Integer.parseInt(splited[1]);
    //Maxi Value
    int maxi = Integer.parseInt(sc.nextLine());


    int[][][] rgbBuffer = new int[height][width][3];
    int row = 0;
    int col = 0;
    int count = 0;

    while (sc.hasNextLine()) {
      s = sc.nextLine();
      if (s.isEmpty()) {
        continue;
      }
      if (s.charAt(0) != '#') {
        splited = s.split(" ");
        for (String ss : splited) {
          if (!ss.equals("")) {
            rgbBuffer[row][col][count] = Integer.parseInt(ss);
            count++;
            if (count == 3) {
              count = 0;
              col++;
              if (col == width) {
                col = 0;
                row++;
              }
            }
          }
        }
      }
    }
    return rgbBuffer;
  }

  @Test
  public void testVerifyFormat() {
    MoreImageProcessor processor = new MoreImageProcessorImpl();
    assertTrue(processor.verifyFormat("res/harbour.ppm"));
    assertTrue(processor.verifyFormat("res/harbour.jpg"));
    assertTrue(processor.verifyFormat("res/harbour.jpeg"));
    assertTrue(processor.verifyFormat("res/harbour.png"));
    assertTrue(processor.verifyFormat("res/harbour.bmp"));
  }

  @Test
  public void testDeepCopy3DArray() throws IOException {
    int[][][] array = new int[3][3][2];
    for (int row = 0; row < 3; row++) {
      for (int col = 0; col < 3; col++) {
        array[row][col] = new int[]{row, col};
      }
    }

    int[][][] copied = new int[3][3][];
    for (int row = 0; row < 3; row++) {
      copied[row] = array[row].clone();
      for (int col = 0; col < 3; col++) {
        copied[row][col] = array[row][col].clone();
      }
    }

    System.out.println(copied[2][2] + " " + array[2][2]);
    copied[2][2][0] = 10;
    copied[2][2][1] = 10;
    System.out.println(copied[2][2][0] + " " + copied[2][2][1] + " " + array[2][2][0] + " " + array[2][2][1]);
    System.out.println(copied[2][2] + " " + array[2][2]);

  }

  @Test
  public void testMathRound() {
    System.out.println(Math.round((float) 200 / 255));
  }

  @Test
  public void testDiv() {
    System.out.println((209+194+193)/3);
  }


  @Test
  public void testLoopMatrix() {
    float[][] sharpening = {
            {(float) -1 / 8, (float) -1 / 8, (float) -1 / 8, (float) -1 / 8, (float) -1 / 8},
            {(float) -1 / 8, (float) 1 / 4, (float) 1 / 4, (float) 1 / 4, (float) -1 / 8},
            {(float) -1 / 8, (float) 1 / 4, 1, (float) 1 / 4, (float) -1 / 8},
            {(float) -1 / 8, (float) 1 / 4, (float) 1 / 4, (float) 1 / 4, (float) -1 / 8},
            {(float) -1 / 8, (float) -1 / 8, (float) -1 / 8, (float) -1 / 8, (float) -1 / 8}
    };

    int[][] filtermatrix = {
            {2, 1, 2},
            {-1, 0, 2},
            {1, 1, 1}
    };

    int[][] target = {
            {1, 6, 4, 5,},
            {8, 7, 9, 0},
            {43, 54, 56, 76},
            {65, 43, 123, 32}
    };

    int width = 4;
    int height = 4;
    int halfmatrix = (filtermatrix.length - 1) / 2;

    int row = 2;
    int col = 3;

    for (int fRow = Math.abs(Math.min(row - halfmatrix, 0)); fRow < ((row + halfmatrix) >= height ? height - row + halfmatrix : filtermatrix.length); fRow++) {
      for (int fCol = Math.abs(Math.min(col - halfmatrix, 0)); fCol < ((col + halfmatrix) >= width ? width - col + halfmatrix : filtermatrix.length); fCol++) {
        System.out.println(target[row - halfmatrix + fRow][col - halfmatrix + fCol] + " " + filtermatrix[fRow][fCol]);
      }
    }
  }

}