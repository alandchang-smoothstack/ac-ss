package com.ss.lms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ss.lms.entity.Book;
import com.ss.lms.entity.BookCopy;
import com.ss.lms.entity.LibraryBranch;

public class BookCopyDAO extends BaseDAO<BookCopy> {
	public BookCopyDAO(Connection connection) {
		super(connection);
	}

	public void create(BookCopy bookCopy) throws ClassNotFoundException, SQLException {
		save("insert into tbl_book_copies (bookId, branchId, noOfCopies) values(?, ?, ?)",
				new Object[] { bookCopy.getBook().getId(), bookCopy.getLibraryBranch().getId(), bookCopy.getAmount() });
	}

	public void update(BookCopy bookCopy) throws ClassNotFoundException, SQLException {
		save("update tbl_book_copies set noOfCopies = ? where bookId = ? and branchId = ?",
				new Object[] { bookCopy.getAmount(), bookCopy.getBook().getId(), bookCopy.getLibraryBranch().getId() });
	}

	public void delete(Integer bookId, Integer libraryBranchId) throws ClassNotFoundException, SQLException {
		save("delete from tbl_book_copies where bookId = ? and branchId = ?", new Object[] { bookId, libraryBranchId });
	}

	public void deleteByBookId(Integer bookId) throws ClassNotFoundException, SQLException {
		save("delete from tbl_book_copies where bookId = ?", new Object[] { bookId });
	}

	public void deleteByLibraryBranchId(Integer libraryBranchId) throws ClassNotFoundException, SQLException {
		save("delete from tbl_book_copies where branchId = ?", new Object[] { libraryBranchId });
	}

	public List<BookCopy> readAll() throws ClassNotFoundException, SQLException {
		return read("select * from tbl_book_copies", null);
	}

	public BookCopy readOne(Integer bookId, Integer libraryBranchId) throws ClassNotFoundException, SQLException {
		PreparedStatement pstmt = connection
				.prepareStatement("select * from tbl_book_copies where bookId = ? and branchId = ?");
		pstmt.setObject(1, bookId);
		pstmt.setObject(2, libraryBranchId);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			BookDAO bookDao = new BookDAO(connection);
			LibraryBranchDAO libraryBranchDao = new LibraryBranchDAO(connection);
			BookCopy bookCopy = new BookCopy();
			bookCopy.setBook(bookDao.readOneFirstLevel(rs.getInt("bookId")));
			bookCopy.setLibraryBranch(libraryBranchDao.readOneFirstLevel(rs.getInt("branchId")));
			bookCopy.setAmount(rs.getInt("noOfCopies"));
			return bookCopy;
		}
		return null;
	}

	public BookCopy readOneFirstLevel(Integer bookId, Integer libraryBranchId) throws SQLException {
		PreparedStatement pstmt = connection
				.prepareStatement("select * from tbl_book_copies where bookId = ? and branchId = ?");
		pstmt.setObject(1, bookId);
		pstmt.setObject(2, libraryBranchId);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			BookCopy bookCopy = new BookCopy();
			Book book = new Book();
			book.setId(rs.getInt("bookId"));
			bookCopy.setBook(book);
			LibraryBranch libraryBranch = new LibraryBranch();
			libraryBranch.setId(rs.getInt("branchId"));
			bookCopy.setLibraryBranch(libraryBranch);
			bookCopy.setAmount(rs.getInt("noOfCopies"));
			return bookCopy;
		}
		return null;
	}

	@Override
	protected List<BookCopy> extractData(ResultSet rs) throws ClassNotFoundException, SQLException {
		List<BookCopy> bookCopies = new ArrayList<BookCopy>();
		BookDAO bookDao = new BookDAO(connection);
		LibraryBranchDAO libraryBranchDao = new LibraryBranchDAO(connection);
		while (rs.next()) {
			BookCopy bookCopy = new BookCopy();
			bookCopy.setBook(bookDao.readOneFirstLevel(rs.getInt("bookId")));
			bookCopy.setLibraryBranch(libraryBranchDao.readOneFirstLevel(rs.getInt("branchId")));
			bookCopy.setAmount(rs.getInt("noOfCopies"));
			bookCopies.add(bookCopy);
		}
		return bookCopies;
	}

	@Override
	protected List<BookCopy> extractDataFirstLevel(ResultSet rs) throws SQLException {
		List<BookCopy> bookCopies = new ArrayList<BookCopy>();
		while (rs.next()) {
			BookCopy bookCopy = new BookCopy();
			Book book = new Book();
			book.setId(rs.getInt("bookId"));
			bookCopy.setBook(book);
			LibraryBranch libraryBranch = new LibraryBranch();
			libraryBranch.setId(rs.getInt("branchId"));
			bookCopy.setLibraryBranch(libraryBranch);
			bookCopy.setAmount(rs.getInt("noOfCopies"));
			bookCopies.add(bookCopy);
		}
		return bookCopies;
	}
}