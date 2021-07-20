package ita.softserve.course_evaluation.entity;

import com.sun.istack.NotNull;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "course")
public class Course implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    @Column(name = "course_Name", nullable = false)
    private String courseName;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "start_Date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "end_Date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @OneToMany(mappedBy="course")
    private List<User> users;

}
