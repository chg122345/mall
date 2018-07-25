package org.jleopard.mall.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime 2018-07-21  下午12:11
 *
 * <p>
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 * </p>
 */
@Configuration
@MapperScan(basePackages = "org.jleopard.mall.dao")
public class DataSourceConfig {

    private String url = "jdbc:mysql://localhost:3306/mall?useUnicode=true&characterEncoding=utf8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

    private String username = "root";

    private String password = "chg122345";

    private String driverClass = "com.mysql.jdbc.Driver";

    private int initialSize = 5;

    private int minIdle = 5;

    private int maxActive = 20;

    private int maxWait = 60000;

    private int timeBetweenEvictionRunsMillis = 60000;

    private int minEvictableIdleTimeMillis  = 30000;

    private String validationQuery = "SELECT 1 FROM DUAL";

    private boolean testWhileIdle = true;

    private boolean testOnBorrow = false;

    private boolean testOnReturn = false;

    private boolean poolPreparedStatements = true;

    private int maxPoolPreparedStatementPerConnectionSize = 20;

    private String filters = "stat,wall,slf4j";

    private String connectionProperties = "druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000";

    @Bean
    public DataSource dataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClass);

        dataSource.setInitialSize(initialSize);
        dataSource.setMinIdle(minIdle);
        dataSource.setMaxActive(maxActive);
        dataSource.setMaxWait(maxWait);
        dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        dataSource.setValidationQuery(validationQuery);
        dataSource.setTestWhileIdle(testWhileIdle);
        dataSource.setTestOnBorrow(testOnBorrow);
        dataSource.setTestOnReturn(testOnReturn);
        dataSource.setPoolPreparedStatements(poolPreparedStatements);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        try {
            dataSource.setFilters(filters);
        } catch (SQLException e) {
            throw new RuntimeException("druid configuration initialization filter----->>:", e);
        }
        dataSource.setConnectionProperties(connectionProperties);
        return dataSource;
    }

    private org.apache.ibatis.session.Configuration getConfiguration(){
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setCacheEnabled(true);
        configuration.setDefaultStatementTimeout(3000);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setUseGeneratedKeys(true);
        configuration.setUseColumnLabel(true);
        return configuration;
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource){
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        try {
            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            sqlSessionFactoryBean.setDataSource(dataSource);
            sqlSessionFactoryBean.setConfiguration(getConfiguration());
            sqlSessionFactoryBean.setMapperLocations(resourcePatternResolver.getResources("classpath:mapper/*.xml"));
            sqlSessionFactoryBean.setTypeAliasesPackage("org.jleopard.mall.dao");
        } catch (IOException e) {
            throw new RuntimeException("初始化sessionFactoryBean失败.." , e);
        }
        return sqlSessionFactoryBean;
    }
    @Bean(name = "transactionManager")
    public DataSourceTransactionManager dataSourceTransactionManager(){
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource());
        return dataSourceTransactionManager;
    }

    @Bean(name="transactionInterceptor")
    public TransactionInterceptor interceptor(){
        TransactionInterceptor interceptor = new TransactionInterceptor();
        interceptor.setTransactionManager(dataSourceTransactionManager());

        Properties transactionAttributes = new Properties();
        transactionAttributes.setProperty("*","PROPAGATION_REQUIRED");

        interceptor.setTransactionAttributes(transactionAttributes);
        return interceptor;
    }
}
