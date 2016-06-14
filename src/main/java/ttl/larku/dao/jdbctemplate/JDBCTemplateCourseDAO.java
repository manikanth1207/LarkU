package ttl.larku.dao.jdbctemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import ttl.larku.dao.BaseDAO;
import ttl.larku.domain.Course;

public class JDBCTemplateCourseDAO implements BaseDAO<Course> {

	private final String UPDATE_SQL = "update course set code = ?, title = ?, credits = ? where id = ?";
	private final String DELETE_SQL = "delete from course where id = ?";
	private final String INSERT_SQL = "insert into course (code, title, credits) values(?, ?, ?)";
	private final String SELECT_SQL = "select * from course where id = ?";
	private final String SELECT_ALL_SQL = "select * from course";
	
	private final String DELETE_ALL = "delete from Course";

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public void update(Course updateObject) {
		int result = getJdbcTemplate().update(UPDATE_SQL, updateObject.getCode(), 
				updateObject.getTitle(), 
				updateObject.getCredits(),
				updateObject.getId());
	}

	@Override
	public void delete(Course course) {
		int result = getJdbcTemplate().update(DELETE_SQL, course.getId());
	}

	@Override
	public Course create(final Course newObject) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
		    new PreparedStatementCreator() {
		        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		            PreparedStatement ps =
		                connection.prepareStatement(INSERT_SQL, new String[] {"id"});
		            ps.setString(1, newObject.getCode());
		            ps.setString(2, newObject.getTitle());
		            ps.setFloat(3, newObject.getCredits());
		            return ps;
		        }
		    },
		    keyHolder);
		
		newObject.setId(keyHolder.getKey().intValue());
		return newObject;
	}

	@Override
	public Course get(int id) {
		Course course = getJdbcTemplate().queryForObject(SELECT_SQL,
				new CourseRowMapper(), id);

		return course;
	}

	@Override
	public List<Course> getAll() {
		List<Course> students = getJdbcTemplate().query(SELECT_ALL_SQL,  
				new CourseRowMapper());
		
		return students;
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
	
	public class CourseRowMapper implements RowMapper<Course> {
		@Override
		public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
			int id = rs.getInt("id");
			String code = rs.getString("code");
			String title = rs.getString("title");
			
			Course course = new Course(code, title);
			course.setId(id);
			
			return course;
		}

	}
	
}
