package mime;

import org.junit.AfterClass;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import mime.model.MoreImageProcessor;
import mime.model.MoreImageProcessorImpl;

public class JpgTest extends AbstractMIPTest {
  public JpgTest() {
    super("res/cat.jepg", "res/processor.jpg", "jpg");
  }

  /**
   * A method to be executed at the end of the test class. Remove image exported from this test
   * class.
   */
  @AfterClass
  public static void removeDstPpm() {
    String dst = "res/processor.jpg";
    File myObj = new File(dst);
    myObj.delete();
  }

  //     JPG: Save JPG -> PNG, BMP

}
