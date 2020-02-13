package com.ss.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ss.lms.entity.Book;
import com.ss.lms.entity.BookGenre;
import com.ss.lms.entity.Genre;

public class BookGenreDAO extends BaseDAO<BookGenre> {
	public BookGenreDAO(Connection connection) {
		super(connection);
	}

	public void create(BookGenre bookGenre) throws ClassNotFoundException, SQLException {
		save("insert into tbl_book_genres (genre_id, bookId) values(?, ?)",
				new Object[] { bookGenre.getGenre().getId(), bookGenre.getBook().getId() });
	}

	public void delete(Integer genreId, Integer bookId) throws ClassNotFoundException, SQLException {
		save("delete from tbl_book_genres where genre_id = ? and bookId = ?", new Object[] { genreId, bookId });
	}

	public void deleteByGenreId(Integer genreId) throws ClassNotFoundException, SQLException {
		save("delete from tbl_book_genres where genre_id = ?", new Object[] { genreId });
	}

	public void deleteByBookId(Integer bookId) throws ClassNotFoundException, SQLException {
		save("delete from tbl_book_genres where bookId = ?", new Object[] { bookId });
	}

	public List<BookGenre> readAll() throws ClassNotFoundException, SQLException {
		return read("select * from tbl_book_genres", null);
	}

	@Override
	protected List<BookGenre> extractData(ResultSet rs) throws ClassNotFoundException, SQLException {
		List<BookGenre> bookGenres = new ArrayList<BookGenre>();
		GenreDAO genreDao = new GenreDAO(connection);
		BookDAO bookDao = new BookDAO(connection);
		while (rs.next()) {
			BookGenre bookGenre = new BookGenre();
			bookGenre.setGenre(genreDao.readOneFirstLevel(rs.getInt("genre_id")));
			bookGenre.setBook(bookDao.readOneFirstLevel(rs.getInt("bookId")));
			bookGenres.add(bookGenre);
		}
		return bookGenres;
	}

	@Override
	protected List<BookGenre> extractDataFirstLevel(ResultSet rs) throws SQLException {
		List<BookGenre> bookGenres = new ArrayList<BookGenre>();
		while (rs.next()) {
			BookGenre bookGenre = new BookGenre();
			Genre genre = new Genre();
			genre.setId(rs.getInt("genre_id"));
			bookGenre.setGenre(genre);
			Book book = new Book();
			book.setId(rs.getInt("bookId"));
			bookGenre.setBook(book);
			bookGenres.add(bookGenre);
		}
		return bookGenres;
	}
}