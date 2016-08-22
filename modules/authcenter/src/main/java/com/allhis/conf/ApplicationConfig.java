package com.allhis.conf;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import com.allhis.filter.SSLContextGenerator;
import com.zaxxer.hikari.HikariDataSource;
//import org.slf4j.Logger;
import org.apache.mina.filter.ssl.SslFilter;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import javax.sql.DataSource;
import java.io.File;

//import com.allcom.handler.MinaClientHandler;

//import com.zaxxer.hikari.HikariDataSource;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.xbill.DNS.Resolver;
//import org.xbill.DNS.SimpleResolver;

/**
 * Created by ljy on 15/10/16.
 * ok
 */
@Configuration
public class ApplicationConfig {

    @Value("${dataSource.idbuser}")
    private String idbUsername;
    @Value("${dataSource.driver}")
    private String jdbcDriver;
    @Value("${dataSource.idbpass}")
    private String idbPassword;
    @Value("${dataSource.idburl}")
    private String idbUrl;

    @Value("${dataSource.idbuser2}")
    private String idbUsername2;
    @Value("${dataSource.driver2}")
    private String jdbcDriver2;
    @Value("${dataSource.idbpass2}")
    private String idbPassword2;
    @Value("${dataSource.idburl2}")
    private String idbUrl2;

    @Value("${passwordencoder.key}")
    private String encodekey;
    //在标注了@Configuration的java类中，通过在类方法标注@Bean定义一个Bean。方法必须提供Bean的实例化逻辑。
    //通过@Bean的name属性可以定义Bean的名称，未指定时默认名称为方法名。
    @Bean
    public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
        PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
        //ClassPathResource 的根目录在本项目是指resources目录
        //ppc.setLocation(new ClassPathResource("/test.properties"));
        ppc.setLocation(new FileSystemResource("/appconf/ah_authcenter/app.properties"));
        return ppc;
    }

    @Bean
    public static JoranConfigurator readLogbackPropertyFile(){
        File logbackFile = new File("/appconf/ah_authcenter/logback.xml");
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(lc);
        lc.reset();
        try {
            configurator.doConfigure(logbackFile);
        }
        catch (JoranException e) {
            e.printStackTrace(System.err);
            System.exit(-1);
        }
        return configurator;

    }

    @Bean(name="jdbctemplateemail")
    JdbcTemplate jdbcTemplate(@Qualifier("db1")DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate;
    }

    @Bean(name="jdbctemplatelog")
    JdbcTemplate jdbcTemplate2(@Qualifier("db2")DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate;
    }


    @Bean(name="dataSource")
    @Qualifier("db1")
    @Scope("prototype")
    DataSource dataSource(){
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setUsername(idbUsername);
        hikariDataSource.setDriverClassName(jdbcDriver);
        hikariDataSource.setPassword(idbPassword);
        hikariDataSource.setJdbcUrl(idbUrl);
        hikariDataSource.setMaximumPoolSize(3);
        hikariDataSource.setConnectionTestQuery("select count(*) from iprofile");
        return hikariDataSource;
    }


    @Bean(name="dataSource2")
    @Qualifier("db2")
    @Scope("prototype")
    DataSource dataSource2(){
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setUsername(idbUsername2);
        hikariDataSource.setDriverClassName(jdbcDriver2);
        hikariDataSource.setPassword(idbPassword2);
        hikariDataSource.setJdbcUrl(idbUrl2);
        hikariDataSource.setMaximumPoolSize(3);
        hikariDataSource.setConnectionTestQuery("select count(*) from loginfaillog_m");
        return hikariDataSource;
    }

    @Bean
    public SslFilter sslFilter(){
        SslFilter sslFilter = new SslFilter(new SSLContextGenerator().getSslContext());
        return sslFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new StandardPasswordEncoder(encodekey);
    }

}
