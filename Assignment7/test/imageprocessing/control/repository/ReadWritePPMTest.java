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
 * Tests ReadWritePPM Class.
 */
public class ReadWritePPMTest {
  IReadWrite readWritePpm;

  private void initialize(String imageName, String filePath, String fileOperations) {
    this.readWritePpm = new ReadWritePPM(imageName, filePath, fileOperations);
  }

  @Test
  public void readFromFile() throws InvalidFileException, IOException {
    String destinationImageName = "destinationImage";
    String filePath = "testPPMfolder/test-red.ppm";
    String fileOperations = "load";

    initialize(destinationImageName, filePath, fileOperations);

    IImageModel actualImageModel = this.readWritePpm.readFromFile();

    List<IPixel> image = new ArrayList<>();
    image.add(new Pixel(10, 10, 10));

    IImageModel expectedImageModel = new ImageModel(1, 1, 255, image);

    assertEquals(expectedImageModel, actualImageModel);
  }

  @Test
  public void saveToFile() throws InvalidFileException, IOException {
    String sourceImageName = "sourceImage";
    String filePath = "resultPPMfolder/result.ppm";
    String fileOperations = "save";

    initialize(sourceImageName, filePath, fileOperations);

    List<IPixel> image = new ArrayList<>();
    image.add(new Pixel(10, 10, 10));

    IImageModel imageModel = new ImageModel(1, 1, 255, image);

    IImageModel actualImageModel = this.readWritePpm.saveToFile(imageModel);

    fileOperations = "load";
    initialize(sourceImageName, filePath, fileOperations);

    IImageModel expectedImageModel = this.readWritePpm.readFromFile();

    assertEquals(expectedImageModel, actualImageModel);
  }
}