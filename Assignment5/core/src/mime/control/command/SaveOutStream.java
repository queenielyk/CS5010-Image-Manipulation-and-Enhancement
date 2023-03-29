package mime.control.command;

import ime.control.command.Save;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import mime.model.MoreImageProcessor;

/**
 * This represents a save command.
 */
public class SaveOutStream extends Save implements MoreImageCommand {

  public SaveOutStream(String path, String from) {
    super(path, from);
  }

  /**
   * delegate command to model's different method.
   *
   * @param model model to work on
   * @throws IOException If IO error occurs in model’s method(ex. load can not find file)
   */
  @Override
  public void execute(MoreImageProcessor model) throws IOException {
    OutputStream outputStream = new FileOutputStream(path);
    String format = path.split("\\.")[1];
    model.save(from, outputStream, format);
  }
}
