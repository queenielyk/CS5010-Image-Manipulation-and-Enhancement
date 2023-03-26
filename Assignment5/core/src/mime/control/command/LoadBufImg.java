package mime.control.command;

import ime.control.ImageCommand;
import ime.control.command.Load;
import ime.model.ImageProcessor;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import mime.model.MoreImageProcessor;

public class LoadBufImg implements MoreImageCommand {

  private String path;
  private String imgName;

  /**
   * Build a load command.
   *
   * @param path string to load file from
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
    if(path.endsWith(".ppm")){
      model.loadImage(path,imgName);
    }
    else {
      BufferedImage bufImg = ImageIO.read(new FileInputStream(path));
      model.loadImage(bufImg, imgName);
    }
  }


}
