package com.example.blogwithsecurity.service;

import com.example.blogwithsecurity.entity.Category;
import com.example.blogwithsecurity.entity.Post;
import com.example.blogwithsecurity.entity.User;
import com.example.blogwithsecurity.exceptation.ResourceNotFoundException;
import com.example.blogwithsecurity.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImpl implements PostService{
    @Autowired
    private final DataSource dataSource;
    private Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    @Autowired
    private User user;
    @Autowired
    private Category category;
    @Autowired
    private UserRepository userRepository;

    public PostServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Post createPost(Post post, Integer userId, Integer categoryId) {
        if (post.getTitle().isEmpty() || post.getContent().isEmpty()) {
            throw new IllegalArgumentException("Post title and content cannot be empty");
        }
        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");
            try (Statement statement = connection.createStatement()) {

                // Get the current date and time
                LocalDateTime now = LocalDateTime.now();

                // Format the current date and time to match the SQL format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDate = now.format(formatter);

                String sql = "INSERT INTO post(title, content, addedDate, userId, categoryId) VALUES ('" + post.getTitle()+ "', '" + post.getContent()  + "', '" + formattedDate + "', '" + userId + "', '" + categoryId +"')";
                statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                post.setUser(user);
                post.setCategory(category);
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
    public Post updatePost(Post post, Integer postId) {
        if (post.getTitle().isEmpty() || post.getContent().isEmpty()) {
            throw new IllegalArgumentException("Post title and content cannot be empty");
        }
        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");
            try (Statement statement = connection.createStatement()) {

                String sql = "UPDATE post SET title = '" + post.getTitle() + "', content = '" + post.getContent() + "' WHERE id = " + postId;
                int rowsAffected = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                if (rowsAffected == 0) {
                    logger.warn("No post found with ID: " + postId);
                    throw new ResourceNotFoundException("Post", "id", postId);
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
    public void deletePost(Integer postId) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            String sql = "DELETE FROM post WHERE id = " + postId;
            int rowsAffected = statement.executeUpdate(sql);
            if (rowsAffected == 0) {
                logger.warn("No post found with ID: " + postId);
            } else {
                logger.info("Post with ID " + postId + " deleted successfully");
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
                        blog.setAddedDate(resultSet.getDate("addedDate"));
//                        blog.setUser(user);
//                        blog.setCategory(category);
                        int userId = resultSet.getInt("userId");
                        User user = getUserById(userId); // Implement this method to fetch user by ID
                        blog.setUser(user);

                        int categoryId = resultSet.getInt("categoryId");
                        Category category = getCategoryById(categoryId); // Implement this method to fetch category by ID
                        blog.setCategory(category);
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
                        blog.setAddedDate(resultSet.getDate("addedDate"));

                        int userId = resultSet.getInt("userId");
                        User user = getUserById(userId); // Implement this method to fetch user by ID
                        blog.setUser(user);

                        int categoryId = resultSet.getInt("categoryId");
                        Category category = getCategoryById(categoryId);
                        blog.setCategory(category);
                        return blog;
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
    public List<Post> getPostByCategory(Integer categoryId) {
        List<Post> blogs = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");

            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT * FROM post WHERE categoryId = " + categoryId;
                try (ResultSet resultSet = statement.executeQuery(sql)) {
                    while (resultSet.next()) {
                        Post blog = new Post();
                        blog.setId(resultSet.getInt("id"));
                        blog.setTitle(resultSet.getString("title"));
                        blog.setContent(resultSet.getString("content"));
                        blog.setAddedDate(resultSet.getDate("addedDate"));

                        int userId = resultSet.getInt("userId");
                        User user = getUserById(userId); // Implement this method to fetch user by ID
                        blog.setUser(user);

                        int catId = resultSet.getInt("categoryId");
                        Category category = getCategoryById(catId);
                        blog.setCategory(category);
                        blogs.add(blog);
                    }
                }
                logger.info("Record selected successfully");
            } catch (SQLException e) {
                logger.error("Error executing the SQL query: " + e.getMessage());

            }
        } catch (Exception e) {
            logger.error("Error connecting to the database: " + e.getMessage());
        }
        return blogs;
    }

    @Override
    public List<Post> getPostByUser(Integer userId) {
        List<Post> blogs = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");

            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT * FROM post WHERE userId = " + userId;
                try (ResultSet resultSet = statement.executeQuery(sql)) {
                   while (resultSet.next()) {
                        Post blog = new Post();
                        blog.setId(resultSet.getInt("id"));
                        blog.setTitle(resultSet.getString("title"));
                        blog.setContent(resultSet.getString("content"));
                        blog.setAddedDate(resultSet.getDate("addedDate"));

                        int uId = resultSet.getInt("userId");
                        User user = getUserById(uId); // Implement this method to fetch user by ID
                        blog.setUser(user);

                        int categoryId = resultSet.getInt("categoryId");
                        Category category = getCategoryById(categoryId);
                        blog.setCategory(category);
                        blogs.add(blog);
                    }
                }
                logger.info("Record selected successfully");
            } catch (SQLException e) {
                logger.error("Error executing the SQL query: " + e.getMessage());

            }
        } catch (Exception e) {
            logger.error("Error connecting to the database: " + e.getMessage());
        }
        return  blogs;
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

    // Method to get Category by ID using Statement
    private Category getCategoryById(int categoryId) {
        Category category = null;
        String sql = "SELECT * FROM category WHERE catid = " + categoryId;
        try (   Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                category = new Category();
                category.setCatid(resultSet.getInt("catid"));
                category.setTitle(resultSet.getString("title"));
                category.setDescription(resultSet.getString("description"));
                // Populate other category properties as needed
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }


//
//    public String addUser(User user) {
//    user.setPassword(passwordEncoder.encode(user.getPassword()));
//    userRepository.save(user);
//    return  "user added to system";
//    }
}
