package ita.softserve.course_evaluation.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "course")
public class Course implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter @Setter
    private long id;

    @NotNull
    @Column(name = "course_Name", nullable = false)
    @Getter @Setter
    private String courseName;

    @Column(name = "description", nullable = false)
    @Getter @Setter
    private String description;

    @Column(name = "start_Date", nullable = false)
    @Temporal(TemporalType.DATE)
    @Getter @Setter
    private Date startDate;

    @Column(name = "end_Date", nullable = false)
    @Temporal(TemporalType.DATE)
    @Getter @Setter
    private Date endDate;

    @ManyToOne(cascade= CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    @Getter @Setter
    private User user;



}

