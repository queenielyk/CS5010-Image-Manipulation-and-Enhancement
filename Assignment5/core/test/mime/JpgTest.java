package mime;

import org.junit.AfterClass;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import mime.model.MoreImageProcessor;
import mime.model.MoreImageProcessorImpl;

public class JpgTest {

  /**
   * A method to be executed at the end of the test class. Remove image exported from this test
   * class.
   */
  @AfterClass
  public static void removeDstPpm() {
    for (String dst : new String[]{"res/processor.jpeg", "res/processor.jpg"}) {
      File myObj = new File(dst);
      myObj.delete();
    }
  }

  @Test
  public void testLoadJPEG() throws FileNotFoundException, IOException {
    String dst = "res/processor.jpeg";
    MoreImageProcessor processor = new MoreImageProcessorImpl();
    BufferedImage image = ImageIO.read(new File("res/cat.jpeg"));
    processor.loadImage(image, "original");
    processor.save("original", dst);
  }

  @Test
  public void testLoadJPG() throws FileNotFoundException, IOException {
    String dst = "res/processor.jpg";
    MoreImageProcessor processor = new MoreImageProcessorImpl();
    BufferedImage image = ImageIO.read(new File("res/cat.jpg"));
    processor.loadImage(image, "original");
    processor.save("original", dst);
  }

}
