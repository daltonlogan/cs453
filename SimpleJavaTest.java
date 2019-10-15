public class SimpleJavaTest{

    public static void TestThreeAddrGen(){
        System.out.println("*******************************************");
        System.out.println("Testing Three Address Generation");
        SimpleJava parser = new SimpleJava();

        String eval = "void blarg() {}";
        String result = "";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!");

        eval = "void main() {int x = 3 + 2;}";
        result = "temp0 = 3\ntemp1 = 2\ntemp2 = temp0 + temp1\nx = temp2\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!");

        eval = "void main() {int x = 4 / 2;}";
        result = "temp0 = 4\ntemp1 = 2\ntemp2 = temp0 / temp1\nx = temp2\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!");

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
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!");

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
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!");

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
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!");

        eval = "void main() {if(5 == 5){int x = 5;}}";
        result = "temp0 = 5\n" +
                "temp1 = 5\n" +
                "IF_EQ: temp0, temp1, trueLabel0\n" +
                "GOTO: falseLabel0\n" +
                "trueLabel0\n" +
                "temp0 = 5\n" +
                "x = temp0\n" +
                "falseLabel0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!");

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
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!");

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
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!");

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
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!");

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
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!");

        eval = "void main() {if(2 >= 3){int x = 5 - 5;}}";
        result ="temp0 = 2\n" +
                "temp1 = 3\n" +
                "IF_GTE: temp0, temp1, trueLabel0\n" +
                "GOTO: falseLabel0\n" +
                "trueLabel0\n" +
                "temp0 = 5\n" +
                "temp1 = 5\n" +
                "temp2 = temp0 - temp1\n" +
                "x = temp2\n" +
                "falseLabel0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!");

        eval = "void main() {while(2 != 3){int x = 5 + 5 * 5;}}";
        result ="repeatLabel0\n" +
                "temp0 = 2\n" +
                "temp1 = 3\n" +
                "IF_NE: temp0, temp1, trueLabel0\n" +
                "GOTO: falseLabel0\n" +
                "trueLabel0\n" +
                "temp0 = 5\n" +
                "temp1 = 5\n" +
                "temp2 = 5\n" +
                "temp3 = temp1 * temp2\n" +
                "temp4 = temp0 + temp3\n" +
                "x = temp4\n" +
                "GOTO: repeatLabel0\n" +
                "falseLabel0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!");

        System.out.println("*******************************************");
        System.out.println("Starting full test suite!!!");
        System.out.println("*******************************************");

        System.out.println("Testing: void main() {}");
        eval = "void main() {}";
        result = "";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!");

        System.out.println("Testing: void main() {int x = 3;}");
        eval = "void main() {int x = 3;}";
        result = "temp0 = 3\nx = temp0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!");

        System.out.println("Testing: void main() {int x = 3; int    y = 12;}");
        eval = "void main() {int x = 3; int    y = 12;}";
        result = "temp0 = 3\nx = temp0\ntemp0 = 12\ny = temp0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!");

        System.out.println("Testing: void main() {int x = 3; int y = x;}");
        eval = "void main() {int x = 3; int y = x;}";
        result = "temp0 = 3\n" +
                "x = temp0\n" +
                "y = x\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!");

        System.out.println("Testing: void main() {int x = 3 + 4 * 19 / (2 + 4); int y = x * 15;}");
        eval = "void main() {int x = 3 + 4 * 19 / (2 + 4); int y = x * 15;}";
        result = "temp0 = 3\n" +
                "temp1 = 4\n" +
                "temp2 = 19\n" +
                "temp3 = temp1 * temp2\n" +
                "temp4 = 2\n" +
                "temp5 = 4\n" +
                "temp6 = temp4 + temp5\n" +
                "temp7 = temp3 / temp6\n" +
                "temp8 = temp0 + temp7\n" +
                "x = temp8\n" +
                "temp0 = 15\n" +
                "temp1 = x * temp0\n" +
                "y = temp1\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n");

        System.out.println("Testing: void main() {int x = 3; int yooo = 12; int t11 = 15; int names23 = 9; int h2t4y78e = 43;}");
        eval = "void main() {int x = 3; int yooo = 12; int t11 = 15; int names23 = 9; int h2t4y78e = 43;}";
        result = "temp0 = 3\n" +
                "x = temp0\n" +
                "temp0 = 12\n" +
                "yooo = temp0\n" +
                "temp0 = 15\n" +
                "t11 = temp0\n" +
                "temp0 = 9\n" +
                "names23 = temp0\n" +
                "temp0 = 43\n" +
                "h2t4y78e = temp0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!");

        System.out.println("Testing: void main() { if (3 < 4){}}");
        eval = "void main() { if (3 < 4){}}";
        result = "temp0 = 3\n" +
                "temp1 = 4\n" +
                "IF_LT: temp0, temp1, trueLabel0\n" +
                "GOTO: falseLabel0\n" +
                "trueLabel0\n" +
                "falseLabel0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!");


        System.out.println("Testing: void main() { if (3 <= 4){int zebra12 = 88;    }}");
        eval = "void main() { if (3 <= 4){int zebra12 = 88;    }}";
        result = "temp0 = 3\n" +
                "temp1 = 4\n" +
                "IF_LTE: temp0, temp1, trueLabel0\n" +
                "GOTO: falseLabel0\n" +
                "trueLabel0\n" +
                "temp0 = 88\n" +
                "zebra12 = temp0\n" +
                "falseLabel0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!");


        System.out.println("Testing: void main() { int te3 = 57; if (3 < 4){int zebra12 = 88;    }}");
        eval = "void main() { int te3 = 57; if (3 < 4){int zebra12 = 88;    }}";
        result = "temp0 = 57\n" +
                "te3 = temp0\n" +
                "temp0 = 3\n" +
                "temp1 = 4\n" +
                "IF_LT: temp0, temp1, trueLabel0\n" +
                "GOTO: falseLabel0\n" +
                "trueLabel0\n" +
                "temp0 = 88\n" +
                "zebra12 = temp0\n" +
                "falseLabel0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!");


        System.out.println("Testing: void main() { int te3 = 57; if (3 < 4){int zebra12 = 88;    } if(9082 >= te3){}");
        eval = "void main() { int te3 = 57; if (3 < 4){int zebra12 = 88;    } if(9082 >= te3){} }";
        result = "temp0 = 57\n" +
                "te3 = temp0\n" +
                "temp0 = 3\n" +
                "temp1 = 4\n" +
                "IF_LT: temp0, temp1, trueLabel0\n" +
                "GOTO: falseLabel0\n" +
                "trueLabel0\n" +
                "temp0 = 88\n" +
                "zebra12 = temp0\n" +
                "falseLabel0\n" +
                "temp0 = 9082\n" +
                "IF_GTE: temp0, te3, trueLabel1\n" +
                "GOTO: falseLabel1\n" +
                "trueLabel1\n" +
                "falseLabel1\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!");

        System.out.println("*******************************************");
        System.out.println("End of test suite!!!");
        System.out.println("*******************************************");
    }

    public static void main(String[] args){
        TestThreeAddrGen();
    }

}