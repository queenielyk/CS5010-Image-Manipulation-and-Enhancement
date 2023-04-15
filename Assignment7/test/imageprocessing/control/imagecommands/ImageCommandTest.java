package imageprocessing.control.imagecommands;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import imageprocessing.control.exceptionhandler.InvalidFileException;
import imageprocessing.model.IImageModel;
import imageprocessing.model.IPixel;
import imageprocessing.model.ImageModel;
import imageprocessing.model.Pixel;

import static org.junit.Assert.assertEquals;

/**
 * tests ImageCommand class.
 */
public class ImageCommandTest {
  private IImageCommand imageCommand;

  @Test
  public void testGetSourceImageName() {
    imageCommand = new Brighten("test", "testbrighter", "10");
    assertEquals(new ArrayList<>(Arrays.asList("test")), imageCommand.getSourceImageName());
  }

  @Test
  public void testGetDestinationImageName() {
    imageCommand = new Brighten("test", "testbrighter", "10");
    assertEquals("testbrighter", imageCommand.getDestinationImageName());
  }

  @Test
  public void testExecuteCommand() throws InvalidFileException, IOException {
    List<IPixel> pixels = new ArrayList<>();
    pixels.add(new Pixel(10, 12, 13));
    IImageModel imageModel = new ImageModel(1, 1, 255, pixels);
    List<IImageModel> imageModels = new ArrayList<>();
    imageModels.add(imageModel);
    List<IPixel> resultPixels = new ArrayList<>();
    resultPixels.add(new Pixel(20, 22, 23));
    IImageModel resultImageModel = new ImageModel(1, 1, 255, resultPixels);
    imageCommand = new Brighten("test", "testbrighter", "10");
    assertEquals(true, resultImageModel.equals(imageCommand.executeCommand(imageModels)));
  }
}