package imageprocessing.view;

import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;

/**
 * This class represents the histogram. It contains the components of the Histogram.
 */

class LineChart extends JPanel {
  private final int width;
  private final int height;
  private final int gap = 5;
  private final Stroke stroke = new BasicStroke(1f);
  private final List<int[]> listOfColors;
  private Graphics2D graphics;

  LineChart(List<int[]> listOfColors) {
    this.listOfColors = listOfColors;
    this.width = 300;
    this.height = listOfColors.get(0).length + 100;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    this.graphics = (Graphics2D) g;

    double x = ((double) getWidth() - 2 * gap) / 255;
    double y = ((double) getHeight() - 2 * gap) / this.getNumberOfPixels();

    createHatchesOnGraph();

    this.graphics.drawLine(gap, getHeight() - gap, gap, gap);
    this.graphics.drawLine(gap, getHeight() - gap, getWidth() - gap, getHeight() - gap);

    plotLine("red", x, y);
    plotLine("green", x, y);
    plotLine("blue", x, y);
    plotLine("intensity", x, y);
  }

  private void plotLine(String lineType, double xScale, double yScale) {
    int[] arrayToBePlotted;
    if (lineType.equalsIgnoreCase("red")) {
      graphics.setColor(Color.RED);
      arrayToBePlotted = this.listOfColors.get(0);
    } else if (lineType.equalsIgnoreCase("green")) {
      graphics.setColor(Color.GREEN);
      arrayToBePlotted = this.listOfColors.get(1);
    } else if (lineType.equalsIgnoreCase("blue")) {
      graphics.setColor(Color.BLUE);
      arrayToBePlotted = this.listOfColors.get(2);
    } else {
      graphics.setColor(Color.BLACK);
      arrayToBePlotted = this.listOfColors.get(3);
    }

    List<Point> points = new ArrayList<>();
    for (int i = 0; i < 256; i++) {
      int x1 = (int) (i * xScale + gap);
      int y1 = (int) ((arrayToBePlotted[i]) * yScale + gap);
      points.add(new Point(x1, y1));
    }

    graphics.setStroke(stroke);
    for (int i = 0; i < points.size() - 1; i++) {
      graphics.drawLine(points.get(i).x, getHeight() - points.get(i).y
              , points.get(i + 1).x, getHeight() - points.get(i + 1).y);
    }
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(width, height);
  }

  private int getNumberOfPixels() {
    int maxInRed = Arrays.stream(this.listOfColors.get(0)).max().getAsInt();
    int maxInGreen = Arrays.stream(this.listOfColors.get(1)).max().getAsInt();
    int maxInBlue = Arrays.stream(this.listOfColors.get(2)).max().getAsInt();
    int maxPixelValue = maxInRed > maxInGreen ? maxInRed : maxInGreen;
    return maxPixelValue > maxInBlue ? maxPixelValue : maxInBlue;
  }

  private void createHatchesOnGraph() {
    int widthOfPoint = 4;
    for (int i = 0; i < 12; i++) {
      graphics.drawLine(gap, getHeight() - (((i + 1) * (getHeight() - gap * 2)) / 12 + gap)
              , widthOfPoint + gap, getHeight() - (((i + 1) * (getHeight() - gap * 2)) / 12
                      + gap));
    }

    for (int i = 0; i < 26; i++) {
      graphics.drawLine((i + 1) * (getWidth() - gap * 2) /
                      25 + gap, getHeight() - gap
              , (i + 1) * (getWidth() - gap * 2) / 25 + gap
              , getHeight() - gap - widthOfPoint);
    }
  }
}