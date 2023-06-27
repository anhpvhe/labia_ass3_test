package lab.ia.ExpenseManagement.Models;

import jakarta.persistence.*;
import lab.ia.ExpenseManagement.Models.Enums.ERole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles")
@NoArgsConstructor
@Getter
@Setter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private ERole name;

    public Role(ERole name) {
        this.name = name;
    }
}
