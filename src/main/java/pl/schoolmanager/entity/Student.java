package pl.schoolmanager.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "student")
public class Student {

	@Id
	private long id;

	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<Division> division = new ArrayList<>();

	@OneToMany (mappedBy = "student", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
	List <Mark> mark = new ArrayList<>();
	
	@ManyToOne(fetch = FetchType.EAGER)
	private School school;
	
	@OneToOne(fetch = FetchType.EAGER)
	@MapsId
	private UserRole userRole;

	public Student() {
		super();
	}
	
	public Student(School school, UserRole userRole) {
		super();
		this.school = school;
		this.userRole = userRole;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public List<Division> getDivision() {
		return division;
	}

	public void setDivision(List<Division> division) {
		this.division = division;
	}

	public List<Mark> getMark() {
		return mark;
	}

	public void setMark(List<Mark> mark) {
		this.mark = mark;
	}
	
	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", division=" + division + ", mark=" + mark + ", school=" + school + ", userRole="
				+ userRole + "]";
	}
	
	
	
}