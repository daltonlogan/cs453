
import java.lang.String;
import java.util.*;

public class EvalParser {
    Scanner scan = new Scanner();
    String evalString;

    int tempID = 0;
    String threeAddressResult = "";
    ArrayList<Integer> IDs = new ArrayList<Integer>();
    private int parenCounter = 0;
    boolean expressionInside;

    /***************** Simple Expression Evaluator ***********************/

    private int E() {
        int result = T();
        while (true) {
            Scanner.Token nextToken = lookahead();
            if (nextToken != null && nextToken.tokenType == Scanner.TokenType.PLUS) {
                match(nextToken);
                result += T();
                continue;
            } else if (nextToken != null && nextToken.tokenType == Scanner.TokenType.MINUS) {
                match(nextToken);
                result -= T();
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
                continue;
            } else if (nextToken != null && nextToken.tokenType == Scanner.TokenType.DIV) {
                match(nextToken);
                result /= F();
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

    /***************** Three Address Translator ***********************/

    private int threeE() {
        int result = threeT();
        while (true) {
            Scanner.Token nextToken = lookahead();
            if (nextToken != null && nextToken.tokenType == Scanner.TokenType.PLUS) {
                match(nextToken);
                result += threeT();
                int first = IDs.remove(IDs.size() - 1);
                int second = IDs.remove(IDs.size() - 1);
                IDs.add(tempID);
                threeAddressResult += "temp" + tempID++ + " = " +
                        "temp" + second + " + " +
                        "temp" + first + "\n";
                continue;
            } else if (nextToken != null && nextToken.tokenType == Scanner.TokenType.MINUS) {
                match(nextToken);
                result -= threeT();
                int first = IDs.remove(IDs.size() - 1);
                int second = IDs.remove(IDs.size() - 1);
                IDs.add(tempID);
                threeAddressResult += "temp" + tempID++ + " = " +
                        "temp" + second + " - " +
                        "temp" + first + "\n";
                continue;
            }
            return result;
        }
    }

    private int threeT() {
        int result = threeF();
        while (true) {
            Scanner.Token nextToken = lookahead();
            if (nextToken != null && nextToken.tokenType == Scanner.TokenType.MUL) {
                match(nextToken);
                result *= threeF();
                int first = IDs.remove(IDs.size() - 1);
                int second = IDs.remove(IDs.size() - 1);
                IDs.add(tempID);
                threeAddressResult += "temp" + tempID++ + " = " +
                        "temp" + second + " * " +
                        "temp" + first + "\n";
                continue;
            } else if (nextToken != null && nextToken.tokenType == Scanner.TokenType.DIV) {
                match(nextToken);
                result /= threeF();
                int first = IDs.remove(IDs.size() - 1);
                int second = IDs.remove(IDs.size() - 1);
                IDs.add(tempID);
                threeAddressResult += "temp" + tempID++ + " = " +
                        "temp" + second + " / " +
                        "temp" + first + "\n";
                continue;
            }
            return result;
        }
    }

    private int threeF() {
        Scanner.Token nextToken = lookahead();
        int result = 0;
        if (nextToken != null && nextToken.tokenType == Scanner.TokenType.LEFTPAREN) {
            match(nextToken);
            nextToken = lookahead();
            while (nextToken != null && nextToken.tokenType != Scanner.TokenType.RIGHTPAREN) {
                result += threeE();
                nextToken = lookahead();
            }
            match(nextToken);
        } else if (nextToken != null && nextToken.tokenType == Scanner.TokenType.NUM) {
            int num = Integer.parseInt(nextToken.tokenVal);
            IDs.add(tempID);
            threeAddressResult += "temp" + tempID++ + " = " + num + "\n";
            match(nextToken);
            return num;
        }
        return result;
    }

    public String getThreeAddr(String eval) {
        this.threeAddressResult = "";
        evalString = eval;
        tempID = 0;
        IDs.clear();
        threeE();
        return this.threeAddressResult;
    }

    /****************************************/

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
