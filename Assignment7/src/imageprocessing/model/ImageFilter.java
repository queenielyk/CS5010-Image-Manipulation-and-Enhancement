package imageprocessing.model;

/**
 * enum for kernels used for filtering images.
 */
public enum ImageFilter {
  Blur(new double[][]{
          {1d / 16d, 1d / 8d, 1d / 16d},
          {1d / 8d, 1d / 4d, 1d / 8d},
          {1d / 16d, 1d / 8d, 1d / 16d}
  }),
  Sharpen(new double[][]{
          {-1d / 8d, -1d / 8d, -1d / 8d, -1d / 8d, -1d / 8d},
          {-1d / 8d, 1d / 4d, 1d / 4d, 1d / 4d, -1d / 8d},
          {-1d / 8d, 1d / 4d, 1d, 1d / 4d, -1d / 8d},
          {-1d / 8d, 1d / 4d, 1d / 4d, 1d / 4d, -1d / 8d},
          {-1d / 8d, -1d / 8d, -1d / 8d, -1d / 8d, -1d / 8d}
  });

  final double[][] filterValues;

  ImageFilter(double[][] filterValues) {
    this.filterValues = filterValues;
  }
}
