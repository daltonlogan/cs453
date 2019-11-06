public class SimpleJavaTest{

    public static void TestThreeAddrGen(){
        System.out.println("*******************************************");
        System.out.println("Testing Three Address Generation");
        SimpleJava parser = new SimpleJava();

        String eval = "public class test { void blarg() {} }";
        String result = "";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        eval = "public class test { void main() {int x = 3 + 2;} }";
        result = "temp0 = 3\ntemp1 = 2\ntemp2 = temp0 + temp1\nx = temp2\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        eval = "private class test { void main() {int x = 4 / 2;} }";
        result = "temp0 = 4\ntemp1 = 2\ntemp2 = temp0 / temp1\nx = temp2\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        eval = "private class test { void main() { int x = 12; if(3 < 6) { int y = 3; } } }";
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
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        eval = "public class test {void main() { int x = 3; while(3 < 2) { int x = 4;} } }";
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
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        eval = "private class test {void blarg() {" +
                "if( 2 + 3 - 2 < 2) {" +
                "int x = 9+(2*2);" +
                "}" +
                "}}";
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
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        eval = "private class test {void main() {if(5 == 5){int x = 5;}}}";
        result = "temp0 = 5\n" +
                "temp1 = 5\n" +
                "IF_EQ: temp0, temp1, trueLabel0\n" +
                "GOTO: falseLabel0\n" +
                "trueLabel0\n" +
                "temp0 = 5\n" +
                "x = temp0\n" +
                "falseLabel0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        eval = "public class test {void main() {if(2 + 3 == 5){int x = 5;}}}";
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
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        eval = "private class t {void main() {if(2 + 3 == 1 + 4){int x = 5;}}}";
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
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        eval = "private class test {void main() {if(2 + 3 == 1 + 4){int x = 5;} if( 2 < 3 ) {int a = 1;}}}";
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
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        eval = "private class test {void main() {if(2 + 3 != 1 + 4){int x = 1;}}}";
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
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        eval = "private class test {void main() {if(2 >= 3){int x = 5 - 5;}}}";
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
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        eval = "private class test {void main() {while(2 != 3){int x = 5 + 5 * 5;}}}";
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
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        System.out.println("*******************************************");
        System.out.println("Starting full test suite!!!");
        System.out.println("*******************************************");

        eval = "private class test {void main() {}}";
        System.out.println("Testing: " + eval);
        result = "";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        eval = "private class test {void main() {int x = 3;}}";
        System.out.println("Testing: " + eval);
        result = "temp0 = 3\nx = temp0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        eval = "private class test {void main() {int x = 3;" +
                "\nint      y = 12;}}";
        System.out.println("Testing: " + eval);
        result = "temp0 = 3\nx = temp0\ntemp0 = 12\ny = temp0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        eval = "private class test {void main() {int x = 3;" +
                "\nint y = x;}}";
        System.out.println("Testing: " + eval);
        result = "temp0 = 3\n" +
                "x = temp0\n" +
                "y = x\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        eval = "private class test {void main() {int x = 3 + 4 * 19 / (2 + 4);" +
                "\nint y = x * 15;}}";
        System.out.println("Testing: " + eval);
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
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        eval = "public class test {void main() {int x = 3; int yooo = 12; int t11 = 15; int names23 = 9; int h2t4y78e = 43;}}";
        System.out.println("Testing: " + eval);
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
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        eval = "public class test {void main() { if (3 < 4){}}}";
        System.out.println("Testing: " + eval);
        result = "temp0 = 3\n" +
                "temp1 = 4\n" +
                "IF_LT: temp0, temp1, trueLabel0\n" +
                "GOTO: falseLabel0\n" +
                "trueLabel0\n" +
                "falseLabel0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));


        eval = "private class test {void main() { if (3 <= 4){int zebra12 = 88;    " +
                "\n}}}";
        System.out.println("Testing: " + eval);
        result = "temp0 = 3\n" +
                "temp1 = 4\n" +
                "IF_LTE: temp0, temp1, trueLabel0\n" +
                "GOTO: falseLabel0\n" +
                "trueLabel0\n" +
                "temp0 = 88\n" +
                "zebra12 = temp0\n" +
                "falseLabel0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        eval = "private class test {void main() { int te3 = 57; if (3 < 4){int zebra12 = 88;    " +
                "\n}}}";
        System.out.println("Testing: " + eval);
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
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));


        eval = "private class test {void main() { int te3 = 57; if (3 < 4){int zebra12 = 88;    " +
                "\n} if(9082 >= te3){}}}";
        System.out.println("Testing: " + eval);
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
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));


        eval = "private class test {void main() {if (3 < 4){if(9082 >= te3){}}}}";
        System.out.println("Testing: " + eval);
        result = "temp0 = 3\n" +
        		"temp1 = 4\n" +
        		"IF_LT: temp0, temp1, trueLabel0\n" +
        		"GOTO: falseLabel0\n" +
        		"trueLabel0\n" +
        		"temp0 = 9082\n" +
        		"IF_GTE: temp0, te3, trueLabel1\n" +
        		"GOTO: falseLabel1\n" +
        		"trueLabel1\n" +
        		"falseLabel1\n" +
        		"falseLabel0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));


        eval = "private class test {void main() {if (3 < 4){if(9082 >= te3){}} int y4t = 34;}}";
        System.out.println("Testing: " + eval);
        result = "temp0 = 3\n" +
                "temp1 = 4\n" +
                "IF_LT: temp0, temp1, trueLabel0\n" +
                "GOTO: falseLabel0\n" +
                "trueLabel0\n" +
                "temp0 = 9082\n" +
                "IF_GTE: temp0, te3, trueLabel1\n" +
                "GOTO: falseLabel1\n" +
                "trueLabel1\n" +
                "falseLabel1\n" +
                "falseLabel0\n" +
                "temp0 = 34\n" +
                "y4t = temp0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));


        eval = "private class test {void main() {while(15 < 98) {}}}";
        System.out.println("Testing: " + eval);
        result = "repeatLabel0\n" +
                "temp0 = 15\n" +
                "temp1 = 98\n" +
                "IF_LT: temp0, temp1, trueLabel0\n" +
                "GOTO: falseLabel0\n" +
                "trueLabel0\n" +
                "GOTO: repeatLabel0\n" +
                "falseLabel0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));


        eval = "public class test {void main() {while(15 < 98) {int yeeee7 = 44;}}}";
        System.out.println("Testing: " + eval);
        result = "repeatLabel0\n" +
                "temp0 = 15\n" +
                "temp1 = 98\n" +
                "IF_LT: temp0, temp1, trueLabel0\n" +
                "GOTO: falseLabel0\n" +
                "trueLabel0\n" +
                "temp0 = 44\n" +
                "yeeee7 = temp0\n" +
                "GOTO: repeatLabel0\n" +
                "falseLabel0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));


        eval = "public class test {void main() {while(15 < 98) { if(9 == 8){} }}}";
        System.out.println("Testing: " + eval);
        result = "repeatLabel0\n" +
        		"temp0 = 15\n" +
        		"temp1 = 98\n" +
        		"IF_LT: temp0, temp1, trueLabel0\n" +
        		"GOTO: falseLabel0\n" +
        		"trueLabel0\n" +
        		"temp0 = 9\n" +
        		"temp1 = 8\n" +
        		"IF_EQ: temp0, temp1, trueLabel1\n" +
        		"GOTO: falseLabel1\n" +
        		"trueLabel1\n" +
        		"falseLabel1\n" +
        		"GOTO: repeatLabel0\n" +
        		"falseLabel0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));


        eval = "private class test {void main() {while(15 < 98) { if(9 == 8){int z = 98;} }}}";
        System.out.println("Testing: " + eval);
        result = "repeatLabel0\n" +
        		"temp0 = 15\n" +
        		"temp1 = 98\n" +
        		"IF_LT: temp0, temp1, trueLabel0\n" +
        		"GOTO: falseLabel0\n" +
        		"trueLabel0\n" +
        		"temp0 = 9\n" +
        		"temp1 = 8\n" +
        		"IF_EQ: temp0, temp1, trueLabel1\n" +
        		"GOTO: falseLabel1\n" +
        		"trueLabel1\n" +
        		"temp0 = 98\n" +
        		"z = temp0\n" +
        		"falseLabel1\n" +
        		"GOTO: repeatLabel0\n" +
        		"falseLabel0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));


        eval = "public class test {void main() {while(15 < 98) { while(9 == 8){}}}}";
        System.out.println("Testing: " + eval);
        result = "repeatLabel0\n" +
        		"temp0 = 15\n" +
        		"temp1 = 98\n" +
        		"IF_LT: temp0, temp1, trueLabel0\n" +
        		"GOTO: falseLabel0\n" +
        		"trueLabel0\n" +
        		"repeatLabel1\n" +
        		"temp0 = 9\n" +
        		"temp1 = 8\n" +
        		"IF_EQ: temp0, temp1, trueLabel1\n" +
        		"GOTO: falseLabel1\n" +
        		"trueLabel1\n" +
        		"GOTO: repeatLabel1\n" +
        		"falseLabel1\n" +
        		"GOTO: repeatLabel0\n" +
        		"falseLabel0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));


        eval = "public class test {void main() {while(15 < 98) { while(9 == 8){ if(2 != 9){} }}}}";
        System.out.println("Testing: " + eval);
        result = "repeatLabel0\n" +
        		"temp0 = 15\n" +
        		"temp1 = 98\n" +
        		"IF_LT: temp0, temp1, trueLabel0\n" +
        		"GOTO: falseLabel0\n" +
        		"trueLabel0\n" +
        		"repeatLabel1\n" +
        		"temp0 = 9\n" +
        		"temp1 = 8\n" +
        		"IF_EQ: temp0, temp1, trueLabel1\n" +
        		"GOTO: falseLabel1\n" +
        		"trueLabel1\n" +
        		"temp0 = 2\n" +
        		"temp1 = 9\n" +
        		"IF_NE: temp0, temp1, trueLabel2\n" +
        		"GOTO: falseLabel2\n" +
        		"trueLabel2\n" +
        		"falseLabel2\n" +
        		"GOTO: repeatLabel1\n" +
        		"falseLabel1\n" +
        		"GOTO: repeatLabel0\n" +
        		"falseLabel0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));


        eval = "private class test {void main() {while(15 < 98) { while(9 == 8){ if(2 != 9){} } if(9 < 2){} }}}";
        System.out.println("Testing: " + eval);
        result = "repeatLabel0\n" +
        		"temp0 = 15\n" +
        		"temp1 = 98\n" +
        		"IF_LT: temp0, temp1, trueLabel0\n" +
        		"GOTO: falseLabel0\n" +
        		"trueLabel0\n" +
        		"repeatLabel1\n" +
        		"temp0 = 9\n" +
        		"temp1 = 8\n" +
        		"IF_EQ: temp0, temp1, trueLabel1\n" +
        		"GOTO: falseLabel1\n" +
        		"trueLabel1\n" +
        		"temp0 = 2\n" +
        		"temp1 = 9\n" +
        		"IF_NE: temp0, temp1, trueLabel2\n" +
        		"GOTO: falseLabel2\n" +
        		"trueLabel2\n" +
        		"falseLabel2\n" +
        		"GOTO: repeatLabel1\n" +
        		"falseLabel1\n" +
        		"temp0 = 9\n" +
        		"temp1 = 2\n" +
        		"IF_LT: temp0, temp1, trueLabel3\n" +
        		"GOTO: falseLabel3\n" +
        		"trueLabel3\n" +
        		"falseLabel3\n" +
        		"GOTO: repeatLabel0\n" +
        		"falseLabel0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));


        eval = "private class test {void main() {while((17 - (9 + 2)) < 17) {}}}";
        System.out.println("Testing: " + eval);
        result = "repeatLabel0\n" +
                "temp0 = 17\n" +
                "temp1 = 9\n" +
                "temp2 = 2\n" +
                "temp3 = temp1 + temp2\n" +
                "temp4 = temp0 - temp3\n" +
                "temp5 = 17\n" +
                "IF_LT: temp4, temp5, trueLabel0\n" +
                "GOTO: falseLabel0\n" +
                "trueLabel0\n" +
                "GOTO: repeatLabel0\n" +
                "falseLabel0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));


        eval = "private class test {int x; void first() {int y = 1; int z = 2;}}";
        System.out.println( "Testing: " + eval );
        result = "temp0 = 1\n" +
                "y = temp0\n" +
                "temp0 = 2\n" +
                "z = temp0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        eval = "private class test {void main() {int x; int y; int z = 1;}}";
        System.out.println( "Testing: " + eval );
        result = "temp0 = 1\n" +
                "z = temp0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));


        eval = "public class test " +
                "{" +
                "int x;" +
                "int y;" +
                "void main()" +
                "{" +
                "int a = 5;" +
                "int b;" +
                "if( 5 + 5 == 10 )" +
                "{" +
                "int c = 2;" +
                "}" +
                "}" +
                "}";
        System.out.println( "Testing: " + eval );
        result = "temp0 = 5\n" +
                "a = temp0\n" +
                "temp0 = 5\n" +
                "temp1 = 5\n" +
                "temp2 = temp0 + temp1\n" +
                "temp3 = 10\n" +
                "IF_EQ: temp2, temp3, trueLabel0\n" +
                "GOTO: falseLabel0\n" +
                "trueLabel0\n" +
                "temp0 = 2\n" +
                "c = temp0\n" +
                "falseLabel0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));


        eval = "public class test" +
                "{" +
                "int x;" +
                "void main()" +
                "{" +
                "    int a = 2;" +
                "    int b = 4;" +
                "    while( a < b )" +
                "    {" +
                "        a = a + 1;" +
                "    }" +
                "}" +
                "}";
        System.out.println( "Testing: " + eval );
        result = "temp0 = 2\n" +
                "a = temp0\n" +
                "temp0 = 4\n" +
                "b = temp0\n" +
                "repeatLabel0\n" +
                "IF_LT: a, b, trueLabel0\n" +
                "GOTO: falseLabel0\n" +
                "trueLabel0\n" +
                "temp0 = 1\n" +
                "temp1 = a + temp0\n" +
                "a = temp1\n" +
                "GOTO: repeatLabel0\n" +
                "falseLabel0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));


        eval = "public class test" +
                "{" +
                "void main1()" +
                "{" +
                "    int x = 1;" +
                "}" +
                "void main2()" +
                "{" +
                "    int y = 2;" +
                "}" +
                "}";
        System.out.println( "Testing: " + eval );
        result = "temp0 = 1\n" +
                "x = temp0\n" +
                "temp0 = 2\n" +
                "y = temp0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));


        eval = "public class test" +
                "{" +
                "void main()" +
                "{" +
                "    int a = 2;" +
                "    if( 1 + 1 != 2 )" +
                "    {" +
                "        int b = 5;" +
                "    }" +
                "    int c;" +
                "    int d = 6;" +
                "    }" +
                "}";
        System.out.println( "Testing: " + eval );
        result = "temp0 = 2\n" +
                "a = temp0\n" +
                "temp0 = 1\n" +
                "temp1 = 1\n" +
                "temp2 = temp0 + temp1\n" +
                "temp3 = 2\n" +
                "IF_NE: temp2, temp3, trueLabel0\n" +
                "GOTO: falseLabel0\n" +
                "trueLabel0\n" +
                "temp0 = 5\n" +
                "b = temp0\n" +
                "falseLabel0\n" +
                "temp0 = 6\n" +
                "d = temp0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        eval = "public class test {int x; int y; void mainEntry(){ int x; x = 3; if(2 > 3 && 5 < 4){ x = 42; }}}";
        System.out.println( "Testing: " + eval );
        result = "temp0 = 3\n"+
                "x = temp0\n"+
                "temp0 = 2\n"+
                "temp1 = 3\n"+
                "IF_GT: temp0, temp1, trueLabel1\n"+
                "GOTO: falseLabel0\n"+
                "trueLabel1\n"+
                "temp2 = 5\n"+
                "temp3 = 4\n"+
                "IF_LT: temp2, temp3, trueLabel0\n"+
                "GOTO: falseLabel0\n"+
                "trueLabel0\n"+
                "temp0 = 42\n"+
                "x = temp0\n"+
                "falseLabel0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        eval = "public class test {int x; int y; void mainEntry(){ int x; x = 3; if(2 > 3 && 5 == 5){ x = 42; y = 12; }}}";
        System.out.println( "Testing: " + eval );
        result = "temp0 = 3\n"+
                "x = temp0\n"+
                "temp0 = 2\n"+
                "temp1 = 3\n"+
                "IF_GT: temp0, temp1, trueLabel1\n"+
                "GOTO: falseLabel0\n"+
                "trueLabel1\n"+
                "temp2 = 5\n"+
                "temp3 = 5\n"+
                "IF_EQ: temp2, temp3, trueLabel0\n"+
                "GOTO: falseLabel0\n"+
                "trueLabel0\n"+
                "temp0 = 42\n"+
                "x = temp0\n"+
                "temp0 = 12\n"+
                "y = temp0\n"+
                "falseLabel0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        eval = "public class test { void mainEntry() { int res = 14; if( 2 < 3 || 9 < 10 ) { res = 42; } res = res + 1; } }";
        System.out.println( "Testing: " + eval );
        result ="temp0 = 14\n" +
                "res = temp0\n" +
                "temp0 = 2\n" +
                "temp1 = 3\n" +
                "IF_LT: temp0, temp1, trueLabel0\n" +
                "GOTO: falseLabel1\n" +
                "falseLabel1\n" +
                "temp2 = 9\n" +
                "temp3 = 10\n" +
                "IF_LT: temp2, temp3, trueLabel0\n" +
                "GOTO: falseLabel0\n" +
                "trueLabel0\n" +
                "temp0 = 42\n" +
                "res = temp0\n" +
                "falseLabel0\n" +
                "temp0 = 1\n" +
                "temp1 = res + temp0\n" +
                "res = temp1\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));


        eval = "public class multiple { void main() { if( 2 < 3 && 3 != 4 ) { int x = 1; } } }";
        System.out.println( "Testing: " + eval );
        result = "temp0 = 2\n" +
                "temp1 = 3\n" +
                "IF_LT: temp0, temp1, trueLabel1\n" +
                "GOTO: falseLabel0\n" +
                "trueLabel1\n" +
                "temp2 = 3\n" +
                "temp3 = 4\n" +
                "IF_NE: temp2, temp3, trueLabel0\n" +
                "GOTO: falseLabel0\n" +
                "trueLabel0\n" +
                "temp0 = 1\n" +
                "x = temp0\n" +
                "falseLabel0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        eval = "public class test {int x; int y; void mainEntry(){ int x; x = 3; if(2 < 3 && 5 < 4){ x = 42; }}}";
        System.out.println( "Testing: " + eval );
        result = "temp0 = 3\n"+
                "x = temp0\n"+
                "temp0 = 2\n"+
                "temp1 = 3\n"+
                "IF_LT: temp0, temp1, trueLabel1\n"+
                "GOTO: falseLabel0\n"+
                "trueLabel1\n"+
                "temp2 = 5\n"+
                "temp3 = 4\n"+
                "IF_LT: temp2, temp3, trueLabel0\n"+
                "GOTO: falseLabel0\n"+
                "trueLabel0\n"+
                "temp0 = 42\n"+
                "x = temp0\n"+
                "falseLabel0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        eval = "public class test { void main() { int x = 0; int y = 0; while( x >= 3 || y < 5 ) { x = x + 1; y = y + 2; } int a = 2; } }";
        System.out.println( "Testing: " + eval );
        result = "temp0 = 0\n" +
                "x = temp0\n" +
                "temp0 = 0\n" +
                "y = temp0\n" +
                "repeatLabel0\n" +
                "temp0 = 3\n" +
                "IF_GTE: x, temp0, trueLabel0\n" +
                "GOTO: falseLabel1\n" +
                "falseLabel1\n" +
                "temp1 = 5\n" +
                "IF_LT: y, temp1, trueLabel0\n" +
                "GOTO: falseLabel0\n" +
                "trueLabel0\n" +
                "temp0 = 1\n" +
                "temp1 = x + temp0\n" +
                "x = temp1\n" +
                "temp0 = 2\n" +
                "temp1 = y + temp0\n" +
                "y = temp1\n" +
                "GOTO: repeatLabel0\n" +
                "falseLabel0\n" +
                "temp0 = 2\n" +
                "a = temp0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        eval = "public class test { void main() { if( 17 - (9 + 2) < 20 || 5 + 5 * 2 == 15 ) { int a = 0; } } }";
        System.out.println( "Testing: " + eval );
        result = "temp0 = 17\n" +
                "temp1 = 9\n" +
                "temp2 = 2\n" +
                "temp3 = temp1 + temp2\n" +
                "temp4 = temp0 - temp3\n" +
                "temp5 = 20\n" +
                "IF_LT: temp4, temp5, trueLabel0\n" +
                "GOTO: falseLabel1\n" +
                "falseLabel1\n" +
                "temp6 = 5\n" +
                "temp7 = 5\n" +
                "temp8 = 2\n" +
                "temp9 = temp7 * temp8\n" +
                "temp10 = temp6 + temp9\n" +
                "temp11 = 15\n" +
                "IF_EQ: temp10, temp11, trueLabel0\n" +
                "GOTO: falseLabel0\n" +
                "trueLabel0\n" +
                "temp0 = 0\n" +
                "a = temp0\n" +
                "falseLabel0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        eval = "public class testLocalScope { void main() { int x = 5; } void main2() { x = 3; } }";
        System.out.println( "Testing: " + eval );
        result = "temp0 = 5\n" +
                "x = temp0\n" +
                "temp0 = 3\n" +
                "x = temp0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        eval = "public class testGlobalScope { int x; void main() { int x = 5; } void main2() { x = 3; } }";
        System.out.println( "Testing: " + eval );
        result = "temp0 = 5\n" +
                "x = temp0\n" +
                "temp0 = 3\n" +
                "x = temp0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        eval = "public class testGlobalScope2 { int x; void main() { x = 5; } void main2() { x = 3; } }";
        System.out.println( "Testing: " + eval );
        result = "temp0 = 5\n" +
                "x = temp0\n" +
                "temp0 = 3\n" +
                "x = temp0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        eval = "public class testLocalAndGlobal { int x; int y; void main() { int x; x = 5; } void main2() { y = 3; } }";
        System.out.println( "Testing: " + eval );
        result = "temp0 = 5\n" +
                "x = temp0\n" +
                "temp0 = 3\n" +
                "y = temp0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        eval = "public class testMultipleDefinitions { void main() { int x; x = 5; } void main() { int y = 3; } }";
        System.out.println( "Testing: " + eval );
        result = "temp0 = 5\n" +
                "x = temp0\n" +
                "temp0 = 3\n" +
                "y = temp0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        eval = "public class test { void main() { int x = 0; if( x < 3 ) { x = 5; int y; } } void main2() { int y = 3; } }";
        System.out.println( "Testing: " + eval );
        result = "temp0 = 0\n" +
                "x = temp0\n" +
                "temp0 = 3\n" +
                "IF_LT: x, temp0, trueLabel0\n" +
                "GOTO: falseLabel0\n" +
                "trueLabel0\n" +
                "temp0 = 5\n" +
                "x = temp0\n" +
                "falseLabel0\n" +
                "temp0 = 3\n" +
                "y = temp0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        eval = "public class testOR { void main() { int x = 0; if( x < 3 || y > 3 ) { x = 5; int y; } } void main2() { int y = 3; } }";
        System.out.println( "Testing: " + eval );
        result = "temp0 = 0\n" +
                "x = temp0\n" +
                "temp0 = 3\n" +
                "IF_LT: x, temp0, trueLabel0\n" +
                "GOTO: falseLabel1\n" +
                "falseLabel1\n" +
                "temp1 = 3\n" +
                "IF_GT: y, temp1, trueLabel0\n" +
                "GOTO: falseLabel0\n" +
                "trueLabel0\n" +
                "temp0 = 5\n" +
                "x = temp0\n" +
                "falseLabel0\n" +
                "temp0 = 3\n" +
                "y = temp0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        eval = "public class testAND { void main() { int x = 0; if( x < 3 && y > 3 ) { x = 5; int y; } } void main2() { int y = 3; } }";
        System.out.println( "Testing: " + eval );
        result = "temp0 = 0\n" +
                "x = temp0\n" +
                "temp0 = 3\n" +
                "IF_LT: x, temp0, trueLabel1\n" +
                "GOTO: falseLabel0\n" +
                "trueLabel1\n" +
                "temp1 = 3\n" +
                "IF_GT: y, temp1, trueLabel0\n" +
                "GOTO: falseLabel0\n" +
                "trueLabel0\n" +
                "temp0 = 5\n" +
                "x = temp0\n" +
                "falseLabel0\n" +
                "temp0 = 3\n" +
                "y = temp0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        eval = "public class testAND2 { int x; int y; void main() { int x = 0; if( x < 3 && y > 3 ) { x = 5; int y; } } void main2() { int y = 3; } }";
        System.out.println( "Testing: " + eval );
        result = "temp0 = 0\n" +
                "x = temp0\n" +
                "temp0 = 3\n" +
                "IF_LT: x, temp0, trueLabel1\n" +
                "GOTO: falseLabel0\n" +
                "trueLabel1\n" +
                "temp1 = 3\n" +
                "IF_GT: y, temp1, trueLabel0\n" +
                "GOTO: falseLabel0\n" +
                "trueLabel0\n" +
                "temp0 = 5\n" +
                "x = temp0\n" +
                "falseLabel0\n" +
                "temp0 = 3\n" +
                "y = temp0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        eval = "public class testAlreadyDefined { int x; int y; void main() { int x = 0; } int y; }";
        System.out.println( "Testing: " + eval );
        result = "temp0 = 0\n" +
                "x = temp0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        eval = "public class testMultipleError { int x; int y; void main() { int x = 0; a = 1; b = 2; } void main() { a = 2; y = 3; } }";
        System.out.println( "Testing: " + eval );
        result = "temp0 = 0\n" +
                "x = temp0\n" +
                "temp0 = 1\n" +
                "a = temp0\n" +
                "temp0 = 2\n" +
                "b = temp0\n" +
                "temp0 = 2\n" +
                "a = temp0\n" +
                "temp0 = 3\n" +
                "y = temp0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

        eval = "public class test {void main() { if(15 < 98 && 2 < 3 && 3 < 4){ } } }";
        System.out.println( "Testing: " + eval );
        result = "temp0 = 15\n" +
                "temp1 = 98\n" +
                "IF_LT: temp0, temp1, trueLabel2\n" +
                "GOTO: falseLabel0\n" +
                "trueLabel2\n" +
                "temp2 = 2\n" +
                "temp3 = 3\n" +
                "IF_LT: temp2, temp3, trueLabel1\n" +
                "GOTO: falseLabel0\n" +
                "trueLabel1\n" +
                "temp4 = 3\n" +
                "temp5 = 4\n" +
                "IF_LT: temp4, temp5, trueLabel0\n" +
                "GOTO: falseLabel0\n" +
                "trueLabel0\n" +
                "falseLabel0\n";
        if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));


        System.out.println("*******************************************");
        System.out.println("End of test suite!!!");
        System.out.println("*******************************************");
    }

    public static void main(String[] args){
        TestThreeAddrGen();
    }

}