package imageprocessing.view;

import imageprocessing.control.IGUIController;

/**
 * This interface represents IImageGUIView. It is responsible for GUI's view.
 */
public interface IImageGUIView extends IImageView {
  /**
   * Add features to the various components in the GUI.
   *
   * @param controller - instance of GUIController
   */
  void addFeatures(IGUIController controller);
}
