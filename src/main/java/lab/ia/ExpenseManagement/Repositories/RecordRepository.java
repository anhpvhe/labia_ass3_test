package lab.ia.ExpenseManagement.Repositories;


import lab.ia.ExpenseManagement.Models.Enums.ERecordType;
import lab.ia.ExpenseManagement.Models.Record;
import lab.ia.ExpenseManagement.Models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.Date;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class RecordRepository {

    private final Connection connection;

    public RecordRepository(Connection connection) {
        this.connection = connection;
    }

    public List<Record> findAllRecords(User user) throws SQLException {
        String query = "SELECT * FROM records WHERE user_id = ?";
        List<Record> records = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, user.getId());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    String name = resultSet.getString("name");
                    String note = resultSet.getString("note");
                    String type = resultSet.getString("type");
                    double amount = resultSet.getDouble("amount");
                    Date date = resultSet.getTimestamp("date");
                    long userId = resultSet.getLong("user_id");
                    // Retrieve other fields

                    Record record = new Record();
                    record.setId(id);
                    record.setName(name);
                    record.setNote(note);
                    record.setType(ERecordType.valueOf(type));
                    record.setAmount(amount);
                    record.setDate(date);
                    record.setUser(user);
                    // Set other field values

                    records.add(record);
                }
            }
        }

        return records;
    }

    public Record findRecordById(User user, Long id) throws SQLException {
        String query = "SELECT * FROM records WHERE id = ? AND user_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.setLong(2, user.getId());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String note = resultSet.getString("note");
                    String type = resultSet.getString("type");
                    double amount = resultSet.getDouble("amount");
                    Date date = resultSet.getTimestamp("date");
                    long userId = resultSet.getLong("user_id");
                    // Retrieve other fields

                    Record record = new Record();
                    record.setId(id);
                    record.setName(name);
                    record.setNote(note);
                    record.setType(ERecordType.valueOf(type));
                    record.setAmount(amount);
                    record.setDate(date);
                    record.setUser(user);
                    // Set other field values

                    return record;
                }
            }
        }

        return null;
    }

    public void saveRecord(Record record) throws SQLException {
        String query = "INSERT INTO records (name, note, type, amount, date, user_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, record.getName());
            statement.setString(2, record.getNote());
            statement.setString(3, record.getType().name());
            statement.setDouble(4, record.getAmount());
            statement.setTimestamp(5, new java.sql.Timestamp(record.getDate().getTime()));
            // Set other field values

            statement.executeUpdate();
        }
    }

    public void updateRecord(Record record) throws SQLException {
        String query = "UPDATE records SET name = ?, note = ?, type = ?, amount = ?, date = ? WHERE id = ? AND user_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, record.getName());
            statement.setString(2, record.getNote());
            statement.setString(3, record.getType().name());
            statement.setDouble(4, record.getAmount());
            statement.setTimestamp(5, new java.sql.Timestamp(record.getDate().getTime()));
            statement.setLong(6, record.getId());
            // Set other field values

            statement.executeUpdate();
        }
    }

    public void deleteRecord(Record record) throws SQLException {
        String query = "DELETE FROM records WHERE id = ? AND user_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, record.getId());

            statement.executeUpdate();
        }
    }

//    @Query("SELECT r FROM Record r WHERE r.user = :user")
//    Page<Record> findRecordsByUser(@Param("user") User user, Pageable pageable);
//
//    @Query("SELECT r FROM Record r WHERE r.user = :user AND r.id = :id")
//    Record findRecordByUserAndId(@Param("user") User user, @Param("id") Long id);
//
//    @Query("SELECT r FROM Record r WHERE r.user = :user AND r.date BETWEEN :startDate AND :endDate")
//    Page<Record> findRecordsByUserAndDateBetween(@Param("user") User user,
//                                                 @Param("startDate") Date startDate,
//                                                 @Param("endDate") Date endDate,
//                                                 Pageable pageable);
//
//    @Query("SELECT r FROM Record r WHERE r.user = :user AND r.date > :date")
//    Page<Record> findRecordsByUserAndDateAfter(@Param("user") User user, @Param("date") Date date, Pageable pageable);
//
//    @Query("SELECT r FROM Record r WHERE r.user = :user AND r.date < :date")
//    Page<Record> findRecordsByUserAndDateBefore(@Param("user") User user, @Param("date") Date date, Pageable pageable);

//    Page<Record> findRecordsByUser(User user, Pageable pageable);
//
//    Record findRecordByUserAndId(User user, Long id);
//
//    Page<Record> findRecordsByUserAndDateBetween(User user, Date date, Date date2, Pageable pageable);
//
//    Page<Record> findRecordsByUserAndDateAfter(User user, Date date, Pageable pageable);
//
//    Page<Record> findRecordsByUserAndDateBefore(User user, Date date, Pageable pageable);

//    @Modifying
//    @Query(value = "INSERT INTO records (user_id, amount, date, name, note, type) " +
//            "VALUES (:userId, :amount, :date, :name, :note, :type)",
//            nativeQuery = true)
//    void createRecord(@Param("userId") Long userId, @Param("amount") Double amount,
//                      @Param("date") Date date, @Param("name") String name,
//                      @Param("note") String note, @Param("type") String type);
//
//    @Modifying
//    @Query(value = "UPDATE records SET amount = :amount, date = :date, name = :name, " +
//            "note = :note, type = :type WHERE id = :id",
//            nativeQuery = true)
//    void updateRecord(@Param("id") Long id, @Param("amount") Double amount,
//                      @Param("date") Date date, @Param("name") String name,
//                      @Param("note") String note, @Param("type") String type);
//
//    @Modifying
//    @Query(value = "DELETE FROM records WHERE id = :id",
//            nativeQuery = true)
//    void deleteRecord(@Param("id") Long id);
}
