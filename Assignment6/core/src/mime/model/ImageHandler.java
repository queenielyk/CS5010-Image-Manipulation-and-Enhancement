package mime.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * An interface to represent an image handler. An image handler is a reader and a writer.
 * To read image from an InputStream, then store it as a 3D-Array `int[Height][Width][RGB]`.
 * To write image from 3D-Array into an OutputStream.
 */
public interface ImageHandler {

  /**
   * A method to read image from an InputStream.
   *
   * @param stream an InputStream of image
   * @throws IOException Reading the image is problematic
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
   * @return an int array of image info
   */
  int[] getInfo();

  /**
   * A method to save image from model through OutputStream.
   *
   * @param stream an OutputStream Object
   * @param format the format of exporting image
   * @param info   the info of exporting image
   * @param image  the image
   * @throws IOException Writing the image is problematic
   */
  void saveImage(OutputStream stream, String format, int[] info, int[][][] image)
          throws IOException;
}
