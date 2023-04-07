package gime;

import org.junit.Test;

import gime.model.ReadOnlyImageProcessor;
import gime.model.ReadOnlyImageProcessorImpl;
import mime.model.MoreImageProcessor;
import mime.model.MoreImageProcessorImpl;

import static org.junit.Assert.assertEquals;

public class ProcessorTest {

  @Test(expected = ClassCastException.class)
  public void testCastingReadOnly() {
    MoreImageProcessor processor = new MoreImageProcessorImpl();
    ReadOnlyImageProcessor ro = new ReadOnlyImageProcessorImpl(processor);

    processor.loadImage(new int[]{1, 1, 255}, new int[][][]{{{255, 255, 255}}}, "image");
    ro = (MoreImageProcessor) ro;
  }

  @Test
  public void testSameInstance() {
    MoreImageProcessor processor = new MoreImageProcessorImpl();
    ReadOnlyImageProcessor ro = new ReadOnlyImageProcessorImpl(processor);

    processor.loadImage(new int[]{1, 1, 255}, new int[][][]{{{255, 255, 255}}}, "image");
    assertEquals(processor.getInfo("image").toString(), ro.getInfo("image").toString());
  }

}
