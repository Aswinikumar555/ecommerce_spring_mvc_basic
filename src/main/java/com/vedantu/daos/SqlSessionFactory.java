package com.vedantu.daos;

import com.mysql.jdbc.AbandonedConnectionCleanupThread;
import com.vedantu.models.Employee;
import com.vedantu.utils.LogFactory;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Set;
import javax.annotation.PreDestroy;
import javax.persistence.Entity;
import org.reflections.Reflections;

/*
https://stackoverflow.com/questions/11895205/hibernate-4-annotation-configuration
 */
@Service
public class SqlSessionFactory {

    @Autowired
    private LogFactory logFactory;

    @SuppressWarnings("static-access")
    private Logger logger = logFactory.getLogger(SqlSessionFactory.class);

    private SessionFactory sessionFactory = null;

    public SqlSessionFactory() {
        logger.info("initializing Sql Session Factory >>");
        try {
            Configuration configuration = new Configuration();
            String path = "ENV-LOCAL" + java.io.File.separator
                    + "hibernate.cfg.xml";
            configuration.configure(path);
            final Reflections reflections = new Reflections(Employee.class.getPackage().getName());
            final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Entity.class);
            for (final Class<?> clazz : classes) {
                logger.info("addAnnotatedClass " + "pkg: "
                        + clazz.getPackage() + ", class: " + clazz.getCanonicalName());
                configuration.addAnnotatedClass(clazz);
            }
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Exception e) {
            logger.error("error in creating sql connection ", e);
        }
    }

    /**
     * @return the session
     */
    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    @PreDestroy
    public void cleanUp() {
        try {
            if (sessionFactory != null) {
                sessionFactory.close();
            }
            Enumeration<Driver> drivers = DriverManager.getDrivers();

            Driver driver = null;

            // clear drivers
            while (drivers.hasMoreElements()) {
                try {
                    driver = drivers.nextElement();
                    logger.info("deregistering driver " + driver.getMajorVersion());
                    DriverManager.deregisterDriver(driver);
                } catch (SQLException ex) {
                    logger.error("exceoton in driver deregister " + ex.getMessage());
                }
            }
            // MySQL driver leaves around a thread. This static method cleans it up.
            AbandonedConnectionCleanupThread.shutdown();
            logger.info("cleaning done");
        } catch (Exception e) {
            logger.error("Error in closing sql connection ", e);
        }
    }
}
