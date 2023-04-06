package ime.model;

/**
 * An interface to represent an image component. Each image component is a combination of red,
 * green, blue. An image is a series of image component, each image component may reference to a
 * followed image component, otherwise null, if it is the last component.
 */
public interface ImageComp {

  /**
   * A method access the red, green blue values of this image component.
   *
   * @return an array of int: red, green, blue (fixed order)
   */
  int[] getRGB();

  /**
   * A method to get the reference of the image component that following this component.
   *
   * @return the image component that following this component
   */
  ImageComp getNext();

  /**
   * A method to set the reference to the image component that following this component.
   *
   * @param next the image component that following this component
   */
  void setNext(ImageComp next);


}
