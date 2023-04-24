package imageprocessing.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


/**
 * This class represents an 8 bit image. It implements IImageModel. An image has a List of Pixel,
 * height, width and maximum pixel value.
 */
public class ImageModel implements IImageModel {

  private final int height;
  private final int width;
  private final int maxValue;
  private final List<IPixel> image;

  /**
   * Creates an object of ImageModel.
   */
  public ImageModel(int height, int width, int maxValue, List<IPixel> image) {
    this.height = height;
    this.width = width;
    this.maxValue = maxValue;
    this.image = image;
  }


  @Override
  public IImageModel brightenImage(int increment) {
    int red;
    int green;
    int blue;
    List<IPixel> image = new ArrayList<>();

    for (IPixel p : this.image) {
      red = checkIfColorInRange(p.getRed() + increment);
      green = checkIfColorInRange(p.getGreen() + increment);
      blue = checkIfColorInRange(p.getBlue() + increment);

      image.add(new Pixel(red, green, blue));
    }

    return new ImageModel(this.height, this.width, this.maxValue, image);
  }

  private List<List<Integer>> getClusterList(int seed) {
    List<List<Integer>> seedClusterList = new ArrayList<>();
    //get random seed pixels
    List<Integer> rand = new ArrayList<>();
    Random generator = new Random(seed);
    for (int i = 0; i < seed; i++) {
      int randomNum = generator.nextInt(width * height);
      rand.add(randomNum);
    }
    Collections.sort(rand);
    for (Integer integer : rand) {
      //put index of seed at position zero
      List<Integer> cluster = new ArrayList<>();
      cluster.add(integer);
      seedClusterList.add(cluster);
    }
    return seedClusterList;
  }

  private int HelpIndexOf(List<IPixel> list, IPixel p) {
    if (p != null) {
      for (int i = 0; i < list.size(); i++) {
        if (p == list.get(i)) {
          return i;
        }
      }
    }
    return -1;
  }

  private void MatchImgPixelWithCluster(List<List<Integer>> seedClusterList) {
    //pair pixel with seedPix
    //Go through each pixel of image
    for (IPixel p : image) {
      int px = HelpIndexOf(image, p) % width;
      int py = HelpIndexOf(image, p) / height;
      List<Integer> match = null;
      double curDis;
      double MinDis = Double.MAX_VALUE;

      //Go through all cluster
      for (List<Integer> cluster : seedClusterList) {
        int sx = cluster.get(0) % width;
        int sy = cluster.get(0) / height;
        curDis = Math.hypot(Math.abs(sy - py), Math.abs(sx - px));
        //Update if Shorter Distance
        if (curDis < MinDis) {
          MinDis = curDis;
          match = cluster;
        }
      }
      //add p to the closest cluster
      List<Integer> temp = match;
      match.add(HelpIndexOf(image, p));
      seedClusterList.set(seedClusterList.indexOf(match), temp);
    }
  }

  @Override
  public IImageModel mosaic(int seed) {
    List<List<Integer>> seedClusterList = getClusterList(seed);
    MatchImgPixelWithCluster(seedClusterList);

    //Avg all pixel in cluster
    IPixel[] resultArr = new IPixel[height * width];
    for (List<Integer> cluster : seedClusterList) {
      int R = 0;
      int G = 0;
      int B = 0;
      for (int i = 1; i < cluster.size(); i++) {
        R += image.get(cluster.get(i)).getRed();
        G += image.get(cluster.get(i)).getGreen();
        B += image.get(cluster.get(i)).getBlue();
      }
      R /= cluster.size();
      G /= cluster.size();
      B /= cluster.size();
      for (int i : cluster) {
        resultArr[i] = new Pixel(R, G, B);
      }
    }

    //get result img
    List<IPixel> resultImg = new ArrayList<>(Arrays.asList(resultArr));

    return new ImageModel(height, width, maxValue, resultImg);
  }

  @Override
  public IImageModel greyscale(String component) throws IllegalArgumentException {
    int individualChannel;
    List<IPixel> image = new ArrayList<>();

    for (IPixel p : this.image) {
      switch (component) {
        case "red":
          individualChannel = p.getRed();
          break;
        case "green":
          individualChannel = p.getGreen();
          break;
        case "blue":
          individualChannel = p.getBlue();
          break;
        case "value":
          individualChannel = Math.max(p.getRed(), Math.max(p.getGreen(), p.getBlue()));
          break;
        case "luma":
          return colorTransformations("luma");
        case "intensity":
          individualChannel = (p.getRed() + p.getGreen() + p.getBlue()) / 3;
          break;
        default:
          throw new IllegalArgumentException("Invalid Component must be either "
              + "red/green/blue/value/luma/intensity.");
      }

      image.add(new Pixel(individualChannel, individualChannel, individualChannel));
    }

    return new ImageModel(this.height, this.width, this.maxValue, image);
  }

  @Override
  public IImageModel horizontalFlip() {
    List<IPixel> image = new ArrayList<>();

    for (int i = 0; i < height; i++) {
      for (int j = width - 1; j >= 0; j--) {
        image.add(this.image.get((i * width) + j));
      }
    }

    return new ImageModel(this.height, this.width, this.maxValue, image);
  }

  @Override
  public IImageModel verticalFlip() {
    List<IPixel> image = new ArrayList<>();

    for (int i = height - 1; i >= 0; i--) {
      for (int j = 0; j < width; j++) {
        image.add(this.image.get((i * width) + j));
      }
    }

    return new ImageModel(this.height, this.width, this.maxValue, image);
  }

  @Override
  public IImageModel combineRGB(IImageModel sourceGreenModel, IImageModel sourceBlueModel)
      throws IllegalArgumentException {
    int red;
    int green;
    int blue;
    List<IPixel> image = new ArrayList<>();
    List<IPixel> sourceRedImage = this.image;
    List<IPixel> sourceGreenImage = sourceGreenModel.getImage();
    List<IPixel> sourceBlueImage = sourceBlueModel.getImage();

    if (checkEqualityOf3ImagesProperties(this.height, sourceGreenModel.getHeight()
        , sourceBlueModel.getHeight())) {
      throw new IllegalArgumentException("The Images provided do not have the same Height.");
    }
    if (checkEqualityOf3ImagesProperties(this.width, sourceGreenModel.getWidth()
        , sourceBlueModel.getWidth())) {
      throw new IllegalArgumentException("The Images provided do not have the same Width.");
    }

    for (int i = 0; i < sourceRedImage.size(); ++i) {
      red = sourceRedImage.get(i).getRed();
      green = sourceGreenImage.get(i).getGreen();
      blue = sourceBlueImage.get(i).getBlue();

      image.add(new Pixel(red, green, blue));
    }

    return new ImageModel(this.height, this.width, this.maxValue, image);
  }

  @Override
  public IImageModel filter(String action) {
    double[][] respectiveKernel;

    if (action.equalsIgnoreCase("blur")) {
      respectiveKernel = ImageFilter.Blur.filterValues;
    } else {
      respectiveKernel = ImageFilter.Sharpen.filterValues;
    }

    List<IPixel> image = new ArrayList<>();

    for (int i = 0; i < this.height; i++) {
      for (int j = 0; j < this.width; j++) {
        image.add(filterOnAPixel(respectiveKernel, i, j));
      }
    }

    return new ImageModel(this.height, this.width, this.maxValue, image);
  }

  @Override
  public IImageModel colorTransformations(String typeOfTransformation) {
    double[][] matrix = new double[3][3];
    if (typeOfTransformation.equalsIgnoreCase("sepia")) {
      matrix = ImageColorTransformation.Sepia.transformationMatrix;
    } else if (typeOfTransformation.equalsIgnoreCase("luma")) {
      matrix = ImageColorTransformation.GreyscaleLuma.transformationMatrix;
    }

    List<IPixel> image = new ArrayList<>();
    int red = 0;
    int green = 0;
    int blue = 0;

    for (IPixel pixel : this.image) {
      for (int i = 0; i < 3; i++) {
        if (i == 0) {
          red = (int) (matrix[i][0] * pixel.getRed() + matrix[i][1] * pixel.getGreen()
              + matrix[i][2] * pixel.getBlue());
        } else if (i == 1) {
          green = (int) (matrix[i][0] * pixel.getRed() + matrix[i][1] * pixel.getGreen()
              + matrix[i][2] * pixel.getBlue());
        } else {
          blue = (int) (matrix[i][0] * pixel.getRed() + matrix[i][1] * pixel.getGreen()
              + matrix[i][2] * pixel.getBlue());
        }
      }

      red = checkIfColorInRange(red);
      green = checkIfColorInRange(green);
      blue = checkIfColorInRange(blue);

      image.add(new Pixel(red, green, blue));
    }

    return new ImageModel(this.height, this.width, this.maxValue, image);
  }

  @Override
  public IImageModel dither() {
    List<IPixel> image = new ArrayList<>();
    int oldPixel;
    int newPixel;
    int error;
    int red;
    int green;
    int blue;

    for (int i = 0; i < this.height; ++i) {
      for (int j = 0; j < this.width; ++j) {
        oldPixel = this.image.get(i * width + j).getRed();
        newPixel = oldPixel < 255 / 2 ? 0 : 255;
        error = oldPixel - newPixel;

        image.add(new Pixel(newPixel, newPixel, newPixel));

        if (j + 1 < this.width) {
          red = (int) (getRespectivePixelColorValue(i, j + 1, "red")
              + 7d / 16d * error);
          green = (int) (getRespectivePixelColorValue(i, j + 1, "green")
              + 7d / 16d * error);
          blue = (int) (getRespectivePixelColorValue(i, j + 1, "blue")
              + 7d / 16d * error);

          this.image.set(i * width + j + 1, new Pixel(red, green, blue));
        }

        if (j - 1 >= 0 && i + 1 < this.height) {
          red = (int) (getRespectivePixelColorValue(i + 1, j - 1, "red")
              + 3d / 16d * error);
          green = (int) (getRespectivePixelColorValue(i + 1, j - 1, "green")
              + 3d / 16d * error);
          blue = (int) (getRespectivePixelColorValue(i + 1, j - 1, "blue")
              + 3d / 16d * error);

          this.image.set((i + 1) * width + j - 1, new Pixel(red, green, blue));
        }

        if (i + 1 < this.height) {
          red = (int) (getRespectivePixelColorValue(i + 1, j, "red")
              + 5d / 16d * error);
          green = (int) (getRespectivePixelColorValue(i + 1, j, "green")
              + 5d / 16d * error);
          blue = (int) (getRespectivePixelColorValue(i + 1, j, "blue")
              + 5d / 16d * error);

          this.image.set((i + 1) * width + j, new Pixel(red, green, blue));
        }

        if (i + 1 < this.height && j + 1 < this.width) {
          red = (int) (getRespectivePixelColorValue(i + 1, j + 1, "red")
              + 1d / 16d * error);
          green = (int) (getRespectivePixelColorValue(i + 1, j + 1, "green")
              + 1d / 16d * error);
          blue = (int) (getRespectivePixelColorValue(i + 1, j + 1, "blue")
              + 1d / 16d * error);

          this.image.set((i + 1) * width + j + 1, new Pixel(red, green, blue));
        }
      }
    }

    return new ImageModel(this.height, this.width, this.maxValue, image);
  }

  private IPixel filterOnAPixel(double[][] filter, int pixelRow, int pixelCol) {
    int midRow = filter.length / 2;
    int midCol = filter[0].length / 2;
    int diffRow = pixelRow - midRow;
    int diffCol = pixelCol - midCol;
    int red = 0;
    int green = 0;
    int blue = 0;

    for (int i = 0; i < filter.length; ++i) {
      for (int j = 0; j < filter[0].length; ++j) {
        int relativeRow = diffRow + i;
        int relativeCol = diffCol + j;
        red += (int) (filter[i][j] * getRespectivePixelColorValue(relativeRow, relativeCol
            , "red"));
        green += (int) (filter[i][j] * getRespectivePixelColorValue(relativeRow, relativeCol
            , "green"));
        blue += (int) (filter[i][j] * getRespectivePixelColorValue(relativeRow, relativeCol
            , "blue"));
      }
    }

    red = checkIfColorInRange(red);
    green = checkIfColorInRange(green);
    blue = checkIfColorInRange(blue);

    return new Pixel(red, green, blue);
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
    return new ArrayList<>(this.image);
  }

  private int checkIfColorInRange(int color) {
    if (color < 0) {
      color = 0;
    }
    if (color > 255) {
      color = 255;
    }

    return color;
  }

  private boolean checkEqualityOf3ImagesProperties(int image1, int image2, int image3) {
    return (image1 != image2) || (image2 != image3);
  }

  private int getRespectivePixelColorValue(int i, int j, String color) {
    if (i < 0 || i >= this.height) {
      return 0;
    }
    if (j < 0 || j >= this.width) {
      return 0;
    }

    IPixel pixel = this.image.get(i * this.width + j);

    if (color.equals("red")) {
      return pixel.getRed();
    } else if (color.equals("green")) {
      return pixel.getGreen();
    }
    return pixel.getBlue();
  }

  /**
   * Overriding equals method such that 2 image objects are equal if they have the same width,
   * height and the list of pixels are the same.
   *
   * @param o - an instance of IImageModel type
   * @return - true if 2 images are same, else false
   */
  @Override
  public boolean equals(Object o) {
    if (o instanceof IImageModel) {
      IImageModel imageModel = (IImageModel) o;

      if (this.getHeight() != imageModel.getHeight() || this.getWidth() != imageModel.getWidth()) {
        return false;
      }

      for (int i = 0; i < this.image.size(); ++i) {
        if (!this.getImage().get(i).equals(imageModel.getImage().get(i))) {
          return false;
        }
      }
    } else {
      return false;
    }

    return true;
  }

  /**
   * Overriding hashcode such that 2 images with the same width, height, and list of pixels have the
   * same hashvalue.
   *
   * @return - hashcode of the image object
   */
  @Override
  public int hashCode() {
    StringBuilder hash = new StringBuilder();

    for (IPixel p : this.getImage()) {
      hash.append(p.hashCode());
    }

    return Double.hashCode(hash.hashCode());
  }
}
