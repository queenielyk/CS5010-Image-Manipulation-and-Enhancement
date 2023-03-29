package mime;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import mime.model.ImageHandler;
import mime.model.ImageIOHandler;
import mime.model.MoreImageProcessor;
import mime.model.MoreImageProcessorImpl;
import org.junit.Test;

/**
 * This is a test class to generate other formats of source file cat.ppm.
 */
public class FormatConvertTest {

  /**
   * Compares two images pixel by pixel.
   *
   * @param imgA the first image.
   * @param imgB the second image.
   * @return whether the images are both the same or not.
   */
  private static boolean compareImage(ImageHandler imgA, ImageHandler imgB) {

    // The images must be the same size and maxi-value.
    int[] infoA = imgA.getInfo();
    int[] infoB = imgB.getInfo();
    for (int i = 0; i < infoA.length; i++) {
      if (infoA[i] != infoB[i]) {
        return false;
      }
    }

    int width = infoA[0];
    int height = infoA[1];

    int[][][] imgAContent = imgA.getImage();
    int[][][] imgBContent = imgB.getImage();

    // Loop over every pixel.
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        for (int rgb = 0; rgb < 3; rgb++) {
          // Compare the pixels for equality.
          if (imgAContent[x][y][rgb] != imgBContent[x][y][rgb]) {
            System.out.println("A:" + imgAContent[x][y][rgb] + "\tB:" + imgBContent[x][y][rgb]);
            return false;
          }
        }
      }
    }

    return true;
  }

  @Test
  public void Test() throws IOException {
    String[] type = {"jpeg", "jpg", "png", "bmp"};

    for (int j = 0; j < type.length; j++) {
      for (int i = 0; i < type.length; i++) {
        MoreImageProcessor model = new MoreImageProcessorImpl();
        InputStream stream = new FileInputStream("res/format/cat." + type[i]);
        ImageHandler imageH = new ImageIOHandler();
        imageH.readImage(stream);
//        BufferedImage img = ImageIO.read(new FileInputStream("res/format/cat." + type[i]));
        OutputStream outputStream = new FileOutputStream("res/new/cat-" + type[i] + "." + type[j]);
        model.loadImage(imageH, "cat");
        model.save("cat", outputStream, type[j]);

        InputStream newStream = new FileInputStream("res/new/cat-" + type[i] + "." + type[j]);
        ImageHandler newImageH = new ImageIOHandler();
        newImageH.readImage(newStream);
//        BufferedImage NewImg = ImageIO.read(
//                new FileInputStream("res/new/cat-" + type[i] + "." + type[j]));
        System.out.println(
            "In:\t" + type[i] + " \tOut:\t" + type[j] + "\t-----\t" + compareImage(imageH,
                newImageH));

//        assertTrue(compareBufferImages(img, NewImg));
      }
    }
  }


}
