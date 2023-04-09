package gime.view;

import gime.control.Features;

public interface IView {

  void addFeatures(Features features);

  void showImage(String name);

  void showDialog(int type, String msg);

  void dialogAskImgAfterSplit(String[] options);
}
