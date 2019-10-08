import java.util.ArrayList;

public class EvalParser
{
    Scanner scan = new Scanner();
    String evalString;

    int tempID = 0;
    String threeAddressResult = "";
    ArrayList< Integer > IDs = new ArrayList< Integer >();
    ArrayList< node > nodes = new ArrayList< node >();
    node root = new node( Scanner.TokenType.PROGRAM );
    private int parenCounter = 0;
    boolean expressionInside;

    int tlabelID = 0; // Label id for true
    int flabelID = 0; // Label id for false
    int rlabelID = 0; // Label id for loops
    private String threeAddress = "";


    /***************** Simple Expression Evaluator ***********************/

    public node program( String eval )
    {
        threeAddressResult = "";
        evalString = eval;
        tempID = 0;
        IDs.clear();

        try
        {
            Scanner.Token nextToken = lookahead();
            match( nextToken, Scanner.TokenType.VOID );
            nextToken = lookahead();
            match( nextToken, Scanner.TokenType.ID );
            nextToken = lookahead();
            match( nextToken, Scanner.TokenType.LEFTPAREN );
            nextToken = lookahead();
            match( nextToken, Scanner.TokenType.RIGHTPAREN );
            nextToken = lookahead();
            match( nextToken, Scanner.TokenType.LEFTCURLY );
            root = stmt_list();
            root.type = Scanner.TokenType.PROGRAM;
            nextToken = lookahead();
            match( nextToken, Scanner.TokenType.RIGHTCURLY );
        } catch ( Exception e )
        {
            e.printStackTrace();
        }

        return root;
    }

    private node stmt_list( )
    {
        node result = new node();
        node stmt = stmt();
        while ( stmt != null )
        {
            result.stmts.add( stmt );
            stmt = stmt();
        }
        return result;
    }

    private node stmt( )
    {
        Scanner.Token nextToken = lookahead();
        if ( nextToken == null )
        {
            return null;
        }

        switch ( nextToken.tokenType )
        {
            case INT:
                return assignment();
            case IF:
            case WHILE:
                return control_flow();
        }
        return null;
    }

    private node assignment( )
    {
        Scanner.Token nextToken = lookahead();
        match( nextToken, Scanner.TokenType.INT );
        node result = A();
        nextToken = lookahead();
        match( nextToken, Scanner.TokenType.ASSIGN );
        node mid = new node( Scanner.TokenType.ASSIGN );
        node right = E();
        mid.left = result;
        mid.right = right;
        result = mid;
        result.loc = mid.left.loc;
        nextToken = lookahead();
        match( nextToken, Scanner.TokenType.SEMICOLON );

        tempID = 0;

        return result;
    }

    private node control_flow( )
    {
        Scanner.Token nextToken = lookahead();
        node result = new node();
        if ( nextToken != null && nextToken.tokenType == Scanner.TokenType.IF )
        {
//            if ( expr ) { stmt_list } |
            match( nextToken, Scanner.TokenType.IF );
            nextToken = lookahead();
            match( nextToken, Scanner.TokenType.LEFTPAREN );
            result = A();
            nextToken = lookahead();
            match( nextToken, Scanner.TokenType.RIGHTPAREN );
            nextToken = lookahead();

            tempID = 0;

            match( nextToken, Scanner.TokenType.LEFTCURLY );

            node mid = new node( Scanner.TokenType.IF );
            node right = stmt_list();
            mid.left = result;
            mid.right = right;
            result = mid;
            result.loc = mid.left.loc;

            nextToken = lookahead();
            match( nextToken, Scanner.TokenType.RIGHTCURLY );


//            threeAddressResult += "falseLabel" + flabelID + "\n";
        }
        else if ( nextToken != null && nextToken.tokenType == Scanner.TokenType.WHILE )
        {
//            while ( expr ) { stmt_list }
            match( nextToken, Scanner.TokenType.WHILE );
            threeAddressResult += "repeatLabel" + rlabelID + "\n";
            nextToken = lookahead();
            match( nextToken, Scanner.TokenType.LEFTPAREN );
            result = A();
            nextToken = lookahead();
            match( nextToken, Scanner.TokenType.RIGHTPAREN );
            nextToken = lookahead();

            tempID = 0;

            match( nextToken, Scanner.TokenType.LEFTCURLY );

            node mid = new node( Scanner.TokenType.WHILE );
            node right = stmt_list();
            mid.left = result;
            mid.right = right;
            result = mid;
            result.loc = mid.left.loc;

            nextToken = lookahead();
            match( nextToken, Scanner.TokenType.RIGHTCURLY );
//            threeAddressResult += "GOTO: repeatLabel" + rlabelID + "\n";
//            threeAddressResult += "falseLabel" + flabelID + "\n";
        }
        else
        {
            return null;
        }
        return result;
    }


    private node A( )
    {

        node result = B();

        while ( lookahead().tokenType == Scanner.TokenType.EQUALS || lookahead().tokenType == Scanner.TokenType.NOTEQUALS )
        {
            Scanner.Token nextToken = lookahead();

            if ( nextToken.tokenType == Scanner.TokenType.EQUALS )
            {
                match( nextToken, Scanner.TokenType.EQUALS );
                node mid = new node( Scanner.TokenType.EQUALS );
                node right = B();
                mid.left = result;
                mid.right = right;
                result = mid;
                result.loc = tempID++;
            }
            else if ( nextToken.tokenType == Scanner.TokenType.NOTEQUALS )
            {
                match( nextToken, Scanner.TokenType.NOTEQUALS );
                node mid = new node( Scanner.TokenType.NOTEQUALS );
                node right = B();
                mid.left = result;
                mid.right = right;
                result = mid;
                result.loc = tempID++;
            }
        }
        return result;
    }

    private node B( )
    {
        node result = E();

        while ( lookahead().tokenType == Scanner.TokenType.LT || lookahead().tokenType == Scanner.TokenType.GT
                || lookahead().tokenType == Scanner.TokenType.LTE || lookahead().tokenType == Scanner.TokenType.GTE )
        {
            Scanner.Token nextToken = lookahead();

            if ( nextToken.tokenType == Scanner.TokenType.LT )
            {
                match( nextToken, Scanner.TokenType.LT );
                node mid = new node( Scanner.TokenType.LT );
                node right = E();
                mid.left = result;
                mid.right = right;
                result = mid;
                result.loc = tempID++;
            }
            else if ( nextToken.tokenType == Scanner.TokenType.GT )
            {
                match( nextToken, Scanner.TokenType.GT );
                node mid = new node( Scanner.TokenType.GT );
                node right = E();
                mid.left = result;
                mid.right = right;
                result = mid;
                result.loc = tempID++;
            }
            else if ( nextToken.tokenType == Scanner.TokenType.LTE )
            {
                match( nextToken, Scanner.TokenType.LTE );
                node mid = new node( Scanner.TokenType.LTE );
                node right = E();
                mid.left = result;
                mid.right = right;
                result = mid;
                result.loc = tempID++;
            }
            else if ( nextToken.tokenType == Scanner.TokenType.GTE )
            {
                match( nextToken, Scanner.TokenType.GTE );
                node mid = new node( Scanner.TokenType.GTE );
                node right = E();
                mid.left = result;
                mid.right = right;
                result = mid;
                result.loc = tempID++;
            }
        }
        return result;
    }

    private node E( )
    {
        node result = T();

        while ( lookahead().tokenType == Scanner.TokenType.PLUS || lookahead().tokenType == Scanner.TokenType.MINUS )
        {
            Scanner.Token nextToken = lookahead();

            if ( nextToken.tokenType == Scanner.TokenType.PLUS )
            {
                match( nextToken, Scanner.TokenType.PLUS );
                node mid = new node( Scanner.TokenType.PLUS );
                node right = T();
                mid.left = result;
                mid.right = right;
                result = mid;
                result.loc = tempID++;
            }
            else if ( nextToken.tokenType == Scanner.TokenType.MINUS )
            {
                match( nextToken, Scanner.TokenType.MINUS );
                node mid = new node( Scanner.TokenType.MINUS );
                node right = T();
                mid.left = result;
                mid.right = right;
                result = mid;
                result.loc = tempID++;
            }
        }
        return result;
    }

    private node T( )
    {
        node result = F();
        while ( lookahead().tokenType == Scanner.TokenType.MUL || lookahead().tokenType == Scanner.TokenType.DIV )
        {
            Scanner.Token nextToken = lookahead();

            if ( nextToken.tokenType == Scanner.TokenType.MUL )
            {
                match( nextToken, Scanner.TokenType.MUL );
                node mid = new node( Scanner.TokenType.MUL );
                node right = T();
                mid.left = result;
                mid.right = right;
                result = mid;
                result.loc = tempID++;
            }
            else if ( nextToken.tokenType == Scanner.TokenType.DIV )
            {
                match( nextToken, Scanner.TokenType.DIV );
                node mid = new node( Scanner.TokenType.DIV );
                node right = T();
                mid.left = result;
                mid.right = right;
                result = mid;
                result.loc = tempID++;
            }
        }
        return result;
    }

    private node F( )
    {
        node aNode;
        try
        {
            Scanner.Token nextToken = lookahead();
            if ( nextToken != null && nextToken.tokenType == Scanner.TokenType.LEFTPAREN )
            {
                parenCounter++;
                match( nextToken, Scanner.TokenType.LEFTPAREN );
                aNode = A();

                nextToken = lookahead();
                if ( nextToken != null && nextToken.tokenType == Scanner.TokenType.RIGHTPAREN )
                {
                    parenCounter--;
                    match( nextToken, Scanner.TokenType.RIGHTPAREN );
                    if ( !expressionInside )
                    {
                        throw new Exception();
                    }
                }
                return aNode;
            }


            if ( nextToken != null && nextToken.tokenType == Scanner.TokenType.NUM )
            {
                match( nextToken, Scanner.TokenType.NUM );
                expressionInside = true;
                aNode = new node( Scanner.TokenType.NUM, nextToken.tokenVal );
                aNode.loc = tempID++;
                return aNode;
            }
            else if ( nextToken != null && nextToken.tokenType == Scanner.TokenType.ID )
            {
                match( nextToken, Scanner.TokenType.ID );
                aNode = new node( Scanner.TokenType.ID, nextToken.tokenVal );
                return aNode;
            }

            if ( parenCounter != 0 )
            {
                throw new Exception();
            }
        } catch ( Exception e )
        {
            System.out.println( "Invalid input" );
            expressionInside = false;
        }
        return null;
    }

    /****************************************/

    public int evaluateExpression( String eval )
    {

        evalString = eval;
        try
        {
            if ( !evalString.isEmpty() )
            {
                throw new Exception();
            }
        } catch ( Exception e )
        {
            System.out.println( "Input string not empty" );
        }
        return 0;
    }

    public String getThreeAddr( String eval )
    {
        this.threeAddressResult = "";
        evalString = eval;
        tempID = 0;
        IDs.clear();
        E();
        return this.threeAddressResult;
    }

    public String emitTAC( node aNode )
    {
        if ( aNode == null )
        {
            return threeAddress;
        }

        if( aNode.type == Scanner.TokenType.WHILE )
        {
            threeAddress += "repeatLabel" + rlabelID + "\n";
        }

        emitTAC( aNode.left );
        emitTAC( aNode.right );

        for ( node theNode : aNode.stmts )
        {
            emitTAC( theNode );
        }

        switch ( aNode.type )
        {
            case NUM:
                threeAddress += "temp" + aNode.loc + " = " + aNode.value + "\n";
                break;
            case ASSIGN:
                threeAddress += aNode.left.value + " = " + "temp" + aNode.right.loc + "\n";
                break;
            case PLUS:
                threeAddress += "temp" + aNode.loc + " = " + "temp" + aNode.left.loc + " + " + "temp" + aNode.right.loc + "\n";
                break;
            case MINUS:
                threeAddress += "temp" + aNode.loc + " = " + "temp" + aNode.left.loc + " - " + "temp" + aNode.right.loc + "\n";
                break;
            case MUL:
                threeAddress += "temp" + aNode.loc + " = " + "temp" + aNode.left.loc + " * " + "temp" + aNode.right.loc + "\n";
                break;
            case DIV:
                threeAddress += "temp" + aNode.loc + " = " + "temp" + aNode.left.loc + " / " + "temp" + aNode.right.loc + "\n";
                break;
            case LT:
                threeAddress += "IF_LT: " + "temp" + aNode.left.loc + ", " +
                        "temp" + aNode.right.loc + ", " + "trueLabel" + tlabelID + "\n";
                threeAddress += "GOTO: falseLabel" + flabelID + "\n";
                threeAddress += "trueLabel" + tlabelID + "\n";
                break;
            case GT:
                threeAddress += "IF_GT: " + "temp" + aNode.left.loc + ", " +
                        "temp" + aNode.right.loc + ", " + "trueLabel" + tlabelID + "\n";
                threeAddress += "GOTO: falseLabel" + flabelID + "\n";
                threeAddress += "trueLabel" + tlabelID + "\n";
                break;
            case LTE:
                threeAddress += "IF_LTE: " + "temp" + aNode.left.loc + ", " +
                        "temp" + aNode.right.loc + ", " + "trueLabel" + tlabelID + "\n";
                threeAddress += "GOTO: falseLabel" + flabelID + "\n";
                threeAddress += "trueLabel" + tlabelID + "\n";
                break;
            case GTE:
                threeAddress += "IF_GTE: " + "temp" + aNode.left.loc + ", " +
                        "temp" + aNode.right.loc + ", " + "trueLabel" + tlabelID + "\n";
                threeAddress += "GOTO: falseLabel" + flabelID + "\n";
                threeAddress += "trueLabel" + tlabelID + "\n";
                break;
            case EQUALS:
                threeAddress += "IF_EQ: " + "temp" + aNode.left.loc + ", " +
                        "temp" + aNode.right.loc + ", " + "trueLabel" + tlabelID + "\n";
                threeAddress += "GOTO: falseLabel" + flabelID + "\n";
                threeAddress += "trueLabel" + tlabelID + "\n";
                break;
            case NOTEQUALS:
                threeAddress += "IF_NE: " + "temp" + aNode.left.loc + ", " +
                        "temp" + aNode.right.loc + ", " + "trueLabel" + tlabelID + "\n";
                threeAddress += "GOTO: falseLabel" + flabelID + "\n";
                threeAddress += "trueLabel" + tlabelID + "\n";
                break;
            case IF:
                threeAddress += "falseLabel" + flabelID + "\n";
                break;
            case WHILE:
                threeAddress += "GOTO: repeatLabel" + rlabelID + "\n" +
                        "falseLabel" + flabelID + "\n";
                break;
        }
        return threeAddress;
    }

    private Scanner.Token lookahead( )
    {
        try
        {
            if ( evalString.isEmpty() )
            {
                return null;
            }
            Scanner.Token aToken = scan.extractToken( new StringBuilder( evalString ) );
            if ( aToken == null )
            {
                throw new Exception();
            }
            else
            {
                return aToken;
            }
        } catch ( Exception e )
        {
            e.printStackTrace();
            System.exit( -1 );
        }
        return null;
    }

    private void match( Scanner.Token aToken, Scanner.TokenType expectedToken )
    {
        try
        {
            if ( aToken == null )
            {
                return;
            }
            while ( Character.isWhitespace( evalString.charAt( 0 ) ) )
            {
                evalString = evalString.substring( 1 );
            }
            if ( aToken.tokenType == expectedToken )
            {
                evalString = evalString.substring( aToken.tokenVal.length() );
            }
            else
            {
                throw new Exception();
            }
        } catch ( Exception e )
        {
            e.printStackTrace();
            System.exit( -1 );
        }
    }

}
