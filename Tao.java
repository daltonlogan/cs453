/**
 * Public Class tao
 * Used as the basis for building three address code
 * 
 * op: defines what operation this object is representing
 * 
 * src1: depending on the operation this will hold either the value of
 * the operation or the first reference
 * 
 * src2: the second reference for the object, will be empty when src1
 * is just holding a value for the operation
 * 
 * destination: the destination of the operation one it has finished
 */
public class Tao { // Three Address Object
	enum Operation{
		NUM, PLUS, MINUS, MUL, DIV, LT, LTE, GT, GTE, LEFTPAREN, RIGHTPAREN, EQUALS, NOTEQUALS, ASSIGN, ID, INT, SEMICOLON, LEFTCURLY, RIGHTCURLY, IF, WHILE, START_WHILE, AND, OR, VOID, PUBLIC, PRIVATE, CLASS, PROGRAM, INVALID, LABEL, GOTO
	}
	
	/** Values for Tao class */
	Operation op;
	Operand src1;
	Operand src2;
	Operand destination;
	
	/*
	 * This method should never be called, it is here for testing
	 */
	Tao(Operation oper){
		op = oper;
	}

	Tao(Tao aTao){
		op = aTao.op;
		src1 = aTao.src1;
		src2 = aTao.src2;
		destination = aTao.destination;
	}

	/*
	 * This method covers all operations that need only one source or destination
	 * 
	 * This currently covers labels and goto
	 */
	Tao(Operation oper, Operand value){
		if(oper == Operation.LABEL) {
			op = oper;
			src1 = value;
		}
		else if(oper == Operation.START_WHILE)
		{
			op = oper;
			src1 = value;
		}
		else {// if(oper == Operation.GOTO) {
			op = oper;
			destination = value;
		}
	}
	
	/*
	 * This method covers all operations that need only one source and destination
	 * 
	 * This currently covers assign
	 */
	Tao(Operation oper, Operand value, Operand dest){
		op = oper;
		src1 = value;
		destination = dest;
	}
	
	/*
	 * This method covers all operations that need both sources
	 * 
	 * This currently covers plus, minus, mult, div, gt, gte, lt, lte
	 */
	Tao(Operation oper, Operand one, Operand two, Operand dest){
		op = oper;
		src1 = one;
		src2 = two;
		destination = dest;
	}
	
	/*
	 * toString that changes based on the type of operation it is
	 */
	public String toString() {
		String ans = "";
		
		switch(op) {
			case ASSIGN:
				ans = this.destination.toString() + " = " + this.src1.toString() + "\n";
				break;
			case NUM:
				ans = destination.toString() + " = " + src1.toString() + "\n";
				break;
			case PLUS:
				ans = this.destination.toString() + " = " + this.src1.toString() + " + " + this.src2.toString() + "\n";
				break;
			case MINUS:
				ans = this.destination.toString() + " = " + this.src1.toString() + " - " + this.src2.toString() + "\n";
				break;
			case MUL:
				ans = this.destination.toString() + " = " + this.src1.toString() + " * " + this.src2.toString() + "\n";
				break;
			case DIV:
				ans = this.destination.toString() + " = " + this.src1.toString() + " / " + this.src2.toString() + "\n";
				break;
			case LT:
				ans = "IF_LT: " + this.src1.toString() + ", " + this.src2.toString() + ", " + "trueLabel" + this.destination.toString() + "\n";
				break;
			case GT:
				ans = "IF_GT: " + this.src1.toString() + ", " + this.src2.toString() + ", " + "trueLabel" + this.destination.toString() + "\n";
				break;
			case LTE:
				ans = "IF_LTE: " + this.src1.toString() + ", " + this.src2.toString() + ", " + "trueLabel" + this.destination.toString() + "\n";
				break;
			case GTE:
				ans = "IF_GTE: " + this.src1.toString() + ", " + this.src2.toString() + ", " + "trueLabel" + this.destination.toString() + "\n";
				break;
			case LABEL:
				ans = this.src1.toString() + "\n";
				break;
			case GOTO:
				ans = "GOTO: falseLabel" + this.destination.toString() + "\n";
				break;
			case EQUALS:
				ans = "IF_EQ: " + this.src1.toString() + ", " + this.src2.toString() + ", " + "trueLabel" + this.destination.toString() + "\n";
				break;
			case NOTEQUALS:
				ans = "IF_NE: " + this.src1.toString() + ", " + this.src2.toString() + ", " + "trueLabel" + this.destination.toString() + "\n";
				break;
			case IF: // destination for IF statement should be where it goes if true
				ans = "falseLabel" + this.destination.toString() + "\n";
				break;
			case START_WHILE:
				ans = "repeatLabel" + src1.toString() + "\n";
				break;
			case WHILE: //src1 is the repeating label while the statement continues to be true and the destination is for where it goes after false
				ans = "GOTO: repeatLabel" + this.src1.toString() + "\n" + "falseLabel" + this.destination.toString() + "\n";
 				break;
			default:
				ans = "ERROR there is no toString for this operation: " + op.name();
		}
		
		return ans;
	}
}
