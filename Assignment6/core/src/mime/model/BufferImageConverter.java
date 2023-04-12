package mime.model;

import java.awt.image.BufferedImage;

/**
 * An interface to indicate a conversion of image from int[][][] to a BufferImage.
 */
public interface BufferImageConverter {

  /**
   * A method to convert an image from int[][][] to a BufferImage.
   *
   * @param info  the info of an image, int[width, height, maxi-value]
   * @param image the content of an image, int[row, col, [r, g, b]]
   * @return a BufferImage
   */
  BufferedImage convertToBufferImg(int[] info, int[][][] image);
}
