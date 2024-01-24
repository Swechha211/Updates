package com.example.blogjdbcnew.repositories;

import com.example.blogjdbcnew.entities.Comment;
import com.example.blogjdbcnew.entities.Post;
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
    private DataSource dataSource;
    @Override
    public List<Comment> findAll() {
        List<Comment> comments = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM comment")) {

            while (resultSet.next()) {
                Comment comment = new Comment();
                comment.setCommentId(resultSet.getInt("commentId"));
                comment.setContent(resultSet.getString("content"));
                comment.setAddedDate(resultSet.getDate("addedDate"));

                comments.add(comment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }

    @Override
    public Comment findById(Integer commentId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM comment WHERE commentId = ?")) {

            statement.setInt(1, commentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Comment comment = new Comment();
                    comment.setCommentId(resultSet.getInt("commentId"));
                    comment.setContent(resultSet.getString("content"));
                    comment.setAddedDate(resultSet.getDate("addedDate"));
                    return comment;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(Comment comment) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO comment(content, addedDate) VALUES (?, ?)")) {

            statement.setString(1, comment.getContent());
            statement.setDate(2, comment.getAddedDate());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update(Comment comment) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE comment SET content = ?, addedDate = ? WHERE commentId = ?")) {

            statement.setString(1, comment.getContent());
            statement.setDate(2, comment.getAddedDate());
            statement.setInt(3, comment.getCommentId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void delete(Integer commentId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM comment WHERE commentId = ?")) {

            statement.setInt(1, commentId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
