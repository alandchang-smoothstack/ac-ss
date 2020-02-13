package com.ss.lms.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.ss.lms.entity.Book;
import com.ss.lms.entity.BookLoan;
import com.ss.lms.entity.Borrower;
import com.ss.lms.entity.LibraryBranch;

public class BookLoanDAO extends BaseDAO<BookLoan> {
	public BookLoanDAO(Connection connection) {
		super(connection);
	}

	public void create(BookLoan bookLoan) throws ClassNotFoundException, SQLException {
		save("insert into tbl_book_loans (bookId, branchId, cardNo, dateOut, dueDate) values(?, ?, ?, ?, ?)",
				new Object[] { bookLoan.getBook().getId(), bookLoan.getLibraryBranch().getId(),
						bookLoan.getBorrower().getCardNumber(), Date.valueOf(LocalDate.now()),
						Date.valueOf(LocalDate.now().plusWeeks(1)) });
	}

	public void update(BookLoan bookLoan) throws ClassNotFoundException, SQLException {
		save("update tbl_book_loans set dateIn = ? where bookId = ? and branchId = ? and cardNo = ?",
				new Object[] { Date.valueOf(bookLoan.getDateIn()), bookLoan.getBook().getId(),
						bookLoan.getLibraryBranch().getId(), bookLoan.getBorrower().getCardNumber() });
	}

	public void delete(Integer bookId, Integer libraryBranchId, Integer cardNumber)
			throws ClassNotFoundException, SQLException {
		save("delete from tbl_book_loans where bookId = ? and branchId = ? and cardNo = ?",
				new Object[] { bookId, libraryBranchId, cardNumber });
	}

	public void deleteByBookId(Integer bookId) throws ClassNotFoundException, SQLException {
		save("delete from tbl_book_loans where bookId = ?", new Object[] { bookId });
	}

	public void deleteByLibraryBranchId(Integer libraryBranchId) throws ClassNotFoundException, SQLException {
		save("delete from tbl_book_loans where branchId = ?", new Object[] { libraryBranchId });
	}

	public void deleteByCardNumber(Integer cardNumber) throws ClassNotFoundException, SQLException {
		save("delete from tbl_book_loans where cardNo = ?", new Object[] { cardNumber });
	}

	public List<BookLoan> readAll() throws ClassNotFoundException, SQLException {
		return read("select * from tbl_book_loans", null);
	}

	@Override
	protected List<BookLoan> extractData(ResultSet rs) throws ClassNotFoundException, SQLException {
		List<BookLoan> bookLoans = new ArrayList<BookLoan>();
		BookDAO bookDao = new BookDAO(connection);
		LibraryBranchDAO libraryBranchDao = new LibraryBranchDAO(connection);
		BorrowerDAO borrowerDao = new BorrowerDAO(connection);
		while (rs.next()) {
			BookLoan bookLoan = new BookLoan();
			bookLoan.setBook(bookDao.readOneFirstLevel(rs.getInt("bookId")));
			bookLoan.setLibraryBranch(libraryBranchDao.readOneFirstLevel(rs.getInt("branchId")));
			bookLoan.setBorrower(borrowerDao.readOneFirstLevel(rs.getInt("cardNo")));
			bookLoan.setDateOut(rs.getDate("dateOut").toLocalDate());
			bookLoan.setDueDate(rs.getDate("dueDate").toLocalDate());
			bookLoan.setDateIn(rs.getDate("dateIn").toLocalDate());
			bookLoans.add(bookLoan);
		}
		return bookLoans;
	}

	@Override
	protected List<BookLoan> extractDataFirstLevel(ResultSet rs) throws SQLException {
		List<BookLoan> bookLoans = new ArrayList<BookLoan>();
		while (rs.next()) {
			BookLoan bookLoan = new BookLoan();
			Book book = new Book();
			book.setId(rs.getInt("bookId"));
			bookLoan.setBook(book);
			LibraryBranch libraryBranch = new LibraryBranch();
			libraryBranch.setId(rs.getInt("branchId"));
			bookLoan.setLibraryBranch(libraryBranch);
			Borrower borrower = new Borrower();
			borrower.setCardNumber(rs.getInt("cardNo"));
			bookLoan.setBorrower(borrower);
			bookLoan.setDateOut(rs.getDate("dateOut").toLocalDate());
			bookLoan.setDueDate(rs.getDate("dueDate").toLocalDate());
			bookLoan.setDateIn(rs.getDate("dateIn").toLocalDate());
			bookLoans.add(bookLoan);
		}
		return bookLoans;
	}
}