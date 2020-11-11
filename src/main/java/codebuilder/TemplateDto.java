package codebuilder;

import codebuilder.mysql.Column;
import lombok.Data;

import java.util.List;

/**
 * @author maozhi.k
 * @date 2020/11/4 2:57 PM
 */
@Data
public class TemplateDto {
    private String date;
    private String packageName;
    private String modelContent;
    private String author;
    private String name;
    private String prefix;
    private String suffix;

    private String tableName;
    private List<Column> columns;
}
