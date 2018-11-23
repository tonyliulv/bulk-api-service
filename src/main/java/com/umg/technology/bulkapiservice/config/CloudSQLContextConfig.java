package com.umg.technology.bulkapiservice.config;

import com.umg.technology.bulkapiservice.service.cloudsql.dao.impl.mapper.DBPartnerQueryMapper;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.MappedTypes;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * The {@code CloudSQLContextConfig} class configures
 * cloud sql mysql datasource
 */

@Configuration
@MappedTypes({DBPartnerQueryMapper.class})
@MapperScan(value = {"com.umg.technology.bulkapiservice.service.cloudsql.dao.impl.mapper"},  sqlSessionFactoryRef="sqlSessionFactoryCloudSQL")
@ConfigurationProperties("cloudsql")
public class CloudSQLContextConfig {

    @Value("${cloudsql.url}")
    private String cloudsqlUrl;

    @Value("${cloudsql.username}")
    private String cloudsqlUsername;

    @Value("${cloudsql.password}")
    private String cloudsqlPassword;

    @Bean
    public DataSource cloudSQLDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(cloudsqlUrl);
        dataSource.setUsername(cloudsqlUsername);
        dataSource.setPassword(cloudsqlPassword);
        return dataSource;
    }

    @Bean(name = "sqlSessionFactoryCloudSQL")
    public SqlSessionFactory sqlSessionFactoryPrepdb() throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(cloudSQLDataSource());
        return sqlSessionFactory.getObject();
    }
}
