
import java.lang.String;
import java.util.*;

public class EvalParser
{
    Scanner scan = new Scanner();
    String evalString;

    int tempID = 0;
    String threeAddressResult = "";
    ArrayList<Integer> IDs = new ArrayList<Integer>();

    /***************** Three Address Translator ***********************/
    // TODO #2 Continued: Write the functions for E/E', T/T', and F. Return the temporary ID associated with each subexpression and
    //                    build the threeAddressResult string with your three address translation
    /****************************************/

    /***************** Simple Expression Evaluator ***********************/

    private int E( )
    {
        int result = T();
        while ( true )
        {
            Scanner.Token nextToken = lookahead();
            if ( nextToken != null && nextToken.tokenType == Scanner.TokenType.PLUS )
            {
                match( nextToken );
                result += T();
                continue;
            }
            else if ( nextToken != null && nextToken.tokenType == Scanner.TokenType.MINUS )
            {
                match( nextToken );
                result -= T();
                continue;
            }
            return result;
        }
    }

    private int T( )
    {
        int result = F();
        while ( true )
        {
            Scanner.Token nextToken = lookahead();
            if ( nextToken != null && nextToken.tokenType == Scanner.TokenType.MUL )
            {
                match( nextToken );
                result *= F();
                continue;
            }
            else if ( nextToken != null && nextToken.tokenType == Scanner.TokenType.DIV )
            {
                match( nextToken );
                result /= F();
                continue;
            }
            return result;
        }
    }

    private int F( )
    {
        Scanner.Token nextToken = lookahead();
        int result = 0;
        if ( nextToken != null && nextToken.tokenType == Scanner.TokenType.LEFTPAREN )
        {
            match( nextToken );
            nextToken = lookahead();
            while ( nextToken != null && nextToken.tokenType != Scanner.TokenType.RIGHTPAREN )
            {
                result += E();
                nextToken = lookahead();
            }
            match( nextToken );
        }
        else if ( nextToken != null && nextToken.tokenType == Scanner.TokenType.NUM )
        {
            int num = Integer.parseInt( nextToken.tokenVal );
            match( nextToken );
            return num;
        }
        return result;
    }

    /****************************************/

    public int evaluateExpression( String eval )
    {
        evalString = eval;
        return E();
    }

    /* TODO #2: Now add three address translation to your parser*/
    //***
    private int threeE()
    {
        int result = threeT();
        while ( true )
        {
            Scanner.Token nextToken = lookahead();
            if ( nextToken != null && nextToken.tokenType == Scanner.TokenType.PLUS )
            {
                match( nextToken );
                result += threeT();
                int first = IDs.remove(IDs.size()-1);
                int second = IDs.remove(IDs.size()-1);
                IDs.add(tempID);
                threeAddressResult += "temp" + tempID++ + " = " + 
                		"temp" + second + " + " + 
                		"temp" + first + "\n";
                continue;
            }
            else if ( nextToken != null && nextToken.tokenType == Scanner.TokenType.MINUS )
            {
                match( nextToken );
                result -= threeT();
                int first = IDs.remove(IDs.size()-1);
                int second = IDs.remove(IDs.size()-1);
                IDs.add(tempID);
                threeAddressResult += "temp" + tempID++ + " = " + 
                		"temp" + second + " - " + 
                		"temp" + first + "\n";
                continue;
            }
            return result;
        }
    }

    private int threeT()
    {
        int result = threeF();
        while ( true )
        {
            Scanner.Token nextToken = lookahead();
            if ( nextToken != null && nextToken.tokenType == Scanner.TokenType.MUL )
            {
                match( nextToken );
                result *= threeF();
                int first = IDs.remove(IDs.size()-1);
                int second = IDs.remove(IDs.size()-1);
                IDs.add(tempID);
                threeAddressResult += "temp" + tempID++ + " = " + 
                		"temp" + second + " * " + 
                		"temp" + first + "\n";
                continue;
            }
            else if ( nextToken != null && nextToken.tokenType == Scanner.TokenType.DIV )
            {
                match( nextToken );
                result /= threeF();
                int first = IDs.remove(IDs.size()-1);
                int second = IDs.remove(IDs.size()-1);
                IDs.add(tempID);
                threeAddressResult += "temp" + tempID++ + " = " + 
                		"temp" + second + " / " + 
                		"temp" + first + "\n";
                continue;
            }
            return result;
        }
    }

    private int threeF()
    {
        Scanner.Token nextToken = lookahead();
        int result = 0;
        if ( nextToken != null && nextToken.tokenType == Scanner.TokenType.LEFTPAREN )
        {
            match( nextToken );
            nextToken = lookahead();
            while ( nextToken != null && nextToken.tokenType != Scanner.TokenType.RIGHTPAREN )
            {
                result += threeE();
                nextToken = lookahead();
            }
            match( nextToken );
        }
        else if ( nextToken != null && nextToken.tokenType == Scanner.TokenType.NUM )
        {
            int num = Integer.parseInt( nextToken.tokenVal );
            IDs.add(tempID);
            threeAddressResult += "temp" + tempID++ + " = " + num + "\n";
            match( nextToken );
            return num;
        }
        return result;
    }
    //***
    public String getThreeAddr( String eval )
    {
        this.threeAddressResult = "";
        evalString = eval;
        tempID = 0;
        IDs.clear();
        threeE();
        return this.threeAddressResult;
    }

    private Scanner.Token lookahead( )
    {
        if ( evalString.isEmpty() )
        {
            return null;
        }
        return scan.extractToken( new StringBuilder( evalString ) );
    }

    private void match( Scanner.Token aToken )
    {
        if ( aToken == null )
        {
            return;
        }
        while ( Character.isWhitespace( evalString.charAt( 0 ) ) )
        {
            evalString = evalString.substring( 1 );
        }
        evalString = evalString.substring( aToken.tokenVal.length() );
    }

}
