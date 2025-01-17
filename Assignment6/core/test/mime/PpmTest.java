package mime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import mime.model.ImageHandler;
import mime.model.PpmHandler;
import org.junit.AfterClass;

/**
 * A test class for ppm image.
 */
public class PpmTest extends AbstractMIPTest {

  public PpmTest() throws FileNotFoundException {
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

  @Override
  protected void loadImageInvoker(String path, String name) throws IOException {
    InputStream stream = new FileInputStream(path);
    ImageHandler handler = new PpmHandler();
    handler.readImage(stream);
    processor.loadImage(handler.getInfo(), handler.getImage(), name);
  }

}