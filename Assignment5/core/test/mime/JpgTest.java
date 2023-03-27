package mime;

import org.junit.AfterClass;
import java.io.File;

public class JpgTest extends AbstractMIPTest {
  public JpgTest() {
    super("res/cat.jpeg", "res/processor.jpeg", "jpeg");
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
