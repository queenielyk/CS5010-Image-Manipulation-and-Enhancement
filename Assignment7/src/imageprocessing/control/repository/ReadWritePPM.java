package imageprocessing.control.repository;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import imageprocessing.control.exceptionhandler.InvalidFileException;
import imageprocessing.model.IImageModel;
import imageprocessing.model.IPixel;
import imageprocessing.model.ImageModel;
import imageprocessing.model.Pixel;

/**
 * This class implements IReadWrite. It is responsible for loading and saving ppm files.
 */
public class ReadWritePPM extends ReadWrite {
  /**
   * Creates an object of readwriteppm, and instantiates its member variables.
   *
   * @param imageName      - name of the image
   * @param filePath       - path of the image file
   * @param fileOperations - operation to be done on the image, i.e. either load or save
   */
  public ReadWritePPM(String imageName, String filePath, String fileOperations) {
    super(fileOperations, filePath, imageName);
  }

  @Override
  public IImageModel readFromFile()
          throws FileNotFoundException, InvalidFileException {
    List<IPixel> image = new ArrayList<>();
    List<String> lines;
    int i = 0;

    try {
      lines = runFile();

      if (lines.size() == 0) {
        throw new InvalidFileException("Invalid PPM file: plain RAW file " +
                "should begin with P3.");
      }
      String token = lines.get(i++);

      if (!token.equals("P3")) {
        throw new InvalidFileException("Invalid PPM file: plain RAW file " +
                "should begin with P3.");
      }

      if (lines.size() % 3 != 0) {
        throw new InvalidFileException("RGB pixel values are not present " +
                "in the right order.");
      }
    }
    catch (FileNotFoundException e) {
      throw new FileNotFoundException(e.getMessage());
    }

    String[] imageWidthHeight = lines.get(i++).split(" ");

    int width = Integer.parseInt(imageWidthHeight[0]);
    int height = Integer.parseInt(imageWidthHeight[1]);
    int maxValue = Integer.parseInt(lines.get(i++));

    int red;
    int green;
    int blue;

    while (i < lines.size()) {
      red = Integer.parseInt(lines.get(i++));
      green = Integer.parseInt(lines.get(i++));
      blue = Integer.parseInt(lines.get(i++));

      image.add(new Pixel(red, green, blue));
    }

    return new ImageModel(height, width, maxValue, image);
  }

  @Override
  public IImageModel saveToFile(IImageModel sourceImageModel)
          throws IOException, InvalidFileException {
    if (!filePath.endsWith(".ppm")) {
      throw new InvalidFileException(filePath +
              " is of invalid file type - file should be ppm type");
    }

    try {
      FileWriter myWriter = new FileWriter(filePath);
      myWriter.write("P3\n");
      myWriter.write("# Created by GIMP version 2.10.20 PNM plug-in\n");
      myWriter.write(sourceImageModel.getWidth() + " " +
              sourceImageModel.getHeight() + "\n");
      myWriter.write("" + sourceImageModel.getMaxValue() + "\n");

      for (IPixel p : sourceImageModel.getImage()) {
        myWriter.write("" + p.getRed() + "\n");
        myWriter.write("" + p.getGreen() + "\n");
        myWriter.write("" + p.getBlue() + "\n");
      }

      myWriter.close();
    }
    catch (IOException e) {
      throw new IOException("An error occurred in writing to the file - " + filePath);
    }

    return sourceImageModel;
  }
}
