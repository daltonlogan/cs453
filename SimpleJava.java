public class SimpleJava {

    public String getThreeAddr(String eval){
        EvalParser parser = new EvalParser();
        node root = parser.program( eval );
        String a = parser.emitTAC( root );
        return a;
    }
}