package com.ss.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ss.lms.entity.Author;
import com.ss.lms.entity.Book;
import com.ss.lms.entity.BookAuthor;

public class BookAuthorDAO extends BaseDAO<BookAuthor> {
	public BookAuthorDAO(Connection connection) {
		super(connection);
	}

	public void create(BookAuthor bookAuthor) throws ClassNotFoundException, SQLException {
		save("insert into tbl_book_authors (bookId, authorId) values(?, ?)",
				new Object[] { bookAuthor.getBook().getId(), bookAuthor.getAuthor().getId() });
	}

	public void delete(Integer bookId, Integer authorId) throws ClassNotFoundException, SQLException {
		save("delete from tbl_book_authors where bookId = ? and authorId = ?", new Object[] { bookId, authorId });
	}

	public void deleteByBookId(Integer bookId) throws ClassNotFoundException, SQLException {
		save("delete from tbl_book_authors where bookId = ?", new Object[] { bookId });
	}

	public void deleteByAuthorId(Integer authorId) throws ClassNotFoundException, SQLException {
		save("delete from tbl_book_authors where authorId = ?", new Object[] { authorId });
	}

	public List<BookAuthor> readAll() throws ClassNotFoundException, SQLException {
		return read("select * from tbl_book_authors", null);
	}

	@Override
	protected List<BookAuthor> extractData(ResultSet rs) throws ClassNotFoundException, SQLException {
		List<BookAuthor> bookAuthors = new ArrayList<BookAuthor>();
		BookDAO bookDao = new BookDAO(connection);
		AuthorDAO authorDao = new AuthorDAO(connection);
		while (rs.next()) {
			BookAuthor bookAuthor = new BookAuthor();
			bookAuthor.setBook(bookDao.readOneFirstLevel(rs.getInt("bookId")));
			bookAuthor.setAuthor(authorDao.readOneFirstLevel(rs.getInt("authorId")));
			bookAuthors.add(bookAuthor);
		}
		return bookAuthors;
	}

	@Override
	protected List<BookAuthor> extractDataFirstLevel(ResultSet rs) throws SQLException {
		List<BookAuthor> bookAuthors = new ArrayList<BookAuthor>();
		while (rs.next()) {
			BookAuthor bookAuthor = new BookAuthor();
			Book book = new Book();
			book.setId(rs.getInt("bookId"));
			bookAuthor.setBook(book);
			Author author = new Author();
			author.setId(rs.getInt("authorId"));
			bookAuthor.setAuthor(author);
			bookAuthors.add(bookAuthor);
		}
		return bookAuthors;
	}
}