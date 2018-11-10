import java.util.Stack;

public class test {

	public static void main(String[]args) {
		
		
		Stack<Float> operands = new Stack<Float>();
		Stack<Character> operators = new Stack<Character>();
		
		operators.push('-');
		operators.push('+');
		operators.push('-');
		
		System.out.println(operators.get(1));
		
		float current = (float)Integer.parseInt("-1");
		operands.push(current);
		
		System.out.println(operands.peek());
	}
	
}
