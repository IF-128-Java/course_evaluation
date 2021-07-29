package ita.softserve.course_evaluation.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

	@ManyToOne(fetch = FetchType.LAZY)
	private Group group;

	public User(Long id, String firstName, String lastName, String email, String password) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}
}