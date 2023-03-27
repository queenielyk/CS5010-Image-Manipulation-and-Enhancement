package mime;

import org.junit.AfterClass;

import java.io.File;

public class PpmTest extends AbstractMIPTest {
  public PpmTest() {
    super("res/cat.ppm", "res/processor.ppm", "ppm");
  }

  /**
   * A method to be executed at the end of the test class. Remove image exported from this test
   * class.
   */
  @AfterClass
  public static void removeDstPpm() {
    String dst = "res/processor.ppm";
    File myObj = new File(dst);
    myObj.delete();
  }

}