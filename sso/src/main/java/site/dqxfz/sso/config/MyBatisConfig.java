package site.dqxfz.sso.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

/**
 * @author WENG Yang
 * @date 2020年04月13日
 **/
@Configuration
@MapperScan("site.dqxfz.sso.dao")
public class MyBatisConfig {
    @Value("${jdbc.driver}")
    private String driver;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.user}")
    private String user;
    @Value("${jdbc.password}")
    private String password;
    @Value("${jdbc.initial-size}")
    private Integer initialSize;
    @Value("${jdbc.max-idle}")
    private Integer maxIdle;
    @Value("${jdbc.max-active}")
    private Integer maxActive;
    @Value("${jdbc.validation-query}")
    private String validationQuery;
    @Value("${jdbc.test-on-borrow}")
    private Boolean testOnBorrow;
    @Value("${jdbc.test-while-idle}")
    private Boolean testWhileIdle;
    @Value("${jdbc.max-wait}")
    private Long maxWait;

    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        return factoryBean;
    }

    @Bean
    public DataSource dataSource(){
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        dataSource.setInitialSize(initialSize);
        dataSource.setMaxIdle(maxIdle);
        dataSource.setMaxActive(maxActive);
        dataSource.setMaxWait(maxWait);
        dataSource.setValidationQuery(validationQuery);
        dataSource.setTestOnBorrow(testOnBorrow);
        dataSource.setTestWhileIdle(testWhileIdle);
        return dataSource;
    }
}