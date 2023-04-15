package imageprocessing.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests ImageModel Class.
 */
public class ImageModelTest {
  private IImageModel imageModel;
  private final List<IPixel> resultPixels = new ArrayList<>();
  List<IPixel> image = new ArrayList<>();

  @Before
  public void returnSourceImage() {
    IPixel pixel = new Pixel(10, 12, 13);
    image.add(pixel);
    imageModel = new ImageModel(1, 1, 255, image);
  }

  @Test
  public void testBrightenImage() {
    resultPixels.add(new Pixel(30, 32, 33));
    assertEquals(new ImageModel(1, 1, 255, resultPixels)
            , imageModel.brightenImage(20));
  }

  @Test
  public void testGreyscale() {
    resultPixels.add(new Pixel(10, 10, 10));
    assertEquals(new ImageModel(1, 1, 255, resultPixels)
            , imageModel.greyscale("red"));
  }

  @Test
  public void testHorizontalflip() {
    resultPixels.add(new Pixel(10, 12, 13));
    assertEquals(new ImageModel(1, 1, 255, resultPixels)
            , imageModel.horizontalFlip());
  }

  @Test
  public void testVerticalFlip() {
    resultPixels.add(new Pixel(10, 12, 13));
    assertEquals(new ImageModel(1, 1, 255, resultPixels)
            , imageModel.verticalFlip());
  }

  @Test
  public void testCombineRGB() {
    List<IPixel> redPixels = new ArrayList<>();
    redPixels.add(new Pixel(10, 10, 10));
    List<IPixel> greenPixels = new ArrayList<>();
    greenPixels.add(new Pixel(12, 12, 12));
    List<IPixel> bluePixels = new ArrayList<>();
    bluePixels.add(new Pixel(13, 13, 13));
    IImageModel sourceRedImage = new ImageModel(1, 1, 255, redPixels);
    IImageModel sourceGreenImage = new ImageModel(1, 1, 255, greenPixels);
    IImageModel sorceBlueImage = new ImageModel(1, 1, 255, bluePixels);
    resultPixels.add(new Pixel(10, 12, 13));
    assertEquals(new ImageModel(1, 1, 255, resultPixels)
            , sourceRedImage.combineRGB(sourceGreenImage, sorceBlueImage));
  }

  @Test
  public void testDither() {
    resultPixels.add(new Pixel(0, 0, 0));
    assertEquals(new ImageModel(1, 1, 255, resultPixels)
            , imageModel.dither());
  }

  @Test
  public void testFilterWithBlur() {
    resultPixels.add(new Pixel(2, 3, 3));
    assertEquals(new ImageModel(1, 1, 255, resultPixels)
            , imageModel.filter("blur"));
  }

  @Test
  public void testFilterWithSharpen() {
    resultPixels.add(new Pixel(10, 12, 13));
    assertEquals(new ImageModel(1, 1, 255, resultPixels)
            , imageModel.filter("sharpen"));
  }

  @Test
  public void testSepia() {
    resultPixels.add(new Pixel(15, 13, 10));
    assertEquals(new ImageModel(1, 1, 255, resultPixels)
            , imageModel.colorTransformations("sepia"));
  }

  @Test
  public void testGetImage() {
    resultPixels.add(new Pixel(10, 12, 13));
    assertEquals(resultPixels
            , imageModel.getImage());
  }

  @Test
  public void testGetHeight() {
    assertEquals(1
            , imageModel.getHeight());
  }

  @Test
  public void testGetWidth() {
    assertEquals(1
            , imageModel.getWidth());
  }

  @Test
  public void testGetMaxValue() {
    assertEquals(255
            , imageModel.getMaxValue());
  }

  @Test
  public void testEquals() {
    resultPixels.add(new Pixel(10, 12, 13));
    assertEquals(true, imageModel.equals(new ImageModel(
            1, 1, 255, resultPixels)));
  }
}