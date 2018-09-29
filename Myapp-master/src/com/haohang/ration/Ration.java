package com.haohang.ration;

import java.util.Scanner;

public class Ration {

	public int numerator;
	public int denominator;

	public Ration(int numerator, int denominator) {
		yuefen(numerator, denominator);
	}

	public Ration(String str) {
		if (str.contains("/")) {
			String[] nums = str.split("/");
			int num1 = Integer.parseInt(nums[0]);
			int num2 = Integer.parseInt(nums[1]);
			yuefen(num1, num2);
		} else {
			this.numerator = Integer.parseInt(str);
			this.denominator = 1;
		}
	}

	public Ration mul(Ration mulR) {
		return new Ration((this.numerator * mulR.numerator), (this.denominator * mulR.denominator));
	}

	public Ration div(Ration mulR) {
		return new Ration((this.numerator * mulR.denominator), (this.denominator * mulR.numerator));
	}

	public Ration add(Ration mulR) {
		return new Ration((this.numerator * mulR.denominator + mulR.numerator * this.denominator),
				(this.denominator * mulR.denominator));
	}

	public Ration sub(Ration mulR) {
		return new Ration((this.numerator * mulR.denominator - mulR.numerator * this.denominator),
				(this.denominator * mulR.denominator));
	}

	@Override
	public String toString() {
		if (this.denominator == 1) {
			return String.valueOf(this.numerator);
		} else {
			return new String(this.numerator + "/" + this.denominator);
		}
	}

	public static String toDaiFenShu(String str) {
		Ration r = new Ration(str);
		if (r.denominator == 1) {
			return String.valueOf(r.numerator);
		} else {
			if (r.numerator > r.denominator) {
				int dai = r.numerator / r.denominator;
				return new String(dai + "'" + (r.numerator - r.denominator * dai) + "/" + r.denominator);
			} else {
				return new String(r.numerator + "/" + r.denominator);
			}
		}
	}

	public static boolean isDaiFenShu(Ration r) {
		if (r.numerator > r.denominator) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isNagetive(Ration r) {
		if (r.toString().contains("-")) {
			return true;
		} else {
			return false;
		}
	}

	private void yuefen(int a, int b) {
		int oa = a;
		int ob = b;
		while (b != 0) {
			int r = a % b;
			a = b;
			b = r;
		}
		this.numerator = oa / a;
		this.denominator = ob / a;
	}

}
