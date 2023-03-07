package control;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Objects;
import java.util.Scanner;

import IME.model.ImageProcessor;
import IME.model.PpmProcessor;
import control.command.Brighten;
import control.command.Greyscale;
import control.command.Hflip;
import control.command.Load;
import control.command.RgbCombine;
import control.command.RgbSplit;
import control.command.Save;
import control.command.Vflip;

public class ImageController implements IController {
  private ImageProcessor model;
  private final Readable in;
  private final Appendable out;

  public ImageController(Readable in, Appendable out) {
    this.in = in;
    this.out = out;
  }

  @Override
  public String processCommand(String command) throws IOException {
    StringBuilder output = new StringBuilder();
    Scanner s = new Scanner(command);
    ImageCommand cmd = null;

    System.out.println("cmd+" + command);

    while (s.hasNext()) {
      String in = s.next();

      switch (in) {
        case "Run":
          Scanner fileScan = new Scanner(new FileInputStream(s.next()));
          output.append(processFileScript(fileScan));
          break;
        case "load":
          cmd = new Load(s.next(), s.next());
          break;
        case "save":
          cmd = new Save(s.next(), s.next());
          break;
        case "rgb-split":
          cmd = new RgbSplit(s.next(), s.next(), s.next(), s.next());
          break;
        case "rgb-combine":
          cmd = new RgbCombine(s.next(), s.next(), s.next(), s.next());
          break;
        case "brighten":
          cmd = new Brighten(s.nextInt(), s.next(), s.next());
          break;
        case "greyscale":
          cmd = new Greyscale(s.next(), s.next(), s.next());
          break;
        case "vertical-flip":
          cmd = new Vflip(s.next(), s.next());
          break;
        case "horizontal-flip":
          cmd = new Hflip(s.next(), s.next());
          break;
        default:
          output.append(String.format("Unknown command %s", in));
          System.out.println(String.format("Unknown command %s", in));
          cmd = null;
          break;
      }
      if (cmd != null) {
        cmd.go(model);
        output.append("\nExecuted: " + command);
        System.out.println("\nExecuted: " + command);
      }

    }
    return output.toString();
  }

  public String processFileScript(Scanner fileScan) throws IOException {
    StringBuilder outputs = new StringBuilder();

    while (fileScan.hasNextLine()) {
      String line = fileScan.nextLine();

      //ignore comments
      if (line.charAt(0) == '#') continue;

      outputs.append(
              processCommand(fileScan.nextLine())
      );
    }
    return outputs.toString();
  }

  @Override
  public void go(ImageProcessor model) throws IOException {
    Objects.requireNonNull(model);
    this.model = model;

    Scanner scan = new Scanner(this.in);

    System.out.println("Please enter cmd follow by param:");
    //Keep asking for cmd
    while (scan.hasNextLine()) {
      processCommand(scan.nextLine());
      System.out.println("Please enter cmd follow by param:");
    }
  }

  public static void main(String[] args) throws IOException {
    StringBuffer out = new StringBuffer();
    Reader in = new StringReader("+ 3 4 + 8 9 q");
    IController ctrl = new ImageController(new InputStreamReader(System.in), out);
    ctrl.go(new PpmProcessor());

  }


}
