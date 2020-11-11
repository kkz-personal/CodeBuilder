package codebuilder.bo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

import java.io.Serializable;
import java.util.Date;
import java.sql.Time;

/**
* 
* @author codeBuilder
* @date 2020/11/11 12:04:17
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Books {
/**
* 
*/
private Integer id;
/**
* 
*/
private Double price;
/**
* 
*/
private String name;
/**
* 
*/
private String description;
/**
* 
*/
private Date modifyDate;
/**
* 
*/
private Date createDate;
/**
* 
*/
private String worker;
}