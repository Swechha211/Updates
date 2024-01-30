package com.example.blogjdbcnew.repositories;

import com.example.blogjdbcnew.entities.Post;
import com.example.blogjdbcnew.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PostRepoImpl implements PostRepo{

    @Autowired
    private DataSource dataSource;

    @Autowired
    private User user;

    @Override
    public List<Post> findAll() {
        List<Post> blogs = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM post")) {

            while (resultSet.next()) {
               Post blog = new Post();
                blog.setId(resultSet.getInt("id"));
                blog.setTitle(resultSet.getString("title"));
                blog.setContent(resultSet.getString("content"));
                blogs.add(blog);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return blogs;
    }

    @Override
    public Post findById(Integer id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM post WHERE id = ?")) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Post blog = new Post();
                    blog.setId(resultSet.getInt("id"));
                    blog.setTitle(resultSet.getString("title"));
                    blog.setContent(resultSet.getString("content"));
                    return blog;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(Post blog) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO post(title, content) VALUES (?, ?)")) {

            statement.setString(1, blog.getTitle());
            statement.setString(2, blog.getContent());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update(Post blog) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE post SET title = ?, content = ? WHERE id = ?")) {

            statement.setString(1, blog.getTitle());
            statement.setString(2, blog.getContent());
            statement.setInt(3, blog.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void delete(Integer id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM post WHERE id = ?")) {

            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Post findByUserId(Integer userId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM post WHERE id = ?")) {

            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Post blog = new Post();
                    blog.setId(resultSet.getInt("id"));
                    blog.setTitle(resultSet.getString("title"));
                    blog.setContent(resultSet.getString("content"));
                    blog.setUserId(userId);
                    return blog;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
