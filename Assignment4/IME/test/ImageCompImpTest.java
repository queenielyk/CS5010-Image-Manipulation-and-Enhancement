import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ImageCompImpTest {

  @Test(expected = IllegalStateException.class)
  public void testBuildException() {
    ImageComp component = new ImageCompImp(0, -6, 0);
  }

  @Test
  public void testBuildCompWONext() {
    ImageComp component = new ImageCompImp(0, 0, 0);
    for (int temp : component.getRGB()) {
      assertEquals(0, temp, 0);
    }
  }

  @Test
  public void testBuildCompWNext() {
    ImageComp first = new ImageCompImp(0, 0, 0);
    ImageComp second = new ImageCompImp(2, 2, 2);
    for (int temp : first.getRGB()) {
      assertEquals(0, temp, 0);
    }

    first.setNext(second);

    for (int temp : first.getNext().getRGB()) {
      assertEquals(2, temp, 0);
    }

  }


}