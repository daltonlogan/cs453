import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AdvancedJava {

    public void codeGen(String eval, String fileName){
        EvalParser parser = new EvalParser();
        node root = parser.program( eval );

        String tacString = parser.getThreeAddr( root, false );

        File file = new File(fileName);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(fileName));
            String header = "#include <stdio.h>\n" +
                    "#include <inttypes.h>\n" +
                    "int main(int argc, char **argv){\n" +
                    "int64_t r1 = 0, r2 = 0, r3 = 0, r4 = 0, r5 = 0;\n" +
                    "int64_t stack[100];\n" +
                    "int64_t *sp = &stack[99];\n" +
                    "int64_t *fp = &stack[99];\n" +
                    "int64_t *ra = &&exit;\n" +
                    "goto mainEntry;\n";
            writer.write( header );
            writer.write( parser.getGlobalVariables() );
            writer.write(parser.getLocalVariables());
            String footer = "exit:\n" +
                    "return reserved;\n" +
                    "}";
            writer.write( footer );

            writer.close();
        } catch ( IOException e) {
            e.printStackTrace();
        }finally{
            //close resources
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}