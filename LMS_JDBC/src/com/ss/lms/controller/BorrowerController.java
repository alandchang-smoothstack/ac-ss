package com.ss.lms.controller;

import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.ss.lms.entity.Book;
import com.ss.lms.entity.LibraryBranch;
import com.ss.lms.service.BorrowerService;
import com.ss.lms.util.ScannerUtil;

public class BorrowerController implements IController {
	private enum State {
		BORR0, BORR1
	}

	private Stack<State> states;
	private int cardNumber;

	public BorrowerController() {
		states = new Stack<State>();
	}

	public void init() {
		cardNumber = 0;
		states.push(State.BORR0);
	}

	public void run() {
		try {
			while (!states.isEmpty()) {
				switch (states.peek()) {
				case BORR0:
					borr0();
					break;
				case BORR1:
					borr1();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void borr0() throws Exception {
		System.out.println("Enter your card number:");
		boolean isValid = false;
		while (!isValid) {
			cardNumber = ScannerUtil.loopForInt();
			if (BorrowerService.getInstance().borrowerExists(cardNumber)) {
				isValid = true;
			} else {
				System.out.println("Card number is invalid. Please try again.");
			}
		}
		states.push(State.BORR1);
	}

	public void borr1() throws Exception {
		System.out.println("\t1) Check out book");
		System.out.println("\t2) Return book");
		System.out.println("\t3) Quit to previous");
		boolean isSelected = false;
		while (!isSelected) {
			int selection = ScannerUtil.loopForInt();
			switch (selection) {
			case 1: {
				System.out.println("Choose library branch to check out book from:");
				List<LibraryBranch> libraryBranches = BorrowerService.getInstance().readLibraryBranches();
				Integer libraryBranchId = null;
				IntStream.range(0, libraryBranches.size())
						.forEach(i -> System.out.println(new StringBuilder("\t").append(i + 1).append(") ")
								.append(libraryBranches.get(i).getName()).append(", ")
								.append(libraryBranches.get(i).getAddress())));
				System.out.println(
						new StringBuilder("\t").append(libraryBranches.size() + 1).append(") Quit to previous"));
				isSelected = false;
				while (!isSelected) {
					selection = ScannerUtil.loopForInt();
					if (selection >= 1 && selection <= libraryBranches.size()) {
						libraryBranchId = libraryBranches.get(selection - 1).getId();
						isSelected = true;
					} else if (selection == libraryBranches.size() + 1) {
						return;
					} else {
						System.out.println("Invalid selection. Please try again.");
					}
				}
				System.out.println("Choose book to check out:");
				List<Book> availableBooks = BorrowerService.getInstance().readAvailableBooks(cardNumber,
						libraryBranchId);
				Integer bookId = null;
				IntStream.range(0, availableBooks.size())
						.forEach(i -> System.out.println(new StringBuilder("\t").append(i + 1).append(") ")
								.append(availableBooks.get(i).getTitle()).append(", authors: [")
								.append(availableBooks.get(i).getAuthors().stream().map(author -> author.getName())
										.collect(Collectors.joining(", ")))
								.append("], publisher: ").append(availableBooks.get(i).getPublisher().getName())));
				System.out.println(
						new StringBuilder("\t").append(availableBooks.size() + 1).append(") Quit to previous"));
				isSelected = false;
				while (!isSelected) {
					selection = ScannerUtil.loopForInt();
					if (selection >= 1 && selection <= availableBooks.size()) {
						bookId = availableBooks.get(selection - 1).getId();
						BorrowerService.getInstance().checkoutBook(bookId, libraryBranchId, cardNumber);
						isSelected = true;
					} else if (selection == availableBooks.size() + 1) {
						return;
					} else {
						System.out.println("Invalid selection. Please try again.");
					}
				}
				break;
			}
			case 2: {
				System.out.println("Choose library branch to return book to:");
				List<LibraryBranch> libraryBranches = BorrowerService.getInstance().readLibraryBranches();
				Integer libraryBranchId = null;
				IntStream.range(0, libraryBranches.size())
						.forEach(i -> System.out.println(new StringBuilder("\t").append(i + 1).append(") ")
								.append(libraryBranches.get(i).getName()).append(", ")
								.append(libraryBranches.get(i).getAddress())));
				System.out.println(
						new StringBuilder("\t").append(libraryBranches.size() + 1).append(") Quit to previous"));
				isSelected = false;
				while (!isSelected) {
					selection = ScannerUtil.loopForInt();
					if (selection >= 1 && selection <= libraryBranches.size()) {
						libraryBranchId = libraryBranches.get(selection - 1).getId();
						isSelected = true;
					} else if (selection == libraryBranches.size() + 1) {
						return;
					} else {
						System.out.println("Invalid selection. Please try again.");
					}
				}
				System.out.println("Choose book to return:");
				List<Book> borrowedBooks = BorrowerService.getInstance().readBorrowedBooks(cardNumber, libraryBranchId);
				Integer bookId = null;
				IntStream.range(0, borrowedBooks.size())
						.forEach(i -> System.out.println(new StringBuilder("\t").append(i + 1).append(") ")
								.append(borrowedBooks.get(i).getTitle()).append(", authors: [")
								.append(borrowedBooks.get(i).getAuthors().stream().map(author -> author.getName())
										.collect(Collectors.joining(", ")))
								.append("], publisher: ").append(borrowedBooks.get(i).getPublisher().getName())));
				System.out
						.println(new StringBuilder("\t").append(borrowedBooks.size() + 1).append(") Quit to previous"));
				isSelected = false;
				while (!isSelected) {
					selection = ScannerUtil.loopForInt();
					if (selection >= 1 && selection <= borrowedBooks.size()) {
						bookId = borrowedBooks.get(selection - 1).getId();
						BorrowerService.getInstance().checkinBook(bookId, libraryBranchId, cardNumber);
						isSelected = true;
					} else if (selection == borrowedBooks.size() + 1) {
						return;
					} else {
						System.out.println("Invalid selection. Please try again.");
					}
				}
				break;
			}
			case 3:
				states.pop();
				states.pop();
				isSelected = true;
				break;
			}
		}
	}
}
