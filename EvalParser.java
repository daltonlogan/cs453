import java.util.ArrayList;

public class EvalParser {
    Scanner scan = new Scanner();
    String evalString;

    int tempID = 0;
    String threeAddressResult = "";
    ArrayList<Integer> IDs = new ArrayList<Integer>();
    ArrayList<Integer> trueLabelIDs = new ArrayList<Integer>();
    ArrayList<Integer> falseLabelIDs = new ArrayList<Integer>();
    ArrayList<Integer> repeatLabelIDs = new ArrayList<Integer>();
    ArrayList<node> nodes = new ArrayList<node>();
    node root = new node( Scanner.TokenType.INVALID, "");
    private int parenCounter = 0;
    boolean expressionInside;

    int tlabelID = 0; // Label id for true
    int flabelID = 0; // Label id for false
    int rlabelID = 0; // Label id for loops
    
    /* 
     * Any leaf node there should only be one child, this will be the left node
     * Any interior node should have two children, what is left of the operation and what is to the right
     */
    
    private class node{
    	Scanner.TokenType type;
    	String value;
    	int num;
    	node childLeft = null;
    	node childRight = null;
    	
    	node( Scanner.TokenType type, String value){
    		this.type = type;
    		this.value = value;
    	}

        node( Scanner.TokenType type, int expr){
            this.type = type;
            num = expr;
        }
    	
    	public String toString() {
    		String val = "Node type:" + type + ", Node value:" + value;
    		if(this.type != Scanner.TokenType.NUM)
    			val += ", Left Child (" + this.childLeft.toString() + ")" + ", Right Child (" + this.childRight.toString() + ")";
    		return val;
    	}
    }   

    /***************** Simple Expression Evaluator ***********************/

    public String program(String eval)
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
            stmt_list();
            nextToken = lookahead();
            match( nextToken, Scanner.TokenType.RIGHTCURLY );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        return threeAddressResult;
    }


    private void stmt_list()
    {
        Scanner.Token nextToken;
        do
        {
            stmt();
            nextToken = lookahead();
        }
        while( nextToken != null && ( nextToken.tokenType == Scanner.TokenType.IF || nextToken.tokenType == Scanner.TokenType.WHILE ) );
    }

    private void stmt()
    {
        Scanner.Token nextToken = lookahead();
        if( nextToken == null )
        {
            return;
        }

        switch ( nextToken.tokenType ){
            case INT:
                assignment();
                break;
            case IF:
            case WHILE:
                control_flow();
                break;
        }
    }

    private void assignment()
    {
        try
        {
            Scanner.Token nextToken = lookahead();
            match( nextToken, Scanner.TokenType.INT );
            F();
            nextToken = lookahead();
            match( nextToken, Scanner.TokenType.ASSIGN );
            E();

            nextToken = lookahead();
            match( nextToken, Scanner.TokenType.SEMICOLON );
            int first = IDs.remove(IDs.size() - 1);
            int second = IDs.remove(IDs.size() - 1);
            node firstNode = nodes.remove(nodes.size() - 1);
            node secondNode = nodes.remove(nodes.size() - 1);
            node tempNode = new node( Scanner.TokenType.ASSIGN, "=");
            tempNode.childRight = firstNode;
            tempNode.childLeft = secondNode;
            nodes.add(tempNode);
            IDs.add(tempID);
            threeAddressResult += secondNode.value + " = " +
                    "temp" + first + "\n";

            tempID = 0;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    private void control_flow()
    {
        Scanner.Token nextToken = lookahead();
        if( nextToken != null && nextToken.tokenType == Scanner.TokenType.IF )
        {
//            if ( expr ) { stmt_list } |
            match( nextToken, Scanner.TokenType.IF );
            nextToken = lookahead();
            match( nextToken, Scanner.TokenType.LEFTPAREN );
            A();
            nextToken = lookahead();
            match( nextToken, Scanner.TokenType.RIGHTPAREN );
            nextToken = lookahead();

            tempID = 0;

            match( nextToken, Scanner.TokenType.LEFTCURLY );
            stmt_list();
            nextToken = lookahead();
            match( nextToken, Scanner.TokenType.RIGHTCURLY );
            threeAddressResult += "falseLabel" + flabelID + "\n";
        }
        else if( nextToken != null && nextToken.tokenType == Scanner.TokenType.WHILE )
        {
//            while ( expr ) { stmt_list }
            match( nextToken, Scanner.TokenType.WHILE );
            threeAddressResult += "repeatLabel" + rlabelID + "\n";
            nextToken = lookahead();
            match( nextToken, Scanner.TokenType.LEFTPAREN );
            A();
            nextToken = lookahead();
            match( nextToken, Scanner.TokenType.RIGHTPAREN );
            nextToken = lookahead();

            tempID = 0;

            match( nextToken, Scanner.TokenType.LEFTCURLY );
            stmt_list();
            nextToken = lookahead();
            match( nextToken, Scanner.TokenType.RIGHTCURLY );
            threeAddressResult += "GOTO: repeatLabel" + rlabelID + "\n";
            threeAddressResult += "falseLabel" + flabelID + "\n";
        }

    }


    private void A()
    {
        B();
        while (true) {
            Scanner.Token nextToken = lookahead();
            if (nextToken != null && nextToken.tokenType == Scanner.TokenType.EQUALS) {
                match(nextToken, Scanner.TokenType.EQUALS);
                E();
                int first = IDs.remove(IDs.size() - 1);
                int second = IDs.remove(IDs.size() - 1);
                node firstNode = nodes.remove(nodes.size() - 1);
                node secondNode = nodes.remove(nodes.size() - 1);
                node tempNode = new node( Scanner.TokenType.EQUALS, "");
                tempNode.childRight = firstNode;
                tempNode.childLeft = secondNode;
                nodes.add(tempNode);
                IDs.add(tempID);
                threeAddressResult += "IF_EQ: " + "temp" + second  + ", " +
                        "temp" + first + ", " + "trueLabel" + tlabelID + "\n";
                threeAddressResult += "GOTO: falseLabel" + flabelID + "\n";
                threeAddressResult += "trueLabel" + tlabelID + "\n";
                return;

            } else if (nextToken != null && nextToken.tokenType == Scanner.TokenType.NOTEQUALS) {
                match(nextToken, Scanner.TokenType.NOTEQUALS);
                E();
                int first = IDs.remove(IDs.size() - 1);
                int second = IDs.remove(IDs.size() - 1);
                node firstNode = nodes.remove(nodes.size() - 1);
                node secondNode = nodes.remove(nodes.size() - 1);
                node tempNode = new node( Scanner.TokenType.NOTEQUALS, "");
                tempNode.childRight = firstNode;
                tempNode.childLeft = secondNode;
                nodes.add(tempNode);
                IDs.add(tempID);
                threeAddressResult += "IF_NE: " + "temp" + second  + ", " +
                        "temp" + first + ", " + "trueLabel" + tlabelID + "\n";
                threeAddressResult += "GOTO: falseLabel" + flabelID + "\n";
                threeAddressResult += "trueLabel" + tlabelID + "\n";
                return;
            }
            else
            {
                return;
            }
        }
    }

    private void B()
    {
        E();
        while (true) {
            Scanner.Token nextToken = lookahead();
            if (nextToken != null && nextToken.tokenType == Scanner.TokenType.LT) {
                match(nextToken, Scanner.TokenType.LT);
                E();
                int first = IDs.remove(IDs.size() - 1);
                int second = IDs.remove(IDs.size() - 1);
                node firstNode = nodes.remove(nodes.size() - 1);
                node secondNode = nodes.remove(nodes.size() - 1);
                node tempNode = new node( Scanner.TokenType.LT, "");
                tempNode.childRight = firstNode;
                tempNode.childLeft = secondNode;
                nodes.add(tempNode);
                IDs.add(tempID);
                trueLabelIDs.add( tlabelID );
                threeAddressResult += "IF_LT: " + "temp" + second  + ", " +
                        "temp" + first + ", " + "trueLabel" + tlabelID + "\n";
                threeAddressResult += "GOTO: falseLabel" + flabelID + "\n";
                threeAddressResult += "trueLabel" + tlabelID + "\n";
            }
            else if (nextToken != null && nextToken.tokenType == Scanner.TokenType.GT) {
                match(nextToken, Scanner.TokenType.GT);
                E();
                int first = IDs.remove(IDs.size() - 1);
                int second = IDs.remove(IDs.size() - 1);
                node firstNode = nodes.remove(nodes.size() - 1);
                node secondNode = nodes.remove(nodes.size() - 1);
                node tempNode = new node( Scanner.TokenType.GT, "");
                tempNode.childRight = firstNode;
                tempNode.childLeft = secondNode;
                nodes.add(tempNode);
                IDs.add(tempID);
                trueLabelIDs.add( tlabelID );
                threeAddressResult += "IF_GT: " + "temp" + second  + ", " +
                        "temp" + first + ", " + "trueLabel" + tlabelID + "\n";
                threeAddressResult += "GOTO: falseLabel" + flabelID + "\n";
                threeAddressResult += "trueLabel" + tlabelID + "\n";
            }
            else if (nextToken != null && nextToken.tokenType == Scanner.TokenType.LTE) {
                match(nextToken, Scanner.TokenType.LTE);
                E();
                int first = IDs.remove(IDs.size() - 1);
                int second = IDs.remove(IDs.size() - 1);
                node firstNode = nodes.remove(nodes.size() - 1);
                node secondNode = nodes.remove(nodes.size() - 1);
                node tempNode = new node( Scanner.TokenType.LTE, "");
                tempNode.childRight = firstNode;
                tempNode.childLeft = secondNode;
                nodes.add(tempNode);
                IDs.add(tempID);
                trueLabelIDs.add( tlabelID );
                threeAddressResult += "IF_LTE: " + "temp" + second  + ", " +
                        "temp" + first + ", " + "trueLabel" + tlabelID + "\n";
                threeAddressResult += "GOTO: falseLabel" + flabelID + "\n";
                threeAddressResult += "trueLabel" + tlabelID + "\n";
            }
            else if (nextToken != null && nextToken.tokenType == Scanner.TokenType.GTE) {
                match(nextToken, Scanner.TokenType.GTE);
                E();
                int first = IDs.remove(IDs.size() - 1);
                int second = IDs.remove(IDs.size() - 1);
                node firstNode = nodes.remove(nodes.size() - 1);
                node secondNode = nodes.remove(nodes.size() - 1);
                node tempNode = new node( Scanner.TokenType.GTE, "");
                tempNode.childRight = firstNode;
                tempNode.childLeft = secondNode;
                nodes.add(tempNode);
                IDs.add(tempID);
                trueLabelIDs.add( tlabelID );
                threeAddressResult += "IF_GTE: " + "temp" + second  + ", " +
                        "temp" + first + ", " + "trueLabel" + tlabelID + "\n";
                threeAddressResult += "GOTO: falseLabel" + flabelID + "\n";
                threeAddressResult += "trueLabel" + tlabelID + "\n";
            }
            else
            {
                return;
            }
        }
    }

    private int E() {
        int result = T();
        while (true) {
            Scanner.Token nextToken = lookahead();
            if (nextToken != null && nextToken.tokenType == Scanner.TokenType.PLUS) {
                match(nextToken, Scanner.TokenType.PLUS);
                result += T();
                int first = IDs.remove(IDs.size() - 1);
                int second = IDs.remove(IDs.size() - 1);
                node firstNode = nodes.remove(nodes.size() - 1);
                node secondNode = nodes.remove(nodes.size() - 1);
                node tempNode = new node( Scanner.TokenType.PLUS, "");
                tempNode.childRight = firstNode;
                tempNode.childLeft = secondNode;
                nodes.add(tempNode);
                threeAddressResult += "temp" + tempID + " = " +
                        "temp" + second + " + " +
                        "temp" + first + "\n";
                IDs.add(tempID++);
                continue;
            } else if (nextToken != null && nextToken.tokenType == Scanner.TokenType.MINUS) {
                match(nextToken, Scanner.TokenType.MINUS);
                result -= T();
                int first = IDs.remove(IDs.size() - 1);
                int second = IDs.remove(IDs.size() - 1);
                node firstNode = nodes.remove(nodes.size() - 1);
                node secondNode = nodes.remove(nodes.size() - 1);
                node tempNode = new node( Scanner.TokenType.MINUS, "");
                tempNode.childRight = firstNode;
                tempNode.childLeft = secondNode;
                nodes.add(tempNode);
                IDs.add(tempID);
                threeAddressResult += "temp" + tempID++ + " = " +
                        "temp" + second + " - " +
                        "temp" + first + "\n";
                continue;
            }
            return result;
        }
    }

    private int T() {
        int result = F();
        while (true) {
            Scanner.Token nextToken = lookahead();
            if (nextToken != null && nextToken.tokenType == Scanner.TokenType.MUL) {
                match(nextToken, Scanner.TokenType.MUL);
                result *= F();
                int first = IDs.remove(IDs.size() - 1);
                int second = IDs.remove(IDs.size() - 1);
                node firstNode = nodes.remove(nodes.size() - 1);
                node secondNode = nodes.remove(nodes.size() - 1);
                node tempNode = new node( Scanner.TokenType.MUL, "");
                tempNode.childRight = firstNode;
                tempNode.childLeft = secondNode;
                nodes.add(tempNode);
                IDs.add(tempID);
                threeAddressResult += "temp" + tempID++ + " = " +
                        "temp" + second + " * " +
                        "temp" + first + "\n";
                continue;
            } else if (nextToken != null && nextToken.tokenType == Scanner.TokenType.DIV) {
                match(nextToken, Scanner.TokenType.DIV);
                result /= F();
                int first = IDs.remove(IDs.size() - 1);
                int second = IDs.remove(IDs.size() - 1);
                node firstNode = nodes.remove(nodes.size() - 1);
                node secondNode = nodes.remove(nodes.size() - 1);
                node tempNode = new node( Scanner.TokenType.DIV, "");
                tempNode.childRight = firstNode;
                tempNode.childLeft = secondNode;
                nodes.add(tempNode);
                IDs.add(tempID);
                threeAddressResult += "temp" + tempID++ + " = " +
                        "temp" + second + " / " +
                        "temp" + first + "\n";
                continue;
            }
            return result;
        }
    }

    private int F() {

        int result = 0;
        try {
            Scanner.Token nextToken = lookahead();
            if (nextToken != null && nextToken.tokenType == Scanner.TokenType.LEFTPAREN) {
                parenCounter++;
                match(nextToken, Scanner.TokenType.LEFTPAREN);
                result += E();
            }
            nextToken = lookahead();
            if (nextToken != null && nextToken.tokenType == Scanner.TokenType.RIGHTPAREN) {
                parenCounter--;
                match(nextToken, Scanner.TokenType.RIGHTPAREN);
                if (!expressionInside) {
                    throw new Exception();
                }
            } else if (nextToken != null && nextToken.tokenType == Scanner.TokenType.NUM) {
                int num = Integer.parseInt(nextToken.tokenVal);
                node tempNode = new node( Scanner.TokenType.NUM, String.valueOf(num));
                nodes.add(tempNode);
                IDs.add(tempID);
                threeAddressResult += "temp" + tempID++ + " = " + num + "\n";
                match(nextToken, Scanner.TokenType.NUM);
                expressionInside = true;
                return num;
            }
            else if( nextToken != null && nextToken.tokenType == Scanner.TokenType.ID )
            {
                String id = nextToken.tokenVal;
                node tempNode = new node( Scanner.TokenType.ID, id);
                nodes.add(tempNode);
                IDs.add(tempID);
                match(nextToken, Scanner.TokenType.ID);
            }
            else {
                throw new Exception();
            }

            if (parenCounter != 0) {
                throw new Exception();
            }
        } catch (Exception e) {
            System.out.println("Invalid input");
            expressionInside = false;
        }
        return result;
    }

    /****************************************/

    public int evaluateExpression(String eval) {

        evalString = eval;
        int result = E();
        try {
            if (!evalString.isEmpty()) {
                throw new Exception();
            }
        } catch (Exception e) {
            System.out.println( "Input string not empty" );
        }
        return result;
    }

    public String getThreeAddr(String eval) {
        this.threeAddressResult = "";
        evalString = eval;
        tempID = 0;
        IDs.clear();
        E();
        return this.threeAddressResult;
    }
    
    public String printTree(String eval) {
    	this.threeAddressResult = "";
    	evalString = eval;
    	tempID = 0;
    	nodes.clear();
    	IDs.clear();
    	//return root.toString();
        return program( eval );
    }

    private Scanner.Token lookahead() {
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
            else return aToken;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            System.exit( -1 );
        }
        return null;
    }

    private void match(Scanner.Token aToken, Scanner.TokenType expectedToken) {
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
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            System.exit( -1 );
        }
    }

}
