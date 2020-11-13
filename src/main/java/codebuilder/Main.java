package codebuilder;

import codebuilder.mysql.MysqlFactory;

/**
 * @author maozhi.k
 * @date 2020/11/13 2:53 PM
 */
public class Main {
    public static void main(String[] args){
        String path = "";
        if (args.length != 0){
            path = args[0];
        }
        VelocityFactory velocityFactory = new VelocityFactory(path);
        MysqlFactory.init(path);
        velocityFactory.generateTable();
    }
}
