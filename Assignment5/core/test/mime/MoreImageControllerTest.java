package mime;

import static org.junit.Assert.assertEquals;

import ime.ImageControllerTest;
import ime.control.IController;
import ime.model.ImageProcessor;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import javax.imageio.ImageIO;
import mime.control.MoreImageController;
import mime.model.MoreImageProcessor;
import org.junit.Test;

/**
 * This is a test class for {@link MoreImageController}.
 */
public class MoreImageControllerTest extends ImageControllerTest {

  /**
   * This class represent a mock of {@link ImageProcessor} to take method input and store it for
   * testing purposes.
   */
  public static class MoreMockModel extends MockModel implements MoreImageProcessor {


    public MoreMockModel(StringBuilder log) {
      super(log);
    }

    /**
     * A method to verify whether accepting them image format. Accepted image format includes: - ppm
     * - jpg - png - bmp
     *
     * @param pathname pathname of incoming image
     * @return true if acceptable; else otherwise
     */
    @Override
    public boolean verifyFormat(String pathname) {
      return false;
    }

    @Override
    public void loadImage(BufferedImage image, String name) {
      log.append(
          "BufImg:"
              + image.toString().replaceAll("BufferedImage@[A-Za-z0-9]+:", "")
              + " "
              + "name:" + name + "\n");
    }

    @Override
    public void filter(String mode, String from, String to) {
      log.append("Mode:" + mode + " " + "From:" + from + " " + "To:" + to + "\n");
    }

    @Override
    public void dithering(String from, String to) {
      log.append("From:" + from + " " + "To:" + to + "\n");
    }

  }


  @Test
  public void mockLoadBufTest() throws IOException {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("load res/cat.jpeg cat");
    IController controller = new MoreImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.run(new MoreMockModel(log));
    String imgInfo = ImageIO.read(new FileInputStream("res/cat.jpeg")).toString()
        .replaceAll("BufferedImage@[A-Za-z0-9]+:", "");
    assertEquals("BufImg:" + imgInfo + " name:cat\n", log.toString());
  }

  @Test
  public void mockFilterTest() throws IOException {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("blur cat cat-blur");
    IController controller = new MoreImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.run(new MoreMockModel(log));
    assertEquals("Mode:blur From:cat To:cat-blur\n", log.toString());
  }

  @Test
  public void mockDitherBufTest() throws IOException {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("dither cat cat-dither");
    IController controller = new MoreImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.run(new MoreMockModel(log));
    assertEquals("From:cat To:cat-dither\n", log.toString());
  }


}
