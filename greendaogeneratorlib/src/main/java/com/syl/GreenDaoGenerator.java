package com.syl;


import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/*
 * PACKAGE_NAME :com.syl
 * VERSION :[V 1.0.0]
 * AUTHOR :  yulongsun 
 * CREATE AT : 7/30/2015 11:26 AM
 * COPYRIGHT : InSigma HengTian Software Ltd.
 * E-MAIL: yulongsun@hengtiansoft.com
 * NOTE : 需要编译 compile 'de.greenrobot:greendao-generator:1.3.1'
 */

public class GreenDaoGenerator {

    public static void main(String[] args ){
        try {
        //1.params1:db_version
//          params2:自动生成代码的包路径
        Schema schema = new Schema(1, "com.syl.dao");

//        schema.setDefaultJavaPackageTest();
//        schema.setDefaultJavaPackageDao();
        //2.add Entity
        addUser(schema);

        //3.参数2：生成代码的路径：即前面设置的java-gen
        new DaoGenerator().generateAll(schema,"app/src/main/java-gen");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**添加用户实体
     * @param schema
     */
    private static void addUser(Schema schema) {
        //1.add tableName 表名即类名[User]
        Entity user = schema.addEntity("User");
        //2.add property
        user.addIdProperty();
        user.addStringProperty("name").notNull();
        user.addStringProperty("mobile");
        user.addDateProperty("createAt");
    }

}
