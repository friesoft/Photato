package photato.core.entities;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<User> findAll() {
		String sql = "SELECT USERNAME,PASSWORD,ROLE FROM USER";
		RowMapper<User> rowMapper = new BeanPropertyRowMapper<User>(User.class);
		return jdbcTemplate.query(sql, rowMapper);
	}
}
