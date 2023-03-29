package mime;

import org.junit.AfterClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import mime.model.ImageHandler;
import mime.model.ImageIOHandler;

public class BmpTest extends AbstractMIPTest {
  public BmpTest() throws FileNotFoundException {
    super("res/format/cat.bmp", "res/processor.bmp", "bmp");
  }

  /**
   * A method to be executed at the end of the test class. Remove image exported from this test
   * class.
   */
  @AfterClass
  public static void removeDstPpm() {
    String dst = "res/processor.bmp";
    File myObj = new File(dst);
    myObj.delete();
  }

  @Override
  protected void loadImageInvoker(String path, String name) throws IOException {
    InputStream stream = new FileInputStream(path);
    ImageHandler handler = new ImageIOHandler();
    handler.readImage(stream);
    processor.loadImage(handler, name);
  }

}
