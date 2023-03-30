package ime;

import static org.junit.Assert.assertEquals;

import ime.control.IController;
import ime.control.ImageController;
import ime.model.ImageProcessor;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.ComparisonFailure;
import org.junit.Test;

/**
 * This is a test class for {@link ImageController}.
 */
public class ImageControllerTest {

  @Test
  public void mockLoadTest() throws IOException {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("load res/cat.ppm cat");
    IController controller = new ImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.run(new MockModel(log));
    assertEquals("Path:res/cat.ppm Name:cat\n", log.toString());
  }

  @Test
  public void mockGreyscaleTest() throws IOException {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("greyscale value-component cat cat-greyscale");
    IController controller = new ImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.run(new MockModel(log));
    assertEquals("Mode:value-component From:cat To:cat-greyscale\n", log.toString());
  }

  @Test
  public void mockRgbSplitTest() throws IOException {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("rgb-split cat cat-red cat-green cat-blue");
    IController controller = new ImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.run(new MockModel(log));
    assertEquals("Mode:red-component From:cat To:cat-red\n"
            + "Mode:green-component From:cat To:cat-green\n"
            + "Mode:blue-component From:cat To:cat-blue\n", log.toString());
  }

  @Test
  public void mockHflipTest() throws IOException {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("horizontal-flip cat-vertical cat-vertical-horizontal");
    IController controller = new ImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.run(new MockModel(log));
    assertEquals("From:cat-vertical To:cat-vertical-horizontal\n", log.toString());
  }

  @Test
  public void mockVflipTest() throws IOException {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("vertical-flip cat cat-vertical");
    IController controller = new ImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.run(new MockModel(log));
    assertEquals("From:cat To:cat-vertical\n", log.toString());
  }

  @Test
  public void mockVBrightenTest() throws IOException {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("brighten 10 cat cat-brighter");
    IController controller = new ImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.run(new MockModel(log));
    assertEquals("From:cat Add:10 To:cat-brighter\n", log.toString());
  }

  @Test
  public void mockCombineTest() throws IOException {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("rgb-combine cat-red-tint cat-red cat-green cat-blue");
    IController controller = new ImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.run(new MockModel(log));
    assertEquals("R:cat-red G:cat-green B:cat-blue To:cat-red-tint\n", log.toString());
  }

  @Test
  public void mockSaveTest() throws IOException {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("save res/ime/cat-gs.ppm cat-greyscale");
    IController controller = new ImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.run(new MockModel(log));
    assertEquals("From:cat-greyscale Path:res/ime/cat-gs.ppm\n", log.toString());
  }

  @Test
  public void ScriptNotFoundTest() throws IOException {
    StringBuilder out = new StringBuilder();
    Reader in = new StringReader("run IME/test/IME/fake.text");
    IController controller = new ImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.run(new MockModel(log));

    try {
      assertEquals(
              "Enter Command:!<Error>!: \tjava.io.FileNotFoundException: IME" + File.separator + "test" + File.separator + "IME" + File.separator + "fake.text (The system cannot find the path specified)\n"
                      + "\n"
                      + "Enter Command:", out.toString());
    } catch (ComparisonFailure e) {
      try {
        assertEquals(
                "Enter Command:!<Error>!: \tjava.io.FileNotFoundException: IME" + File.separator + "test" + File.separator + "IME" + File.separator + "fake.text (No such file or directory)\n"
                        + "\n"
                        + "Enter Command:", out.toString());
      } catch (ComparisonFailure failure) {
        throw failure;
      }
    }
  }

  @Test
  public void MissingArgumentTest() throws IOException {
    StringBuilder out = new StringBuilder();
    Reader in = new StringReader("run ");
    IController controller = new ImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.run(new MockModel(log));
    assertEquals(
            "Enter Command:!<Error>!: \tjava.lang.IllegalArgumentException: Wrong number of arguments\n"
                    + "\n"
                    + "Enter Command:", out.toString());
  }

  @Test
  public void ExtraArgumentTest() throws IOException {
    StringBuilder out = new StringBuilder();
    Reader in = new StringReader("run script.text script2.text");
    IController controller = new ImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.run(new MockModel(log));
    assertEquals(
            "Enter Command:!<Error>!: \tjava.lang.IllegalArgumentException: Wrong number of arguments\n"
                    + "\n"
                    + "Enter Command:", out.toString());
  }

  @Test
  public void UnknownCommandTest() throws IOException {
    StringBuilder out = new StringBuilder();
    Reader in = new StringReader("grey value-component cat cat-greyscale");
    IController controller = new ImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.run(new MockModel(log));
    assertEquals("Enter Command:!<Error>!: \tUnknown command [grey]\n"
            + "\n"
            + "Enter Command:", out.toString());
  }

  @Test
  public void BrightNotIntTest() throws IOException {
    StringBuilder out = new StringBuilder();
    Reader in = new StringReader("brighten a cat cat-a");
    IController controller = new ImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.run(new MockModel(log));
    assertEquals(
            "Enter Command:!<Error>!: \tjava.lang.NumberFormatException: For input string: \"a\"\n"
                    + "\n"
                    + "Enter Command:", out.toString());
  }

  /**
   * This class represent a mock of {@link ImageProcessor} to take method input and store it for
   * testing purposes.
   */
  public static class MockModel implements ImageProcessor {

    protected final StringBuilder log;

    public MockModel(StringBuilder log) {
      this.log = log;
    }

    @Override
    public void loadImage(String path, String name) throws IllegalStateException {
      log.append("Path:" + path + " " + "Name:" + name + "\n");
    }

    @Override
    public void greyscale(String mode, String from, String to) {
      log.append("Mode:" + mode + " " + "From:" + from + " " + "To:" + to + "\n");
    }

    @Override
    public void horizontalFlip(String from, String to) {
      log.append("From:" + from + " " + "To:" + to + "\n");
    }

    @Override
    public void verticalFlip(String from, String to) {
      log.append("From:" + from + " " + "To:" + to + "\n");
    }

    @Override
    public void adjustBrightness(String from, int add, String to) {
      log.append("From:" + from + " " + "Add:" + add + " " + "To:" + to + "\n");
    }

    @Override
    public void combines(String redName, String greenName, String blueName, String to) {
      log.append("R:" + redName + " " + "G:" + greenName + " " + "B:" + blueName + " "
              + "To:" + to + "\n");
    }

    @Override
    public void save(String from, String path) {
      log.append("From:" + from + " " + "Path:" + path + "\n");
    }
  }


}
