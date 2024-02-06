package com.example.blogwithsecurity.service;

import com.example.blogwithsecurity.entity.User;
import com.example.blogwithsecurity.exceptation.ResourceNotFoundExceptation;

import com.example.blogwithsecurity.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{


    @Autowired
    private final  DataSource dataSource;
    @Autowired
    private UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public User create(User user) {
        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");
            try (Statement statement = connection.createStatement()) {

                String sql = "INSERT INTO user(name, email,password, about) VALUES ('" + user.getName() + "', '" + user.getEmail() + "', '" + user.getPassword() + "', '" + user.getAbout()   + "')";
                statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                logger.info("Record saved successfully");
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getInt(1));
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
        return user;
    }

    @Override
    public User update(User user, Integer userId) {
        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");
            try (Statement statement = connection.createStatement()) {
                 user =this.userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundExceptation("User", "id", userId));

                String sql = "UPDATE user SET name = '" + user.getName() + "', email = '" + user.getEmail() + "', password = '" + user.getPassword() + "', about = '" + user.getAbout() + "' WHERE id = " + user.getId();
                statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                logger.info("Record updated successfully");
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getInt(1));
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
    public User findById(Integer userId) {
        User user = null;

        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");
            try (Statement statement = connection.createStatement()) {

                String sql = "SELECT * FROM user WHERE id = " + userId;

                try (ResultSet resultSet = statement.executeQuery(sql)) {
                    if (resultSet.next()) {
                        user = new User();
                        user.setId(resultSet.getInt("id"));
                        user.setName(resultSet.getString("name"));
                        user.setEmail(resultSet.getString("email"));
                        user.setPassword(resultSet.getString("password"));
                        user.setAbout(resultSet.getString("about"));
                    } else {
                        throw new ResourceNotFoundExceptation("User", "id", userId);
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
        return user;
    }

    @Override
    public List<User> findAll() {
        List<User> userList = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");

            try (Statement statement = connection.createStatement()) {

                String sql = "SELECT * FROM user";
                try (ResultSet resultSet = statement.executeQuery(sql)) {
                    while (resultSet.next()) {
                        User user = new User();
                        user.setId(resultSet.getInt("id"));
                        user.setName(resultSet.getString("name"));
                        user.setEmail(resultSet.getString("email"));
                        user.setPassword(resultSet.getString("password"));
                        user.setAbout(resultSet.getString("about"));
                        // Populate other fields as needed
                        userList.add(user);
                    }
                }
                logger.info("Records selected successfully");
            } catch (SQLException e) {
                logger.error("Error executing the SQL query: " + e.getMessage());
                throw new SQLException("Error executing the SQL query: " + e.getMessage());
            }
        } catch (Exception e) {
            logger.error("Error connecting to the database: " + e.getMessage());
        }

        return userList;
    }

    @Override
    public void delete(Integer userId) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            String sql = "DELETE FROM user WHERE id = " + userId;
            int rowsAffected = statement.executeUpdate(sql);
            if (rowsAffected == 0) {
                logger.warn("No user found with ID: " + userId);

            } else {
                logger.info("User with ID " + userId + " deleted successfully");
            }
        } catch (SQLException e) {
            logger.error("Error executing the SQL query: " + e.getMessage());

        } catch (Exception e) {
            logger.error("Error connecting to the database: " + e.getMessage());
        }

    }


}