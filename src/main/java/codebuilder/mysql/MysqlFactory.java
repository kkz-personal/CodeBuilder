package codebuilder.mysql;

import lombok.extern.log4j.Log4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author maozhi.k
 * @date 2020/11/4 11:13 AM
 */
@Log4j
public class MysqlFactory {
    private Connection connection;
    private String dbName;
    private static MysqlFactory helper;
    private static String driverClass;
    private static String url;
    private static String userName;
    private static String passWord;
    private static String database;
    private static String typeMapping;
    private static String databaseType;
    private static String language;
    private static String mapperDatabaseType;
    private static String mapperLanguage;
    private static String mapperTypeMapping;

    public static void init(String path) {
        try {
            Properties properties = new Properties();
//            DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();
//            Resource resource = defaultResourceLoader.getResource("datasource.properties");
//            properties.load(resource.getInputStream());
            properties.load(new FileInputStream(path + "datasource.properties"));
            driverClass = properties.getProperty("jdbc.driverClassName");
            url = properties.getProperty("jdbc.url");
            userName = properties.getProperty("jdbc.username");
            passWord = properties.getProperty("jdbc.password");
            database = properties.getProperty("jdbc.database");

            databaseType = properties.getProperty("database.type");
            typeMapping = properties.getProperty("typeMapping");
            language = properties.getProperty("language");

            mapperDatabaseType = properties.getProperty("mapper.database.type");
            mapperLanguage = properties.getProperty("mapper.language");
            mapperTypeMapping = properties.getProperty("mapper.typeMapping");
            ;
        } catch (IOException e) {
            log.error("load database properties error.", e);
            e.printStackTrace();
        }
    }

    private MysqlFactory(String url, String user, String pwd, String dbName) {
        this.dbName = dbName;
        try {
            Class.forName(driverClass);
            connection = DriverManager.getConnection(url, user, pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MysqlFactory getInstance() {
        if (helper == null) {
            helper = new MysqlFactory(url, userName, passWord, database);
        }
        return helper;
    }

    /**
     * 查询mysql中所有的表
     *
     * @return
     */
    private List<Table> getAllTable() {
        List<Table> tables = new ArrayList<>();
        String sqlTables = String.format("select TABLE_NAME,TABLE_COMMENT from information_schema.tables where table_schema='%s'", dbName);
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlTables);
            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                List<Column> columnList = helper.getTableColumns(tableName);
                tables.add(new Table(tableName, resultSet.getString("TABLE_COMMENT"), columnList));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }

    public Table getTableByName(String tableName) {
        for (Table table : getAllTable()) {
            if (tableName.equalsIgnoreCase(table.getTableName())) {
                return table;
            }
        }
        throw new RuntimeException(tableName + " not found");
    }

    /**
     * 查询指定表的字段集合
     *
     * @param tableName
     * @return
     */
    private List<Column> getTableColumns(String tableName) {
        String sqlGetCloumns = String.format("select COLUMN_NAME, COLUMN_TYPE, COLUMN_KEY, COLUMN_COMMENT, COLUMN_KEY, EXTRA from information_schema.columns" +
                " where table_schema='%s' and table_name='%s'", dbName, tableName);
        List<Column> columnList = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlGetCloumns);
            while (resultSet.next()) {
                columnList.add(new Column(
                        resultSet.getString("COLUMN_NAME"),
                        resultSet.getString("COLUMN_TYPE"),
                        resultSet.getString("COLUMN_COMMENT"),
                        resultSet.getString("EXTRA").contains("auto_increment"),
                        "PRI".equals(resultSet.getString("COLUMN_KEY"))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columnList;
    }

    public List<Element> getTypeMapper() {
        SAXReader reader = new SAXReader();
        String file = "src/main/resources/TypeMapping.xml";
        try {
            Document document = reader.read(file);
            List<Element> elems = document.selectNodes("/typeMappingSection/typeMappings/typeMapping");
            for (Element element : elems) {
                if (typeMapping.equals(element.attributeValue("name")) && databaseType.equals(element.attributeValue("database"))
                        && language.equals(element.attributeValue("language"))) {
                    return element.elements();
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}