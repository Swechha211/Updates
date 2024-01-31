package com.example.blogjdbcnew.repositories;

import com.example.blogjdbcnew.entities.Comment;
import com.example.blogjdbcnew.entities.Post;
import com.example.blogjdbcnew.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PostRepoImpl implements PostRepo{

    @Autowired
    private final DataSource dataSource;
    private Logger logger = LoggerFactory.getLogger(PostRepoImpl.class);

    @Autowired
    private User user;

    public PostRepoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Post> findAll() {
        List<Post> blogs = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();)
        {
            logger.info("Connectes to mysql database");
            try(Statement statement = connection.createStatement()){
                String sql = "SELECT * FROM post";
                try(ResultSet resultSet = statement.executeQuery(sql)) {
                    while (resultSet.next()) {
                        Post blog = new Post();
                        blog.setId(resultSet.getInt("id"));
                        blog.setTitle(resultSet.getString("title"));
                        blog.setContent(resultSet.getString("content"));
                        blogs.add(blog);
                    }

                }
            } catch (SQLException e){
                logger.error("Error in sql querry" + e.getMessage());
            }
        } catch (Exception e) {
            logger.error("Error while connecting to the database" + e.getMessage());
        }
        return blogs;
    }

    @Override
    public Post findById(Integer id) {
        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");

            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT * FROM post WHERE id = " + id;
                try (ResultSet resultSet = statement.executeQuery(sql)) {
                    if (resultSet.next()) {
                    Post blog = new Post();
                    blog.setId(resultSet.getInt("id"));
                    blog.setTitle(resultSet.getString("title"));
                    blog.setContent(resultSet.getString("content"));
                    return blog;
                }
                }
                logger.info("Record selected successfully");
            } catch (SQLException e) {
                logger.error("Error executing the SQL query: " + e.getMessage());
                throw new SQLException("Error executing the SQL query: " + e.getMessage());
            }
        } catch (Exception e) {
            logger.error("Error connecting to the database: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void save(Post blog) {

        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");
            try (Statement statement = connection.createStatement()) {

                String sql = "INSERT INTO post(title, content) VALUES ('" + blog.getTitle()+ "', '" + blog.getContent()  + "')";
                statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                logger.info("Record saved successfully");
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        blog.setId(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Creating record failed, no ID obtained.");
                    }
                }
            } catch (SQLException e) {
                logger.error("Error executing the SQL query" + e.getMessage());
                throw new SQLException("Error executing the SQL query" + e.getMessage());
            }
        } catch (Exception e) {
            logger.error("Error connecting to the database" + e.getMessage());
        }
    }

    @Override
    public void update(Post blog) {

        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");
            try (Statement statement = connection.createStatement()) {

                String sql = "UPDATE post SET title = '" + blog.getTitle() + "', content = '" + blog.getContent() + "' WHERE id = " + blog.getId();
                statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                logger.info("Record updated successfully");
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        blog.setId(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Updating record failed, no ID obtained.");
                    }
                }
            } catch (SQLException e) {
                logger.error("Error executing the SQL query" + e.getMessage());
                throw new SQLException("Error executing the SQL query" + e.getMessage());
            }
        } catch (Exception e) {
            logger.error("Error connecting to the database" + e.getMessage());
        }


    }

    @Override
    public void delete(Integer id) {

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            String sql = "DELETE FROM post WHERE id = " + id;
            int rowsAffected = statement.executeUpdate(sql);
            if (rowsAffected == 0) {
                logger.warn("No user found with ID: " + id);
            } else {
                logger.info("User with ID " + id + " deleted successfully");
            }
        } catch (SQLException e) {
            logger.error("Error executing the SQL query: " + e.getMessage());

        } catch (Exception e) {
            logger.error("Error connecting to the database: " + e.getMessage());
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
