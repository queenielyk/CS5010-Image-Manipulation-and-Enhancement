package mime;

import static org.junit.Assert.assertEquals;

import ime.ImageControllerTest;
import ime.control.IController;
import ime.model.ImageProcessor;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import mime.control.MoreImageController;
import mime.model.ImageHandler;
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
      return true;
    }

    /**
     * A method to load image from an InputStream and store it.
     *
     * @param reader
     * @param name   the name of image
     * @throws IOException           if unable to read file
     * @throws IllegalStateException if format is not supported
     */
    @Override
    public void loadImage(ImageHandler reader, String name) {
      log.append("Handler:" + Arrays.deepToString(reader.getImage()) + " " + "name:" + name + "\n");
    }


    @Override
    public void save(String from, OutputStream stream, String format)
        throws IOException, IllegalStateException {
      log.append("from:" + from + " " + "format:" + format + "\n");
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
  public void mockLoadInputStreamTest() throws IOException {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("load res/cat.ppm cat");
    IController controller = new MoreImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.run(new MoreMockModel(log));
    assertEquals("Handler:[["
        + "[234, 232, 236], [209, 194, 193], [168, 150, 148]],"
        + " [[234, 230, 231], [194, 184, 187], [116, 99, 101]],"
        + " [[211, 203, 206], [170, 150, 150], [70, 42, 43]]] name:cat\n", log.toString());
  }

  @Test
  public void mockSaveOutputStreamTest() throws IOException {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("save res/cat-gs.ppm cat-greyscale");
    IController controller = new MoreImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.run(new MoreMockModel(log));
    assertEquals("from:cat-greyscale format:ppm\n", log.toString());
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
  public void mockDitherTest() throws IOException {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("dither cat cat-dither");
    IController controller = new MoreImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.run(new MoreMockModel(log));
    assertEquals("From:cat To:cat-dither\n", log.toString());
  }

  @Test
  public void mockGreyscaleTwoArgsTest() throws IOException {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("greyscale cat cat-greyscale");
    IController controller = new MoreImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.run(new MoreMockModel(log));
    assertEquals("Mode:luma-component From:cat To:cat-greyscale\n", log.toString());
  }

  @Test
  public void mockSepiaTest() throws IOException {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("sepia cat cat-sepia");
    IController controller = new MoreImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.run(new MoreMockModel(log));
    assertEquals("Mode:sepia From:cat To:cat-sepia\n", log.toString());
  }

  @Test
  public void mockBlurTest() throws IOException {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("blur cat cat-blur");
    IController controller = new MoreImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.run(new MoreMockModel(log));
    assertEquals("Mode:blur From:cat To:cat-blur\n", log.toString());
  }

  @Test
  public void mockSharpTest() throws IOException {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("sharpen cat cat-sharp");
    IController controller = new MoreImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.run(new MoreMockModel(log));
    assertEquals("Mode:sharpen From:cat To:cat-sharp\n", log.toString());
  }


}
