package com.ss.lms.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ss.lms.dao.BookCopyDAO;
import com.ss.lms.dao.BookDAO;
import com.ss.lms.dao.BookLoanDAO;
import com.ss.lms.dao.BorrowerDAO;
import com.ss.lms.dao.LibraryBranchDAO;
import com.ss.lms.entity.Book;
import com.ss.lms.entity.BookCopy;
import com.ss.lms.entity.BookLoan;
import com.ss.lms.entity.Borrower;
import com.ss.lms.entity.LibraryBranch;
import com.ss.lms.util.ConnectUtil;

public class BorrowerService {
	private static BorrowerService instance;

	public static BorrowerService getInstance() {
		if (instance == null) {
			synchronized (AdministratorService.class) {
				if (instance == null) {
					instance = new BorrowerService();
				}
			}
		}
		return instance;
	}

	public void checkinBook(Integer bookId, Integer libraryBranchId, Integer cardNumber) throws SQLException {
		Connection connection = null;
		try {
			connection = ConnectUtil.getInstance().getConnection();
			BookLoanDAO bookLoanDao = new BookLoanDAO(connection);
			BookLoan bookLoan = new BookLoan();
			Book book = new Book();
			book.setId(bookId);
			bookLoan.setBook(book);
			LibraryBranch libraryBranch = new LibraryBranch();
			libraryBranch.setId(libraryBranchId);
			bookLoan.setLibraryBranch(libraryBranch);
			Borrower borrower = new Borrower();
			borrower.setCardNumber(cardNumber);
			bookLoan.setBorrower(borrower);
			bookLoanDao.delete(bookId, libraryBranchId, cardNumber);
			BookCopyDAO bookCopyDao = new BookCopyDAO(connection);
			BookCopy bookCopy = bookCopyDao.readOneFirstLevel(bookId, libraryBranchId);
			bookCopy.setAmount(bookCopy.getAmount() + 1);
			bookCopyDao.update(bookCopy);
			connection.commit();
		} catch (ClassNotFoundException | SQLException e) {
			connection.rollback();
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	public void checkoutBook(Integer bookId, Integer libraryBranchId, Integer cardNumber) throws SQLException {
		Connection connection = null;
		try {
			connection = ConnectUtil.getInstance().getConnection();
			BookLoanDAO bookLoanDao = new BookLoanDAO(connection);
			BookLoan bookLoan = new BookLoan();
			Book book = new Book();
			book.setId(bookId);
			bookLoan.setBook(book);
			LibraryBranch libraryBranch = new LibraryBranch();
			libraryBranch.setId(libraryBranchId);
			bookLoan.setLibraryBranch(libraryBranch);
			Borrower borrower = new Borrower();
			borrower.setCardNumber(cardNumber);
			bookLoan.setBorrower(borrower);
			bookLoanDao.create(bookLoan);
			BookCopyDAO bookCopyDao = new BookCopyDAO(connection);
			BookCopy bookCopy = bookCopyDao.readOneFirstLevel(bookId, libraryBranchId);
			bookCopy.setAmount(bookCopy.getAmount() - 1);
			bookCopyDao.update(bookCopy);
			connection.commit();
		} catch (ClassNotFoundException | SQLException e) {
			connection.rollback();
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	public List<Book> readAvailableBooks(Integer cardNumber, Integer libraryBranchId) throws SQLException {
		Connection connection = null;
		try {
			connection = ConnectUtil.getInstance().getConnection();
			return new BookDAO(connection).readAllAvailable(cardNumber, libraryBranchId);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
		return new ArrayList<Book>();
	}

	public List<Book> readBorrowedBooks(Integer cardNumber, Integer libraryBranchId) throws SQLException {
		Connection connection = null;
		try {
			connection = ConnectUtil.getInstance().getConnection();
			return new BookDAO(connection).readAllBorrowed(cardNumber, libraryBranchId);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
		return new ArrayList<Book>();
	}

	public Boolean borrowerExists(Integer cardNumber) throws SQLException {
		Connection connection = null;
		try {
			connection = ConnectUtil.getInstance().getConnection();
			return new BorrowerDAO(connection).readOneFirstLevel(cardNumber) != null;

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
		return null;
	}

	public List<LibraryBranch> readLibraryBranches() throws SQLException {
		Connection connection = null;
		try {
			connection = ConnectUtil.getInstance().getConnection();
			return new LibraryBranchDAO(connection).readAll();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
		return new ArrayList<LibraryBranch>();
	}
}
