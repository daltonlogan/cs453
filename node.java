/*
 * Any leaf node there should only be one child, this will be the left node
 * Any interior node should have two children, what is left of the operation and what is to the right
 */

import java.util.LinkedList;

public class node{
    node left = null;
    node right = null;
    String value;
    LinkedList<node> stmts = new LinkedList<>(  );
    Scanner.TokenType type = Scanner.TokenType.INVALID;
    String op = "";
    int loc;
    int rLoc;
    int fLoc;
    int tLoc;

    node()
    {

    }

    node( node aNode )
    {
        this.left = aNode.left;
        this.right = aNode.right;
        this.value = aNode.value;
        this.stmts = aNode.stmts;
        this.type = aNode.type;
        this.op = aNode.op;
    }

    node( Scanner.TokenType type){
        this.type = type;
        this.stmts = new LinkedList<>(  );
    }

    node( Scanner.TokenType type, String value){
        this.type = type;
        this.value = value;
        this.stmts = new LinkedList<>(  );
    }

    public String toString() {
        String val = "Node type:" + type + ", Node value:" + value;
        if(this.type != Scanner.TokenType.NUM)
            val += ", Left Child (" + this.left.toString() + ")" + ", Right Child (" + this.right.toString() + ")";
        return val;
    }
}