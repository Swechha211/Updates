package com.example.blogjdbcnew.repositories;

import com.example.blogjdbcnew.entities.Category;
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
import java.sql.Date;

@Repository
public class CommentRepoImpl implements CommentRepo{

    @Autowired
    private final DataSource dataSource;
    private Logger logger = LoggerFactory.getLogger(CommentRepoImpl.class);

    @Autowired
    private User user;

    public CommentRepoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Comment> findAll() {
        List<Comment> comments = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();)
        {
            logger.info("Connectes to mysql database");
            try(Statement statement = connection.createStatement()){
                String sql = "SELECT * FROM comment";
                try(ResultSet resultSet = statement.executeQuery(sql)) {
                    while (resultSet.next()) {
                Comment comment = new Comment();
                comment.setCommentId(resultSet.getInt("commentId"));
                comment.setContent(resultSet.getString("content"));
                comment.setAddedDate(resultSet.getDate("addedDate"));
                int userId = user.getId();

                comment.setUserId(userId);

                comments.add(comment);
            }

                }
            } catch (SQLException e){
                logger.error("Error in sql querry" + e.getMessage());
            }
        } catch (Exception e) {
            logger.error("Error while connecting to the database" + e.getMessage());
        }
        return comments;
    }

    @Override
    public Comment findById(Integer commentId) {

        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");

            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT * FROM comment WHERE commentId = " + commentId;
                try (ResultSet resultSet = statement.executeQuery(sql)) {
                    if (resultSet.next()) {
                        Comment comment = new Comment();
                        comment.setCommentId(resultSet.getInt("commentId"));
                        comment.setContent(resultSet.getString("content"));
                        comment.setAddedDate(resultSet.getDate("addedDate"));
                        int userId = user.getId();
                        comment.setUserId(userId);
                        return comment;
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
    public void save(Comment comment) {

        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");
            try (Statement statement = connection.createStatement()) {

                String sql = "INSERT INTO comment(content, addedDate) VALUES ('" + comment.getContent()+ "', '" + comment.getAddedDate()  + "')";
                statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                logger.info("Record saved successfully");
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        comment.setCommentId(generatedKeys.getInt(1));
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
    public void update(Comment comment) {

        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");
            try (Statement statement = connection.createStatement()) {

                String sql = "UPDATE comment SET content = '" + comment.getContent() + "', addedDate = '" + comment.getAddedDate() + "' WHERE commentId = " + comment.getCommentId();
                statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                logger.info("Record updated successfully");
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        comment.setCommentId(generatedKeys.getInt(1));
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
    public void delete(Integer commentId) {

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            String sql = "DELETE FROM comment WHERE commentId = " + commentId;
            int rowsAffected = statement.executeUpdate(sql);
            if (rowsAffected == 0) {
                logger.warn("No user found with ID: " + commentId);
            } else {
                logger.info("User with ID " + commentId + " deleted successfully");
            }
        } catch (SQLException e) {
            logger.error("Error executing the SQL query: " + e.getMessage());

        } catch (Exception e) {
            logger.error("Error connecting to the database: " + e.getMessage());
        }


    }

    @Override
    public List<Comment> findByPost(Integer id) {
        return null;
    }

    @Override
    public List<Comment> findByUser(Integer id) {
        List<Comment> comments = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM comment WHERE userId = ?")) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Comment comment = new Comment();
                    User user = new User();
                    comment.setCommentId(resultSet.getInt("commentId"));
                    comment.setContent(resultSet.getString("content"));
                    comment.setAddedDate(resultSet.getDate("addedDate"));
                    int userId = user.getId();
                    comment.setUserId(userId);
                    return comments;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Comment findByUserId(Integer userId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM comment WHERE user_id = ?")) {

            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Comment comment = new Comment();
                    comment.setCommentId(resultSet.getInt("commentId"));
                    comment.setContent(resultSet.getString("content"));
                    comment.setAddedDate(resultSet.getDate("addedDate"));
                    comment.setUserId(userId);
                    return comment;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    }

