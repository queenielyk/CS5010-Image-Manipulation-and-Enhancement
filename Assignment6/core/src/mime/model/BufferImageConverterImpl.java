package mime.model;

import java.awt.image.BufferedImage;
import java.awt.Color;

public class BufferImageConverterImpl implements BufferImageConverter {
  @Override
  public BufferedImage convertToBufferImg(int[] info, int[][][] image) {
    BufferedImage buffImage = new BufferedImage(info[0], info[1], BufferedImage.TYPE_INT_RGB);
    int[] rgb;
    for (int row = 0; row < info[1]; row++) {
      for (int col = 0; col < info[0]; col++) {
        rgb = image[row][col];
        Color c = new Color(rgb[0], rgb[1], rgb[2]);
        buffImage.setRGB(col, row, c.getRGB());
      }
    }
    return buffImage;
  }
}
