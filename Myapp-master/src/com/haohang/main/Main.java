package com.haohang.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashSet;

import com.haohang.GUI.MyAppGUI;
import com.haohang.ration.Ration;
import com.haohang.utils.QuizUtils;

public class Main {

	public static void main(String[] args) {
		int range = -1, num = -1;
		boolean r_exist = false;
		for (int i = 0; i < args.length; i += 2) {
			switch (args[i]) {
			case "-r":
				if (Integer.parseInt(args[i + 1]) > 0) {
					r_exist = true;
					range = Integer.parseInt(args[i + 1]);
				}
				if (range > 10) {
					System.out.println("-r can't greater than 10");
					System.exit(0);
				}

				break;
			case "-n":
				num = Integer.parseInt(args[i + 1]);
				break;
			case "-e":
				String exerciseFileName = args[i + 1];
				String answerFileName = args[i + 3];
				File exerciseFile = new File(exerciseFileName);
				File answerFile = new File(answerFileName);
				QuizUtils.check(exerciseFile, answerFile);

			default:
				break;
			}
		}
		if (!r_exist) {
			System.exit(0);
		} else {
			HashSet<String> set = QuizUtils.generateQuiz(range, num);
			File exeFile = new File("./Exercises.txt");
			File ansFile = new File("./Answer.txt");
			BufferedWriter bw_exe = null;
			BufferedWriter bw_ans = null;
			try {
				bw_exe = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(exeFile)));
				bw_ans = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ansFile)));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String excercise_line = null;
			String answer_line = null;
			int count = 1;
			for (String string : set) {
				// System.out.println(string);
				excercise_line = new String(count + ". " + string);
				answer_line = new String(count + ". " + Ration.toDaiFenShu(QuizUtils.caclRPN(QuizUtils.toRPN(string))));
				count++;
				try {
					bw_exe.write(excercise_line);
					bw_ans.write(answer_line);
					bw_exe.newLine();
					bw_ans.newLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// String str = QuizUtils.caclRPN(QuizUtils.toRPN(string));
				// System.out.println(Ration.toDaiFenShu(str));
			}
			try {
				bw_exe.close();
				bw_ans.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
