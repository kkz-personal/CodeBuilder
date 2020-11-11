package codebuilder.dao;

import codebuilder.commons.CrudRepository;
import codebuilder.bo.Books;
import codebuilder.bo.example.BooksExample;
import org.springframework.stereotype.Repository;

/**
 * 数据访问类
 *
 * @author codeBuilder
 * @date 2020/11/11 12:04:17
 */
@Repository
public interface BooksRepository
        extends CrudRepository<Books, BooksExample, Integer> {
}