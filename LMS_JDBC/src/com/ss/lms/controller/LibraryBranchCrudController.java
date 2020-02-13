package com.ss.lms.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.IntStream;

import com.ss.lms.entity.LibraryBranch;
import com.ss.lms.service.AdministratorService;
import com.ss.lms.util.ScannerUtil;

public class LibraryBranchCrudController implements IController {
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
		System.out.println("\t1) Create library branch");
		System.out.println("\t2) Read library branch list");
		System.out.println("\t3) Update library branch");
		System.out.println("\t4) Delete library branch");
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
		LibraryBranch libraryBranch = new LibraryBranch();
		System.out.println("Enter library branch name:");
		libraryBranch.setName(ScannerUtil.loopForString());
		System.out.println("Enter library branch address:");
		libraryBranch.setAddress(ScannerUtil.loopForString());
		System.out.println(new StringBuilder("Are you sure you want to create library branch with name: ")
				.append(libraryBranch.getName()).append(", address: ").append(libraryBranch.getAddress())
				.append("? (y/n)"));
		boolean isSelected = false;
		while (!isSelected) {
			String confirmation = ScannerUtil.loopForString().toLowerCase();
			switch (confirmation) {
			case "y":
				AdministratorService.getInstance().createLibraryBranch(libraryBranch);
				System.out.println("Library branch has been created.");
				this.state = State.MENU;
				isSelected = true;
				break;
			case "n":
				System.out.println("Library branch has not been created.");
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
		List<LibraryBranch> libraryBranchList = AdministratorService.getInstance().readLibraryBranches();
		if (libraryBranchList.isEmpty()) {
			System.out.println("Library branch list is empty.");
		} else {
			libraryBranchList.stream().forEach(
					i -> System.out.println(new StringBuilder(i.getName()).append(", ").append(i.getAddress())));
		}
		state = State.MENU;
	}

	public void update() throws SQLException {
		LibraryBranch libraryBranch = new LibraryBranch();
		System.out.println("Choose library branch:");
		List<LibraryBranch> libraryBranchList = AdministratorService.getInstance().readLibraryBranches();
		IntStream.range(0, libraryBranchList.size())
				.forEach(i -> System.out.println(
						new StringBuilder("\t").append(i + 1).append(") ").append(libraryBranchList.get(i).getName())
								.append(", ").append(libraryBranchList.get(i).getAddress())));
		System.out.println(
				new StringBuilder("\t").append(libraryBranchList.size() + 1).append(") Return to previous menu"));
		boolean isValid = false;
		while (!isValid) {
			int selection = ScannerUtil.loopForInt();
			if (selection < 1 || selection > libraryBranchList.size() + 1) {
				System.out.println("Invalid selection. Please try again");
			} else if (selection == libraryBranchList.size() + 1) {
				this.state = State.MENU;
				return;
			} else {
				libraryBranch.setId(libraryBranchList.get(selection - 1).getId());
				isValid = true;
			}
		}
		System.out.println("Enter new library branch name:");
		libraryBranch.setName(ScannerUtil.loopForString());
		System.out.println("Enter new library branch address:");
		libraryBranch.setAddress(ScannerUtil.loopForString());
		AdministratorService.getInstance().updateLibraryBranch(libraryBranch);
		System.out.println("Library branch has been updated.");
		this.state = State.MENU;
	}

	public void delete() throws SQLException {
		int id = 0;
		System.out.println("Choose library branch:");
		List<LibraryBranch> libraryBranchList = AdministratorService.getInstance().readLibraryBranches();
		IntStream.range(0, libraryBranchList.size())
				.forEach(i -> System.out.println(
						new StringBuilder("\t").append(i + 1).append(") ").append(libraryBranchList.get(i).getName())
								.append(", ").append(libraryBranchList.get(i).getAddress())));
		System.out.println(
				new StringBuilder("\t").append(libraryBranchList.size() + 1).append(") Return to previous menu"));
		boolean isValid = false;
		while (!isValid) {
			int selection = ScannerUtil.loopForInt();
			if (selection < 1 || selection > libraryBranchList.size() + 1) {
				System.out.println("Invalid selection. Please try again");
			} else if (selection == libraryBranchList.size() + 1) {
				this.state = State.MENU;
				return;
			} else {
				id = libraryBranchList.get(selection - 1).getId();
				isValid = true;
			}
		}
		AdministratorService.getInstance().deleteLibraryBranch(id);
		System.out.println("Library branch has been deleted.");
		this.state = State.MENU;
	}
}
