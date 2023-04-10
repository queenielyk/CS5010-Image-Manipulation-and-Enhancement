package gime.model;

import mime.model.MoreImageProcessor;

/**
 * A class as an object adaptor, provides read-only on an image processor.
 */
public class ReadOnlyImageProcessorImpl implements ReadOnlyImageProcessor {

  ReadOnlyImageProcessor processor;

  /**
   * A constructor to construct a ReadOnlyImageProcessor by taking a MoreImageProcessor.
   *
   * @param processor a MoreImageProcessor
   */
  public ReadOnlyImageProcessorImpl(MoreImageProcessor processor) {
    this.processor = processor;
  }

  @Override
  public int[][][] getImage(String name) {
    return processor.getImage(name);
  }

  @Override
  public int[] getInfo(String name) {
    return processor.getInfo(name);
  }

  @Override
  public String[] getNameList() {
    return processor.getNameList();
  }
}
