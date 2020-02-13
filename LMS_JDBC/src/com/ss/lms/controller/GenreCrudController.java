package com.ss.lms.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.IntStream;

import com.ss.lms.entity.Genre;
import com.ss.lms.service.AdministratorService;
import com.ss.lms.util.ScannerUtil;

public class GenreCrudController implements IController {
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
		System.out.println("\t1) Create genre");
		System.out.println("\t2) Read genre list");
		System.out.println("\t3) Update genre");
		System.out.println("\t4) Delete genre");
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
		Genre genre = new Genre();
		System.out.println("Enter genre name:");
		genre.setName(ScannerUtil.loopForString());
		System.out.println(new StringBuilder("Are you sure you want to create genre with name: ")
				.append(genre.getName()).append("? (y/n)"));
		boolean isSelected = false;
		while (!isSelected) {
			String confirmation = ScannerUtil.loopForString().toLowerCase();
			switch (confirmation) {
			case "y":
				AdministratorService.getInstance().createGenre(genre);
				System.out.println("Genre has been created.");
				this.state = State.MENU;
				isSelected = true;
				break;
			case "n":
				System.out.println("Genre has not been created.");
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
		List<Genre> genreList = AdministratorService.getInstance().readGenres();
		if (genreList.isEmpty()) {
			System.out.println("Genre list is empty.");
		} else {
			genreList.stream().forEach(i -> System.out.println(i.getName()));
		}
		state = State.MENU;
	}

	public void update() throws SQLException {
		Genre genre = new Genre();
		System.out.println("Choose genre:");
		List<Genre> genreList = AdministratorService.getInstance().readGenres();
		IntStream.range(0, genreList.size()).forEach(i -> System.out
				.println(new StringBuilder("\t").append(i + 1).append(") ").append(genreList.get(i).getName())));
		System.out.println(new StringBuilder("\t").append(genreList.size() + 1).append(") Return to previous menu"));
		boolean isValid = false;
		while (!isValid) {
			int selection = ScannerUtil.loopForInt();
			if (selection < 1 || selection > genreList.size() + 1) {
				System.out.println("Invalid selection. Please try again");
			} else if (selection == genreList.size() + 1) {
				this.state = State.MENU;
				return;
			} else {
				genre.setId(genreList.get(selection - 1).getId());
				isValid = true;
			}
		}
		System.out.println("Enter new genre name:");
		genre.setName(ScannerUtil.loopForString());
		AdministratorService.getInstance().updateGenre(genre);
		System.out.println("Genre has been updated.");
		this.state = State.MENU;
	}

	public void delete() throws SQLException {
		int id = 0;
		System.out.println("Choose genre:");
		List<Genre> genreList = AdministratorService.getInstance().readGenres();
		IntStream.range(0, genreList.size()).forEach(i -> System.out
				.println(new StringBuilder("\t").append(i + 1).append(") ").append(genreList.get(i).getName())));
		System.out.println(new StringBuilder("\t").append(genreList.size() + 1).append(") Return to previous menu"));
		boolean isValid = false;
		while (!isValid) {
			int selection = ScannerUtil.loopForInt();
			if (selection < 1 || selection > genreList.size() + 1) {
				System.out.println("Invalid selection. Please try again");
			} else if (selection == genreList.size() + 1) {
				this.state = State.MENU;
				return;
			} else {
				id = genreList.get(selection - 1).getId();
				isValid = true;
			}
		}
		AdministratorService.getInstance().deleteGenre(id);
		System.out.println("Genre has been deleted.");
		this.state = State.MENU;
	}
}
