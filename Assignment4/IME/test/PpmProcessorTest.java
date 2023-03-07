import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class PpmProcessorTest {

  @Test
  public void testLoad() throws FileNotFoundException, IllegalArgumentException, IOException {
    ImageProcessor ppm = new PpmProcessor();
    ppm.loadImage(System.getProperty("user.dir") + "/test/smallBackground.ppm", "original");
    ppm.save("original", System.getProperty("user.dir") + "/test/newSmallBackground.ppm");
  }
}