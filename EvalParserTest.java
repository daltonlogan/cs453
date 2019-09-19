public class EvalParserTest{

  public static void TestExpressionEval(){
    System.out.println("*******************************************");
    System.out.println("Testing Expression evaluation");
    EvalParser parser = new EvalParser();

    String eval = "9+(2*2)";
    assert(parser.evaluateExpression(eval) == 13);

    eval = "10-9";
    assert(parser.evaluateExpression(eval) == 1);

    eval = "3-5*17";
    assert(parser.evaluateExpression(eval) == -82);

    System.out.println("Congrats: expression evaluation tests passed! Now make your own test cases "+
                       "(this is only a subset of what we will test your code on)");
    System.out.println("*******************************************");
  }

  public static void TestThreeAddrGen(){
    System.out.println("*******************************************");
    System.out.println("Testing Three Address Generation");
    EvalParser parser = new EvalParser();

    String eval = "9+(2*2)";
    String result = "temp0 = 9\n"+
                    "temp1 = 2\n"+
                    "temp2 = 2\n"+
                    "temp3 = temp1 * temp2\n"+
                    "temp4 = temp0 + temp3\n";
    assert(parser.getThreeAddr(eval).equals(result));

    System.out.println("Congrats: three address generation tests passed! Now make your own test cases "+
                       "(this is only a subset of what we will test your code on)");
    System.out.println("*******************************************");
  }

  public static void main(String[] args){
    TestExpressionEval();
    TestThreeAddrGen();
  }

}
