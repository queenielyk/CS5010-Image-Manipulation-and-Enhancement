package mime.control.command;

import java.io.IOException;

import mime.control.MoreImageCommand;
import mime.model.MoreImageProcessor;

/**
 * This represents a Filter command.
 */
public class Filter implements MoreImageCommand {

  String mode;
  String from;
  String to;

  /**
   * Build a filter command.
   *
   * @param mode name of the greyscale mode to apply
   * @param from name of source image
   * @param to   name of image producing
   */
  public Filter(String mode, String from, String to) {
    this.mode = mode;
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
    model.filter(mode, from, to);
  }


}
