package gime.view;

import gime.control.Features;

/**
 * An interface indicates actions provided by GUI that can be triggered by Controller.
 */
public interface IView {

  /**
   * A method for Controller to provide features method to the View.
   *
   * @param features a Features object
   */
  void addFeatures(Features features);

  /**
   * A method to initiate the GUI to show a specific image according to the name.
   *
   * @param name the name of image
   */
  void showImage(String name);

  /**
   * A method to initiate a dialog to send information to the user.
   * It could be an information dialog or an error dialog.
   *
   * @param type OptionPane.INFORMATION_MESSAGE or JOptionPane.ERROR_MESSAGE
   * @param msg  message to be shown
   */
  void showDialog(int type, String msg);


  /**
   * A method to initiate a dialog to ask which image the user want to see.
   *
   * @param options a list of image names
   */
  void dialogAskImgAfterSplit(String[] options);
}
