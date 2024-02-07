package com.example.blogwithsecurity.service;

import com.example.blogwithsecurity.entity.Category;
import com.example.blogwithsecurity.entity.Comment;
import com.example.blogwithsecurity.entity.Post;
import com.example.blogwithsecurity.entity.User;
import com.example.blogwithsecurity.exceptation.ResourceNotFoundException;
import com.example.blogwithsecurity.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{
    @Autowired
    private final DataSource dataSource;
    private Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    @Autowired
    private User user;
    @Autowired
    private Post post;
    @Autowired
    private UserRepository userRepository;

    public CommentServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Comment createComment(Comment comment, Integer userId, Integer postId) {
        if (comment.getContent().isEmpty() ) {
            throw new IllegalArgumentException("Comment content cannot be empty");
        }
        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");
            try (Statement statement = connection.createStatement()) {

                // Get the current date and time
                LocalDateTime now = LocalDateTime.now();

                // Format the current date and time to match the SQL format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDate = now.format(formatter);


                String sql = "INSERT INTO comment( content, addedDate, userId, postId) VALUES ('" + comment.getContent()+ "', '" + formattedDate + "', '" + userId + "', '" + postId +"')";
                statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                comment.setUser(user);
                comment.setPost(post);
                logger.info("Record saved successfully");

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
    public Comment updateComment(Comment comment, Integer commentId) {
        if (comment.getContent().isEmpty() ) {
            throw new IllegalArgumentException("Comment content cannot be empty");
        }
        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");
            try (Statement statement = connection.createStatement()) {

                String sql = "UPDATE comment SET  content = '" + comment.getContent() + "' WHERE commentId = " + commentId;
                int rowsAffected = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                if (rowsAffected == 0) {
                    logger.warn("No comment found with ID: " + commentId);
                    throw new ResourceNotFoundException("Comment", "id", commentId);
                }
                logger.info("Record updated successfully");

            } catch (SQLException e) {
                logger.error("Error executing the SQL query" + e.getMessage());
                throw new SQLException("Error executing the SQL query" + e.getMessage());
            }
        } catch (Exception e) {
            logger.error("Error connecting to the database " + e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteComment(Integer commentId) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            String sql = "DELETE FROM comment WHERE commentId = " + commentId;
            int rowsAffected = statement.executeUpdate(sql);
            if (rowsAffected == 0) {
                logger.warn("No comment found with ID: " + commentId);
            } else {
                logger.info("Comment with ID " + commentId + " deleted successfully");
            }
        } catch (SQLException e) {
            logger.error("Error executing the SQL query: " + e.getMessage());

        } catch (Exception e) {
            logger.error("Error connecting to the database: " + e.getMessage());
        }

    }

    @Override
    public List<Comment> getAllComment() {
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

                        int userId = resultSet.getInt("userId");
                        User user = getUserById(userId); // Implement this method to fetch user by ID
                        comment.setUser(user);

                        int postId = resultSet.getInt("postId");
                        Post post = getPostById(postId); // Implement this method to fetch category by ID
                        comment.setPost(post);
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
    public Comment getCommentById(Integer commentId) {
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

                        int userId = resultSet.getInt("userId");
                        User user = getUserById(userId); // Implement this method to fetch user by ID
                        comment.setUser(user);

                        int postId = resultSet.getInt("postId");
                        Post post= getPostById(postId);
                        comment.setPost(post);
                        return comment;
                    }
                }
                logger.info("Record selected successfully");
            } catch (SQLException e) {
                logger.error("Error executing the SQL query: " + e.getMessage());

            }
        } catch (Exception e) {
            logger.error("Error connecting to the database: " + e.getMessage());
        }
        return  null;
    }

    @Override
    public List<Comment> getCommentByPost(Integer postId) {
        List<Comment> comments = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");

            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT * FROM comment WHERE postId = " + postId;
                try (ResultSet resultSet = statement.executeQuery(sql)) {
                    while (resultSet.next()) {
                        Comment comment = new Comment();
                        comment.setCommentId(resultSet.getInt("commentId"));
                        comment.setContent(resultSet.getString("content"));
                        comment.setAddedDate(resultSet.getDate("addedDate"));

                        int uId = resultSet.getInt("userId");
                        User user = getUserById(uId); // Implement this method to fetch user by ID
                        comment.setUser(user);

                        int postId1 = resultSet.getInt("postId");
                        Post post= getPostById(postId1);
                        comment.setPost(post);
                        comments.add(comment);
                    }
                }
                logger.info("Record selected successfully");
            } catch (SQLException e) {
                logger.error("Error executing the SQL query: " + e.getMessage());

            }
        } catch (Exception e) {
            logger.error("Error connecting to the database: " + e.getMessage());
        }
        return comments;
    }

    @Override
    public List<Comment> getCommentByUser(Integer userId) {
        List<Comment> comments = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");

            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT * FROM comment WHERE userId = " + userId;
                try (ResultSet resultSet = statement.executeQuery(sql)) {
                    while (resultSet.next()) {
                        Comment comment = new Comment();
                        comment.setCommentId(resultSet.getInt("commentId"));
                        comment.setContent(resultSet.getString("content"));
                        comment.setAddedDate(resultSet.getDate("addedDate"));

                        int uId = resultSet.getInt("userId");
                        User user = getUserById(uId); // Implement this method to fetch user by ID
                        comment.setUser(user);

                        int postId = resultSet.getInt("postId");
                        Post post= getPostById(postId);
                        comment.setPost(post);
                        comments.add(comment);
                    }
                }
                logger.info("Record selected successfully");
            } catch (SQLException e) {
                logger.error("Error executing the SQL query: " + e.getMessage());

            }
        } catch (Exception e) {
            logger.error("Error connecting to the database: " + e.getMessage());
        }
        return  comments;
    }

    // Method to get User by ID using Statement
    private User getUserById(int userId) {
        User user = null;
        String sql = "SELECT * FROM user WHERE id = " + userId;
        try (   Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setAbout(resultSet.getString("about"));
                // Populate other user properties as needed
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    // Method to get Post by ID using Statement
    private Post getPostById(int postId) {
        Post post = null;
        String sql = "SELECT * FROM post WHERE id = " + postId;
        try (   Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                post = new Post();
                post.setId(resultSet.getInt("id"));
                post.setTitle(resultSet.getString("title"));
                post.setContent(resultSet.getString("content"));
                post.setAddedDate(resultSet.getDate("addedDate"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }
}
