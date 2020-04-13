package site.dqxfz.sso.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author WENG Yang
 * @date 2020年04月13日
 **/
@Configuration
public class MyBatisConfig {
    public @Bean SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(null);
        return factoryBean.getObject();
    }
}