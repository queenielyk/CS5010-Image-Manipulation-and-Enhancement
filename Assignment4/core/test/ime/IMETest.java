package ime;


import ime.control.IController;
import ime.control.ImageController;
import ime.model.PpmProcessor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.Test;

/**
 * This is a test class for MVC as whole program.
 */
public class IMETest {

  @Test(expected = FileNotFoundException.class)
  public void wrongPathFileTest() throws IOException {
    StringBuilder out = new StringBuilder();
    Reader in = new StringReader("load IME/test/IME/fake.ppm fake");
    IController controller = new ImageController(in, out);
    controller.run(new PpmProcessor());
  }

  @Test(expected = IllegalArgumentException.class)
  public void wrongGreyTypeTest() throws IOException {
    StringBuilder out = new StringBuilder();
    Reader in = new StringReader("load res/cat.ppm cat \n"
        + "greyscale xxxx-component cat cat-greyscale");
    IController controller = new ImageController(in, out);
    controller.run(new PpmProcessor());
  }

  @Test (expected = IllegalArgumentException.class)
  public void wrongImageTypeTest() throws IOException {
    StringBuilder out = new StringBuilder();
    Reader in = new StringReader("load res/test.png cat");
    IController controller = new ImageController(in, out);
    controller.run(new PpmProcessor());
  }

  @Test(expected = IllegalStateException.class)
  public void ImageNotInMapTest() throws IOException {
    StringBuilder out = new StringBuilder();
    Reader in = new StringReader("greyscale xxxx-component cat cat-greyscale");
    IController controller = new ImageController(in, out);
    controller.run(new PpmProcessor());
  }
}
