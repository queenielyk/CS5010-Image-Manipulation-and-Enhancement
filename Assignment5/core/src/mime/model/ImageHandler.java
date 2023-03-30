package mime.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * An interface to represent an image reader. An image reader is a reader to read image from an
 * InputStream, then store it as a 3D-Array `int[Height][Width][RGB]`.
 */
public interface ImageHandler {

  /**
   * A method to read image from an InputStream.
   *
   * @param stream an InputStream of image
   */
  void readImage(InputStream stream) throws IOException;

  /**
   * A method to return a read image in form of int[][][].
   *
   * @return 3D-Array of image
   */
  int[][][] getImage();

  /**
   * A method to return the info of read image in terms of `int[width, height, maxi-value]`.
   *
   * @return a int array of image info
   */
  int[] getInfo();

  /**
   * A method to save image from model through OutputStream
   */
  void saveImage(OutputStream stream, String format, int[] info, int[][][] image) throws IOException;
}
