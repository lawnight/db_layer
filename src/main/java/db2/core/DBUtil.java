package db2.core;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import db2.config.AppContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.*;

/**
 * Created by lijiangtao on 2018/3/23.
 */
public class DBUtil {

    private Connection connect;

    PlatformTransactionManager txManager = AppContext.getBean("transactionManager");


    public boolean init() {
        String driver = "com.mysql.jdbc.Driver";
        // URL指向要访问的数据库名scutcs
        String url = "jdbc:mysql://192.168.2.179/king5?characterEncoding=utf-8";
        // MySQL配置时的用户名
        String user = "root";
        // Java连接MySQL配置时的密码
        String password = "123456";
        try {
            // 加载驱动程序
            Class.forName(driver);
            // 连续数据库
            connect = DriverManager.getConnection(url, user, password);

            // connect.setCatalog("game");
            if (!connect.isClosed()) {
                System.out.println("Succeeded connecting to the Database!");
            } else {
                System.out.println("failed connecting to the Database!");
                return false;
            }

        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return true;
    }

    public void init2() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl("jdbc:mysql://192.168.2.179/king5?characterEncoding=utf-8");
        dataSource.setDatabaseName("king5");
        dataSource.setPort(3306);
        dataSource.setUser("root");
        dataSource.setPassword("123456");
        try {
            connect = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnect() {
        return connect;
    }

    public void setConnect(Connection connect) {
        this.connect = connect;
    }

    public static void main(String[] args) throws Exception {
        DBUtil util = new DBUtil();
        util.init2();
        util.test();
    }

    private void test() throws SQLException {
        int count = 1000;
        try {
            connect.setAutoCommit(true);
            String sql = "INSERT into t_g_map_unit VALUES(?,?,?,?)";
            PreparedStatement statement = connect.prepareStatement(sql);

            //事务
            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            //事务的传播行为
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            TransactionStatus status = txManager.getTransaction(def);


            long start = System.currentTimeMillis();
            for (int i = 0; i < count; i++) {
                statement.setInt(1, 1);
                statement.setInt(2, 1);
                statement.setInt(3, 1);
                statement.setInt(4, i);
                int state = statement.executeUpdate();
                //statement.addBatch();
                //System.err.println("state:" + state);
            }
            //statement.executeBatch();
            long end = System.currentTimeMillis();
            txManager.commit(status);

            System.err.println("cost:" + (end - start));
            connect.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {

        }
    }
}
