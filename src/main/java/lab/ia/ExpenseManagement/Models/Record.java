package lab.ia.ExpenseManagement.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lab.ia.ExpenseManagement.Models.Enums.ERecordType;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "records")
@NoArgsConstructor
@Getter
@Setter
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String name;

    @Column
    private String note;

    @Column
    @Enumerated(EnumType.STRING)
    private ERecordType type;

    @Column
    private double amount;

    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    private Date date;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "category_records", joinColumns = @JoinColumn(name = "record_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Category> categories;

    public Record(User user, String name, String note, ERecordType type, double amount, Date date, List<Category> categories) {
        this.user = user;
        this.name = name;
        this.note = note;
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.categories = categories;
    }
}
