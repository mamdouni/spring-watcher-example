package com.example.demo;

import com.example.demo.factory.YamlPropertySourceFactory;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;

@Configuration
@ConfigurationProperties(prefix = "metadata")
@PropertySource(value = "classpath:acl-default.yaml", factory = YamlPropertySourceFactory.class)
@PropertySource(value = "file:${user.home}/acls/acl.yaml", factory = YamlPropertySourceFactory.class, ignoreResourceNotFound = true)
@Slf4j
@Getter
@Setter
public class ACLConfig implements InitializingBean {

    private List<User> users;

    @Override
    public void afterPropertiesSet() {

        if (users==null)
            throw new IllegalArgumentException("ACL configuration is not found");
        showUsers();
    }

    @EventListener({
            RefreshScopeRefreshedEvent.class
    })
    public void onEventRefresh() {

        // RefreshScopeRefreshedEvent : will be triggered when calling the /refresh endpoint
        System.out.println("Calling the refresh endpoint for users");
        showUsers();
    }

    public void showUsers() {
        if(log.isDebugEnabled()) {
            log.debug("The ACLs of our users are : ");
            users.forEach(u -> log.debug("{}", u));
        }
    }

    @Getter
    @Setter
    @ToString
    public static class User {
        private String name;
        private String password;
    }
}
