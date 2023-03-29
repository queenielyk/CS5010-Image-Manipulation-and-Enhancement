package mime.model;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

/**
 * A class to be an image reader by using ImageIO library.
 * Acceptable image format:
 * - bmp
 * - jpg
 * - jpeg
 * - png
 */
public class ImageIOHandler extends AbsrtuctImageHandler {
  public ImageIOHandler() {
    super();
  }

  @Override
  public void readImage(InputStream stream) throws IOException {
    BufferedImage image = ImageIO.read(stream);

    if (image == null) {
      throw new IllegalStateException("Image is not accessible!");
    }

    int width = image.getWidth();
    int height = image.getHeight();
    this.info = new int[]{width, height, 255};

    int[] dataBuffInt = image.getRGB(0, 0, width, height, null, 0, width);
    this.image = new int[height][width][3];
    int row = 0;
    int col = 0;

    for (int rgb : dataBuffInt) {
      int red = (rgb >> 16) & 0xFF;
      int green = (rgb >> 8) & 0xFF;
      int blue = (rgb) & 0xFF;
      this.image[row][col] = new int[]{red, green, blue};
      col++;
      if (col == width) {
        col = 0;
        row++;
      }
    }
  }
}
