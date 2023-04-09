package gime.control;

public interface Features {


  void loadImage(String path, String ImgName);

  void save(String SavingPath, String ImgName);

  void rgbSplit(String from);

  void rgbCombine(String r, String g, String b);

  void brighten(int level, String from, String to);

  void greyscale(String mode, String from, String to);

  void vflip(String from);

  void hflip(String from);

  void sepia(String from, String to);

  void blur(String from, String to);

  void sharpen(String from, String to);

  void dither(String from, String to);

}
