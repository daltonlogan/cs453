public class SimpleJava {

    public String getThreeAddr(String eval){
        EvalParser parser = new EvalParser();
        node root = parser.program( eval );
        return parser.getThreeAddr( root, false );
    }
}