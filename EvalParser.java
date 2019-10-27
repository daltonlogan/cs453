import java.util.ArrayList;

public class EvalParser
{
    Scanner scan = new Scanner();
    String evalString;

    int tempID = 0;
    String threeAddressResult = "";
    ArrayList< Integer > IDs = new ArrayList< Integer >();
    node root = new node( Scanner.TokenType.PROGRAM );
    private int parenCounter = 0;
    boolean expressionInside;
    ArrayList< Tao > threeAddressObjects = new ArrayList<>();

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
                temp = var_decl();
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
                    temp = var_decl();
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
        nextToken = lookahead();
        match( nextToken, Scanner.TokenType.LEFTPAREN );
        nextToken = lookahead();
        match( nextToken, Scanner.TokenType.RIGHTPAREN );
        nextToken = lookahead();
        match( nextToken, Scanner.TokenType.LEFTCURLY );
        node result = stmt_list();
        nextToken = lookahead();
        match( nextToken, Scanner.TokenType.RIGHTCURLY );

        return result;
    }

    private node var_decl( ) throws Exception
    {
        Scanner.Token nextToken = lookahead();
        match( nextToken, Scanner.TokenType.INT );
        nextToken = lookahead();
        match( nextToken, Scanner.TokenType.ID );
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
            result = var_decl();

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
                node right = F();
                mid.left = result;
                mid.right = right;
                result = mid;
                result.loc = tempID++;
            }
            else if ( nextToken.tokenType == Scanner.TokenType.DIV )
            {
                match( nextToken, Scanner.TokenType.DIV );
                node mid = new node( Scanner.TokenType.DIV );
                node right = F();
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

    public String getThreeAddr( String eval )
    {
        this.threeAddressResult = "";
        evalString = eval;
        tempID = 0;
        IDs.clear();
        E();
        return this.threeAddressResult;
    }

    public String emitTAC( node aNode, boolean isOR )
    {
        try
        {
            if ( aNode == null )
            {
                return threeAddress;
            }

            if ( aNode.type == Scanner.TokenType.WHILE )
            {
                //threeAddress += "repeatLabel" + aNode.rLoc + "\n";
                Operand start = new Operand( aNode.rLoc );
                Tao startObject = new Tao( Tao.Operation.START_WHILE, start );
                threeAddress += startObject.toString();
                threeAddressObjects.add( startObject );
            }

            if( aNode.type == Scanner.TokenType.OR )
            {
                isOR = true;
            }

            emitTAC( aNode.left, isOR );

            if( aNode.type == Scanner.TokenType.OR )
            {
                isOR = false;
            }

            emitTAC( aNode.right, isOR );


            for ( node theNode : aNode.stmts )
            {
                emitTAC( theNode, isOR );
            }

            switch ( aNode.type )
            {
                case NUM:
                    //threeAddress += "temp" + aNode.loc + " = " + aNode.value + "\n";
                    Operand num_src1 = new Operand( aNode.value );
                    Operand num_dest = new Operand( printIdOrLoc( aNode ) );
                    Tao numObject = new Tao( Tao.Operation.NUM, num_src1, num_dest );
                    threeAddress += numObject.toString();
                    threeAddressObjects.add( numObject );
                    break;
                case ASSIGN:
                    //threeAddress += printIdOrLoc( aNode.left ) + " = " + printIdOrLoc( aNode.right ) + "\n";
                    Operand assign_src1 = new Operand( printIdOrLoc( aNode.right ) );
                    Operand assign_dest = new Operand( printIdOrLoc( aNode.left ) );
                    Tao assignObject = new Tao( Tao.Operation.ASSIGN, assign_src1, assign_dest );
                    threeAddress += assignObject.toString();
                    threeAddressObjects.add( assignObject );
                    break;
                case PLUS:
                    //threeAddress += "temp" + aNode.loc + " = " + printIdOrLoc( aNode.left ) + " + " + printIdOrLoc( aNode.right ) + "\n";
                    Operand plus_src1 = new Operand( printIdOrLoc( aNode.left ) );
                    Operand plus_src2 = new Operand( printIdOrLoc( aNode.right ) );
                    Operand plus_dest = new Operand( printIdOrLoc( aNode ) );
                    Tao plusObject = new Tao( Tao.Operation.PLUS, plus_src1, plus_src2, plus_dest );
                    threeAddress += plusObject.toString();
                    threeAddressObjects.add( plusObject );
                    break;
                case MINUS:
                    //threeAddress += "temp" + aNode.loc + " = " + printIdOrLoc( aNode.left ) + " - " + printIdOrLoc( aNode.right ) + "\n";
                    Operand minus_src1 = new Operand( printIdOrLoc( aNode.left ) );
                    Operand minus_src2 = new Operand( printIdOrLoc( aNode.right ) );
                    Operand minus_dest = new Operand( printIdOrLoc( aNode ) );
                    Tao minusObject = new Tao( Tao.Operation.MINUS, minus_src1, minus_src2, minus_dest );
                    threeAddress += minusObject.toString();
                    threeAddressObjects.add( minusObject );
                    break;
                case MUL:
                    //threeAddress += "temp" + aNode.loc + " = " + printIdOrLoc( aNode.left ) + " * " + printIdOrLoc( aNode.right ) + "\n";
                    Operand mul_src1 = new Operand( printIdOrLoc( aNode.left ) );
                    Operand mul_src2 = new Operand( printIdOrLoc( aNode.right ) );
                    Operand mul_dest = new Operand( printIdOrLoc( aNode ) );
                    Tao mulObject = new Tao( Tao.Operation.MUL, mul_src1, mul_src2, mul_dest );
                    threeAddress += mulObject.toString();
                    threeAddressObjects.add( mulObject );
                    break;
                case DIV:
                    //threeAddress += "temp" + aNode.loc + " = " + printIdOrLoc( aNode.left ) + " / " + printIdOrLoc( aNode.right ) + "\n";
                    Operand div_src1 = new Operand( printIdOrLoc( aNode.left ) );
                    Operand div_src2 = new Operand( printIdOrLoc( aNode.right ) );
                    Operand div_dest = new Operand( printIdOrLoc( aNode ) );
                    Tao divObject = new Tao( Tao.Operation.DIV, div_src1, div_src2, div_dest );
                    threeAddress += divObject.toString();
                    threeAddressObjects.add( divObject );
                    break;
                case LT:
                    //threeAddress += "IF_LT: " + printIdOrLoc( aNode.left ) + ", " +
                            //printIdOrLoc( aNode.right ) + ", " + "trueLabel" + aNode.tLoc + "\n";
                    Operand lt_src1 = new Operand( printIdOrLoc( aNode.left ) );
                    Operand lt_src2 = new Operand( printIdOrLoc( aNode.right ) );
                    Operand lt_dest = new Operand( aNode.tLoc );
                    Tao ltObject = new Tao( Tao.Operation.LT, lt_src1, lt_src2, lt_dest );
                    threeAddress += ltObject.toString();
                    threeAddressObjects.add( ltObject );


                    //threeAddress += "GOTO: falseLabel" + aNode.fLoc + "\n";

                    Operand lt_goto = new Operand( aNode.fLoc );
                    Tao ltGotoObject = new Tao( Tao.Operation.GOTO, lt_goto );
                    threeAddress += ltGotoObject.toString();
                    threeAddressObjects.add( ltGotoObject );

                    if( isOR )
                    {
                        //threeAddress += "falseLabel" + aNode.fLoc + "\n";
                        Operand lt_falseLabel = new Operand( "falseLabel" + aNode.fLoc );
                        Tao ltFalseObject = new Tao( Tao.Operation.LABEL, lt_falseLabel );
                        threeAddress += ltFalseObject.toString();
                        threeAddressObjects.add( ltFalseObject );
                    }
                    else
                    {
                        //threeAddress += "trueLabel" + aNode.tLoc + "\n";
                        Operand lt_trueLabel = new Operand( "trueLabel" + aNode.tLoc );
                        Tao ltTrueObject = new Tao( Tao.Operation.LABEL, lt_trueLabel );
                        threeAddress += ltTrueObject.toString();
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
                    threeAddress += gtObject.toString();
                    threeAddressObjects.add( gtObject );


                    //threeAddress += "GOTO: falseLabel" + aNode.fLoc + "\n";
                    Operand gt_goto = new Operand( aNode.fLoc );
                    Tao gtGotoObject = new Tao( Tao.Operation.GOTO, gt_goto );
                    threeAddress += gtGotoObject.toString();
                    threeAddressObjects.add( gtGotoObject );

                    if( isOR )
                    {
                        //threeAddress += "falseLabel" + aNode.fLoc + "\n";
                        Operand gt_falseLabel = new Operand( "falseLabel" + aNode.fLoc );
                        Tao gtFalseObject = new Tao( Tao.Operation.LABEL, gt_falseLabel );
                        threeAddress += gtFalseObject.toString();
                        threeAddressObjects.add( gtFalseObject );
                    }
                    else
                    {
                        //threeAddress += "trueLabel" + aNode.tLoc + "\n";
                        Operand gt_trueLabel = new Operand( "trueLabel" + aNode.tLoc );
                        Tao gtTrueObject = new Tao( Tao.Operation.LABEL, gt_trueLabel );
                        threeAddress += gtTrueObject.toString();
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
                    threeAddress += lteObject.toString();
                    threeAddressObjects.add( lteObject );


                    //threeAddress += "GOTO: falseLabel" + aNode.fLoc + "\n";
                    Operand lte_goto = new Operand( aNode.fLoc );
                    Tao lteGotoObject = new Tao( Tao.Operation.GOTO, lte_goto );
                    threeAddress += lteGotoObject.toString();
                    threeAddressObjects.add( lteGotoObject );


                    if( isOR )
                    {
                        //threeAddress += "falseLabel" + aNode.fLoc + "\n";
                        Operand lte_falseLabel = new Operand( "falseLabel" + aNode.fLoc );
                        Tao lteFalseObject = new Tao( Tao.Operation.LABEL, lte_falseLabel );
                        threeAddress += lteFalseObject.toString();
                        threeAddressObjects.add( lteFalseObject );
                    }
                    else
                    {
                        //threeAddress += "trueLabel" + aNode.tLoc + "\n";
                        Operand lte_trueLabel = new Operand( "trueLabel" + aNode.tLoc );
                        Tao lteTrueObject = new Tao( Tao.Operation.LABEL, lte_trueLabel );
                        threeAddress += lteTrueObject.toString();
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
                    threeAddress += gteObject.toString();
                    threeAddressObjects.add( gteObject );


                    //threeAddress += "GOTO: falseLabel" + aNode.fLoc + "\n";
                    Operand gte_goto = new Operand( aNode.fLoc );
                    Tao gteGotoObject = new Tao( Tao.Operation.GOTO, gte_goto );
                    threeAddress += gteGotoObject.toString();
                    threeAddressObjects.add( gteGotoObject );


                    if( isOR )
                    {
                        //threeAddress += "falseLabel" + aNode.fLoc + "\n";
                        Operand gte_falseLabel = new Operand( "falseLabel" + aNode.fLoc );
                        Tao gteFalseObject = new Tao( Tao.Operation.LABEL, gte_falseLabel );
                        threeAddress += gteFalseObject.toString();
                        threeAddressObjects.add( gteFalseObject );
                    }
                    else
                    {
                        //threeAddress += "trueLabel" + aNode.tLoc + "\n";
                        Operand gte_trueLabel = new Operand( "trueLabel" + aNode.tLoc );
                        Tao gteTrueObject = new Tao( Tao.Operation.LABEL, gte_trueLabel );
                        threeAddress += gteTrueObject.toString();
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
                    threeAddress += equalsObject.toString();
                    threeAddressObjects.add( equalsObject );


                    //threeAddress += "GOTO: falseLabel" + aNode.fLoc + "\n";
                    Operand equals_goto = new Operand( aNode.fLoc );
                    Tao equalsGotoObject = new Tao( Tao.Operation.GOTO, equals_goto );
                    threeAddress += equalsGotoObject.toString();
                    threeAddressObjects.add( equalsGotoObject );


                    if( isOR )
                    {
                        //threeAddress += "falseLabel" + aNode.fLoc + "\n";
                        Operand equals_falseLabel = new Operand( "falseLabel" + aNode.fLoc );
                        Tao equalsFalseObject = new Tao( Tao.Operation.LABEL, equals_falseLabel );
                        threeAddress += equalsFalseObject.toString();
                        threeAddressObjects.add( equalsFalseObject );
                    }
                    else
                    {
                        //threeAddress += "trueLabel" + aNode.tLoc + "\n";
                        Operand equals_trueLabel = new Operand( "trueLabel" + aNode.tLoc );
                        Tao equalsTrueObject = new Tao( Tao.Operation.LABEL, equals_trueLabel );
                        threeAddress += equalsTrueObject.toString();
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
                    threeAddress += notEqualsObject.toString();
                    threeAddressObjects.add( notEqualsObject );


                    //threeAddress += "GOTO: falseLabel" + aNode.fLoc + "\n";
                    Operand not_equals_goto = new Operand( aNode.fLoc );
                    Tao notEqualsGotoObject = new Tao( Tao.Operation.GOTO, not_equals_goto );
                    threeAddress += notEqualsGotoObject.toString();
                    threeAddressObjects.add( notEqualsGotoObject );

                    if( isOR )
                    {
                        //threeAddress += "falseLabel" + aNode.fLoc + "\n";
                        Operand not_equals_falseLabel = new Operand( "falseLabel" + aNode.fLoc );
                        Tao notEqualsFalseObject = new Tao( Tao.Operation.LABEL, not_equals_falseLabel );
                        threeAddress += notEqualsFalseObject.toString();
                        threeAddressObjects.add( notEqualsFalseObject );
                    }
                    else
                    {
                        //threeAddress += "trueLabel" + aNode.tLoc + "\n";
                        Operand not_equals_trueLabel = new Operand( "trueLabel" + aNode.tLoc );
                        Tao notEqualsTrueObject = new Tao( Tao.Operation.LABEL, not_equals_trueLabel );
                        threeAddress += notEqualsTrueObject.toString();
                        threeAddressObjects.add( notEqualsTrueObject );
                    }
                    break;
                case IF:
                    //threeAddress += "falseLabel" + aNode.fLoc + "\n";
                    Operand if_dest = new Operand( aNode.fLoc );
                    Tao ifObject = new Tao( Tao.Operation.IF, if_dest );
                    threeAddress += ifObject.toString();
                    threeAddressObjects.add( ifObject );
                    break;
                case WHILE:
                    //threeAddress += "GOTO: repeatLabel" + aNode.rLoc + "\n" +
                            //"falseLabel" + aNode.fLoc + "\n";
                    Operand while_src1 = new Operand( aNode.rLoc );
                    Operand while_dest = new Operand( aNode.fLoc );
                    Tao whileObject = new Tao( Tao.Operation.WHILE, while_src1, while_dest );
                    threeAddress += whileObject.toString();
                    threeAddressObjects.add( whileObject );
                    break;
            }
        }
        catch ( Exception e )
        {
            System.out.println( "ERROR: Syntax error" );
            System.exit( -1 );
        }
        return threeAddress;
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
