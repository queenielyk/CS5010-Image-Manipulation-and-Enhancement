package gime.model;

import mime.model.MoreImageProcessor;

public class ReadOnlyImageProcessorImpl implements ReadOnlyImageProcessor {

  ReadOnlyImageProcessor processor;

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
}
