package mime;


import static org.junit.Assert.assertEquals;

import ime.IMETest;
import ime.control.IController;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import mime.control.MoreImageController;
import mime.model.MoreImageProcessorImpl;
import org.junit.Test;

/**
 * This is a test class for MVC as whole program.
 */
public class MIMETest extends IMETest {

  @Test
  public void RunScriptForMIMETest() throws IOException {
    StringBuilder out = new StringBuilder();
    Reader in = new StringReader("run res/newerror.text");
    IController controller = new MoreImageController(in, out);
    controller.run(new MoreImageProcessorImpl());
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
        + "Executed: \tload res/cat.ppm cat\n"
        + "Executed: \trgb-split cat cat-red cat-green cat-blue\n"
        + "Executed: \tbrighten 50 cat-red cat-red\n"
        + "Executed: \trgb-combine cat-red-tint cat-red cat-green cat-blue\n"
        + "Executed: \tsave res/cat-red-tint.ppm cat-red-tint\n"
        + "Executed: \tload res/cat.ppm cat\n"
        + "Executed: \tsave res/format/cat.jpg cat\n"
        + "Executed: \tsave res/format/cat.jpeg cat\n"
        + "Executed: \tsave res/format/cat.png cat\n"
        + "Executed: \tsave res/format/cat.bmp cat\n"
        + "Executed: \tload res/format/cat.jpg cat\n"
        + "Executed: \tload res/format/cat.bmp cat\n"
        + "Executed: \tload res/format/cat.jpeg cat\n"
        + "Executed: \tload res/format/cat.png cat\n"
        + "Executed: \tload res/cat.ppm cat\n"
        + "Executed: \tgreyscale cat cat-grey2\n"
        + "Executed: \tsave res/new/cat-grey2.jpeg cat-grey2\n"
        + "Executed: \tsave res/new/cat-grey2.jpg cat-grey2\n"
        + "Executed: \tsave res/new/cat-grey2.png cat-grey2\n"
        + "Executed: \tsave res/new/cat-grey2.bmp cat-grey2\n"
        + "Executed: \tsave res/new/cat-grey2.ppm cat-grey2\n"
        + "Executed: \tsepia cat cat-sepia\n"
        + "Executed: \tsave res/new/cat-sepia.jpeg cat-sepia\n"
        + "Executed: \tsave res/new/cat-sepia.jpg cat-sepia\n"
        + "Executed: \tsave res/new/cat-sepia.png cat-sepia\n"
        + "Executed: \tsave res/new/cat-sepia.bmp cat-sepia\n"
        + "Executed: \tsave res/new/cat-sepia.ppm cat-sepia\n"
        + "Executed: \tsharpen cat cat-sharpen\n"
        + "Executed: \tsave res/new/cat-sharpen.jpeg cat-sharpen\n"
        + "Executed: \tsave res/new/cat-sharpen.jpg cat-sharpen\n"
        + "Executed: \tsave res/new/cat-sharpen.png cat-sharpen\n"
        + "Executed: \tsave res/new/cat-sharpen.bmp cat-sharpen\n"
        + "Executed: \tsave res/new/cat-sharpen.ppm cat-sharpen\n"
        + "Executed: \tblur cat cat-blur\n"
        + "Executed: \tsave res/new/cat-blur.jpeg cat-blur\n"
        + "Executed: \tsave res/new/cat-blur.jpg cat-blur\n"
        + "Executed: \tsave res/new/cat-blur.png cat-blur\n"
        + "Executed: \tsave res/new/cat-blur.bmp cat-blur\n"
        + "Executed: \tsave res/new/cat-blur.ppm cat-blur\n"
        + "Executed: \tdither cat cat-dither\n"
        + "Executed: \tsave res/new/cat-dither.jpeg cat-dither\n"
        + "Executed: \tsave res/new/cat-dither.jpg cat-dither\n"
        + "Executed: \tsave res/new/cat-dither.png cat-dither\n"
        + "Executed: \tsave res/new/cat-dither.bmp cat-dither\n"
        + "Executed: \tsave res/new/cat-dither.ppm cat-dither\n"
        + "!<Error>!: \tUnknown command [lo]\n"
        + "!<Error>!: \tjava.lang.IllegalArgumentException: This grayscale component is not an option!\n"
        + "!<Error>!: \tjava.io.FileNotFoundException: xxx\\building.ppm (No such file or directory)\n"
        + "Executed: \trgb-combine cat-red-tint cat-red cat-green cat-blue\n"
        + "Executed: \tsave res/cat-red-tint.ppm cat-red-tint\n"
        + "Executed: \t-EXIT-\n", out.toString());
  }

  @Test
  public void OutImageFormatNotInMap() {

  }

  @Test
  public void InImageFormatNotInMap() {

  }


  @Test
  public void greyNotInMap() {

  }

  @Test
  public void filterNotInMap() {

  }


}
