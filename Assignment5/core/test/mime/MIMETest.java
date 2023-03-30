package mime;


import static org.junit.Assert.assertEquals;

import ime.IMETest;
import ime.control.IController;

import java.io.File;
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
    Reader in = new StringReader("run res/newscript.text");
    IController controller = new MoreImageController(in, out);
    controller.run(new MoreImageProcessorImpl());
    assertEquals("Enter Command:Executed: \tload res/cat.ppm cat\n"
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
            + "Executed: \tsave res/mime/cat-grey2.jpeg cat-grey2\n"
            + "Executed: \tsave res/mime/cat-grey2.jpg cat-grey2\n"
            + "Executed: \tsave res/mime/cat-grey2.png cat-grey2\n"
            + "Executed: \tsave res/mime/cat-grey2.bmp cat-grey2\n"
            + "Executed: \tsave res/mime/cat-grey2.ppm cat-grey2\n"
            + "Executed: \tsepia cat cat-sepia\n"
            + "Executed: \tsave res/mime/cat-sepia.jpeg cat-sepia\n"
            + "Executed: \tsave res/mime/cat-sepia.jpg cat-sepia\n"
            + "Executed: \tsave res/mime/cat-sepia.png cat-sepia\n"
            + "Executed: \tsave res/mime/cat-sepia.bmp cat-sepia\n"
            + "Executed: \tsave res/mime/cat-sepia.ppm cat-sepia\n"
            + "Executed: \tsharpen cat cat-sharpen\n"
            + "Executed: \tsave res/mime/cat-sharpen.jpeg cat-sharpen\n"
            + "Executed: \tsave res/mime/cat-sharpen.jpg cat-sharpen\n"
            + "Executed: \tsave res/mime/cat-sharpen.png cat-sharpen\n"
            + "Executed: \tsave res/mime/cat-sharpen.bmp cat-sharpen\n"
            + "Executed: \tsave res/mime/cat-sharpen.ppm cat-sharpen\n"
            + "Executed: \tblur cat cat-blur\n"
            + "Executed: \tsave res/mime/cat-blur.jpeg cat-blur\n"
            + "Executed: \tsave res/mime/cat-blur.jpg cat-blur\n"
            + "Executed: \tsave res/mime/cat-blur.png cat-blur\n"
            + "Executed: \tsave res/mime/cat-blur.bmp cat-blur\n"
            + "Executed: \tsave res/mime/cat-blur.ppm cat-blur\n"
            + "Executed: \tdither cat cat-dither\n"
            + "Executed: \tsave res/mime/cat-dither.jpeg cat-dither\n"
            + "Executed: \tsave res/mime/cat-dither.jpg cat-dither\n"
            + "Executed: \tsave res/mime/cat-dither.png cat-dither\n"
            + "Executed: \tsave res/mime/cat-dither.bmp cat-dither\n"
            + "Executed: \tsave res/mime/cat-dither.ppm cat-dither\n"
            + "Executed: \tload res/cat.ppm cat\n"
            + "Executed: \tbrighten 30 cat cat-brighter\n"
            + "Executed: \tsave res/ime/cat-brighter.ppm cat-brighter\n"
            + "Executed: \tbrighten -30 cat cat-darker\n"
            + "Executed: \tsave res/ime/cat-darker.ppm cat-darker\n"
            + "Executed: \tvertical-flip cat cat-vertical\n"
            + "Executed: \tsave res/ime/cat-vertical.ppm cat-vertical\n"
            + "Executed: \thorizontal-flip cat cat-horizontal\n"
            + "Executed: \tsave res/ime/cat-horizontal.ppm cat-horizontal\n"
            + "Executed: \thorizontal-flip cat-vertical cat-vertical-horizontal\n"
            + "Executed: \tsave res/ime/cat-v-h.ppm cat-vertical-horizontal\n"
            + "Executed: \tgreyscale value-component cat cat-greyscale\n"
            + "Executed: \tsave res/ime/cat-gs.ppm cat-greyscale\n"
            + "Executed: \tload res/cat.ppm cat\n"
            + "Executed: \trgb-split cat cat-red cat-green cat-blue\n"
            + "Executed: \tbrighten 50 cat-red cat-red\n"
            + "Executed: \trgb-combine cat-red-tint cat-red cat-green cat-blue\n"
            + "Executed: \tsave res/ime/cat-red-tint.ppm cat-red-tint\n"
            + "\n"
            + "Enter Command:", out.toString());
  }


}
