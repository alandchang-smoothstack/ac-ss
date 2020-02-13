package com.ss.lms.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ss.lms.dao.BookCopyDAO;
import com.ss.lms.dao.BookDAO;
import com.ss.lms.dao.LibraryBranchDAO;
import com.ss.lms.entity.Book;
import com.ss.lms.entity.BookCopy;
import com.ss.lms.entity.LibraryBranch;
import com.ss.lms.util.ConnectUtil;

public class LibrarianService {
	private static LibrarianService instance;

	public static LibrarianService getInstance() {
		if (instance == null) {
			synchronized (AdministratorService.class) {
				if (instance == null) {
					instance = new LibrarianService();
				}
			}
		}
		return instance;
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

	public void createBookCopy(BookCopy bookCopy) throws SQLException {
		Connection connection = null;
		try {
			connection = ConnectUtil.getInstance().getConnection();
			new BookCopyDAO(connection).create(bookCopy);
			connection.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			connection.rollback();
		} finally {
			connection.close();
		}
	}

	public BookCopy readBookCopy(Integer bookId, Integer libraryBranchId) throws SQLException {
		Connection connection = null;
		try {
			connection = ConnectUtil.getInstance().getConnection();
			return new BookCopyDAO(connection).readOne(bookId, libraryBranchId);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
		return null;
	}

	public void updateBookCopy(BookCopy bookCopy) throws SQLException {
		Connection connection = null;
		try {
			connection = ConnectUtil.getInstance().getConnection();
			new BookCopyDAO(connection).update(bookCopy);
			connection.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			connection.rollback();
		} finally {
			connection.close();
		}
	}

	public LibraryBranch readLibraryBranch(Integer id) throws SQLException {
		Connection connection = null;
		try {
			connection = ConnectUtil.getInstance().getConnection();
			return new LibraryBranchDAO(connection).readOne(id);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
		return null;
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
			e.printStackTrace();
			connection.rollback();
		} finally {
			connection.close();
		}
	}
}
