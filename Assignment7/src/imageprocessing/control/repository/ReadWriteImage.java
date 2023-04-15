package imageprocessing.control.repository;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import imageprocessing.control.exceptionhandler.InvalidFileException;
import imageprocessing.model.IImageModel;
import imageprocessing.model.IPixel;
import imageprocessing.model.ImageModel;
import imageprocessing.model.Pixel;

/**
 * This class implements IReadWrite. It is responsible for loading and saving png,
 * bmp and jpg files.
 */
public class ReadWriteImage extends ReadWrite {
  private BufferedImage img;

  /**
   * Creates an object of readwriteimage, and instantiates its member variables.
   *
   * @param imageName      - name of the image
   * @param filePath       - path of the image file
   * @param fileOperations - operation to be done on the image, i.e. either load or save
   */
  public ReadWriteImage(String imageName, String filePath, String fileOperations) {
    super(fileOperations, filePath, imageName);
  }

  @Override
  public IImageModel readFromFile() throws IOException, InvalidFileException {
    int height;
    int width;
    int maxValue;
    int max;
    int red;
    int green;
    int blue;
    List<IPixel> image = new ArrayList<>();

    try {
      String formatName = filePath.substring(filePath.lastIndexOf('.') + 1);
      if (!(formatName.equalsIgnoreCase("ppm") ||
              formatName.equalsIgnoreCase("jpg") ||
              formatName.equalsIgnoreCase("bmp") ||
              formatName.equalsIgnoreCase("png"))) {
        throw new InvalidFileException("Invalid type of file only ppm, png, jpg and bmp allowed.");
      }

      img = ImageIO.read(new File(filePath));

      height = img.getHeight();
      width = img.getWidth();
      maxValue = 0;

      for (int i = 0; i < height; ++i) {
        for (int j = 0; j < width; ++j) {
          Color c = new Color(img.getRGB(j, i));
          red = c.getRed();
          green = c.getGreen();
          blue = c.getBlue();

          max = Math.max(red, Math.max(green, blue));

          if (maxValue < max) {
            maxValue = max;
          }

          image.add(new Pixel(red, green, blue));
        }
      }
    }
    catch (IOException e) {
      throw new IOException("Error occurred while reading file - " + filePath);
    } catch (InvalidFileException e) {
      throw new InvalidFileException(e.getMessage());
    }

    return new ImageModel(height, width, maxValue, image);
  }

  @Override
  public IImageModel saveToFile(IImageModel sourceImageModel)
          throws IOException, InvalidFileException {
    int height = sourceImageModel.getHeight();
    int width = sourceImageModel.getWidth();
    int red;
    int green;
    int blue;
    List<IPixel> tempImage = sourceImageModel.getImage();
    try {
      String formatName = filePath.substring(filePath.lastIndexOf('.') + 1);
      if (!(formatName.equalsIgnoreCase("ppm") ||
              formatName.equalsIgnoreCase("jpg") ||
              formatName.equalsIgnoreCase("bmp") ||
              formatName.equalsIgnoreCase("png"))) {
        throw new InvalidFileException("Invalid type of file only ppm, png, jpg and bmp allowed.");
      }

      img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

      for (int i = 0; i < height; ++i) {
        for (int j = 0; j < width; ++j) {
          red = tempImage.get(i * width + j).getRed();
          green = tempImage.get(i * width + j).getGreen();
          blue = tempImage.get(i * width + j).getBlue();

          Color pixelColor = new Color(red, green, blue);

          img.setRGB(j, i, pixelColor.getRGB());
        }
      }

      ImageIO.write(img, formatName, new File(filePath));
    }
    catch (IOException e) {
      throw new IOException("Error occurred while saving file - " + filePath);
    } catch (InvalidFileException e) {
      throw new InvalidFileException(e.getMessage());
    }

    return sourceImageModel;
  }
}
