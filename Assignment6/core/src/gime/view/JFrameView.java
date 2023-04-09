package gime.view;

import java.awt.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import gime.control.Features;
import gime.model.ReadOnlyImageProcessor;
import gime.model.ReadOnlyImageProcessorImpl;


import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class JFrameView extends JFrame implements IView {

  private final ReadOnlyImageProcessor processor;
  private Map<String, String> commandsMap = new LinkedHashMap<>();
  private JPanel topbar;
  private JPanel namep;
  private JPanel btnp;
  private JPanel histogramp;

  private JScrollPane scrollImage;
  private JLabel sImageLabel;

  private JButton addBtn;
  private JButton saveBtn;
  private JButton horizontalBtn;
  private JButton verticalBtn;

  private JComboBox<String> commandDD;
  private JComboBox<String> imagenameDD;
  private JComboBox<String> imagenameEffectDD;

  private Map<String, ArrayList<String>> processedImgNames;

  private void initCommandsMap() {
    commandsMap.put("Color Transform - Red Component", "colorTrans red-component");
    commandsMap.put("Color Transform - Green Component", "colorTrans green-component");
    commandsMap.put("Color Transform - Blue Component", "colorTrans blue-component");
    commandsMap.put("Color Transform - Value", "colorTrans value-component");
    commandsMap.put("Color Transform - Intensity", "colorTrans intensity-component");
    commandsMap.put("Color Transform - Luma", "colorTrans luma-component");
    commandsMap.put("Color Transform - Greyscale", "colorTrans greyscale");
    commandsMap.put("Color Transform - Sepia", "colorTrans sepia");
    commandsMap.put("Brighten", "brighten");
    commandsMap.put("RGB combines", "rgb-combine");
    commandsMap.put("Blur", "blur");
    commandsMap.put("Sharpen", "sharpen");
    commandsMap.put("Dither", "dither");
  }

  public JFrameView(String caption, ReadOnlyImageProcessor processor) {
    super(caption);
    this.processor = processor;
    initCommandsMap();

    setPreferredSize(new Dimension(1750, 900));
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pack();

    this.setLayout(new BorderLayout());

    topbar = new JPanel();
    topbar.setLayout(new BoxLayout(topbar, BoxLayout.X_AXIS));


    commandDD = new JComboBox<>() {
      protected void fireActionEvent() {
        if (this.hasFocus()) {
          super.fireActionEvent();
        }
      }
    };
    commandDD.addItem("Raw");
    commandDD.setSelectedItem("Raw");
    for (String key : commandsMap.keySet()) {
      commandDD.addItem(key);
    }
    topbar.add(commandDD);

    namep = new JPanel();
    namep.setLayout(new BoxLayout(namep, BoxLayout.X_AXIS));
    namep.add(new Box.Filler(new Dimension(20, 25), new Dimension(20, 25), new Dimension(20, 25)));

    imagenameDD = new JComboBox<>() {
      protected void fireActionEvent() {
        if (this.hasFocus()) {
          super.fireActionEvent();
        }
      }
    };
    namep.add(imagenameDD);

    imagenameEffectDD = new JComboBox<>() {
      protected void fireActionEvent() {
        if (this.hasFocus()) {
          super.fireActionEvent();
        }
      }
    };
    namep.add(imagenameEffectDD);

    namep.add(new Box.Filler(new Dimension(20, 25), new Dimension(20, 25), new Dimension(20, 25)));
    topbar.add(namep);

    btnp = new JPanel();
    btnp.setLayout(new BoxLayout(btnp, BoxLayout.X_AXIS));
    btnp.add(Box.createHorizontalGlue());


    horizontalBtn = new JButton();
    try {
      Image img = ImageIO.read(getClass().getResource("resources/horizontal-flip.png"));
      horizontalBtn.setIcon(new ImageIcon(img.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH)));
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }
    horizontalBtn.setOpaque(false);
    horizontalBtn.setFocusPainted(false);
    horizontalBtn.setBorderPainted(false);
    horizontalBtn.setContentAreaFilled(false);
    horizontalBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Especially important
    horizontalBtn.setActionCommand("horizontal-flip");
    btnp.add(horizontalBtn);

    Dimension boxFiller = new Dimension(20, 30);
    btnp.add(new Box.Filler(boxFiller, boxFiller, boxFiller));

    verticalBtn = new JButton();
    try {
      Image img = ImageIO.read(getClass().getResource("resources/vertical-flip.png"));
      verticalBtn.setIcon(new ImageIcon(img.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH)));
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }
    verticalBtn.setOpaque(false);
    verticalBtn.setFocusPainted(false);
    verticalBtn.setBorderPainted(false);
    verticalBtn.setContentAreaFilled(false);
    verticalBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Especially important
    verticalBtn.setActionCommand("vertical-flip");
    btnp.add(verticalBtn);

    btnp.add(new Box.Filler(boxFiller, boxFiller, boxFiller));

    addBtn = new JButton();
    try {
      Image img = ImageIO.read(getClass().getResource("resources/add-new-50.png"));
      addBtn.setIcon(new ImageIcon(img.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH)));
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }
    addBtn.setOpaque(false);
    addBtn.setFocusPainted(false);
    addBtn.setBorderPainted(false);
    addBtn.setContentAreaFilled(false);
    addBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Especially important
    btnp.add(addBtn);

    btnp.add(new Box.Filler(boxFiller, boxFiller, boxFiller));

    saveBtn = new JButton();
    try {
      Image img = ImageIO.read(getClass().getResource("resources/save-50.png"));
      saveBtn.setIcon(new ImageIcon(img.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH)));
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }
    saveBtn.setOpaque(false);
    saveBtn.setFocusPainted(false);
    saveBtn.setBorderPainted(false);
    saveBtn.setContentAreaFilled(false);
    saveBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Especially important
    btnp.add(saveBtn);

    btnp.add(new Box.Filler(boxFiller, boxFiller, boxFiller));
    topbar.add(btnp);

    topbar.setPreferredSize(new Dimension(this.getWidth(), 75));

    sImageLabel = new JLabel();
    scrollImage = new JScrollPane(sImageLabel);
    scrollImage.setPreferredSize(new Dimension(this.getWidth() / 2, this.getHeight() - 75));

    histogramp = new JPanel();
    histogramp.add(new JLabel("Histogram Panel"));
    histogramp.setPreferredSize(new Dimension(this.getWidth() / 2, this.getHeight() - 75));

    this.add(topbar, BorderLayout.NORTH);
    this.add(scrollImage, BorderLayout.WEST);
    this.add(histogramp, BorderLayout.EAST);

    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }


  @Override
  public void addFeatures(Features features) {

    horizontalBtn.addActionListener(evt -> System.out.println(evt.getActionCommand()));
    verticalBtn.addActionListener(evt -> System.out.println(evt.getActionCommand()));


    addBtn.addActionListener(evt -> {
      JFileChooser addChooser = new JFileChooser(".");
      FileNameExtensionFilter filter = new FileNameExtensionFilter(
              "BMP, JPG, JPEG, PNG, PPM Images", "jpg", "jpeg", "bmp", "png", "ppm");
      addChooser.setFileFilter(filter);
      int selected = addChooser.showOpenDialog(JFrameView.this);
      if (selected == JFileChooser.APPROVE_OPTION) {
        File selectedFile = addChooser.getSelectedFile();
        System.out.println("Selected file: " + selectedFile.getAbsolutePath() + " " + selectedFile.getName());
        features.loadImage(selectedFile.getAbsolutePath(), selectedFile.getName());
      }
    });

    saveBtn.addActionListener(evt -> {
      JFileChooser saveChooser = new JFileChooser(".");
      int selected = saveChooser.showSaveDialog(JFrameView.this);
      if (selected == JFileChooser.APPROVE_OPTION) {
        File selectedFile = saveChooser.getSelectedFile();
        System.out.println("Selected file: " + selectedFile.getAbsolutePath());
      }
    });


    commandDD.addActionListener(evt -> {
      System.out.println("Selected command: " + commandDD.getSelectedItem());
    });

    imagenameDD.addActionListener(evt -> showImage(imagenameDD.getSelectedItem().toString() + "-" + imagenameEffectDD.getSelectedItem().toString()));

    imagenameEffectDD.addActionListener(evt -> showImage(imagenameDD.getSelectedItem().toString() + "-" + imagenameEffectDD.getSelectedItem().toString()));

  }


  public void showImage(String name) {
    int[][][] imgList = this.processor.getImage(name);
    int[] imgInfo = this.processor.getInfo(name);
    BufferedImage convertedImg = convertImgToBufferImage(imgInfo, imgList);
    sImageLabel.setIcon(new ImageIcon(convertedImg));
  }

  public void updateNameList(String showname) {
    String[] original = processor.getNameList();
    processedImgNames = new LinkedHashMap<>();
    for (String name : original) {
      String[] splited = name.split("-", 2);
      System.out.println(splited);
      if (processedImgNames.containsKey(splited[0])) {
        ArrayList<String> tmp = processedImgNames.get(splited[0]);
        tmp.add(splited[1]);
      } else {
        ArrayList<String> tmp = new ArrayList<>();
        tmp.add(splited[1]);
        processedImgNames.put(splited[0], tmp);
      }
    }

    String[] splited = showname.split("-", 2);
    imagenameDD.removeAllItems();
    for (String name : processedImgNames.keySet()) {
      imagenameDD.addItem(name);
      if (name.equals(splited[0])) {
        imagenameDD.setSelectedItem(name);
      }
    }

    imagenameEffectDD.removeAllItems();
    for (String name : processedImgNames.get(splited[0])) {
      imagenameEffectDD.addItem(name);
      if (name.equals(splited[1])) {
        imagenameEffectDD.setSelectedItem(name);
      }
    }

  }

  private BufferedImage convertImgToBufferImage(int[] info, int[][][] image) {
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

  public void showErrorDialog(String msg) {
    JOptionPane.showMessageDialog(this, msg,
            "Action Incomplete", JOptionPane.ERROR_MESSAGE);
  }

}
