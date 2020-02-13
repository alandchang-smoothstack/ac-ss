package com.ss.lms.controller;

import com.ss.lms.util.ScannerUtil;

public class AdministratorController implements IController {
	private IController[] controllers;
	boolean isRunning;

	public AdministratorController() {
		controllers = new IController[] { new AuthorCrudController(), new PublisherCrudController(),
				new GenreCrudController(), new BookCrudController(), new LibraryBranchCrudController(),
				new BorrowerCrudController() };
	}

	public void init() {
		isRunning = true;
	}

	public void run() {
		while (isRunning) {
			System.out.println("Choose an entity to operate on:");
			System.out.println("\t1) Author");
			System.out.println("\t2) Publisher");
			System.out.println("\t3) Genre");
			System.out.println("\t4) Book");
			System.out.println("\t5) Library Branch");
			System.out.println("\t6) Borrower");
			System.out.println("\t7) Return to previous");
			boolean isSelected = false;
			while (!isSelected) {
				int selection = ScannerUtil.loopForInt() - 1;
				if (selection >= 0 && selection < controllers.length) {
					controllers[selection].init();
					controllers[selection].run();
					isSelected = true;
				} else if (selection == controllers.length) {
					isRunning = false;
					isSelected = true;
				} else {
					System.out.println("Invalid selection. Please try again.");
				}
			}
		}
	}
}
