import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AdvancedJavaTest{

  public static void consume(Process cmdProc) throws IOException{
    BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(cmdProc.getInputStream()));
    String line;
    while ((line = stdoutReader.readLine()) != null) {
       // process procs standard output here
    }

    BufferedReader stderrReader = new BufferedReader(new InputStreamReader(cmdProc.getErrorStream()));
    while ((line = stderrReader.readLine()) != null) {
       // process procs standard error here
    }
  }

  public static void TestThreeAddrGen() throws IOException, InterruptedException{
    System.out.println("*******************************************");
    System.out.println("Testing Three Address Generation");

    String  eval = "public class test { int reserved; void mainEntry() { reserved = 0; } }";
    AdvancedJava parser = new AdvancedJava();
    String fileName = "test_official_1.c";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    Process cmdProc = Runtime.getRuntime().exec("gcc -g -Wall " + fileName + " -o test");
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    int retValue = cmdProc.exitValue();
    if( retValue != 0 )
    {
      System.out.println( "FAILED official test  1\nExpected: 0\nActual: " + retValue );
    }
    else
    {
      System.out.println("PASSED official test  1");
    }

    System.out.println();
    fileName = "test_official_2.c";

    eval = "public class test { int reserved; void mainEntry() { reserved = 2 + 2; } }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc = Runtime.getRuntime().exec("gcc -g -Wall " + fileName + " -o test");
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 4 )
    {
      System.out.println( "FAILED official test  2\nExpected: 4\nActual: " + retValue );
    }
    else
    {
      System.out.println("PASSED official test  2");
    }

    System.out.println();
    fileName = "test_official_3.c";

    eval = "public class test { int reserved; void mainEntry() { int x = 14; int y = x * 8; reserved = y + x; } }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc = Runtime.getRuntime().exec("gcc -g -Wall " + fileName + " -o test");
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 126 )
    {
      System.out.println( "FAILED official test  3\nExpected: 126\nActual: " + retValue );
    }
    else
    {
      System.out.println("PASSED official test  3");
    }

    System.out.println();
    fileName = "test_official_4.c";

    eval = "public class test { int reserved; int glob; void mainEntry() { glob = 24; reserved = glob / 4; } }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc = Runtime.getRuntime().exec("gcc -g -Wall " + fileName + " -o test");
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 6 )
    {
      System.out.println( "FAILED official test  4\nExpected: 6\nActual: " + retValue );
    }
    else
    {
      System.out.println("PASSED official test  4");
    }

    System.out.println();
    fileName = "test_official_5.c";

    eval = "public class test { int reserved; void blarg(){ reserved = 12; } void mainEntry() { reserved = 9; reserved = reserved - 4; } }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc = Runtime.getRuntime().exec("gcc -g -Wall " + fileName + " -o test");
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 5 )
    {
      System.out.println( "FAILED official test  5\nExpected: 5\nActual: " + retValue );
    }
    else
    {
      System.out.println("PASSED official test  5");
    }

    System.out.println();
    fileName = "test_official_6.c";

    eval = "private class test { int reserved; void hey(){} int glob; void hey2(){} void mainEntry() { glob = 2; reserved = 42; reserved = reserved * (glob - 0); } }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc = Runtime.getRuntime().exec("gcc -g -Wall " + fileName + " -o test");
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 84 )
    {
      System.out.println( "FAILED official test  6\nExpected: 84\nActual: " + retValue );
    }
    else
    {
      System.out.println("PASSED official test  6");
    }

    System.out.println();
    fileName = "test_official_7.c";

    eval = "private class test { int reserved; int glob; void mainEntry() { int glob = 31; reserved = glob; } }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc = Runtime.getRuntime().exec("gcc -g -Wall " + fileName + " -o test");
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 31 )
    {
      System.out.println( "FAILED official test  7\nExpected: 31\nActual: " + retValue );
    }
    else
    {
      System.out.println("PASSED official test  7");
    }

    System.out.println();
    fileName = "test_official_8.c";

    eval = "private class test { int reserved; int glob; void mainEntry() { glob = 15; int glob = 2; reserved = glob; } }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc = Runtime.getRuntime().exec("gcc -g -Wall " + fileName + " -o test");
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 2 )
    {
      System.out.println( "FAILED official test  8\nExpected: 2\nActual: " + retValue );
    }
    else
    {
      System.out.println("PASSED official test  8");
    }

    System.out.println();
    fileName = "test_official_9.c";

    eval = "private class test { int reserved; int glob; void mainEntry() { int loc = 15; glob = loc; loc = glob; reserved = loc; } }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc = Runtime.getRuntime().exec("gcc -g -Wall " + fileName + " -o test");
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 15 )
    {
      System.out.println( "FAILED official test  9\nExpected: 15\nActual: " + retValue );
    }
    else
    {
      System.out.println("PASSED official test  9");
    }

    System.out.println();
    fileName = "test_official_10.c";

    eval = "private class test { int reserved; int glob; void mainEntry() { int loc = 15; glob = loc+1; loc = glob+1; reserved = loc; } }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc = Runtime.getRuntime().exec("gcc -g -Wall " + fileName + " -o test");
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 17 )
    {
      System.out.println( "FAILED official test  10\nExpected: 17\nActual: " + retValue );
    }
    else
    {
      System.out.println("PASSED official test  10");
    }

    System.out.println();
    fileName = "test_official_11.c";

    eval = "private class test { int reserved; void mainEntry() { if(1 < 2){reserved = 47;} } }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc = Runtime.getRuntime().exec("gcc -g -Wall " + fileName + " -o test");
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 47 )
    {
      System.out.println( "FAILED official test  11\nExpected: 47\nActual: " + retValue );
    }
    else
    {
      System.out.println("PASSED official test  11");
    }

    System.out.println();
    fileName = "test_official_12.c";

    eval = "private class test { int reserved; void mainEntry() { if(1 < 2){ reserved = 17; if(2>1){ reserved = 42; } reserved = reserved + 2; } } }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc = Runtime.getRuntime().exec("gcc -g -Wall " + fileName + " -o test");
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 44 )
    {
      System.out.println( "FAILED official test  12\nExpected: 44\nActual: " + retValue );
    }
    else
    {
      System.out.println("PASSED official test  12");
    }

    System.out.println();
    fileName = "test_official_13.c";

    eval = "private class test { int reserved; void mainEntry() { reserved = 12; if(reserved < 22){reserved = 42;} } }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc = Runtime.getRuntime().exec("gcc -g -Wall " + fileName + " -o test");
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 42 )
    {
      System.out.println( "FAILED official test  13\nExpected: 42\nActual: " + retValue );
    }
    else
    {
      System.out.println("PASSED official test  13");
    }

    System.out.println();
    fileName = "test_official_14.c";

    eval = "private class test { int reserved; void mainEntry() { reserved = 12; int loc = 14; if(reserved <= loc-2){reserved = 1;} } }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc = Runtime.getRuntime().exec("gcc -g -Wall " + fileName + " -o test");
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 1 )
    {
      System.out.println( "FAILED official test  14\nExpected: 1\nActual: " + retValue );
    }
    else
    {
      System.out.println("PASSED official test  14");
    }

    System.out.println();
    fileName = "test_official_15.c";

    eval = "private class test { int reserved; void mainEntry() { reserved = 12; if(reserved < 22 && reserved > 9){reserved = 18;} } }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc = Runtime.getRuntime().exec("gcc -g -Wall " + fileName + " -o test");
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 18 )
    {
      System.out.println( "FAILED official test  15\nExpected: 18\nActual: " + retValue );
    }
    else
    {
      System.out.println("PASSED official test  15");
    }

    System.out.println();
    fileName = "test_official_16.c";

    eval = "private class test { int reserved; void mainEntry() { reserved = 12; while(reserved < 22){reserved = reserved+1;} } }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc = Runtime.getRuntime().exec("gcc -g -Wall " + fileName + " -o test");
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 22 )
    {
      System.out.println( "FAILED official test  16\nExpected: 22\nActual: " + retValue );
    }
    else
    {
      System.out.println("PASSED official test  16");
    }

    System.out.println();
    fileName = "test_official_17.c";

    eval = "private class test { int reserved; void mainEntry() { reserved = 12; while(reserved < 22 && reserved != 14){reserved = reserved+1;} } }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc = Runtime.getRuntime().exec("gcc -g -Wall " + fileName + " -o test");
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 14 )
    {
      System.out.println( "FAILED official test  17\nExpected: 14\nActual: " + retValue );
    }
    else
    {
      System.out.println("PASSED official test  17");
    }

    System.out.println();
    fileName = "test_official_18.c";

    eval = "private class test { int reserved; void mainEntry() { reserved = 2; while(reserved > 22 || reserved <= 10){if(reserved < 10){reserved = reserved+1;} reserved = reserved + 1;} reserved = reserved + 9; } }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc = Runtime.getRuntime().exec("gcc -g -Wall " + fileName + " -o test");
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 20 )
    {
      System.out.println( "FAILED official test  18\nExpected: 20\nActual: " + retValue );
    }
    else
    {
      System.out.println("PASSED official test  18");
    }

    System.out.println();
    fileName = "test_official_19.c";

    eval = "private class test { int reserved; void mainEntry() { reserved = 2; while(reserved > 22 || reserved <= 10){if(reserved < 10){reserved = reserved+1;} int loc = 1; reserved = reserved + loc;} } }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc = Runtime.getRuntime().exec("gcc -g -Wall " + fileName + " -o test");
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 11 )
    {
      System.out.println( "FAILED official test  19\nExpected: 11\nActual: " + retValue );
    }
    else
    {
      System.out.println("PASSED official test  19");
    }

    System.out.println();
    fileName = "test_official_20.c";

    eval = "private class test { int reserved; void mainEntry() { reserved = 2; while(reserved > 22 || reserved <= 10){if(reserved < 10 && reserved > 4){reserved = reserved+1;} reserved = reserved + 1;} reserved = reserved + 2;} }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc = Runtime.getRuntime().exec("gcc -g -Wall " + fileName + " -o test");
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 13 )
    {
      System.out.println( "FAILED official test  20\nExpected: 13\nActual: " + retValue );
    }
    else
    {
      System.out.println("PASSED official test  20");
    }

    System.out.println();
    fileName = "test_1.c";

    System.out.println("Congrats: three address generation tests passed! Now make your own test cases "+
                       "(this is only a subset of what we will test your code on)");
    System.out.println("*******************************************");

    eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; if(2 < 3) {reserved = 120;}} }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc = Runtime.getRuntime().exec("gcc -g -Wall " + fileName + " -o test");
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 120 )
    {
      System.out.println( "FAIL\nExpected: 120\nActual: " + retValue );
    }
    else
    {
      System.out.println("Passed test  1");
    }

    System.out.println();
    fileName = "test_2.c";

    eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; if(2 > 3) {reserved = 42;}} }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc = Runtime.getRuntime().exec("gcc -g -Wall " + fileName + " -o test");
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 0 )
    {
      System.out.println( "FAIL\nExpected: 0\nActual: " + retValue );
    }
    else
    {
      System.out.println("Passed test  2");
    }

    System.out.println();
    fileName = "test_3.c";

    eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; if(2 < 3 && 3 < 4) {reserved = 42;}} }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc = Runtime.getRuntime().exec("gcc -g -Wall " + fileName + " -o test");
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 42 )
    {
      System.out.println( "FAIL\nExpected: 42\nActual: " + retValue );
    }
    else
    {
      System.out.println("Passed test  3");
    }

    System.out.println();
    fileName = "test_4.c";

    eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; x = 2+3; if(2 < 3) {reserved = 42;}} }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc = Runtime.getRuntime().exec("gcc -g -Wall " + fileName + " -o test");
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 42 )
    {
      System.out.println( "FAIL\nExpected: 42\nActual: " + retValue );
    }
    else
    {
      System.out.println("Passed test  4");
    }

    System.out.println();
    fileName = "test_5.c";

    eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; x = 2+3; if(x == 5) {reserved = 42;}} }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc = Runtime.getRuntime().exec("gcc -g -Wall " + fileName + " -o test");
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 42 )
    {
      System.out.println( "FAIL\nExpected: 42\nActual: " + retValue );
    }
    else
    {
      System.out.println("Passed test  5");
    }

    System.out.println();
    fileName = "test_6.c";

    eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; int x = 2+3; if(5 == 4) {reserved = 42;}} }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc = Runtime.getRuntime().exec("gcc -g -Wall " + fileName + " -o test");
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 0 )
    {
      System.out.println( "FAIL\nExpected: 0\nActual: " + retValue );
    }
    else
    {
      System.out.println("Passed test  6");
    }

    System.out.println();
    fileName = "test_7.c";

    eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; int x = 2+3; if(5 == 5 && 4 < 3) {reserved = 42;}} }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc = Runtime.getRuntime().exec("gcc -g -Wall " + fileName + " -o test");
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 0 )
    {
      System.out.println( "FAIL\nExpected: 0\nActual: " + retValue );
    }
    else
    {
      System.out.println("Passed test  7");
    }

    System.out.println();
    fileName = "test_8.c";

    eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; int x = 2+3; if(5 == 4 || 4 < 3) {reserved = 42;}} }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc = Runtime.getRuntime().exec("gcc -g -Wall " + fileName + " -o test");
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 0 )
    {
      System.out.println( "FAIL\nExpected: 0\nActual: " + retValue );
    }
    else
    {
      System.out.println("Passed test  8");
    }

    System.out.println();
    fileName = "test_9.c";

    eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; int x = 2+3; if(5 == 4 || 4 > 3) {reserved = 42;}} }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc = Runtime.getRuntime().exec("gcc -g -Wall " + fileName + " -o test");
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 42 )
    {
      System.out.println( "FAIL\nExpected: 42\nActual: " + retValue );
    }
    else
    {
      System.out.println("Passed test  9");
    }

    System.out.println();
    fileName = "test_10.c";

    eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; int x = 2+3; if(5 == 5 && 4 > 3) {reserved = 42;}} }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc = Runtime.getRuntime().exec("gcc -g -Wall " + fileName + " -o test");
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 42 )
    {
      System.out.println( "FAIL\nExpected: 42\nActual: " + retValue );
    }
    else
    {
      System.out.println("Passed test  10");
    }

    System.out.println();
    fileName = "test_11.c";

    eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; int x = 2+3; if(5 == 5) {if (4 != 5) {reserved = 42;}}} }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc = Runtime.getRuntime().exec("gcc -g -Wall " + fileName + " -o test");
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 42 )
    {
      System.out.println( "FAIL\nExpected: 42\nActual: " + retValue );
    }
    else
    {
      System.out.println("Passed test  11");
    }

    System.out.println();
    fileName = "test_12.c";

    eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; int x = 2+3; while(4 == 5) {reserved = 42;}} }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc = Runtime.getRuntime().exec("gcc -g -Wall " + fileName + " -o test");
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 0 )
    {
      System.out.println( "FAIL\nExpected: 0\nActual: " + retValue );
    }
    else
    {
      System.out.println("Passed test  12");
    }

    System.out.println();
    fileName = "test_13.c";

    eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; x = 2+3; if(1 < 2 && 4 > 3) {reserved = x+1;}} }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc = Runtime.getRuntime().exec("gcc -g -Wall " + fileName + " -o test");
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 6 )
    {
      System.out.println( "FAIL\nExpected: 6\nActual: " + retValue );
    }
    else
    {
      System.out.println("Passed test  13");
    }

    System.out.println();
    fileName = "test_14.c";

    eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0;} }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc = Runtime.getRuntime().exec("gcc -g -Wall " + fileName + " -o test");
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 0 )
    {
      System.out.println( "FAIL\nExpected: 0\nActual: " + retValue );
    }
    else
    {
      System.out.println("Passed test  14");
    }

    System.out.println();
    fileName = "test_15.c";

    eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; x = 0; while(x < 2){x = x+1;reserved = x;}} }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 2 )
    {
      System.out.println( "FAIL\nExpected: 2\nActual: " + retValue );
    }
    else
    {
      System.out.println("Passed test  15");
    }

    System.out.println();
    fileName = "test_15_2.c";

    eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; if(2 < 3) {reserved = 42; if(5 > 5) {reserved = 4;}}} }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 42 )
    {
      System.out.println( "FAIL\nExpected: 42\nActual: " + retValue );
    }
    else
    {
      System.out.println("Passed test  15");
    }

    System.out.println();
    fileName = "test_16.c";

    eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; if(2 < 3) {reserved = 42; if(5 < 5) {reserved = 4;}}} }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 42 )
    {
      System.out.println( "FAIL\nExpected: 42\nActual: " + retValue );
    }
    else
    {
      System.out.println("Passed test  16");
    }

    System.out.println();
    fileName = "test_17.c";

    eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; if(2 < 3) {reserved = 42; if(5 != 5) {reserved = 4;}}} }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 42 )
    {
      System.out.println( "FAIL\nExpected: 42\nActual: " + retValue );
    }
    else
    {
      System.out.println("Passed test  17");
    }

    System.out.println();
    fileName = "test_18.c";

    eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; if(2 < 3) {x = 4; if(5 == 5) {reserved = 40; x = 42;} reserved = x;}} }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 4 )
    {
      System.out.println( "FAIL\nExpected: 4\nActual: " + retValue );
    }
    else
    {
      System.out.println("Passed test  18");
    }

    System.out.println();
    fileName = "test_19.c";

    eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; if(2 < 3) {x = 4; if(5 == 5) {reserved = 40; int z = 42; reserved = z;}}} }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 4 )
    {
      System.out.println( "FAIL\nExpected: 4\nActual: " + retValue );
    }
    else
    {
      System.out.println("Passed test  19");
    }

    System.out.println();
    fileName = "test_20.c";

    eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; if(3 >= 3) {reserved = 42;}} }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 42 )
    {
      System.out.println( "FAIL\nExpected: 42\nActual: " + retValue );
    }
    else
    {
      System.out.println("Passed test  20");
    }

    System.out.println();
    fileName = "test_21.c";

    eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; if(2 >= 3) {reserved = 42;}} }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 0 )
    {
      System.out.println( "FAIL\nExpected: 0\nActual: " + retValue );
    }
    else
    {
      System.out.println("Passed test  21");
    }

    System.out.println();
    fileName = "test_22.c";

    eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; if(2 <= 3) {reserved = 42;}} }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 42 )
    {
      System.out.println( "FAIL\nExpected: 42\nActual: " + retValue );
    }
    else
    {
      System.out.println("Passed test  22");
    }

    System.out.println();
    fileName = "test_23.c";

    eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; if(4 <= 3) {reserved = 42;}} }";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    retValue = cmdProc.exitValue();
    if( retValue != 0 )
    {
      System.out.println( "FAIL\nExpected: 0\nActual: " + retValue );
    }
    else
    {
      System.out.println("Passed test  23");
    }

  }

  public static void TestObjectCreation()
  {
    SimpleJava parser = new SimpleJava();

    String eval = "public class test { void main() { int i = 4; } void main2() { int j = 5; } }";
    String result = "";
    if ( ( !parser.getThreeAddr( eval ).equals( result ) ) ) System.out.println("ERROR!!!!!!!!!!!!!!!!!!!!!\n" + parser.getThreeAddr( eval ));

  }

  public static void main(String[] args){
    try {
      TestThreeAddrGen();
    } catch (Exception e){
      e.printStackTrace();
    }
  }

}
