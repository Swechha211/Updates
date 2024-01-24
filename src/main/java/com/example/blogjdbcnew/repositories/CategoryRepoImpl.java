package com.example.blogjdbcnew.repositories;

import com.example.blogjdbcnew.entities.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CategoryRepoImpl implements CategoryRepo{

    @Autowired
    private DataSource dataSource;
    @Override
    public List<Category> findAll() {
        List<Category> categories = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM category")) {

            while (resultSet.next()) {
                Category category = new Category();
                category .setCatid(resultSet.getInt("catid"));
                category .setTitle(resultSet.getString("title"));
                category .setDescription(resultSet.getString("description"));
                categories .add(category );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    @Override
    public Category findById(Integer catid) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM category WHERE catid = ?")) {

            statement.setInt(1, catid);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Category category = new Category();
                    category.setCatid(resultSet.getInt("catid"));
                    category.setTitle(resultSet.getString("title"));
                    category.setDescription(resultSet.getString("description"));
                    return  category;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(Category category) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO category(title, description) VALUES (?, ?)")) {

            statement.setString(1, category.getTitle());
            statement.setString(2, category.getDescription());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update(Category category) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE category SET title = ?, description = ? WHERE catid = ?")) {

            statement.setString(1, category.getTitle());
            statement.setString(2, category.getDescription());
            statement.setInt(3, category.getCatid());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void delete(Integer catid) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM category WHERE catid = ?")) {

            statement.setInt(1, catid);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
