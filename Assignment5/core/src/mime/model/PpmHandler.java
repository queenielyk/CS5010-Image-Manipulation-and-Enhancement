package mime.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

/**
 * A class to be an image handler only handler ascii ppm image.
 */
public class PpmHandler extends AbsrtuctImageHandler {

  /**
   * A constructor to construct a PpmHandler.
   */
  public PpmHandler() {
    super();
  }

  @Override
  public void readImage(InputStream stream) {
    Scanner sc = new Scanner(stream);

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
    this.info = new int[]{width, height, maxi};

    image = new int[height][width][3];
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
            image[row][col][count] = Integer.parseInt(ss);
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
  }

  @Override
  public void saveImage(OutputStream stream, String format, int[] info, int[][][] image) throws IOException {

    stream.write(("P3" + System.lineSeparator()).getBytes());
    stream.write((info[0] + " " + info[1] + System.lineSeparator()).getBytes());
    stream.write((info[2] + System.lineSeparator()).getBytes());

    for (int row = 0; row < info[1]; row++) {
      for (int col = 0; col < info[0]; col++) {
        for (int tmp : image[row][col]) {
          stream.write(String.format("%s", tmp).getBytes());
          stream.write(System.lineSeparator().getBytes());
        }
      }
    }
    stream.close();
  }


}
