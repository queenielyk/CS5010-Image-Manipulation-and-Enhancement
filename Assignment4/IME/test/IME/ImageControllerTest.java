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
    Reader in = new StringReader("load res/cat.ppm cat");
    IController controller = new ImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.go(new MockModel(log));
    assertEquals("Path:res/cat.ppm Name:cat\n", log.toString());
  }

  @Test
  public void mockGreyscaleTest() throws IOException {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("greyscale value-component cat cat-greyscale");
    IController controller = new ImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.go(new MockModel(log));
    assertEquals("Mode:value-component From:cat To:cat-greyscale\n", log.toString());
  }

  @Test
  public void mockRgbSplitTest() throws IOException {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("rgb-split cat cat-red cat-green cat-blue");
    IController controller = new ImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.go(new MockModel(log));
    assertEquals("Mode:red-component From:cat To:cat-red\n" +
                    "Mode:green-component From:cat To:cat-green\n" +
                    "Mode:blue-component From:cat To:cat-blue\n"
            , log.toString());
  }

  @Test
  public void mockHflipTest() throws IOException {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("horizontal-flip cat-vertical cat-vertical-horizontal");
    IController controller = new ImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.go(new MockModel(log));
    assertEquals("From:cat-vertical To:cat-vertical-horizontal\n", log.toString());
  }


  @Test
  public void mockVflipTest() throws IOException {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("vertical-flip cat cat-vertical");
    IController controller = new ImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.go(new MockModel(log));
    assertEquals("From:cat To:cat-vertical\n", log.toString());
  }

  @Test
  public void mockVBrightenTest() throws IOException {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("brighten 10 cat cat-brighter");
    IController controller = new ImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.go(new MockModel(log));
    assertEquals("From:cat Add:10 To:cat-brighter\n", log.toString());
  }

  @Test
  public void mockCombineTest() throws IOException {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("rgb-combine cat-red-tint cat-red cat-green cat-blue");
    IController controller = new ImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.go(new MockModel(log));
    assertEquals("R:cat-red G:cat-green B:cat-blue To:cat-red-tint\n", log.toString());
  }

  @Test
  public void mockSaveTest() throws IOException {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("save res/cat-gs.ppm cat-greyscale");
    IController controller = new ImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.go(new MockModel(log));
    assertEquals("From:cat-greyscale Path:res/cat-gs.ppm\n", log.toString());
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
    Reader in = new StringReader("run res/script.text");
    IController controller = new ImageController(in, out);
    StringBuilder log = new StringBuilder();
    controller.go(new MockModel(log));
    assertEquals("Enter Command:Executed: \tload res/cat.ppm cat\n" +
                    "Executed: \tbrighten 30 cat cat-brighter\n" +
                    "Executed: \tsave res/cat-brighter.ppm cat-brighter\n" +
                    "Executed: \tbrighten -30 cat cat-darker\n" +
                    "Executed: \tsave res/cat-darker.ppm cat-darker\n" +
                    "Executed: \tvertical-flip cat cat-vertical\n" +
                    "Executed: \tsave res/cat-vertical.ppm cat-vertical\n" +
                    "Executed: \thorizontal-flip cat cat-horizontal\n" +
                    "Executed: \tsave res/cat-horizontal.ppm cat-horizontal\n" +
                    "Executed: \thorizontal-flip cat-vertical cat-vertical-horizontal\n" +
                    "Executed: \tsave res/cat-v-h.ppm cat-vertical-horizontal\n" +
                    "Executed: \tgreyscale value-component cat cat-greyscale\n" +
                    "Executed: \tsave res/cat-gs.ppm cat-greyscale\n" +
                    "Executed: \tload res/building.ppm cat\n" +
                    "Executed: \trgb-split cat cat-red cat-green cat-blue\n" +
                    "Executed: \tbrighten 50 cat-red cat-red\n" +
                    "Executed: \trgb-combine cat-red-tint cat-red cat-green cat-blue\n" +
                    "Executed: \tsave res/cat-red-tint.ppm cat-red-tint\n" +
                    "\n" +
                    "Enter Command:"
            , out.toString());
  }


}
