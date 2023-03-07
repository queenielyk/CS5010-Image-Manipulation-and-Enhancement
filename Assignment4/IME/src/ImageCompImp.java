/**
 * An implementation class of interface ImageComp.
 */
public class ImageCompImp implements ImageComp {
  private final int red;
  private final int green;
  private final int blue;
  private ImageComp nextComp;


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
