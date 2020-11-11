package codebuilder;

import codebuilder.mysql.Column;
import codebuilder.mysql.MysqlFactory;
import codebuilder.mysql.Table;
import lombok.extern.log4j.Log4j;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static jdk.nashorn.internal.codegen.OptimisticTypesPersistence.load;

/**
 * @author maozhi.k
 * @date 2020/11/3 6:07 PM
 */
@Log4j
public class VelocityFactory {
    private static Properties properties;
    private static String templateConfig = "src/main/resources/templateconfig.properties";

    static {
        //配置velocity的资源加载路径
        Properties velocityPros = new Properties();
        velocityPros.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityPros.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        velocityPros.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
        velocityPros.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
        velocityPros.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
        Velocity.init(velocityPros);

        try {
            properties = new Properties();
            properties.load(new FileInputStream(templateConfig));
        } catch (IOException e) {
            throw new RuntimeException("load template config error", e);
        }
    }

    /**
     * 根据模板生成JavaBean文件内容
     *
     * @param packageName 包名
     * @param className   类名
     * @param columnList  表字段集合
     * @return
     */
    static String getVmContent(Table table, String packageName, String className, List<Column> columnList, String templateFiel) throws IOException {

        TemplateDto dto = new TemplateDto();
        dto.setAuthor("codeBuilder");
        dto.setDate(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        dto.setModelContent(table.getComment());
        dto.setPackageName(packageName);
        dto.setPrefix("");
        dto.setSuffix("");
        dto.setName(className);
        dto.setTableName(table.getTableName());
        dto.setColumns(columnList);
        //绑定velocity数据
        VelocityContext context = new VelocityContext();
        //实体类分包 com.openxu.bean
        context.put("tdo", dto);
        //根据模板生成java文件内容
        Template template = Velocity.getTemplate(templateFiel);
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        writer.flush();
        writer.close();
        return writer.toString();
    }

    private void generateJavaBean(Table table, List<String> classIgnorePrefix) {
        try {
            String templateFile = properties.getProperty("pojo.template");
            String suffix = properties.getProperty("suffix.pojo");
            String outPath = properties.getProperty("outPath.pojo");
            writeFile(outPath, table, templateFile, suffix, classIgnorePrefix);
        } catch (IOException e) {
            log.error("generateJavaBean error", e);
            e.printStackTrace();
        }
    }

    private void generateBeanExample(Table table, List<String> classIgnorePrefix) {
        try {
            String templateFile = properties.getProperty("mybatis-example.template");
            String suffix = properties.getProperty("suffix.example");
            String outPath = properties.getProperty("outPath.example");
            writeFile(outPath, table, templateFile, suffix, classIgnorePrefix);
        } catch (IOException e) {
            log.error("generateBeanExample error", e);
            e.printStackTrace();
        }
    }

    private void generateMapper(Table table, List<String> classIgnorePrefix) {
        try {
            String templateFile = properties.getProperty("mybatis-mapper.template");
            String suffix = properties.getProperty("suffix.mapper");
            String outPath = properties.getProperty("outPath.mapper");
            writeFile(outPath, table, templateFile, suffix, classIgnorePrefix);
        } catch (IOException e) {
            log.error("generateMapper error", e);
            e.printStackTrace();
        }
    }

    private void generateShardingMapper(Table table, List<String> classIgnorePrefix) {
        try {
            String templateFile = properties.getProperty("mybatis-sharding-mapper.template");
            String suffix = properties.getProperty("suffix.mapper");
            String outPath = properties.getProperty("outPath.sharding-mapper");
            writeFile(outPath, table, templateFile, suffix, classIgnorePrefix);
        } catch (IOException e) {
            log.error("generateMapper error", e);
            e.printStackTrace();
        }
    }

    private void generateRepository(Table table, List<String> classIgnorePrefix) {
        try {
            String templateFile = properties.getProperty("mybatis-repository.template");
            String outPath = properties.getProperty("outPath.repository");
            String suffix = properties.getProperty("suffix.repository");
            writeFile(outPath, table, templateFile, suffix, classIgnorePrefix);
        } catch (IOException e) {
            log.error("generateRepository error", e);
            e.printStackTrace();
        }
    }

    private void generateShardingRepository(Table table, List<String> classIgnorePrefix) {
        try {
            String templateFile = properties.getProperty("mybatis-sharding-repository.template");
            String outPath = properties.getProperty("outPath.sharding-repository");
            String suffix = properties.getProperty("suffix.repository");
            writeFile(outPath, table, templateFile, suffix, classIgnorePrefix);
        } catch (IOException e) {
            log.error("generateRepository error", e);
            e.printStackTrace();
        }
    }

    public void generateTable() {
        String ignorePrefix = properties.getProperty("ignore.prefix");
        MysqlFactory factory = MysqlFactory.getInstance();
        List<String> ignorePrefixList = Arrays.asList(ignorePrefix.split(","));
        String tableNames = properties.getProperty("generate.tables");
        for (String tableName : Arrays.asList(tableNames.split(","))) {
            Table table = factory.getTableByName(tableName);

            if (Boolean.valueOf(properties.getProperty("generate.pojo", "false"))) {
                this.generateJavaBean(table, ignorePrefixList);
            }
            if (Boolean.valueOf(properties.getProperty("generate.example", "false"))) {
                this.generateBeanExample(table, ignorePrefixList);
            }
            if (Boolean.valueOf(properties.getProperty("generate.mapper", "false"))) {
                this.generateMapper(table, ignorePrefixList);
            }
            if (Boolean.valueOf(properties.getProperty("generate.repository", "false"))) {
                this.generateRepository(table, ignorePrefixList);
            }
            if (Boolean.valueOf(properties.getProperty("generate.sharding-mapper", "false"))) {
                this.generateShardingMapper(table, ignorePrefixList);
            }
            if (Boolean.valueOf(properties.getProperty("generate.sharding-repository", "false"))) {
                this.generateShardingRepository(table, ignorePrefixList);
            }

        }

    }

    /**
     * 根据Mysql表及字段集合，通过Velocity模板自动生成实体类代码，写入对应类文件中
     *
     * @param filePath 实体类存放路径
     * @param table    表
     */
    void writeFile(String filePath, Table table, String templateFile, String suffix, List<String> classIgnorePrefix) throws IOException {
        //获取实体类名
        String className = getClassName(table.getTableName(), classIgnorePrefix);
        //获取文件内容
        String packageName = properties.getProperty("packageName");
        String classContent = VelocityFactory.getVmContent(table, packageName, className, table.getColumns(), templateFile);
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, className + suffix);
        java.io.FileWriter writer = null;
        try {
            writer = new java.io.FileWriter(file);
            writer.write(classContent);
        } catch (IOException e) {
            throw new RuntimeException("Error creating file " + className, e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private String getClassName(String tableName, List<String> prefixList) {
        String name = tableName;
        StringBuilder className = new StringBuilder();
        for (String prefix : prefixList) {
            if (tableName.startsWith(prefix)) {
                name = name.substring(prefix.length(), tableName.length());
                break;
            }
        }
        for (String string : Arrays.asList(name.split("_"))) {
            char[] strChar = string.toCharArray();
            strChar[0] -= 32;
            className.append(String.valueOf(strChar));
        }
        return className.toString();
    }
}
