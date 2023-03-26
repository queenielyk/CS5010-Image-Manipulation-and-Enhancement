package mime;

import org.junit.AfterClass;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import mime.model.MoreImageProcessor;
import mime.model.MoreImageProcessorImpl;

public class PngTest extends AbstractMIPTest {
  public PngTest() {
    super("res/cat.png", "res/processor.png", "png");
  }

  /**
   * A method to be executed at the end of the test class. Remove image exported from this test
   * class.
   */
  @AfterClass
  public static void removeDstPpm() {
    String dst = "res/processor.png";
    File myObj = new File(dst);
    myObj.delete();
  }

  //   PNG: Save  PNG -> BMP

}
