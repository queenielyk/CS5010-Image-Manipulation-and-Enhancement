package ime.control.command;

import ime.control.ImageCommand;
import ime.model.ImageProcessor;

/**
 * This class represent a rgb-split command.
 */
public class RgbSplit implements ImageCommand {

  private final String from;
  private final String r;
  private final String g;
  private final String b;

  /**
   * Build a Rgb Split command.
   *
   * @param from name of source image
   * @param r    name of red-component image to produce
   * @param g    name of green-component image to produce
   * @param b    name of blue-component image to produce
   */
  public RgbSplit(String from, String r, String g, String b) {
    this.from = from;
    this.r = r;
    this.g = g;
    this.b = b;
  }


  @Override
  public void execute(ImageProcessor model) {
    model.colorTrans("red-component", from, r);
    model.colorTrans("green-component", from, g);
    model.colorTrans("blue-component", from, b);
  }
}
