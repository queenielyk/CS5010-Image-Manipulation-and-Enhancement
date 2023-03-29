package mime.control.command;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import mime.model.MoreImageProcessor;

public class LoadBufImg implements MoreImageCommand {

  private final String path;
  private final String imgName;

  /**
   * Build a load command.
   *
   * @param path    string to load file from
   * @param imgName name of image file will be store as
   */
  public LoadBufImg(String path, String imgName) {
    this.path = path;
    this.imgName = imgName;
  }

  /**
   * delegate command to model's different method.
   *
   * @param model model to work on
   * @throws IOException if IO error occurs in model's method(ex. load can not find file)
   */
  @Override
  public void execute(MoreImageProcessor model) throws IOException {
    InputStream inputStream = new FileInputStream(path);
    String format = path.split("\\.")[1];
    model.loadImage(inputStream, imgName, format);

  }


}
