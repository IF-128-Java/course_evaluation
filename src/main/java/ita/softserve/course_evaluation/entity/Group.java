package ita.softserve.course_evaluation.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@Table(name = "groups")
public class Group {


        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "group_name")
        private String groupName;

        @OneToMany(mappedBy = "group")
        private List<User> users = new ArrayList<>();

        //@ManyToMany
        //@JoinTable(
        //        name = "course_group",
        //        joinColumns = @JoinColumn(name = "group_id"),
        //        inverseJoinColumns = @JoinColumn(name = "course_id"))
        //private Set<Course> courses = new HashSet<>();

        public Group() {
        }

        public Group(String groupName) {
            this.groupName = groupName;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Group group = (Group) o;
            return Objects.equals(id, group.id) && Objects.equals(groupName, group.groupName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, groupName);
        }

        @Override
        public String toString() {
            return "Group{" +
                    "id=" + id +
                    ", groupName='" + groupName + '\'' +
                    '}';
        }
    }

