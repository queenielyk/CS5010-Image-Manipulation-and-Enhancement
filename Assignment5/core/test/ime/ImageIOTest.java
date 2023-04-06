package ime;

import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

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

}