package com.ss.lms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ss.lms.entity.Publisher;

public class PublisherDAO extends BaseDAO<Publisher> {
	public PublisherDAO(Connection connection) {
		super(connection);
	}

	public void create(Publisher publisher) throws ClassNotFoundException, SQLException {
		save("insert into tbl_publisher (publisherName, publisherAddress, publisherPhone) values(?, ?, ?)",
				new Object[] { publisher.getName(), publisher.getAddress(), publisher.getPhone() });
	}

	public void update(Publisher publisher) throws ClassNotFoundException, SQLException {
		save("update tbl_publisher set publisherName = ?, publisherAddress = ?, publisherPhone = ? where publisherId = ?",
				new Object[] { publisher.getName(), publisher.getAddress(), publisher.getPhone(), publisher.getId() });
	}

	public void delete(Integer id) throws ClassNotFoundException, SQLException {
		save("delete from tbl_publisher where publisherId = ?", new Object[] { id });
	}

	public List<Publisher> readAll() throws ClassNotFoundException, SQLException {
		return read("select * from tbl_publisher", null);
	}

	public Publisher readOneFirstLevel(Integer id) throws SQLException {
		PreparedStatement pstmt = connection.prepareStatement("select * from tbl_publisher where publisherId = ?");
		pstmt.setObject(1, id);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			Publisher publisher = new Publisher();
			publisher.setId(rs.getInt("publisherId"));
			publisher.setName(rs.getString("publisherName"));
			publisher.setAddress(rs.getString("publisherAddress"));
			publisher.setAddress(rs.getString("publisherPhone"));
			return publisher;
		}
		return null;
	}

	@Override
	protected List<Publisher> extractData(ResultSet rs) throws ClassNotFoundException, SQLException {
		List<Publisher> publishers = new ArrayList<>();
		BookDAO bookDao = new BookDAO(connection);
		while (rs.next()) {
			Publisher publisher = new Publisher();
			publisher.setId(rs.getInt("publisherId"));
			publisher.setName(rs.getString("publisherName"));
			publisher.setAddress(rs.getString("publisherAddress"));
			publisher.setPhone(rs.getString("publisherPhone"));
			publisher.setBooks(bookDao.readFirstLevel("select * from tbl_book where tbl_book.pubId = ?",
					new Object[] { rs.getInt("publisherId") }));
			publishers.add(publisher);
		}
		return publishers;
	}

	@Override
	protected List<Publisher> extractDataFirstLevel(ResultSet rs) throws SQLException {
		List<Publisher> publishers = new ArrayList<>();
		while (rs.next()) {
			Publisher publisher = new Publisher();
			publisher.setId(rs.getInt("publisherId"));
			publisher.setName(rs.getString("publisherName"));
			publisher.setAddress(rs.getString("publisherAddress"));
			publisher.setAddress(rs.getString("publisherPhone"));
			publishers.add(publisher);
		}
		return publishers;
	}
}