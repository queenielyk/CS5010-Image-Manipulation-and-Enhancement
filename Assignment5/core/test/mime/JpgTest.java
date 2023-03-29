package mime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import mime.model.ImageHandler;
import mime.model.ImageIOHandler;
import mime.model.MoreImageProcessor;
import mime.model.MoreImageProcessorImpl;
import org.junit.AfterClass;
import org.junit.Test;

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
  public void testLoadJPEG() throws IOException {
    String dst = "res/processor.jpeg";
    MoreImageProcessor processor = new MoreImageProcessorImpl();
    InputStream stream = new FileInputStream("res/format/cat.jpeg");
    ImageHandler handler = new ImageIOHandler();
    handler.readImage(stream);
    processor.loadImage(handler, "original");
    OutputStream outputStream = new FileOutputStream(dst);
    processor.save("original", outputStream, "jpeg");
  }

  @Test
  public void testLoadJPG() throws IOException {
    String dst = "res/processor.jpg";
    MoreImageProcessor processor = new MoreImageProcessorImpl();
    InputStream stream = new FileInputStream("res/format/cat.jpeg");
    ImageHandler handler = new ImageIOHandler();
    handler.readImage(stream);
    processor.loadImage(handler, "original");
    OutputStream outputStream = new FileOutputStream(dst);
    processor.save("original", outputStream, "jpg");
  }

}
