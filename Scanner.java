public class Scanner
{
    enum TokenType
    {
        NUM, PLUS, MINUS, MUL, DIV, LT, LTE, GT, GTE, INVALID
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
            return this.tokenType + ": " + this.tokenVal + " ";
        }
    }

    public Token extractToken( StringBuilder stream )
    {
        char aChar = stream.charAt( 0 );
        TokenType aType;

        if ( Character.isDigit( aChar ) )
        {
            aType = TokenType.NUM;
        }
        else if ( aChar == '+' )
        {
            aType = TokenType.PLUS;
        }
        else if ( aChar == '-' )
        {
            aType = TokenType.MINUS;
        }
        else if ( aChar == '*' )
        {
            aType = TokenType.MUL;
        }
        else if ( aChar == '/' )
        {
            aType = TokenType.DIV;
        }
        else if ( aChar == '<' )
        {
            if ( stream.charAt( 1 ) == '=' )
            {
                aType = TokenType.LTE;
            }
            else
            {
                aType = TokenType.LT;
            }
        }
        else if ( aChar == '>' )
        {
            if ( stream.charAt( 1 ) == '=' )
            {
                aType = TokenType.GTE;
            }
            else
            {
                aType = TokenType.GT;
            }
        }
        else
        {
            aType = TokenType.INVALID;
        }

        String aValue = String.valueOf( aChar );
        Token aToken = new Token( aType, aValue );
        return aToken;
    }

    public String extractTokens( String arg )
    {
    /* TODO #1: Finish this function to iterate over all tokens in the input string.

       Pseudo code:
       String extractTokens(String arg):
         String result= “”;
         while(arg is not empty)
            Token nextToken = extractToken(arg)
            result += nextToken.toString()
         return result
    */
        return null;
    }

}
