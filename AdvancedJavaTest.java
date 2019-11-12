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

    String  eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; if(2 < 3) {reserved = 42;}} }";
    AdvancedJava parser = new AdvancedJava();
    String fileName = "test.c";
    parser.codeGen(eval, fileName);

    /* Run Shell command */
    Process cmdProc = Runtime.getRuntime().exec("gcc -g -Wall " + fileName + " -o test");
    cmdProc.waitFor();
    consume(cmdProc);
    cmdProc = Runtime.getRuntime().exec("./test");
    cmdProc.waitFor();
    consume(cmdProc);
    int retValue = cmdProc.exitValue();
    if( retValue != 42 )
    {
      System.out.println( "FAIL\nExpected: 42\nActual: " + retValue );
    }

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
