package mime;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import mime.model.MoreImageProcessor;
import mime.model.MoreImageProcessorImpl;
import org.junit.Test;

public class FormatConvertTest {

  /**
   * Compares two images pixel by pixel.
   *
   * @param imgA the first image.
   * @param imgB the second image.
   * @return whether the images are both the same or not.
   */
  private static boolean compareBufferImages(BufferedImage imgA, BufferedImage imgB) {
    // The images must be the same size.
    if (imgA.getWidth() != imgB.getWidth() || imgA.getHeight() != imgB.getHeight()) {
      return false;
    }

    int width = imgA.getWidth();
    int height = imgA.getHeight();

    // Loop over every pixel.
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {

        // Compare the pixels for equality.
        if (imgA.getRGB(x, y) != imgB.getRGB(x, y)) {
          System.out.println("A:" + imgA.getRGB(x, y) + "\tB:" + imgB.getRGB(x, y));
          return false;
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
        BufferedImage img = ImageIO.read(new FileInputStream("res/format/cat." + type[i]));
        model.loadImage(img, "cat");
        model.save("cat", "res/new/cat-" + type[i] + "." + type[j]);
        BufferedImage NewImg = ImageIO.read(
            new FileInputStream("res/new/cat-" + type[i] + "." + type[j]));
        System.out.println(
            "In:\t" + type[i] + " \tOut:\t" + type[j] + "\t-----\t" + compareBufferImages(img,
                NewImg));

//        assertTrue(compareBufferImages(img, NewImg));
      }
    }
  }


}
