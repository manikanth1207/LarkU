package ttl.larku.dao.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ttl.larku.dao.BaseDAO;
import ttl.larku.domain.ScheduledClass;

@Transactional
public class HibernateClassDAO implements BaseDAO<ScheduledClass> {

	/*
	@PersistenceContext(unitName="LarkUPU_SE")
	private EntityManager entityManager;
	*/
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private static int nextId = 0;
	
	@Override
	public void update(ScheduledClass updateObject) {
		sessionFactory.getCurrentSession().update(updateObject);
	}

	@Override
	public void delete(ScheduledClass deleteObject) {
		sessionFactory.getCurrentSession().delete(deleteObject);
	}

	@Override
	public ScheduledClass create(ScheduledClass newObject) {
		sessionFactory.getCurrentSession().save(newObject);
		return newObject;
	}

	@Override
	public ScheduledClass get(int id) {
		return (ScheduledClass)sessionFactory.getCurrentSession().get(ScheduledClass.class, id);
	}

	@Override
	public List<ScheduledClass> getAll() {
		Query query = sessionFactory.getCurrentSession().createQuery("Select s from ScheduledClass s");
		List<ScheduledClass> classes = query.list();
		return classes;
	}
	
	@Override
	public void deleteStore() {
		Query query = sessionFactory.getCurrentSession().createSQLQuery("Delete from ScheduledClass_Student");
		query.executeUpdate();
		
		query = sessionFactory.getCurrentSession().createQuery("Delete from ScheduledClass");
		query.executeUpdate();
	}
	
	@Override
	public void createStore() {
	}
}
