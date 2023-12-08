package com.lawyus.study.nested.tx.config;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.Objects;

/**
 * TODO : Describe please!
 *
 * @author lyc
 * @since 2019/12/17
 */
@Configuration
@MapperScan(basePackages = {"com.lawyus.study.nested.tx.dao", "com.lawyus.study.**.dao"},
		sqlSessionTemplateRef = "mysqlSessionTemplate")
public class MysqlDataSourceConfig {
	@Bean("dataSource")
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource mysqlDataSource() {
		return DataSourceBuilder.create().build();
	}
	@Bean("mysqlSessionFactory")
	public SqlSessionFactory mysqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
		GlobalConfig globalConfig = new GlobalConfig();
		globalConfig.setBanner(false);

		MybatisConfiguration mybatisConfiguration = new MybatisConfiguration();
		MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
		mybatisConfiguration.setLogImpl(StdOutImpl.class);

		MybatisPlusInterceptor mpi = new MybatisPlusInterceptor();
		mpi.addInnerInterceptor(new PaginationInnerInterceptor());

		mybatisConfiguration.addInterceptor(mpi);
		bean.setConfiguration(mybatisConfiguration);
		bean.setGlobalConfig(globalConfig);
		bean.setDataSource(dataSource);
		bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mappers/*.xml"));
		Objects.requireNonNull(bean.getObject()).getConfiguration().setMapUnderscoreToCamelCase(true);
		return bean.getObject();
	}
	@Bean("transactionManager")
	@Primary
	public DataSourceTransactionManager mysqlTransactionManager(@Qualifier("dataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
	@Bean("mysqlSessionTemplate")
	public SqlSessionTemplate mysqlSessionTemplate(@Qualifier("mysqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}
