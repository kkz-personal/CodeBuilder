package codebuilder.commons;

import codebuilder.mybatis.DeleteRepository;
import codebuilder.mybatis.InsertRepository;
import codebuilder.mybatis.SelectRepository;
import codebuilder.mybatis.UpdateRepository;

/**
 * 基本增删改查(CRUD)数据访问接口
 *
 * @param <T> Po
 * @param <U> Example
 * @param <K> Key字段数据类型(Integer,Long,String等)
 * @author maozhi.k
 * @date 2020/11/11 11:39 AM
 */
public interface CrudRepository <T, U, K> extends
        InsertRepository<T>,
        DeleteRepository<T, U, K>,
        UpdateRepository<T, U>,
        SelectRepository<T, U, K> {
}
