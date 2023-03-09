package control.command;

import IME.model.ImageProcessor;
import control.ImageCommand;

/**
 * This class represent a rgb-combine command.
 */
public class RgbCombine implements ImageCommand {
  String to;
  String R;
  String G;
  String B;

  public RgbCombine(String to, String R, String G, String B) {
    this.to = to;
    this.R = R;
    this.G = G;
    this.B = B;
  }

  @Override
  public void go(ImageProcessor model) {
    model.combines(R, G, B, to);
  }
}
