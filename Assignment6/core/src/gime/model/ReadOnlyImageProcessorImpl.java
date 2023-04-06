package gime.model;

public class ReadOnlyImageProcessorImpl implements ReadOnlyImageProcessor {

  ReadOnlyImageProcessor processor;

  public ReadOnlyImageProcessorImpl() {
    this.processor = ImageProcessorImpl.getInstance();
  }

  @Override
  public int[][][] getImage(String name) {
    return processor.getImage(name);
  }

  @Override
  public int[] getInfo(String name) {
    return processor.getInfo(name);
  }
}
