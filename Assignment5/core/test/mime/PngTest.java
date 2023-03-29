package mime;

import org.junit.AfterClass;

import java.io.File;
import java.io.FileNotFoundException;

public class PngTest extends AbstractMIPTest {
  public PngTest() throws FileNotFoundException {
    super("res/format/cat.png", "res/processor.png", "png");
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

}
