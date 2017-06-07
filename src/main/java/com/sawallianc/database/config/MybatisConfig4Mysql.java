package com.sawallianc.database.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author fingertap
 * Created by fingertap on 2017/6/7.
 */
@Configuration
@EnableTransactionManagement
@MapperScan({"com.sawallianc.**.dao"})
public class MybatisConfig4Mysql {
    private static final String MAPPER_LOCATION = "classpath*:mapper/**/*Mapper.xml";

    public MybatisConfig4Mysql(){

    }
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource(){
        return new DruidDataSource();
    }
    @Bean
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception{
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(this.dataSource());
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        bean.setMapperLocations(resolver.getResources(MAPPER_LOCATION));
        PageHelper pageHelper = new PageHelper();
        Properties props = new Properties();
        props.setProperty("reasonable","true");
        props.setProperty("supportMethodArguments","true");
        props.setProperty("returnPageInfo","check");
        props.setProperty("params","count=countSql");
        props.setProperty("dialect","mysql");
        props.setProperty("rowBoundsWithCount","true");
        pageHelper.setProperties(props);
        bean.setPlugins(new Interceptor[]{pageHelper});
        return bean.getObject();
    }
    @Bean
    public PlatformTransactionManager transactionManager(){
        return new DataSourceTransactionManager(this.dataSource());
    }
}
