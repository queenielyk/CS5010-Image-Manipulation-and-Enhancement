package gime;

import org.junit.Test;

import gime.model.ImageProcessor;
import gime.model.ImageProcessorImpl;
import gime.model.ReadOnlyImageProcessor;
import gime.model.ReadOnlyImageProcessorImpl;

import static org.junit.Assert.assertEquals;

public class ProcessorTest {

  @Test(expected = ClassCastException.class)
  public void testCastingReadOnly() {
    ImageProcessor processor = ImageProcessorImpl.getInstance();
    ReadOnlyImageProcessor ro = new ReadOnlyImageProcessorImpl();

    processor.loadImage(new int[]{1, 1, 255}, new int[][][]{{{255, 255, 255}}}, "image");
    ro = (ImageProcessor) ro;
  }

  @Test
  public void testSameInstance() {
    ImageProcessor processor = ImageProcessorImpl.getInstance();
    ReadOnlyImageProcessor ro = new ReadOnlyImageProcessorImpl();

    processor.loadImage(new int[]{1, 1, 255}, new int[][][]{{{255, 255, 255}}}, "image");
    assertEquals(processor.getInfo("image").toString(), ro.getInfo("image").toString());
  }


}
