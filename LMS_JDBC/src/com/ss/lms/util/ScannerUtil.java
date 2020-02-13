package com.ss.lms.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class ScannerUtil {
	private static Scanner scanner = new Scanner(System.in);

	public static Integer loopForInt() {
		Integer output = null;
		boolean isValid = false;
		while (!isValid) {
			try {
				output = Integer.parseInt(scanner.nextLine());
				isValid = true;
			} catch (Exception e) {
				System.out.println("Invalid input. Please enter a number.");
			}
		}
		return output;
	}

	public static List<Integer> loopForInts() {
		List<Integer> output = new ArrayList<Integer>();
		boolean isValid = false;
		while (!isValid) {
			try {
				String[] s = scanner.nextLine().trim().split(" ");
				for (int i = 0; i < s.length; i++) {
					Integer j = Integer.parseInt(s[i]);
					if (!output.contains(j)) {
						output.add(j);
					}
				}
				Collections.sort(output);
				isValid = true;
			} catch (Exception e) {
				System.out.println("Invalid input. Please enter valid numbers separated by single spaces.");
			}
		}
		return output;
	}

	public static String loopForString() {
		String output = null;
		boolean isValid = false;
		while (!isValid) {
			output = scanner.nextLine().trim();
			if (!output.isEmpty()) {
				isValid = true;
			} else {
				System.out.println("Input cannot be empty. Please try again.");
			}
		}
		return output;
	}
}
