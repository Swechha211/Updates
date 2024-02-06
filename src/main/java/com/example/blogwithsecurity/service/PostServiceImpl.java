package com.example.blogwithsecurity.service;

import com.example.blogwithsecurity.entity.Post;
import com.example.blogwithsecurity.entity.User;
import com.example.blogwithsecurity.exceptation.ResourceNotFoundExceptation;
import com.example.blogwithsecurity.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PostServiceImpl implements PostService{
    @Autowired
    private final DataSource dataSource;
    private Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    @Autowired
    private User user;
    @Autowired
    private UserRepository userRepository;

    public PostServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Post createPost(Post post, Integer userId, Integer categoryId) {
        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");
            try (Statement statement = connection.createStatement()) {

                // Get the current date and time
                LocalDateTime now = LocalDateTime.now();

                // Format the current date and time to match the SQL format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDate = now.format(formatter);

                String sql = "INSERT INTO post(title, content, addedDate) VALUES ('" + post.getTitle()+ "', '" + post.getContent()  + "', '" + formattedDate + "')";
                statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                logger.info("Record saved successfully");
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        post.setId(generatedKeys.getInt(1));
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
        return null;
    }

    @Override
    public Post updatePost(Post post, Integer postId) {
        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");
            try (Statement statement = connection.createStatement()) {

                String sql = "UPDATE post SET title = '" + post.getTitle() + "', content = '" + post.getContent() + "' WHERE id = " + postId;
                statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                logger.info("Record updated successfully");
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        post.setId(generatedKeys.getInt(1));
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
        return null;
    }

    @Override
    public void deletePost(Integer postId) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            String sql = "DELETE FROM post WHERE id = " + postId;
            int rowsAffected = statement.executeUpdate(sql);
            if (rowsAffected == 0) {
                logger.warn("No user found with ID: " + postId);
            } else {
                logger.info("User with ID " + postId + " deleted successfully");
            }
        } catch (SQLException e) {
            logger.error("Error executing the SQL query: " + e.getMessage());

        } catch (Exception e) {
            logger.error("Error connecting to the database: " + e.getMessage());
        }

    }

    @Override
    public List<Post> getAllPost() {
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
    public Post getPostById(Integer postId) {
        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");

            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT * FROM post WHERE id = " + postId;
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
    public List<Post> getPostByCategory(Integer categoryId) {
        return null;
    }

    @Override
    public List<Post> getPostByUser(Integer userId) {
        return null;
    }
}
