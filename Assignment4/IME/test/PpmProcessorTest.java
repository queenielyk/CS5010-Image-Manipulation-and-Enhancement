import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class PpmProcessorTest {

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
  public void testLoad5x5() throws FileNotFoundException, IllegalArgumentException, IOException {
    ImageProcessor ppm = new PpmProcessor();
    ppm.loadImage(System.getProperty("user.dir") + "/test/smallBackground.ppm", "original");
    ppm.save("original", System.getProperty("user.dir") + "/test/newSmallBackground.ppm");
  }

  @Test
  public void testLoadKoala() throws FileNotFoundException, IllegalArgumentException, IOException {
    ImageProcessor ppm = new PpmProcessor();
    ppm.loadImage(System.getProperty("user.dir") + "/test/Koala.ppm", "original");
    ppm.save("original", System.getProperty("user.dir") + "/test/newKoala.ppm");
  }

  @Test
  public void testAdjustBrightnessPos() throws FileNotFoundException, IOException {
    int brightness = 18;
    ImageProcessor ppm = new PpmProcessor();
    ppm.loadImage(System.getProperty("user.dir") + "/test/Koala.ppm", "original");
    ppm.adjustBrightness("original", brightness, "brighter");
    ppm.save("brighter", System.getProperty("user.dir") + "/test/brighterKoala.ppm");

    List<String> ppmBefore = readPPM(System.getProperty("user.dir") + "/test/Koala.ppm");
    List<String> ppmAfter = readPPM(System.getProperty("user.dir") + "/test/brighterKoala.ppm");

    int maxValue = Integer.parseInt(ppmAfter.get(2));
    for (int channel = 3; channel < ppmAfter.size(); channel++) {
      assertEquals(Math.min(Integer.parseInt(ppmBefore.get(channel)) + brightness, maxValue), Integer.parseInt(ppmAfter.get(channel)));
    }
  }


}