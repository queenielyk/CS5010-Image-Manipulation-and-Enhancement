package ime.control.command;

import ime.control.ImageCommand;
import ime.model.ImageProcessor;

/**
 * This class represent a VerticalFlip command.
 */
public class Vflip implements ImageCommand {

  private String from;
  private String to;

  public Vflip(String from, String to) {
    this.from = from;
    this.to = to;
  }

  @Override
  public void execute(ImageProcessor model) {
    model.verticalFlip(from, to);
  }
}
