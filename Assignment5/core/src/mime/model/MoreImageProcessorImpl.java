package mime.model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
   * A protected helper method to clamp a RGB that 0 < RGB < maxi-value.
   *
   * @param rgb  rgb value
   * @param maxi maximum value of an image
   * @return a clamped rgb value
   */
  protected int clampRGB(int rgb, int maxi) {
    return Math.max(0, Math.min(rgb, maxi));
  }


  /**
   * @deprecated This method is no longer be supported because of supporting more image format.
   * Replaced by {@link #loadImage(String path, String name)}
   * Using this method will throw IllegalStateException.
   */
  @Deprecated
  public void loadImage(String path, String name)
          throws FileNotFoundException, IllegalStateException {
    throw new IllegalStateException("This method is deprecated!");
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
   * @throws IllegalStateException image is not exist
   */
  private void checkImageExistence(String name) throws IllegalStateException {
    if (!images.containsKey(name)) {
      throw new IllegalStateException("This image is not exist!");
    }
  }

  @Override
  public void greyscale(String mode, String from, String to) throws IllegalArgumentException {

    checkImageExistence(from);

    switch (mode) {
      case "red-component":
        greyscaleLooper(from, to, RGB -> new int[]{RGB[0], RGB[0], RGB[0]});
        break;
      case "green-component":
        greyscaleLooper(from, to, RGB -> new int[]{RGB[1], RGB[1], RGB[1]});
        break;
      case "blue-component":
        greyscaleLooper(from, to, RGB -> new int[]{RGB[2], RGB[2], RGB[2]});
        break;
      case "value-component":
        greyscaleLooper(from, to, RGB -> new int[]{
                calValueValue(RGB), calValueValue(RGB), calValueValue(RGB)
        });
        break;
      case "intensity-component":
        greyscaleLooper(from, to, RGB -> new int[]{
                calIntensityValue(RGB), calIntensityValue(RGB), calIntensityValue(RGB)
        });
        break;
      case "greyscale":
      case "luma-component":
        greyscaleLooper(from, to, RGB -> new int[]{
                calLumaValue(RGB), calLumaValue(RGB), calLumaValue(RGB),
        });
        break;
      case "sepia":
        greyscaleLooper(from, to, RGB -> new int[]{
                (int) (0.393 * RGB[0] + 0.769 * RGB[1] + 0.189 * RGB[2]),
                (int) (0.349 * RGB[0] + 0.686 * RGB[1] + 0.168 * RGB[2]),
                (int) (0.272 * RGB[0] + 0.534 * RGB[1] + 0.131 * RGB[2])
        });
        break;
      default:
        throw new IllegalArgumentException("This grayscale component is not an option!");
    }
  }

  /**
   * A protected helper method to assist calculating value value.
   *
   * @param RGB int[red, green, blue] of a pixel
   * @return the value value
   */
  protected int calValueValue(int[] RGB) {
    return Math.max(Math.max(RGB[0], RGB[1]), RGB[2]);
  }

  /**
   * A protected helper method to assist calculating intensity value.
   *
   * @param RGB int[red, green, blue] of a pixel
   * @return the intensity value
   */
  protected int calIntensityValue(int[] RGB) {
    return (RGB[0] + RGB[1] + RGB[2]) / 3;
  }

  /**
   * A protected helper method to assist calculating luma value.
   *
   * @param RGB int[red, green, blue] of a pixel
   * @return the luma value
   */
  protected int calLumaValue(int[] RGB) {
    return (int) (0.2126 * RGB[0] + 0.7152 * RGB[1] + 0.0722 * RGB[2]);
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
  protected void greyscaleLooper(String from, String to, Function<int[], int[]> conversion) {

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
   * @deprecated This method is no longer be supported because of supporting more image format.
   * Replaced by {@link #save(String from, OutputStream stream, String format)}
   * Using this method will throw IllegalStateException.
   */
  @Deprecated
  public void save(String from, String path) throws IOException, IllegalStateException {
    throw new IllegalStateException("This operation is not supported!");
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

    this.greyscale("greyscale", from, to);
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
    return images.get(name);
  }

  @Override
  public int[] getInfo(String name) {
    return infos.get(name);
  }


}
