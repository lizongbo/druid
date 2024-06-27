module com.alibaba.druid.admin {
    requires java.sql;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.web;
    exports com.alibaba.druid.admin.config;
}
