package imageprocessing.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Contain an assertion test for Dither, to further supporting the implementation issue.
 */
public class FailDitherTest {
  @Test
  public void testDitherByReceiver() {
    List<IPixel> image = new ArrayList<>();
    image.add(new Pixel(234, 232, 236));
    image.add(new Pixel(208, 195, 193));
    image.add(new Pixel(168, 150, 149));
    image.add(new Pixel(234, 230, 231));
    image.add(new Pixel(194, 184, 187));
    image.add(new Pixel(116, 99, 101));
    image.add(new Pixel(212, 203, 206));
    image.add(new Pixel(170, 150, 150));
    image.add(new Pixel(70, 42, 44));

    IImageModel imageModel = new ImageModel(3, 3, 255, image);
    List<IPixel> imageList = imageModel.dither().getImage();

    List<IPixel> resultPixels = new ArrayList<>();
    resultPixels.add(new Pixel(255, 255, 255));
    resultPixels.add(new Pixel(255, 255, 255));
    resultPixels.add(new Pixel(0, 0, 0));
    resultPixels.add(new Pixel(255, 255, 255));
    resultPixels.add(new Pixel(255, 255, 255));
    resultPixels.add(new Pixel(0, 0, 0));
    resultPixels.add(new Pixel(255, 255, 255));
    resultPixels.add(new Pixel(0, 0, 0));
    resultPixels.add(new Pixel(0, 0, 0));
    List<IPixel> resultImage = new ImageModel(3, 3, 255, resultPixels).getImage();

    for (int i = 0; i < 3 * 3; i++) {
      try {
        assertEquals(resultImage.get(i).getRed(), imageList.get(i).getRed());
      } catch (AssertionError e) {
        System.out.format("Assertion Error at index: %d - getRed()\n", i);
      }
      try {
        assertEquals(resultImage.get(i).getGreen(), imageList.get(i).getGreen());
      } catch (AssertionError e) {
        System.out.format("Assertion Error at index: %d - getGreen()\n", i);
      }
      try {
        assertEquals(resultImage.get(i).getBlue(), imageList.get(i).getBlue());
      } catch (AssertionError e) {
        System.out.format("Assertion Error at index: %d - getBlue()\n", i);
      }
    }
  }
}
