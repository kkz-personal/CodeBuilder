package codebuilder.mysql;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.dom4j.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author maozhi.k
 * @date 2020/11/4 11:23 AM
 */
@Data
@NoArgsConstructor
public class Column {
    /**
     * 实体类属性名
     */
    private String fieldName;
    /**
     * 数据库字段名
     */
    private String databaseFieldName;
    /**
     * 用于方法的名
     */
    private String methodFieldName;
    /**
     * 实体类属性类型
     */
    private String fieldType;
    /**
     * 全称
     */
    private String fieldTypeAlias;
    /**
     * 数据库类型
     */
    private String jdbcType;
    /**
     * 备注
     */
    private String comment;
    /**
     * 是否自增长
     */
    private boolean autoIncremented;
    /**
     * 是否是主键
     */
    private boolean primaryKey;

    Column(String mysqlColumnName, String columnType, String comment, boolean autoIncremented, boolean primaryKey) {
        this.fieldName = transformColumnName(mysqlColumnName);
        this.databaseFieldName = mysqlColumnName;
        char[] fieldNameChars = this.fieldName.toCharArray();
        fieldNameChars[0] -= 32;
        this.methodFieldName = String.valueOf(fieldNameChars);
        this.fieldType = transformColumnType(columnType);
        this.comment = comment;
        this.autoIncremented = autoIncremented;
        this.primaryKey = primaryKey;
    }

    private String transformColumnName(String mysqlColumnName) {
        List<String> fields = new ArrayList<>(Arrays.asList(mysqlColumnName.split("_")));
        StringBuilder stringBuilder = new StringBuilder(fields.get(0));
        for (int i = 1; i < fields.size(); i++) {
            char[] sChar = fields.get(i).toCharArray();
            sChar[0] -= 32;
            stringBuilder.append(String.valueOf(sChar));
        }
        return stringBuilder.toString();
    }

    String transformColumnType(String columnType) {
        List<Element> list = MysqlFactory.getInstance().getTypeMapper();
        for (Element element : list) {
            String type = element.attributeValue("name");
            if (columnType.startsWith(type)) {
                this.jdbcType = type.equalsIgnoreCase("int") ? "INTEGER" : type.toUpperCase();
                this.fieldTypeAlias = element.attributeValue("alias");
                return element.attributeValue("langtype");
            }
        }
        return columnType;
    }
}