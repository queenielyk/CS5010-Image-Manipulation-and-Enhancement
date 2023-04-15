package imageprocessing.control.repository;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import imageprocessing.control.exceptionhandler.InvalidFileException;
import imageprocessing.model.IImageModel;
import imageprocessing.model.IPixel;
import imageprocessing.model.ImageModel;
import imageprocessing.model.Pixel;

import static org.junit.Assert.assertEquals;

/**
 * Tests ReadWriteImage Class.
 */
public class ReadWriteImageTest {
  IReadWrite readWriteImage;

  private void initialize(String imageName, String filePath, String fileOperations) {
    this.readWriteImage = new ReadWriteImage(imageName, filePath, fileOperations);
  }

  @Test
  public void readFromFile() throws InvalidFileException, IOException {
    String destinationImageName = "destinationImage";
    String filePath = "testPNGfolder/test.png";
    String fileOperations = "load";

    initialize(destinationImageName, filePath, fileOperations);

    IImageModel actualImageModel = this.readWriteImage.readFromFile();

    List<IPixel> image = new ArrayList<>();
    image.add(new Pixel(10, 12, 13));

    IImageModel expectedImageModel = new ImageModel(1, 1, 13, image);

    assertEquals(expectedImageModel, actualImageModel);
  }

  @Test
  public void saveToFile() throws InvalidFileException, IOException {
    String sourceImageName = "sourceImage";
    String filePath = "testPNGfolder/test123.png";
    String fileOperations = "save";

    initialize(sourceImageName, filePath, fileOperations);

    List<IPixel> image = new ArrayList<>();
    image.add(new Pixel(10, 12, 13));

    IImageModel imageModel = new ImageModel(1, 1, 13, image);

    IImageModel actualImageModel = this.readWriteImage.saveToFile(imageModel);

    fileOperations = "load";
    initialize(sourceImageName, filePath, fileOperations);

    IImageModel expectedImageModel = this.readWriteImage.readFromFile();

    assertEquals(expectedImageModel, actualImageModel);
  }
}