package gime.view;

import gime.control.Features;

public interface IView {

  void addFeatures(Features features);

  void showImage(String name);

  void updateNameList(String showname);

  void showErrorDialog(String msg);
}
