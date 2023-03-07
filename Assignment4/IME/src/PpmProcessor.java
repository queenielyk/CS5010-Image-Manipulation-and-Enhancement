import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Map;

/**
 * A class to implement interface ImageProcessor than specialized to ppm format image.
 * A processor may contain more than one images, including the original image and processed images.
 */
public class PpmProcessor implements ImageProcessor {
  private Map<String, ImageComp> images;
  private int height;
  private int width;
  private int maxValue;

  /**
   * A constructor to construct a new PpmProcessor.
   */
  public PpmProcessor() {
    images = new HashMap<>();
  }

  @Override
  public void loadImage(String path, String name) throws FileNotFoundException, IllegalStateException {
    String imageText = readPPM(path);

    Scanner sc = new Scanner(imageText);

    String token;

    token = sc.next();
    if (!token.equals("P3")) {
      throw new IllegalStateException("Invalid PPM file: plain RAW file should begin with P3");
    }
    this.width = sc.nextInt();
    this.height = sc.nextInt();
    this.maxValue = sc.nextInt();

    ImageComp chain = null;
    ImageComp traveler = null;
    for (int row = 0; row < this.height; row++) {
      for (int col = 0; col < this.width; col++) {
        int r = sc.nextInt();
        int g = sc.nextInt();
        int b = sc.nextInt();

        if (row == 0 && col == 0) {
          chain = new ImageCompImp(r, g, b);
          traveler = chain;
        } else {
          ImageComp current = new ImageCompImp(r, g, b);
          traveler.setNext(current);
          traveler = current;
        }
      }
    }
    images.put(name, chain);
  }

  private String readPPM(String path) throws FileNotFoundException {
    Scanner sc = new Scanner(new FileInputStream(path));

    StringBuilder builder = new StringBuilder();
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (s.charAt(0) != '#') {
        builder.append(s).append(System.lineSeparator());
      }
    }
    return builder.toString();
  }

  @Override
  public void greyscale(String mode, String from, String to) {

  }

  @Override
  public void horizontalFlip(String from, String to) {

  }

  @Override
  public void verticalFlip(String from, String to) {

  }

  @Override
  public void adjustBrightness(String from, int add, String to) {

    ImageComp fromChain = images.get(from);
    ImageComp toChain = null;
    ImageComp prev = null;

    while (fromChain != null) {
      int[] RGB = fromChain.getRGB();
      ImageComp current = new ImageCompImp(
              Math.min(RGB[0] + add, this.maxValue),
              Math.min(RGB[1] + add, this.maxValue),
              Math.min(RGB[2] + add, this.maxValue)
      );
      if (toChain == null) {
        toChain = current;
      } else {
        prev.setNext(current);
      }
      prev = current;
      fromChain = fromChain.getNext();
    }
    images.put(to, toChain);
  }

  @Override
  public void combines(String redName, String greenName, String blueName, String to) {

  }

  @Override
  public void save(String from, String path) throws IllegalArgumentException, IOException {

    if (images.get(from) == null) {
      throw new IllegalArgumentException("This image is not exist!");
    }

    StringBuilder builder = new StringBuilder();
    builder.append("P3").append(System.lineSeparator());
    builder.append(this.width).append(" ").append(this.height).append(System.lineSeparator());
    builder.append(this.maxValue).append(System.lineSeparator());

    ImageComp temp = images.get(from);
    while (temp != null) {
      int[] RGB = temp.getRGB();
      builder.append(RGB[0]).append(System.lineSeparator());
      builder.append(RGB[1]).append(System.lineSeparator());
      builder.append(RGB[2]).append(System.lineSeparator());
      temp = temp.getNext();
    }

    FileWriter imageWriter = new FileWriter(path);
    imageWriter.write(builder.toString());
    imageWriter.close();
  }
}
