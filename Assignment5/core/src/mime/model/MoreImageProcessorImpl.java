package mime.model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Function;

import javax.imageio.ImageIO;

public class MoreImageProcessorImpl implements MoreImageProcessor {
  private Map<String, int[][][]> images; // <name, int[row, col, [r, g, b]]>
  private Map<String, int[]> infos; // <name, int[width, height, maxi value]>

  protected Set<String> acceptFormat;

  public MoreImageProcessorImpl() {
    this.images = new HashMap<String, int[][][]>();
    this.infos = new HashMap<>();
    this.acceptFormat = new HashSet<>(Arrays.asList("ppm", "jpg", "jpeg", "png", "bmp"));
  }

  @Override
  public boolean verifyFormat(String path) {
    if (this.acceptFormat.contains(path.substring(path.lastIndexOf('.') + 1))) {
      return true;
    }
    return false;
  }

  @Override
  public void loadImage(BufferedImage image, String name) throws IllegalStateException {

    if (image == null) {
      throw new IllegalStateException("Image is not accessible!");
    }

    int width = image.getWidth();
    int height = image.getHeight();

    int[] dataBuffInt = image.getRGB(0, 0, width, height, null, 0, width);
    int[][][] imageArray = new int[height][width][3];
    int row = 0;
    int col = 0;

    for (int rgb : dataBuffInt) {
      int red = (rgb >> 16) & 0xFF;
      int green = (rgb >> 8) & 0xFF;
      int blue = (rgb) & 0xFF;
      imageArray[row][col] = new int[]{red, green, blue};
      col++;
      if (col == width) {
        col = 0;
        row++;
      }
    }

    infos.put(name, new int[]{width, height, 255});
    images.put(name, imageArray);
  }

  @Override
  public void loadImage(String path, String name) throws FileNotFoundException, IllegalStateException {

    if (!path.endsWith(".ppm")) {
      throw new IllegalStateException("This input format is not available");
    }


    Scanner sc = new Scanner(new FileInputStream(path));

    if (!sc.nextLine().equals("P3")) {
      throw new IllegalStateException("Invalid PPM file: plain RAW file should begin with P3");
    }

    String s = sc.nextLine();
    if (s.charAt(0) == '#') {
      s = sc.nextLine();
    }
    //Dimension
    String[] splited = s.split(" ");
    int width = Integer.parseInt(splited[0]);
    int height = Integer.parseInt(splited[1]);
    //Maxi Value
    int maxi = Integer.parseInt(sc.nextLine());


    int[][][] imageArray = new int[height][width][3];
    int row = 0;
    int col = 0;
    int count = 0;

    while (sc.hasNextLine()) {
      s = sc.nextLine();
      if (s.isEmpty()) {
        continue;
      }
      if (s.charAt(0) != '#') {
        splited = s.split(" ");
        for (String ss : splited) {
          if (!ss.equals("")) {
            imageArray[row][col][count] = Integer.parseInt(ss);
            count++;
            if (count == 3) {
              count = 0;
              col++;
              if (col == width) {
                col = 0;
                row++;
              }
            }
          }
        }
      }
    }

    infos.put(name, new int[]{width, height, maxi});
    images.put(name, imageArray);
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
                Math.max(Math.max(RGB[0], RGB[1]), RGB[2]),
                Math.max(Math.max(RGB[0], RGB[1]), RGB[2]),
                Math.max(Math.max(RGB[0], RGB[1]), RGB[2])
        });
        break;
      case "intensity-component":
        greyscaleLooper(from, to, RGB -> new int[]{
                (RGB[0] + RGB[1] + RGB[2]) / 3,
                (RGB[0] + RGB[1] + RGB[2]) / 3,
                (RGB[0] + RGB[1] + RGB[2]) / 3
        });
        break;
      case "greyscale":
      case "luma-component":
        greyscaleLooper(from, to, RGB -> new int[]{
                (int) (0.2126 * RGB[0] + 0.7152 * RGB[1] + 0.0722 * RGB[2]),
                (int) (0.2126 * RGB[0] + 0.7152 * RGB[1] + 0.0722 * RGB[2]),
                (int) (0.2126 * RGB[0] + 0.7152 * RGB[1] + 0.0722 * RGB[2])
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
        toImage[row][col] = new int[]{Math.min(rgb[0], info[2]), Math.min(rgb[1], info[2]), Math.min(rgb[2], info[2])};
      }
    }
    infos.put(to, info);
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

    infos.put(to, info);
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

    infos.put(to, info);
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
                Math.max(0, Math.min(rgb[0] + add, info[2])),
                Math.max(0, Math.min(rgb[1] + add, info[2])),
                Math.max(0, Math.min(rgb[2] + add, info[2]))
        };
      }
    }

    infos.put(to, info);
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

    if (redInfo != greenInfo || greenInfo != blueInfo) {
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

  @Override
  public void save(String from, String path) throws IOException, IllegalStateException {

    checkImageExistence(from);
    if (!verifyFormat(path)) {
      throw new IllegalStateException("This output format is not available");
    }


    if (path.endsWith(".ppm")) {
      savePPM(from, path);
      return;
    }

    saveBJP(from, path);
  }

  private void saveBJP(String from, String path) throws IOException {
    int[] info = infos.get(from);
    int[][][] fromImage = images.get(from);

    BufferedImage image = new BufferedImage(info[0], info[1], BufferedImage.TYPE_INT_RGB);
    int[] rgb;
    for (int row = 0; row < info[1]; row++) {
      for (int col = 0; col < info[0]; col++) {
        rgb = fromImage[row][col];
        Color c = new Color(rgb[0], rgb[1], rgb[2]);
        image.setRGB(col, row, c.getRGB());

      }
    }
    File outputfile = new File(path);
    ImageIO.write(image, path.substring(path.lastIndexOf('.') + 1), outputfile);
  }

  private void savePPM(String from, String path) throws IOException {
    int[] info = infos.get(from);

    FileWriter imageWriter = new FileWriter(path);
    imageWriter.write("P3" + System.lineSeparator());
    imageWriter.write(info[0] + " " + info[1] + System.lineSeparator());
    imageWriter.write(info[2] + System.lineSeparator());

    int[][][] fromImage = images.get(from);
    for (int row = 0; row < info[1]; row++) {
      for (int col = 0; col < info[0]; col++) {
        for (int tmp : fromImage[row][col]) {
          imageWriter.write(tmp + System.lineSeparator());
        }
      }
    }
    imageWriter.close();
  }


  @Override
  public void filter(String mode, String from, String to) {

  }

  @Override
  public void dithering(String from, String to) {

  }
}
