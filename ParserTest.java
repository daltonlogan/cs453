//import java.security.Permission;
//
//public class ParserTest{
//
//    int numTests = 0;
//
//    class MySecurityManager extends SecurityManager {
//        @Override public void checkExit(int status) {
//            throw new SecurityException();
//        }
//
//        @Override public void checkPermission(Permission perm) {
//            // Allow other activities by default
//        }
//    }
//
//    /* Quick test of token extraction*/
//    public int testEvaluation(){
//        System.out.println("*******************************************");
//        System.out.println("Testing Parser Evaluation");
//        int points = 0;
//        int total = 0;
//
//        EvalParser parser = new EvalParser();
//
//        // Addition
//        String eval = "9+4";
//        int expected = 13;
//        int result = parser.evaluateExpression(eval);
//        System.out.println("Testing: " + eval);
//        System.out.println("Expecting: " + expected);
//        if(result == (expected)){
//            points++;
//        } else {
//            System.out.println("Error, Got: " + result);
//        }
//        System.out.println();
//        total++;
//
//        // Subtraction
//        eval = "101   -  2";
//        expected = 99;
//        result = parser.evaluateExpression(eval);
//        System.out.println("Testing: " + eval);
//        System.out.println("Expecting: " + expected);
//        if(result == (expected)){
//            points++;
//        } else {
//            System.out.println("Error, Got: " + result);
//        }
//        System.out.println();
//        total++;
//
//        // Multiplication
//        eval = "17 * 8";
//        expected = 136;
//        result = parser.evaluateExpression(eval);
//        System.out.println("Testing: " + eval);
//        System.out.println("Expecting: " + expected);
//        if(result == (expected)){
//            points++;
//        } else {
//            System.out.println("Error, Got: " + result);
//        }
//        System.out.println();
//        total++;
//
//        // Division
//        eval = "10 * 10";
//        expected = 100;
//        result = parser.evaluateExpression(eval);
//        System.out.println("Testing: " + eval);
//        System.out.println("Expecting: " + expected);
//        if(result == (expected)){
//            points++;
//        } else {
//            System.out.println("Error, Got: " + result);
//        }
//        System.out.println();
//        total++;
//
//        // Order of Operations
//        eval = "5 - 3 * 7";
//        expected = -16;
//        result = parser.evaluateExpression(eval);
//        System.out.println("Testing: " + eval);
//        System.out.println("Expecting: " + expected);
//        if(result == (expected)){
//            points++;
//        } else {
//            System.out.println("Error, Got: " + result);
//        }
//        System.out.println();
//        total++;
//
//        // Order of Operations pt 2
//        eval = "5 - 3 * 7 - 4";
//        expected = -20;
//        result = parser.evaluateExpression(eval);
//        System.out.println("Testing: " + eval);
//        System.out.println("Expecting: " + expected);
//        if(result == (expected)){
//            points++;
//        } else {
//            System.out.println("Error, Got: " + result);
//        }
//        System.out.println();
//        total++;
//
//        // Order of Operations
//        eval = "2 * 2 + 8    / 4";
//        expected = 6;
//        result = parser.evaluateExpression(eval);
//        System.out.println("Testing: " + eval);
//        System.out.println("Expecting: " + expected);
//        if(result == (expected)){
//            points++;
//        } else {
//            System.out.println("Error, Got: " + result);
//        }
//        System.out.println();
//        total++;
//
//        // Order of Operations
//        eval = "9 - 16 + 3 - 10";
//        expected = -14;
//        result = parser.evaluateExpression(eval);
//        System.out.println("Testing: " + eval);
//        System.out.println("Expecting: " + expected);
//        if(result == (expected)){
//            points++;
//        } else {
//            System.out.println("Error, Got: " + result);
//        }
//        System.out.println();
//        total++;
//
//        // Order of Operations
//        eval = "2 * 8 / 4";
//        expected = 4;
//        result = parser.evaluateExpression(eval);
//        System.out.println("Testing: " + eval);
//        System.out.println("Expecting: " + expected);
//        if(result == (expected)){
//            points++;
//        } else {
//            System.out.println("Error, Got: " + result);
//        }
//        System.out.println();
//        total++;
//
//        // Parens
//        eval = "3 + (4 - 9)";
//        expected = -2;
//        result = parser.evaluateExpression(eval);
//        System.out.println("Testing: " + eval);
//        System.out.println("Expecting: " + expected);
//        if(result == (expected)){
//            points++;
//        } else {
//            System.out.println("Error, Got: " + result);
//        }
//        System.out.println();
//        total++;
//
//        numTests += total;
//        System.out.println("Got: " + points + "/" + total);
//        System.out.println("*******************************************");
//        System.out.println();
//        return points;
//    }
//
//    /* Quick test of token extraction*/
////    public int testThreeAddr(){
////        System.out.println("*******************************************");
////        System.out.println("Testing Parser Evaluation");
////        int points = 0;
////        int total = 0;
////
////        EvalParser parser = new EvalParser();
////
////        // Addition
////        String eval = "9+4";
////        String expected = "temp0 = 9\n"+
////                "temp1 = 4\n"+
////                "temp2 = temp0 + temp1\n";
////        String result = parser.getThreeAddr();
////        System.out.println("Testing: " + eval);
////        System.out.println("Expecting: " + expected);
////        if(result.equals(expected)){
////            points++;
////        } else {
////            System.out.println("Error, Got: " + result);
////        }
////        System.out.println();
////        total++;
////
////        // Subtraction
////        parser = new EvalParser();
////        eval = "101   -  2";
////        expected = "temp0 = 101\n"+
////                "temp1 = 2\n"+
////                "temp2 = temp0 - temp1\n";
////        result = parser.getThreeAddr(eval);
////        System.out.println("Testing: " + eval);
////        System.out.println("Expecting: " + expected);
////        if(result.equals(expected)){
////            points++;
////        } else {
////            System.out.println("Error, Got: " + result);
////        }
////        System.out.println();
////        total++;
////
////        // Multiplication
////        parser = new EvalParser();
////        eval = "17 * 8";
////        expected = "temp0 = 17\n"+
////                "temp1 = 8\n"+
////                "temp2 = temp0 * temp1\n";
////        result = parser.getThreeAddr(eval);
////        System.out.println("Testing: " + eval);
////        System.out.println("Expecting: " + expected);
////        if(result.equals(expected)){
////            points++;
////        } else {
////            System.out.println("Error, Got: " + result);
////        }
////        System.out.println();
////        total++;
////
////        // Division
////        parser = new EvalParser();
////        eval = "10 * 10";
////        expected = "temp0 = 10\n"+
////                "temp1 = 10\n"+
////                "temp2 = temp0 * temp1\n";
////        result = parser.getThreeAddr(eval);
////        System.out.println("Testing: " + eval);
////        System.out.println("Expecting: " + expected);
////        if(result.equals(expected)){
////            points++;
////        } else {
////            System.out.println("Error, Got: " + result);
////        }
////        System.out.println();
////        total++;
////
////        // Order of Operations
////        parser = new EvalParser();
////        eval = "5 - 3 * 7";
////        expected = "temp0 = 5\n"+
////                "temp1 = 3\n"+
////                "temp2 = 7\n"+
////                "temp3 = temp1 * temp2\n"+
////                "temp4 = temp0 - temp3\n";
////        result = parser.getThreeAddr(eval);
////        System.out.println("Testing: " + eval);
////        System.out.println("Expecting: " + expected);
////        if(result.equals(expected)){
////            points++;
////        } else {
////            System.out.println("Error, Got: " + result);
////        }
////        System.out.println();
////        total++;
////
////        // Order of Operations pt 2
////        parser = new EvalParser();
////        eval = "5 - 3 * 7 - 4";
////        expected = "temp0 = 5\n"+
////                "temp1 = 3\n"+
////                "temp2 = 7\n"+
////                "temp3 = temp1 * temp2\n"+
////                "temp4 = temp0 - temp3\n"+
////                "temp5 = 4\n"+
////                "temp6 = temp4 - temp5\n";
////        result = parser.getThreeAddr(eval);
////        System.out.println("Testing: " + eval);
////        System.out.println("Expecting: " + expected);
////        if(result.equals(expected)){
////            points++;
////        } else {
////            System.out.println("Error, Got: " + result);
////        }
////        System.out.println();
////        total++;
////
////        // Order of Operations
////        parser = new EvalParser();
////        eval = "2 * 2 + 8    / 4";
////        expected = "temp0 = 2\n"+
////                "temp1 = 2\n"+
////                "temp2 = temp0 * temp1\n"+
////                "temp3 = 8\n"+
////                "temp4 = 4\n"+
////                "temp5 = temp3 / temp4\n"+
////                "temp6 = temp2 + temp5\n";
////        result = parser.getThreeAddr(eval);
////        System.out.println("Testing: " + eval);
////        System.out.println("Expecting: " + expected);
////        if(result.equals(expected)){
////            points++;
////        } else {
////            System.out.println("Error, Got: " + result);
////        }
////        System.out.println();
////        total++;
////
////        // Order of Operations
////        parser = new EvalParser();
////        eval = "9 - 16 + 3 - 10";
////        expected = "temp0 = 9\n"+
////                "temp1 = 16\n"+
////                "temp2 = temp0 - temp1\n"+
////                "temp3 = 3\n"+
////                "temp4 = temp2 + temp3\n"+
////                "temp5 = 10\n"+
////                "temp6 = temp4 - temp5\n";
////        result = parser.getThreeAddr(eval);
////        System.out.println("Testing: " + eval);
////        System.out.println("Expecting: " + expected);
////        if(result.equals(expected)){
////            points++;
////        } else {
////            System.out.println("Error, Got: " + result);
////        }
////        System.out.println();
////        total++;
////
////        // Order of Operations
////        parser = new EvalParser();
////        eval = "2 * 8 / 4";
////        expected = "temp0 = 2\n"+
////                "temp1 = 8\n"+
////                "temp2 = temp0 * temp1\n"+
////                "temp3 = 4\n"+
////                "temp4 = temp2 / temp3\n";
////        result = parser.getThreeAddr(eval);
////        System.out.println("Testing: " + eval);
////        System.out.println("Expecting: " + expected);
////        if(result.equals(expected)){
////            points++;
////        } else {
////            System.out.println("Error, Got: " + result);
////        }
////        System.out.println();
////        total++;
////
////        // Parens
////        parser = new EvalParser();
////        eval = "3 + (4 - 9)";
////        expected = "temp0 = 3\n"+
////                "temp1 = 4\n"+
////                "temp2 = 9\n"+
////                "temp3 = temp1 - temp2\n"+
////                "temp4 = temp0 + temp3\n";
////        result = parser.getThreeAddr(eval);
////        System.out.println("Testing: " + eval);
////        System.out.println("Expecting: " + expected);
////        if(result.equals(expected)){
////            points++;
////        } else {
////            System.out.println("Error, Got: " + result);
////        }
////        System.out.println();
////        total++;
////
////        numTests += total;
////        System.out.println("Got: " + points + "/" + total);
////        System.out.println("*******************************************");
////        System.out.println();
////        return points;
////    }
//
//
//    public ParserTest(){
//        // Test set #1: Evaluation
//        int testsCorrect = testEvaluation();
//
//        // Test set #2: 3 addr translation
//        testsCorrect += testThreeAddr();
//
//        // Test set #3: Eval but with errors
//        // Test set #4: 3 addr trans but with errors
//        MySecurityManager secManager = new MySecurityManager();
//        System.setSecurityManager(secManager);
//        ParserErrorTest t1 = new ParserErrorTest();
//        try {
//            t1.main(new String[]{"1"});
//        } catch (SecurityException e){
//            System.out.println();
//        }
//        try {
//            t1.main(new String[]{"2"});
//        } catch (SecurityException e){
//            System.out.println();
//        }
//        try {
//            t1.main(new String[]{"3"});
//        } catch (SecurityException e){
//            System.out.println();
//        }
//        try {
//            t1.main(new String[]{"4"});
//        } catch (SecurityException e){
//            System.out.println();
//        }
//        try {
//            t1.main(new String[]{"5"});
//        } catch (SecurityException e){
//            System.out.println();
//        }
//        try {
//            t1.main(new String[]{"6"});
//        } catch (SecurityException e){
//            System.out.println();
//        }
//        try {
//            t1.main(new String[]{"7"});
//        } catch (SecurityException e){
//            System.out.println();
//        }
//        try {
//            t1.main(new String[]{"8"});
//        } catch (SecurityException e){
//            System.out.println();
//        }
//        try {
//            t1.main(new String[]{"9"});
//        } catch (SecurityException e){
//            System.out.println();
//        }
//        try {
//            t1.main(new String[]{"10"});
//        } catch (SecurityException e){
//            System.out.println();
//        }
//
//        System.out.println("Got: " + testsCorrect + "/" + numTests + " for non error test cases");
//        System.out.println("Number of error test cases passed: /10");
//    }
//
//    public static void main(String[] args){
//        ParserTest scan = new ParserTest();
//    }
//
//}