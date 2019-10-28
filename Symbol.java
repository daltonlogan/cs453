public class Symbol
{
    enum SymbolType
    {
        LEXEME, INT, FUNC
    }
    private SymbolType type;
    private String identifier;
    private String value;
    
    public void setType( SymbolType type )
    {
        this.type = type;
    }
    
    public void setIdentifier( String identifier )
    {
    	this.identifier = identifier;
    }
    
    public void setValue( String value )
    {
    	this.value = value;
    }
    
    public SymbolType getType()
    {
    	return this.type;
    }
    
    public String getIdentifier()
    {
    	return this.identifier;
    }
    
    public String getValue()
    {
    	return this.value;
    }
}
