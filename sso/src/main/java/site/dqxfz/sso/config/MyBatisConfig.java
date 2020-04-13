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
//@MapperScan("site.dqxfz.sso.dao")
public class MyBatisConfig implements EnvironmentAware {
    @Value("${jdbc.driver}")
    private String driver;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.user}")
    private String user;
    @Value("${jdbc.password}")
    private String password;
    private Environment environment;


//    public @Bean MapperScannerConfigurer mapperScannerConfigurer() throws Exception {
//        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
//        configurer.setBasePackage("site.dqxfz.sso.dao");
//        configurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
//        return configurer;
//    }
//    public @Bean SqlSessionFactory sqlSessionFactory() throws Exception {
//        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
//        factoryBean.setDataSource(dataSource());
//        SqlSessionFactory sqlSessionFactory = factoryBean.getObject();
//        return sqlSessionFactory;
//    }



    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

}