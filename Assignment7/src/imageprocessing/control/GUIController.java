package imageprocessing.control;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import imageprocessing.control.exceptionhandler.ImageNotFoundException;
import imageprocessing.control.exceptionhandler.InvalidFileException;
import imageprocessing.control.exceptionhandler.InvalidInputException;
import imageprocessing.model.IImageModel;
import imageprocessing.model.IPixel;
import imageprocessing.view.IImageGUIView;

/**
 * This class implements IGUIController. The program execution for
 * creating an instance of GUI begins here.
 */

public class GUIController implements IGUIController {
  private final IImageGUIView guiView;
  private final LogicController logicController;
  private final String imageName;
  private final String redImageName;
  private final String greenImageName;
  private final String blueImageName;
  private final String combineRed;
  private final String combineGreen;
  private final String combineBlue;
  private String currentImage;

  /**
   * creates an instance of GUIController. It has guiView.
   *
   * @param guiView an instance og IImageGUIView.
   */

  public GUIController(IImageGUIView guiView) {
    this.guiView = guiView;
    this.logicController = new LogicController();
    this.imageName = "imageName";
    this.redImageName = "redImageName";
    this.greenImageName = "greenImageName";
    this.blueImageName = "blueImageName";
    this.combineRed = "combineRed";
    this.combineGreen = "combineGreen";
    this.combineBlue = "combineBlue";
  }

  @Override
  public void goController() {
    this.guiView.addFeatures(this);
  }

  @Override
  public BufferedImage loadCommand(String imagePath) {
    //load sample.ppm koala
    this.currentImage = this.imageName;

    String load = "load" + " " + imagePath + " " + this.currentImage;
    return addCommand(load, this.currentImage);
  }

  @Override
  public void saveCommand(String imagePath) {
    //save sample.ppm koala
    String save = "save" + " " + imagePath + " " + this.currentImage;
    addCommand(save, this.currentImage);
  }

  @Override
  public BufferedImage brightenCommand(String increment) {
    //brighten 10 koala koala-brighter
    String brighten = "brighten" + " " + increment + " " + this.currentImage + " "
            + this.currentImage;
    return addCommand(brighten, this.currentImage);
  }

  @Override
  public BufferedImage mosaicCommand(String seed){
    String mosaic = "mosaic" + " " + seed + " " + this.currentImage + " " + this.currentImage;
    return addCommand(mosaic,this.currentImage);
  }

  @Override
  public BufferedImage greyscaleCommand(String component) {
    //greyscale value-component koala koala-greyscale
    //greyscale koala koala-greyscale
    String greyscale = component.equals(" ") ? "greyscale" + " " + this.currentImage + " "
            + this.currentImage : "greyscale" + " " + component + "-component" + " "
            + this.currentImage + " " + this.currentImage;
    return addCommand(greyscale, this.currentImage);
  }

  @Override
  public BufferedImage splitCommand(String component) {
    //rgb-split koala koala-red koala-green koala-blue
    String split = "rgb-split" + " " + this.currentImage + " " + this.redImageName + " "
            + this.greenImageName + " " + this.blueImageName;

    if (component.equalsIgnoreCase("red")) {
      this.currentImage = this.redImageName;
    } else if (component.equalsIgnoreCase("green")) {
      this.currentImage = this.greenImageName;
    } else {
      this.currentImage = this.blueImageName;
    }

    return addCommand(split, this.currentImage);
  }

  @Override
  public BufferedImage horizontalFlipCommand() {
    //horizontal-flip koala koala-horizontal
    String horizontalFlip = "horizontal-flip" + " " + this.currentImage + " " + this.currentImage;
    return addCommand(horizontalFlip, this.currentImage);
  }

  @Override
  public BufferedImage verticalFlipCommand() {
    //vertical-flip koala koala-vertical
    String verticalFlip = "vertical-flip" + " " + this.currentImage + " " + this.currentImage;
    return addCommand(verticalFlip, this.currentImage);
  }

  @Override
  public BufferedImage combineCommand(String[] selectedImages) {
    //rgb-combine koala-red-tint koala-red koala-green koala-blue
    this.currentImage = this.imageName;

    String load1 = "load" + " " + selectedImages[0] + " " + this.combineRed;
    String load2 = "load" + " " + selectedImages[1] + " " + this.combineGreen;
    String load3 = "load" + " " + selectedImages[2] + " " + this.combineBlue;
    String combine = "rgb-combine" + " " + this.currentImage + " " + this.combineRed + " "
            + this.combineGreen + " " + this.combineBlue;
    addCommand(load1, this.combineRed);
    addCommand(load2, this.combineGreen);
    addCommand(load3, this.combineBlue);

    return addCommand(combine, this.currentImage);
  }

  @Override
  public BufferedImage combineCommand() {
    //rgb-combine koala-red-tint koala-red koala-green koala-blue
    this.currentImage = this.imageName;
    String combine = "rgb-combine" + " " + this.currentImage + " " + this.redImageName + " "
            + this.greenImageName + " " + this.blueImageName;
    return addCommand(combine, this.currentImage);
  }

  @Override
  public BufferedImage filterCommand(String filterType) {
    //blur koala koala-blurred
    //sharpen koala koala-sharpened
    String filter = filterType.equalsIgnoreCase("blur") ? "blur" + " "
            + this.currentImage + " " + this.currentImage : "sharpen" + " " + this.currentImage
            + " " + this.currentImage;
    return addCommand(filter, this.currentImage);
  }

  @Override
  public BufferedImage sepiaCommand() {
    //sepia koala koala-sepia
    String sepia = "sepia" + " " + this.currentImage + " " + this.currentImage;
    return addCommand(sepia, this.currentImage);
  }

  @Override
  public BufferedImage ditherCommand() {
    //dither koala koala-dither
    String dither = "dither" + " " + this.currentImage + " " + this.currentImage;
    return addCommand(dither, this.currentImage);
  }

  @Override
  public List<int[]> getListOfPixels() {
    List<IPixel> pixels = logicController.getImageModel(this.currentImage).getImage();
    List<int[]> listOfColors = new ArrayList<>();

    int[] redList = new int[256];
    int[] greenList = new int[256];
    int[] blueList = new int[256];
    int[] intensityList = new int[256];

    for (IPixel p : pixels) {
      redList[p.getRed()]++;
      greenList[p.getGreen()]++;
      blueList[p.getBlue()]++;
      intensityList[(p.getGreen() + p.getRed() + p.getBlue()) / 3]++;
    }

    listOfColors.add(redList);
    listOfColors.add(greenList);
    listOfColors.add(blueList);
    listOfColors.add(intensityList);

    return listOfColors;
  }

  private BufferedImage addCommand(String command, String imageName) {
    try {
      List<List<IUserCommands>> userCommands = new ArrayList<>();
      userCommands.add(logicController.parseCommand(command));
      logicController.executeImageCommands(userCommands);

      return imagePreview(logicController.getImageModel(imageName));
    } catch (InvalidFileException | InvalidInputException | ImageNotFoundException |
             IllegalArgumentException | IOException e) {
      guiView.showOutput(e.getMessage());
    }

    return null;
  }

  private BufferedImage imagePreview(IImageModel sourceImageModel) {
    int height = sourceImageModel.getHeight();
    int width = sourceImageModel.getWidth();
    int red;
    int green;
    int blue;
    List<IPixel> tempImage = sourceImageModel.getImage();
    BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

    for (int i = 0; i < height; ++i) {
      for (int j = 0; j < width; ++j) {
        red = tempImage.get(i * width + j).getRed();
        green = tempImage.get(i * width + j).getGreen();
        blue = tempImage.get(i * width + j).getBlue();

        Color pixelColor = new Color(red, green, blue);

        img.setRGB(j, i, pixelColor.getRGB());
      }
    }
    return img;
  }


}
