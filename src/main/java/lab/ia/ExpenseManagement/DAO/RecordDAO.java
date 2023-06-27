package lab.ia.ExpenseManagement.DAO;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import lab.ia.ExpenseManagement.Models.Record;

public class RecordDAO {
    private final DBContext dbContext;

    public RecordDAO(DBContext dbContext) {
        this.dbContext = dbContext;
    }

    public void updateRecord(Record record) throws SQLException {
        String sql = "UPDATE records SET name = ?, note = ?, type = ?, amount = ?, date = ? WHERE id = ?";
        try (Connection connection = dbContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, record.getName());
            statement.setString(2, record.getNote());
            statement.setString(3, String.valueOf(record.getType()));
            statement.setDouble(4, record.getAmount());
            statement.setDate(5, new java.sql.Date(record.getDate().getTime()));
            statement.setLong(6, record.getId());
            statement.executeUpdate();
        }
    }


}