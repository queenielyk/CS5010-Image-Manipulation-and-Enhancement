package ime;


import static org.junit.Assert.assertEquals;

import ime.control.IController;
import ime.control.ImageController;
import ime.model.PpmProcessor;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.junit.Test;

/**
 * This is a test class for MVC as whole program.
 */
public class IMETest {

  @Test
  public void RunScriptTest() throws IOException {
    StringBuilder out = new StringBuilder();
    Reader in = new StringReader("run res/error.text");
    IController controller = new ImageController(in, out);
    controller.run(new PpmProcessor());
    assertEquals("Enter Command:Executed: \tload res/cat.ppm cat\n"
            + "Executed: \tbrighten 30 cat cat-brighter\n"
            + "Executed: \tsave res/cat-brighter.ppm cat-brighter\n"
            + "Executed: \tbrighten -30 cat cat-darker\n"
            + "Executed: \tsave res/cat-darker.ppm cat-darker\n"
            + "Executed: \tvertical-flip cat cat-vertical\n"
            + "Executed: \tsave res/cat-vertical.ppm cat-vertical\n"
            + "Executed: \thorizontal-flip cat cat-horizontal\n"
            + "Executed: \tsave res/cat-horizontal.ppm cat-horizontal\n"
            + "Executed: \thorizontal-flip cat-vertical cat-vertical-horizontal\n"
            + "Executed: \tsave res/cat-v-h.ppm cat-vertical-horizontal\n"
            + "Executed: \tgreyscale value-component cat cat-greyscale\n"
            + "Executed: \tsave res/cat-gs.ppm cat-greyscale\n"
            + "Executed: \tload res/building.ppm cat\n"
            + "Executed: \trgb-split cat cat-red cat-green cat-blue\n"
            + "Executed: \tbrighten 50 cat-red cat-red\n"
            + "Executed: \trgb-combine cat-red-tint cat-red cat-green cat-blue\n"
            + "Executed: \tsave res/cat-red-tint.ppm cat-red-tint\n"
            + "!<Error>!: \tUnknown command [lo]\n"
            + "!<Error>!: \tjava.lang.IllegalArgumentException: "
            + "This grayscale component is not an option!\n"
            + "!<Error>!: \tjava.io.FileNotFoundException: "
            + "xxx\\building.ppm (No such file or directory)\n"
            + "Executed: \trgb-combine cat-red-tint cat-red cat-green cat-blue\n"
            + "Executed: \tsave res/cat-red-tint.ppm cat-red-tint\n"
            + "Executed: \t-EXIT-\n", out.toString());
  }

  @Test
  public void wrongPathFileTest() throws IOException {
    StringBuilder out = new StringBuilder();
    Reader in = new StringReader("load IME/test/IME/fake.ppm fake");
    IController controller = new ImageController(in, out);
    controller.run(new PpmProcessor());
    assertEquals(
            "Enter Command:!<Error>!: \tjava.io.FileNotFoundException: "
                    + "IME\\test\\IME\\fake.ppm (No such file or directory)\n"
                    + "\n"
                    + "Enter Command:", out.toString());
  }

  @Test
  public void wrongGreyTypeTest() throws IOException {
    StringBuilder out = new StringBuilder();
    Reader in = new StringReader("load res/cat.ppm cat \n"
            + "greyscale xxxx-component cat cat-greyscale");
    IController controller = new ImageController(in, out);
    controller.run(new PpmProcessor());
    assertEquals("Enter Command:Executed: \tload res/cat.ppm cat \n"
            + "\n"
            + "Enter Command:!<Error>!: \tjava.lang.IllegalArgumentException: "
            + "This grayscale component is not an option!\n"
            + "\n"
            + "Enter Command:", out.toString());
  }

  @Test
  public void wrongImageTypeTest() throws IOException {
    StringBuilder out = new StringBuilder();
    Reader in = new StringReader("load res/test.png cat");
    IController controller = new ImageController(in, out);
    controller.run(new PpmProcessor());
    assertEquals(
            "Enter Command:!<Error>!: \tjava.lang.IllegalStateException: "
                    + "Invalid Image file: Only ppm images are accepted\n"
                    + "\n"
                    + "Enter Command:", out.toString());
  }

  @Test
  public void ImageNotInMapTest() throws IOException {
    StringBuilder out = new StringBuilder();
    Reader in = new StringReader("greyscale xxxx-component cat cat-greyscale");
    IController controller = new ImageController(in, out);
    controller.run(new PpmProcessor());
    assertEquals(
            "Enter Command:!<Error>!: \tjava.lang.IllegalStateException: "
                    + "This image is not exist!\n"
                    + "\n"
                    + "Enter Command:", out.toString());
  }

  @Test(expected = IllegalStateException.class)
  public void RunScriptTestLoop() throws IOException {
    StringBuilder out = new StringBuilder();
    Reader in = new StringReader("run res/loop1.text");
    IController controller = new ImageController(in, out);
    controller.run(new PpmProcessor());
  }

}
