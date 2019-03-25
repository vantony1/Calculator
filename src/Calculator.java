import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Pattern;

public class Calculator {
	
	//Datastructures required for this project
	static Stack<Float> operands = new Stack<Float>();
	static Stack<Character> operators = new Stack<Character>();
	static HashMap<String, Float> contents = new HashMap();
	static ArrayList<Character> trigFunction = new ArrayList<>();			
	
	//method to convert from in-fix to post-fox
	//aslo checks for valid and well-formed expressions
	public static void process_input(String string) {
		
		//clears operands and operators stack before starting new calculation 
		operands.clear();
		operators.clear();
		
		//Changes input to a char array
		char[]data = string.toCharArray();
		
		System.out.println("Processing Input: " + Arrays.toString(data));
		
		String holder = ""; 
		float current = 0;
		
		
		//Checks and handles paranthesis mismatches
		boolean round = false;
		boolean roundx = false;
		boolean square = false;
		boolean squarex = false;
		boolean curly = false;
		boolean curlyx = false;
		boolean parenthesisMismatch = false; 
		
		int c = 0;
		int s = 0;
		int r = 0;
		
		//Iterates through the input and checks for input mismatch
		for(int i = 0; i < data.length; i++) {
			
			switch(data[i]) {
			case '[':
				
				s++;
				
				square = true; 
				
				break;
			case ']':
				
				s--;
				
				squarex = true; 
				
				if(curlyx || roundx) {
					throw new IllegalArgumentException("Expression Not Well Formed: Parenthesis Mismatch");
				}
				
				
				break;
			case '{':
				
				c++;
				
				curly = true;
				
				break;
			case '}':
				
				c--;
				
				curlyx = true;
				
				if(squarex || roundx) {
					throw new IllegalArgumentException("Expression Not Well Formed: Parenthesis Mismatch");
				}
				
				break;
			case '(':
				
				
				r++;
				round = true;
				
				break;
			case ')':
				
				r--;
				
				roundx = true;
				
				if(curlyx || squarex) {
					throw new IllegalArgumentException("Expression Not Well Formed: Parenthesis Mismatch");
				}
				
				break;
			default:
				break;
			}
		}
		
		if(r!=0 || c!=0 || s!=0) {
			
			throw new IllegalArgumentException("Expression Not Well Formed: Parenthesis Mismatch");
			
		}
		
		for(int i = 0; i < data.length; i++) {
			
			switch(data[i]) {
			case '[':
		
				data[i] = '(';
				break;
			case ']':
				data[i] = ')';
				break;
			case '{':
				data[i] = '(';
				break;
			case '}':
				data[i] = ')';
				break;
			default:
				break;
			}
		}
		
		
		//Runs through the char array to push objects into appropriate stacks
		//preparing the data for shunting yard
		for(int i = 0; i < data.length; i++) {
			
			
			System.out.print(
				    operators.toString().replaceAll("\\[", "").replaceAll("]", ""));
			System.out.println();
			System.out.print(
				    operands.toString().replaceAll("\\[", "").replaceAll("]", ""));
			System.out.println();
			
			
			//uses switch cases to determine hierarchy and performs appropriate actions
			switch(data[i]) {
			
			
			case ' ' : 
				
				
				if(isNumber(holder) && Character.isDigit(data[i++])) {
					throw new IllegalArgumentException("Invalid Expression");
				}
				
				else if(isNumber(holder)) {
					current = Float.parseFloat(holder);
			        operands.push((float)current);
			        holder = "";
				}
				
				
				break;
	
		    case '+':
		    	
		    		if (holder != "" && !isNumber(holder)) {
		    		
		    		operands.push(handleVariable(holder));
		    		
		    		} else if (holder != "") {
		        current = Float.parseFloat(holder);
		        operands.push((float)current);
		        holder = "";
		    		} 
		    		
		        if(!operators.isEmpty() && (operators.peek() == '*' || operators.peek() == '/' || trigFunction.contains(operators.peek()))) {
		       
		       
		        		while(!operators.isEmpty() && operators.peek() != '(') { 		 	
		        			
		        		if(operators.peek() == '+') {
		        			
		        			operands.push(operands.pop()+operands.pop());
		        			
		        		} else if (operators.peek() == '-') {
		        			
		        			operands.push(operands.pop()-operands.pop());
		        			
		        		} else if (operators.peek() == '*') {
		        			
		        			operands.push(operands.pop()*operands.pop());
		        			
		        		} else if (operators.peek() == '/') {
		        			
		        			float divider = operands.pop();
		        			float divisor = operands.pop();
		    				
		    				operands.push(divisor/divider);
		        			
		        		} else if(operators.peek().equals('S')) {
		        			operands.push((float) Math.sin(Math.toRadians(operands.pop())));
		    			}  else if (operators.peek().equals('C')) {
		    				operands.push((float) Math.cos(Math.toRadians(operands.pop())));
		    			} else if (operators.peek().equals('T')) {
		    				
		    				float angle = operands.pop(); 
		    				
		    				if(Math.cos(angle) != 0) {
		    					operands.push((float) Math.tan(angle));
		    				} else {
		    					throw new IllegalArgumentException("Cannot take the tangent of " + angle);
		    				}
		    				
		    			} else if (operators.peek().equals('t')) {
		    				
		    				float angle = operands.pop(); 
		    				
		    				if(Math.sin(angle) != 0) {
		    					operands.push((float) Math.tan(angle));
		    				} else {
		    					throw new IllegalArgumentException("Cannot take the co-tangent of " + angle);
		    				}
		    				
		    				
		    			} else if (operators.peek().equals('s')) {
		    				operands.push((float) Math.asin(Math.toRadians(operands.pop())));
		    				
		    			} else if (operators.peek().equals('c')) {
		    				operands.push((float) Math.acos(Math.toRadians(operands.pop())));
		    				
		    			} else if (operators.peek().equals('M')) {
		    				float one = operands.pop();
		    				float two = operands.pop();
		    			
		    				
		    				System.out.println("Maxing: " + Math.max(one, two));
		    				operands.push((float) Math.max(one, two));
		    			} else if (operators.peek().equals('m')) {
		    				
		    				float one = operands.pop();
		    				float two = operands.pop();
		    			
		    				operands.push((float) Math.min(one, two));
		    			}
		        			
		        		operators.pop();
		        		
		        		}
		        } 
		        		
		        
		        System.out.println("pushing +");
		        operators.push('+');
		        holder = "";
		        break;
		        
		    case ',':
		    	
		    		current = Float.parseFloat(holder);
		        operands.push((float)current);
		        holder = "";
		    	
		        break;
		        
		    case '-':
		    	
		    		 if (holder != "" && isNumber(holder)) {
		    			current = Float.parseFloat(holder);
			        operands.push((float)current);
			        System.out.println("pushing: " + (float)current);
			    	
		    		} else if (holder == "" || isOnlyOperators(holder)) {
			    			System.out.println("Adding" + data[i]);
			    			holder += data[i];
			    			break;
			    	} else if (holder != "" && !isNumber(holder) && !containsDigits(holder)) {
			    		
			    		operands.push(handleVariable(holder));
			    		
			    	} else {
			    			System.out.println("Handling Negation: " + holder);
			    			System.out.println(negationHandler(holder));
			    			operands.push(negationHandler(holder));
			    			System.out.println("pushing: " + negationHandler(holder));
			    		}
		    
		    		
		        if(!operators.isEmpty() && (operators.peek() == '*' || operators.peek() == '/' || trigFunction.contains(operators.peek()))) {
				       
	        		while(!operators.isEmpty() && operators.peek() != '(') {
	        		
	        			if(operators.peek() == '+') {
		        			
		        			operands.push(operands.pop()+operands.pop());
		        			
		        		} else if (operators.peek() == '-') {
		        			
		        			operands.push(operands.pop()-operands.pop());
		        			
		        		} else if (operators.peek() == '*') {
		        			
		        			operands.push(operands.pop()*operands.pop());
		        			
		        		} else if (operators.peek() == '/') {
		        			
		        			float divider = operands.pop();
		        			float divisor = operands.pop();
		    				
		    				operands.push(divisor/divider);
		        			
		        		} else if(operators.peek().equals('S')) {
		        			operands.push((float) Math.sin(Math.toRadians(operands.pop())));
		    			}  else if (operators.peek().equals('C')) {
		    				operands.push((float) Math.cos(Math.toRadians(operands.pop())));
		    			} else if (operators.peek().equals('T')) {
		    				
		    				float angle = operands.pop(); 
		    				
		    				if(Math.cos(angle) != 0) {
		    					operands.push((float) Math.tan(angle));
		    				} else {
		    					throw new IllegalArgumentException("Cannot take the tangent of " + angle);
		    				}
		    				
		    			} else if (operators.peek().equals('t')) {
		    				
		    				float angle = operands.pop(); 
		    				
		    				if(Math.sin(angle) != 0) {
		    					operands.push((float) Math.tan(angle));
		    				} else {
		    					throw new IllegalArgumentException("Cannot take the tangent of " + angle);
		    				}
		    				
		    				
		    			} else if (operators.peek().equals('s')) {
		    				operands.push((float) Math.asin(Math.toRadians(operands.pop())));
		    				
		    			} else if (operators.peek().equals('c')) {
		    				operands.push((float) Math.acos(Math.toRadians(operands.pop())));
		    				
		    			} else if (operators.peek().equals('M')) {
		    				System.out.println("popping");
		    				
		    				float one = operands.pop();
		    				float two = operands.pop();
		    						
		    				
		    				operands.push((float) Math.max(one, two));
		    			} else if (operators.peek().equals('m')) {
		    				
		    				float one = operands.pop();
		    				float two = operands.pop();
		    				operands.push((float) Math.min(one, two));
		    			}
		        			
		        		operators.pop();
	        		
	        		}
	        } 
		        
		        operators.push('-');
		        holder = "";
		        break;
		        
		    case '*':
		    	
		    	if (holder != "" && !isNumber(holder)) {
		    		
		    		System.out.println("pulling: " + holder + " == " + handleVariable(holder));
		    		operands.push(handleVariable(holder));
		    		
		    	} else if (holder != "") {
		    			current = Float.parseFloat(holder);
			        operands.push((float)current);
			}
		    	
		        operators.push('*');
		        holder = "";
		        break;
		        
		    case '/':
			
		    		if(!isNumber(holder)) {
		    			current = negationHandler(holder); 
		    			operands.push((float)current);
		    		} else if (holder != "") {
			        current = Integer.parseInt(holder);
			        operands.push((float)current);
			    	}
		    	
		        operators.push('/');
		        holder = "";
		        break;
		        
		    case '=':
		    	
		    		String value = "";
		    		
		    		int position = i;
		    		i++;
		    		while(i < data.length && data[i] != '=' ) {
		    			value += data[i];
		    			System.out.println(value + ": VALUE");
		    		
		    			
		    			i++;
		    			
		    		}
		    		
		    		contents.put(holder, Float.parseFloat(value));
		    		
		    		holder = "";
		    		i = position; 
		    	
		    		break; 

		    case '(':
		    	
		    	boolean pushed = false;
		    		System.out.println("holder :"+holder);
		    		if (holder != "") {
		    			if(holder.equals("sin")) {
		    				pushed = true;
		    				operators.push('(');
		    				operators.push('S');
		    			} else if (holder.equals("-")) {
		    				operators.push('-');
		    			} else if (holder.equals("+")) {
		    				
		    				operators.push('+');
		    			} else if (holder == "/") {
		    				operators.push('/');
		    			} else if (holder == "*") {
		    				operators.push('*');
		    			} else if (holder.equals("cos")) {
		    				pushed = true;
		    				operators.push('(');
		    				operators.push('C');
		    			} else if (holder.equals("tan")) {
		    				pushed = true;
		    				operators.push('(');
		    				operators.push('T');
		    			} else if (holder.equals("cot")) {
		    				pushed = true;
		    				operators.push('(');
		    				operators.push('t');
		    			} else if (holder.equals("csc")) {
		    				pushed = true;
		    				operators.push('(');
		    				operators.push('s');
		    			} else if (holder.equals("sec")) {
		    				pushed = true;
		    				operators.push('(');
		    				pushed = true;
		    				operators.push('(');
		    				operators.push('c');
		    			} else if (holder.equals("max")) {
		    				pushed = true;
		    				operators.push('(');
		    				operators.push('M');
		    			} else if (holder.equals("min")) {
		    				pushed = true;
		    				operators.push('(');
		    				operators.push('m');
		    			} else {
		    				current = Float.parseFloat(holder);
		    				operands.push((float)current);
		    				
		    				
		    				holder = "";
		    				break;
		    			}
		    		}
		    		
		    		if(!pushed) {
		    		operators.push('(');
		    		}
		        holder = "";
		    		break;
		   
		    case ')':
		    	
		    	System.out.println("Encountered closer");
		      	System.out.println(holder);
		    	
		    	if (holder != "" && !isNumber(holder)) {
		    		
		    		System.out.println("pulling: " + holder + " == " + handleVariable(holder));
		    		operands.push(handleVariable(holder));
		    		
		    	} else if (holder != "") {
		    		
		    		
		    	 	System.out.println("parsing");
		    			current = Float.parseFloat(holder);
		    			System.out.println(current);
			        operands.push((float)current);
			        
			        holder = "";
			        
			    }
		    		
		    		
		    		while(!operators.isEmpty() && operators.peek() != '(') {
		    			
		    			if(operators.peek() == '+') {
		        			
		    				operands.push(operands.pop()+operands.pop());

		    				
		    			} else if (operators.peek() == '-') {
		    				
		    			 
		    				float subtractor = operands.pop();
		    				float subtractee = operands.pop();
		    	
		    					
		    					Character now = operators.pop();
		    					
		    					if(!operators.isEmpty() && operators.peek() == '-') {
		    						operands.push(-subtractee-subtractor);
		        					System.out.println("Subtracting * negation: " + subtractee + " - " + subtractor);
		    					} else {
		    						operands.push(subtractee-subtractor);
		        					System.out.println("Subtracting * normal: " + subtractee + " - " + subtractor);
		    					}
		    					
		    					operators.push(now);
		    			
		    			} else if (operators.peek() == '*') {
		    			
		    				operands.push(operands.pop()*operands.pop());
		
		    			
		    			} else if (operators.peek() == '/') {
		    			
		    				float divider = operands.pop();
		    				float divisor = operands.pop();
		    				
		    				if(divider == 0) {
		    					throw new IllegalArgumentException("Division by 0 is illegal");
		    				}
		    				
		    				operands.push(divisor/divider);
		    			
		        		} else if(operators.peek().equals('S')) {
		        			operands.push((float) Math.sin(Math.toRadians(operands.pop())));
		    			}  else if (operators.peek().equals('C')) {
		    				operands.push((float) Math.cos(Math.toRadians(operands.pop())));
		    			} else if (operators.peek().equals('T')) {
		    				
		    				float angle = operands.pop(); 
		    				
		    				if(Math.cos(angle) != 0) {
		    					operands.push((float) Math.tan(angle));
		    				} else {
		    					throw new IllegalArgumentException("Cannot take the tangent of " + angle);
		    				}
		    				
		    			} else if (operators.peek().equals('t')) {
		    				
		    				float angle = operands.pop(); 
		    				
		    				if(Math.sin(angle) != 0) {
		    					operands.push((float) Math.tan(angle));
		    				} else {
		    					throw new IllegalArgumentException("Cannot take the co-tangent of " + angle);
		    				}
		    				
		    				
		    			} else if (operators.peek().equals('s')) {
		    				operands.push((float) Math.asin(Math.toRadians(operands.pop())));
		    				
		    			} else if (operators.peek().equals('c')) {
		    				operands.push((float) Math.acos(Math.toRadians(operands.pop())));
		    				
		    			} else if (operators.peek().equals('M')) {
		    				float one = operands.pop();
		    				float two = operands.pop();
		    			
		    				
		    				System.out.println("Maxing: " + Math.max(one, two));
		    				operands.push((float) Math.max(one, two));
		    			} else if (operators.peek().equals('m')) {
		    				
		    				float one = operands.pop();
		    				float two = operands.pop();
		    			
		    				operands.push((float) Math.min(one, two));
		    			}
		        			
		    			
					
		    			holder = "";
		    			
		    			
					operators.pop();
		    			
		    		}
		    		
		    		if(!operators.isEmpty()) {
						operators.pop();
			    		}
		    	
		    		break;
		    		
		    case '.':
		    	
		    		holder += data[i];
		    		
		    		break;
		    
		    default:
		    		
		    			
		    	
		    			holder += data[i];
		    		
		    			
		    			if(holder.equals("clearall")) {
		    				
		    				contents.clear();
		    				holder = "";
		    				
		    			} else if (holder.equals("showall")) {
		    		
		    				
		    				for (String name: contents.keySet()){

		    		            String key =name.toString();
		    		            String content = contents.get(name).toString();  
		    		            System.out.println(key + " " + content);  


		    		} 
		    				holder = "";
		    				
		    			} else if (holder.equals("clearvar")) {
		    				
		    				
		    				holder = "";
		    				i++;
		    				while(i < data.length) {
		    					if(data[i] != ' ') {
		    					holder += data[i];
		    					}
		    					i++;
		    				}
		    				
		    				if(contents.containsKey(holder)) {
		    					System.out.println("Deleting Variable " + holder);
		    					contents.remove(holder);
		    					
		    				} else {
		    					
		    					throw new IllegalArgumentException("Variable does not exist");
		    				}
		    				
		    				
		    			}
		    	
		    		
		    			
		    		
		    		break;
			}
			
			
		}
		
		
		if(isNumber(holder)) {
			current = Float.parseFloat(holder);
			operands.push((float)current);
			
		} else if(data[data.length-1] != ')' && Character.isDigit(data[data.length-1])) {
		holder = "";
		holder += data[data.length-1];
		current = Float.parseFloat(holder);
		operands.push((float)current);
		}
		
		
		System.out.print("FINAL OPERATORS:");
		System.out.print(
			    operators.toString().replaceAll("\\[", "").replaceAll("]", ""));
		System.out.println();
		
		System.out.print("FINAL OPERANDS:");
		System.out.print(
			    operands.toString().replaceAll("\\[", "").replaceAll("]", ""));
		System.out.println();
	}
	
	
	//Handles variables by getting their vales from the hashmap
	public static float handleVariable(String holder) {
		float now = 0; 
		try {
			now = contents.get(holder);
		} catch (NullPointerException problem) {
			System.out.println("Variable not defined: " + holder);
			throw new IllegalArgumentException("Undefined Variable");
			
		}
		
		return now;
		
	}
	
	//checks whether string has digits
	public static boolean containsDigits(String holder) {
		
		boolean answer = false; 
		
		char[]now = holder.toCharArray();
		
		for (int i = 0; i < holder.length(); i++) {
			if(Character.isDigit(now[i])) {
				answer = true;
			}
		}
		
		return answer;
	}
	
	//checks whether stacks has said functions
	
	public static boolean containsFunctions(Stack<Character> operators) {
		
		char[]functions = new char[] {'S','C','T','s','c','t','M', 'm'};
		
		for(int i = 0; i < operators.size(); i++) {
			for(int x = 0; x < operators.size(); x++) {
			if(operators.get(i).equals(functions[x])) {
				return true;
			}
		}
		}
		return false;
		
	}
	
	
	//Shunting yard method to find off the calculation by evaluating the post-fix expression
	public static float shunting_yard(Stack<Float> operands, Stack<Character> operators) {
		
		
		
		//runs while operator is not empty and perform appropriate functions
		while((!operators.isEmpty() && operands.size() > 1) || containsFunctions(operators)) {
			
			if(operators.peek().equals('+')) {
    			
    				operands.push(operands.pop()+operands.pop());
    				System.out.println(operators.peek());
    				System.out.println(operands.peek());
    			
    			} else if (operators.peek().equals('-')) {
    				
    				
    				
    				float subtractor = operands.pop();
    				float subtractee = operands.pop();
    				
    				
    					
    					
    					Character holder = operators.pop();
    					
    					if(!operators.isEmpty() && operators.peek().equals('-')) {
    						operands.push(-subtractee-subtractor);
        					System.out.println("Subtracting * negation: " + subtractee + " - " + subtractor);
    					} else {
    						operands.push(subtractee-subtractor);
        					System.out.println("Subtracting * normal: " + subtractee + " - " + subtractor);
    					}
    					
    					operators.push(holder);
    				
    				   				
    				
    			
    			} else if (operators.peek().equals('*')) {
    			
    				operands.push(operands.pop()*operands.pop());
    				System.out.println(operators.peek());
    				System.out.println(operands.peek());
    			
    			} else if (operators.peek().equals('/')) {
    			
    				float divider = operands.pop();
    				float divisor = operands.pop();
    				
    				if(divider == 0) {
    					throw new IllegalArgumentException("Division by 0 is illegal");
    				}
    				
    				operands.push(divisor/divider);
    			
    			}else if(operators.peek().equals('S')) {
        			operands.push((float) Math.sin(Math.toRadians(operands.pop())));
    			}  else if (operators.peek().equals('C')) {
    				operands.push((float) Math.cos(Math.toRadians(operands.pop())));
    			} else if (operators.peek().equals('T')) {
    				
    				float angle = operands.pop(); 
    				
    				if(Math.cos(angle) != 0) {
    					operands.push((float) Math.tan(angle));
    				} else {
    					throw new IllegalArgumentException("Cannot take the tangent of " + angle);
    				}
    				
    			} else if (operators.peek().equals('t')) {
    				
    				float angle = operands.pop(); 
    				
    				if(Math.sin(angle) != 0) {
    					operands.push((float) Math.tan(angle));
    				} else {
    					throw new IllegalArgumentException("Cannot take the tangent of " + angle);
    				}
    				
    				
    			} else if (operators.peek().equals('s')) {
    				operands.push((float) Math.asin(Math.toRadians(operands.pop())));
    				
    			} else if (operators.peek().equals('c')) {
    				operands.push((float) Math.acos(Math.toRadians(operands.pop())));
    				
    			} else if (operators.peek().equals('M')) {
    				System.out.println("popping");
    				
    				float one = operands.pop();
    				float two = operands.pop();
    						
    				
    				operands.push((float) Math.max(one, two));
    			} else if (operators.peek().equals('m')) {
    				
    				float one = operands.pop();
    				float two = operands.pop();
    				operands.push((float) Math.min(one, two));
    			}  else if (operators.peek() == '(') {
        			
        			continue;
        		} else if (operators.peek() == ')') {
        		
        			continue;
        		}
			System.out.println("Popping");
			
			
			operators.pop();
			
			System.out.print("OPERATORS:");
			System.out.print(
				    operators.toString().replaceAll("\\[", "").replaceAll("]", ""));
			System.out.println();
			
			System.out.print("OPERANDS:");
			System.out.print(
				    operands.toString().replaceAll("\\[", "").replaceAll("]", ""));
			System.out.println();
			
		}
		System.out.println("Returning");
		
		if (!operands.empty()) {
		return operands.pop();
		} else {
			return (float) 0;
		}
	}
	
	
	//method to see wether string is a number
	public static boolean isNumber(String holder) {
		try {
			float current = Float.parseFloat(holder);
		} catch (NumberFormatException error) {
			return false;
		}
		
		return true;
	}
	
	//method to handle negation
	public static float negationHandler(String holder) {
		
		char[]current = holder.toCharArray();
		int negation = 0;
		float ret_val = 0;
		String now = "";
		
		for(int i = 0; i < current.length; i++) {
			
			if(current[i] == '-') {
				negation += 1; 
			} else {
				now += current[i];
			}
			
		}
		
		if(negation % 2 == 0) {
			ret_val = (float) Integer.parseInt(now); 
		} else {
			ret_val = (float) Integer.parseInt(now); 
			ret_val = ret_val - (2*ret_val);
		}
		
		return ret_val;
	}
	
	//method to see if string only has operators
	public static boolean isOnlyOperators(String holder) {
		
		char[]current = holder.toCharArray();
		
		System.out.println(current.length);
		
		for(int i = 0; i < current.length; i++) {
			
			if(current[i] != '-' && current[i] != '+') {
				return false;
			} 
			
		}
		
		return true; 
		
	}
	
	//main method to run the program appropriately
	public static void main(String[]args) {

		trigFunction.add('S');trigFunction.add('C');trigFunction.add('T');
		trigFunction.add('s');trigFunction.add('c');trigFunction.add('t');
		trigFunction.add('M');trigFunction.add('m');
		
		
		System.out.println("Enter your Expression:");
		
		Scanner input = new Scanner(System.in);
		
		while(!input.equals("exit")) {
			process_input(input.nextLine());
			System.out.println(shunting_yard(operands, operators));
			System.out.println("Enter your Expression:");
		}
		
		
		

	
	}
	
}
