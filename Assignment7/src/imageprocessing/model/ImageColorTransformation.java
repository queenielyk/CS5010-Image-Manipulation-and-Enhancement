package imageprocessing.model;

/**
 * enum for matrices used for color transformations.
 */
public enum ImageColorTransformation {
  GreyscaleLuma(new double[][]{{0.2126, 0.7152, 0.0722}
          , {0.2126, 0.7152, 0.0722}
          , {0.2126, 0.7152, 0.0722}}),
  Sepia(new double[][]{{0.393, 0.769, 0.189}
          , {0.349, 0.686, 0.168}
          , {0.272, 0.534, 0.131}});
  final double[][] transformationMatrix;

  ImageColorTransformation(double[][] transformationMatrix) {
    this.transformationMatrix = transformationMatrix;
  }
}
