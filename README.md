# CodeBuilder

## 描述
基于Velocity模板引擎的Java版 codebuilder

## 使用
- 配置配置文件，指定数据库（datasource.properties文件）、模板配置(templateconfig.properties)
- 执行VelocityFactory的方法

```
 VelocityFactory factory = new VelocityFactory();
        factory.generateTable();
```

