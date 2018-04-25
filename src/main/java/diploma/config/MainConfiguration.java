package diploma.config;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MainConfiguration {

    private SessionFactory factory;

    @Bean(name="org.hibernate.SessionFactory")
    @Autowired
    public SessionFactory getSessionFactory() {
        if (factory==null) {
            factory = new org.hibernate.cfg.Configuration().configure().buildSessionFactory();
        }

        return factory;
    }
}