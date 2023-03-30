package mime.control.command;

import java.io.IOException;

import mime.control.MoreImageCommand;
import mime.model.MoreImageProcessor;

/**
 * This represent a dither command.
 */
public class Dither implements MoreImageCommand {

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
  public void execute(MoreImageProcessor model) throws IOException {
    model.dithering(from, to);
  }
}
