package mime.model;

/**
 * A abstract class to implement ImageHandler. Implement common methods here.
 */
public abstract class AbsrtuctImageHandler implements ImageHandler {

  protected int[][][] image;
  protected int[] info;

  public AbsrtuctImageHandler() {
  }

  @Override
  public int[][][] getImage() {
    return this.image;
  }

  @Override
  public int[] getInfo() {
    return this.info;
  }



}
