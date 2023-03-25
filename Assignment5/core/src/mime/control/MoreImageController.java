package mime.control;

import ime.control.IController;
import ime.control.ImageCommand;
import ime.control.ImageController;
import ime.control.command.Brighten;
import mime.control.command.Dither;
import mime.control.command.Filter;
import ime.control.command.Greyscale;
import ime.control.command.Hflip;
import ime.control.command.Load;
import ime.control.command.RgbCombine;
import ime.control.command.RgbSplit;
import ime.control.command.Save;
import ime.control.command.Vflip;
import ime.model.ImageProcessor;
import ime.model.PpmProcessor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;


/**
 * This class represents a controller for {@link ImageProcessor} that will delegate command from
 * Readable. In and return Output to Appendable.
 */
public class MoreImageController extends ImageController {

  private ImageProcessor model;
  private Set<String> setScript;

  /**
   * Builder a controller and pass with In and Out stream.
   *
   * @param in  Input object
   * @param out Output object
   */
  public MoreImageController(Readable in, Appendable out) {
    super(in, out);
    this.setScript = new HashSet<>();
  }

  @Override
  public String processCommand(String command) throws IOException {
    StringBuilder output = new StringBuilder();
    Scanner s = new Scanner(command);
    ImageCommand cmd = null;
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
        case "q":
        case "quit":
        case "exit":
          output.append("Executed: \t" + "-EXIT-" + "\n");
          break;
        case "run":
          if (args.size() != 1) {
            throw wnaE;
          }

          if (this.setScript.contains(args.get(0))) {
            throw new IllegalStateException("Encountered looping through scripts");
          }

          setScript.add(args.get(0));
          Scanner fileScan = new Scanner(new FileInputStream(args.get(0)));
          output.append(processFileScript(fileScan));

          setScript.remove(args.get(0));
          break;
        case "load":
          if (args.size() != 2) {
            throw wnaE;
          }
          cmd = new Load(args.get(0), args.get(1));
          break;
        case "save":
          if (args.size() != 2) {
            throw wnaE;
          }
          cmd = new Save(args.get(0), args.get(1));
          break;
        case "rgb-split":
          if (args.size() != 4) {
            throw wnaE;
          }
          cmd = new RgbSplit(args.get(0), args.get(1), args.get(2), args.get(3));
          break;
        case "rgb-combine":
          if (args.size() != 4) {
            throw wnaE;
          }
          cmd = new RgbCombine(args.get(0), args.get(1), args.get(2), args.get(3));
          break;
        case "brighten":
          if (args.size() != 3) {
            throw wnaE;
          }
          cmd = new Brighten(Integer.parseInt(args.get(0)), args.get(1), args.get(2));
          break;
        case "vertical-flip":
          if (args.size() != 2) {
            throw wnaE;
          }
          cmd = new Vflip(args.get(0), args.get(1));
          break;
        case "horizontal-flip":
          if (args.size() != 2) {
            throw wnaE;
          }
          cmd = new Hflip(args.get(0), args.get(1));
          break;
        // color Trans
        case "greyscale":
        case "sepia":
          if (args.size() != 2 && args.size() != 3) {
            throw wnaE;
          }
          cmd = args.size() == 2 ?
                  new Greyscale(in, args.get(0), args.get(1))
                  : new Greyscale(args.get(0), args.get(1), args.get(2));
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
        default:
          output.append("!<Error>!: \t" + String.format("Unknown command [%s]", in) + "\n");
          cmd = null;
          break;
      }
    } catch (IllegalArgumentException wnag) {
      output.append("!<Error>!: \t" + wnag + "\n");
    }

    if (cmd != null) {
      try {
        cmd.execute(model);
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

  /**
   * Helper method for run command from script file line by line.
   *
   * @param fileScan Scanner for script file
   * @return String of execution result.
   * @throws IOException if command in script try to run script file that can not be found
   */
  protected String processFileScript(Scanner fileScan) throws IOException {
    StringBuilder outputs = new StringBuilder();

    while (fileScan.hasNextLine()) {
      String line = fileScan.nextLine();
      //ignore comments
      if (line.isEmpty() || line.charAt(0) == '#') {
        continue;
      }
      //processCommand
      String result = processCommand(line);
      outputs.append(result);
      if (result.contains("-EXIT-")) {
        break;
      }
    }

    return outputs.toString();
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
      ctrl.run(new PpmProcessor());
    } else {
      //Example main program
      IController ctrl = new MoreImageController(new InputStreamReader(System.in), System.out);
      ctrl.run(new PpmProcessor());
    }
  }


}
