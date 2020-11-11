package codebuilder.mysql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author maozhi.k
 * @date 2020/11/9 3:32 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Table {
    private String tableName;
    private String comment;
    /**
     * 字段
     */
    private List<Column> columns;
}
