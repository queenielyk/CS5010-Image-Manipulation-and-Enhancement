package mime;

import org.junit.AfterClass;

import java.io.File;

public class BmpTest extends AbstractMIPTest {
  public BmpTest() {
    super("res/cat.bmp", "res/processor.bmp", "bmp");
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

}
