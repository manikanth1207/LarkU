package ttl.larku.dao.jdbctemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import ttl.larku.dao.BaseDAO;
import ttl.larku.domain.Course;
import ttl.larku.domain.ScheduledClass;

public class JDBCTemplateClassDAO implements BaseDAO<ScheduledClass> {

	private final String UPDATE_SQL = "update ScheduledClass set startdate = ?, " +
			"enddate = ?, course_id = ? where id = ?";
	private final String DELETE_SQL = "delete from scheduledclass where id = ?";
	private final String INSERT_SQL = "insert into scheduledclass " +
			"(startdate, enddate, course_id) values(?, ?, ?)";
	private final String SELECT_SQL = "select * from scheduledclass where id = ?";
	private final String SELECT_ALL_SQL = "select * from scheduledclass";

	private final String DELETE_ALL = "delete from ScheduledClass";
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private BaseDAO<Course> courseDAO;

	@Override
	public void update(ScheduledClass updateObject) {
		int result = getJdbcTemplate().update(UPDATE_SQL, 
				updateObject.getStartDate(), updateObject.getEndDate(), 
				updateObject.getCourse().getId(), updateObject.getId());
	}

	@Override
	public void delete(ScheduledClass sc) {
		int result = getJdbcTemplate().update(DELETE_SQL, sc.getId());
	}

	@Override
	public ScheduledClass create(final ScheduledClass newObject) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
		    new PreparedStatementCreator() {
		        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		            PreparedStatement ps =
		                connection.prepareStatement(INSERT_SQL, new String[] {"id"});
		            ps.setString(1, newObject.getStartDate());
		            ps.setString(2, newObject.getEndDate());
		            ps.setInt(3, newObject.getCourse().getId());
		            return ps;
		        }
		    },
		    keyHolder);
		
		newObject.setId(keyHolder.getKey().intValue());
		return newObject;
	}

	@Override
	public ScheduledClass get(int id) {
		ScheduledClass sc = getJdbcTemplate().queryForObject(SELECT_SQL,
				new ScheduledClassRowMapper(), id);

		return sc;
	}

	@Override
	public List<ScheduledClass> getAll() {
		List<ScheduledClass> scs = getJdbcTemplate().query(SELECT_ALL_SQL,  
				new ScheduledClassRowMapper());
		
		return scs;
	}
	
	@Override
	public void deleteStore() {
		int result = getJdbcTemplate().update(DELETE_ALL);
	}
	
	@Override
	public void createStore() {
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public BaseDAO<Course> getCourseDAO() {
		return courseDAO;
	}

	public void setCourseDAO(BaseDAO<Course> courseDAO) {
		this.courseDAO = courseDAO;
	}

	public class ScheduledClassRowMapper implements RowMapper<ScheduledClass> {
		@Override
		public ScheduledClass mapRow(ResultSet rs, int rowNum) throws SQLException {
			int id = rs.getInt("id");
			String startDate = rs.getString("startDate");
			String endDate = rs.getString("endDate");
			int courseId = rs.getInt("course_id");
			
			//Now go get the course for this ID.  Here we are
			//mapping the relationship ourselves
			Course course = courseDAO.get(courseId);
			
			ScheduledClass sc = new ScheduledClass(course, startDate, endDate);
			sc.setId(id);
			
			return sc;
		}

	}
}
