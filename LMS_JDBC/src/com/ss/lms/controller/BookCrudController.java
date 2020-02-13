package com.ss.lms.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.ss.lms.entity.Author;
import com.ss.lms.entity.Book;
import com.ss.lms.entity.Genre;
import com.ss.lms.entity.Publisher;
import com.ss.lms.service.AdministratorService;
import com.ss.lms.util.ScannerUtil;

public class BookCrudController implements IController {
	private enum State {
		MENU, CREATE, READ_LIST, UPDATE, DELETE, EXIT
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
				case READ_LIST:
					read_list();
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
		System.out.println("\t1) Create book");
		System.out.println("\t2) Read book list");
		System.out.println("\t3) Update book");
		System.out.println("\t4) Delete book");
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
				state = State.READ_LIST;
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
		Book book = new Book();
		System.out.println("Enter book title:");
		book.setTitle(ScannerUtil.loopForString());
		System.out.println("Choose authors:");
		List<Author> authorList = AdministratorService.getInstance().readAuthors();
		IntStream.range(0, authorList.size()).forEach(i -> System.out
				.println(new StringBuilder("\t").append(i + 1).append(") ").append(authorList.get(i).getName())));
		boolean isValid = false;
		while (!isValid) {
			List<Integer> selections = ScannerUtil.loopForInts();
			if (selections.isEmpty()) {
				System.out.println("Invalid selections. Please try again.");
			} else {
				boolean areValid = true;
				for (int selection : selections) {
					if (selection < 1 || selection > authorList.size()) {
						areValid = false;
						System.out.println("Invalid selections. Please try again.");
						break;
					}
				}
				if (areValid) {
					book.setAuthors(selections.stream().map(selection -> authorList.get(selection - 1))
							.collect(Collectors.toList()));
					isValid = true;
				}
			}
		}
		System.out.println("Choose publisher:");
		List<Publisher> publisherList = AdministratorService.getInstance().readPublishers();
		IntStream.range(0, publisherList.size())
				.forEach(i -> System.out.println(
						new StringBuilder("\t").append(i + 1).append(") ").append(publisherList.get(i).getName())
								.append(", ").append(publisherList.get(i).getAddress())));
		isValid = false;
		while (!isValid) {
			int selection = ScannerUtil.loopForInt();
			if (selection < 1 || selection > publisherList.size() + 1) {
				System.out.println("Invalid selection. Please try again.");
			} else {
				book.setPublisher(publisherList.get(selection - 1));
				isValid = true;
			}
		}
		System.out.println("Choose genres:");
		List<Genre> genreList = AdministratorService.getInstance().readGenres();
		IntStream.range(0, genreList.size()).forEach(i -> System.out
				.println(new StringBuilder("\t").append(i + 1).append(") ").append(genreList.get(i).getName())));
		isValid = false;
		while (!isValid) {
			List<Integer> selections = ScannerUtil.loopForInts();
			if (selections.isEmpty()) {
				System.out.println("Invalid selections. Please try again.");
			} else {
				boolean areValid = true;
				for (int selection : selections) {
					if (selection < 1 || selection > authorList.size()) {
						areValid = false;
						System.out.println("Invalid selections. Please try again.");
						break;
					}
				}
				if (areValid) {
					book.setGenres(selections.stream().map(selection -> genreList.get(selection - 1))
							.collect(Collectors.toList()));
					isValid = true;
				}
			}
		}
		System.out.println(new StringBuilder("Title: ").append(book.getTitle()));
		System.out.println(new StringBuilder("Authors: ")
				.append(book.getAuthors().stream().map(author -> author.getName()).collect(Collectors.joining(", "))));
		System.out.println(new StringBuilder("Publisher: ").append(book.getPublisher().getName()));
		System.out.println(new StringBuilder("Genres: ")
				.append(book.getGenres().stream().map(genre -> genre.getName()).collect(Collectors.joining(", "))));
		System.out.println("Are you sure you want to create book? (y/n)");
		boolean isSelected = false;
		while (!isSelected) {
			String confirmation = ScannerUtil.loopForString().toLowerCase();
			switch (confirmation) {
			case "y":
				AdministratorService.getInstance().createBook(book);
				System.out.println("Book has been created.");
				state = State.MENU;
				isSelected = true;
				break;
			case "n":
				System.out.println("Book has not been created.");
				state = State.MENU;
				isSelected = true;
				break;
			default:
				System.out.println("Invalid selection. Please try again.");
				break;
			}
		}
	}

	public void read_list() throws SQLException {
		List<Book> bookList = AdministratorService.getInstance().readBooks();
		if (bookList.isEmpty()) {
			System.out.println("Book list is empty.");
		} else {
			IntStream
					.range(0,
							bookList.size())
					.forEach(
							i -> System.out
									.println(new StringBuilder(bookList.get(i).getTitle()).append(", [")
											.append(bookList.get(i).getAuthors().stream()
													.map(author -> author.getName()).collect(Collectors.joining(", ")))
											.append("], ").append(bookList.get(i).getPublisher().getName())
											.append(", [").append(bookList.get(i).getGenres().stream()
													.map(genre -> genre.getName()).collect(Collectors.joining(", ")))
											.append("]")));
		}
		state = State.MENU;
	}

	public void update() throws SQLException {
		Book book = new Book();
		System.out.println("Choose book:");
		List<Book> bookList = AdministratorService.getInstance().readBooks();
		IntStream.range(0, bookList.size()).forEach(i -> System.out.println(new StringBuilder("\t").append(i + 1)
				.append(") ").append(bookList.get(i).getTitle()).append(", [")
				.append(bookList.get(i).getAuthors().stream().map(author -> author.getName())
						.collect(Collectors.joining(", ")))
				.append("], ").append(bookList.get(i).getPublisher().getName()).append(", [").append(bookList.get(i)
						.getGenres().stream().map(genre -> genre.getName()).collect(Collectors.joining(", ")))
				.append("]")));
		System.out.println(new StringBuilder("\t").append(bookList.size() + 1).append(") Return to previous menu"));
		boolean isValid = false;
		while (!isValid) {
			int selection = ScannerUtil.loopForInt();
			if (selection < 1 || selection > bookList.size() + 1) {
				System.out.println("Invalid selection. Please try again.");
			} else if (selection == bookList.size() + 1) {
				state = State.MENU;
				return;
			} else {
				book.setId(bookList.get(selection - 1).getId());
				isValid = true;
			}
		}
		System.out.println("Enter new book title:");
		book.setTitle(ScannerUtil.loopForString());
		System.out.println("Choose authors:");
		List<Author> authorList = AdministratorService.getInstance().readAuthors();
		IntStream.range(0, authorList.size()).forEach(i -> System.out
				.println(new StringBuilder("\t").append(i + 1).append(") ").append(authorList.get(i).getName())));
		isValid = false;
		while (!isValid) {
			List<Integer> selections = ScannerUtil.loopForInts();
			if (selections.isEmpty()) {
				System.out.println("Invalid selections. Please try again.");
			} else {
				boolean areValid = true;
				for (int selection : selections) {
					if (selection < 1 || selection > authorList.size()) {
						areValid = false;
						System.out.println("Invalid selections. Please try again.");
						break;
					}
				}
				if (areValid) {
					book.setAuthors(selections.stream().map(selection -> authorList.get(selection - 1))
							.collect(Collectors.toList()));
					isValid = true;
				}
			}
		}
		System.out.println("Choose publisher:");
		List<Publisher> publisherList = AdministratorService.getInstance().readPublishers();
		IntStream.range(0, publisherList.size())
				.forEach(i -> System.out.println(
						new StringBuilder("\t").append(i + 1).append(") ").append(publisherList.get(i).getName())
								.append(", ").append(publisherList.get(i).getAddress())));
		isValid = false;
		while (!isValid) {
			int selection = ScannerUtil.loopForInt();
			if (selection < 1 || selection > publisherList.size() + 1) {
				System.out.println("Invalid selection. Please try again.");
			} else {
				book.setPublisher(publisherList.get(selection - 1));
				isValid = true;
			}
		}
		System.out.println("Choose genres:");
		List<Genre> genreList = AdministratorService.getInstance().readGenres();
		IntStream.range(0, genreList.size()).forEach(i -> System.out
				.println(new StringBuilder("\t").append(i + 1).append(") ").append(genreList.get(i).getName())));
		isValid = false;
		while (!isValid) {
			List<Integer> selections = ScannerUtil.loopForInts();
			if (selections.isEmpty()) {
				System.out.println("Invalid selections. Please try again.");
			} else {
				boolean areValid = true;
				for (int selection : selections) {
					if (selection < 1 || selection > authorList.size()) {
						areValid = false;
						System.out.println("Invalid selections. Please try again.");
						break;
					}
				}
				if (areValid) {
					book.setGenres(selections.stream().map(selection -> genreList.get(selection - 1))
							.collect(Collectors.toList()));
					isValid = true;
				}
			}
		}
		AdministratorService.getInstance().updateBook(book);
		System.out.println("Book has been updated.");
		state = State.MENU;
	}

	public void delete() throws SQLException {
		int id = 0;
		System.out.println("Choose book:");
		List<Book> bookList = AdministratorService.getInstance().readBooks();
		IntStream.range(0, bookList.size()).forEach(i -> System.out.println(new StringBuilder("\t").append(i + 1)
				.append(") ").append(bookList.get(i).getTitle()).append(", [")
				.append(bookList.get(i).getAuthors().stream().map(author -> author.getName())
						.collect(Collectors.joining(", ")))
				.append("], ").append(bookList.get(i).getPublisher().getName()).append(", [").append(bookList.get(i)
						.getGenres().stream().map(genre -> genre.getName()).collect(Collectors.joining(", ")))
				.append("]")));
		System.out.println(new StringBuilder("\t").append(bookList.size() + 1).append(") Return to previous menu"));
		boolean isValid = false;
		while (!isValid) {
			int selection = ScannerUtil.loopForInt();
			if (selection < 1 || selection > bookList.size() + 1) {
				System.out.println("Invalid selection. Please try again.");
			} else if (selection == bookList.size() + 1) {
				state = State.MENU;
				return;
			} else {
				id = bookList.get(selection - 1).getId();
				isValid = true;
			}
		}
		AdministratorService.getInstance().deleteBook(id);
		System.out.println("Book has been deleted.");
		state = State.MENU;
	}
}
