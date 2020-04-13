package site.dqxfz.sso.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author WENG Yang
 * @date 2020年04月14日
 **/
@Configuration
@MapperScan("site.dqxfz.sso.dao")
public class MyConfig {
    @Value("${jdbc.driver}")
    private String driver;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.user}")
    private String user;
    @Value("${jdbc.password}")
    private String password;

//    public @Bean
//    MapperScannerConfigurer mapperScannerConfigurer() throws Exception {
//        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
//        configurer.setBasePackage("site.dqxfz.sso.dao");
//        configurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
//        return configurer;
//    }
    public @Bean
    SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        return factoryBean;
    }
    @Bean
    public DataSource dataSource(){
        BasicDataSource dataSource = new BasicDataSource();
//        dataSource.setDriverClassName(environment.getProperty("jdbc.driver"));
//        dataSource.setUrl(environment.getProperty("jdbc.url"));
//        dataSource.setUsername(environment.getProperty("jdbc.user"));
//        dataSource.setPassword(environment.getProperty("jdbc.password"));
//        dataSource.setInitialSize(Integer.parseInt(environment.getProperty("jdbc.initial.size")));
//        dataSource.setMaxActive(Integer.parseInt(environment.getProperty("jdbc.max.active")));
        return dataSource;
    }
    @Bean
    public Object object(){
        return new Object();
    }
}
