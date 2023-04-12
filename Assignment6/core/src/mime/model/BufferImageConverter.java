package mime.model;

import java.awt.image.BufferedImage;

public interface BufferImageConverter {

  BufferedImage convertToBufferImg(int[] info, int[][][] image);
}
