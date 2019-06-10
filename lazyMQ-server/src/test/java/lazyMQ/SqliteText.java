package lazyMQ;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhao.lazy.MqApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=MqApplication.class)
public class SqliteText {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Test
	public void t() {
		//jdbcTemplate.execute("CREATE TABLE save_data(num integer primary key, id int, data text, time text)");
		for(int i = 0 ; i < 100 ; i++) {
			jdbcTemplate.update("insert into save_data values(null,201"+i+",'测试','2019-1-"+i+"');");
		}
		
		System.out.println(JSONArray.toJSONString(jdbcTemplate.queryForList("select * from save_data")));
	}
}
