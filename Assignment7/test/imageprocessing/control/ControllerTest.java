package imageprocessing.control;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import imageprocessing.control.exceptionhandler.InvalidFileException;
import imageprocessing.control.imagecommands.Brighten;
import imageprocessing.control.imagecommands.Filter;
import imageprocessing.control.imagecommands.IImageCommand;
import imageprocessing.control.imagecommands.Combine;
import imageprocessing.control.imagecommands.Greyscale;
import imageprocessing.model.IImageModel;
import imageprocessing.model.IPixel;
import imageprocessing.model.Pixel;

import static org.junit.Assert.assertEquals;

/**
 * tests Controller.
 */
public class ControllerTest {
  /**
   * Mocks the image model.
   */
  public static class MockImageModel implements IImageModel {
    List<IPixel> image;
    int height;
    int width;
    int maxValue;
    StringBuilder mockLog;

    MockImageModel(int height, int width, int maxValue, List<IPixel> image
                   , StringBuilder mockLog) {
      this.height = height;
      this.width = width;
      this.maxValue = maxValue;
      this.image = image;
      this.mockLog = mockLog;
    }

    @Override
    public IImageModel brightenImage(int increment) {
      mockLog.append(increment);

      return null;
    }

    /**
     * Execute the mosaic command.
     *
     * @param seed the seed for mosaic cluster algorithm
     * @return - a new processed image object
     */
    @Override
    public IImageModel mosaic(int seed) {
      mockLog.append(seed);
      return null;
    }

    @Override
    public IImageModel greyscale(String component) throws IllegalArgumentException {
      mockLog.append(component);

      return null;
    }

    @Override
    public IImageModel horizontalFlip() {
      return null;
    }

    @Override
    public IImageModel verticalFlip() {
      return null;
    }

    @Override
    public IImageModel combineRGB(IImageModel sourceGreenModel, IImageModel sourceBlueModel)
            throws IllegalArgumentException {
      writeImageToString(sourceGreenModel);
      writeImageToString(sourceBlueModel);

      return null;
    }

    @Override
    public IImageModel filter(String action) {
      mockLog.append(action);

      return null;
    }

    @Override
    public IImageModel dither() {
      return null;
    }

    @Override
    public IImageModel colorTransformations(String typeOfTransformation) {
      return null;
    }

    @Override
    public int getHeight() {
      return this.height;
    }

    @Override
    public int getWidth() {
      return this.width;
    }

    @Override
    public int getMaxValue() {
      return this.maxValue;
    }

    @Override
    public List<IPixel> getImage() {
      return this.image;
    }

    private void writeImageToString(IImageModel sourceImageModel) {
      mockLog.append(sourceImageModel.getHeight());
      mockLog.append(sourceImageModel.getWidth());
      mockLog.append(sourceImageModel.getMaxValue());

      for (IPixel p : sourceImageModel.getImage()) {
        mockLog.append(p.getRed());
        mockLog.append(p.getGreen());
        mockLog.append(p.getBlue());
      }
    }
  }

  @Test
  public void testBrightenControllerUsingMocks() throws InvalidFileException, IOException {
    StringBuilder logs = new StringBuilder();
    List<IImageModel> sourceImageModels = new ArrayList<>();

    List<IPixel> image = new ArrayList<>();
    image.add(new Pixel(10, 12, 13));
    String imageName = "koala";
    int height = 1;
    int width = 1;
    int maxValue = 200;

    IImageModel mockImageModel = new MockImageModel(height, width, maxValue, image, logs);
    sourceImageModels.add(mockImageModel);

    String increment = "10";
    StringBuilder expectedImageModel = new StringBuilder(increment);

    IImageCommand bright = new Brighten(imageName, "koala-brighter"
            , increment);
    bright.executeCommand(sourceImageModels);

    assertEquals(expectedImageModel.toString(), logs.toString());
  }

  @Test
  public void testGrayscaleControllerUsingMocks() throws IOException, InvalidFileException {
    StringBuilder logs = new StringBuilder();
    List<IImageModel> sourceImageModels = new ArrayList<>();

    List<IPixel> image = new ArrayList<>();
    image.add(new Pixel(10, 12, 13));
    String imageName = "koala";
    int height = 1;
    int width = 1;
    int maxValue = 200;

    IImageModel mockImageModel = new MockImageModel(height, width, maxValue, image, logs);
    sourceImageModels.add(mockImageModel);

    String component = "luma";
    StringBuilder expectedImageModel = new StringBuilder(component);

    IImageCommand greyscale = new Greyscale(imageName, "koala-grayscale"
            , component);
    greyscale.executeCommand(sourceImageModels);

    assertEquals(expectedImageModel.toString(), logs.toString());
  }

  @Test
  public void testCombineControllerUsingMocks() throws InvalidFileException, IOException {
    StringBuilder logs = new StringBuilder();
    List<IImageModel> sourceImageModels = new ArrayList<>();

    List<IPixel> redImage = new ArrayList<>();
    redImage.add(new Pixel(230, 120, 70));
    int redHeight = 1;
    int redWidth = 1;
    int redMaxValue = 230;
    String redImageName = "koala-red";

    List<IPixel> greenImage = new ArrayList<>();
    greenImage.add(new Pixel(90, 200, 130));
    int greenHeight = 1;
    int greenWidth = 1;
    int greenMaxValue = 200;
    String greenImageName = "koala-green";

    List<IPixel> blueImage = new ArrayList<>();
    blueImage.add(new Pixel(240, 40, 255));
    int blueHeight = 1;
    int blueWidth = 1;
    int blueMaxValue = 255;
    String blueImageName = "koala-blue";

    IImageModel mockRedImageModel = new MockImageModel(redHeight
            , redWidth, redMaxValue, redImage, logs);
    sourceImageModels.add(mockRedImageModel);

    IImageModel mockGreenImageModel = new MockImageModel(greenHeight
            , greenWidth, greenMaxValue, greenImage, logs);
    sourceImageModels.add(mockGreenImageModel);

    IImageModel mockBlueImageModel = new MockImageModel(blueHeight
            , blueWidth, blueMaxValue, blueImage, logs);
    sourceImageModels.add(mockBlueImageModel);

    StringBuilder expectedImageModel = constructExpectedImageModel(mockGreenImageModel);
    expectedImageModel.append(constructExpectedImageModel(mockBlueImageModel));

    IImageCommand combine = new Combine(redImageName, greenImageName, blueImageName
            , "koala-combine");
    combine.executeCommand(sourceImageModels);

    assertEquals(expectedImageModel.toString(), logs.toString());
  }

  @Test
  public void testFilterControllerUsingMocks() throws IOException, InvalidFileException {
    StringBuilder logs = new StringBuilder();
    List<IImageModel> sourceImageModels = new ArrayList<>();

    List<IPixel> image = new ArrayList<>();
    image.add(new Pixel(10, 12, 13));
    String imageName = "koala";
    int height = 1;
    int width = 1;
    int maxValue = 200;

    IImageModel mockImageModel = new MockImageModel(height, width, maxValue, image, logs);
    sourceImageModels.add(mockImageModel);

    String action = "blur";
    StringBuilder expectedImageModel = new StringBuilder(action);

    IImageCommand filter = new Filter(imageName, "koala-grayscale"
            , action);
    filter.executeCommand(sourceImageModels);

    assertEquals(expectedImageModel.toString(), logs.toString());
  }

  private StringBuilder constructExpectedImageModel(IImageModel mockImageModel) {
    StringBuilder expectedImageModel = new StringBuilder();
    expectedImageModel.append(mockImageModel.getHeight()).append(mockImageModel.getWidth())
            .append(mockImageModel.getMaxValue());

    for (IPixel p : mockImageModel.getImage()) {
      expectedImageModel.append(p.getRed());
      expectedImageModel.append(p.getGreen());
      expectedImageModel.append(p.getBlue());
    }

    return expectedImageModel;
  }
}