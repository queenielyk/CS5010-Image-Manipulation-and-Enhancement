package mime.control.command;

import java.io.IOException;

import mime.control.MoreImageCommand;
import mime.model.MoreImageProcessor;

/**
 * This represents a dither command.
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


  @Override
  public void execute(MoreImageProcessor model) throws IOException {
    model.dithering(from, to);
  }
}
