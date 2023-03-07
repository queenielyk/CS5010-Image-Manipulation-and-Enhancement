package IME;

import org.junit.Test;

import java.util.Scanner;

public class ImageControllerTest {
  @Test
  public void test() {
    String s = "test1 test2 test3/test3.text";
    Scanner scan = new Scanner(s);
    testNext(scan.next(), scan.next());
  }

  public static void testNext(String olds, String news) {
    System.out.println(olds);
    System.out.println(news);
  }
}
