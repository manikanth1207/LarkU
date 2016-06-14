package ttl.larku.dao.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ttl.larku.dao.BaseDAO;
import ttl.larku.domain.Course;

@Transactional
public class HibernateCourseDAO implements BaseDAO<Course> {

	/*
	@PersistenceContext(unitName="LarkUPU_SE")
	private EntityManager entityManager;
	*/
	
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public void update(Course updateObject) {
		sessionFactory.getCurrentSession().update(updateObject);
	}

	@Override
	public void delete(Course course) {
		sessionFactory.getCurrentSession().delete(course);
	}

	@Override
	public Course create(Course newObject) {
		sessionFactory.getCurrentSession().save(newObject);
		return newObject;
	}

	@Override
	public Course get(int id) {
		return (Course)sessionFactory.
				getCurrentSession().get(Course.class, id);
	}

	@Override
	public List<Course> getAll() {
		Query query = sessionFactory.getCurrentSession().createQuery("Select s from Course s");
		List<Course> courses = query.list();
		return courses;
	}
	
	@Override
	public void deleteStore() {
		Query query = sessionFactory.getCurrentSession().createQuery("Delete from Course");
		query.executeUpdate();
	}
	
	@Override
	public void createStore() {
	}
}
