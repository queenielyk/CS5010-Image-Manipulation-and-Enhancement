package gime.model;

public interface ReadOnlyImageProcessor {
  /**
   * A method to return an image.
   *
   * @param name name of image
   * @return image in form of int[][][]
   */
  int[][][] getImage(String name);

  /**
   * A method to return info of image.
   *
   * @param name name of image
   * @return info in form of int[]
   */
  int[] getInfo(String name);

}
