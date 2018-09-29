package com.haohang.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Stack;

import com.haohang.ration.Ration;

public class QuizUtils {

	private static HashSet<Character> num_set = new HashSet<>();

	static {
		num_set.add('0');
		num_set.add('1');
		num_set.add('2');
		num_set.add('3');
		num_set.add('4');
		num_set.add('5');
		num_set.add('6');
		num_set.add('7');
		num_set.add('8');
		num_set.add('9');
	}

	private QuizUtils() {
	}

	// public static void main(String[] args) {
	// HashSet<String> set = generateQuiz(10, 100);
	// for (String string : set) {
	// System.out.println(string);
	// String str = caclRPN(toRPN(string));
	// System.out.println(str);
	// System.out.println(Ration.toDaiFenShu(str));
	// System.out.println();
	// System.out.println();
	// }
	//
	// }

	public static HashSet<String> generateQuiz(int range, int num) {
		if (range == -1) {
			range = 10;
		}

		HashSet<String> set = new HashSet<>();
		while (set.size() < num) {
			String str = second(second(first(range), range), range);
			if (caclRPN(toRPN(str)).contains("-")) {
				continue;
			}
			set.add(str);
		}
		return set;
	}

	public static String caclRPN(String expr) {
		Stack<Ration> ration_stack = new Stack<>();
		char[] chars = expr.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (num_set.contains(c)) {
				int num = Integer.parseInt(String.valueOf(c));
				ration_stack.push(new Ration(num, 1));
			} else {
				Ration r2 = ration_stack.pop();
				Ration r1 = ration_stack.pop();
				if (c == '+') {
					ration_stack.push(r1.add(r2));
				}
				if (c == '-') {
					ration_stack.push(r1.sub(r2));
				}
				if (c == '*') {
					ration_stack.push(r1.mul(r2));
				}
				if (c == '/') {
					ration_stack.push(r1.div(r2));
				}
			}
		}
		return ration_stack.pop().toString();
	}

	public static String first(int range) {
		Random random = new Random();
		String[] op = { "+", "-", "*", "/" };
		StringBuilder sb = new StringBuilder();
		int num1 = random.nextInt(range - 1) + 1;
		int num2 = random.nextInt(range - 1) + 1;
		String operator = op[random.nextInt(4)];
		if (operator.equals("-")) {
			if (num1 < num2) {
				int temp;
				temp = num1;
				num1 = num2;
				num2 = temp;
			}
		}
		if (operator.equals("/")) {
			if (num1 > num2) {
				num1 = num2 % num1;
			}
		}
		sb.append(num1);
		sb.append(operator);
		sb.append(num2);
		return new String(sb);

	}

	public static String second(String str, int range) {
		StringBuilder sb = new StringBuilder(str);
		Random random = new Random();
		String[] op = { "+", "-", "*", "/" };
		Ration r1 = new Ration(caclRPN(toRPN(str)));
		int num2 = random.nextInt(range - 1) + 1;
		Ration r2 = new Ration(num2, 1);
		String operator = op[random.nextInt(4)];
		if (operator.equals("-")) {
			if (Ration.isNagetive(r1.sub(r2))) {
				operator = "/";
			}
		}
		if (operator.equals("/")) {
			if (Ration.isDaiFenShu(r1.div(r2))) {
				operator = "-";
			}
		}
		sb.append(operator);
		sb.append(num2);
		return sb.toString();
	}

	public static void check(File exeFile, File ansFile) {
		File gradeFile = new File("Grade.txt");
		String exeLine = null;
		String ansLine = null;
		int rightCount = 0;
		int wrongCount = 0;
		ArrayList<String> rightList = new ArrayList<>();
		ArrayList<String> wrongList = new ArrayList<>();
		BufferedReader br_exe = null;
		BufferedReader br_ans = null;
		BufferedWriter bw_grade = null;
		try {
			br_exe = new BufferedReader(new InputStreamReader(new FileInputStream(exeFile)));
			br_ans = new BufferedReader(new InputStreamReader(new FileInputStream(ansFile)));
			bw_grade = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(gradeFile)));
			while (true) {
				exeLine = br_exe.readLine();
				// System.out.println(exeLine);
				ansLine = br_ans.readLine();
				// System.out.println(ansLine);
				if (exeLine == null || ansLine == null) {
					break;
				}
				String[] exeStr = exeLine.split(". ");
				String[] ansStr = ansLine.split(". ");
				// System.out.println(Ration.toDaiFenShu(QuizUtils.caclRPN(QuizUtils.toRPN(exeStr[1]))));
				if (ansStr[1].equals(Ration.toDaiFenShu(QuizUtils.caclRPN(QuizUtils.toRPN(exeStr[1]))))) {
					rightCount++;
					// System.out.println("right");
					rightList.add(exeStr[0]);
				} else {
					wrongCount++;
					// System.out.println("wrong");
					wrongList.add(exeStr[0]);
				}
			}
			String rightString = rightList.toString().replace('[', '(').replace(']', ')');
			String wrongString = wrongList.toString().replace('[', '(').replace(']', ')');
			bw_grade.write("Correct: " + rightCount + rightString);
			bw_grade.newLine();
			bw_grade.write("Wrong: " + wrongCount + wrongString);
			bw_grade.flush();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				br_ans.close();
				br_exe.close();
				bw_grade.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static String toRPN(String str) {
		Stack<Character> operators = new Stack<>();
		StringBuilder sb = new StringBuilder();
		char[] chars = str.toCharArray();
		int pre = 0;
		boolean digital;
		int len = chars.length;
		int bracket = 0;

		for (int i = 0; i < len;) {
			pre = i;
			digital = Boolean.FALSE;
			while (i < len && !Operator.isOperator(chars[i])) {
				i++;
				digital = Boolean.TRUE;
			}

			if (digital) {
				sb.append(str.substring(pre, i));
			} else {
				char o = chars[i++];
				if (o == '(') {
					bracket++;
				}
				if (bracket > 0) {
					if (o == ')') {
						while (!operators.empty()) {
							char top = operators.pop();
							if (top == '(') {
								break;
							}
							sb.append(top);
						}
						bracket--;
					} else {
						while (!operators.empty() && operators.peek() != '('
								&& Operator.cmp(o, operators.peek()) <= 0) {
							sb.append(operators.pop());
						}
						operators.push(o);
					}
				} else {
					while (!operators.empty() && Operator.cmp(o, operators.peek()) <= 0) {
						sb.append(operators.pop());
					}
					operators.push(o);
				}
			}

		}
		while (!operators.empty()) {
			sb.append(operators.pop());
		}
		return new String(sb);
	}
}

enum Operator {
	ADD('+', 1), SUBTRACT('-', 1), MULTIPLY('*', 2), DIVIDE('/', 2), LEFT_BRACKET('(', 3), RIGHT_BRACKET(')', 3);
	char value;
	int priority;

	Operator(char value, int priority) {
		this.value = value;
		this.priority = priority;
	}

	public static int cmp(char c1, char c2) {
		int p1 = 0;
		int p2 = 0;
		for (Operator o : Operator.values()) {
			if (o.value == c1) {
				p1 = o.priority;
			}
			if (o.value == c2) {
				p2 = o.priority;
			}
		}
		return p1 - p2;
	}

	public static boolean isOperator(char c) {
		for (Operator o : Operator.values()) {
			if (o.value == c) {
				return true;
			}
		}
		return false;
	}

}
