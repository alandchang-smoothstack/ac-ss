package com.ss.lms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ss.lms.entity.Author;

public class AuthorDAO extends BaseDAO<Author> {
	public AuthorDAO(Connection connection) {
		super(connection);
	}

	public void create(Author author) throws ClassNotFoundException, SQLException {
		save("insert into tbl_author (authorName) values(?)", new Object[] { author.getName() });
	}

	public void update(Author author) throws ClassNotFoundException, SQLException {
		save("update tbl_author set authorName = ? where authorId = ?",
				new Object[] { author.getName(), author.getId() });
	}

	public void delete(Integer id) throws ClassNotFoundException, SQLException {
		save("delete from tbl_author where authorId = ?", new Object[] { id });
	}

	public List<Author> readAll() throws ClassNotFoundException, SQLException {
		return read("select * from tbl_author", null);
	}

	public Author readOneFirstLevel(Integer id) throws SQLException {
		PreparedStatement pstmt = connection.prepareStatement("select * from tbl_author where authorId = ?");
		pstmt.setObject(1, id);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			Author author = new Author();
			author.setId(rs.getInt("authorId"));
			author.setName(rs.getString("authorName"));
			return author;
		}
		return null;
	}

	@Override
	protected List<Author> extractData(ResultSet rs) throws ClassNotFoundException, SQLException {
		List<Author> authors = new ArrayList<Author>();
		BookDAO bookDao = new BookDAO(connection);
		while (rs.next()) {
			Author author = new Author();
			author.setId(rs.getInt("authorId"));
			author.setName(rs.getString("authorName"));
			author.setBooks(bookDao.readFirstLevel(
					"select * from tbl_book inner join tbl_book_authors on tbl_book.bookId = tbl_book_authors.bookId where tbl_book_authors.authorId = ?",
					new Object[] { rs.getInt("authorId") }));
			authors.add(author);
		}
		return authors;
	}

	@Override
	protected List<Author> extractDataFirstLevel(ResultSet rs) throws SQLException {
		List<Author> authors = new ArrayList<Author>();
		while (rs.next()) {
			Author author = new Author();
			author.setId(rs.getInt("authorId"));
			author.setName(rs.getString("authorName"));
			authors.add(author);
		}
		return authors;
	}
}