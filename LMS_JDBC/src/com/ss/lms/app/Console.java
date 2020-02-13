package com.ss.lms.app;

import com.ss.lms.controller.AdministratorController;
import com.ss.lms.controller.BorrowerController;
import com.ss.lms.controller.IController;
import com.ss.lms.controller.LibrarianController;
import com.ss.lms.util.ScannerUtil;

public class Console {
	private IController[] controllers;

	public Console() {
		controllers = new IController[] { new LibrarianController(), new AdministratorController(),
				new BorrowerController() };
	}

	public void run() {
		while (true) {
			System.out.println("Welcome to the SmoothStack Library Management System. Please select a user category:");
			System.out.println("\t1) Librarian");
			System.out.println("\t2) Administrator");
			System.out.println("\t3) Borrower");
			boolean isSelected = false;
			while (!isSelected) {
				int index = ScannerUtil.loopForInt() - 1;
				if (index < 0 || index >= controllers.length) {
					System.out.println("Invalid selection. Please try again.");
					continue;
				}
				controllers[index].init();
				controllers[index].run();
				isSelected = true;
			}
		}
	}
}
