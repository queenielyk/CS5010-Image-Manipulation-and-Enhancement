package control.command;

import java.io.FileNotFoundException;

import IME.model.ImageProcessor;
import control.ImageCommand;

public class Load implements ImageCommand {
  private String path;
  private String imgName;

  public Load(String path, String imgName) {
    this.path = path;
    this.imgName = imgName;
  }

  @Override
  public void go(ImageProcessor model) throws FileNotFoundException {
    model.loadImage(this.path, this.imgName);
  }
}
