package ime.control.command;

import ime.control.ImageCommand;
import ime.model.ImageProcessor;
import java.io.IOException;

public class Dither implements ImageCommand {

  String from;
  String to;

  /**
   * Build a Color transformation command.
   *
   * @param from name of source image
   * @param to   name of image producing
   */
  public Dither(String from, String to) {
    this.from = from;
    this.to = to;
  }

  /**
   * delegate command to model's different method.
   *
   * @param model model to work on
   * @throws IOException if IO error occurs in model's method(ex. load can not find file)
   */
  @Override
  public void execute(ImageProcessor model) throws IOException {

  }
}
