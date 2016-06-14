package ttl.larku.dao.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ttl.larku.dao.BaseDAO;
import ttl.larku.domain.Student;

@Transactional
public class HibernateStudentDAO implements BaseDAO<Student> {

	/*
	@PersistenceContext(unitName="LarkUPU_SE")
	private EntityManager entityManager;
	*/
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void update(Student updateObject) {
		sessionFactory.getCurrentSession().update(updateObject);
	}

	@Override
	public void delete(Student student) {
		sessionFactory.getCurrentSession().delete(student);
	}

	@Override
	@Transactional
	public Student create(Student newObject) {
		sessionFactory.getCurrentSession().save(newObject);
		return newObject;
	}

	@Override
	public Student get(int id) {
		return (Student)sessionFactory.getCurrentSession().get(Student.class, id);
	}

	@Override
	public List<Student> getAll() {
		Query query = sessionFactory.getCurrentSession().createQuery("Select s from Student s");
		//Query query = entityManager.createQuery("Select s from Student s");
		List<Student> students = query.list();
		return students;
	}
	
	@Override
	public void deleteStore() {
		Query query = sessionFactory.getCurrentSession().createQuery("Delete from Student");
		query.executeUpdate();
	}
	
	@Override
	public void createStore() {
	}

}
