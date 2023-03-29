package ime.control.command;

import ime.control.ImageCommand;
import ime.model.ImageProcessor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This class represent a save command.
 */
public class Save implements ImageCommand {

  private final String path;
  private final String from;

  public Save(String path, String from) {
    this.path = path;
    this.from = from;
  }


  @Override
  public void execute(ImageProcessor model) throws IOException {
    OutputStream outputStream = new FileOutputStream(path);
    String format = path.split(".")[1];
    model.save(from, outputStream, format);
  }
}
