public class Symbol
{
    enum SymbolType
    {
        LEXEME, ID, INT, FUNC
    }
    private SymbolType type;

    public void setType( SymbolType type )
    {
        this.type = type;
    }
}
