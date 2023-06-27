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
        String name = record.getName();
        String note = record.getNote();
        String type = String.valueOf(record.getType());
        String amount = String.valueOf(record.getAmount());
        String date = String.valueOf(new java.sql.Date(record.getDate().getTime()));
        String id = String.valueOf(record.getId());
        String sql = "UPDATE records SET name = " + name + ", note = " + note + ", type = "+type+", amount = "+amount+", date = "+date+" WHERE id = "+id+"";
        try (Connection connection = dbContext.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        }
    }


}