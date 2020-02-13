package com.ss.lms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ss.lms.entity.Borrower;

public class BorrowerDAO extends BaseDAO<Borrower> {
	public BorrowerDAO(Connection connection) {
		super(connection);
	}

	public void create(Borrower borrower) throws ClassNotFoundException, SQLException {
		save("insert into tbl_borrower (name, address, phone) values(?, ?, ?)",
				new Object[] { borrower.getName(), borrower.getAddress(), borrower.getPhone() });
	}

	public void update(Borrower borrower) throws ClassNotFoundException, SQLException {
		save("update tbl_borrower set name = ?, address = ?, phone = ? where cardNo = ?", new Object[] {
				borrower.getName(), borrower.getAddress(), borrower.getPhone(), borrower.getCardNumber() });
	}

	public void delete(Integer cardNumber) throws ClassNotFoundException, SQLException {
		save("delete from tbl_borrower where cardNo = ?", new Object[] { cardNumber });
	}

	public List<Borrower> readAll() throws ClassNotFoundException, SQLException {
		return read("select * from tbl_borrower", null);
	}

	public Borrower readOneFirstLevel(Integer cardNumber) throws SQLException {
		PreparedStatement pstmt = connection.prepareStatement("select * from tbl_borrower where cardNo = ?");
		pstmt.setObject(1, cardNumber);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			Borrower borrower = new Borrower();
			borrower.setCardNumber(rs.getInt("cardNo"));
			borrower.setName(rs.getString("name"));
			borrower.setAddress(rs.getString("address"));
			borrower.setPhone(rs.getString("phone"));
			return borrower;
		}
		return null;
	}

	@Override
	protected List<Borrower> extractData(ResultSet rs) throws ClassNotFoundException, SQLException {
		List<Borrower> borrowers = new ArrayList<Borrower>();
		BookDAO bookDao = new BookDAO(connection);
		while (rs.next()) {
			Borrower borrower = new Borrower();
			borrower.setCardNumber(rs.getInt("cardNo"));
			borrower.setName(rs.getString("name"));
			borrower.setAddress(rs.getString("address"));
			borrower.setPhone(rs.getString("phone"));
			borrower.setBooks(bookDao.readFirstLevel(
					"select * from tbl_book inner join tbl_book_loans on tbl_book.bookId = tbl_book_loans.bookId where tbl_book_loans.cardNo = ?",
					new Object[] { rs.getInt("cardNo") }));
			borrowers.add(borrower);
		}
		return borrowers;
	}

	@Override
	protected List<Borrower> extractDataFirstLevel(ResultSet rs) throws SQLException {
		List<Borrower> borrowers = new ArrayList<Borrower>();
		while (rs.next()) {
			Borrower borrower = new Borrower();
			borrower.setCardNumber(rs.getInt("cardNo"));
			borrower.setName(rs.getString("name"));
			borrower.setAddress(rs.getString("address"));
			borrower.setPhone(rs.getString("phone"));
			borrowers.add(borrower);
		}
		return borrowers;
	}
}