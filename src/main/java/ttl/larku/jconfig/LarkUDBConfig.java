package ttl.larku.jconfig;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import ttl.larku.dao.BaseDAO;
import ttl.larku.dao.inmemory.InMemoryClassDAO;
import ttl.larku.dao.inmemory.InMemoryCourseDAO;
import ttl.larku.dao.inmemory.InMemoryStudentDAO;
import ttl.larku.dao.jdbctemplate.JDBCTemplateClassDAO;
import ttl.larku.dao.jdbctemplate.JDBCTemplateCourseDAO;
import ttl.larku.dao.jdbctemplate.JDBCTemplateStudentDAO;
import ttl.larku.dao.jpahibernate.JPAClassDAO;
import ttl.larku.dao.jpahibernate.JPACourseDAO;
import ttl.larku.dao.jpahibernate.JPAStudentDAO;
import ttl.larku.domain.Course;
import ttl.larku.domain.ScheduledClass;
import ttl.larku.domain.Student;

@Configuration
@EnableTransactionManagement
public class LarkUDBConfig implements TransactionManagementConfigurer {

	@Autowired
	private ApplicationContext appContext;

	// In Memory DAOs
	@Bean(name = { "inMemoryClassDAO" })
	public InMemoryClassDAO inMemoryClassDAO() {
		return new InMemoryClassDAO();
	}

	@Bean(name = { "inMemoryCourseDAO" })
	public InMemoryCourseDAO inMemoryCourseDAO() {
		return new InMemoryCourseDAO();
	}

	@Bean(name = { "inMemoryStudentDAO" })
	public InMemoryStudentDAO inMemoryStudentDAO() {
		return new InMemoryStudentDAO();
	}
	
	@Bean
	public JDBCTemplateClassDAO jdbcTemplateClassDAO() {
		return new JDBCTemplateClassDAO();
	}

	@Bean
	public JDBCTemplateCourseDAO jdbcTemplateCourseDAO() {
		return new JDBCTemplateCourseDAO();
	}

	@Bean
	public JDBCTemplateStudentDAO jdbcTemplateStudentDAO() {
		return new JDBCTemplateStudentDAO();
	}

	// JPA DAOs
	@Bean
	public BaseDAO<Student> jpaStudentDAO() {
		return new JPAStudentDAO();
	}

	@Bean
	public BaseDAO<ScheduledClass> jpaClassDAO() {
		return new JPAClassDAO();
	}

	@Bean
	public BaseDAO<Course> jpaCourseDAO() {
		return new JPACourseDAO();
	}


	@Bean(name = "dataSource")
	public DataSource dataSource() {
		SimpleDriverDataSource sdds = new SimpleDriverDataSource();
		sdds.setUrl("jdbc:derby://localhost/LarkUDB");
		sdds.setDriverClass(org.apache.derby.jdbc.ClientDriver.class);
		sdds.setUsername("larku");
		sdds.setPassword("larku");

		return sdds;
	}
	

	@Bean
	public JdbcTemplate jdbcTemplate() {
		JdbcTemplate template = new JdbcTemplate();
		template.setDataSource(dataSource());
		
		return template;
	}

	@Bean(name = "LarkUPU_SE")
	public LocalContainerEntityManagerFactoryBean getLcemfg() {
		LocalContainerEntityManagerFactoryBean lcemfg = new LocalContainerEntityManagerFactoryBean();
		lcemfg.setDataSource(dataSource());
		lcemfg.setPersistenceUnitName("LarkUPU_SE");

		return lcemfg;
	}

	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean lsfb = new LocalSessionFactoryBean();
		lsfb.setDataSource(dataSource());
		lsfb.setPackagesToScan("ttl.larku.domain");

		Properties props = new Properties();
		props.put("dialect", "org.hibernate.dialect.DerbyDialect");
		props.put("javax.persistence.validation.mode", "none");

		lsfb.setHibernateProperties(props);

		return lsfb;
	}

	@Bean @Primary
	public JpaTransactionManager jpaTransactionManager() {
		JpaTransactionManager jtm = new JpaTransactionManager();
		jtm.setEntityManagerFactory(getLcemfg().getObject());

		return jtm;
	}

	@Bean
	public DataSourceTransactionManager dsTransactionManager() {
		DataSourceTransactionManager tm = new DataSourceTransactionManager();
		tm.setDataSource(dataSource());

		return tm;
	}

	@Bean
	public HibernateTransactionManager hibernateTransactionManager() {
		HibernateTransactionManager jtm = new HibernateTransactionManager();
		jtm.setSessionFactory(sessionFactory().getObject());

		return jtm;
	}

	// Set the transaction manager to be used,
	// as, in <tx:annotation-driven transaction-manager="jpaTransactionManager"
	// />
	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		return jpaTransactionManager();
	}

	private boolean containsProfile(String[] profiles, String profile) {
		for (String p : profiles) {
			if (p.equals(profile)) {
				return true;
			}
		}

		return false;
	}
}
