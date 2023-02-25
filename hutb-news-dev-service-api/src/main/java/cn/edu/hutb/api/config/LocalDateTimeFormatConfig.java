package cn.edu.hutb.api.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author 田章
 * @description LocalDateTime 格式化配置类，解决前后端交互日期格式问题。
 * @date 2022/12/23
 */
@Configuration
public class LocalDateTimeFormatConfig {

    /* 当@Configruation、@Bean和@Value出现在同一个类中时，@Bean会比@Value先执行 */

    private final DateTimeFormatter FORMATTER;

    {
        String pattern;
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("jackson");
            pattern = bundle.getString("localDateTime.format");
        } catch (MissingResourceException e) {
            // 如果类路径下没有 jackson.properties 配置文件，或者配置文件中没有 localDateTime.format 值
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        FORMATTER = DateTimeFormatter.ofPattern(pattern);
    }

    /**
     * 自定义 Formatter，将前端请求的 String 类型转为 LocalDateTime 类型。
     * 另外，也可以选择自定义 {@link org.springframework.core.convert.converter.Converter}
     */
    @Bean
    public Formatter<LocalDateTime> localDateTimeFormatter() {
        return new Formatter<LocalDateTime>() {
            @Override
            public LocalDateTime parse(String text, Locale locale) throws ParseException {
                return LocalDateTime.parse(text, FORMATTER);
            }

            @Override
            public String print(LocalDateTime object, Locale locale) {
                return FORMATTER.format(object);
            }
        };
    }

    /**
     * 自定义 LocalDateTime 类型与 json 格式之间的转换。
     * 另外也可以使用 {@link com.fasterxml.jackson.databind.ObjectMapper}(需结合@Primary)
     * 或者 {@link org.springframework.http.converter.json.Jackson2ObjectMapperBuilder}
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer localDateTimeCustomizer() {
        return builder -> builder
                .serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(FORMATTER))
                .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(FORMATTER));
    }

}