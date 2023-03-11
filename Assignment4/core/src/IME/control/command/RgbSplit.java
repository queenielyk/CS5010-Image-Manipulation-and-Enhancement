package IME.control.command;

import IME.control.ImageCommand;
import IME.model.ImageProcessor;

/**
 * This class represent a rgb-split command.
 */
public class RgbSplit implements ImageCommand {
  String from;
  String R;
  String G;
  String B;

  public RgbSplit(String from, String R, String G, String B) {
    this.from = from;
    this.R = R;
    this.G = G;
    this.B = B;
  }


  @Override
  public void go(ImageProcessor model) {
    model.greyscale("red-component", from, R);
    model.greyscale("green-component", from, G);
    model.greyscale("blue-component", from, B);
  }
}
