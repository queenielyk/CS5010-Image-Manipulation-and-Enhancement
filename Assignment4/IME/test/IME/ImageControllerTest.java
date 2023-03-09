package IME;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import IME.model.ImageProcessor;
import control.IController;
import control.ImageController;

import static org.junit.Assert.assertEquals;

/**
 * This is a test class for {@link ImageController}
 */
public class ImageControllerTest {
  /**
   * This class represent a mock of {@link ImageProcessor} to take method input and store it for
   * testing purposes.
   */
  public class MockModel implements ImageProcessor {

    private StringBuilder log;

    public MockModel(StringBuilder log) {
      this.log = log;
    }

    @Override
    public void loadImage(String path, String name) throws FileNotFoundException, IllegalStateException {
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
      log.append("R:" + redName + " " + "G:" + greenName + " " + "B:" + blueName + " " + "To:" + to + "\n");
    }

    @Override
    public void save(String from, String path) {
      log.append("From:" + from + " " + "Path:" + path + "\n");
    }
  }

  @Test
  public void mockLoadTest() throws IOException {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("load images/koala.ppm koala");
    IController controller = new ImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.go(new MockModel(log));
    assertEquals("Path:images/koala.ppm Name:koala\n", log.toString());
  }

  @Test
  public void mockGreyscaleTest() throws IOException {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("greyscale value-component koala koala-greyscale");
    IController controller = new ImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.go(new MockModel(log));
    assertEquals("Mode:value-component From:koala To:koala-greyscale\n", log.toString());
  }

  @Test
  public void mockRgbSplitTest() throws IOException {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("rgb-split koala koala-red koala-green koala-blue");
    IController controller = new ImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.go(new MockModel(log));
    assertEquals("Mode:red-component From:koala To:koala-red\n" +
                    "Mode:green-component From:koala To:koala-green\n" +
                    "Mode:blue-component From:koala To:koala-blue\n"
            , log.toString());
  }

  @Test
  public void mockHflipTest() throws IOException {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("horizontal-flip koala-vertical koala-vertical-horizontal");
    IController controller = new ImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.go(new MockModel(log));
    assertEquals("From:koala-vertical To:koala-vertical-horizontal\n", log.toString());
  }


  @Test
  public void mockVflipTest() throws IOException {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("vertical-flip koala koala-vertical");
    IController controller = new ImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.go(new MockModel(log));
    assertEquals("From:koala To:koala-vertical\n", log.toString());
  }

  @Test
  public void mockVBrightenTest() throws IOException {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("brighten 10 koala koala-brighter");
    IController controller = new ImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.go(new MockModel(log));
    assertEquals("From:koala Add:10 To:koala-brighter\n", log.toString());
  }

  @Test
  public void mockCombineTest() throws IOException {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("rgb-combine koala-red-tint koala-red koala-green koala-blue");
    IController controller = new ImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.go(new MockModel(log));
    assertEquals("R:koala-red G:koala-green B:koala-blue To:koala-red-tint\n", log.toString());
  }

  @Test
  public void mockSaveTest() throws IOException {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("save images/koala-gs.ppm koala-greyscale");
    IController controller = new ImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.go(new MockModel(log));
    assertEquals("From:koala-greyscale Path:images/koala-gs.ppm\n", log.toString());
  }

  @Test(expected = FileNotFoundException.class)
  public void ScriptNotFoundTest() throws IOException {
    StringBuilder out = new StringBuilder();
    Reader in = new StringReader("run IME/test/IME/fake.text");
    IController controller = new ImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.go(new MockModel(log));
  }

  @Test
  public void RunScriptTest() throws IOException {
    StringBuilder out = new StringBuilder();
    Reader in = new StringReader("run IME/test/IME/script.text");
    IController controller = new ImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.go(new MockModel(log));
    assertEquals("Enter Command:" +
                    "Executed: \tload images/Koala.ppm koala\n" +
                    "Executed: \tbrighten 10 koala koala-brighter\n" +
                    "Executed: \tvertical-flip koala koala-vertical\n" +
                    "Executed: \thorizontal-flip koala-vertical koala-vertical-horizontal\n" +
                    "Executed: \tsave images/koala-v-h.ppm koala-vertical-horizontal\n" +
                    "Executed: \tgreyscale value-component koala koala-greyscale\n" +
                    "Executed: \tsave images/koala-brighter.ppm koala-brighter\n" +
                    "Executed: \tsave images/koala-gs.ppm koala-greyscale\n" +
                    "Executed: \tload images/upper.ppm koala\n" +
                    "Executed: \trgb-split koala koala-red koala-green koala-blue\n" +
                    "Executed: \tbrighten 50 koala-red koala-red\n" +
                    "Executed: \trgb-combine koala-red-tint koala-red koala-green koala-blue\n" +
                    "Executed: \tsave images/koala-red-tint.ppm koala-red-tint\n" +
                    "\n" +
                    "Enter Command:"
            , out.toString());
  }


}
