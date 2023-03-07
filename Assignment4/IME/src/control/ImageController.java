package control;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

import IME.model.ImageProcessor;
import control.command.Brighten;
import control.command.Greyscale;
import control.command.Load;

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
        case "brighten":
          cmd = new Brighten(s.nextInt(), s.next(), s.next());
          break;
        case "greyscale":
          cmd = new Greyscale(s.next(), s.next(), s.next());
        case "vertical-flip":


      }

      if (cmd != null) {
        cmd.go(model);
        output.append("\nExecuted: " + command);
      }

    }
    return null;
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
    Scanner scan = new Scanner(this.in);

    //Keep asking for cmd
    while (scan.hasNext()) {
      processCommand(scan.next());
    }
  }


}
