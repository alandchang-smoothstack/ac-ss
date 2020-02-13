package com.ss.lms.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.IntStream;

import com.ss.lms.entity.Borrower;
import com.ss.lms.service.AdministratorService;
import com.ss.lms.util.ScannerUtil;

public class BorrowerCrudController implements IController {
	private enum State {
		MENU, CREATE, READ, UPDATE, DELETE, EXIT
	}

	private State state;
	private boolean isRunning;

	public void init() {
		state = State.MENU;
		isRunning = true;
	}

	public void run() {
		try {
			while (isRunning) {
				switch (state) {
				case MENU:
					menu();
					break;
				case CREATE:
					create();
					break;
				case READ:
					read();
					break;
				case UPDATE:
					update();
					break;
				case DELETE:
					delete();
					break;
				case EXIT:
					isRunning = false;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void menu() {
		System.out.println("Choose an operation: ");
		System.out.println("\t1) Create borrower");
		System.out.println("\t2) Read borrower list");
		System.out.println("\t3) Update borrower");
		System.out.println("\t4) Delete borrower");
		System.out.println("\t5) Return to previous menu");
		boolean isSelected = false;
		while (!isSelected) {
			int selection = ScannerUtil.loopForInt();
			switch (selection) {
			case 1:
				state = State.CREATE;
				isSelected = true;
				break;
			case 2:
				state = State.READ;
				isSelected = true;
				break;
			case 3:
				state = State.UPDATE;
				isSelected = true;
				break;
			case 4:
				state = State.DELETE;
				isSelected = true;
				break;
			case 5:
				state = State.EXIT;
				isSelected = true;
				break;
			default:
				System.out.println("Invalid selection. Please try again.");
				break;
			}
		}
	}

	public void create() throws SQLException {
		Borrower borrower = new Borrower();
		System.out.println("Enter borrower name:");
		borrower.setName(ScannerUtil.loopForString());
		System.out.println("Enter borrower address:");
		borrower.setAddress(ScannerUtil.loopForString());
		System.out.println("Enter borrower phone:");
		borrower.setPhone(ScannerUtil.loopForString());
		System.out.println(new StringBuilder("Are you sure you want to create borrower with name: ")
				.append(borrower.getName()).append(", address: ").append(borrower.getAddress()).append(", phone: ")
				.append(borrower.getPhone()).append("? (y/n)"));
		boolean isSelected = false;
		while (!isSelected) {
			String confirmation = ScannerUtil.loopForString().toLowerCase();
			switch (confirmation) {
			case "y":
				AdministratorService.getInstance().createBorrower(borrower);
				System.out.println("Borrower has been created.");
				this.state = State.MENU;
				isSelected = true;
				break;
			case "n":
				System.out.println("Borrower has not been created.");
				this.state = State.MENU;
				isSelected = true;
				break;
			default:
				System.out.println("Invalid selection. Please try again.");
				break;
			}
		}
	}

	public void read() throws SQLException {
		List<Borrower> borrowerList = AdministratorService.getInstance().readBorrowers();
		if (borrowerList.isEmpty()) {
			System.out.println("Borrower list is empty.");
		} else {
			borrowerList.stream().forEach(i -> System.out.println(new StringBuilder(i.getName()).append(", ")
					.append(i.getAddress()).append(", ").append(i.getPhone())));
		}
		state = State.MENU;
	}

	public void update() throws SQLException {
		Borrower borrower = new Borrower();
		System.out.println("Choose borrower:");
		List<Borrower> borrowerList = AdministratorService.getInstance().readBorrowers();
		IntStream.range(0, borrowerList.size())
				.forEach(i -> System.out.println(new StringBuilder("\t").append(i + 1).append(") ")
						.append(borrowerList.get(i).getName()).append(", ").append(borrowerList.get(i).getAddress())
						.append(", ").append(borrowerList.get(i).getPhone())));
		System.out.println(new StringBuilder("\t").append(borrowerList.size() + 1).append(") Return to previous menu"));
		boolean isValid = false;
		while (!isValid) {
			int selection = ScannerUtil.loopForInt();
			if (selection < 1 || selection > borrowerList.size() + 1) {
				System.out.println("Invalid selection. Please try again");
			} else if (selection == borrowerList.size() + 1) {
				this.state = State.MENU;
				return;
			} else {
				borrower.setCardNumber(borrowerList.get(selection - 1).getCardNumber());
				isValid = true;
			}
		}
		System.out.println("Enter new borrower name:");
		borrower.setName(ScannerUtil.loopForString());
		System.out.println("Enter new borrower address:");
		borrower.setAddress(ScannerUtil.loopForString());
		System.out.println("Enter new borrower phone:");
		borrower.setPhone(ScannerUtil.loopForString());
		AdministratorService.getInstance().updateBorrower(borrower);
		System.out.println("Borrower has been updated.");
		this.state = State.MENU;
	}

	public void delete() throws SQLException {
		int cardNumber = 0;
		System.out.println("Choose borrower:");
		List<Borrower> borrowerList = AdministratorService.getInstance().readBorrowers();
		IntStream.range(0, borrowerList.size())
				.forEach(i -> System.out.println(new StringBuilder("\t").append(i + 1).append(") ")
						.append(borrowerList.get(i).getName()).append(", ").append(borrowerList.get(i).getAddress())
						.append(", ").append(borrowerList.get(i).getPhone())));
		System.out.println(new StringBuilder("\t").append(borrowerList.size() + 1).append(") Return to previous menu"));
		boolean isValid = false;
		while (!isValid) {
			int selection = ScannerUtil.loopForInt();
			if (selection < 1 || selection > borrowerList.size() + 1) {
				System.out.println("Invalid selection. Please try again");
			} else if (selection == borrowerList.size() + 1) {
				this.state = State.MENU;
				return;
			} else {
				cardNumber = borrowerList.get(selection - 1).getCardNumber();
				isValid = true;
			}
		}
		AdministratorService.getInstance().deleteBorrower(cardNumber);
		System.out.println("Borrower has been deleted.");
		this.state = State.MENU;
	}
}
