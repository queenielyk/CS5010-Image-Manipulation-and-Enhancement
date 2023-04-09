package gime.control;

import java.io.IOException;

public interface Features {


  void loadImage(String path,String ImgName) throws IOException;

  void save(String SavingPath,String ImgName) throws IOException;

  void rgbSplit(String from,String r,String g,String b) throws IOException;

  void rgbCombine(String to,String r,String g,String b) throws IOException;

  void brighten(int level,String from,String to) throws IOException;

  void greyscale(String mode,String from,String to) throws IOException;

  void vflip(String from,String to) throws IOException;

  void hflip(String from,String to) throws IOException;

  void sepia(String from,String to) throws IOException;

  void blur(String from,String to) throws IOException;

  void sharpen(String from,String to) throws IOException;

  void dither(String from,String to) throws IOException;

}
