public class ScannerTest{

  /* Quick test of token extraction*/
  public static void testTokenExtraction(){
    System.out.println("*******************************************");
    System.out.println("Testing Token Extraction");
    Scanner test = new Scanner();

    String result = test.extractTokens("123");
    String expected = "|NUM: 123|";
    if ( ( !result.equals( expected ) ) ) throw new AssertionError();

    result = test.extractTokens("+ 3 3");
    expected = "|PLUS: +||NUM: 3||NUM: 3|";
    if ( ( !result.equals( expected ) ) ) throw new AssertionError();

    result = test.extractTokens("+ - * / < >");
    expected = "|PLUS: +||MINUS: -||MUL: *||DIV: /||LT: <||GT: >|";
    if ( ( !result.equals( expected ) ) ) throw new AssertionError();

    result = test.extractTokens("<= - >=");
    expected = "|LTE: <=||MINUS: -||GTE: >=|";
    if ( ( !result.equals( expected ) ) ) throw new AssertionError();

    System.out.println("Congrats: preliminary token extraction tests passed! Now make your own test cases "+
                       "(this is only a subset of what we will test your code on)");
    System.out.println("*******************************************");
    System.out.println();
  }

  public static void main(String[] args){
    testTokenExtraction();
  }

}
