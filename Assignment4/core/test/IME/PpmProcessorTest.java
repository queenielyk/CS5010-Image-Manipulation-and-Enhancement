package IME;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import IME.model.ImageProcessor;
import IME.model.PpmProcessor;

import static org.junit.Assert.assertEquals;

public class PpmProcessorTest {

  private List<String> readPPM(String path) throws FileNotFoundException {
    Scanner sc = new Scanner(new FileInputStream(System.getProperty("user.dir") + path));

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
  public void testLoad5x5() throws FileNotFoundException, IOException {
    ImageProcessor ppm = new PpmProcessor();
    ppm.loadImage("/test/IME/smallBackground.ppm", "original");
    ppm.save("original", "/test/IME/newSmallBackground.ppm");
  }

  @Test
  public void testLoadKoala() throws FileNotFoundException, IOException {
    ImageProcessor ppm = new PpmProcessor();
    ppm.loadImage("/test/IME/Koala.ppm", "original");
    ppm.save("original", "/test/IME/newKoala.ppm");
  }

  @Test(expected = IllegalStateException.class)
  public void testAdjustBrightnessNotExist() throws FileNotFoundException {
    ImageProcessor ppm = new PpmProcessor();
    ppm.loadImage("/test/IME/Koala.ppm", "original");
    ppm.adjustBrightness("origin", 0, "brighter");
  }

  @Test
  public void testAdjustBrightnessPos() throws FileNotFoundException, IOException {
    int brightness = 18;
    ImageProcessor ppm = new PpmProcessor();
    ppm.loadImage("/test/IME/Koala.ppm", "original");
    ppm.adjustBrightness("original", brightness, "brighter");
    ppm.save("brighter", "/test/IME/brighterKoala.ppm");

    List<String> ppmBefore = readPPM("/test/IME/Koala.ppm");
    List<String> ppmAfter = readPPM("/test/IME/brighterKoala.ppm");

    int maxValue = Integer.parseInt(ppmAfter.get(2));
    for (int channel = 3; channel < ppmAfter.size(); channel++) {
      assertEquals(Math.min(Integer.parseInt(ppmBefore.get(channel)) + brightness, maxValue), Integer.parseInt(ppmAfter.get(channel)));
    }
  }

  @Test
  public void testAdjustBrightnessPoNeg() throws FileNotFoundException, IOException {
    int brightness = -30;
    ImageProcessor ppm = new PpmProcessor();
    ppm.loadImage("/test/IME/Koala.ppm", "original");
    ppm.adjustBrightness("original", brightness, "brighter");
    ppm.save("brighter", "/test/IME/dimmerKoala.ppm");

    List<String> ppmBefore = readPPM("/test/IME/Koala.ppm");
    List<String> ppmAfter = readPPM("/test/IME/dimmerKoala.ppm");

    int maxValue = Integer.parseInt(ppmAfter.get(2));
    for (int channel = 3; channel < ppmAfter.size(); channel++) {
      assertEquals(Math.max(0, Math.min(Integer.parseInt(ppmBefore.get(channel)) + brightness, maxValue)),
              Integer.parseInt(ppmAfter.get(channel))
      );
    }
  }

  @Test
  public void testHorizontalFlip() throws FileNotFoundException, IOException {
    ImageProcessor ppm = new PpmProcessor();
    ppm.loadImage("/test/IME/flowers.ppm", "original");
    ppm.horizontalFlip("original", "horizontal");
    ppm.save("horizontal", "/test/IME/customHorizontalFlowers.ppm");

    List<String> sample = readPPM("/test/IME/flowers-horizontal.ppm");
    List<String> custom = readPPM("/test/IME/customHorizontalFlowers.ppm");
    assertEquals(sample.toString(), custom.toString());
  }

  @Test
  public void testVerticalFlip() throws FileNotFoundException, IOException {
    ImageProcessor ppm = new PpmProcessor();
    ppm.loadImage("/test/IME/flowers.ppm", "original");
    ppm.verticalFlip("original", "vertical");
    ppm.save("vertical", "/test/IME/customVerticalFlowers.ppm");

    List<String> sample = readPPM("/test/IME/flowers-vertical.ppm");
    List<String> custom = readPPM("/test/IME/customVerticalFlowers.ppm");
    assertEquals(sample.toString(), custom.toString());
  }

  @Test
  public void testGreyscaleRed() throws FileNotFoundException, IOException {
    ImageProcessor ppm = new PpmProcessor();
    ppm.loadImage("/test/IME/flowers.ppm", "original");
    ppm.greyscale("red-component", "original", "red");
    ppm.save("red", "/test/IME/customRedFlowers.ppm");

    List<String> sample = readPPM("/test/IME/flowers-red.ppm");
    List<String> custom = readPPM("/test/IME/customRedFlowers.ppm");
    assertEquals(sample.toString(), custom.toString());
  }

  @Test
  public void testGreyscaleGreen() throws FileNotFoundException, IOException {
    ImageProcessor ppm = new PpmProcessor();
    ppm.loadImage("/test/IME/flowers.ppm", "original");
    ppm.greyscale("green-component", "original", "green");
    ppm.save("green", "/test/IME/customGreenFlowers.ppm");

    List<String> sample = readPPM("/test/IME/flowers-green.ppm");
    List<String> custom = readPPM("/test/IME/customGreenFlowers.ppm");
    assertEquals(sample.toString(), custom.toString());
  }

  @Test
  public void testGreyscaleBlue() throws FileNotFoundException, IOException {
    ImageProcessor ppm = new PpmProcessor();
    ppm.loadImage("/test/IME/flowers.ppm", "original");
    ppm.greyscale("blue-component", "original", "blue");
    ppm.save("blue", "/test/IME/customBlueFlowers.ppm");

    List<String> sample = readPPM("/test/IME/flowers-blue.ppm");
    List<String> custom = readPPM("/test/IME/customBlueFlowers.ppm");
    assertEquals(sample.toString(), custom.toString());
  }

  @Test
  public void testGreyscaleValue() throws FileNotFoundException, IOException {
    ImageProcessor ppm = new PpmProcessor();
    ppm.loadImage("/test/IME/flowers.ppm", "original");
    ppm.greyscale("value-component", "original", "value");
    ppm.save("value", "/test/IME/customValueFlowers.ppm");

    List<String> sample = readPPM("/test/IME/flowers-value.ppm");
    List<String> custom = readPPM("/test/IME/customValueFlowers.ppm");
    assertEquals(sample.toString(), custom.toString());
  }
  
  @Test
  public void testGreyscaleIntensity() throws FileNotFoundException, IOException {
    ImageProcessor ppm = new PpmProcessor();
    ppm.loadImage("/test/IME/flowers.ppm", "original");
    ppm.greyscale("intensity-component", "original", "intensity");
    ppm.save("intensity", "/test/IME/customIntensityFlowers.ppm");

    List<String> sample = readPPM("/test/IME/flowers-intensity.ppm");
    List<String> custom = readPPM("/test/IME/customIntensityFlowers.ppm");
    assertEquals(sample.toString(), custom.toString());
  }

  @Test
  public void testGreyscaleLuma() throws FileNotFoundException, IOException {
    ImageProcessor ppm = new PpmProcessor();
    ppm.loadImage("/test/IME/flowers.ppm", "original");
    ppm.greyscale("luma-component", "original", "luma");
    ppm.save("luma", "/test/IME/customLumaFlowers.ppm");

    List<String> sample = readPPM("/test/IME/flowers-luma.ppm");
    List<String> custom = readPPM("/test/IME/customLumaFlowers.ppm");
    assertEquals(sample.toString(), custom.toString());
  }

  @Test
  public void testCombine() throws FileNotFoundException, IOException {
    ImageProcessor ppm = new PpmProcessor();
    ppm.loadImage("/test/IME/flowers.ppm", "original");
    ppm.greyscale("red-component", "original", "red");
    ppm.greyscale("green-component", "original", "green");
    ppm.greyscale("blue-component", "original", "blue");
    ppm.combines("red", "green", "blue", "combine");
    ppm.save("combine", "/test/IME/customCombineFlowers.ppm");

    List<String> sample = readPPM("/test/IME/flowers.ppm");
    List<String> custom = readPPM("/test/IME/customCombineFlowers.ppm");
    assertEquals(sample.toString(), custom.toString());
  }

}