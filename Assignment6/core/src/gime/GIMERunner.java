package gime;

import gime.control.GraphicImageController;
import gime.control.IGraphicController;
import gime.model.ReadOnlyImageProcessorImpl;
import gime.view.JFrameView;
import ime.control.IController;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Arrays;

import mime.control.MoreImageController;
import mime.model.MoreImageProcessor;
import mime.model.MoreImageProcessorImpl;

/**
 * This is the main program class.
 */
public class GIMERunner {

  /**
   * Base on different command line options one of three mode will get run (TextCMD/Script/GUI).
   *
   * @param args command line arguments
   * @throws IOException if path is not found or something when wrong with FileIO.
   */
  public static void main(String[] args) throws IOException {
    int fileOption = Arrays.asList(args).indexOf("-file");
    int textOption = Arrays.asList(args).indexOf("-text");

    //RUN script then END
    if (args.length > 0 && fileOption != -1) {
      IController ctrl = new MoreImageController(new StringReader("run " + args[fileOption + 1]),
              System.out);
      ctrl.run(new MoreImageProcessorImpl());
      //Text-Mode
    } else if (textOption != -1) {
      IController ctrl = new MoreImageController(new InputStreamReader(System.in), System.out);
      ctrl.run(new MoreImageProcessorImpl());
    }
    // GUI-Mode
    else {
      IGraphicController ctrl = new GraphicImageController();
      MoreImageProcessor processor = new MoreImageProcessorImpl();
      ctrl.runGUI(new JFrameView("GIME APP", new ReadOnlyImageProcessorImpl(processor)), processor);
    }
  }

}
