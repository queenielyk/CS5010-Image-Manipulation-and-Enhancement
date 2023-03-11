package IME.control.command;

import java.io.IOException;

import IME.control.ImageCommand;
import IME.model.ImageProcessor;

/**
 * This class represent a save command.
 */
public class Save implements ImageCommand {
  String path;
  String from;

  public Save(String path, String from) {
    this.path = path;
    this.from = from;
  }


  @Override
  public void go(ImageProcessor model) throws IOException {
    model.save(from, path);
  }
}
