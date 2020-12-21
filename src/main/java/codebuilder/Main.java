package codebuilder;

import codebuilder.mysql.MysqlFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author maozhi.k
 * @date 2020/11/11 11:57 AM
 */
public class Main {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String path = "";
        if (args.length != 0){
            path = args[0];
        }
        path = URLDecoder.decode(path,"utf-8");
        VelocityFactory factory = new VelocityFactory(path);
        MysqlFactory.init(path);
        factory.generateTable();
    }
}
