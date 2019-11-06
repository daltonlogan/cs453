public class AdvancedJava {

    public void codeGen(String eval, String fileName){
        EvalParser parser = new EvalParser();
        parser.program( eval );
    }
}