public class FinalTestNoGCC{

    public static void TestThreeAddrGen(){
        System.out.println("*******************************************");
        System.out.println("Testing Three Address Generation");

        String eval;
        AdvancedJava parser = new AdvancedJava();
        String fileName = "test.c";

        eval = "public class test { int reserved; void main() {} void mainEntry(int five) { five = 0; reserved = five; main() } }";
        parser.codeGen(eval, fileName);

//        eval = "public class test { int reserved; void mainEntry( int a, int b ) { a = 5; b = 1; reserved = a + b; } }";
//        parser.codeGen(eval, fileName);
//
//        eval = "public class test { int reserved; void mainEntry( int a, int b, int c ) { a = 3; b = 2; c = a * b; reserved = a + b + c; } }";
//        parser.codeGen(eval, fileName);
//
//        eval = "public class test { int reserved; int glob; void mainEntry( int a, int b, int c, int d ) { reserved = 1; glob = 2; b = 4; c = b; d = b + glob; a = c * d + reserved; reserved = glob + reserved + a; } }";
//        parser.codeGen(eval, fileName);
//
//        eval = "public class test { int reserved; void mainEntry( int b ) { b = 5; reserved = b; } }";
//        parser.codeGen(eval, fileName);
//
//        eval = "private class test { int reserved; void hey(){} int glob; void hey2(){} void mainEntry() { glob = 2; reserved = 42; reserved = reserved * (glob - 0); } }";
//        parser.codeGen(eval, fileName);
//
//        eval = "private class test { int reserved; int glob; void mainEntry() { int glob = 31; reserved = glob; } }";
//        parser.codeGen(eval, fileName);
//
//        eval = "private class test { int reserved; int glob; void mainEntry() { glob = 15; int glob = 2; reserved = glob; } }";
//        parser.codeGen(eval, fileName);
//
//        eval = "private class test { int reserved; int glob; void mainEntry() { int loc = 15; glob = loc; loc = glob; reserved = loc; } }";
//        parser.codeGen(eval, fileName);
//
//        eval = "private class test { int reserved; int glob; void mainEntry() { int loc = 15; glob = loc+1; loc = glob+1; reserved = loc; } }";
//        parser.codeGen(eval, fileName);
//
//        eval = "private class test { int reserved; void mainEntry() { if(1 < 2){reserved = 47;} } }";
//        parser.codeGen(eval, fileName);
//
//        eval = "private class test { int reserved; void mainEntry() { if(1 < 2){ reserved = 17; if(2>1){ reserved = 42; } reserved = reserved + 2; } } }";
//        parser.codeGen(eval, fileName);
//
//        eval = "private class test { int reserved; void mainEntry() { reserved = 12; if(reserved < 22){reserved = 42;} } }";
//        parser.codeGen(eval, fileName);
//
//        eval = "private class test { int reserved; void mainEntry() { reserved = 12; int loc = 14; if(reserved <= loc-2){reserved = 1;} } }";
//        parser.codeGen(eval, fileName);
//
//        eval = "private class test { int reserved; void mainEntry() { reserved = 12; if(reserved < 22 && reserved > 9){reserved = 18;} } }";
//        parser.codeGen(eval, fileName);
//
//        eval = "private class test { int reserved; void mainEntry() { reserved = 12; while(reserved < 22){reserved = reserved+1;} } }";
//        parser.codeGen(eval, fileName);
//
//        eval = "private class test { int reserved; void mainEntry() { reserved = 12; while(reserved < 22 && reserved != 14){reserved = reserved+1;} } }";
//        parser.codeGen(eval, fileName);
//
//        eval = "private class test { int reserved; void mainEntry() { reserved = 2; while(reserved > 22 || reserved <= 10){if(reserved < 10){reserved = reserved+1;} reserved = reserved + 1;} reserved = reserved + 9; } }";
//        parser.codeGen(eval, fileName);
//
//        eval = "private class test { int reserved; void mainEntry() { reserved = 2; while(reserved > 22 || reserved <= 10){if(reserved < 10){reserved = reserved+1;} int loc = 1; reserved = reserved + loc;} } }";
//        parser.codeGen(eval, fileName);
//
//        eval = "private class test { int reserved; void mainEntry() { reserved = 2; while(reserved > 22 || reserved <= 10){if(reserved < 10 && reserved > 4){reserved = reserved+1;} reserved = reserved + 1;} reserved = reserved + 2;} }";
//        parser.codeGen(eval, fileName);
//
//        System.out.println("Congrats: three address generation tests passed! Now make your own test cases "+
//                "(this is only a subset of what we will test your code on)");
//        System.out.println("*******************************************");
//
//        eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; if(2 < 3) {reserved = 120;}} }";
//        parser.codeGen(eval, fileName);
//
//        eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; if(2 > 3) {reserved = 42;}} }";
//        parser.codeGen(eval, fileName);
//
//        eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; if(2 < 3 && 3 < 4) {reserved = 42;}} }";
//        parser.codeGen(eval, fileName);
//
//        eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; x = 2+3; if(2 < 3) {reserved = 42;}} }";
//        parser.codeGen(eval, fileName);
//
//        eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; x = 2+3; if(x == 5) {reserved = 42;}} }";
//        parser.codeGen(eval, fileName);
//
//        eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; int x = 2+3; if(5 == 4) {reserved = 42;}} }";
//        parser.codeGen(eval, fileName);
//
//        eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; int x = 2+3; if(5 == 5 && 4 < 3) {reserved = 42;}} }";
//        parser.codeGen(eval, fileName);
//
//        eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; int x = 2+3; if(5 == 4 || 4 < 3) {reserved = 42;}} }";
//        parser.codeGen(eval, fileName);
//
//        eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; int x = 2+3; if(5 == 4 || 4 > 3) {reserved = 42;}} }";
//        parser.codeGen(eval, fileName);
//
//        eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; int x = 2+3; if(5 == 5 && 4 > 3) {reserved = 42;}} }";
//        parser.codeGen(eval, fileName);
//
//        eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; int x = 2+3; if(5 == 5) {if (4 != 5) {reserved = 42;}}} }";
//        parser.codeGen(eval, fileName);
//
//        eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; int x = 2+3; while(4 == 5) {reserved = 42;}} }";
//        parser.codeGen(eval, fileName);
//
//        eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; x = 2+3; if(1 < 2 && 4 > 3) {reserved = x+1;}} }";
//        parser.codeGen(eval, fileName);
//
//        eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0;} }";
//        parser.codeGen(eval, fileName);
//
//        eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; x = 0; while(x < 2){x = x+1;reserved = x;}} }";
//        parser.codeGen(eval, fileName);
//
//        eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; if(2 < 3) {reserved = 42; if(5 > 5) {reserved = 4;}}} }";
//        parser.codeGen(eval, fileName);
//
//        eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; if(2 < 3) {reserved = 42; if(5 < 5) {reserved = 4;}}} }";
//        parser.codeGen(eval, fileName);
//
//        eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; if(2 < 3) {reserved = 42; if(5 != 5) {reserved = 4;}}} }";
//        parser.codeGen(eval, fileName);
//
//        eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; if(2 < 3) {x = 4; if(5 == 5) {reserved = 40; x = 42;} reserved = x;}} }";
//        parser.codeGen(eval, fileName);
//
//        eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; if(2 < 3) {x = 4; if(5 == 5) {reserved = 40; int z = 42; reserved = z;}}} }";
//        parser.codeGen(eval, fileName);
//
//        eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; if(3 >= 3) {reserved = 42;}} }";
//        parser.codeGen(eval, fileName);
//
//        eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; if(2 >= 3) {reserved = 42;}} }";
//        parser.codeGen(eval, fileName);
//
//        eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; if(2 <= 3) {reserved = 42;}} }";
//        parser.codeGen(eval, fileName);
//
//        eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; if(4 <= 3) {reserved = 42;}} }";
//        parser.codeGen(eval, fileName);
    }

    public static void main(String[] args){
        try {
            TestThreeAddrGen();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
