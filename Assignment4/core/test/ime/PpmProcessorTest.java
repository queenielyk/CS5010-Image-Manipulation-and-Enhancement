package ime;

import org.junit.AfterClass;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ime.model.ImageProcessor;
import ime.model.PpmProcessor;

import static org.junit.Assert.assertEquals;

/**
 * A test class for object PpmProcessor.
 */
public class PpmProcessorTest {

  private final String src = "res/cat.ppm";
  private final String dst = "res/processor.ppm";

  /**
   * A method to be executed at the end of the test class.
   * Remove image exported from this test class.
   */
  @AfterClass
  public static void removeDstPpm() {
    String dst = "res/processor.ppm";
    File myObj = new File(dst);
    myObj.delete();
  }

  private List<String> readPPM(String path) throws FileNotFoundException {
    Scanner sc = new Scanner(new FileInputStream(path));

    List<String> builder = new ArrayList<>();
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (s.charAt(0) != '#') {
        builder.add(s);
      }
    }
    return builder;
  }

  @Test
  public void testLoadCat() throws FileNotFoundException, IOException {
    ImageProcessor ppm = new PpmProcessor();
    ppm.loadImage("res/cat-brighter.ppm", "brighter");
    ppm.save("brighter", dst);

    List<String> sample = readPPM("res/cat-brighter.ppm");
    List<String> custom = readPPM(dst);
    assertEquals(sample.toString(), custom.toString());
  }

  @Test(expected = FileNotFoundException.class)
  public void testLoadNotExist() throws FileNotFoundException, IOException {
    ImageProcessor ppm = new PpmProcessor();
    ppm.loadImage("res/nocat.ppm", "original");
  }

  @Test
  public void testLoadOverwrite() throws FileNotFoundException, IOException {
    ImageProcessor ppm = new PpmProcessor();
    ppm.loadImage("res/building.ppm", "original");
    ppm.loadImage(src, "original");
    ppm.loadImage("res/cat-brighter.ppm", "original");
    ppm.save("original", dst);

    List<String> sample = readPPM("res/cat-brighter.ppm");
    List<String> custom = readPPM(dst);
    assertEquals(sample.toString(), custom.toString());
  }

  @Test
  public void testLoadDifferentImages() throws FileNotFoundException, IOException {
    ImageProcessor ppm = new PpmProcessor();
    ppm.loadImage("res/cat-brighter.ppm", "brighter");
    ppm.loadImage("res/cat-horizontal.ppm", "horizontal");

    ppm.save("brighter", dst);
    List<String> sample = readPPM("res/cat-brighter.ppm");
    List<String> custom = readPPM(dst);
    assertEquals(sample.toString(), custom.toString());

    ppm.save("horizontal", dst);
    sample = readPPM("res/cat-horizontal.ppm");
    custom = readPPM(dst);
    assertEquals(sample.toString(), custom.toString());
  }

  @Test(expected = IllegalStateException.class)
  public void testAdjustBrightnessNotExist() throws FileNotFoundException {
    ImageProcessor ppm = new PpmProcessor();
    ppm.loadImage(src, "original");
    ppm.adjustBrightness("origin", 0, "brighter");
  }

  @Test
  public void testAdjustBrightnessPos() throws FileNotFoundException, IOException {
    int brightness = 30;

    ImageProcessor ppm = new PpmProcessor();
    ppm.loadImage(src, "original");
    ppm.adjustBrightness("original", brightness, "brighter");
    ppm.save("brighter", dst);

    List<String> sample = readPPM("res/cat-brighter.ppm");
    List<String> custom = readPPM(dst);
    assertEquals(sample.toString(), custom.toString());
  }

  @Test
  public void testAdjustBrightnessNeg() throws FileNotFoundException, IOException {
    int brightness = -30;

    ImageProcessor ppm = new PpmProcessor();
    ppm.loadImage(src, "original");
    ppm.adjustBrightness("original", brightness, "darker");
    ppm.save("darker", dst);

    List<String> sample = readPPM("res/cat-darker.ppm");
    List<String> custom = readPPM(dst);
    assertEquals(sample.toString(), custom.toString());
  }

  @Test
  public void testHorizontalFlip() throws FileNotFoundException, IOException {
    ImageProcessor ppm = new PpmProcessor();
    ppm.loadImage(src, "original");
    ppm.horizontalFlip("original", "horizontal");
    ppm.save("horizontal", dst);

    List<String> sample = readPPM("res/cat-horizontal.ppm");
    List<String> custom = readPPM(dst);
    assertEquals(sample.toString(), custom.toString());
  }

  @Test
  public void testVerticalFlip() throws FileNotFoundException, IOException {
    ImageProcessor ppm = new PpmProcessor();
    ppm.loadImage(src, "original");
    ppm.verticalFlip("original", "vertical");
    ppm.save("vertical", dst);

    List<String> sample = readPPM("res/cat-vertical.ppm");
    List<String> custom = readPPM(dst);
    assertEquals(sample.toString(), custom.toString());
  }

  @Test
  public void testGreyscaleRed() throws FileNotFoundException, IOException {
    ImageProcessor ppm = new PpmProcessor();
    ppm.loadImage(src, "original");
    ppm.greyscale("red-component", "original", "red");
    ppm.save("red", dst);

    List<String> sample = readPPM("res/cat-red.ppm");
    List<String> custom = readPPM(dst);
    assertEquals(sample.toString(), custom.toString());
  }

  @Test
  public void testGreyscaleGreen() throws FileNotFoundException, IOException {
    ImageProcessor ppm = new PpmProcessor();
    ppm.loadImage(src, "original");
    ppm.greyscale("green-component", "original", "green");
    ppm.save("green", dst);

    List<String> sample = readPPM("res/cat-green.ppm");
    List<String> custom = readPPM(dst);
    assertEquals(sample.toString(), custom.toString());
  }

  @Test
  public void testGreyscaleBlue() throws FileNotFoundException, IOException {
    ImageProcessor ppm = new PpmProcessor();
    ppm.loadImage(src, "original");
    ppm.greyscale("blue-component", "original", "blue");
    ppm.save("blue", dst);

    List<String> sample = readPPM("res/cat-blue.ppm");
    List<String> custom = readPPM(dst);
    assertEquals(sample.toString(), custom.toString());
  }

  @Test
  public void testGreyscaleValue() throws FileNotFoundException, IOException {
    ImageProcessor ppm = new PpmProcessor();
    ppm.loadImage(src, "original");
    ppm.greyscale("value-component", "original", "value");
    ppm.save("value", dst);

    List<String> sample = readPPM("res/cat-value.ppm");
    List<String> custom = readPPM(dst);
    assertEquals(sample.toString(), custom.toString());
  }

  @Test
  public void testGreyscaleIntensity() throws FileNotFoundException, IOException {
    ImageProcessor ppm = new PpmProcessor();
    ppm.loadImage(src, "original");
    ppm.greyscale("intensity-component", "original", "intensity");
    ppm.save("intensity", dst);

    List<String> sample = readPPM("res/cat-intensity.ppm");
    List<String> custom = readPPM(dst);
    assertEquals(sample.toString(), custom.toString());
  }

  @Test
  public void testGreyscaleLuma() throws FileNotFoundException, IOException {
    ImageProcessor ppm = new PpmProcessor();
    ppm.loadImage(src, "original");
    ppm.greyscale("luma-component", "original", "luma");
    ppm.save("luma", dst);

    List<String> sample = readPPM("res/cat-luma.ppm");
    List<String> custom = readPPM(dst);
    assertEquals(sample.toString(), custom.toString());
  }

  @Test
  public void testCombine() throws FileNotFoundException, IOException {
    ImageProcessor ppm = new PpmProcessor();
    ppm.loadImage("res/cat-brighter.ppm", "original");
    ppm.greyscale("red-component", "original", "red");
    ppm.greyscale("green-component", "original", "green");
    ppm.greyscale("blue-component", "original", "blue");
    ppm.combines("red", "green", "blue", "combine");
    ppm.save("combine", dst);

    List<String> sample = readPPM("res/cat-brighter.ppm");
    List<String> custom = readPPM(dst);
    assertEquals(sample.toString(), custom.toString());
  }

}