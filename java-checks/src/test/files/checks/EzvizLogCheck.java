import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import org.hibernate.Session;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import javax.jdo.PersistenceManager;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Spring {

    private static final Logger logger = LoggerFactory.getLogger(Spring.class);

    private JdbcTemplate jdbcTemplate;
    private JdbcOperations jdbcOperations;
    private PreparedStatementCreatorFactory preparedStatementCreatorFactory;

    public void run() {
        try{
            cloudUserInfoSynService.execCloudUserInfoNotify(notifyId, info);
        }catch(Exception e){
            logger.debug("pwd CloudNotifyTask run error notifyId="+notifyId+" info="+info,e);
        }

    }

    void test(String parameter) {
        logger.debug("This is debug message.password");  // Noncompliant
        java.lang.String sqlInjection = "select count(*) from t_actor where column =  " + parameter;
        jdbcTemplate.queryForObject(sqlInjection, Integer.class);
        jdbcOperations.queryForObject(sqlInjection, Integer.class);

        new PreparedStatementCreatorFactory(sqlInjection);
        preparedStatementCreatorFactory.newPreparedStatementCreator(sqlInjection, new int[] {});
    }
}