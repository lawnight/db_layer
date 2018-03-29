package db2.core;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import db2.config.AppContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by lijiangtao on 2018/3/26.
 */
public class JdbcTemplateUtil {


    private static final MysqlDataSource dataSource;

    static {
        dataSource = new MysqlDataSource();
        dataSource.setUrl("jdbc:mysql://192.168.2.179/king5?characterEncoding=utf-8");
        dataSource.setPort(3306);
        dataSource.setUser("root");
        dataSource.setPassword("123456");
    }


    public static void main(String[] args) throws Exception {
        JdbcTemplate jdbcTemplate = AppContext.getBean("jdbcTemplate");//new JdbcTemplate(dataSource);
        long start = System.currentTimeMillis();

        boolean transaction = false;
        PlatformTransactionManager txManager = AppContext.getBean("jdbctransactionManager");
        TransactionStatus status = null;
        if (transaction) {
            //事务
            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            //事务的隔离级别
            def.setIsolationLevel(TransactionDefinition.ISOLATION_SERIALIZABLE);
            //事务的传播行为
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            status = txManager.getTransaction(def);
        }
        int count = 2999999;
        for (int i = 185343; i < count; i++) {
            String str = String.format("INSERT into log_usr_machine VALUES(%d,%d,%d,%d,%d)", i, i, i, i, i);
            try {
                jdbcTemplate.update(str);
                Thread.sleep(10);
                System.err.println("process:" + i);
            } catch (Exception ex) {

            }

        }
        if (transaction) {
            txManager.commit(status);
        }
        long end = System.currentTimeMillis();
        System.err.println("cost:" + (end - start));
    }
}
