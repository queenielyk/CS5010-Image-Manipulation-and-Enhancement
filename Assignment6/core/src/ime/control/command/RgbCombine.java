package ime.control.command;

import ime.control.ImageCommand;
import ime.model.ImageProcessor;

/**
 * This class represent a rgb-combine command.
 */
public class RgbCombine implements ImageCommand {

  private final String to;
  private final String r;
  private final String g;
  private final String b;

  /**
   * Build a Rgb Combine command.
   *
   * @param to name of producing image
   * @param r  red-component to combine
   * @param g  green-component to combine
   * @param b  blue-component to combine
   */
  public RgbCombine(String to, String r, String g, String b) {
    this.to = to;
    this.r = r;
    this.g = g;
    this.b = b;
  }

  @Override
  public void execute(ImageProcessor model) {
    model.combines(r, g, b, to);
  }
}
