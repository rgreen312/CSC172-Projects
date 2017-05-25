//Author: Ryan Green
//Collaborated with Sam Triest

package project2;

import java.io.*;
import java.util.Scanner;

public class Infix {
	//Set the precedence of different operators in a double array
	//This array will be checked to see which of two operators is higher precedence
	private static String[][] precedence = {{")"}, {"sin", "cos", "tan"}, {"^"}, {"*", "/", "%"}, {"+", "-"}, {"<", ">", "="}, {"!"}, {"&"}, {"|"}};
	
	public static void main(String[] args) {
		//create scanner to get user input from the console
		Scanner s = new Scanner(System.in); 
		//Get name of input file from the user
		System.out.println("Enter the input file name: ");
		String inFileName = s.nextLine();
		System.out.println("Enter the output file name: ");
		String outFileName = s.nextLine();
		s.close(); //close scanner	
		String outputString = "";
		try {
			File f = new File(inFileName); //find file user gave
			Scanner inFile = new Scanner(f); //create scanner on file
			while (inFile.hasNextLine()) { //for each line in the file
				String expression = inFile.nextLine(); //get that line
				MyQueue m = infix_to_postfix(expression); //convert that line to a postfix queue
				Double result = postfixEvaluation(m); //evaluate the postfix expression to get the result
				outputString += result+"\n";
			}
			inFile.close();
		}
		catch (FileNotFoundException e) { //if file can't be found, print error message
			System.out.println("File not found");
		}
		try {
			File output = new File(outFileName);
			PrintStream p = new PrintStream(output);
			p.print(outputString);
		}
		catch (FileNotFoundException e) {
			System.out.println("Output file not found");
		}
	}
	/**
	 * This method takes a mathematical expression in the form of a string, and converts it into a postfix queue.
	 * 
	 * @param infix The mathematical expression in string form.
	 * @return The postfix queue containing the correct order of elements to calculate the answer
	 */
	public static MyQueue<Object> infix_to_postfix(String infix) {
		MyQueue<Object> postfixQueue = new MyQueue<Object>(); //create postfix queue
		MyStack<String> operators = new MyStack<String>(); //create operator to temporarily store operators
		//Add spaces around parentheses so the expression can be split apart more easily
		//also do the same for the logical ! operator and trig functions
		infix = infix.replace("(", "( "); 
		infix = infix.replace(")", " )");
		infix = infix.replace("!", "! ");
		infix = infix.replace("sin", "sin ");
		infix = infix.replace("cos", "cos ");
		infix = infix.replace("tan", "tan ");
		String[] characters = infix.split(" "); //split the string into a string array of each operator and operand
		//for each element in the array
		for (int i = 0; i < characters.length; i++) {		
			if (Character.isDigit(characters[i].charAt(0))) { //if this element is a number
				Double operand = Double.parseDouble(characters[i]); //make this element an operand
				postfixQueue.enqueue(operand); //enqueue operands
			}
			else if (characters[i].equals("(")) { // if it is an open parenthesis
                operators.push(characters[i]); //push it to the top of the stack
            }
			else if (characters[i].equals(")")) { //if it is a close parenthesis
				//pop all operators from the stack until an open parenthesis is found
				while (!operators.isEmpty() && !operators.peek().equals("(")) {
					postfixQueue.enqueue(operators.pop()); //enqueue the operator on the top of the stack
				}
				String open = operators.pop(); //pop the open parenthesis to remove if from the stack
			}
			else { //if character is an operator
				//pop operators from the stack until one of lower precedence is found, then push new operator to stack
  				while (!operators.isEmpty() && isHigherPrecedence(operators.peek(), characters[i]) && !operators.peek().equals("(")) {
					postfixQueue.enqueue(operators.pop());
				}
				operators.push(characters[i]);
			}
		}
		//After all parts of expression have been added to the stack or queue
		while (operators.peek() != null) { //pop all operators in stack and add them to the queue
			postfixQueue.enqueue(operators.pop());
		}
		return postfixQueue; //return queue with the postfix expresssion
	}
	/**
	 * Checks to see which operator has a higher precedence.
	 * @param op1 The first operator to be compared
	 * @param op2 The second operator to be compared
	 * @return True if the first operator is higher precedence than the second, false otherwise
	 */
	public static boolean isHigherPrecedence(String op1, String op2) {
      return (getPrecedence(op1) < getPrecedence(op2));
	}
	/**
	 * Gets the precedence of an operator in the form of an integer.
	 * @param op The operator to get the precedence of
	 * @return The precedence of the operator
	 */
	public static int getPrecedence(String op) {
      if (op == null) //if not an operator
         return -1;
      //Run through the double array defined at the top of this program to get the precedence number of an operator
		for (int i = 0; i < precedence.length; i++) {
			for (int j = 0; j <precedence[i].length; j++) {
				if (op.equals(precedence[i][j])) //if this operator is equal to the one in the array
					return i;
			}
		}
		return -1;
	}
	/**
	 * Evaluates a postfix expression and returns the result.
	 * @param postfix The postfix expression to be evaluated.
	 * @return The result of the postfix expression, in the form of a Double.
	 */
 	public static Double postfixEvaluation(MyQueue postfix) {
 		MyStack<Double> stack = new MyStack<Double>(); //create stack to store operands from the expression
 		while(postfix.peek() != null) { //while there are elements in the postfix array
 			if (postfix.peek() instanceof Double) { //if the element is an operand
 				stack.push((Double)postfix.dequeue()); //push the operand to the stack
 			}
 			else { //if the element is an operator
 				String operator = (String) postfix.dequeue(); //get operator from expression
 				Double result = 0.0;
 				if (!operator.equals("!") && !operator.equals("sin") && !operator.equals("cos") && !operator.equals("tan")) { //if the operator requires two operands to be performed
	 				//Get two operands from the stack
 					Double val2 = stack.pop();
	 				Double val1 = stack.pop();
	 				//Switch statement for all the different operators, perform the correct operation on the two operands
	 				switch (operator) {
	 					case "+":
	 						result = val1+val2;
	 						break;
	 					case "-":
	 						result = val1-val2;
	 						break;
	 					case "*":
	 						result = val1*val2;
	 						break;
	 					case "/":
	 						result = val1/val2;
	 						break;
	 					case "%":
	 						result = val1%val2;
	 						break;
	 					case "^":
	 						result = Math.pow(val1, val2);
	 						break;
	 					case "=":
	 						if (val1.equals(val2))
	 							result = 1.0;
	 						else 
	 							result = 0.0;
	 						break;
	 					case ">":
	 						result = val1>val2 ? 1.0 : 0.0;
	 						break;
	 					case "<":
	 						result = val1<val2 ? 1.0 : 0.0;
	 						break;
	 					case "&":
	 						if (val1 == 1.0 && val2 == 1.0)
	 							result = 1.0;
	 						else 
	 							result = 0.0;
	 						break;
	 					case "|":
	 						if (val1 == 1.0 || val2 == 1.0)
	 							result = 1.0;
	 						else
	 							result = 0.0;
	 						break;
	 				}
 				}
 				else if (operator.equalsIgnoreCase("!")){ //if the operator is !
 					//Get 1 value instead of 2
 					Double val = stack.pop();
 					if (val == 1) //flip value of boolean from 1 to 0 or 0 to 1
 						result = 0.0;
 					else
 						result = 1.0;
 				}
 				else { //if the operator is a trig function
 					Double val = stack.pop();
 					switch (operator) {
 						case "sin":
 							result = Math.sin(val);
 							break;
 						case "cos":
 							result = Math.cos(val);
 							break;
 						case "tan":
 							result = Math.tan(val);
 							break;
 					}
 				}
 				stack.push(result);
 			}		
 		}
 		return stack.pop(); //return final value in the stack, after all operations have been performed
 	}
}
