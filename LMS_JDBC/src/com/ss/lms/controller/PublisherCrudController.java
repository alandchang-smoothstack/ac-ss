package com.ss.lms.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.IntStream;

import com.ss.lms.entity.Publisher;
import com.ss.lms.service.AdministratorService;
import com.ss.lms.util.ScannerUtil;

public class PublisherCrudController implements IController {
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
		System.out.println("\t1) Create publisher");
		System.out.println("\t2) Read publisher list");
		System.out.println("\t3) Update publisher");
		System.out.println("\t4) Delete publisher");
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
		Publisher publisher = new Publisher();
		System.out.println("Enter publisher name:");
		publisher.setName(ScannerUtil.loopForString());
		System.out.println("Enter publisher address:");
		publisher.setAddress(ScannerUtil.loopForString());
		System.out.println("Enter publisher phone:");
		publisher.setPhone(ScannerUtil.loopForString());
		System.out.println(new StringBuilder("Are you sure you want to create publisher with name: ")
				.append(publisher.getName()).append(", address: ").append(publisher.getAddress()).append(", phone: ")
				.append(publisher.getPhone()).append("? (y/n)"));
		boolean isSelected = false;
		while (!isSelected) {
			String confirmation = ScannerUtil.loopForString().toLowerCase();
			switch (confirmation) {
			case "y":
				AdministratorService.getInstance().createPublisher(publisher);
				System.out.println("Publisher has been created.");
				state = State.MENU;
				isSelected = true;
				break;
			case "n":
				System.out.println("Publisher has not been created.");
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
		List<Publisher> publisherList = AdministratorService.getInstance().readPublishers();
		if (publisherList.isEmpty()) {
			System.out.println("Publisher list is empty.");
		} else {
			publisherList.stream().forEach(i -> System.out.println(new StringBuilder(i.getName()).append(", ")
					.append(i.getAddress()).append(", ").append(i.getPhone())));
		}
		state = State.MENU;
	}

	public void update() throws SQLException {
		Publisher publisher = new Publisher();
		System.out.println("Choose publisher:");
		List<Publisher> publisherList = AdministratorService.getInstance().readPublishers();
		IntStream.range(0, publisherList.size())
				.forEach(i -> System.out.println(new StringBuilder("\t").append(i + 1).append(") ")
						.append(publisherList.get(i).getName()).append(", ").append(publisherList.get(i).getAddress())
						.append(", ").append(publisherList.get(i).getPhone())));
		System.out
				.println(new StringBuilder("\t").append(publisherList.size() + 1).append(") Return to previous menu"));
		boolean isValid = false;
		while (!isValid) {
			int selection = ScannerUtil.loopForInt();
			if (selection < 1 || selection > publisherList.size() + 1) {
				System.out.println("Invalid selection. Please try again");
			} else if (selection == publisherList.size() + 1) {
				state = State.MENU;
				return;
			} else {
				publisher.setId(publisherList.get(selection - 1).getId());
				isValid = true;
			}
		}
		System.out.println("Enter new publisher name:");
		publisher.setName(ScannerUtil.loopForString());
		System.out.println("Enter new publisher address:");
		publisher.setAddress(ScannerUtil.loopForString());
		System.out.println("Enter new publisher phone:");
		publisher.setPhone(ScannerUtil.loopForString());
		AdministratorService.getInstance().updatePublisher(publisher);
		System.out.println("Publisher has been updated.");
		state = State.MENU;
	}

	public void delete() throws SQLException {
		int id = 0;
		System.out.println("Choose publisher:");
		List<Publisher> publisherList = AdministratorService.getInstance().readPublishers();
		IntStream.range(0, publisherList.size())
				.forEach(i -> System.out.println(new StringBuilder("\t").append(i + 1).append(") ")
						.append(publisherList.get(i).getName()).append(", ").append(publisherList.get(i).getAddress())
						.append(", ").append(publisherList.get(i).getPhone())));
		System.out
				.println(new StringBuilder("\t").append(publisherList.size() + 1).append(") Return to previous menu"));
		boolean isValid = false;
		while (!isValid) {
			int selection = ScannerUtil.loopForInt();
			if (selection < 1 || selection > publisherList.size() + 1) {
				System.out.println("Invalid selection. Please try again");
			} else if (selection == publisherList.size() + 1) {
				state = State.MENU;
				return;
			} else {
				id = publisherList.get(selection - 1).getId();
				isValid = true;
			}
		}
		AdministratorService.getInstance().deletePublisher(id);
		System.out.println("Publisher has been deleted.");
		state = State.MENU;
	}
}
