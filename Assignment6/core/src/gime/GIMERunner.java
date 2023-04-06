package gime;

import gime.control.GraphicImageController;
import gime.view.JFrameView;
import ime.control.IController;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Arrays;
import mime.control.MoreImageController;
import mime.model.MoreImageProcessorImpl;

public class GIMERunner {

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
      GraphicImageController ctrl = new GraphicImageController(new InputStreamReader(System.in), System.out);
      ctrl.runGUI(new JFrameView("GIME APP"),new MoreImageProcessorImpl());
    }
  }

}
