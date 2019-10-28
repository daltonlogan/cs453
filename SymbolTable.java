import java.util.Hashtable;

public class SymbolTable
{
    private Hashtable< String, Symbol > table;
    private SymbolTable prev;

    public SymbolTable( SymbolTable p )
    {
        table = new Hashtable();
        prev = p;
    }

    public void add( String key, Symbol sym )
    {
        table.put( key, sym );
    }

    public Symbol find( Scanner.Token aToken )
    {
        String key = aToken.tokenVal;

        for ( SymbolTable e = this; e != null; e = e.prev )
        {
            Symbol found = e.table.get( key );
            if ( found != null )
            {
                return found;
            }
        }
        return null;
    }
}