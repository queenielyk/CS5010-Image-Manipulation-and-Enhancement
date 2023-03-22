package ime.control.command;

import java.io.IOException;

import ime.control.ImageCommand;
import ime.model.ImageProcessor;

/**
 * This class represent a save command.
 */
public class Save implements ImageCommand {

  private String path;
  private String from;

  public Save(String path, String from) {
    this.path = path;
    this.from = from;
  }


  @Override
  public void execute(ImageProcessor model) throws IOException {
    model.save(from, path);
  }
}
