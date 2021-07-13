package ita.softserve.course_evaluation.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "role")
public class Role {
     @Id
     @Column(name="id")
     @GeneratedValue(strategy= GenerationType.IDENTITY)
     private Integer id;

     @Column(name="role_name")
     @Enumerated(EnumType.STRING)
     private ERole roleName;

     @ManyToMany(mappedBy="roles")
    private List<User> users = new ArrayList();

     public Integer getId() {
          return id;
     }

     public void setId(Integer id) {
          this.id = id;
     }

     public ERole getRoleName() {
          return roleName;
     }

     public void setRoleName(ERole roleName) {
          this.roleName = roleName;
     }

     public List<User> getUsers() {
          return users;
     }

     public void setUsers(List<User> users) {
          this.users = users;
     }

     @Override
     public boolean equals(Object o) {
          if (this == o) return true;
          if (o == null || getClass() != o.getClass()) return false;
          Role role = (Role) o;
          return Objects.equals(id, role.id) && roleName == role.roleName && Objects.equals(users, role.users);
     }

     @Override
     public int hashCode() {
          return Objects.hash(id, roleName, users);
     }
}
