package org.example;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class GeneratorUtils {

    private static void generator(String name) throws Exception {
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        // 指定 逆向工程配置文件
        File configFile = new File("mybatis-generator-database"
                + File.separator
                + "generatorConfig-" + name + ".xml");
        Configuration config = new ConfigurationParser(warnings).parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        new MyBatisGenerator(config, callback, warnings).generate(null);
    }

    public static void main(String[] args) throws Exception {
        // generator("user");
        // generator("admin");
        generator("article");
    }
}
