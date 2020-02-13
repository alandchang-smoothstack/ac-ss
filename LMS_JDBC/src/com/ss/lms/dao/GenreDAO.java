package com.ss.lms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ss.lms.entity.Genre;

public class GenreDAO extends BaseDAO<Genre> {
	public GenreDAO(Connection connection) {
		super(connection);
	}

	public void create(Genre genre) throws ClassNotFoundException, SQLException {
		save("insert into tbl_genre (genre_name) values(?)", new Object[] { genre.getName() });
	}

	public void update(Genre genre) throws ClassNotFoundException, SQLException {
		save("update tbl_genre set genre_name = ? where genre_id = ?", new Object[] { genre.getName(), genre.getId() });
	}

	public void delete(Integer id) throws ClassNotFoundException, SQLException {
		save("delete from tbl_genre where genre_id = ?", new Object[] { id });
	}

	public List<Genre> readAll() throws ClassNotFoundException, SQLException {
		return read("select * from tbl_genre", null);
	}

	public Genre readOneFirstLevel(Integer id) throws SQLException {
		PreparedStatement pstmt = connection.prepareStatement("select * from tbl_genre where genre_id = ?");
		pstmt.setObject(1, id);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			Genre genre = new Genre();
			genre.setId(rs.getInt("genre_id"));
			genre.setName(rs.getString("genre_name"));
			return genre;
		}
		return null;
	}

	@Override
	protected List<Genre> extractData(ResultSet rs) throws ClassNotFoundException, SQLException {
		List<Genre> genres = new ArrayList<Genre>();
		BookDAO bookDao = new BookDAO(connection);
		while (rs.next()) {
			Genre genre = new Genre();
			genre.setId(rs.getInt("genre_id"));
			genre.setName(rs.getString("genre_name"));
			genre.setBooks(bookDao.readFirstLevel(
					"select * from tbl_book inner join tbl_book_genres on tbl_book.bookId = tbl_book_genres.bookId where tbl_book_genres.genre_id = ?",
					new Object[] { rs.getInt("genre_id") }));
			genres.add(genre);
		}
		return genres;
	}

	@Override
	protected List<Genre> extractDataFirstLevel(ResultSet rs) throws SQLException {
		List<Genre> genres = new ArrayList<Genre>();
		while (rs.next()) {
			Genre genre = new Genre();
			genre.setId(rs.getInt("genre_id"));
			genre.setName(rs.getString("genre_name"));
			genres.add(genre);
		}
		return genres;
	}
}