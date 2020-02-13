package com.ss.lms.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ss.lms.dao.AuthorDAO;
import com.ss.lms.dao.BookAuthorDAO;
import com.ss.lms.dao.BookCopyDAO;
import com.ss.lms.dao.BookDAO;
import com.ss.lms.dao.BookGenreDAO;
import com.ss.lms.dao.BookLoanDAO;
import com.ss.lms.dao.BorrowerDAO;
import com.ss.lms.dao.GenreDAO;
import com.ss.lms.dao.LibraryBranchDAO;
import com.ss.lms.dao.PublisherDAO;
import com.ss.lms.entity.Author;
import com.ss.lms.entity.Book;
import com.ss.lms.entity.BookAuthor;
import com.ss.lms.entity.BookGenre;
import com.ss.lms.entity.Borrower;
import com.ss.lms.entity.Genre;
import com.ss.lms.entity.LibraryBranch;
import com.ss.lms.entity.Publisher;
import com.ss.lms.util.ConnectUtil;

public class AdministratorService {
	private static AdministratorService instance;

	public static AdministratorService getInstance() {
		if (instance == null) {
			synchronized (AdministratorService.class) {
				if (instance == null) {
					instance = new AdministratorService();
				}
			}
		}
		return instance;
	}

	public void createAuthor(Author author) throws SQLException {
		Connection connection = null;
		try {
			connection = ConnectUtil.getInstance().getConnection();
			new AuthorDAO(connection).create(author);
			connection.commit();
		} catch (ClassNotFoundException | SQLException e) {
			connection.rollback();
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	public List<Author> readAuthors() throws SQLException {
		Connection connection = null;
		try {
			connection = ConnectUtil.getInstance().getConnection();
			return new AuthorDAO(connection).readAll();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
		return new ArrayList<Author>();
	}

	public void updateAuthor(Author author) throws SQLException {
		Connection connection = null;
		try {
			connection = ConnectUtil.getInstance().getConnection();
			new AuthorDAO(connection).update(author);
			connection.commit();
		} catch (ClassNotFoundException | SQLException e) {
			connection.rollback();
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	public void deleteAuthor(int id) throws SQLException {
		Connection connection = null;
		try {
			connection = ConnectUtil.getInstance().getConnection();
			new AuthorDAO(connection).delete(id);
			new BookAuthorDAO(connection).deleteByAuthorId(id);
			new BookDAO(connection).deleteNoAuthors();
			connection.commit();
		} catch (ClassNotFoundException | SQLException e) {
			connection.rollback();
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	public void createBook(Book book) throws SQLException {
		Connection connection = null;
		try {
			connection = ConnectUtil.getInstance().getConnection();
			book.setId(new BookDAO(connection).createReturnPk(book));
			for (Author author : book.getAuthors()) {
				BookAuthor bookAuthor = new BookAuthor();
				bookAuthor.setBook(book);
				bookAuthor.setAuthor(author);
				new BookAuthorDAO(connection).create(bookAuthor);
			}
			for (Genre genre : book.getGenres()) {
				BookGenre bookGenre = new BookGenre();
				bookGenre.setGenre(genre);
				bookGenre.setBook(book);
				new BookGenreDAO(connection).create(bookGenre);
			}
			connection.commit();
		} catch (ClassNotFoundException | SQLException e) {
			connection.rollback();
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	public List<Book> readBooks() throws SQLException {
		Connection connection = null;
		try {
			connection = ConnectUtil.getInstance().getConnection();
			return new BookDAO(connection).readAll();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
		return new ArrayList<Book>();
	}

	public void updateBook(Book book) throws SQLException {
		Connection connection = null;
		try {
			connection = ConnectUtil.getInstance().getConnection();
			new BookDAO(connection).update(book);
			new BookAuthorDAO(connection).deleteByBookId(book.getId());
			for (Author author : book.getAuthors()) {
				BookAuthor bookAuthor = new BookAuthor();
				bookAuthor.setBook(book);
				bookAuthor.setAuthor(author);
				new BookAuthorDAO(connection).create(bookAuthor);
			}
			new BookGenreDAO(connection).deleteByBookId(book.getId());
			for (Genre genre : book.getGenres()) {
				BookGenre bookGenre = new BookGenre();
				bookGenre.setGenre(genre);
				bookGenre.setBook(book);
				new BookGenreDAO(connection).create(bookGenre);
			}
			connection.commit();
		} catch (ClassNotFoundException | SQLException e) {
			connection.rollback();
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	public void deleteBook(int id) throws SQLException {
		Connection connection = null;
		try {
			connection = ConnectUtil.getInstance().getConnection();
			new BookDAO(connection).delete(id);
			new BookAuthorDAO(connection).deleteByBookId(id);
			new BookGenreDAO(connection).deleteByBookId(id);
			new BookCopyDAO(connection).deleteByBookId(id);
			new BookLoanDAO(connection).deleteByBookId(id);
			connection.commit();
		} catch (ClassNotFoundException | SQLException e) {
			connection.rollback();
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	public void createBorrower(Borrower borrower) throws SQLException {
		Connection connection = null;
		try {
			connection = ConnectUtil.getInstance().getConnection();
			new BorrowerDAO(connection).create(borrower);
			connection.commit();
		} catch (ClassNotFoundException | SQLException e) {
			connection.rollback();
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	public List<Borrower> readBorrowers() throws SQLException {
		Connection connection = null;
		try {
			connection = ConnectUtil.getInstance().getConnection();
			return new BorrowerDAO(connection).readAll();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
		return new ArrayList<Borrower>();
	}

	public void updateBorrower(Borrower borrower) throws SQLException {
		Connection connection = null;
		try {
			connection = ConnectUtil.getInstance().getConnection();
			new BorrowerDAO(connection).update(borrower);
			connection.commit();
		} catch (ClassNotFoundException | SQLException e) {
			connection.rollback();
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	public void deleteBorrower(int id) throws SQLException {
		Connection connection = null;
		try {
			connection = ConnectUtil.getInstance().getConnection();
			new BorrowerDAO(connection).delete(id);
			new BookLoanDAO(connection).deleteByCardNumber(id);
			connection.commit();
		} catch (ClassNotFoundException | SQLException e) {
			connection.rollback();
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	public void createGenre(Genre genre) throws SQLException {
		Connection connection = null;
		try {
			connection = ConnectUtil.getInstance().getConnection();
			new GenreDAO(connection).create(genre);
			connection.commit();
		} catch (ClassNotFoundException | SQLException e) {
			connection.rollback();
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	public List<Genre> readGenres() throws SQLException {
		Connection connection = null;
		try {
			connection = ConnectUtil.getInstance().getConnection();
			return new GenreDAO(connection).readAll();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
		return new ArrayList<Genre>();
	}

	public void updateGenre(Genre genre) throws SQLException {
		Connection connection = null;
		try {
			connection = ConnectUtil.getInstance().getConnection();
			new GenreDAO(connection).update(genre);
			connection.commit();
		} catch (ClassNotFoundException | SQLException e) {
			connection.rollback();
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	public void deleteGenre(int id) throws SQLException {
		Connection connection = null;
		try {
			connection = ConnectUtil.getInstance().getConnection();
			new GenreDAO(connection).delete(id);
			new BookGenreDAO(connection).deleteByGenreId(id);
			connection.commit();
		} catch (ClassNotFoundException | SQLException e) {
			connection.rollback();
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	public void createLibraryBranch(LibraryBranch libraryBranch) throws SQLException {
		Connection connection = null;
		try {
			connection = ConnectUtil.getInstance().getConnection();
			new LibraryBranchDAO(connection).create(libraryBranch);
			connection.commit();
		} catch (ClassNotFoundException | SQLException e) {
			connection.rollback();
			e.printStackTrace();
		} finally {
			connection.close();
		}
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

	public void updateLibraryBranch(LibraryBranch libraryBranch) throws SQLException {
		Connection connection = null;
		try {
			connection = ConnectUtil.getInstance().getConnection();
			new LibraryBranchDAO(connection).update(libraryBranch);
			connection.commit();
		} catch (ClassNotFoundException | SQLException e) {
			connection.rollback();
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	public void deleteLibraryBranch(int id) throws SQLException {
		Connection connection = null;
		try {
			connection = ConnectUtil.getInstance().getConnection();
			new LibraryBranchDAO(connection).delete(id);
			new BookCopyDAO(connection).deleteByLibraryBranchId(id);
			new BookLoanDAO(connection).deleteByLibraryBranchId(id);
			connection.commit();
		} catch (ClassNotFoundException | SQLException e) {
			connection.rollback();
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	public void createPublisher(Publisher publisher) throws SQLException {
		Connection connection = null;
		try {
			connection = ConnectUtil.getInstance().getConnection();
			new PublisherDAO(connection).create(publisher);
			connection.commit();
		} catch (ClassNotFoundException | SQLException e) {
			connection.rollback();
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	public List<Publisher> readPublishers() throws SQLException {
		Connection connection = null;
		try {
			connection = ConnectUtil.getInstance().getConnection();
			return new PublisherDAO(connection).readAll();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
		return new ArrayList<Publisher>();
	}

	public void updatePublisher(Publisher publisher) throws SQLException {
		Connection connection = null;
		try {
			connection = ConnectUtil.getInstance().getConnection();
			new PublisherDAO(connection).update(publisher);
			connection.commit();
		} catch (ClassNotFoundException | SQLException e) {
			connection.rollback();
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

	public void deletePublisher(int id) throws SQLException {
		Connection connection = null;
		try {
			connection = ConnectUtil.getInstance().getConnection();
			new PublisherDAO(connection).delete(id);
			new BookDAO(connection).deleteByPublisherId(id);
			connection.commit();
		} catch (ClassNotFoundException | SQLException e) {
			connection.rollback();
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}
}