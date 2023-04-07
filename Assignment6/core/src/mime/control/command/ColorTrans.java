package mime.control.command;

import java.io.IOException;

import mime.control.MoreImageCommand;
import mime.model.MoreImageProcessor;

/**
 * This represents a ColorTransformation command.
 */
public class ColorTrans implements MoreImageCommand {

  protected String mode;
  protected String from;
  protected String to;

  /**
   * Build a greyscale command.
   *
   * @param mode name of the greyscale mode to apply
   * @param from name of source image
   * @param to   name of image producing
   */
  public ColorTrans(String mode, String from, String to) {
    this.mode = mode;
    this.from = from;
    this.to = to;
  }


  @Override
  public void execute(MoreImageProcessor model) throws IOException {
    model.colorTrans(mode, from, to);
  }
}
