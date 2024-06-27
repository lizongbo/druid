module com.alibaba.druid.demo.petclinic {
    requires java.sql;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.data.jpa;
    requires spring.web;
    exports com.alibaba.druid.demo.petclinic;
}
