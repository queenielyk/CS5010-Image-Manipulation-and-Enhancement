package mime;

import static org.junit.Assert.assertEquals;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import javax.imageio.ImageIO;
import mime.model.ImageHandler;
import mime.model.ImageIOHandler;
import mime.model.PpmHandler;
import org.junit.Test;

public class ImageHandlerTest {

  InputStream stream;
  ImageHandler reader;


  public ImageHandlerTest() {
  }

  private int[][][] readImage(String path) throws IOException {
    if (path.endsWith(".ppm")) {
      return readPPM(path);
    }
    return readBJP(path);
  }

  private int[][][] readPPM(String path) throws FileNotFoundException {

    Scanner sc = new Scanner(new FileInputStream(path));

    sc.nextLine();
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

    int[][][] imageArray = new int[height][width][3];
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
            imageArray[row][col][count] = Integer.parseInt(ss);
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
    return imageArray;
  }

  private int[][][] readBJP(String path) throws IOException {
    BufferedImage image = ImageIO.read(new File(path));
    int width = image.getWidth();
    int height = image.getHeight();

    int[] dataBuffInt = image.getRGB(0, 0, width, height, null, 0, width);
    int[][][] imageArray = new int[height][width][3];
    int row = 0;
    int col = 0;

    for (int rgb : dataBuffInt) {
      int red = (rgb >> 16) & 0xFF;
      int green = (rgb >> 8) & 0xFF;
      int blue = (rgb) & 0xFF;
      imageArray[row][col] = new int[]{red, green, blue};
      col++;
      if (col == width) {
        col = 0;
        row++;
      }
    }
    return imageArray;
  }

  private void assertLooper(int[][][] sample, int[][][] image) throws IOException {
    for (int row = 0; row < image.length; row++) {
      for (int col = 0; col < image[row].length; col++) {
        for (int rgb = 0; rgb < 3; rgb++) {
          try {
            assertEquals(sample[row][col][rgb], image[row][col][rgb]);
          } catch (AssertionError e) {
            System.out.println(row + " " + col + " " + rgb);
            throw e;
          }
        }
      }
    }
  }

  private void assertLooper(int[] sample, int[] image) throws IOException {
    for (int col = 0; col < image.length; col++) {
      try {
        assertEquals(sample[col], image[col]);
      } catch (AssertionError e) {
        System.out.println(col + " ");
        throw e;
      }
    }
  }


  @Test
  public void testImageIOReader() throws IOException {
    this.stream = new FileInputStream("res/format/cat.png");
    this.reader = new ImageIOHandler();
    this.reader.readImage(stream);
    assertLooper(new int[][][]{
            {{234, 232, 236}, {209, 194, 193}, {168, 150, 148}},
            {{234, 230, 231}, {194, 184, 187}, {116, 99, 101}},
            {{211, 203, 206}, {170, 150, 150}, {70, 42, 43}}
        }
        , this.reader.getImage());
    assertLooper(new int[]{3, 3, 255}, this.reader.getInfo());
  }


  @Test
  public void testPpmReader() throws IOException {
    this.stream = new FileInputStream("res/cat.ppm");
    this.reader = new PpmHandler();
    this.reader.readImage(stream);
    assertLooper(new int[][][]{
            {{234, 232, 236}, {209, 194, 193}, {168, 150, 148}},
            {{234, 230, 231}, {194, 184, 187}, {116, 99, 101}},
            {{211, 203, 206}, {170, 150, 150}, {70, 42, 43}}
        }
        , this.reader.getImage());
    assertLooper(new int[]{3, 3, 255}, this.reader.getInfo());
  }


}