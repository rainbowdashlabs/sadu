package de.chojo.sqlutil.datasource;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

class DataSourceCreatorTest {

    public void creation() {
        HikariDataSource build = DataSourceCreator.create(DataSource.class)
                .withAddress("localhost")
                .forDatabase("db")
                .withPort(2000)
                .withUser("root")
                .withPassword("passy")
                .create()
                .setMaximumPoolSize(20)
                .setMinimumIdle(2)
                .build();

        build = DataSourceCreator.create(DataSource.class)
                .withSettings(new DbConfig("localhost", "5432", "root", "passy", "db"))
                .create()
                .setMaximumPoolSize(20)
                .setMinimumIdle(2)
                .build();
    }

}