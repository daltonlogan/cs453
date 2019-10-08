public class SimpleJava {

    public String getThreeAddr(String eval){
        EvalParser parser = new EvalParser();
        return parser.program(eval);
    }
}