package com_receive;

import SQL.MySql_operate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataDao {

    private static boolean con1 = false;
    static Connection connection;
    /**
     * 存入数据库操作
     */
    public static void insert(DataBean data)throws SQLException {
        if(con1 == false){
            connection = MySql_operate.JDBC_connect();
            con1 = true;
        }

        String sql = "insert into soilProjectData (information_id,information_data) values (null,?)";

        // 写入数据库 数值
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setNString(1,data.getData());
        preparedStatement.execute();
    }
}

