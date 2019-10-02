public class ParserErrorTest{

    public static void testErrorOne(){
        System.out.println("*******************************************");
        System.out.println("Testing Errors");
        System.out.println("Testing: 3 +");
        EvalParser parser = new EvalParser();
        int result = parser.evaluateExpression("3 +");
        //System.out.println("Error Test Case (should report an error): " + int result);
        System.out.println();
    }

    public static void testErrorTwo(){
        System.out.println("*******************************************");
        System.out.println("Testing Errors");
        System.out.println("Testing: (");
        EvalParser parser = new EvalParser();
        int result = parser.evaluateExpression("  (   ");
        //System.out.println("Error Test Case (should report an error): " + int result);
        System.out.println();
    }

    public static void testErrorThree(){
        System.out.println("*******************************************");
        System.out.println("Testing Errors");
        System.out.println("Testing: ()");
        EvalParser parser = new EvalParser();
        int result = parser.evaluateExpression("()");
        //System.out.println("Error Test Case (should report an error): " + int result);
        System.out.println();
    }

    public static void testErrorFour(){
        System.out.println("*******************************************");
        System.out.println("Testing Errors");
        System.out.println("Testing: 8- 9- ");
        EvalParser parser = new EvalParser();
        int result = parser.evaluateExpression(" 8- 9- ");
        //System.out.println("Error Test Case (should report an error): " + int result);
        System.out.println();
    }

    public static void testErrorFive(){
        System.out.println("*******************************************");
        System.out.println("Testing Errors");
        System.out.println("Testing: 3*(((((((((((((((0)");
        EvalParser parser = new EvalParser();
        int result = parser.evaluateExpression("3*(((((((((((((((0)");
        //System.out.println("Error Test Case (should report an error): " + int result);
        System.out.println();
    }

    public static void testErrorSix(){
        System.out.println("*******************************************");
        System.out.println("Testing Errors");
        System.out.println("Testing: 17--3");
        EvalParser parser = new EvalParser();
        int result = parser.evaluateExpression("17--3");
        //System.out.println("Error Test Case (should report an error): " + int result);
        System.out.println();
    }

    public static void testErrorSeven(){
        System.out.println("*******************************************");
        System.out.println("Testing Errors");
        System.out.println("Testing: (9*(4-()");
        EvalParser parser = new EvalParser();
        int result = parser.evaluateExpression("(9*(4-()");
        //System.out.println("Error Test Case (should report an error): " + int result);
        System.out.println();
    }

    public static void testErrorEight(){
        System.out.println("*******************************************");
        System.out.println("Testing Errors");
        System.out.println("Testing: 12-9() + 3");
        EvalParser parser = new EvalParser();
        int result = parser.evaluateExpression("12-9() + 3");
        //System.out.println("Error Test Case (should report an error): " + int result);
        System.out.println();
    }

    public static void testErrorNine(){
        System.out.println("*******************************************");
        System.out.println("Testing Errors");
        System.out.println("Testing: )");
        EvalParser parser = new EvalParser();
        int result = parser.evaluateExpression(")");
        //System.out.println("Error Test Case (should report an error): " + int result);
        System.out.println();
    }

    public static void testErrorTen(){
        System.out.println("*******************************************");
        System.out.println("Testing Errors");
        System.out.println("Testing: -8");
        EvalParser parser = new EvalParser();
        int result = parser.evaluateExpression("-8");
        //System.out.println("Error Test Case (should report an error): " + int result);
        System.out.println();
    }


    public static void main(String[] args){
        if(args[0].equals("1")){
            testErrorOne();
        } else if(args[0].equals("2")){
            testErrorTwo();
        } else if(args[0].equals("3")){
            testErrorThree();
        } else if(args[0].equals("4")){
            testErrorFour();
        } else if(args[0].equals("5")){
            testErrorFive();
        } else if(args[0].equals("6")){
            testErrorSix();
        } else if(args[0].equals("7")){
            testErrorSeven();
        } else if(args[0].equals("8")){
            testErrorEight();
        } else if(args[0].equals("9")){
            testErrorNine();
        } else if(args[0].equals("10")){
            testErrorTen();
        }

    }

}