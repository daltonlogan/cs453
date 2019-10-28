public class Symbol
{
    enum SymbolType
    {
        LEXEME, ID, INT
    }
    private SymbolType type;

    public void setType( SymbolType type )
    {
        this.type = type;
    }
}
