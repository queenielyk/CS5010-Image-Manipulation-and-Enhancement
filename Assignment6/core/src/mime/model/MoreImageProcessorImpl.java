package mime.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Function;

/**
 * A class to implement interface MoreImageProcessor.
 */
public class MoreImageProcessorImpl implements MoreImageProcessor {

  private final Map<String, int[][][]> images; // <name, int[row, col, [r, g, b]]>
  private final Map<String, int[]> infos; // <name, int[width, height, maxi value]>
  protected Set<String> acceptFormat;
  protected Map<String, float[][]> filterings; // <name, 2d-array filter matrix>

  /**
   * A constructor to construct a MoreImageProcessor.
   */
  public MoreImageProcessorImpl() {
    this.images = new HashMap<String, int[][][]>();
    this.infos = new HashMap<>();
    this.acceptFormat = new HashSet<>(Arrays.asList("ppm", "jpg", "jpeg", "png", "bmp"));
    this.filterings = new HashMap<>();
    this.filterings.put("blur", new float[][]{
            {(float) 1 / 16, (float) 1 / 8, (float) 1 / 16},
            {(float) 1 / 8, (float) 1 / 4, (float) 1 / 8},
            {(float) 1 / 16, (float) 1 / 8, (float) 1 / 16}
    });
    this.filterings.put("sharpen", new float[][]{
            {(float) -1 / 8, (float) -1 / 8, (float) -1 / 8, (float) -1 / 8, (float) -1 / 8},
            {(float) -1 / 8, (float) 1 / 4, (float) 1 / 4, (float) 1 / 4, (float) -1 / 8},
            {(float) -1 / 8, (float) 1 / 4, (float) 1, (float) 1 / 4, (float) -1 / 8},
            {(float) -1 / 8, (float) 1 / 4, (float) 1 / 4, (float) 1 / 4, (float) -1 / 8},
            {(float) -1 / 8, (float) -1 / 8, (float) -1 / 8, (float) -1 / 8, (float) -1 / 8}
    });
  }

  /**
   * A protected helper method to clamp a RGB that 0 &lt; RGB &lt; maxi-value.
   *
   * @param rgb  rgb value
   * @param maxi maximum value of an image
   * @return a clamped rgb value
   */
  protected final int clampRGB(int rgb, int maxi) {
    return Math.max(0, Math.min(rgb, maxi));
  }


  /**
   * This method is deprecated.
   * Replaced by {@link #loadImage(int[] info, int[][][] image, String name)}
   *
   * @throws UnsupportedOperationException when calling this method
   * @deprecated This method is no longer be supported because of supporting more image format.
   */
  @Deprecated
  public void loadImage(String path, String name)
          throws FileNotFoundException {
    throw new UnsupportedOperationException("This method is deprecated!");
  }

  @Override
  public void loadImage(int[] info, int[][][] image, String name) {
    infos.put(name, info);
    images.put(name, image);
  }


  /**
   * A helper method to check existence of  the to be processed image.
   *
   * @param name image to be processed
   * @throws NoSuchElementException image is not exist
   */
  protected final void checkImageExistence(String name) throws NoSuchElementException {
    if (!images.containsKey(name)) {
      throw new NoSuchElementException("Image: " + name + " is not exist!");
    }
  }

  @Override
  public void colorTrans(String mode, String from, String to) throws IllegalArgumentException {

    checkImageExistence(from);

    switch (mode) {
      case "red-component":
      case "green-component":
      case "blue-component":
      case "intensity-component":
      case "sepia":
        colortransLooper(from, to, rgb -> applyColorTransMatrix(mode, rgb));
        break;
      case "value-component":
        colortransLooper(from, to, this::calValueValue);
        break;
      case "greyscale":
      case "luma-component":
        colortransLooper(from, to, rgb -> applyColorTransMatrix("luma-component", rgb));
        break;
      default:
        throw new IllegalArgumentException(mode + " is not supported!");
    }
  }

  /**
   * A protected helper method to apply the desired ColorTrans matrix on the current int[] rgb.
   *
   * @param matrixName the name of applied ColorTrans matrix.
   * @param rgb        the current rgb in form of int[]
   * @return the new rgb value in form of int[]
   */
  protected final int[] applyColorTransMatrix(String matrixName, int[] rgb) {
    int[] newRGB = new int[3];
    float[][] matrix = ColorTransMatrix.valueOf(matrixName.split("-")[0].toUpperCase()).getFloats();

    for (int row = 0; row < 3; row++) {
      float sum = 0;
      for (int col = 0; col < 3; col++) {
        sum += rgb[col] * matrix[row][col];
      }
      newRGB[row] = (int) sum;
    }
    return newRGB;
  }

  /**
   * A protected helper method to assist calculating value of value-component,
   * which is the maximum value of rgb.
   * Then return a int[max, max, max] as a new rgb value.
   *
   * @param rgb int[red, green, blue] of a pixel
   * @return int[max, max, max] where max is the maximum value of the original array
   */
  protected final int[] calValueValue(int[] rgb) {
    int max = Math.max(Math.max(rgb[0], rgb[1]), rgb[2]);
    return new int[]{max, max, max};
  }


  /**
   * A helper method to loop over the component chain of the target image, meanwhile executing the
   * specified function to each component to attain a new component. The function is passed by
   * invoker that is specified based on the visualization mode.
   *
   * @param from       name of image to be converted
   * @param to         new image's name
   * @param conversion function define how a new component to be constructed
   */
  protected final void colortransLooper(String from, String to, Function<int[], int[]> conversion) {

    int[][][] fromImage = images.get(from);
    int[] info = infos.get(from);
    int[][][] toImage = new int[info[1]][info[0]][];

    for (int row = 0; row < info[1]; row++) {
      for (int col = 0; col < info[0]; col++) {
        int[] rgb = conversion.apply(fromImage[row][col]);
        toImage[row][col] = new int[]{
                clampRGB(rgb[0], info[2]), clampRGB(rgb[1], info[2]), clampRGB(rgb[2], info[2])};
      }
    }
    infos.put(to, info.clone());
    images.put(to, toImage);
  }

  @Override
  public void horizontalFlip(String from, String to) {

    checkImageExistence(from);

    int[][][] fromImage = images.get(from);
    int[] info = infos.get(from);
    int[][][] toImage = new int[info[1]][info[0]][];

    for (int row = 0; row < info[1]; row++) {
      for (int col = 0; col < info[0]; col++) {
        toImage[row][col] = fromImage[row][info[0] - 1 - col];
      }
    }

    infos.put(to, info.clone());
    images.put(to, toImage);
  }


  @Override
  public void verticalFlip(String from, String to) {

    checkImageExistence(from);

    int[][][] fromImage = images.get(from);
    int[] info = infos.get(from);
    int[][][] toImage = new int[info[1]][info[0]][];

    for (int row = 0; row < info[1]; row++) {
      toImage[row] = fromImage[info[1] - 1 - row];
    }

    infos.put(to, info.clone());
    images.put(to, toImage);
  }

  @Override
  public void adjustBrightness(String from, int add, String to) {

    checkImageExistence(from);

    int[][][] fromImage = images.get(from);
    int[] info = infos.get(from);
    int[][][] toImage = new int[info[1]][info[0]][];

    for (int row = 0; row < info[1]; row++) {
      for (int col = 0; col < info[0]; col++) {
        int[] rgb = fromImage[row][col];
        toImage[row][col] = new int[]{
                clampRGB(rgb[0] + add, info[2]),
                clampRGB(rgb[1] + add, info[2]),
                clampRGB(rgb[2] + add, info[2])
        };
      }
    }

    infos.put(to, info.clone());
    images.put(to, toImage);
  }


  @Override
  public void combines(String redName, String greenName, String blueName, String to)
          throws IllegalStateException {

    checkImageExistence(redName);
    checkImageExistence(greenName);
    checkImageExistence(blueName);

    int[] redInfo = infos.get(redName);
    int[] greenInfo = infos.get(greenName);
    int[] blueInfo = infos.get(blueName);

    if (!Arrays.equals(redInfo, greenInfo) || !Arrays.equals(greenInfo, blueInfo)) {
      throw new IllegalStateException("These images have different info, can't combine them!");
    }

    int[][][] redImage = images.get(redName);
    int[][][] greenImage = images.get(greenName);
    int[][][] blueImage = images.get(blueName);
    int[][][] toImage = new int[redInfo[1]][redInfo[0]][];

    for (int row = 0; row < redInfo[1]; row++) {
      for (int col = 0; col < redInfo[0]; col++) {
        toImage[row][col] = new int[]{
                redImage[row][col][0], greenImage[row][col][1], blueImage[row][col][2]
        };
      }
    }

    infos.put(to, redInfo);
    images.put(to, toImage);
  }

  /**
   * This method is deprecated.
   * Saving the image is Controller duty now.
   *
   * @throws UnsupportedOperationException when calling this method
   * @deprecated This method is no longer be supported because of supporting more image format.
   */
  @Deprecated
  public void save(String from, String path) throws IOException, UnsupportedOperationException {
    throw new UnsupportedOperationException("This operation is not supported!");
  }

  @Override
  public void filter(String mode, String from, String to) throws IllegalStateException {
    checkImageExistence(from);

    if (!filterings.containsKey(mode)) {
      throw new IllegalStateException("This filter is not supported!");
    }

    float[][] filterMatrix = filterings.get(mode);
    int[] info = infos.get(from);
    int[][][] fromImage = images.get(from);
    int[][][] toImage = new int[info[1]][info[0]][];
    int halfmatrix = (filterMatrix.length - 1) / 2;

    for (int row = 0; row < info[1]; row++) {
      for (int col = 0; col < info[0]; col++) {
        double red = 0;
        double green = 0;
        double blue = 0;

        for (int fRow = Math.abs(Math.min(row - halfmatrix, 0));
             fRow < ((row + halfmatrix) >= info[1] ? info[1] - row + halfmatrix
                     : filterMatrix.length); fRow++) {
          for (int fCol = Math.abs(Math.min(col - halfmatrix, 0));
               fCol < ((col + halfmatrix) >= info[0] ? info[0] - col + halfmatrix
                       : filterMatrix.length); fCol++) {
            int[] rgb = fromImage[row - halfmatrix + fRow][col - halfmatrix + fCol];
            red += rgb[0] * filterMatrix[fRow][fCol];
            green += rgb[1] * filterMatrix[fRow][fCol];
            blue += rgb[2] * filterMatrix[fRow][fCol];
          }
        }

        toImage[row][col] = new int[]{
                clampRGB((int) red, info[2]),
                clampRGB((int) green, info[2]),
                clampRGB((int) blue, info[2]),
        };
      }
    }
    infos.put(to, info.clone());
    images.put(to, toImage);
  }

  @Override
  public void dithering(String from, String to) {
    checkImageExistence(from);

    this.colorTrans("greyscale", from, to);
    int[] info = infos.get(to);
    int[][][] fromImage = images.get(to);

    int[][][] toImage = new int[info[1]][info[0]][];
    for (int row = 0; row < info[1]; row++) {
      for (int col = 0; col < info[0]; col++) {
        toImage[row][col] = fromImage[row][col].clone();
      }
    }

    int oldColor;
    int newColor;
    int error;
    for (int row = 0; row < info[1]; row++) {
      for (int col = 0; col < info[0]; col++) {
        for (int rgb = 0; rgb < 3; rgb++) {
          oldColor = toImage[row][col][rgb];
          newColor = Math.round((float) oldColor / info[2]) * info[2];
          toImage[row][col][rgb] = newColor;

          error = oldColor - newColor;

          // [row+1][col+1]
          if (row + 1 < info[1] && col + 1 < info[0]) {
            toImage[row + 1][col + 1][rgb] += (int) ((float) 1 / 16 * error);
          }

          // [row+1][col-1]
          if (row + 1 < info[1] && col - 1 >= 0) {
            toImage[row + 1][col - 1][rgb] += (int) ((float) 3 / 16 * error);
          }

          // [row+1][col]
          if (row + 1 < info[1]) {
            toImage[row + 1][col][rgb] += (int) ((float) 5 / 16 * error);
          }

          // [row][col+1]
          if (col + 1 < info[0]) {
            toImage[row][col + 1][rgb] += (int) ((float) 7 / 16 * error);
          }
        }
      }
    }

    infos.put(to, info.clone());
    images.put(to, toImage);
  }


  @Override
  public int[][][] getImage(String name) {
    checkImageExistence(name);
    return images.get(name);
  }

  @Override
  public int[] getInfo(String name) {
    checkImageExistence(name);
    return infos.get(name);
  }

  @Override
  public String[] getNameList() {
    return images.keySet().toArray(new String[0]);
  }

}
