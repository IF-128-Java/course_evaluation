package ita.softserve.course_evaluation.registration;

import ita.softserve.course_evaluation.entity.User;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class UserActive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean locked;
    private boolean enabled;
    @OneToOne
    @JoinColumn(
            nullable = false,
            name = "USER_ID"
    )
    private User user;

}
