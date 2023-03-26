package ime.control;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

import ime.control.command.Brighten;
import ime.control.command.Greyscale;
import ime.control.command.Hflip;
import ime.control.command.Load;
import ime.control.command.RgbCombine;
import ime.control.command.RgbSplit;
import ime.control.command.Save;
import ime.control.command.Vflip;
import ime.model.ImageProcessor;
import ime.model.PpmProcessor;


/**
 * This class represents a controller for {@link ImageProcessor} that will delegate command from
 * Readable. In and return Output to Appendable.
 */
public class ImageController implements IController {

  protected ImageProcessor model;
  private final Readable in;
  private final Appendable out;
  private Set<String> setScript;

  /**
   * Builder a controller and pass with In and Out a stream.
   *
   * @param in  Input object
   * @param out Output object
   */
  public ImageController(Readable in, Appendable out) {
    this.in = in;
    this.out = out;
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
        case "greyscale":
          if (args.size() != 3) {
            throw wnaE;
          }
          cmd = new Greyscale(args.get(0), args.get(1), args.get(2));
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
      } catch (IllegalArgumentException | IllegalStateException | FileNotFoundException iae) {
        output.append("!<Error>!: \t" + iae + "\n");
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

  @Override
  public void run(ImageProcessor model) throws IOException {
    Objects.requireNonNull(model);
    this.model = model;
    Scanner scan = new Scanner(this.in);
    out.append("Enter Command:");
    //Keep asking for cmd
    while (scan.hasNextLine()) {
      String result = processCommand(scan.nextLine());
      out.append(result);
      if (result.contains("-EXIT-")) {
        break;
      }
      out.append("\nEnter Command:");
    }
  }


  /**
   * Main method to run for controller.
   *
   * @param args command line argument passing in
   * @throws IOException if command input into program cuase IOException (such as nosuchfile or no
   *                     such path).
   */
  public static void main(String[] args) throws IOException {
    //Example main program
    IController ctrl = new ImageController(new InputStreamReader(System.in), System.out);
    ctrl.run(new PpmProcessor());
  }


}
