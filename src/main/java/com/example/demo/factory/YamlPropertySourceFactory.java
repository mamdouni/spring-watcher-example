package com.example.demo.factory;

import java.util.Objects;
import java.util.Properties;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.lang.Nullable;

public class YamlPropertySourceFactory implements PropertySourceFactory {

	@Override
	public PropertySource<?> createPropertySource(@Nullable String name, EncodedResource resource) {

		YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
		if(resource.getResource().exists()) {

			factory.setResources(resource.getResource());
			Properties properties = factory.getObject();
			assert properties != null;
			return new PropertiesPropertySource(Objects.requireNonNull(resource.getResource().getFilename()), properties);
		} else {

			return new PropertiesPropertySource(Objects.requireNonNull(resource.getResource().getFilename()), new Properties());
		}
	}
}
