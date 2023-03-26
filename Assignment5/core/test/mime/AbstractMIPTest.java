package mime;

import org.junit.Before;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;

import mime.model.MoreImageProcessor;
import mime.model.MoreImageProcessorImpl;

import static org.junit.Assert.assertEquals;

public abstract class AbstractMIPTest {


  protected final String src;
  protected final String dst;
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

  private String readImage(String path) throws IOException {
    if (path.endsWith(".ppm")) {
      return readPPM(path);
    }
    return readBJP(path);
  }

  private String readPPM(String path) throws FileNotFoundException {

    Scanner sc = new Scanner(new FileInputStream(path));

    List<String> builder = new ArrayList<>();
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (s.charAt(0) != '#') {
        builder.add(s);
      }
    }
    return builder.toString();
  }

  private String readBJP(String path) throws IOException {
    BufferedImage image = ImageIO.read(new File(path));
    return Arrays.toString(image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth()));
  }

  private void loadImageInvoker(String path, String name) throws IOException {
    if (path.endsWith(".ppm")) {
      processor.loadImage(path, name);
    } else {
      BufferedImage image = ImageIO.read(new File(path));
      processor.loadImage(image, name);
    }
  }

  @Test
  public void testLoadCat() throws FileNotFoundException, IOException {

    loadImageInvoker("res/cat-brighter." + this.format, "brighter");
    processor.save("brighter", dst);

    String sample = readImage("res/cat-brighter." + this.format);
    String custom = readImage(dst);
    assertEquals(sample, custom);
  }

  @Test(expected = FileNotFoundException.class)
  public void testLoadNotExist() throws FileNotFoundException, IOException {

    loadImageInvoker("res/nocat." + this.format, "original");
  }

  @Test(expected = IllegalStateException.class)
  public void testLoadInvalidExtension() throws FileNotFoundException, IOException {

    processor.loadImage("res/nocat.gif", "original");
  }

  @Test
  public void testLoadOverwrite() throws FileNotFoundException, IOException {

    loadImageInvoker("res/building." + this.format, "original");
    loadImageInvoker(src, "original");
    loadImageInvoker("res/cat-brighter." + this.format, "original");
    processor.save("original", dst);

    String sample = readImage("res/cat-brighter." + this.format);
    String custom = readImage(dst);
    assertEquals(sample, custom);
  }

  @Test
  public void testLoadDifferentImages() throws FileNotFoundException, IOException {

    loadImageInvoker("res/cat-brighter." + this.format, "brighter");
    loadImageInvoker("res/cat-horizontal." + this.format, "horizontal");

    processor.save("brighter", dst);
    String sample = readImage("res/cat-brighter." + this.format);
    String custom = readImage(dst);
    assertEquals(sample, custom);

    processor.save("horizontal", dst);
    sample = readImage("res/cat-horizontal." + this.format);
    custom = readImage(dst);
    assertEquals(sample, custom);
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

    String sample = readImage("res/cat-brighter." + this.format);
    String custom = readImage(dst);
    assertEquals(sample, custom);
  }

  @Test
  public void testAdjustBrightnessNeg() throws FileNotFoundException, IOException {
    int brightness = -30;


    loadImageInvoker(src, "original");
    processor.adjustBrightness("original", brightness, "darker");
    processor.save("darker", dst);

    String sample = readImage("res/cat-darker." + this.format);
    String custom = readImage(dst);
    assertEquals(sample, custom);
  }

  @Test
  public void testHorizontalFlip() throws FileNotFoundException, IOException {

    loadImageInvoker(src, "original");
    processor.horizontalFlip("original", "horizontal");
    processor.save("horizontal", dst);

    String sample = readImage("res/cat-horizontal." + this.format);
    String custom = readImage(dst);
    assertEquals(sample, custom);
  }

  @Test
  public void testVerticalFlip() throws FileNotFoundException, IOException {

    loadImageInvoker(src, "original");
    processor.verticalFlip("original", "vertical");
    processor.save("vertical", dst);

    String sample = readImage("res/cat-vertical." + this.format);
    String custom = readImage(dst);
    assertEquals(sample, custom);
  }

  @Test
  public void testGreyscaleRed() throws FileNotFoundException, IOException {

    loadImageInvoker(src, "original");
    processor.greyscale("red-component", "original", "red");
    processor.save("red", dst);

    String sample = readImage("res/cat-red." + this.format);
    String custom = readImage(dst);
    assertEquals(sample, custom);
  }

  @Test
  public void testGreyscaleGreen() throws FileNotFoundException, IOException {

    loadImageInvoker(src, "original");
    processor.greyscale("green-component", "original", "green");
    processor.save("green", dst);

    String sample = readImage("res/cat-green." + this.format);
    String custom = readImage(dst);
    assertEquals(sample, custom);
  }

  @Test
  public void testGreyscaleBlue() throws FileNotFoundException, IOException {

    loadImageInvoker(src, "original");
    processor.greyscale("blue-component", "original", "blue");
    processor.save("blue", dst);

    String sample = readImage("res/cat-blue." + this.format);
    String custom = readImage(dst);
    assertEquals(sample, custom);
  }

  @Test
  public void testGreyscaleValue() throws FileNotFoundException, IOException {

    loadImageInvoker(src, "original");
    processor.greyscale("value-component", "original", "value");
    processor.save("value", dst);

    String sample = readImage("res/cat-value." + this.format);
    String custom = readImage(dst);
    assertEquals(sample, custom);
  }

  @Test
  public void testGreyscaleIntensity() throws FileNotFoundException, IOException {

    loadImageInvoker(src, "original");
    processor.greyscale("intensity-component", "original", "intensity");
    processor.save("intensity", dst);

    String sample = readImage("res/cat-intensity." + this.format);
    String custom = readImage(dst);
    assertEquals(sample, custom);
  }

  @Test
  public void testGreyscaleLuma() throws FileNotFoundException, IOException {

    loadImageInvoker(src, "original");
    processor.greyscale("luma-component", "original", "luma");
    processor.save("luma", dst);

    String sample = readImage("res/cat-luma." + this.format);
    String custom = readImage(dst);
    assertEquals(sample, custom);
  }

  @Test
  public void testGreyScaleSepia() throws FileNotFoundException, IOException {

    loadImageInvoker(src, "original");
    processor.greyscale("sepia", "original", "sepia");
    processor.save("sepia", dst);

    String sample = readImage("res/cat-sepia." + this.format);
    String custom = readImage(dst);
    assertEquals(sample, custom);
  }

  @Test
  public void testCombine() throws FileNotFoundException, IOException {
    loadImageInvoker("res/cat-brighter." + this.format, "original");
    processor.greyscale("red-component", "original", "red");
    processor.greyscale("green-component", "original", "green");
    processor.greyscale("blue-component", "original", "blue");
    processor.combines("red", "green", "blue", "combine");
    processor.save("combine", dst);

    String sample = readImage("res/cat-brighter." + this.format);
    String custom = readImage(dst);
    assertEquals(sample, custom);
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidSaveExtension() throws FileNotFoundException, IOException {
    processor.loadImage(src, "original");
    processor.save("original", "/test/cat.gif");
  }


}
