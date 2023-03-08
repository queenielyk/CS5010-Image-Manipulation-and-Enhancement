package IME.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.function.Function;

/**
 * A class to implement interface IME.model.ImageProcessor than specialized to ppm format image.
 * A processor may contain more than one images, including the original image and processed images.
 */
public class PpmProcessor implements ImageProcessor {
  private Map<String, ImageComp> images;
  private int height;
  private int width;
  private int maxValue;

  /**
   * A constructor to construct a new IME.model.PpmProcessor.
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
      if (s.charAt(0) != '#') {
        builder.append(s).append(System.lineSeparator());
      }
    }
    return builder.toString();
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
  public void greyscale(String mode, String from, String to) {

    checkImageExistence(from);

    ImageComp fromChain = images.get(from);

    switch (mode) {
      case "red-component":
        greyscaleLooper(fromChain, to, RGB -> new ImageCompImp(RGB[0], RGB[0], RGB[0]));
        break;
      case "green-component":
        greyscaleLooper(fromChain, to, RGB -> new ImageCompImp(RGB[1], RGB[1], RGB[1]));
        break;
      case "blue-component":
        greyscaleLooper(fromChain, to, RGB -> new ImageCompImp(RGB[2], RGB[2], RGB[2]));
        break;
      case "value-component":
        greyscaleLooper(fromChain, to, RGB -> new ImageCompImp(
                Math.max(Math.max(RGB[0], RGB[1]), RGB[2]),
                Math.max(Math.max(RGB[0], RGB[1]), RGB[2]),
                Math.max(Math.max(RGB[0], RGB[1]), RGB[2]))
        );
        break;
      case "intensity-component":
        greyscaleLooper(fromChain, to, RGB -> new ImageCompImp(
                (RGB[0] + RGB[1] + RGB[2]) / 3,
                (RGB[0] + RGB[1] + RGB[2]) / 3,
                (RGB[0] + RGB[1] + RGB[2]) / 3)
        );
        break;
      case "luma-component":
        greyscaleLooper(fromChain, to, RGB -> new ImageCompImp(
                (int) (0.2126 * RGB[0] + 0.7152 * RGB[1] + 0.0722 * RGB[2]),
                (int) (0.2126 * RGB[0] + 0.7152 * RGB[1] + 0.0722 * RGB[2]),
                (int) (0.2126 * RGB[0] + 0.7152 * RGB[1] + 0.0722 * RGB[2]))
        );
        break;
    }
  }

  /**
   * A helper method to loop over the component chain of the target image,
   * meanwhile executing the specified function to each component to attain a new component.
   * The function is passed by invoker that is specified based on the visualization mode.
   *
   * @param fromChain  component chain of the target image
   * @param to         new image's name
   * @param conversion function define how a new component to be constructed
   */
  private void greyscaleLooper(ImageComp fromChain, String to,
                               Function<int[], ImageComp> conversion) {
    ImageComp toChain = null;
    ImageComp prevComp = null;
    while (fromChain != null) {
      ImageComp current = conversion.apply(fromChain.getRGB());
      if (toChain == null) {
        toChain = current;
      }
      if (prevComp != null) {
        prevComp.setNext(current);
      }
      prevComp = current;

      fromChain = fromChain.getNext();
    }
    images.put(to, toChain);
  }

  @Override
  public void horizontalFlip(String from, String to) {

    checkImageExistence(from);

    ImageComp fromChain = images.get(from);
    ImageComp toChain = null;
    Queue<ImageComp> prevRowEnds = new LinkedList<>();
    ImageComp prevCol = null;
    int column = 0;

    while (fromChain != null) {
      int[] RGB = fromChain.getRGB();
      ImageComp current = new ImageCompImp(RGB[0], RGB[1], RGB[2]);
      column += 1;
      if (prevCol != null) {
        current.setNext(prevCol);
      } else {
        prevRowEnds.add(current);
      }
      prevCol = current;

      if (column == this.width) {
        column = 0;
        if (toChain == null) {
          toChain = current;
        }
        prevCol = null;

        if (toChain != current) {
          ImageComp ending = prevRowEnds.poll();
          if (ending != null) {
            ending.setNext(current);
          }
        }
      }

      fromChain = fromChain.getNext();
    }
    images.put(to, toChain);
  }

  @Override
  public void verticalFlip(String from, String to) {

    checkImageExistence(from);

    ImageComp fromChain = images.get(from);
    Queue<ImageComp> prevRowStarts = new LinkedList<>();
    ImageComp prevStart = null;
    int column = 0;

    while (fromChain != null) {
      int[] RGB = fromChain.getRGB();
      ImageComp current = new ImageCompImp(RGB[0], RGB[1], RGB[2]);
      column += 1;
      if (prevStart != null) {
        prevStart.setNext(current);
      } else {
        prevRowStarts.add(current);
      }
      prevStart = current;

      if (column == this.width) {
        column = 0;
        if (prevRowStarts.size() == 2) {
          current.setNext(prevRowStarts.poll());
        }
        prevStart = null;
      }
      fromChain = fromChain.getNext();
    }
    images.put(to, prevRowStarts.poll());
  }

  @Override
  public void adjustBrightness(String from, int add, String to) {

    checkImageExistence(from);

    ImageComp fromChain = images.get(from);
    ImageComp toChain = null;
    ImageComp prev = null;

    while (fromChain != null) {
      int[] RGB = fromChain.getRGB();
      ImageComp current = new ImageCompImp(
              Math.max(0, Math.min(RGB[0] + add, this.maxValue)),
              Math.max(0, Math.min(RGB[1] + add, this.maxValue)),
              Math.max(0, Math.min(RGB[2] + add, this.maxValue))
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

    checkImageExistence(redName);
    checkImageExistence(greenName);
    checkImageExistence(blueName);

    ImageComp redChain = images.get(redName);
    ImageComp greenChain = images.get(greenName);
    ImageComp blueChain = images.get(blueName);
    ImageComp toChain = null;
    ImageComp prev = null;

    while (redChain != null && greenChain != null && blueChain != null) {

      ImageComp current = new ImageCompImp(
              redChain.getRGB()[0], greenChain.getRGB()[1], blueChain.getRGB()[2]);

      if (toChain == null) {
        toChain = current;
      }
      if (prev != null) {
        prev.setNext(current);
      }
      prev = current;

      redChain = redChain.getNext();
      greenChain = greenChain.getNext();
      blueChain = blueChain.getNext();
    }
    images.put(to, toChain);
  }

  @Override
  public void save(String from, String path) throws IOException {

    checkImageExistence(from);

    FileWriter imageWriter = new FileWriter(path);
    imageWriter.write("P3" + System.lineSeparator());
    imageWriter.write(this.width + " " + this.height + System.lineSeparator());
    imageWriter.write(this.maxValue + System.lineSeparator());

    ImageComp temp = images.get(from);
    while (temp != null) {
      int[] RGB = temp.getRGB();
      imageWriter.write(RGB[0] + System.lineSeparator());
      imageWriter.write(RGB[1] + System.lineSeparator());
      imageWriter.write(RGB[2] + System.lineSeparator());
      temp = temp.getNext();
    }
    imageWriter.close();
  }
}
