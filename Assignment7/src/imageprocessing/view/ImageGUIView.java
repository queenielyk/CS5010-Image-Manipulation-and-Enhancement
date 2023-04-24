package imageprocessing.view;

import imageprocessing.control.IGUIController;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * This class implements IImageGUIView. It is responsible for GUI's view. It contains the components
 * that construct the GUI.
 */
public class ImageGUIView extends JFrame implements IImageGUIView {

  private final JPanel mainPanel;
  private final JButton fileOpenButton;
  private final JLabel fileOpenDisplay;
  private final JButton fileSaveButton;
  private final JPanel histogramPanel;
  private final JLabel fileSaveDisplay;
  private final JSpinner brightenIncrementValue;
  private final JSpinner mosaicIncrementValue;

  private final JButton brighten;
  private final JComboBox greyscaleOptions;
  private final JButton greyscale;
  private final JComboBox splitOptions;
  private final JButton split;
  private final JButton horizontal;
  private final JButton vertical;
  private final JButton fileSelectButton;
  private final JLabel fileSelectDisplay;
  private final JLabel counterDisplay;
  private final String[] selectedFiles;
  private int counter;
  private final JButton combineSplit;
  private final JButton blur;
  private final JButton sharpen;
  private final JButton sepia;
  private final JButton dither;
  private final JButton mosaic;
  private final JLabel imageLabel;

  /**
   * create an object of ImageGUIView.
   *
   * @param message title showed on GUI window.
   */

  public ImageGUIView(String message) {
    super(message);

    setSize(1000, 700);
    setLocation(0, 0);

    //main panel
    mainPanel = new JPanel();
    mainPanel.setPreferredSize(new Dimension(100, 600));
    mainPanel.setLayout(new GridLayout(2, 1));
    JPanel mainPanelTwo = new JPanel();
    mainPanelTwo.setLayout(new GridLayout(1, 2));

    //scroll main panel
    JScrollPane mainScrollPane = new JScrollPane(mainPanel);

    add(mainScrollPane);

    //dialog boxes
    JPanel dialogBoxesPanel = new JPanel();
    dialogBoxesPanel.setBorder(BorderFactory.createTitledBorder("Dialog boxes"));
    dialogBoxesPanel.setLayout(new GridLayout(7, 2));
    mainPanel.add(dialogBoxesPanel, BorderLayout.NORTH);

    //histogram
    histogramPanel = new JPanel();
    histogramPanel.setBorder(BorderFactory.createTitledBorder("Histogram"));
    histogramPanel.setLayout(new GridLayout(1, 0, 10, 10));
    mainPanelTwo.add(histogramPanel);

    //file open
    JPanel fileOpenPanel = new JPanel();
    fileOpenPanel.setLayout(new FlowLayout());
    dialogBoxesPanel.add(fileOpenPanel);

    fileOpenButton = new JButton("Open a file");
    fileOpenButton.setActionCommand("Open file");
    fileOpenPanel.add(fileOpenButton);

    fileOpenDisplay = new JLabel("File path");
    fileOpenPanel.add(fileOpenDisplay);

    //file save
    JPanel fileSavePanel = new JPanel();
    fileSavePanel.setLayout(new FlowLayout());
    dialogBoxesPanel.add(fileSavePanel);

    fileSaveButton = new JButton("Save a file");
    fileSaveButton.setActionCommand("Save file");
    fileSaveButton.setEnabled(false);
    fileSavePanel.add(fileSaveButton);

    fileSaveDisplay = new JLabel("File path");
    fileSavePanel.add(fileSaveDisplay);

    //split
    JPanel splitPanel = new JPanel();
    splitPanel.setLayout(new FlowLayout());
    dialogBoxesPanel.add(splitPanel);

    String[] rgbSplit = {"Red", "Green", "Blue"};
    splitOptions = new JComboBox(rgbSplit);
    splitOptions.setEnabled(false);
    splitPanel.add(splitOptions);

    split = new JButton("Split");
    split.setActionCommand("Split");
    split.setEnabled(false);
    splitPanel.add(split);

    JLabel splitDisplay = new JLabel("Please select which split component to load");
    splitPanel.add(splitDisplay);

    //brighten
    JPanel brightenPanel = new JPanel();
    brightenPanel.setLayout(new FlowLayout());
    dialogBoxesPanel.add(brightenPanel);

    brightenIncrementValue = new JSpinner();
    brightenIncrementValue.setEnabled(false);
    brightenPanel.add(brightenIncrementValue);
    brightenIncrementValue.setPreferredSize(new Dimension(50, 25));

    brighten = new JButton("Brighten");
    brighten.setActionCommand("Brighten");
    brighten.setEnabled(false);
    brightenPanel.add(brighten);

    //mosaic
    JPanel mosaicPanel = new JPanel();
    mosaicPanel.setLayout(new FlowLayout());
    dialogBoxesPanel.add(mosaicPanel);

    mosaicIncrementValue = new JSpinner(new SpinnerNumberModel(500, 500, 1000, 100));
    mosaicIncrementValue.setEnabled(false);
    mosaicPanel.add(mosaicIncrementValue);
    mosaicIncrementValue.setPreferredSize(new Dimension(50, 25));

    mosaic = new JButton("Mosaic");
    mosaic.setActionCommand("Mosaic");
    mosaic.setEnabled(false);
    mosaicPanel.add(mosaic);

    //greyscale
    JPanel greyscalePanel = new JPanel();
    greyscalePanel.setLayout(new FlowLayout());
    dialogBoxesPanel.add(greyscalePanel);

    String[] components = {" ", "Red", "Green", "Blue", "Value", "Luma", "Intensity"};
    greyscaleOptions = new JComboBox(components);
    greyscaleOptions.setEnabled(false);
    greyscalePanel.add(greyscaleOptions);

    greyscale = new JButton("Greyscale");
    greyscale.setActionCommand("Greyscale");
    greyscale.setEnabled(false);
    greyscalePanel.add(greyscale);

    JLabel greyscaleDisplay = new JLabel("Default is luma");
    greyscalePanel.add(greyscaleDisplay);

    //horizontal flip
    JPanel horizontalPanel = new JPanel();
    horizontalPanel.setLayout(new FlowLayout());
    dialogBoxesPanel.add(horizontalPanel);

    horizontal = new JButton("Horizontal Flip");
    horizontal.setActionCommand("Horizontal Flip");
    horizontal.setEnabled(false);
    horizontalPanel.add(horizontal);

    //file select
    JPanel fileSelectPanel = new JPanel();
    fileSelectPanel.setLayout(new FlowLayout());
    dialogBoxesPanel.add(fileSelectPanel);

    fileSelectButton = new JButton("Select a file");
    fileSelectButton.setActionCommand("Select file");
    fileSelectPanel.add(fileSelectButton);

    counterDisplay = new JLabel("Number of files selected");
    fileSelectPanel.add(counterDisplay);

    fileSelectDisplay = new JLabel("File path");
    fileSelectPanel.add(fileSelectDisplay);

    selectedFiles = new String[3];
    counter = 0;

    //vertical flip
    JPanel verticalPanel = new JPanel();
    verticalPanel.setLayout(new FlowLayout());
    dialogBoxesPanel.add(verticalPanel);

    vertical = new JButton("Vertical Flip");
    vertical.setActionCommand("Vertical Flip");
    vertical.setEnabled(false);
    verticalPanel.add(vertical);

    //combineSplit
    JPanel combineSplitPanel = new JPanel();
    combineSplitPanel.setLayout(new FlowLayout());
    dialogBoxesPanel.add(combineSplitPanel);

    combineSplit = new JButton("Combine");
    combineSplit.setActionCommand("Combine");
    combineSplit.setEnabled(false);
    combineSplitPanel.add(combineSplit);

    //blur
    JPanel blurPanel = new JPanel();
    blurPanel.setLayout(new FlowLayout());
    dialogBoxesPanel.add(blurPanel);

    blur = new JButton("Blur");
    blur.setActionCommand("Blur");
    blur.setEnabled(false);
    blurPanel.add(blur);

    //sharpen
    JPanel sharpenPanel = new JPanel();
    sharpenPanel.setLayout(new FlowLayout());
    dialogBoxesPanel.add(sharpenPanel);

    sharpen = new JButton("Sharpen");
    sharpen.setActionCommand("Sharpen");
    sharpen.setEnabled(false);
    sharpenPanel.add(sharpen);

    //sepia
    JPanel sepiaPanel = new JPanel();
    sepiaPanel.setLayout(new FlowLayout());
    dialogBoxesPanel.add(sepiaPanel);

    sepia = new JButton("Sepia");
    sepia.setActionCommand("Sepia");
    sepia.setEnabled(false);
    sepiaPanel.add(sepia);

    //dither
    JPanel ditherPanel = new JPanel();
    ditherPanel.setLayout(new FlowLayout());
    dialogBoxesPanel.add(ditherPanel);

    dither = new JButton("Dither");
    dither.setActionCommand("Dither");
    dither.setEnabled(false);
    ditherPanel.add(dither);

    //image operations
    JPanel imagePanel = new JPanel();
    imagePanel.setBorder(BorderFactory.createTitledBorder("Image"));
    imagePanel.setLayout(new GridLayout(1, 0, 10, 10));
    mainPanelTwo.add(imagePanel);
    mainPanel.add(mainPanelTwo);

    imageLabel = new JLabel();
    JScrollPane imageScrollPane = new JScrollPane(imageLabel);
    imageScrollPane.setPreferredSize(new Dimension(100, 600));
    imagePanel.add(imageScrollPane);

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }

  @Override
  public void showUserInputMessage() {
    //GUI doesn't need to show an input message.
  }

  @Override
  public void showOutput(String message) {
    JOptionPane.showMessageDialog(mainPanel, message);
  }

  @Override
  public void addFeatures(IGUIController controller) {
    fileOpenButton.addActionListener(e -> {
      final JFileChooser fChooser = new JFileChooser(".");
      FileNameExtensionFilter filter = new FileNameExtensionFilter(
          "PPM, JPG, PNG, BMP Images", "ppm", "jpg", "png", "bmp");
      fChooser.setFileFilter(filter);
      File f = null;
      BufferedImage image = null;

      if (fChooser.showOpenDialog(fileOpenButton) == JFileChooser.APPROVE_OPTION) {
        f = fChooser.getSelectedFile();
        image = controller.loadCommand(f.getAbsolutePath());
      }

      if (image != null) {
        Path filepath = Paths.get(f.getAbsolutePath());
        fileOpenDisplay.setText(filepath.getFileName().toString());
        enableButtons();
        imageLabel.setIcon(new ImageIcon(image));

        changeHistogram(controller);
      }
    });

    brightenIncrementValue.addChangeListener(e ->
        brighten.setEnabled((int) (brightenIncrementValue.getValue()) != 0));

    brighten.addActionListener(e -> {
      String increment = brightenIncrementValue.getValue().toString();

      BufferedImage image = controller.brightenCommand(increment);
      if (image != null) {
        imageLabel.setIcon(new ImageIcon(image));

        changeHistogram(controller);
      }
    });

    //----------------------------------Mosaic---------------------------
    mosaic.addActionListener(e -> {
      try {
        // Display loading image
        imageLabel.setIcon(new ImageIcon(new URL(
            "https://www.khoury.northeastern.edu/home/sc971008/cs5010/loading.gif")));
      } catch (MalformedURLException ex) {
        throw new RuntimeException(ex);
      }
      new Thread(() -> { // Create a new thread to execute mosaicCommand()
        String seed = mosaicIncrementValue.getValue().toString();
        ImageIcon mosaicImage = new ImageIcon(
            controller.mosaicCommand(seed)); // Call mosaicCommand()
        SwingUtilities.invokeLater(() -> { // Display the mosaic image in the main thread
          imageLabel.setIcon(mosaicImage);
          changeHistogram(controller);
        });
      }).start();
      changeHistogram(controller);
    });
    //----------------------------------Mosaic---------------------------

    greyscale.addActionListener(e -> {
      String component = greyscaleOptions.getSelectedItem().toString();

      BufferedImage image = controller.greyscaleCommand(component);
      if (image != null) {
        imageLabel.setIcon(new ImageIcon(image));

        changeHistogram(controller);
      }
    });

    split.addActionListener(e -> {
      String component = splitOptions.getSelectedItem().toString();

      BufferedImage image = controller.splitCommand(component);
      if (image != null) {
        imageLabel.setIcon(new ImageIcon(image));
        combineSplit.setEnabled(true);

        changeHistogram(controller);
      }
    });

    horizontal.addActionListener(e -> {
      BufferedImage image = controller.horizontalFlipCommand();
      if (image != null) {
        imageLabel.setIcon(new ImageIcon(image));

        changeHistogram(controller);
      }
    });

    vertical.addActionListener(e -> {
      BufferedImage image = controller.verticalFlipCommand();
      if (image != null) {
        imageLabel.setIcon(new ImageIcon(image));

        changeHistogram(controller);
      }
    });

    fileSelectButton.addActionListener(e -> {
      if (counter == 3) {
        counter = 0;
        combineSplit.setEnabled(false);
      }

      final JFileChooser fChooser = new JFileChooser(".");
      FileNameExtensionFilter filter = new FileNameExtensionFilter(
          "PPM, JPG, PNG, BMP Images", "ppm", "jpg", "png", "bmp");
      fChooser.setFileFilter(filter);

      if (fChooser.showOpenDialog(fileSelectButton) == JFileChooser.APPROVE_OPTION) {
        File f = fChooser.getSelectedFile();
        selectedFiles[counter++] = f.getAbsolutePath();

        counterDisplay.setText(Integer.toString(counter));
        Path filepath = Paths.get(f.getAbsolutePath());
        fileSelectDisplay.setText(filepath.getFileName().toString());
      }

      if (counter == 3) {
        combineSplit.setEnabled(true);
      }
    });

    combineSplit.addActionListener(e -> {
      BufferedImage image;
      if (counter == 3) {
        image = controller.combineCommand(selectedFiles);
      } else {
        image = controller.combineCommand();
      }

      if (image != null) {
        imageLabel.setIcon(new ImageIcon(image));
        enableButtons();
        changeHistogram(controller);
      }
    });

    blur.addActionListener(e -> {
      BufferedImage image = controller.filterCommand("blur");
      if (image != null) {
        imageLabel.setIcon(new ImageIcon(image));

        changeHistogram(controller);
      }
    });

    sharpen.addActionListener(e -> {
      BufferedImage image = controller.filterCommand("sharpen");
      if (image != null) {
        imageLabel.setIcon(new ImageIcon(image));

        changeHistogram(controller);
      }
    });

    sepia.addActionListener(e -> {
      BufferedImage image = controller.sepiaCommand();
      if (image != null) {
        imageLabel.setIcon(new ImageIcon(image));

        changeHistogram(controller);
      }
    });

    dither.addActionListener(e -> {
      BufferedImage image = controller.ditherCommand();
      if (image != null) {
        imageLabel.setIcon(new ImageIcon(image));

        changeHistogram(controller);
      }
    });

    fileSaveButton.addActionListener(e -> {
      final JFileChooser fChooser = new JFileChooser(".");

      if (fChooser.showSaveDialog(fileSaveButton) == JFileChooser.APPROVE_OPTION) {
        File f = fChooser.getSelectedFile();
        Path filepath = Paths.get(f.getAbsolutePath());
        fileSaveDisplay.setText(filepath.getFileName().toString());

        controller.saveCommand(f.getAbsolutePath());
      }
    });
  }

  private void changeHistogram(IGUIController controller) {
    LineChart linechart = new LineChart(controller.getListOfPixels());

    histogramPanel.removeAll();
    histogramPanel.add(linechart);
    histogramPanel.revalidate();
    histogramPanel.repaint();
  }

  private void enableButtons() {
    greyscale.setEnabled(true);
    horizontal.setEnabled(true);
    vertical.setEnabled(true);
    sepia.setEnabled(true);
    dither.setEnabled(true);
    blur.setEnabled(true);
    sharpen.setEnabled(true);
    split.setEnabled(true);
    greyscaleOptions.setEnabled(true);
    splitOptions.setEnabled(true);
    fileSaveButton.setEnabled(true);
    brightenIncrementValue.setEnabled(true);
    mosaicIncrementValue.setEnabled(true);
    mosaic.setEnabled(true);
  }

}
