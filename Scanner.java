public class Scanner
{
    enum TokenType
    {
        NUM, PLUS, MINUS, MUL, DIV, LT, LTE, GT, GTE, LEFTPAREN, RIGHTPAREN, INVALID
    }

    class Token
    {
        TokenType tokenType;
        String tokenVal;

        public Token( TokenType tokenType, String tokenVal )
        {
            this.tokenType = tokenType;
            this.tokenVal = tokenVal;
        }

        public String toString( )
        {
            return "|" + this.tokenType + ": " + this.tokenVal + "|";
        }
    }

    public Token extractToken( StringBuilder stream )
    {
        char aChar = stream.charAt( 0 );
        String aValue;
        while ( stream.length() != 0 )
        {
            aChar = stream.charAt( 0 );
            if ( Character.isWhitespace( aChar ) )
            {
                stream.deleteCharAt( 0 );
            }
            else
            {
                break;
            }
        }

        if ( stream.length() == 0 )
        {
            return null;
        }

        TokenType aType;

        if ( Character.isDigit( aChar ) )
        {
            StringBuilder a = new StringBuilder();
            while ( stream.length() != 0 )
            {
                aChar = stream.charAt( 0 );
                if ( Character.isDigit( aChar ) )
                {
                    a.append( aChar );
                    stream.deleteCharAt( 0 );
                }
                else
                {
                    break;
                }
            }
            aValue = a.toString();
            aType = TokenType.NUM;
        }
        else if ( aChar == '+' )
        {
            stream.deleteCharAt( 0 );
            aValue = String.valueOf( aChar );
            aType = TokenType.PLUS;
        }
        else if ( aChar == '-' )
        {
            stream.deleteCharAt( 0 );
            aValue = String.valueOf( aChar );
            aType = TokenType.MINUS;
        }
        else if ( aChar == '*' )
        {
            stream.deleteCharAt( 0 );
            aValue = String.valueOf( aChar );
            aType = TokenType.MUL;
        }
        else if ( aChar == '/' )
        {
            stream.deleteCharAt( 0 );
            aValue = String.valueOf( aChar );
            aType = TokenType.DIV;
        }
        else if ( aChar == '<' )
        {
            aValue = String.valueOf( aChar );
            stream.deleteCharAt( 0 );
            aType = TokenType.LT;

            if ( stream.length() != 0 && stream.charAt( 0 ) == '=' )
            {
                stream.deleteCharAt( 0 );
                aValue += '=';
                aType = TokenType.LTE;
            }
        }
        else if ( aChar == '>' )
        {
            aValue = String.valueOf( aChar );
            stream.deleteCharAt( 0 );
            aType = TokenType.GT;

            if ( stream.length() != 0 && stream.charAt( 0 ) == '=' )
            {
                stream.deleteCharAt( 0 );
                aValue += '=';
                aType = TokenType.GTE;
            }
        }
        else if ( aChar == '(' )
        {
            stream.deleteCharAt( 0 );
            aValue = String.valueOf( aChar );
            aType = TokenType.LEFTPAREN;
        }
        else if ( aChar == ')' )
        {
            stream.deleteCharAt( 0 );
            aValue = String.valueOf( aChar );
            aType = TokenType.RIGHTPAREN;
        }
        else
        {
            stream.deleteCharAt( 0 );
            aValue = String.valueOf( aChar );
            aType = TokenType.INVALID;
        }

        return new Token( aType, aValue );
    }

    public String extractTokens( String arg )
    {
        StringBuilder currentString = new StringBuilder( arg );
        StringBuilder result = new StringBuilder();
        while ( currentString.length() != 0 )
        {
            Token nextToken = extractToken( currentString );
            if ( nextToken != null )
            {
                if ( nextToken.tokenType == TokenType.INVALID )
                {
                    System.out.println( "INVALID TOKEN: " + nextToken.tokenVal );
                    break;
                }
                result.append( nextToken.toString() );
            }
        }
        return result.toString();
    }
}