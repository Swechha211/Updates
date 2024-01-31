package com.example.blogjdbcnew.repositories;

import com.example.blogjdbcnew.entities.Category;
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
public class CategoryRepoImpl implements CategoryRepo{

    @Autowired
    private final DataSource dataSource;
    private Logger logger = LoggerFactory.getLogger(CategoryRepoImpl.class);

    public CategoryRepoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Category> findAll() {
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

    @Override
    public Category findById(Integer catid) {
        Category category = new Category();
        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");

            try (Statement statement = connection.createStatement()) {
                String sql = "SELECT * FROM category WHERE catid = " + catid;
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
    public void save(Category category) {

        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");
            try (Statement statement = connection.createStatement()) {

                String sql = "INSERT INTO category(title, description) VALUES ('" + category.getTitle() + "', '" + category.getDescription()  + "')";
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

    }

    @Override
    public void update(Category category) {

        try (Connection connection = dataSource.getConnection()) {
            logger.info("Connected to the database");
            try (Statement statement = connection.createStatement()) {

                String sql = "UPDATE category SET title = '" + category.getTitle() + "', description = '" + category.getDescription() + "' WHERE catid = " + category.getCatid();
                statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                logger.info("Record updated successfully");
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        category.setCatid(generatedKeys.getInt(1));
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
    public void delete(Integer catid) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            String sql = "DELETE FROM category WHERE catid = " + catid;
            int rowsAffected = statement.executeUpdate(sql);
            if (rowsAffected == 0) {
                logger.warn("No user found with ID: " + catid);
            } else {
                logger.info("User with ID " + catid + " deleted successfully");
            }
        } catch (SQLException e) {
            logger.error("Error executing the SQL query: " + e.getMessage());

        } catch (Exception e) {
            logger.error("Error connecting to the database: " + e.getMessage());
        }

    }
}
