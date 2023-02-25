package cn.edu.hutb.api.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 请求路径url中的参数进行时间日期类型的转换，字符串->日期Date
 */
@Configuration
public class DateConverterConfig implements Converter<String, Date> {

    private static final List<String> FORMATTER_LIST = new ArrayList<>(4);

    static {
        FORMATTER_LIST.add("yyyy-MM");
        FORMATTER_LIST.add("yyyy-MM-dd");
        FORMATTER_LIST.add("yyyy-MM-dd HH:mm");
        FORMATTER_LIST.add("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public Date convert(String source) {
        source = source.trim();
        if (source.matches("^\\d{4}-\\d{2}$")) {
            return parseDate(source, FORMATTER_LIST.get(0));
        } else if (source.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            return parseDate(source, FORMATTER_LIST.get(1));
        } else if (source.matches("^\\d{4}-\\d{2}-\\d{1,2} \\d{2}:\\d{2}$")) {
            return parseDate(source, FORMATTER_LIST.get(2));
        } else if (source.matches("^\\d{4}-\\d{2}-\\d{1,2} \\d{2}:\\d{2}:\\d{2}$")) {
            return parseDate(source, FORMATTER_LIST.get(3));
        } else {
            return null;
        }
    }

    /**
     * 日期转换方法
     */
    private Date parseDate(String dateStr, String formatter) {
        DateFormat dateFormat = new SimpleDateFormat(formatter);
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }
}
