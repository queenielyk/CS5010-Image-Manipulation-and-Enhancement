package imageprocessing.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * tests Pixel.
 */
public class PixelTest {

  IPixel testPixel = new Pixel(1, 1, 1);

  @Test
  public void testGetRed() {
    assertEquals(1, testPixel.getRed());
  }

  @Test
  public void testGetGreen() {
    assertEquals(1, testPixel.getGreen());
  }

  @Test
  public void testGetBlue() {
    assertEquals(1, testPixel.getGreen());
  }
}