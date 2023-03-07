package control.command;

import IME.model.ImageProcessor;
import control.ImageCommand;

public class Greyscale implements ImageCommand {
  String mode;
  String from;
  String to;

  public Greyscale(String mode, String from, String to) {
    this.mode = mode;
    this.from = from;
    this.to = to;
  }

  @Override
  public void go(ImageProcessor model) {
    model.greyscale(mode, from, to);
  }
}
