import java.util.Hashtable;

public class Table
{
    private Hashtable< String, Symbol > table;
    private Table prev;

    public Table( Table p )
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

        for ( Table e = this; e != null; e = e.prev )
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