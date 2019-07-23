package com_receive;

import SQL.MySql_operate;
import javaFX.LogTextArea;

import java.sql.*;

/**
 * 数据Dao层--数据库操作
 */
public class DataDao {

    private static boolean con1 = false;
    static Connection connection;

    /**
     * 数据库是否连接
     * @throws SQLException
     */
    public static void isDBConnnect() throws SQLException {
        if(con1 == false){
            connection = MySql_operate.JDBC_connect();
            if(connection.isClosed())
                LogTextArea.log("<---数据库连接失败--->");
            else
                LogTextArea.log("<---数据库连接成功-->");
            con1 = true;
        }
    }

    /**
     * 存入数据库操作-土壤水势
     */
    public static void insertSoil(String dataSoil ) throws SQLException {
        isDBConnnect(); //判断数据库是否连接

        String[] b = dataSoil.split(";", 3);
        String sql = "insert into soil_water (id, node, humidity) values (?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setInt(1,DataCounter("soil_water")+1);
        preparedStatement.setNString(2, b[0]);
        preparedStatement.setInt(3, Integer.parseInt(b[1]));
        //preparedStatement.setNString(4, );

        preparedStatement.execute();
    }

    /**
     * 存入数据库操作-AD
     */
    public static void insertAD(DataBean data)throws SQLException {

        isDBConnnect(); //判断数据库是否连接

        String sql = "insert into soil_AD (information_id,information_data) values (null,?)";

        String dataAD = null;
        dataAD = data.getData();
        // 写入数据库 数值
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setNString(1, dataAD);
        preparedStatement.execute();

        insertSoil(dataAD); //同时存水势数据
    }

    /**
     * 查询数据表共多少数据
     * @return
     */
    public static int DataCounter(String tableName) throws SQLException {
        if(con1 == false){
            connection = MySql_operate.JDBC_connect();
            if(connection.isClosed())
                System.out.println("<---数据库连接失败--->");
            else
                System.out.println("<---数据库连接成功-->");
            con1 = true;
        }
        int count = 0;
        PreparedStatement preparedStatement = null;
        String sql = "select count(*) number from ";
        sql += tableName;
        try {
            preparedStatement = connection.prepareStatement(sql);
            //preparedStatement.setNString(1, tableName );

            ResultSet rs = preparedStatement.executeQuery();

            System.out.println("成功查询数据库");

            if(rs.next()){
                count = rs.getInt("number");
            }
            return count;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 查询具体某一天数据
     * @param time
     * @return ResultSet
     * @throws SQLException
     */
    public static ResultSet ReadData(String time) throws SQLException {

        isDBConnnect(); //判断数据库是否连接

        PreparedStatement preparedStatement = null;
        String sql = "select * from soil_AD where update_time like  ? ";

        try {
            time = time + '%';
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, time );
           // preparedStatement.setString(2, Date.valueOf(time));
            ResultSet rs = preparedStatement.executeQuery();

            System.out.println("成功查询数据库");
//
            return rs;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static void main(String[] args) {
//        int n = 0;
//        try {
//            n = DataCounter("soil_water");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        System.out.println("+++++++++++" + n);
//    }
}

