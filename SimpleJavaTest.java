public class SimpleJavaTest{

    public static void TestThreeAddrGen(){
        System.out.println("*******************************************");
        System.out.println("Testing Three Address Generation");
        SimpleJava parser = new SimpleJava();

        String eval = "void blarg() {}";
        String result = "";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) throw new AssertionError();

        eval = "void main() {int x = 3 + 2;}";
        result = "temp0 = 3\ntemp1 = 2\ntemp2 = temp0 + temp1\nx = temp2\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) throw new AssertionError();

        eval = "void main() {int x = 4 / 2;}";
        result = "temp0 = 4\ntemp1 = 2\ntemp2 = temp0 / temp1\nx = temp2\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) throw new AssertionError();

        eval = "void main() { int x = 12; if(3 < 6) { int y = 3; } }";
        result = "temp0 = 12\n"+
                "x = temp0\n"+
                "temp0 = 3\n"+
                "temp1 = 6\n"+
                "IF_LT: temp0, temp1, trueLabel0\n"+
                "GOTO: falseLabel0\n"+
                "trueLabel0\n"+
                "temp0 = 3\n"+
                "y = temp0\n"+
                "falseLabel0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) throw new AssertionError();

        eval = "void main() { int x = 3; while(3 < 2) { int x = 4;} }";
        result = "temp0 = 3\n"+
                "x = temp0\n"+
                "repeatLabel0\n"+
                "temp0 = 3\n"+
                "temp1 = 2\n"+
                "IF_LT: temp0, temp1, trueLabel0\n"+
                "GOTO: falseLabel0\n"+
                "trueLabel0\n"+
                "temp0 = 4\n"+
                "x = temp0\n"+
                "GOTO: repeatLabel0\n"+
                "falseLabel0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) throw new AssertionError();

        eval = "void blarg() {" +
                "if( 2 + 3 - 2 < 2) {" +
                "int x = 9+(2*2);" +
                "}" +
                "}";
        result = "temp0 = 2\n" +
                "temp1 = 3\n" +
                "temp2 = temp0 + temp1\n" +
                "temp3 = 2\n" +
                "temp4 = temp2 - temp3\n" +
                "temp5 = 2\n" +
                "IF_LT: temp4, temp5, trueLabel0\n" +
                "GOTO: falseLabel0\n" +
                "trueLabel0\n" +
                "temp0 = 9\n" +
                "temp1 = 2\n" +
                "temp2 = 2\n" +
                "temp3 = temp1 * temp2\n" +
                "temp4 = temp0 + temp3\n" +
                "x = temp4\n" +
                "falseLabel0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) throw new AssertionError();

        eval = "void main() {if(5 == 5){int x = 5;}}";
        result = "temp0 = 5\n" +
                "temp1 = 5\n" +
                "IF_EQ: temp0, temp1, trueLabel0\n" +
                "GOTO: falseLabel0\n" +
                "trueLabel0\n" +
                "temp0 = 5\n" +
                "x = temp0\n" +
                "falseLabel0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) throw new AssertionError();

        eval = "void main() {if(2 + 3 == 5){int x = 5;}}";
        result ="temp0 = 2\n" +
                "temp1 = 3\n" +
                "temp2 = temp0 + temp1\n" +
                "temp3 = 5\n" +
                "IF_EQ: temp2, temp3, trueLabel0\n" +
                "GOTO: falseLabel0\n" +
                "trueLabel0\n" +
                "temp0 = 5\n" +
                "x = temp0\n" +
                "falseLabel0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) throw new AssertionError();

        eval = "void main() {if(2 + 3 == 1 + 4){int x = 5;}}";
        result ="temp0 = 2\n" +
                "temp1 = 3\n" +
                "temp2 = temp0 + temp1\n" +
                "temp3 = 1\n" +
                "temp4 = 4\n" +
                "temp5 = temp3 + temp4\n" +
                "IF_EQ: temp2, temp5, trueLabel0\n" +
                "GOTO: falseLabel0\n" +
                "trueLabel0\n" +
                "temp0 = 5\n" +
                "x = temp0\n" +
                "falseLabel0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) throw new AssertionError();

        eval = "void main() {if(2 + 3 == 1 + 4){int x = 5;} if( 2 < 3 ) {int a = 1;}}";
        result ="temp0 = 2\n" +
                "temp1 = 3\n" +
                "temp2 = temp0 + temp1\n" +
                "temp3 = 1\n" +
                "temp4 = 4\n" +
                "temp5 = temp3 + temp4\n" +
                "IF_EQ: temp2, temp5, trueLabel0\n" +
                "GOTO: falseLabel0\n" +
                "trueLabel0\n" +
                "temp0 = 5\n" +
                "x = temp0\n" +
                "falseLabel0\n"+
                "temp0 = 2\n" +
                "temp1 = 3\n" +
                "IF_LT: temp0, temp1, trueLabel1\n" +
                "GOTO: falseLabel1\n" +
                "trueLabel1\n" +
                "temp0 = 1\n" +
                "a = temp0\n" +
                "falseLabel1\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) throw new AssertionError();

        eval = "void main() {if(2 + 3 != 1 + 4){int x = 1;}}";
        result ="temp0 = 2\n" +
                "temp1 = 3\n" +
                "temp2 = temp0 + temp1\n" +
                "temp3 = 1\n" +
                "temp4 = 4\n" +
                "temp5 = temp3 + temp4\n" +
                "IF_NE: temp2, temp5, trueLabel0\n" +
                "GOTO: falseLabel0\n" +
                "trueLabel0\n" +
                "temp0 = 1\n" +
                "x = temp0\n" +
                "falseLabel0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) throw new AssertionError();

        System.out.println("Congrats: three address generation tests passed! Now make your own test cases "+
                "(this is only a subset of what we will test your code on)");
        System.out.println("*******************************************");
    }

    public static void main(String[] args){
        TestThreeAddrGen();
    }

}