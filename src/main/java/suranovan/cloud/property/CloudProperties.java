package suranovan.cloud.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Properties;

@Component
public class CloudProperties {
    static Properties properties;

    @Value("${servers[0].url}")

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("cloudSpecification.yml"));
        propertySourcesPlaceholderConfigurer.setProperties(yaml.getObject());
        properties = yaml.getObject();
        return propertySourcesPlaceholderConfigurer;
    }

//    @Override
//    public void run(String... args) throws Exception {
//        var arr = properties.toString().split(", ");
//        StringBuilder stringBuilder = new StringBuilder();
//        for (String s : arr){
//            if (s.startsWith("paths./login")){
//                stringBuilder.append(s).append("\n");
//            }
//        }
//        System.out.println(stringBuilder.toString());
//    }
/*
    2-й вариант перевести свойства yml файла но требует анотацию над классом
    @PropertySource(value = "classpath:cloudSpecification.yml", factory = CloudProperties.class)
 */
//    @Override
//    public PropertySource<?> createPropertySource(String s, EncodedResource encodedResource) throws IOException {
//        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
//        factory.setResources(encodedResource.getResource());
//
//        Properties properties = factory.getObject();
//
//        return new PropertiesPropertySource(encodedResource.getResource().getFilename(), properties);
//    }
}
