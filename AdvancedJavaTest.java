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

    String eval = "public class test { int x; int y; int reserved; void mainEntry() { reserved = 0; if(2 < 3) {reserved = 42;}} }";
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
    assert(retValue == 42);

    System.out.println("Congrats: three address generation tests passed! Now make your own test cases "+
                       "(this is only a subset of what we will test your code on)");
    System.out.println("*******************************************");
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
