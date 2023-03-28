package mime;

import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;

import mime.model.MoreImageProcessor;
import mime.model.MoreImageProcessorImpl;

import static org.junit.Assert.assertEquals;

public abstract class AbstractMIPTest {


  protected final String src;
  protected String dst;
  protected final String format;
  protected MoreImageProcessor processor;

  protected AbstractMIPTest(String src, String dst, String format) {
    this.src = src;
    this.dst = dst;
    this.format = format;
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

  private void loadImageInvoker(String path, String name) throws IOException {
    if (path.endsWith(".ppm")) {
      processor.loadImage(path, name);
    } else {
      BufferedImage image = ImageIO.read(new File(path));
      processor.loadImage(image, name);
    }
  }

  private void assertLooper(int[][][] sample, String dst) throws IOException {
    int[][][] custom = readImage(dst);
    for (int row = 0; row < custom.length; row++) {
      for (int col = 0; col < custom[row].length; col++) {
        for (int rgb = 0; rgb < 3; rgb++) {
          try {
            assertEquals(sample[row][col][rgb], custom[row][col][rgb]);
          } catch (AssertionError e) {
            System.out.println(row + " " + col + " " + rgb);
            throw e;
          }
        }
      }
    }
  }


  @Test
  public void testLoadCat() throws FileNotFoundException, IOException {
    loadImageInvoker(src, "original");
    processor.save("original", dst);

    assertLooper(new int[][][]{
                    {{234, 232, 236}, {209, 194, 193}, {168, 150, 148}},
                    {{234, 230, 231}, {194, 184, 187}, {116, 99, 101}},
                    {{211, 203, 206}, {170, 150, 150}, {70, 42, 43}}
            }
            , dst);
  }

  @Test
  public void testLoadNotExist() throws FileNotFoundException, IOException {

    try {
      loadImageInvoker("res/nocat." + this.format, "original");
    } catch (FileNotFoundException e) {
      assertEquals("res" + File.separator + "nocat.ppm (No such file or directory)", e.getMessage());
    } catch (IIOException e) {
      assertEquals("Can't read input file!", e.getMessage());
    }
  }

  @Test(expected = IllegalStateException.class)
  public void testLoadInvalidExtension() throws FileNotFoundException, IOException {

    processor.loadImage("res/nocat.gif", "original");
  }

  @Test
  public void testLoadOverwrite() throws FileNotFoundException, IOException {

    loadImageInvoker("res/building.ppm", "original");
    loadImageInvoker(src, "original");
    loadImageInvoker("res/cat." + this.format, "original");
    processor.save("original", dst);

    assertLooper(new int[][][]{
                    {{234, 232, 236}, {209, 194, 193}, {168, 150, 148}},
                    {{234, 230, 231}, {194, 184, 187}, {116, 99, 101}},
                    {{211, 203, 206}, {170, 150, 150}, {70, 42, 43}}
            }
            , dst);
  }


  @Test
  public void testLoadDifferentImages() throws FileNotFoundException, IOException {

    loadImageInvoker("res/building.ppm", "building");
    loadImageInvoker("res/cat." + this.format, "original");

    processor.save("building", dst);
    assertLooper(new int[][][]{
                    {{167, 172, 180}, {163, 168, 177}, {161, 166, 175}},
                    {{169, 178, 164}, {169, 178, 164}, {169, 178, 164}},
                    {{183, 166, 171}, {184, 166, 173}, {186, 167, 174}}
            }
            , dst);

    processor.save("original", dst);
    assertLooper(new int[][][]{
                    {{234, 232, 236}, {209, 194, 193}, {168, 150, 148}},
                    {{234, 230, 231}, {194, 184, 187}, {116, 99, 101}},
                    {{211, 203, 206}, {170, 150, 150}, {70, 42, 43}}
            }
            , dst);
  }

  @Test(expected = IllegalStateException.class)
  public void testAdjustBrightnessNotExist() throws IOException {

    processor.loadImage(src, "original");
    processor.adjustBrightness("origin", 0, "brighter");
  }

  @Test
  public void testAdjustBrightnessPos() throws FileNotFoundException, IOException {
    int brightness = 30;

    loadImageInvoker(src, "original");
    processor.adjustBrightness("original", brightness, "brighter");
    processor.save("brighter", dst);

    assertLooper(new int[][][]{
                    {{255, 255, 255}, {239, 224, 223}, {198, 180, 178}},
                    {{255, 255, 255}, {224, 214, 217}, {146, 129, 131}},
                    {{241, 233, 236}, {200, 180, 180}, {100, 72, 73}}
            }
            , dst);
  }

  @Test
  public void testAdjustBrightnessNeg() throws FileNotFoundException, IOException {
    int brightness = -50;


    loadImageInvoker(src, "original");
    processor.adjustBrightness("original", brightness, "darker");
    processor.save("darker", dst);

    assertLooper(new int[][][]{
                    {{184, 182, 186}, {159, 144, 143}, {118, 100, 98}},
                    {{184, 180, 181}, {144, 134, 137}, {66, 49, 51}},
                    {{161, 153, 156}, {120, 100, 100}, {20, 0, 0}}
            }
            , dst);
  }

  @Test
  public void testHorizontalFlip() throws FileNotFoundException, IOException {

    loadImageInvoker(src, "original");
    processor.horizontalFlip("original", "horizontal");
    processor.save("horizontal", dst);

    assertLooper(new int[][][]{
                    {{168, 150, 148}, {209, 194, 193}, {234, 232, 236}},
                    {{116, 99, 101}, {194, 184, 187}, {234, 230, 231}},
                    {{70, 42, 43}, {170, 150, 150}, {211, 203, 206}}
            }
            , dst);
  }

  @Test
  public void testVerticalFlip() throws FileNotFoundException, IOException {

    loadImageInvoker(src, "original");
    processor.verticalFlip("original", "vertical");
    processor.save("vertical", dst);

    assertLooper(new int[][][]{
                    {{211, 203, 206}, {170, 150, 150}, {70, 42, 43}},
                    {{234, 230, 231}, {194, 184, 187}, {116, 99, 101}},
                    {{234, 232, 236}, {209, 194, 193}, {168, 150, 148}}
            }
            , dst);
  }

  @Test
  public void testGreyscaleRed() throws FileNotFoundException, IOException {

    loadImageInvoker(src, "original");
    processor.greyscale("red-component", "original", "red");
    processor.save("red", dst);

    assertLooper(new int[][][]{
                    {{234, 234, 234}, {209, 209, 209}, {168, 168, 168}},
                    {{234, 234, 234}, {194, 194, 194}, {116, 116, 116}},
                    {{211, 211, 211}, {170, 170, 170}, {70, 70, 70}}
            }
            , dst);
  }

  @Test
  public void testGreyscaleGreen() throws FileNotFoundException, IOException {

    loadImageInvoker(src, "original");
    processor.greyscale("green-component", "original", "green");
    processor.save("green", dst);

    assertLooper(new int[][][]{
                    {{232, 232, 232}, {194, 194, 194}, {150, 150, 150}},
                    {{230, 230, 230}, {184, 184, 184}, {99, 99, 99}},
                    {{203, 203, 203}, {150, 150, 150}, {42, 42, 42}}
            }
            , dst);
  }

  @Test
  public void testGreyscaleBlue() throws FileNotFoundException, IOException {

    loadImageInvoker(src, "original");
    processor.greyscale("blue-component", "original", "blue");
    processor.save("blue", dst);

    assertLooper(new int[][][]{
                    {{236, 236, 236}, {193, 193, 193}, {148, 148, 148}},
                    {{231, 231, 231}, {187, 187, 187}, {101, 101, 101}},
                    {{206, 206, 206}, {150, 150, 150}, {43, 43, 43}}
            }
            , dst);
  }

  @Test
  public void testGreyscaleValue() throws FileNotFoundException, IOException {

    loadImageInvoker(src, "original");
    processor.greyscale("value-component", "original", "value");
    processor.save("value", dst);

    assertLooper(new int[][][]{
                    {{236, 236, 236}, {209, 209, 209}, {168, 168, 168}},
                    {{234, 234, 234}, {194, 194, 194}, {116, 116, 116}},
                    {{211, 211, 211}, {170, 170, 170}, {70, 70, 70}}
            }
            , dst);
  }

  @Test
  public void testGreyscaleIntensity() throws FileNotFoundException, IOException {

    loadImageInvoker(src, "original");
    processor.greyscale("intensity-component", "original", "intensity");
    processor.save("intensity", dst);

    assertLooper(new int[][][]{
                    {{234, 234, 234}, {198, 198, 198}, {155, 155, 155}},
                    {{231, 231, 231}, {188, 188, 188}, {105, 105, 105}},
                    {{206, 206, 206}, {156, 156, 156}, {51, 51, 51}}
            }
            , dst);
  }

  @Test
  public void testGreyscaleLuma() throws FileNotFoundException, IOException {

    loadImageInvoker(src, "original");
    processor.greyscale("luma-component", "original", "luma");
    processor.save("luma", dst);

    assertLooper(new int[][][]{
                    {{232, 232, 232}, {197, 197, 197}, {153, 153, 153}},
                    {{230, 230, 230}, {186, 186, 186}, {102, 102, 102}},
                    {{204, 204, 204}, {154, 154, 154}, {48, 48, 48}}
            }
            , dst);
  }

  @Test
  public void testGreyScaleSepia() throws FileNotFoundException, IOException {

    loadImageInvoker(src, "original");
    processor.greyscale("sepia", "original", "sepia");
    processor.save("sepia", dst);

    assertLooper(new int[][][]{
                    {{255, 255, 218}, {255, 238, 185}, {209, 186, 145}},
                    {{255, 255, 216}, {253, 225, 175}, {140, 125, 97}},
                    {{255, 247, 192}, {210, 187, 145}, {67, 60, 47}}
            }
            , dst);
  }

  @Test
  public void testCombine() throws FileNotFoundException, IOException {
    loadImageInvoker("res/cat." + this.format, "original");
    processor.greyscale("red-component", "original", "red");
    processor.greyscale("green-component", "original", "green");
    processor.greyscale("blue-component", "original", "blue");
    processor.combines("red", "green", "blue", "combine");
    processor.save("combine", dst);

    assertLooper(new int[][][]{
                    {{234, 232, 236}, {209, 194, 193}, {168, 150, 148}},
                    {{234, 230, 231}, {194, 184, 187}, {116, 99, 101}},
                    {{211, 203, 206}, {170, 150, 150}, {70, 42, 43}}
            }
            , dst);
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidSaveExtension() throws FileNotFoundException, IOException {
    processor.loadImage(src, "original");
    processor.save("original", "/test/cat.gif");
  }


  @Test
  public void testDithering() throws IOException {
    loadImageInvoker(src, "original");
    processor.dithering("original", "dithering");
    processor.save("dithering", dst);

    assertLooper(new int[][][]{
                    {{255, 255, 255}, {255, 255, 255}, {255, 0, 0}},
                    {{255, 255, 255}, {255, 255, 255}, {0, 0, 0}},
                    {{255, 255, 255}, {0, 0, 0}, {0, 0, 0}}
            }
            , dst);
  }


  @Test
  public void testBlur() throws IOException {
    loadImageInvoker(src, "original");
    processor.filter("blur", "original", "blur");
    processor.save("blur", dst);

    assertLooper(new int[][][]{
                    {{126, 122, 123}, {148, 139, 140}, {94, 85, 85}},
                    {{162, 156, 157}, {182, 169, 170}, {106, 93, 93}},
                    {{115, 109, 110}, {123, 111, 112}, {65, 53, 53}}
            }
            , dst);
  }


  @Test
  public void testSharpening() throws IOException {
    loadImageInvoker(src, "original");
    processor.filter("sharpen", "original", "sharpen");
    processor.save("sharpen", dst);

//    assertLooper(new int[][][]{
//                    {{126, 122, 123}, {148, 139, 140}, {94, 85, 85}},
//                    {{162, 156, 157}, {182, 169, 170}, {106, 93, 93}},
//                    {{115, 109, 110}, {123, 111, 112}, {65, 53, 53}}
//            }
//            , dst);
  }


  @Test
  public void testSavePPM() throws IOException {
    dst = dst.substring(0, dst.lastIndexOf(".")).concat(".ppm");

    loadImageInvoker(src, "original");
    processor.save("original", dst);

    assertLooper(new int[][][]{
                    {{234, 232, 236}, {209, 194, 193}, {168, 150, 148}},
                    {{234, 230, 231}, {194, 184, 187}, {116, 99, 101}},
                    {{211, 203, 206}, {170, 150, 150}, {70, 42, 43}}
            }
            , dst);

    File myObj = new File(dst);
    myObj.delete();
  }

  @Test
  public void testSavePNG() throws IOException {
    dst = dst.substring(0, dst.lastIndexOf(".")).concat(".png");

    loadImageInvoker(src, "original");
    processor.save("original", dst);

    assertLooper(new int[][][]{
                    {{234, 232, 236}, {209, 194, 193}, {168, 150, 148}},
                    {{234, 230, 231}, {194, 184, 187}, {116, 99, 101}},
                    {{211, 203, 206}, {170, 150, 150}, {70, 42, 43}}
            }
            , dst);
    File myObj = new File(dst);
    myObj.delete();
  }

  @Test
  public void testSaveBMP() throws IOException {
    dst = dst.substring(0, dst.lastIndexOf(".")).concat(".bmp");

    loadImageInvoker(src, "original");
    processor.save("original", dst);

    assertLooper(new int[][][]{
                    {{234, 232, 236}, {209, 194, 193}, {168, 150, 148}},
                    {{234, 230, 231}, {194, 184, 187}, {116, 99, 101}},
                    {{211, 203, 206}, {170, 150, 150}, {70, 42, 43}}
            }
            , dst);
    File myObj = new File(dst);
    myObj.delete();
  }

}
