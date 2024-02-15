package com.example.blogwithsecurity.service;

import com.example.blogwithsecurity.entity.Role;
import com.example.blogwithsecurity.entity.User;
import com.example.blogwithsecurity.exceptation.ResourceNotFoundException;

import com.example.blogwithsecurity.exceptation.UserNotFoundException;
import com.example.blogwithsecurity.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Service
public class UserServiceImpl implements UserService{


    @Autowired
    private final  DataSource dataSource;

    @Autowired
    private  User user;
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public User create(User user) {
        if (user.getName().isEmpty() || user.getEmail().isEmpty() || user.getPassword().isEmpty() || user.getAbout().isEmpty()) {
            throw new IllegalArgumentException("User's name, email, password and about cannot be empty");
        }
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
        if (user.getName().isEmpty() || user.getEmail().isEmpty() || user.getPassword().isEmpty() || user.getAbout().isEmpty()) {
            throw new IllegalArgumentException("User's name, email, password and about cannot be empty");
        }
        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");
            try (Statement statement = connection.createStatement()) {

                String sql = "UPDATE user SET name = '" + user.getName() + "', email = '" + user.getEmail() + "', password = '" + user.getPassword() + "', about = '" + user.getAbout() + "' WHERE id = " + userId;
                int rowsAffected = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                if (rowsAffected == 0) {
                    throw new ResourceNotFoundException("User", "id", userId);
                }
                logger.info("Record updated successfully");

            } catch (SQLException e) {
                logger.error("Error executing the SQL query" + e.getMessage());

            }
        } catch (Exception e) {
            logger.error("Error connecting to the database " + e.getMessage());
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
                        throw new ResourceNotFoundException("User", "id", userId);
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


    @Override
    public Optional<User>  findByName(String username) {
        User user = null;

        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");

            // Use a prepared statement to safely inject the username and password values
            String sql = "SELECT * FROM user WHERE name = ? ";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username); // Set the username as a parameter

                try (ResultSet resultSet = statement.executeQuery()) {

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
        return Optional.empty();
    }



    @Override
    public User findByNameandPassword(String username, String password) {
        User user = null;

        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");

            // Use a prepared statement to safely inject the username and password values
            String sql = "SELECT * FROM user WHERE name = ? AND password = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username); // Set the username as a parameter
                statement.setString(2, password); // Set the password as a parameter


                try (ResultSet resultSet = statement.executeQuery()) {

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
    public Optional<User> findByEmail(String email) {
//        User user = null;
//
//        try (Connection connection = dataSource.getConnection()) {
//            logger.info("Connected to the database");
//
//            String sql = "SELECT * FROM user WHERE email = ? ";
//            try (PreparedStatement statement = connection.prepareStatement(sql)) {
//                statement.setString(1, email); // Set the email as a parameter
//
//                try (ResultSet resultSet = statement.executeQuery()) {
//                    if (resultSet.next()) {
//                        user = new User();
//                        user.setId(resultSet.getInt("id"));
//                        user.setName(resultSet.getString("name"));
//                        user.setEmail(resultSet.getString("email"));
//                        user.setPassword(resultSet.getString("password"));
//                        user.setAbout(resultSet.getString("about"));
//                    }
//                }
//                logger.info("Record selected successfully");
//            } catch (SQLException e) {
//                logger.error("Error executing the SQL query: " + e.getMessage());
//                throw new SQLException("Error executing the SQL query: " + e.getMessage());
//            }
//
//            if (user != null) {
//                // Query to fetch user roles
//                String roleQuery = "SELECT r.name FROM user_role ur JOIN role r ON ur.role_id = r.id WHERE ur.user_id = ?";
//                try (PreparedStatement roleStatement = connection.prepareStatement(roleQuery)) {
//                    roleStatement.setInt(1, user.getId());
//                    try (ResultSet roleResultSet = roleStatement.executeQuery()) {
//                        while (roleResultSet.next()) {
//                            user.addRole(roleResultSet.getString("name"));
//                        }
//                    }
//                } catch (SQLException e) {
//                    logger.error("Error executing the role SQL query: " + e.getMessage());
//                    throw new SQLException("Error executing the role SQL query: " + e.getMessage());
//                }
//            }
//
//        } catch (SQLException e) {
//            logger.error("Error connecting to the database: " + e.getMessage());
//            throw new RuntimeException("Error connecting to the database: " + e.getMessage());
//        }


        User user = null;

        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");

            String sql = "SELECT * FROM user WHERE email = ? ";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, email); // Set the email as a parameter

                try (ResultSet resultSet = statement.executeQuery()) {
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

            if (user != null) {
                // Query to fetch user roles
                String roleQuery = "SELECT r.id, r.name FROM user_role ur JOIN role r ON ur.role_id = r.id WHERE ur.user_id = ?";
                try (PreparedStatement roleStatement = connection.prepareStatement(roleQuery)) {
                    roleStatement.setInt(1, user.getId());
                    try (ResultSet roleResultSet = roleStatement.executeQuery()) {
                        Set<Role> roles = new HashSet<>();
                        while (roleResultSet.next()) {
                            Role role = new Role();
                            role.setId(roleResultSet.getInt("id"));
                            role.setName(roleResultSet.getString("name"));
                            roles.add(role);
                        }
                        user.setRoles(roles);
                    }
                } catch (SQLException e) {
                    logger.error("Error executing the role SQL query: " + e.getMessage());
                    throw new SQLException("Error executing the role SQL query: " + e.getMessage());
                }
            }

        } catch (SQLException e) {
            logger.error("Error connecting to the database: " + e.getMessage());
            throw new RuntimeException("Error connecting to the database: " + e.getMessage());
        }

        return Optional.ofNullable(user);
    }






}
