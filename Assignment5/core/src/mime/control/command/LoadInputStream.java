package mime.control.command;


import java.io.FileInputStream;
import java.io.IOException;

import mime.model.ImageHandler;
import mime.model.ImageIOHandler;
import mime.model.MoreImageProcessor;
import mime.model.PpmHandler;

/**
 * This represents a loadInputStream command, which takes a path and parse it and delegate a
 * different handler to model.
 */
public class LoadInputStream implements MoreImageCommand {

  private final String path;
  private final String imgName;

  /**
   * Build a load command.
   *
   * @param path    string to load file from
   * @param imgName name of image file will be store as
   */
  public LoadInputStream(String path, String imgName) {
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
  public void execute(MoreImageProcessor model) throws IOException, IllegalStateException {
    String format = path.split("\\.")[1];
    ImageHandler handler = null;

    switch (format) {
      case "ppm":
        handler = new PpmHandler();
        break;
      case "jpg":
      case "jpeg":
      case "png":
      case "bmp":
        handler = new ImageIOHandler();
        break;
      default:
        throw new IllegalStateException("Handler do not support:" + format + "format");
    }

    handler.readImage(new FileInputStream(path));
    model.loadImage(handler.getInfo(), handler.getImage(), imgName);
  }


}
