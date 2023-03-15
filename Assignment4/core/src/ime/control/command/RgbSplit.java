package ime.control.command;

import ime.control.ImageCommand;
import ime.model.ImageProcessor;

/**
 * This class represent a rgb-split command.
 */
public class RgbSplit implements ImageCommand {

  private String from;
  private String r;
  private String g;
  private String b;

  /**
   * Build a Rgb Split command.
   * @param from name of source image
   * @param r name of red-component image to produce
   * @param g name of green-component image to produce
   * @param b name of blue-component image to produce
   */
  public RgbSplit(String from, String r, String g, String b) {
    this.from = from;
    this.r = r;
    this.g = g;
    this.b = b;
  }


  @Override
  public void execute(ImageProcessor model) {
    model.greyscale("red-component", from, r);
    model.greyscale("green-component", from, g);
    model.greyscale("blue-component", from, b);
  }
}
