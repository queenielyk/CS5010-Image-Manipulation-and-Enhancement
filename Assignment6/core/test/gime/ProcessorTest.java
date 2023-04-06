package gime;

import org.junit.Test;

import gime.model.SingletonImageProcessor;
import gime.model.SingletonImageProcessorImpl;
import gime.model.ReadOnlyImageProcessor;
import gime.model.ReadOnlyImageProcessorImpl;

import static org.junit.Assert.assertEquals;

public class ProcessorTest {

  @Test(expected = ClassCastException.class)
  public void testCastingReadOnly() {
    SingletonImageProcessor processor = SingletonImageProcessorImpl.getInstance();
    ReadOnlyImageProcessor ro = new ReadOnlyImageProcessorImpl();

    processor.loadImage(new int[]{1, 1, 255}, new int[][][]{{{255, 255, 255}}}, "image");
    ro = (SingletonImageProcessor) ro;
  }

  @Test
  public void testSameInstance() {
    SingletonImageProcessor processor = SingletonImageProcessorImpl.getInstance();
    ReadOnlyImageProcessor ro = new ReadOnlyImageProcessorImpl();

    processor.loadImage(new int[]{1, 1, 255}, new int[][][]{{{255, 255, 255}}}, "image");
    assertEquals(processor.getInfo("image").toString(), ro.getInfo("image").toString());
  }

  @Test
  public void testROFirst() {
    ReadOnlyImageProcessor ro = new ReadOnlyImageProcessorImpl();
    SingletonImageProcessor processor = SingletonImageProcessorImpl.getInstance();

    processor.loadImage(new int[]{1, 1, 255}, new int[][][]{{{255, 255, 255}}}, "image");
    assertEquals(processor.getInfo("image").toString(), ro.getInfo("image").toString());
  }


}
