package com.ss.lms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ss.lms.entity.Book;
import com.ss.lms.entity.Publisher;

public class BookDAO extends BaseDAO<Book> {
	public BookDAO(Connection connection) {
		super(connection);
	}

	public void create(Book book) throws ClassNotFoundException, SQLException {
		save("insert into tbl_book (title, pubId) values(?, ?)",
				new Object[] { book.getTitle(), book.getPublisher().getId() });
	}

	public Integer createReturnPk(Book book) throws ClassNotFoundException, SQLException {
		return saveReturnPk("insert into tbl_book (title, pubId) values(?, ?)",
				new Object[] { book.getTitle(), book.getPublisher().getId() });
	}

	public void update(Book book) throws ClassNotFoundException, SQLException {
		save("update tbl_book set title = ?, pubId = ? where bookId = ?",
				new Object[] { book.getTitle(), book.getPublisher().getId(), book.getId() });
	}

	public void delete(Integer id) throws ClassNotFoundException, SQLException {
		save("delete from tbl_book where bookId = ?", new Object[] { id });
	}

	public void deleteByPublisherId(Integer publisherId) throws ClassNotFoundException, SQLException {
		save("delete from tbl_book where pubId = ?", new Object[] { publisherId });
	}

	public void deleteNoAuthors() throws ClassNotFoundException, SQLException {
		save("delete from tbl_book where tbl_book.bookId not in (select tbl_book_authors.bookId from tbl_book_authors)",
				null);
	}

	public List<Book> readAll() throws ClassNotFoundException, SQLException {
		return read("select * from tbl_book", null);
	}

	public List<Book> readAllAvailable(Integer cardNumber, Integer libraryBranchId)
			throws ClassNotFoundException, SQLException {
		return read("select * from tbl_book inner join tbl_book_copies on tbl_book.bookId = tbl_book_copies.bookId"
				+ " where tbl_book.bookId not in (select tbl_book_loans.bookId from tbl_book_loans where tbl_book_loans.cardNo = ?) and tbl_book_copies.branchId = ? and tbl_book_copies.noOfCopies > 0",
				new Object[] { cardNumber, libraryBranchId });
	}

	public List<Book> readAllBorrowed(Integer cardNumber, Integer libraryBranchId)
			throws ClassNotFoundException, SQLException {
		return read(
				"select * from tbl_book inner join tbl_book_loans on tbl_book.bookId = tbl_book_loans.bookId"
						+ " where tbl_book_loans.cardNo = ? and tbl_book_loans.branchId = ?",
				new Object[] { cardNumber, libraryBranchId });
	}

	public Book readOneFirstLevel(Integer id) throws SQLException {
		PreparedStatement pstmt = connection.prepareStatement("select * from tbl_book where bookId = ?");
		pstmt.setObject(1, id);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			Book book = new Book();
			Publisher publisher = new Publisher();
			book.setId(rs.getInt("bookId"));
			book.setTitle(rs.getString("title"));
			publisher.setId(rs.getInt("pubId"));
			book.setPublisher(publisher);
			return book;
		}
		return null;
	}

	@Override
	protected List<Book> extractData(ResultSet rs) throws ClassNotFoundException, SQLException {
		List<Book> books = new ArrayList<>();
		PublisherDAO publisherDao = new PublisherDAO(connection);
		AuthorDAO authorDao = new AuthorDAO(connection);
		GenreDAO genreDao = new GenreDAO(connection);
		LibraryBranchDAO libraryBranchDao = new LibraryBranchDAO(connection);
		BorrowerDAO borrowerDao = new BorrowerDAO(connection);
		while (rs.next()) {
			Book book = new Book();
			book.setId(rs.getInt("bookId"));
			book.setTitle(rs.getString("title"));
			book.setPublisher(publisherDao.readOneFirstLevel(rs.getInt("pubId")));
			book.setAuthors(authorDao.readFirstLevel(
					"select * from tbl_author inner join tbl_book_authors on tbl_author.authorId = tbl_book_authors.authorId where tbl_book_authors.bookId = ?",
					new Object[] { rs.getInt("bookId") }));
			book.setGenres(genreDao.readFirstLevel(
					"select * from tbl_genre inner join tbl_book_genres on tbl_genre.genre_id = tbl_book_genres.genre_id where tbl_book_genres.bookId = ?",
					new Object[] { rs.getInt("bookId") }));
			book.setLibraryBranches(libraryBranchDao.readFirstLevel(
					"select * from tbl_library_branch inner join tbl_book_loans on tbl_library_branch.branchId = tbl_book_loans.branchId where tbl_book_loans.bookId = ?",
					new Object[] { rs.getInt("bookId") }));
			book.setBorrowers(borrowerDao.readFirstLevel(
					"select * from tbl_borrower inner join tbl_book_loans on tbl_borrower.cardNo = tbl_book_loans.cardNo where tbl_book_loans.bookId = ?",
					new Object[] { rs.getInt("bookId") }));
			books.add(book);
		}
		return books;
	}

	@Override
	protected List<Book> extractDataFirstLevel(ResultSet rs) throws SQLException {
		List<Book> books = new ArrayList<>();
		while (rs.next()) {
			Book book = new Book();
			book.setId(rs.getInt("bookId"));
			book.setTitle(rs.getString("title"));
			Publisher publisher = new Publisher();
			publisher.setId(rs.getInt("pubId"));
			book.setPublisher(publisher);
			books.add(book);
		}
		return books;
	}
}