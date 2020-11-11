package codebuilder.mybatis;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author maozhi.k
 * @date 2020/11/11 11:42 AM
 */
public interface DeleteRepository<T, U, K> {
    /**
     * 根据主键删除记录
     *
     * @param id id主键值
     * @return 影响的记录数
     */
    int deleteById(@Param("id") K id);

    /**
     * 根据条件删除记录
     *
     * @param example 查询Example条件参数
     * @return 影响的记录数
     */
    int deleteByExample(@Param("example") U example);

    /**
     * @param records
     * @return
     */
    int deleteIn(@Param("records") List<T> records);
}
