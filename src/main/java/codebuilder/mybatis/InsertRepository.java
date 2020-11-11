package codebuilder.mybatis;


import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * @author maozhi.k
 * @date 2020/11/11 11:41 AM
 */
public interface InsertRepository<T> {
    /**
     * 插入一条数据，忽略record中的ID
     *
     * @param record
     * @return 影响的记录数
     */
    int insert(@Param("record") T record);

    /**
     * @param records
     * @return 影响的记录数
     */
    int batchInsert(@Param("records") List<T> records);

    /**
     * 使用mysql on duplicate key 语句插入与修改
     *
     * @param records pojo记录集
     * @return 影响的记录数
     */
    int batchInsertOnDuplicateKey(@Param("records") List<T> records);
}
