package mime.control;

import ime.control.IController;
import ime.control.ImageController;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import mime.control.command.ColorTrans;
import mime.control.command.Dither;
import mime.control.command.Filter;
import mime.control.command.LoadInputStream;
import mime.control.command.MoreImageCommand;
import mime.control.command.SaveOutStream;
import mime.model.MoreImageProcessor;
import mime.model.MoreImageProcessorImpl;


/**
 * This class represents a MIME controller with more command feature and now works for
 * {@link mime.model.MoreImageProcessor} that will delegate command from Readable. In and return
 * Output to Appendable.
 */
public class MoreImageController extends ImageController {

  /**
   * Builder a {@link MoreImageController} and pass with In and Output stream.
   *
   * @param in  Input object
   * @param out Output object
   */
  public MoreImageController(Readable in, Appendable out) {
    super(in, out);
  }

  /**
   * Main method to run for controller.
   *
   * @param args command line argument passing in
   * @throws IOException if command input into program cuase IOException (such as nosuchfile or no
   *                     such path).
   */
  public static void main(String[] args) throws IOException {
    int fileOption = Arrays.asList(args).indexOf("-file");

    //cmd line option
    if (args.length > 0 && fileOption != -1) {
      IController ctrl = new MoreImageController(new StringReader("run " + args[fileOption + 1]),
          System.out);
      ctrl.run(new MoreImageProcessorImpl());
    } else {
      //Example main program
      IController ctrl = new MoreImageController(new InputStreamReader(System.in), System.out);
      ctrl.run(new MoreImageProcessorImpl());
    }
  }


  @Override
  public String processCommand(String command) throws IOException {
    StringBuilder output = new StringBuilder();
    Scanner s = new Scanner(command);
    MoreImageCommand cmd = null;
    List<String> args = new ArrayList<>();
    IllegalArgumentException wnaE = new IllegalArgumentException("Wrong number of arguments");

    //Parse Input command and store arguments to list
    String in = s.next();
    while (s.hasNext()) {
      args.add(s.next());
    }

    //Create cmd
    try {
      switch (in) {
        case "load":
          if (args.size() != 2) {
            throw wnaE;
          }
          cmd = new LoadInputStream(args.get(0), args.get(1));
          break;
        case "save":
          if (args.size() != 2) {
            throw wnaE;
          }
          cmd = new SaveOutStream(args.get(0), args.get(1));
          break;
        // color trans (greyscale)
        case "greyscale":
          if (args.size() != 2 && args.size() != 3) {
            throw wnaE;
          }
          cmd = args.size() == 2 ?
              new ColorTrans("luma-component", args.get(0), args.get(1))
              : new ColorTrans(args.get(0), args.get(1), args.get(2));
          break;
        case "sepia":
          if (args.size() != 2) {
            throw wnaE;
          }
          cmd = new ColorTrans(in, args.get(0), args.get(1));
          break;
        // filter
        case "blur":
        case "sharpen":
          if (args.size() != 2) {
            throw wnaE;
          }
          cmd = new Filter(in, args.get(0), args.get(1));
          break;
        case "dither":
          if (args.size() != 2) {
            throw wnaE;
          }
          cmd = new Dither(args.get(0), args.get(1));
          break;
        default:
          output.append(super.processCommand(command));
          break;
      }
    } catch (IllegalArgumentException wnag) {
      output.append("!<Error>!: \t" + wnag + "\n");
    }

    if (cmd != null) {
      try {
        cmd.execute((MoreImageProcessor) model);
        output.append("Executed: \t").append(command).append("\n");
      } catch (IllegalArgumentException | IllegalStateException iae) {
        output.append("!<Error>!: \t" + iae + "\n");
      } catch (FileNotFoundException fne) {
        output.append("!<Error>!: \t" + fne.toString()
            .replaceAll("The system cannot find the path specified", "No such file or directory")
                + "\n");
      }
    }

    return output.toString();
  }


}
