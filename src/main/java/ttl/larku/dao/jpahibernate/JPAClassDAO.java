package ttl.larku.dao.jpahibernate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.annotation.Transactional;

import ttl.larku.dao.BaseDAO;
import ttl.larku.domain.ScheduledClass;

@Transactional
public class JPAClassDAO implements BaseDAO<ScheduledClass> {

	@PersistenceContext(unitName="LarkUPU_SE")
	private EntityManager entityManager;
	private static int nextId = 0;
	
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	@Override
	public void update(ScheduledClass updateObject) {
		entityManager.merge(updateObject);
	}

	@Override
	public void delete(ScheduledClass objToDelete) {
		ScheduledClass managed = entityManager.find(ScheduledClass.class, objToDelete.getId());
		entityManager.remove(managed);
	}

	@Override
	public ScheduledClass create(ScheduledClass newObject) {
		entityManager.persist(newObject);
		return newObject;
	}

	@Override
	public ScheduledClass get(int id) {
		return entityManager.find(ScheduledClass.class, id);
	}

	@Override
	public List<ScheduledClass> getAll() {
		Query query = entityManager.createQuery("Select s from ScheduledClass s");
		List<ScheduledClass> classes = query.getResultList();
		return classes;
	}
	
	@Override
	public void deleteStore() {
		Query query = entityManager.createQuery("Delete from ScheduledClass");
		query.executeUpdate();
	}
	
	@Override
	public void createStore() {
	}
}
