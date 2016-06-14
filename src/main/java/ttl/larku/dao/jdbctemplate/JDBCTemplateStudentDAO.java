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
import ttl.larku.domain.Student;

public class JDBCTemplateStudentDAO implements BaseDAO<Student> {

	private final String UPDATE_SQL = "update student set name = ?, status = ?, phoneNumber = ? where id = ?";
	private final String DELETE_SQL = "delete from student where id = ?";
	private final String INSERT_SQL = "insert into student (name, status, phoneNumber) values(?, ?, ?)";
	private final String SELECT_SQL = "select * from student where id = ?";
	private final String SELECT_ALL_SQL = "select * from student";
	
	private final String DELETE_ALL = "delete from student";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void update(Student updateObject) {
		int result = getJdbcTemplate().update(UPDATE_SQL, updateObject.getName(), 
				updateObject.getStatus().name(), 
				updateObject.getPhoneNumber(), 
				updateObject.getId());
	}

	@Override
	public void delete(Student student) {
		int result = getJdbcTemplate().update(DELETE_SQL, student.getId());
	}

	@Override
	public Student create(final Student newObject) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
		    new PreparedStatementCreator() {
		        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		            PreparedStatement ps =
		                connection.prepareStatement(INSERT_SQL, new String[] {"id"});
		            ps.setString(1, newObject.getName());
		            ps.setString(2, newObject.getStatus().name());
		            ps.setString(3, newObject.getPhoneNumber());
		            return ps;
		        }
		    },
		    keyHolder);
		
		newObject.setId(keyHolder.getKey().intValue());
		return newObject;
	}

	@Override
	public Student get(int id) {
		Student student = getJdbcTemplate().queryForObject(SELECT_SQL,
				new StudentRowMapper(), id);

		return student;
	}

	@Override
	public List<Student> getAll() {
		List<Student> students = getJdbcTemplate().query(SELECT_ALL_SQL,  new StudentRowMapper());
		
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

	public class StudentRowMapper implements RowMapper<Student> {
		@Override
		public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
			int id = rs.getInt("id");
			String name = rs.getString("name");
			Student.Status status = Student.Status.valueOf(rs.getString("status"));
			String phoneNumber = rs.getString("phoneNumber");
			
			Student student = new Student(name);
			student.setId(id);
			student.setStatus(status);
			student.setPhoneNumber(phoneNumber);
			
			return student;
		}

	}
}
