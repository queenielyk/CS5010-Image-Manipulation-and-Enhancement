package control.command;

import IME.model.ImageProcessor;
import control.ImageCommand;

public class Hflip implements ImageCommand {
  String from;
  String to;

  public Hflip(String from, String to) {
    this.from = from;
    this.to = to;
  }

  @Override
  public void go(ImageProcessor model) {
    model.horizontalFlip(from, to);
  }
}
