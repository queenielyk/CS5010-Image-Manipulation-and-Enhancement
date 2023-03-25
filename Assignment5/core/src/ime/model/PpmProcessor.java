package ime.model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import javax.imageio.ImageIO;

/**
 * A class to implement interface IME.model.ImageProcessor than specialized to ppm format image. A
 * processor may contain more than one images, including the original image and processed images.
 */
public class PpmProcessor implements ImageProcessor {

  private Map<String, int[][][]> images;
  private Map<String, int[]> infos;

  /**
   * A constructor to construct a new IME.model.PpmProcessor.
   */
  public PpmProcessor() {
    images = new HashMap<String, int[][][]>();
    infos = new HashMap<>();
  }

  @Override
  public void loadImage(String path, String name)
          throws FileNotFoundException, IllegalStateException {

    if (!path.endsWith(".ppm")) {
      throw new IllegalStateException("Invalid Image file: Only ppm images are accepted");
    }

    String imageText = readPPM(path);

    Scanner sc = new Scanner(imageText);

    String token;

    token = sc.next();
    if (!token.equals("P3")) {
      throw new IllegalStateException("Invalid PPM file: plain RAW file should begin with P3");
    }
    int width = sc.nextInt();
    int height = sc.nextInt();
    int max = sc.nextInt();
    infos.put(name, new int[]{width, height, max});
    int[][][] imageArray = new int[height][width][];

    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        imageArray[row][col] = new int[]{sc.nextInt(), sc.nextInt(), sc.nextInt()};
      }
    }
    images.put(name, imageArray);

  }

  /**
   * A helper method to read the .ppm image according to the provided path into a String.
   *
   * @param path path of the .ppm image
   * @return a String of image content
   * @throws FileNotFoundException the path and file name is not exist
   */
  private String readPPM(String path) throws FileNotFoundException {
    Scanner sc = new Scanner(new FileInputStream(path));

    StringBuilder builder = new StringBuilder();
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (s.isEmpty()) {
        continue;
      }
      if (s.charAt(0) != '#') {
        builder.append(s).append(System.lineSeparator());
      }
    }
    return builder.toString();
  }


  /**
   * Read Image file (.jpg .png) from path
   * @param path path of the .ppm image
   * @return a String of image content
   * @throws IOException the path and file name is not exist
   */
  public void readImage(String path,String name) throws IOException {
    //Read file from path
    BufferedImage image = ImageIO.read(new FileInputStream(path));

    int width = image.getWidth();
    int height = image.getHeight();
    int[][][] imageArray = new int[height][width][3];

    for(int j=0;j<height;j++) {
      for (int i = 0; i < width; i++) {
        Color c= new Color(image.getRGB(i,j));
        images.values();
        imageArray[i][j][0]=c.getRed();
        imageArray[i][j][1]=c.getGreen();
        imageArray[i][j][2]=c.getBlue();
      }
    }
    images.put(name,imageArray);
    infos.put(name,new int[]{width, height, 255});
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
      case "luma-component":
        greyscaleLooper(from, to, RGB -> new int[]{
                (int) (0.2126 * RGB[0] + 0.7152 * RGB[1] + 0.0722 * RGB[2]),
                (int) (0.2126 * RGB[0] + 0.7152 * RGB[1] + 0.0722 * RGB[2]),
                (int) (0.2126 * RGB[0] + 0.7152 * RGB[1] + 0.0722 * RGB[2])
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
  private void greyscaleLooper(String from, String to, Function<int[], int[]> conversion) {

    int[][][] fromImage = images.get(from);
    int[] info = infos.get(from);
    int[][][] toImage = new int[info[1]][info[0]][];

    for (int row = 0; row < info[1]; row++) {
      for (int col = 0; col < info[0]; col++) {
        toImage[row][col] = conversion.apply(fromImage[row][col]);
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
  public void save(String from, String path) throws IOException {

    checkImageExistence(from);
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
}
