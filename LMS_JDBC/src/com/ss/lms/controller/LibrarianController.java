package com.ss.lms.controller;

import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.ss.lms.entity.Book;
import com.ss.lms.entity.BookCopy;
import com.ss.lms.entity.LibraryBranch;
import com.ss.lms.service.LibrarianService;
import com.ss.lms.util.ScannerUtil;

public class LibrarianController implements IController {
	private enum State {
		LIB1, LIB2, LIB3
	}

	private Stack<State> states;
	private int libraryBranchId;
	private int bookId;

	public LibrarianController() {
		states = new Stack<State>();
	}

	public void init() {
		libraryBranchId = 0;
		bookId = 0;
		states.push(State.LIB1);
	}

	public void run() {
		try {
			while (!states.isEmpty()) {
				switch (states.peek()) {
				case LIB1:
					lib1();
					break;
				case LIB2:
					lib2();
					break;
				case LIB3:
					lib3();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void lib1() throws Exception {
		System.out.println("\t1) Enter library branch you manage");
		System.out.println("\t2) Quit to previous");
		boolean isSelected = false;
		while (!isSelected) {
			int selection = ScannerUtil.loopForInt();
			switch (selection) {
			case 1:
				states.push(State.LIB2);
				isSelected = true;
				break;
			case 2:
				states.pop();
				isSelected = true;
				break;
			default:
				System.out.println("Invalid selection. Please try again");
			}
		}
	}

	public void lib2() throws Exception {
		System.out.println("Please select a library branch:");
		List<LibraryBranch> libraryBranches = LibrarianService.getInstance().readLibraryBranches();
		IntStream.range(0, libraryBranches.size())
				.forEach(i -> System.out.println(
						new StringBuilder("\t").append(i + 1).append(") ").append(libraryBranches.get(i).getName())
								.append(", ").append(libraryBranches.get(i).getAddress())));
		System.out.println(new StringBuilder("\t").append(libraryBranches.size() + 1).append(") Quit to previous"));
		boolean isSelected = false;
		while (!isSelected) {
			int selection = ScannerUtil.loopForInt();
			if (selection >= 1 && selection <= libraryBranches.size()) {
				libraryBranchId = libraryBranches.get(selection - 1).getId();
				states.push(State.LIB3);
				isSelected = true;
			} else if (selection == libraryBranches.size() + 1) {
				states.pop();
				isSelected = true;
			} else {
				System.out.println("Invalid selection. Please try again.");
			}
		}
	}

	public void lib3() throws Exception {
		LibraryBranch libraryBranch = LibrarianService.getInstance().readLibraryBranch(libraryBranchId);
		System.out.println(new StringBuilder(libraryBranch.getName()).append(", ").append(libraryBranch.getAddress()));
		System.out.println("\t1) Update details of library branch");
		System.out.println("\t2) Add copies of book in library branch");
		System.out.println("\t3) Modify copies of book in library branch");
		System.out.println("\t4) Quit to previous");
		boolean isSelected = false;
		while (!isSelected) {
			int selection = ScannerUtil.loopForInt();
			switch (selection) {
			case 1: {
				LibraryBranch newLibraryBranch = new LibraryBranch();
				newLibraryBranch.setId(libraryBranchId);
				System.out.println(new StringBuilder("You have chosen to update library branch with name: ")
						.append(libraryBranch.getName()).append(" and address: ").append(libraryBranch.getAddress())
						.append("."));
				System.out.println("Enter 'quit' at any prompt to cancel operation.");
				System.out.println("Enter new library branch name or enter N/A for no change:");
				newLibraryBranch.setName(libraryBranch.getName());
				boolean isValid = false;
				while (!isValid) {
					String input = ScannerUtil.loopForString();
					if (input.equalsIgnoreCase("n/a")) {
						isValid = true;
					} else if (input.equalsIgnoreCase("quit")) {
						return;
					} else {
						newLibraryBranch.setName(input);
						isValid = true;
					}
				}
				System.out.println("Enter new library branch address or enter N/A for no change:");
				newLibraryBranch.setAddress(libraryBranch.getAddress());
				isValid = false;
				while (!isValid) {
					String input = ScannerUtil.loopForString();
					if (input.equalsIgnoreCase("n/a")) {
						isValid = true;
					} else if (input.equalsIgnoreCase("quit")) {
						return;
					} else {
						newLibraryBranch.setAddress(input);
						isValid = true;
					}
				}
				LibrarianService.getInstance().updateLibraryBranch(newLibraryBranch);
				isSelected = true;
				break;
			}
			case 2: {
				System.out.println("Choose book to add copies of within library branch:");
				List<Book> books = LibrarianService.getInstance().readBooks();
				IntStream.range(0, books.size())
						.forEach(i -> System.out.println(new StringBuilder("\t").append(i + 1).append(") ")
								.append(books.get(i).getTitle()).append(", authors: [")
								.append(books.get(i).getAuthors().stream().map(author -> author.getName())
										.collect(Collectors.joining(", ")))
								.append("], publisher: ").append(books.get(i).getPublisher().getName())));
				System.out.println(new StringBuilder("\t").append(books.size() + 1).append(") Quit to previous"));
				isSelected = false;
				while (!isSelected) {
					selection = ScannerUtil.loopForInt();
					if (selection >= 1 && selection <= books.size()) {
						bookId = books.get(selection - 1).getId();
						BookCopy bookCopy = LibrarianService.getInstance().readBookCopy(bookId, libraryBranchId);
						System.out.println(new StringBuilder("Existing number of copies: ")
								.append(bookCopy == null ? 0 : bookCopy.getAmount()));
						System.out.println("Enter additional number of copies:");
						boolean isValid = false;
						while (!isValid) {
							selection = ScannerUtil.loopForInt();
							if (selection >= 0) {
								isValid = true;
							} else {
								System.out.println("Number of copies should not be less than 0. Please try again.");
							}
						}
						BookCopy newBookCopy = new BookCopy();
						Book newBook = new Book();
						newBook.setId(bookId);
						LibraryBranch newLibraryBranch = new LibraryBranch();
						newLibraryBranch.setId(libraryBranchId);
						newBookCopy.setBook(newBook);
						newBookCopy.setLibraryBranch(newLibraryBranch);
						if (bookCopy == null) {
							newBookCopy.setAmount(selection);
							LibrarianService.getInstance().createBookCopy(newBookCopy);
						} else {
							newBookCopy.setAmount(bookCopy.getAmount() + selection);
							LibrarianService.getInstance().updateBookCopy(newBookCopy);
						}
						isSelected = true;
					} else if (selection == books.size() + 1) {
						return;
					} else {
						System.out.println("Invalid selection. Please try again.");
					}
				}
				break;
			}
			case 3: {
				System.out.println("Choose book to modify copies of within library branch:");
				List<Book> books = LibrarianService.getInstance().readBooks();
				IntStream.range(0, books.size())
						.forEach(i -> System.out.println(new StringBuilder("\t").append(i + 1).append(") ")
								.append(books.get(i).getTitle()).append(", authors: [")
								.append(books.get(i).getAuthors().stream().map(author -> author.getName())
										.collect(Collectors.joining(", ")))
								.append("], publisher: ").append(books.get(i).getPublisher().getName())));
				System.out.println(new StringBuilder("\t").append(books.size() + 1).append(") Quit to previous"));
				isSelected = false;
				while (!isSelected) {
					selection = ScannerUtil.loopForInt();
					if (selection >= 1 && selection <= books.size()) {
						bookId = books.get(selection - 1).getId();
						BookCopy bookCopy = LibrarianService.getInstance().readBookCopy(bookId, libraryBranchId);
						System.out.println(new StringBuilder("Existing number of copies: ")
								.append(bookCopy != null ? bookCopy.getAmount() : 0));
						System.out.println("Enter new number of copies:");
						boolean isValid = false;
						while (!isValid) {
							selection = ScannerUtil.loopForInt();
							if (selection >= 0) {
								isValid = true;
							} else {
								System.out.println("Number of copies should not be less than 0. Please try again.");
							}
						}
						BookCopy newBookCopy = new BookCopy();
						Book newBook = new Book();
						newBook.setId(bookId);
						LibraryBranch newLibraryBranch = new LibraryBranch();
						newLibraryBranch.setId(libraryBranchId);
						newBookCopy.setBook(newBook);
						newBookCopy.setLibraryBranch(newLibraryBranch);
						newBookCopy.setAmount(selection);
						if (bookCopy != null) {
							LibrarianService.getInstance().updateBookCopy(newBookCopy);
						} else {
							LibrarianService.getInstance().createBookCopy(newBookCopy);
						}
						isSelected = true;
					} else if (selection == books.size() + 1) {
						return;
					} else {
						System.out.println("Invalid selection. Please try again.");
					}
				}
				break;
			}
			case 4:
				states.pop();
				isSelected = true;
				break;
			default:
				System.out.println("Invalid selection. Please try again.");
			}
		}
	}
}
