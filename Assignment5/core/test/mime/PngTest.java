package mime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import mime.model.ImageHandler;
import mime.model.ImageIOHandler;

import org.junit.AfterClass;
import org.junit.Test;

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

  @Override
  protected void loadImageInvoker(String path, String name) throws IOException {
    InputStream stream = new FileInputStream(path);
    ImageHandler handler = new ImageIOHandler();
    handler.readImage(stream);
    processor.loadImage(handler.getInfo(), handler.getImage(), name);
  }

  @Test
  public void testLoadOverwrite() throws IOException {

    loadImageInvoker("res/format/something.png", "original");
    loadImageInvoker(src, "original");

    assertLooper(new int[][][]{
                    {{234, 232, 236}, {209, 194, 193}, {168, 150, 148}},
                    {{234, 230, 231}, {194, 184, 187}, {116, 99, 101}},
                    {{211, 203, 206}, {170, 150, 150}, {70, 42, 43}}
            }
            , processor.getImage("original"));
  }

  @Test
  public void testLoadDifferentImages() throws IOException {
    loadImageInvoker("res/format/something.png", "something");
    loadImageInvoker(src, "original");

    assertLooper(new int[][][]{
                    {{234, 232, 236}, {209, 194, 193}, {168, 150, 148}},
                    {{9, 230, 231}, {194, 164, 187}, {116, 99, 101}},
                    {{211, 203, 106}, {170, 150, 150}, {70, 42, 43}}
            }
            , processor.getImage("something"));

    assertLooper(new int[][][]{
                    {{234, 232, 236}, {209, 194, 193}, {168, 150, 148}},
                    {{234, 230, 231}, {194, 184, 187}, {116, 99, 101}},
                    {{211, 203, 206}, {170, 150, 150}, {70, 42, 43}}
            }
            , processor.getImage("original"));
  }


}
