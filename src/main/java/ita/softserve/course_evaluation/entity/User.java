package ita.softserve.course_evaluation.entity;

import com.sun.istack.NotNull;
import ita.softserve.course_evaluation.security.oauth2.users.SocialProvider;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email")
	private String email;


	@Column(name = "password")
	private String password;

	@ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
	@Column(name = "role_id")
	private Set<Role> roles;

	@OneToMany(mappedBy = "student",
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	private List<Feedback> feedbacks = new ArrayList<>();


	@OneToMany(mappedBy = "teacher",
			cascade = CascadeType.ALL,
			orphanRemoval = false)    //  ToDo: What the reason to use orphanRemoval = true ?
	private List<Course> courses;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="group_id")
    private Group group;

    @Column(name = "account_verified")
    private boolean accountVerified = false;

	public User(Long id, String firstName, String lastName, String email, String password) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}

	public User(long teacherId) {
		this.id = teacherId;
	}
}