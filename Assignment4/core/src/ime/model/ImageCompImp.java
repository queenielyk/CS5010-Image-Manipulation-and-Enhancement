package ime.model;

/**
 * An implementation class of interface IME.model.ImageComp. An image component contains red, green,
 * blue value, and the following image component as well.
 */
public class ImageCompImp implements ImageComp {

  private final int red;
  private final int green;
  private final int blue;
  private ImageComp nextComp;


  /**
   * A constructor to construct an image component. Three positive integers should be provided to
   * indicate R, G, B respectively.
   *
   * @param red   an integer to represent red value of a pixel
   * @param green an integer to represent green value of a pixel
   * @param blue  an integer to represent blue value of a pixel
   * @throws IllegalStateException any value less than 0 (a.k.a negative integer)
   */
  public ImageCompImp(int red, int green, int blue) throws IllegalStateException {
    if (red < 0 || green < 0 || blue < 0) {
      throw new IllegalStateException("RGB must be positive integer");
    }

    this.red = red;
    this.green = green;
    this.blue = blue;
    this.nextComp = null;
  }

  @Override
  public int[] getRGB() {
    return new int[]{this.red, this.green, this.blue};
  }

  @Override
  public void setNext(ImageComp next) {
    this.nextComp = next;
  }

  @Override
  public ImageComp getNext() {
    return nextComp;
  }
}
