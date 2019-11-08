import java.util.ArrayList;

public class EvalParser
{
    private Scanner scan = new Scanner();
    private String evalString;

    private node root = new node( Scanner.TokenType.PROGRAM );
    private int parenCounter = 0;
    private boolean expressionInside;
    private ArrayList< Tao > threeAddressObjects = new ArrayList<>();
    private ArrayList<Tao> tacObjects = new ArrayList<>(  );
    private ArrayList<codeGenTuple> funcTuples = new ArrayList<>(  );

    private int tempID = 0;   // Label for location
    private int tlabelID = 0; // Label id for true
    private int flabelID = 0; // Label id for false
    private int rlabelID = 0; // Label id for loops

    private Table globalTable = new Table( null );
    private Table localTable = new Table( null );


    /***************** Simple Expression Evaluator ***********************/

    public node program( String eval )
    {
        evalString = eval;
        tempID = 0;

        try
        {
            Scanner.Token nextToken = lookahead();
            if ( nextToken.tokenType == Scanner.TokenType.PUBLIC )
            {
                match( nextToken, Scanner.TokenType.PUBLIC );
            }
            else if ( nextToken.tokenType == Scanner.TokenType.PRIVATE )
            {
                match( nextToken, Scanner.TokenType.PRIVATE );
            }

            nextToken = lookahead();
            match( nextToken, Scanner.TokenType.CLASS );
            nextToken = lookahead();
            match( nextToken, Scanner.TokenType.ID );
            node programNode = new node(  );
            root.type = Scanner.TokenType.PROGRAM;
            node classInit = new node( Scanner.TokenType.ID, nextToken.tokenVal );
            nextToken = lookahead();
            match( nextToken, Scanner.TokenType.LEFTCURLY );
            classInit.stmts.add( prgm_list() );
            programNode.stmts.add( classInit );
            nextToken = lookahead();
            match( nextToken, Scanner.TokenType.RIGHTCURLY );
            root.stmts.add( programNode );
        } catch ( Exception e )
        {
            System.out.println( "ERROR: Syntax error - " + e.getMessage() );
            System.exit( -1 );
        }

        return root;
    }

    private node prgm_list( ) throws Exception
    {
        Scanner.Token nextToken = lookahead();
        if ( nextToken == null )
        {
            return null;
        }

        node result = new node();
        node temp = new node();

        switch ( nextToken.tokenType )
        {
            case INT:
                temp = var_decl( true );
                nextToken = lookahead();
                match( nextToken, Scanner.TokenType.SEMICOLON );
                break;
            case VOID:
                temp = func();
                break;
        }

        while ( temp != null )
        {
            nextToken = lookahead();
            result.stmts.add( temp );

            switch ( nextToken.tokenType )
            {
                case INT:
                    temp = var_decl( true );
                    nextToken = lookahead();
                    match( nextToken, Scanner.TokenType.SEMICOLON );
                    break;
                case VOID:
                    temp = func();
                    break;
                default:
                    temp = null;
                    break;
            }
        }
        return result;
    }

    private node func( ) throws Exception
    {
        Scanner.Token nextToken = lookahead();
        match( nextToken, Scanner.TokenType.VOID );
        nextToken = lookahead();
        match( nextToken, Scanner.TokenType.ID );

        String funcName = nextToken.tokenVal;

        localTable = new Table( null );

        if( globalTable.find( nextToken ) == null )
        {
            Symbol aFunc = new Symbol();
            aFunc.setType( Symbol.SymbolType.FUNC );
            //globalTable.add( nextToken.tokenVal, aFunc );
        }
        else
        {
            System.out.println( "ERROR: Function \'" + nextToken.tokenVal + "\' already defined" );
            //throw new Exception( "Variable " + nextToken.tokenVal + " already defined" );
        }

        nextToken = lookahead();
        match( nextToken, Scanner.TokenType.LEFTPAREN );
        nextToken = lookahead();
        match( nextToken, Scanner.TokenType.RIGHTPAREN );
        nextToken = lookahead();
        match( nextToken, Scanner.TokenType.LEFTCURLY );
        node result = stmt_list();

        generateTACForFunc( result, false );
        ArrayList<Tao> newObjects = new ArrayList<>(  );
        for( Tao t : tacObjects )
        {
            newObjects.add( new Tao( t ) );
        }
        tacObjects.clear();

        codeGenTuple aTuple = new codeGenTuple( newObjects, localTable, funcName );
        funcTuples.add( aTuple );

        nextToken = lookahead();
        match( nextToken, Scanner.TokenType.RIGHTCURLY );

        return result;
    }

    private node var_decl( boolean isGlobal ) throws Exception
    {
        Scanner.Token nextToken = lookahead();
        match( nextToken, Scanner.TokenType.INT );
        nextToken = lookahead();
        match( nextToken, Scanner.TokenType.ID );

        if( isGlobal )
        {
            if( globalTable.find( nextToken ) == null )
            {
                Symbol s = new Symbol();
                s.setType( Symbol.SymbolType.INT );
                globalTable.add( nextToken.tokenVal, s );
            }
            else
            {
                System.out.println( "ERROR: Variable \'" + nextToken.tokenVal + "\' already defined" );
                //throw new Exception( "Variable " + nextToken.tokenVal + " already defined" );
            }
        }
        else
        {
            if( localTable.find( nextToken ) == null )
            {
                Symbol s = new Symbol();
                s.setType( Symbol.SymbolType.INT );
                localTable.add( nextToken.tokenVal, s );
            }
            else
            {
                System.out.println( "ERROR: Variable \'" + nextToken.tokenVal + "\' already defined" );
                //throw new Exception( "Variable " + nextToken.tokenVal + " already defined" );
            }
        }

        node aNode = new node( Scanner.TokenType.ID, nextToken.tokenVal );

        return aNode;
    }

    private node stmt_list( ) throws Exception
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

    private node stmt( ) throws Exception
    {
        Scanner.Token nextToken = lookahead();
        if ( nextToken == null )
        {
            return null;
        }

        switch ( nextToken.tokenType )
        {
            case INT:
            case ID:
                return assignment();
            case IF:
            case WHILE:
                return control_flow();
        }
        return null;
    }

    private node assignment( ) throws Exception
    {
        Scanner.Token nextToken = lookahead();
        node result = new node(  );

        if( nextToken != null && nextToken.tokenType == Scanner.TokenType.INT )
        {
            result = var_decl( false );

            nextToken = lookahead();

            if( nextToken.tokenType == Scanner.TokenType.SEMICOLON )
            {
                match( nextToken, Scanner.TokenType.SEMICOLON );
                return result;
            }

            match( nextToken, Scanner.TokenType.ASSIGN );
            node mid = new node( Scanner.TokenType.ASSIGN );
            node right = E();
            mid.left = result;
            mid.right = right;
            result = mid;
            result.loc = mid.left.loc;
            nextToken = lookahead();
            match( nextToken, Scanner.TokenType.SEMICOLON );

        }
        else if( nextToken != null && nextToken.tokenType == Scanner.TokenType.ID )
        {
            match( nextToken, Scanner.TokenType.ID );

            if( localTable.find( nextToken ) == null )
            {
                if( globalTable.find( nextToken ) == null )
                {
                    System.out.println( "ERROR: Undefined variable: \'" + nextToken.tokenVal + "\'" );
                }
            }

            result = new node( Scanner.TokenType.ID, nextToken.tokenVal );

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
        }

        tempID = 0;

        return result;
    }

    private node control_flow( ) throws Exception
    {
        Scanner.Token nextToken = lookahead();
        node result;
        if ( nextToken != null && nextToken.tokenType == Scanner.TokenType.IF )
        {
            match( nextToken, Scanner.TokenType.IF );
            node mid = new node( Scanner.TokenType.IF );

            // Set labels for IF node
            mid.fLoc = flabelID;
            mid.tLoc = tlabelID;

            nextToken = lookahead();
            match( nextToken, Scanner.TokenType.LEFTPAREN );

            // Evaluate inside expression
            result = boolCompare();
            if( result == null )
            {
                throw new Exception( "No expression inside if" );
            }

            nextToken = lookahead();
            match( nextToken, Scanner.TokenType.RIGHTPAREN );
            nextToken = lookahead();

            // Reset temp ID
            tempID = 0;

            match( nextToken, Scanner.TokenType.LEFTCURLY );

            node right = stmt_list();
            mid.left = result;
            mid.right = right;
            result = mid;

            nextToken = lookahead();
            match( nextToken, Scanner.TokenType.RIGHTCURLY );
        }
        else if ( nextToken != null && nextToken.tokenType == Scanner.TokenType.WHILE )
        {
            match( nextToken, Scanner.TokenType.WHILE );
            nextToken = lookahead();
            match( nextToken, Scanner.TokenType.LEFTPAREN );
            node mid = new node( Scanner.TokenType.WHILE );

            // Set labels for WHILE node
            mid.fLoc = flabelID;
            mid.tLoc = tlabelID;
            mid.rLoc = rlabelID++;

            // Evaluate inside expression
            result = boolCompare();
            if( result == null )
            {
                throw new Exception( "No expression inside while" );
            }
            nextToken = lookahead();
            match( nextToken, Scanner.TokenType.RIGHTPAREN );
            nextToken = lookahead();

            // Reset temp ID
            tempID = 0;

            match( nextToken, Scanner.TokenType.LEFTCURLY );

            node right = stmt_list();
            mid.left = result;
            mid.right = right;
            result = mid;

            nextToken = lookahead();
            match( nextToken, Scanner.TokenType.RIGHTCURLY );
        }
        else
        {
            return null;
        }
        return result;
    }

    private node boolCompare()
    {
        node result = A();

        while ( lookahead().tokenType == Scanner.TokenType.AND || lookahead().tokenType == Scanner.TokenType.OR )
        {
            Scanner.Token nextToken = lookahead();

            if ( nextToken.tokenType == Scanner.TokenType.AND )
            {
                match( nextToken, Scanner.TokenType.AND );
                node mid = new node( Scanner.TokenType.AND );
                mid.left = result;
                mid.right = A();

                // Set children labels
                mid.left.tLoc = mid.right.tLoc;
                mid.left.fLoc = mid.fLoc;
                mid.right.tLoc = mid.tLoc;
                mid.right.fLoc = mid.fLoc;

                // Set AND labels
                result = mid;
                result.fLoc = flabelID;
                result.tLoc = tlabelID;
            }
            else if ( nextToken.tokenType == Scanner.TokenType.OR )
            {
                match( nextToken, Scanner.TokenType.OR );
                node mid = new node( Scanner.TokenType.OR );
                mid.left = result;
                mid.right = A();

                // Set children labels
                mid.left.tLoc = mid.tLoc;
                mid.left.fLoc = mid.right.fLoc;
                mid.right.tLoc = mid.tLoc;
                mid.right.fLoc = mid.fLoc;

                // Set OR labels
                result = mid;
                result.fLoc = flabelID;
                result.tLoc = tlabelID;
            }
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
                result.fLoc = flabelID++;
                result.tLoc = tlabelID++;
            }
            else if ( nextToken.tokenType == Scanner.TokenType.NOTEQUALS )
            {
                match( nextToken, Scanner.TokenType.NOTEQUALS );
                node mid = new node( Scanner.TokenType.NOTEQUALS );
                node right = B();
                mid.left = result;
                mid.right = right;
                result = mid;
                result.fLoc = flabelID++;
                result.tLoc = tlabelID++;
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
                result.fLoc = flabelID++;
                result.tLoc = tlabelID++;
            }
            else if ( nextToken.tokenType == Scanner.TokenType.GT )
            {
                match( nextToken, Scanner.TokenType.GT );
                node mid = new node( Scanner.TokenType.GT );
                node right = E();
                mid.left = result;
                mid.right = right;
                result = mid;
                result.fLoc = flabelID++;
                result.tLoc = tlabelID++;
            }
            else if ( nextToken.tokenType == Scanner.TokenType.LTE )
            {
                match( nextToken, Scanner.TokenType.LTE );
                node mid = new node( Scanner.TokenType.LTE );
                node right = E();
                mid.left = result;
                mid.right = right;
                result = mid;
                result.fLoc = flabelID++;
                result.tLoc = tlabelID++;
            }
            else if ( nextToken.tokenType == Scanner.TokenType.GTE )
            {
                match( nextToken, Scanner.TokenType.GTE );
                node mid = new node( Scanner.TokenType.GTE );
                node right = E();
                mid.left = result;
                mid.right = right;
                result = mid;
                result.fLoc = flabelID++;
                result.tLoc = tlabelID++;
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

                Symbol s = new Symbol();
                s.setType( Symbol.SymbolType.INT );
                localTable.add( "temp" + mid.left.loc, s );

                Symbol s2 = new Symbol();
                s2.setType( Symbol.SymbolType.INT );
                localTable.add( "temp" + mid.right.loc, s2 );

                result = mid;
                result.loc = tempID++;

                Symbol s3 = new Symbol();
                s3.setType( Symbol.SymbolType.INT );
                localTable.add( "temp" + result.loc, s3 );
            }
            else if ( nextToken.tokenType == Scanner.TokenType.MINUS )
            {
                match( nextToken, Scanner.TokenType.MINUS );
                node mid = new node( Scanner.TokenType.MINUS );
                node right = T();
                mid.left = result;
                mid.right = right;

                Symbol s = new Symbol();
                s.setType( Symbol.SymbolType.INT );
                localTable.add( "temp" + mid.left.loc, s );

                Symbol s2 = new Symbol();
                s2.setType( Symbol.SymbolType.INT );
                localTable.add( "temp" + mid.right.loc, s2 );


                result = mid;
                result.loc = tempID++;

                Symbol s3 = new Symbol();
                s3.setType( Symbol.SymbolType.INT );
                localTable.add( "temp" + result.loc, s3 );
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
                node right = F();
                mid.left = result;
                mid.right = right;

                Symbol s = new Symbol();
                s.setType( Symbol.SymbolType.INT );
                localTable.add( "temp" + mid.left.loc, s );

                Symbol s2 = new Symbol();
                s2.setType( Symbol.SymbolType.INT );
                localTable.add( "temp" + mid.right.loc, s2 );

                result = mid;
                result.loc = tempID++;

                Symbol s3 = new Symbol();
                s3.setType( Symbol.SymbolType.INT );
                localTable.add( "temp" + result.loc, s3 );
            }
            else if ( nextToken.tokenType == Scanner.TokenType.DIV )
            {
                match( nextToken, Scanner.TokenType.DIV );
                node mid = new node( Scanner.TokenType.DIV );
                node right = F();
                mid.left = result;
                mid.right = right;

                Symbol s = new Symbol();
                s.setType( Symbol.SymbolType.INT );
                localTable.add( "temp" + mid.left.loc, s );

                Symbol s2 = new Symbol();
                s2.setType( Symbol.SymbolType.INT );
                localTable.add( "temp" + mid.right.loc, s2 );

                result = mid;
                result.loc = tempID++;

                Symbol s3 = new Symbol();
                s3.setType( Symbol.SymbolType.INT );
                localTable.add( "temp" + result.loc, s3 );
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
                aNode = boolCompare();

                nextToken = lookahead();
                if ( nextToken != null && nextToken.tokenType == Scanner.TokenType.RIGHTPAREN )
                {
                    parenCounter--;
                    match( nextToken, Scanner.TokenType.RIGHTPAREN );
                    if ( !expressionInside )
                    {
                        throw new Exception( "No expression inside parentheses" );
                    }
                }
                return aNode;
            }

            if ( nextToken != null && nextToken.tokenType == Scanner.TokenType.NUM )
            {
                match( nextToken, Scanner.TokenType.NUM );
                expressionInside = true;
                aNode = new node( Scanner.TokenType.NUM, nextToken.tokenVal );

                Symbol s = new Symbol();
                s.setType( Symbol.SymbolType.INT );
                localTable.add( "temp" + aNode.loc, s );

                aNode.loc = tempID++;
                return aNode;
            }
            else if ( nextToken != null && nextToken.tokenType == Scanner.TokenType.ID )
            {
                match( nextToken, Scanner.TokenType.ID );

                if( localTable.find( nextToken ) == null )
                {
                    if ( globalTable.find( nextToken ) == null )
                    {
                        System.out.println( "ERROR: Undefined variable: \'" + nextToken.tokenVal + "\'" );
                    }
                }

                aNode = new node( Scanner.TokenType.ID, nextToken.tokenVal );
                return aNode;
            }

            if ( parenCounter != 0 )
            {
                throw new Exception( "Parenthesis count wrong" );
            }
        } catch ( Exception e )
        {
            System.out.println( "ERROR: Syntax error - " + e.getMessage() );
            expressionInside = false;
            System.exit( -1 );
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

    public String getThreeAddr( node aNode, boolean isOR )
    {
        generateTAC( aNode, isOR );
        StringBuilder theFinishedThreeAddress = new StringBuilder();
        for( Tao aTao : threeAddressObjects )
        {
            theFinishedThreeAddress.append( aTao.toString() );
        }

        for( codeGenTuple aTuple : funcTuples )
        {
            int aOffset = 1;
            for( Symbol aSymbol : aTuple.getTheTable().getTable().values() )
            {
                aSymbol.setOffset( aOffset++ );
            }
            aTuple.setStackSize( aTuple.getTheTable().getTable().values().size() * 4 );
        }

        return theFinishedThreeAddress.toString();
    }

    public String getGlobalVariables()
    {
        StringBuilder theGlobals = new StringBuilder("int64_t ");
        int count = 0;
        for ( String aKey : globalTable.getTable().keySet() )
        {
            theGlobals.append( aKey );
            theGlobals.append( " = 0" );
            if( count != globalTable.getTable().size() - 1 )
            {
                theGlobals.append( ", " );
            }
            count++;
        }
        theGlobals.append( ";\n" );
        return theGlobals.toString();
    }

    public String getLocalVariables()
    {
        StringBuilder theLocals = new StringBuilder();
        for( codeGenTuple aTuple : funcTuples )
        {
            theLocals.append( aTuple.getTheName() ).append( ":\n" );
            theLocals.append( "sp = sp - 2;\n" +
                    "*(sp+2) = fp;\n" +
                    "*(sp+1) = ra;\n" +
                    "fp = sp;\n" +
                    "sp = sp - " + aTuple.getTheTable().getTable().size() + ";\n" );

            for( Tao aTao : aTuple.getTheThreeAddressList() )
            {
                switch ( aTao.op )
                {
                    case NUM:
                        theLocals.append( "r1 = " + aTao.src1 + ";\n" );
                        theLocals.append( "*(fp-" + aTao.destination + ".offset) = r1;\n" );
                        break;
                    case PLUS:
                        theLocals.append( "r1 = *(fp-" + aTao.src1 + ".offset);\n" );
                        theLocals.append( "r2 = *(fp-" + aTao.src2 + ".offset);\n" );
                        theLocals.append( "r3 = r1 + r2;\n" );
                        theLocals.append( "*(fp-" + aTao.destination + ".offset) = r3;\n" );
                        break;
                    case MINUS:
                        theLocals.append( "r1 = *(fp-" + aTao.src1 + ".offset);\n" );
                        theLocals.append( "r2 = *(fp-" + aTao.src2 + ".offset);\n" );
                        theLocals.append( "r3 = r1 - r2;\n" );
                        theLocals.append( "*(fp-" + aTao.destination + ".offset) = r3;\n" );
                        break;
                    case MUL:
                        theLocals.append( "r1 = *(fp-" + aTao.src1 + ".offset);\n" );
                        theLocals.append( "r2 = *(fp-" + aTao.src2 + ".offset);\n" );
                        theLocals.append( "r3 = r1 * r2;\n" );
                        theLocals.append( "*(fp-" + aTao.destination + ".offset) = r3;\n" );
                        break;
                    case DIV:
                        theLocals.append( "r1 = *(fp-" + aTao.src1 + ".offset);\n" );
                        theLocals.append( "r2 = *(fp-" + aTao.src2 + ".offset);\n" );
                        theLocals.append( "r3 = r1 / r2;\n" );
                        theLocals.append( "*(fp-" + aTao.destination + ".offset) = r3;\n" );
                        break;
                    case LT:
                        theLocals.append( "r1 = *(fp-" + aTao.src1 + ".offset);\n" );
                        theLocals.append( "r2 = *(fp-" + aTao.src2 + ".offset);\n" );
                        theLocals.append( "if(r1 < r2) goto truelabel" + aTao.destination + ";\n" );
                        break;
                    case GT:
                        theLocals.append( "r1 = *(fp-" + aTao.src1 + ".offset);\n" );
                        theLocals.append( "r2 = *(fp-" + aTao.src2 + ".offset);\n" );
                        theLocals.append( "if(r1 > r2) goto truelabel" + aTao.destination + ";\n" );
                        break;
                    case LTE:
                        theLocals.append( "r1 = *(fp-" + aTao.src1 + ".offset);\n" );
                        theLocals.append( "r2 = *(fp-" + aTao.src2 + ".offset);\n" );
                        theLocals.append( "if(r1 <= r2) goto truelabel" + aTao.destination + ";\n" );
                        break;
                    case GTE:
                        theLocals.append( "r1 = *(fp-" + aTao.src1 + ".offset);\n" );
                        theLocals.append( "r2 = *(fp-" + aTao.src2 + ".offset);\n" );
                        theLocals.append( "if(r1 >= r2) goto truelabel" + aTao.destination + ";\n" );
                        break;
                    case EQUALS:
                        theLocals.append( "r1 = *(fp-" + aTao.src1 + ".offset);\n" );
                        theLocals.append( "r2 = *(fp-" + aTao.src2 + ".offset);\n" );
                        theLocals.append( "if(r1 == r2) goto truelabel" + aTao.destination + ";\n" );
                        break;
                    case NOTEQUALS:
                        theLocals.append( "r1 = *(fp-" + aTao.src1 + ".offset);\n" );
                        theLocals.append( "r2 = *(fp-" + aTao.src2 + ".offset);\n" );
                        theLocals.append( "if(r1 != r2) goto truelabel" + aTao.destination + ";\n" );
                        break;
                    case GOTO:
                        theLocals.append( "goto falselabel" + aTao.destination + ";\n" );
                        break;
                }
            }

            theLocals.append( "sp = sp + " + aTuple.getTheTable().getTable().size() + ";\n" +
                    "fp = *(sp+2);\n" +
                    "ra = *(sp+1);\n" +
                    "sp = sp + 2;\n" +
                    "goto *ra;\n" );
        }
        return theLocals.toString();
    }

    private void generateTAC( node aNode, boolean isOR )
    {
        try
        {
            if ( aNode == null )
            {
                return;
            }

            if ( aNode.type == Scanner.TokenType.WHILE )
            {
                //threeAddress += "repeatLabel" + aNode.rLoc + "\n";
                Operand start = new Operand( aNode.rLoc );
                Tao startObject = new Tao( Tao.Operation.START_WHILE, start );
                threeAddressObjects.add( startObject );
            }

            if( aNode.type == Scanner.TokenType.OR )
            {
                isOR = true;
            }

            generateTAC( aNode.left, isOR );

            if( aNode.type == Scanner.TokenType.OR )
            {
                isOR = false;
            }

            generateTAC( aNode.right, isOR );


            for ( node theNode : aNode.stmts )
            {
                generateTAC( theNode, isOR );
            }

            switch ( aNode.type )
            {
                case NUM:
                    //threeAddress += "temp" + aNode.loc + " = " + aNode.value + "\n";
                    Operand num_src1 = new Operand( aNode.value );
                    Operand num_dest = new Operand( printIdOrLoc( aNode ) );
                    Tao numObject = new Tao( Tao.Operation.NUM, num_src1, num_dest );
                    threeAddressObjects.add( numObject );
                    break;
                case ASSIGN:
                    //threeAddress += printIdOrLoc( aNode.left ) + " = " + printIdOrLoc( aNode.right ) + "\n";
                    Operand assign_src1 = new Operand( printIdOrLoc( aNode.right ) );
                    Operand assign_dest = new Operand( printIdOrLoc( aNode.left ) );
                    Tao assignObject = new Tao( Tao.Operation.ASSIGN, assign_src1, assign_dest );
                    threeAddressObjects.add( assignObject );
                    break;
                case PLUS:
                    //threeAddress += "temp" + aNode.loc + " = " + printIdOrLoc( aNode.left ) + " + " + printIdOrLoc( aNode.right ) + "\n";
                    Operand plus_src1 = new Operand( printIdOrLoc( aNode.left ) );
                    Operand plus_src2 = new Operand( printIdOrLoc( aNode.right ) );
                    Operand plus_dest = new Operand( printIdOrLoc( aNode ) );
                    Tao plusObject = new Tao( Tao.Operation.PLUS, plus_src1, plus_src2, plus_dest );
                    threeAddressObjects.add( plusObject );
                    break;
                case MINUS:
                    //threeAddress += "temp" + aNode.loc + " = " + printIdOrLoc( aNode.left ) + " - " + printIdOrLoc( aNode.right ) + "\n";
                    Operand minus_src1 = new Operand( printIdOrLoc( aNode.left ) );
                    Operand minus_src2 = new Operand( printIdOrLoc( aNode.right ) );
                    Operand minus_dest = new Operand( printIdOrLoc( aNode ) );
                    Tao minusObject = new Tao( Tao.Operation.MINUS, minus_src1, minus_src2, minus_dest );
                    threeAddressObjects.add( minusObject );
                    break;
                case MUL:
                    //threeAddress += "temp" + aNode.loc + " = " + printIdOrLoc( aNode.left ) + " * " + printIdOrLoc( aNode.right ) + "\n";
                    Operand mul_src1 = new Operand( printIdOrLoc( aNode.left ) );
                    Operand mul_src2 = new Operand( printIdOrLoc( aNode.right ) );
                    Operand mul_dest = new Operand( printIdOrLoc( aNode ) );
                    Tao mulObject = new Tao( Tao.Operation.MUL, mul_src1, mul_src2, mul_dest );
                    threeAddressObjects.add( mulObject );
                    break;
                case DIV:
                    //threeAddress += "temp" + aNode.loc + " = " + printIdOrLoc( aNode.left ) + " / " + printIdOrLoc( aNode.right ) + "\n";
                    Operand div_src1 = new Operand( printIdOrLoc( aNode.left ) );
                    Operand div_src2 = new Operand( printIdOrLoc( aNode.right ) );
                    Operand div_dest = new Operand( printIdOrLoc( aNode ) );
                    Tao divObject = new Tao( Tao.Operation.DIV, div_src1, div_src2, div_dest );
                    threeAddressObjects.add( divObject );
                    break;
                case LT:
                    //threeAddress += "IF_LT: " + printIdOrLoc( aNode.left ) + ", " +
                            //printIdOrLoc( aNode.right ) + ", " + "trueLabel" + aNode.tLoc + "\n";
                    Operand lt_src1 = new Operand( printIdOrLoc( aNode.left ) );
                    Operand lt_src2 = new Operand( printIdOrLoc( aNode.right ) );
                    Operand lt_dest = new Operand( aNode.tLoc );
                    Tao ltObject = new Tao( Tao.Operation.LT, lt_src1, lt_src2, lt_dest );
                    threeAddressObjects.add( ltObject );


                    //threeAddress += "GOTO: falseLabel" + aNode.fLoc + "\n";

                    Operand lt_goto = new Operand( aNode.fLoc );
                    Tao ltGotoObject = new Tao( Tao.Operation.GOTO, lt_goto );
                    threeAddressObjects.add( ltGotoObject );

                    if( isOR )
                    {
                        //threeAddress += "falseLabel" + aNode.fLoc + "\n";
                        Operand lt_falseLabel = new Operand( "falseLabel" + aNode.fLoc );
                        Tao ltFalseObject = new Tao( Tao.Operation.LABEL, lt_falseLabel );
                        threeAddressObjects.add( ltFalseObject );
                    }
                    else
                    {
                        //threeAddress += "trueLabel" + aNode.tLoc + "\n";
                        Operand lt_trueLabel = new Operand( "trueLabel" + aNode.tLoc );
                        Tao ltTrueObject = new Tao( Tao.Operation.LABEL, lt_trueLabel );
                        threeAddressObjects.add( ltTrueObject );
                    }
                    break;
                case GT:
                    //threeAddress += "IF_GT: " + printIdOrLoc( aNode.left ) + ", " +
                            //printIdOrLoc( aNode.right ) + ", " + "trueLabel" + aNode.tLoc + "\n";
                    Operand gt_src1 = new Operand( printIdOrLoc( aNode.left ) );
                    Operand gt_src2 = new Operand( printIdOrLoc( aNode.right ) );
                    Operand gt_dest = new Operand( aNode.tLoc );
                    Tao gtObject = new Tao( Tao.Operation.GT, gt_src1, gt_src2, gt_dest );
                    threeAddressObjects.add( gtObject );


                    //threeAddress += "GOTO: falseLabel" + aNode.fLoc + "\n";
                    Operand gt_goto = new Operand( aNode.fLoc );
                    Tao gtGotoObject = new Tao( Tao.Operation.GOTO, gt_goto );
                    threeAddressObjects.add( gtGotoObject );

                    if( isOR )
                    {
                        //threeAddress += "falseLabel" + aNode.fLoc + "\n";
                        Operand gt_falseLabel = new Operand( "falseLabel" + aNode.fLoc );
                        Tao gtFalseObject = new Tao( Tao.Operation.LABEL, gt_falseLabel );
                        threeAddressObjects.add( gtFalseObject );
                    }
                    else
                    {
                        //threeAddress += "trueLabel" + aNode.tLoc + "\n";
                        Operand gt_trueLabel = new Operand( "trueLabel" + aNode.tLoc );
                        Tao gtTrueObject = new Tao( Tao.Operation.LABEL, gt_trueLabel );
                        threeAddressObjects.add( gtTrueObject );
                    }
                    break;
                case LTE:
                    //threeAddress += "IF_LTE: " + printIdOrLoc( aNode.left ) + ", " +
                            //printIdOrLoc( aNode.right ) + ", " + "trueLabel" + aNode.tLoc + "\n";
                    Operand lte_src1 = new Operand( printIdOrLoc( aNode.left ) );
                    Operand lte_src2 = new Operand( printIdOrLoc( aNode.right ) );
                    Operand lte_dest = new Operand( aNode.tLoc );
                    Tao lteObject = new Tao( Tao.Operation.LTE, lte_src1, lte_src2, lte_dest );
                    threeAddressObjects.add( lteObject );


                    //threeAddress += "GOTO: falseLabel" + aNode.fLoc + "\n";
                    Operand lte_goto = new Operand( aNode.fLoc );
                    Tao lteGotoObject = new Tao( Tao.Operation.GOTO, lte_goto );
                    threeAddressObjects.add( lteGotoObject );


                    if( isOR )
                    {
                        //threeAddress += "falseLabel" + aNode.fLoc + "\n";
                        Operand lte_falseLabel = new Operand( "falseLabel" + aNode.fLoc );
                        Tao lteFalseObject = new Tao( Tao.Operation.LABEL, lte_falseLabel );
                        threeAddressObjects.add( lteFalseObject );
                    }
                    else
                    {
                        //threeAddress += "trueLabel" + aNode.tLoc + "\n";
                        Operand lte_trueLabel = new Operand( "trueLabel" + aNode.tLoc );
                        Tao lteTrueObject = new Tao( Tao.Operation.LABEL, lte_trueLabel );
                        threeAddressObjects.add( lteTrueObject );
                    }
                    break;
                case GTE:
                    //threeAddress += "IF_GTE: " + printIdOrLoc( aNode.left ) + ", " +
                            //printIdOrLoc( aNode.right ) + ", " + "trueLabel" + aNode.tLoc + "\n";
                    Operand gte_src1 = new Operand( printIdOrLoc( aNode.left ) );
                    Operand gte_src2 = new Operand( printIdOrLoc( aNode.right ) );
                    Operand gte_dest = new Operand( aNode.tLoc );
                    Tao gteObject = new Tao( Tao.Operation.GTE, gte_src1, gte_src2, gte_dest );
                    threeAddressObjects.add( gteObject );


                    //threeAddress += "GOTO: falseLabel" + aNode.fLoc + "\n";
                    Operand gte_goto = new Operand( aNode.fLoc );
                    Tao gteGotoObject = new Tao( Tao.Operation.GOTO, gte_goto );
                    threeAddressObjects.add( gteGotoObject );


                    if( isOR )
                    {
                        //threeAddress += "falseLabel" + aNode.fLoc + "\n";
                        Operand gte_falseLabel = new Operand( "falseLabel" + aNode.fLoc );
                        Tao gteFalseObject = new Tao( Tao.Operation.LABEL, gte_falseLabel );
                        threeAddressObjects.add( gteFalseObject );
                    }
                    else
                    {
                        //threeAddress += "trueLabel" + aNode.tLoc + "\n";
                        Operand gte_trueLabel = new Operand( "trueLabel" + aNode.tLoc );
                        Tao gteTrueObject = new Tao( Tao.Operation.LABEL, gte_trueLabel );
                        threeAddressObjects.add( gteTrueObject );
                    }
                    break;
                case EQUALS:
                    //threeAddress += "IF_EQ: " + printIdOrLoc( aNode.left ) + ", " +
                            //printIdOrLoc( aNode.right ) + ", " + "trueLabel" + aNode.tLoc + "\n";
                    Operand equals_src1 = new Operand( printIdOrLoc( aNode.left ) );
                    Operand equals_src2 = new Operand( printIdOrLoc( aNode.right ) );
                    Operand equals_dest = new Operand( aNode.tLoc );
                    Tao equalsObject = new Tao( Tao.Operation.EQUALS, equals_src1, equals_src2, equals_dest );
                    threeAddressObjects.add( equalsObject );


                    //threeAddress += "GOTO: falseLabel" + aNode.fLoc + "\n";
                    Operand equals_goto = new Operand( aNode.fLoc );
                    Tao equalsGotoObject = new Tao( Tao.Operation.GOTO, equals_goto );
                    threeAddressObjects.add( equalsGotoObject );


                    if( isOR )
                    {
                        //threeAddress += "falseLabel" + aNode.fLoc + "\n";
                        Operand equals_falseLabel = new Operand( "falseLabel" + aNode.fLoc );
                        Tao equalsFalseObject = new Tao( Tao.Operation.LABEL, equals_falseLabel );
                        threeAddressObjects.add( equalsFalseObject );
                    }
                    else
                    {
                        //threeAddress += "trueLabel" + aNode.tLoc + "\n";
                        Operand equals_trueLabel = new Operand( "trueLabel" + aNode.tLoc );
                        Tao equalsTrueObject = new Tao( Tao.Operation.LABEL, equals_trueLabel );
                        threeAddressObjects.add( equalsTrueObject );
                    }
                    break;
                case NOTEQUALS:
                    //threeAddress += "IF_NE: " + printIdOrLoc( aNode.left ) + ", " +
                            //printIdOrLoc( aNode.right ) + ", " + "trueLabel" + aNode.tLoc + "\n";
                    Operand not_equals_src1 = new Operand( printIdOrLoc( aNode.left ) );
                    Operand not_equals_src2 = new Operand( printIdOrLoc( aNode.right ) );
                    Operand not_equals_dest = new Operand( aNode.tLoc );
                    Tao notEqualsObject = new Tao( Tao.Operation.NOTEQUALS, not_equals_src1, not_equals_src2, not_equals_dest );
                    threeAddressObjects.add( notEqualsObject );


                    //threeAddress += "GOTO: falseLabel" + aNode.fLoc + "\n";
                    Operand not_equals_goto = new Operand( aNode.fLoc );
                    Tao notEqualsGotoObject = new Tao( Tao.Operation.GOTO, not_equals_goto );
                    threeAddressObjects.add( notEqualsGotoObject );

                    if( isOR )
                    {
                        //threeAddress += "falseLabel" + aNode.fLoc + "\n";
                        Operand not_equals_falseLabel = new Operand( "falseLabel" + aNode.fLoc );
                        Tao notEqualsFalseObject = new Tao( Tao.Operation.LABEL, not_equals_falseLabel );
                        threeAddressObjects.add( notEqualsFalseObject );
                    }
                    else
                    {
                        //threeAddress += "trueLabel" + aNode.tLoc + "\n";
                        Operand not_equals_trueLabel = new Operand( "trueLabel" + aNode.tLoc );
                        Tao notEqualsTrueObject = new Tao( Tao.Operation.LABEL, not_equals_trueLabel );
                        threeAddressObjects.add( notEqualsTrueObject );
                    }
                    break;
                case IF:
                    //threeAddress += "falseLabel" + aNode.fLoc + "\n";
                    Operand if_dest = new Operand( aNode.fLoc );
                    Tao ifObject = new Tao( Tao.Operation.IF, if_dest );
                    threeAddressObjects.add( ifObject );
                    break;
                case WHILE:
                    //threeAddress += "GOTO: repeatLabel" + aNode.rLoc + "\n" +
                            //"falseLabel" + aNode.fLoc + "\n";
                    Operand while_src1 = new Operand( aNode.rLoc );
                    Operand while_dest = new Operand( aNode.fLoc );
                    Tao whileObject = new Tao( Tao.Operation.WHILE, while_src1, while_dest );
                    threeAddressObjects.add( whileObject );
                    break;
            }
        }
        catch ( Exception e )
        {
            System.out.println( "ERROR: Syntax error" );
            System.exit( -1 );
        }
    }

    private void generateTACForFunc( node aNode, boolean isOR )
    {
        try
        {
            if ( aNode == null )
            {
                return;
            }

            if ( aNode.type == Scanner.TokenType.WHILE )
            {
                //threeAddress += "repeatLabel" + aNode.rLoc + "\n";
                Operand start = new Operand( aNode.rLoc );
                Tao startObject = new Tao( Tao.Operation.START_WHILE, start );
                tacObjects.add( startObject );
            }

            if( aNode.type == Scanner.TokenType.OR )
            {
                isOR = true;
            }

            generateTACForFunc( aNode.left, isOR );

            if( aNode.type == Scanner.TokenType.OR )
            {
                isOR = false;
            }

            generateTACForFunc( aNode.right, isOR );


            for ( node theNode : aNode.stmts )
            {
                generateTACForFunc( theNode, isOR );
            }

            switch ( aNode.type )
            {
                case NUM:
                    //threeAddress += "temp" + aNode.loc + " = " + aNode.value + "\n";
                    Operand num_src1 = new Operand( aNode.value );
                    Operand num_dest = new Operand( printIdOrLoc( aNode ) );
                    Tao numObject = new Tao( Tao.Operation.NUM, num_src1, num_dest );
                    tacObjects.add( numObject );
                    break;
                case ASSIGN:
                    //threeAddress += printIdOrLoc( aNode.left ) + " = " + printIdOrLoc( aNode.right ) + "\n";
                    Operand assign_src1 = new Operand( printIdOrLoc( aNode.right ) );
                    Operand assign_dest = new Operand( printIdOrLoc( aNode.left ) );
                    Tao assignObject = new Tao( Tao.Operation.ASSIGN, assign_src1, assign_dest );
                    tacObjects.add( assignObject );
                    break;
                case PLUS:
                    //threeAddress += "temp" + aNode.loc + " = " + printIdOrLoc( aNode.left ) + " + " + printIdOrLoc( aNode.right ) + "\n";
                    Operand plus_src1 = new Operand( printIdOrLoc( aNode.left ) );
                    Operand plus_src2 = new Operand( printIdOrLoc( aNode.right ) );
                    Operand plus_dest = new Operand( printIdOrLoc( aNode ) );
                    Tao plusObject = new Tao( Tao.Operation.PLUS, plus_src1, plus_src2, plus_dest );
                    tacObjects.add( plusObject );
                    break;
                case MINUS:
                    //threeAddress += "temp" + aNode.loc + " = " + printIdOrLoc( aNode.left ) + " - " + printIdOrLoc( aNode.right ) + "\n";
                    Operand minus_src1 = new Operand( printIdOrLoc( aNode.left ) );
                    Operand minus_src2 = new Operand( printIdOrLoc( aNode.right ) );
                    Operand minus_dest = new Operand( printIdOrLoc( aNode ) );
                    Tao minusObject = new Tao( Tao.Operation.MINUS, minus_src1, minus_src2, minus_dest );
                    tacObjects.add( minusObject );
                    break;
                case MUL:
                    //threeAddress += "temp" + aNode.loc + " = " + printIdOrLoc( aNode.left ) + " * " + printIdOrLoc( aNode.right ) + "\n";
                    Operand mul_src1 = new Operand( printIdOrLoc( aNode.left ) );
                    Operand mul_src2 = new Operand( printIdOrLoc( aNode.right ) );
                    Operand mul_dest = new Operand( printIdOrLoc( aNode ) );
                    Tao mulObject = new Tao( Tao.Operation.MUL, mul_src1, mul_src2, mul_dest );
                    tacObjects.add( mulObject );
                    break;
                case DIV:
                    //threeAddress += "temp" + aNode.loc + " = " + printIdOrLoc( aNode.left ) + " / " + printIdOrLoc( aNode.right ) + "\n";
                    Operand div_src1 = new Operand( printIdOrLoc( aNode.left ) );
                    Operand div_src2 = new Operand( printIdOrLoc( aNode.right ) );
                    Operand div_dest = new Operand( printIdOrLoc( aNode ) );
                    Tao divObject = new Tao( Tao.Operation.DIV, div_src1, div_src2, div_dest );
                    tacObjects.add( divObject );
                    break;
                case LT:
                    //threeAddress += "IF_LT: " + printIdOrLoc( aNode.left ) + ", " +
                    //printIdOrLoc( aNode.right ) + ", " + "trueLabel" + aNode.tLoc + "\n";
                    Operand lt_src1 = new Operand( printIdOrLoc( aNode.left ) );
                    Operand lt_src2 = new Operand( printIdOrLoc( aNode.right ) );
                    Operand lt_dest = new Operand( aNode.tLoc );
                    Tao ltObject = new Tao( Tao.Operation.LT, lt_src1, lt_src2, lt_dest );
                    tacObjects.add( ltObject );


                    //threeAddress += "GOTO: falseLabel" + aNode.fLoc + "\n";

                    Operand lt_goto = new Operand( aNode.fLoc );
                    Tao ltGotoObject = new Tao( Tao.Operation.GOTO, lt_goto );
                    tacObjects.add( ltGotoObject );

                    if( isOR )
                    {
                        //threeAddress += "falseLabel" + aNode.fLoc + "\n";
                        Operand lt_falseLabel = new Operand( "falseLabel" + aNode.fLoc );
                        Tao ltFalseObject = new Tao( Tao.Operation.LABEL, lt_falseLabel );
                        tacObjects.add( ltFalseObject );
                    }
                    else
                    {
                        //threeAddress += "trueLabel" + aNode.tLoc + "\n";
                        Operand lt_trueLabel = new Operand( "trueLabel" + aNode.tLoc );
                        Tao ltTrueObject = new Tao( Tao.Operation.LABEL, lt_trueLabel );
                        tacObjects.add( ltTrueObject );
                    }
                    break;
                case GT:
                    //threeAddress += "IF_GT: " + printIdOrLoc( aNode.left ) + ", " +
                    //printIdOrLoc( aNode.right ) + ", " + "trueLabel" + aNode.tLoc + "\n";
                    Operand gt_src1 = new Operand( printIdOrLoc( aNode.left ) );
                    Operand gt_src2 = new Operand( printIdOrLoc( aNode.right ) );
                    Operand gt_dest = new Operand( aNode.tLoc );
                    Tao gtObject = new Tao( Tao.Operation.GT, gt_src1, gt_src2, gt_dest );
                    tacObjects.add( gtObject );


                    //threeAddress += "GOTO: falseLabel" + aNode.fLoc + "\n";
                    Operand gt_goto = new Operand( aNode.fLoc );
                    Tao gtGotoObject = new Tao( Tao.Operation.GOTO, gt_goto );
                    tacObjects.add( gtGotoObject );

                    if( isOR )
                    {
                        //threeAddress += "falseLabel" + aNode.fLoc + "\n";
                        Operand gt_falseLabel = new Operand( "falseLabel" + aNode.fLoc );
                        Tao gtFalseObject = new Tao( Tao.Operation.LABEL, gt_falseLabel );
                        tacObjects.add( gtFalseObject );
                    }
                    else
                    {
                        //threeAddress += "trueLabel" + aNode.tLoc + "\n";
                        Operand gt_trueLabel = new Operand( "trueLabel" + aNode.tLoc );
                        Tao gtTrueObject = new Tao( Tao.Operation.LABEL, gt_trueLabel );
                        tacObjects.add( gtTrueObject );
                    }
                    break;
                case LTE:
                    //threeAddress += "IF_LTE: " + printIdOrLoc( aNode.left ) + ", " +
                    //printIdOrLoc( aNode.right ) + ", " + "trueLabel" + aNode.tLoc + "\n";
                    Operand lte_src1 = new Operand( printIdOrLoc( aNode.left ) );
                    Operand lte_src2 = new Operand( printIdOrLoc( aNode.right ) );
                    Operand lte_dest = new Operand( aNode.tLoc );
                    Tao lteObject = new Tao( Tao.Operation.LTE, lte_src1, lte_src2, lte_dest );
                    tacObjects.add( lteObject );


                    //threeAddress += "GOTO: falseLabel" + aNode.fLoc + "\n";
                    Operand lte_goto = new Operand( aNode.fLoc );
                    Tao lteGotoObject = new Tao( Tao.Operation.GOTO, lte_goto );
                    tacObjects.add( lteGotoObject );


                    if( isOR )
                    {
                        //threeAddress += "falseLabel" + aNode.fLoc + "\n";
                        Operand lte_falseLabel = new Operand( "falseLabel" + aNode.fLoc );
                        Tao lteFalseObject = new Tao( Tao.Operation.LABEL, lte_falseLabel );
                        tacObjects.add( lteFalseObject );
                    }
                    else
                    {
                        //threeAddress += "trueLabel" + aNode.tLoc + "\n";
                        Operand lte_trueLabel = new Operand( "trueLabel" + aNode.tLoc );
                        Tao lteTrueObject = new Tao( Tao.Operation.LABEL, lte_trueLabel );
                        tacObjects.add( lteTrueObject );
                    }
                    break;
                case GTE:
                    //threeAddress += "IF_GTE: " + printIdOrLoc( aNode.left ) + ", " +
                    //printIdOrLoc( aNode.right ) + ", " + "trueLabel" + aNode.tLoc + "\n";
                    Operand gte_src1 = new Operand( printIdOrLoc( aNode.left ) );
                    Operand gte_src2 = new Operand( printIdOrLoc( aNode.right ) );
                    Operand gte_dest = new Operand( aNode.tLoc );
                    Tao gteObject = new Tao( Tao.Operation.GTE, gte_src1, gte_src2, gte_dest );
                    tacObjects.add( gteObject );


                    //threeAddress += "GOTO: falseLabel" + aNode.fLoc + "\n";
                    Operand gte_goto = new Operand( aNode.fLoc );
                    Tao gteGotoObject = new Tao( Tao.Operation.GOTO, gte_goto );
                    tacObjects.add( gteGotoObject );


                    if( isOR )
                    {
                        //threeAddress += "falseLabel" + aNode.fLoc + "\n";
                        Operand gte_falseLabel = new Operand( "falseLabel" + aNode.fLoc );
                        Tao gteFalseObject = new Tao( Tao.Operation.LABEL, gte_falseLabel );
                        tacObjects.add( gteFalseObject );
                    }
                    else
                    {
                        //threeAddress += "trueLabel" + aNode.tLoc + "\n";
                        Operand gte_trueLabel = new Operand( "trueLabel" + aNode.tLoc );
                        Tao gteTrueObject = new Tao( Tao.Operation.LABEL, gte_trueLabel );
                        tacObjects.add( gteTrueObject );
                    }
                    break;
                case EQUALS:
                    //threeAddress += "IF_EQ: " + printIdOrLoc( aNode.left ) + ", " +
                    //printIdOrLoc( aNode.right ) + ", " + "trueLabel" + aNode.tLoc + "\n";
                    Operand equals_src1 = new Operand( printIdOrLoc( aNode.left ) );
                    Operand equals_src2 = new Operand( printIdOrLoc( aNode.right ) );
                    Operand equals_dest = new Operand( aNode.tLoc );
                    Tao equalsObject = new Tao( Tao.Operation.EQUALS, equals_src1, equals_src2, equals_dest );
                    tacObjects.add( equalsObject );


                    //threeAddress += "GOTO: falseLabel" + aNode.fLoc + "\n";
                    Operand equals_goto = new Operand( aNode.fLoc );
                    Tao equalsGotoObject = new Tao( Tao.Operation.GOTO, equals_goto );
                    tacObjects.add( equalsGotoObject );


                    if( isOR )
                    {
                        //threeAddress += "falseLabel" + aNode.fLoc + "\n";
                        Operand equals_falseLabel = new Operand( "falseLabel" + aNode.fLoc );
                        Tao equalsFalseObject = new Tao( Tao.Operation.LABEL, equals_falseLabel );
                        tacObjects.add( equalsFalseObject );
                    }
                    else
                    {
                        //threeAddress += "trueLabel" + aNode.tLoc + "\n";
                        Operand equals_trueLabel = new Operand( "trueLabel" + aNode.tLoc );
                        Tao equalsTrueObject = new Tao( Tao.Operation.LABEL, equals_trueLabel );
                        tacObjects.add( equalsTrueObject );
                    }
                    break;
                case NOTEQUALS:
                    //threeAddress += "IF_NE: " + printIdOrLoc( aNode.left ) + ", " +
                    //printIdOrLoc( aNode.right ) + ", " + "trueLabel" + aNode.tLoc + "\n";
                    Operand not_equals_src1 = new Operand( printIdOrLoc( aNode.left ) );
                    Operand not_equals_src2 = new Operand( printIdOrLoc( aNode.right ) );
                    Operand not_equals_dest = new Operand( aNode.tLoc );
                    Tao notEqualsObject = new Tao( Tao.Operation.NOTEQUALS, not_equals_src1, not_equals_src2, not_equals_dest );
                    tacObjects.add( notEqualsObject );


                    //threeAddress += "GOTO: falseLabel" + aNode.fLoc + "\n";
                    Operand not_equals_goto = new Operand( aNode.fLoc );
                    Tao notEqualsGotoObject = new Tao( Tao.Operation.GOTO, not_equals_goto );
                    tacObjects.add( notEqualsGotoObject );

                    if( isOR )
                    {
                        //threeAddress += "falseLabel" + aNode.fLoc + "\n";
                        Operand not_equals_falseLabel = new Operand( "falseLabel" + aNode.fLoc );
                        Tao notEqualsFalseObject = new Tao( Tao.Operation.LABEL, not_equals_falseLabel );
                        tacObjects.add( notEqualsFalseObject );
                    }
                    else
                    {
                        //threeAddress += "trueLabel" + aNode.tLoc + "\n";
                        Operand not_equals_trueLabel = new Operand( "trueLabel" + aNode.tLoc );
                        Tao notEqualsTrueObject = new Tao( Tao.Operation.LABEL, not_equals_trueLabel );
                        tacObjects.add( notEqualsTrueObject );
                    }
                    break;
                case IF:
                    //threeAddress += "falseLabel" + aNode.fLoc + "\n";
                    Operand if_dest = new Operand( aNode.fLoc );
                    Tao ifObject = new Tao( Tao.Operation.IF, if_dest );
                    tacObjects.add( ifObject );
                    break;
                case WHILE:
                    //threeAddress += "GOTO: repeatLabel" + aNode.rLoc + "\n" +
                    //"falseLabel" + aNode.fLoc + "\n";
                    Operand while_src1 = new Operand( aNode.rLoc );
                    Operand while_dest = new Operand( aNode.fLoc );
                    Tao whileObject = new Tao( Tao.Operation.WHILE, while_src1, while_dest );
                    tacObjects.add( whileObject );
                    break;
            }
        }
        catch ( Exception e )
        {
            System.out.println( "ERROR: Syntax error" );
            System.exit( -1 );
        }
    }

    private String printIdOrLoc( node aNode )
    {
        if( aNode.type == Scanner.TokenType.ID )
        {
            return aNode.value;
        }
        else
        {
            return "temp" + aNode.loc;
        }
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
                throw new Exception( "Token is null" );
            }
            else
            {
                return aToken;
            }
        } catch ( Exception e )
        {
            System.out.println( "ERROR: Syntax error - " + e.getMessage() );
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
                throw new Exception( "Token is null" );
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
                throw new Exception( "Unexpected token type: " + aToken.tokenType + " , Expected Type: " + expectedToken );
            }
        } catch ( Exception e )
        {
            System.out.println( "ERROR: Syntax error - " + e.getMessage() );
            System.exit( -1 );
        }
    }

}
