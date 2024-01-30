package com.example.blogjdbcnew.repositories;



import com.example.blogjdbcnew.entities.User;


import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.util.logging.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@Repository
public class RecordingRepoImpl implements RecordingRepository {


    public final DataSource dataSource;
    private final Logger logger = LoggerFactory.getLogger(RecordingRepoImpl.class);


    public RecordingRepoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }


//    @Override
//    public RecordEntity addRecord(RecordEntity recordEntity) {
//        try (Connection connection = dataSource.getConnection()) {
//            logger.info("Connected to the database");
//            try (Statement statement = connection.createStatement()) {
//                String sql = "INSERT INTO record (title, start, end, url,status) VALUES ('" + recordEntity.getTitle() + "', '" + recordEntity.getStart() + "', '" + recordEntity.getEnd() + "', '" + recordEntity.getUrl() + "','" + recordEntity.getStatus() + "')";
//                statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
//                logger.info("Record saved successfully");
//                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
//                    if (generatedKeys.next()) {
//                        recordEntity.setId(generatedKeys.getInt(1));
//                    } else {
//                        throw new SQLException("Creating record failed, no ID obtained.");
//                    }
//                }
//            } catch (SQLException e) {
//                logger.error("Error executing the SQL query" + e.getMessage());
//                throw new SQLException("Error executing the SQL query" + e.getMessage());
//            }
//        } catch (Exception e) {
//            logger.error("Error connecting to the database" + e.getMessage());
//        }
//        return recordEntity;
//    }

    @Override
    public User addRecord(User user) {
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
}
