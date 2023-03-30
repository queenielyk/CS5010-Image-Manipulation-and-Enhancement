package mime;

import static org.junit.Assert.assertEquals;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Scanner;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;

import mime.model.MoreImageProcessor;
import mime.model.MoreImageProcessorImpl;

import org.junit.Before;
import org.junit.Test;

public abstract class AbstractMIPTest {


  protected final String src;
  protected final String format;
  protected String dst;
  protected MoreImageProcessor processor;
  OutputStream outputStream;

  protected AbstractMIPTest(String src, String dst, String format) throws FileNotFoundException {
    this.src = src;
    this.dst = dst;
    this.format = format;
    this.outputStream = new FileOutputStream(dst);
  }

  @Before
  public void setUp() {
    processor = new MoreImageProcessorImpl();
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

  protected abstract void loadImageInvoker(String path, String name) throws IOException;

  protected void assertLooper(int[][][] sample, int[][][] image) {
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


  @Test
  public void testLoadCat() throws IOException {
    loadImageInvoker(src, "original");

    assertLooper(new int[][][]{
                    {{234, 232, 236}, {209, 194, 193}, {168, 150, 148}},
                    {{234, 230, 231}, {194, 184, 187}, {116, 99, 101}},
                    {{211, 203, 206}, {170, 150, 150}, {70, 42, 43}}
            }
            , processor.getImage("original"));
  }

  @Test
  public void testLoadNotExist() throws IOException {

    try {
      loadImageInvoker("res/nocat." + this.format, "original");
    } catch (FileNotFoundException e) {
      assertEquals("res" + File.separator + "nocat." + this.format + " (No such file or directory)",
              e.getMessage());
    } catch (IIOException e) {
      assertEquals("Can't read input file!", e.getMessage());
    }
  }

  @Test(expected = IllegalStateException.class)
  public void testDeprecatedLoad() throws IOException {
    processor.loadImage("res/nocat.gif", "original");
  }

  @Test(expected = IllegalStateException.class)
  public void testAdjustBrightnessNotExist() throws IOException {

    processor.loadImage(src, "original");
    processor.adjustBrightness("origin", 0, "brighter");
  }

  @Test
  public void testAdjustBrightnessPos() throws IOException {
    int brightness = 30;

    loadImageInvoker(src, "original");
    processor.adjustBrightness("original", brightness, "brighter");

    assertLooper(new int[][][]{
                    {{255, 255, 255}, {239, 224, 223}, {198, 180, 178}},
                    {{255, 255, 255}, {224, 214, 217}, {146, 129, 131}},
                    {{241, 233, 236}, {200, 180, 180}, {100, 72, 73}}
            }
            , processor.getImage("brighter"));
  }

  @Test
  public void testAdjustBrightnessNeg() throws IOException {
    int brightness = -50;

    loadImageInvoker(src, "original");
    processor.adjustBrightness("original", brightness, "darker");

    assertLooper(new int[][][]{
                    {{184, 182, 186}, {159, 144, 143}, {118, 100, 98}},
                    {{184, 180, 181}, {144, 134, 137}, {66, 49, 51}},
                    {{161, 153, 156}, {120, 100, 100}, {20, 0, 0}}
            }
            , processor.getImage("darker"));
  }

  @Test
  public void testHorizontalFlip() throws IOException {

    loadImageInvoker(src, "original");
    processor.horizontalFlip("original", "horizontal");

    assertLooper(new int[][][]{
                    {{168, 150, 148}, {209, 194, 193}, {234, 232, 236}},
                    {{116, 99, 101}, {194, 184, 187}, {234, 230, 231}},
                    {{70, 42, 43}, {170, 150, 150}, {211, 203, 206}}
            }
            , processor.getImage("horizontal"));
  }

  @Test
  public void testVerticalFlip() throws IOException {

    loadImageInvoker(src, "original");
    processor.verticalFlip("original", "vertical");

    assertLooper(new int[][][]{
                    {{211, 203, 206}, {170, 150, 150}, {70, 42, 43}},
                    {{234, 230, 231}, {194, 184, 187}, {116, 99, 101}},
                    {{234, 232, 236}, {209, 194, 193}, {168, 150, 148}}
            }
            , processor.getImage("vertical"));
  }

  @Test
  public void testGreyscaleRed() throws IOException {

    loadImageInvoker(src, "original");
    processor.greyscale("red-component", "original", "red");

    assertLooper(new int[][][]{
                    {{234, 234, 234}, {209, 209, 209}, {168, 168, 168}},
                    {{234, 234, 234}, {194, 194, 194}, {116, 116, 116}},
                    {{211, 211, 211}, {170, 170, 170}, {70, 70, 70}}
            }
            , processor.getImage("red"));
  }

  @Test
  public void testGreyscaleGreen() throws IOException {

    loadImageInvoker(src, "original");
    processor.greyscale("green-component", "original", "green");

    assertLooper(new int[][][]{
                    {{232, 232, 232}, {194, 194, 194}, {150, 150, 150}},
                    {{230, 230, 230}, {184, 184, 184}, {99, 99, 99}},
                    {{203, 203, 203}, {150, 150, 150}, {42, 42, 42}}
            }
            , processor.getImage("green"));
  }

  @Test
  public void testGreyscaleBlue() throws IOException {

    loadImageInvoker(src, "original");
    processor.greyscale("blue-component", "original", "blue");

    assertLooper(new int[][][]{
                    {{236, 236, 236}, {193, 193, 193}, {148, 148, 148}},
                    {{231, 231, 231}, {187, 187, 187}, {101, 101, 101}},
                    {{206, 206, 206}, {150, 150, 150}, {43, 43, 43}}
            }
            , processor.getImage("blue"));
  }

  @Test
  public void testGreyscaleValue() throws IOException {

    loadImageInvoker(src, "original");
    processor.greyscale("value-component", "original", "value");

    assertLooper(new int[][][]{
                    {{236, 236, 236}, {209, 209, 209}, {168, 168, 168}},
                    {{234, 234, 234}, {194, 194, 194}, {116, 116, 116}},
                    {{211, 211, 211}, {170, 170, 170}, {70, 70, 70}}
            }
            , processor.getImage("value"));
  }

  @Test
  public void testGreyscaleIntensity() throws IOException {

    loadImageInvoker(src, "original");
    processor.greyscale("intensity-component", "original", "intensity");

    assertLooper(new int[][][]{
                    {{234, 234, 234}, {198, 198, 198}, {155, 155, 155}},
                    {{231, 231, 231}, {188, 188, 188}, {105, 105, 105}},
                    {{206, 206, 206}, {156, 156, 156}, {51, 51, 51}}
            }
            , processor.getImage("intensity"));
  }

  @Test
  public void testGreyscaleLuma() throws IOException {

    loadImageInvoker(src, "original");
    processor.greyscale("luma-component", "original", "luma");

    assertLooper(new int[][][]{
                    {{232, 232, 232}, {197, 197, 197}, {153, 153, 153}},
                    {{230, 230, 230}, {186, 186, 186}, {102, 102, 102}},
                    {{204, 204, 204}, {154, 154, 154}, {48, 48, 48}}
            }
            , processor.getImage("luma"));
  }

  @Test
  public void testGreyScaleSepia() throws IOException {

    loadImageInvoker(src, "original");
    processor.greyscale("sepia", "original", "sepia");

    assertLooper(new int[][][]{
                    {{255, 255, 218}, {255, 238, 185}, {209, 186, 145}},
                    {{255, 255, 216}, {253, 225, 175}, {140, 125, 97}},
                    {{255, 247, 192}, {210, 187, 145}, {67, 60, 47}}
            }
            , processor.getImage("sepia"));
  }

  @Test
  public void testCombine() throws IOException {
    loadImageInvoker(src, "original");
    processor.greyscale("red-component", "original", "red");
    processor.greyscale("green-component", "original", "green");
    processor.greyscale("blue-component", "original", "blue");
    processor.combines("red", "green", "blue", "combine");

    assertLooper(new int[][][]{
                    {{234, 232, 236}, {209, 194, 193}, {168, 150, 148}},
                    {{234, 230, 231}, {194, 184, 187}, {116, 99, 101}},
                    {{211, 203, 206}, {170, 150, 150}, {70, 42, 43}}
            }
            , processor.getImage("combine"));
  }


  @Test
  public void testDithering() throws IOException {
    loadImageInvoker(src, "original");
    processor.dithering("original", "dithering");

    assertLooper(new int[][][]{
                    {{255, 255, 255}, {255, 255, 255}, {0, 0, 0}},
                    {{255, 255, 255}, {255, 255, 255}, {0, 0, 0}},
                    {{255, 255, 255}, {0, 0, 0}, {0, 0, 0}}
            }
            , processor.getImage("dithering"));
  }


  @Test
  public void testBlur() throws IOException {
    loadImageInvoker(src, "original");
    processor.filter("blur", "original", "blur");

    assertLooper(new int[][][]{
                    {{126, 122, 123}, {148, 139, 140}, {94, 85, 85}},
                    {{162, 156, 157}, {182, 169, 170}, {106, 93, 93}},
                    {{115, 109, 110}, {123, 111, 112}, {65, 53, 53}}
            }
            , processor.getImage("blur"));
  }


  @Test
  public void testSharpening() throws IOException {
    loadImageInvoker(src, "original");
    processor.filter("sharpen", "original", "sharpen");

    assertLooper(new int[][][]{
                    {{255, 255, 255}, {255, 255, 255}, {182, 162, 160}},
                    {{255, 255, 255}, {255, 255, 255}, {233, 195, 197}},
                    {{255, 254, 255}, {255, 255, 255}, {58, 24, 25}}
            }
            , processor.getImage("sharpen"));
  }

}
