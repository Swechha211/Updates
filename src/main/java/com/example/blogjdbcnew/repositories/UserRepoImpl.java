package com.example.blogjdbcnew.repositories;


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
public class UserRepoImpl implements UserRepo{

    @Autowired
    private final DataSource dataSource;
    private final Logger logger = LoggerFactory.getLogger(UserRepoImpl.class);

    public UserRepoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void create(User user) {
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
    }

    @Override
    public void update(User user) {

        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");
            try (Statement statement = connection.createStatement()) {

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

    }

    @Override
    public User findById(Integer id) {

        User user = null;

        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");

            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT * FROM user WHERE id = " + id;
                try (ResultSet resultSet = statement.executeQuery(sql)) {
                    if (resultSet.next()) {
                        user = new User();
                        user.setId(resultSet.getInt("id"));
                        user.setName(resultSet.getString("name"));
                        user.setEmail(resultSet.getString("email"));
                        user.setPassword(resultSet.getString("password"));
                        user.setAbout(resultSet.getString("about"));
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
    public void delete(Integer id) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            String sql = "DELETE FROM user WHERE id = " + id;
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
}
