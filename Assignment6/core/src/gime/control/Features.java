package gime.control;

public interface Features {


  void loadImage(String path, String ImgName);

  void save(String SavingPath, String ImgName);

  void rgbSplit(String from);

  void rgbCombine(String r, String g, String b);

  void brighten(int level, String from);

  void vflip(String from);

  void hflip(String from);

  void commandDispatcher(String command, String from) throws IllegalArgumentException;

}
