package imageprocessing.control;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * This interface represents a IGUIController. IGUIController is responsible for
 * communicating with IGUIView and Model.
 */

public interface IGUIController extends IController {
  BufferedImage loadCommand(String imagePath);

  void saveCommand(String imagePath);

  BufferedImage brightenCommand(String increment);

  BufferedImage mosaicCommand(String seed);

  BufferedImage greyscaleCommand(String component);

  BufferedImage splitCommand(String component);

  BufferedImage horizontalFlipCommand();

  BufferedImage verticalFlipCommand();

  BufferedImage combineCommand(String[] selectedImages);

  BufferedImage combineCommand();

  BufferedImage filterCommand(String filterType);

  BufferedImage sepiaCommand();

  BufferedImage ditherCommand();

  List<int[]> getListOfPixels();
}
