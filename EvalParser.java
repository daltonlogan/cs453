
import java.lang.String;
import java.util.*;

public class EvalParser {
    Scanner scan = new Scanner();
    String evalString;

    int tempID = 0;
    String threeAddressResult = "";
    ArrayList<Integer> IDs = new ArrayList<Integer>();
    ArrayList<node> nodes = new ArrayList<node>();
    node root = new node("root", "");
    private int parenCounter = 0;
    boolean expressionInside;
    
    /* 
     * Any leaf node there should only be one child, this will be the left node
     * Any interior node should have two children, what is left of the operation and what is to the right
     */
    
    private class node{
    	String type;
    	String value;
    	node childLeft = null;
    	node childRight = null;
    	
    	node(String type, String value){
    		this.type = type;
    		this.value = value;
    	}
    	
    	public String toString() {
    		String val = "Node type:" + type + ", Node value:" + value;
    		if(this.type != "num")
    			val += ", Left Child (" + this.childLeft.toString() + ")" + ", Right Child (" + this.childRight.toString() + ")";
    		return val;
    	}
    }   

    /***************** Simple Expression Evaluator ***********************/

    private int E() {
        int result = T();
        while (true) {
            Scanner.Token nextToken = lookahead();
            if (nextToken != null && nextToken.tokenType == Scanner.TokenType.PLUS) {
                match(nextToken);
                result += T();
                int first = IDs.remove(IDs.size() - 1);
                int second = IDs.remove(IDs.size() - 1);
                node firstNode = nodes.remove(nodes.size() - 1);
                node secondNode = nodes.remove(nodes.size() - 1);
                node tempNode = new node("+", "");
                tempNode.childRight = firstNode;
                tempNode.childLeft = secondNode;
                nodes.add(tempNode);
                IDs.add(tempID);
                threeAddressResult += "temp" + tempID++ + " = " +
                        "temp" + second + " + " +
                        "temp" + first + "\n";
                continue;
            } else if (nextToken != null && nextToken.tokenType == Scanner.TokenType.MINUS) {
                match(nextToken);
                result -= T();
                int first = IDs.remove(IDs.size() - 1);
                int second = IDs.remove(IDs.size() - 1);
                node firstNode = nodes.remove(nodes.size() - 1);
                node secondNode = nodes.remove(nodes.size() - 1);
                node tempNode = new node("-", "");
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
                match(nextToken);
                result *= F();
                int first = IDs.remove(IDs.size() - 1);
                int second = IDs.remove(IDs.size() - 1);
                node firstNode = nodes.remove(nodes.size() - 1);
                node secondNode = nodes.remove(nodes.size() - 1);
                node tempNode = new node("*", "");
                tempNode.childRight = firstNode;
                tempNode.childLeft = secondNode;
                nodes.add(tempNode);
                IDs.add(tempID);
                threeAddressResult += "temp" + tempID++ + " = " +
                        "temp" + second + " * " +
                        "temp" + first + "\n";
                continue;
            } else if (nextToken != null && nextToken.tokenType == Scanner.TokenType.DIV) {
                match(nextToken);
                result /= F();
                int first = IDs.remove(IDs.size() - 1);
                int second = IDs.remove(IDs.size() - 1);
                node firstNode = nodes.remove(nodes.size() - 1);
                node secondNode = nodes.remove(nodes.size() - 1);
                node tempNode = new node("/", "");
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
                match(nextToken);
                result += E();
            }
            nextToken = lookahead();
            if (nextToken != null && nextToken.tokenType == Scanner.TokenType.RIGHTPAREN) {
                parenCounter--;
                match(nextToken);
                if (!expressionInside) {
                    throw new Exception();
                }
            } else if (nextToken != null && nextToken.tokenType == Scanner.TokenType.NUM) {
                int num = Integer.parseInt(nextToken.tokenVal);
                node tempNode = new node("num", String.valueOf(num));
                nodes.add(tempNode);
                IDs.add(tempID);
                threeAddressResult += "temp" + tempID++ + " = " + num + "\n";
                match(nextToken);
                expressionInside = true;
                return num;
            } else {
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
    	E();
    	//return root.toString();
    	return nodes.remove(nodes.size()-1).toString();
    }

    private Scanner.Token lookahead() {
        if (evalString.isEmpty()) {
            return null;
        }
        return scan.extractToken(new StringBuilder(evalString));
    }

    private void match(Scanner.Token aToken) {
        if (aToken == null) {
            return;
        }
        while (Character.isWhitespace(evalString.charAt(0))) {
            evalString = evalString.substring(1);
        }
        evalString = evalString.substring(aToken.tokenVal.length());
    }

}
