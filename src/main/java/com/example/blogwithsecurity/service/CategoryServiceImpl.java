package com.example.blogwithsecurity.service;

import com.example.blogwithsecurity.entity.Category;
import com.example.blogwithsecurity.exceptation.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CategoryServiceImpl implements CategoryService{
    @Autowired
    private final DataSource dataSource;
    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    public CategoryServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Category createCategory(Category category) {
        if (category.getTitle().isEmpty() || category.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Category title and description cannot be empty");
        }
        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");
            try (Statement statement = connection.createStatement()) {

                String sql = "INSERT INTO category(title, description) VALUES ('" + category.getTitle() + "', '" + category.getDescription()  +"')";
                statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                logger.info("Record saved successfully");
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        category.setCatid(generatedKeys.getInt(1));
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
        return category;

    }

    @Override
    public Category updateCategory(Category category, Integer categoryId) {
        if (category.getTitle().isEmpty() || category.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Category title and description cannot be empty");
        }
        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");
            try (Statement statement = connection.createStatement()) {

                String sql = "UPDATE category SET title = '" + category.getTitle() + "', description = '" + category.getDescription() + "' WHERE catid = " + categoryId;
                int rowsAffected = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                if (rowsAffected == 0) {
                    throw new ResourceNotFoundException("Category", "id", categoryId);
                }
                logger.info("Record updated successfully");

            } catch (SQLException e) {
                logger.error("Error executing the SQL query" + e.getMessage());
                throw new SQLException("Error executing the SQL query" + e.getMessage());
            }
        } catch (Exception e) {
            logger.error("Error connecting to the database " + e.getMessage());
        }
        return category;
    }

    @Override
    public void deleteCategory(Integer categoryId) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            String sql = "DELETE FROM category WHERE catid = " + categoryId;
            int rowsAffected = statement.executeUpdate(sql);
            if (rowsAffected == 0) {
                logger.warn("No user found with ID: " + categoryId);
            } else {
                logger.info("Category with ID " + categoryId + " deleted successfully");
            }
        } catch (SQLException e) {
            logger.error("Error executing the SQL query: " + e.getMessage());

        } catch (Exception e) {
            logger.error("Error connecting to the database: " + e.getMessage());
        }

    }

    @Override
    public Category getCategory(Integer categoryId) {
        Category category = new Category();
        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");

            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT * FROM category WHERE catid = " + categoryId;
                try (ResultSet resultSet = statement.executeQuery(sql)) {
                    if (resultSet.next()) {
                        category.setCatid(resultSet.getInt("catid"));
                        category.setTitle(resultSet.getString("title"));
                        category.setDescription(resultSet.getString("description"));
                        return  category;
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
        return category;
    }

    @Override
    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();)
        {
            logger.info("Connectes to mysql database");
            try(Statement statement = connection.createStatement()){
                String sql = "SELECT * FROM category";
                try(ResultSet resultSet = statement.executeQuery(sql)) {
                    while (resultSet.next()) {
                        Category category = new Category();
                        category .setCatid(resultSet.getInt("catid"));
                        category .setTitle(resultSet.getString("title"));
                        category .setDescription(resultSet.getString("description"));
                        categories .add(category );
                    }

                }
            } catch (SQLException e){
                logger.error("Error in sql querry" + e.getMessage());
            }
        } catch (Exception e) {
            logger.error("Error while connecting to the database" + e.getMessage());
        }
        return categories;
    }
}
