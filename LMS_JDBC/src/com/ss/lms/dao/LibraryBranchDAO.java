package com.ss.lms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ss.lms.entity.LibraryBranch;

public class LibraryBranchDAO extends BaseDAO<LibraryBranch> {
	public LibraryBranchDAO(Connection connection) {
		super(connection);
	}

	public void create(LibraryBranch libraryBranch) throws ClassNotFoundException, SQLException {
		save("insert into tbl_library_branch (branchName, branchAddress) values(?, ?)",
				new Object[] { libraryBranch.getName(), libraryBranch.getAddress() });
	}

	public void update(LibraryBranch libraryBranch) throws ClassNotFoundException, SQLException {
		save("update tbl_library_branch set branchName = ?, branchAddress = ? where branchId = ?",
				new Object[] { libraryBranch.getName(), libraryBranch.getAddress(), libraryBranch.getId() });
	}

	public void delete(Integer id) throws ClassNotFoundException, SQLException {
		save("delete from tbl_library_branch where branchId = ?", new Object[] { id });
	}

	public List<LibraryBranch> readAll() throws ClassNotFoundException, SQLException {
		return read("select * from tbl_library_branch", null);
	}

	public LibraryBranch readOne(Integer id) throws ClassNotFoundException, SQLException {
		PreparedStatement pstmt = connection.prepareStatement("select * from tbl_library_branch where branchId = ?");
		pstmt.setObject(1, id);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			BookDAO bookDao = new BookDAO(connection);
			BorrowerDAO borrowerDao = new BorrowerDAO(connection);
			LibraryBranch libraryBranch = new LibraryBranch();
			libraryBranch.setId(rs.getInt("branchId"));
			libraryBranch.setName(rs.getString("branchName"));
			libraryBranch.setAddress(rs.getString("branchAddress"));
			libraryBranch.setBooks(bookDao.readFirstLevel(
					"select * from tbl_book inner join tbl_book_copies on tbl_book.bookId = tbl_book_copies.bookId where tbl_book_copies.branchId = ?",
					new Object[] { rs.getInt("branchId") }));
			libraryBranch.setBorrowers(borrowerDao.readFirstLevel(
					"select * from tbl_borrower inner join tbl_book_loans on tbl_borrower.cardNo = tbl_book_loans.cardNo where tbl_book_loans.branchId = ?",
					new Object[] { rs.getInt("branchId") }));
			return libraryBranch;
		}
		return null;
	}

	public LibraryBranch readOneFirstLevel(Integer id) throws SQLException {
		PreparedStatement pstmt = connection.prepareStatement("select * from tbl_library_branch where branchId = ?");
		pstmt.setObject(1, id);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			LibraryBranch libraryBranch = new LibraryBranch();
			libraryBranch.setId(rs.getInt("branchId"));
			libraryBranch.setName(rs.getString("branchName"));
			libraryBranch.setAddress(rs.getString("branchAddress"));
			return libraryBranch;
		}
		return null;
	}

	@Override
	protected List<LibraryBranch> extractData(ResultSet rs) throws ClassNotFoundException, SQLException {
		List<LibraryBranch> libraryBranches = new ArrayList<LibraryBranch>();
		BookDAO bookDao = new BookDAO(connection);
		BorrowerDAO borrowerDao = new BorrowerDAO(connection);
		while (rs.next()) {
			LibraryBranch libraryBranch = new LibraryBranch();
			libraryBranch.setId(rs.getInt("branchId"));
			libraryBranch.setName(rs.getString("branchName"));
			libraryBranch.setAddress(rs.getString("branchAddress"));
			libraryBranch.setBooks(bookDao.readFirstLevel(
					"select * from tbl_book inner join tbl_book_copies on tbl_book.bookId = tbl_book_copies.bookId where tbl_book_copies.branchId = ?",
					new Object[] { rs.getInt("branchId") }));
			libraryBranch.setBorrowers(borrowerDao.readFirstLevel(
					"select * from tbl_borrower inner join tbl_book_loans on tbl_borrower.cardNo = tbl_book_loans.cardNo where tbl_book_loans.branchId = ?",
					new Object[] { rs.getInt("branchId") }));
			libraryBranches.add(libraryBranch);
		}
		return libraryBranches;
	}

	@Override
	protected List<LibraryBranch> extractDataFirstLevel(ResultSet rs) throws SQLException {
		List<LibraryBranch> libraryBranches = new ArrayList<LibraryBranch>();
		while (rs.next()) {
			LibraryBranch libraryBranch = new LibraryBranch();
			libraryBranch.setId(rs.getInt("branchId"));
			libraryBranch.setName(rs.getString("branchName"));
			libraryBranch.setAddress(rs.getString("branchAddress"));
			libraryBranches.add(libraryBranch);
		}
		return libraryBranches;
	}
}