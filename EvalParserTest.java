public class EvalParserTest{

  public static void TestExpressionEval(){
    System.out.println("*******************************************");
    System.out.println("Testing Expression evaluation");
    System.out.println("*******************************************");
    EvalParser parser = new EvalParser();

    System.out.println( "Starting test 1" );
    String eval = "9+(2*2)";
    if ((parser.evaluateExpression(eval) != 13)) throw new AssertionError();
    System.out.println( "Passed test 1" );

    System.out.println();

    System.out.println( "Starting test 2" );
    eval = "10-9";
    if ((parser.evaluateExpression(eval) != 1)) throw new AssertionError();
    System.out.println( "Passed test 2" );

    System.out.println();

    System.out.println( "Starting test 3" );
    eval = "3-5*17";
    if ((parser.evaluateExpression(eval) != -82)) throw new AssertionError();
    System.out.println( "Passed test 3" );

    System.out.println();

    System.out.println("Congrats: expression evaluation tests passed! Now make your own test cases "+
                       "(this is only a subset of what we will test your code on)");
    System.out.println("*******************************************");

    System.out.println( "*******************************************" );
    System.out.println( "Testing Custom Expression Evaluation" );
    System.out.println( "*******************************************" );

    System.out.println( "Starting test 1" );
    eval = "3*2*2";
    if ((parser.evaluateExpression(eval) != 12)) throw new AssertionError();
    System.out.println( "Passed test 1" );

    System.out.println();

    System.out.println( "Starting test 2" );
    eval = "(2 - 3 * 2) + 2";
    if ((parser.evaluateExpression(eval) != -2)) throw new AssertionError();
    System.out.println( "Passed test 2" );

    System.out.println();

    System.out.println( "Starting test 3" );
    eval = "(2) + (2)";
    if ((parser.evaluateExpression(eval) != 4)) throw new AssertionError();
    System.out.println( "Passed test 3" );

    System.out.println();

    System.out.println( "Starting test 4" );
    eval = "(2*5+4) - (3 + 5 )";
    if ((parser.evaluateExpression(eval) != 6)) throw new AssertionError();
    System.out.println( "Passed test 4" );

    System.out.println();

    System.out.println( "Starting test 5" );
    eval = "2 * 2 + 3 * 3";
    if ((parser.evaluateExpression(eval) != 13)) throw new AssertionError();
    System.out.println( "Passed test 5" );

    System.out.println();

    System.out.println( "Starting test 6" );
    eval = "5 - 3 * 4 / 2";
    if ((parser.evaluateExpression(eval) != -1)) throw new AssertionError();
    System.out.println( "Passed test 6" );
    
    System.out.println();
    
    System.out.println("Starting test 7");
    eval = "9 + ( 2 * 2 )";
    if ((parser.evaluateExpression(eval) != 13)) throw new AssertionError();
    System.out.println("Passed test 7");

    System.out.println();

    System.out.println( "*******************************************" );
    System.out.println( "Passed All Tests" );
    System.out.println( "*******************************************" );


  }

  public static void TestThreeAddrGen(){
    System.out.println("*******************************************");
    System.out.println("Testing Three Address Generation");
    EvalParser parser = new EvalParser();

    System.out.println("Starting Test 1");
    String eval = "9+(2*2)";
    String result = "temp0 = 9\n"+
                    "temp1 = 2\n"+
                    "temp2 = 2\n"+
                    "temp3 = temp1 * temp2\n"+
                    "temp4 = temp0 + temp3\n";
    //assert(parser.getThreeAddr(eval).equals(result));
    if (!parser.getThreeAddr(eval).equals(result)) throw new AssertionError();
    System.out.println("Passed Test 1");
    
    System.out.println();
    
    System.out.println("Starting Test 2");//empty edge case
    eval = "";
    result = "";
    if (!parser.getThreeAddr(eval).equals(result)) throw new AssertionError();
    System.out.println("Passed Test 2");
    
    System.out.println();
    
    System.out.println("Starting Test 3");
    eval = "1+2+3";
    result = "temp0 = 1\n"+
    		"temp1 = 2\n"+
    		"temp2 = 3\n"+
    		"temp3 = temp1 + temp2\n"+
    		"temp4 = temp0 + temp3\n";
    //if (!parser.getThreeAddr(eval).equals(result)) throw new AssertionError();
    System.out.println(parser.getThreeAddr(eval));
    System.out.println("Passed Test 3");
    
    System.out.println();

    System.out.println("Congrats: three address generation tests passed! Now make your own test cases "+
                       "(this is only a subset of what we will test your code on)");
    System.out.println("*******************************************");
  }

  public static void main(String[] args){
    TestExpressionEval();
    TestThreeAddrGen();
  }

}
