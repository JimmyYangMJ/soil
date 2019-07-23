package javaFX;

import Port.PortText;
import com_receive.DataAD;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * AD数据表--窗口显示
 * @Date 2019-5-9
 */
public class ADTable {

//    /**
//     * 单例 端口（暂不实现）
//     * @Date 2019-5-9
//     */
//    public ADTable(ObservableList<DataAD> RealList){  //private 确保只能在类内部调用构造函数
//        this.RealList = RealList;
//        System.out.println("调用了table构造函数:端口对象实例化");
//    }

    private String data2; //基准电压
    static PortText port =PortText.getInstance();

    private static int ItemNumber = 0;
    public static int getItemNumber() {
        return ItemNumber;
    }

    public static void setItemNumber(int itemNumber) {
        ItemNumber = itemNumber;
    }

    private static ObservableList<DataAD> RealList = null;

    public static ObservableList<DataAD> getRealList() {
        return RealList;
    }

    public static void setRealList(ObservableList<DataAD> realList) {
        ADTable.RealList = realList;
    }

    private static ObservableList<DataAD> HistoryList = null;

    public static ObservableList<DataAD> getHistoryList() {
        return HistoryList;
    }

    public static void setHistoryList(ObservableList<DataAD> historyList) {
        HistoryList = historyList;
    }

    public void AddRealADTable(){
        System.out.println("******************");
        Date date = new Date();
        String date01;
        date01 = date.toString().substring(0,3)+ " "+date.toString().substring(8,10)+ "号"+date.toString().substring(11,16);
        //date01 = String.format("%tT%n",date);  //t的使用


        Double AD = Double.parseDouble(port.getMassage());
        AD = AD/4096*3.3;
        String ad =  String.format("%.3f", AD); //保留三位小数

        Double baseADDouble = Double.parseDouble(port.getBaseAD());
        baseADDouble = baseADDouble/4096*3.3;
        String baseADString =  String.format("%.3f", baseADDouble); //保留三位小数

        DataAD dataAD = new DataAD();
        dataAD.setNode(port.getNode());
        dataAD.setAD(ad);
        dataAD.setBaseAD(baseADString);
        dataAD.setTime(date01);

        RealList.add(dataAD);
        ItemNumber++;
        System.out.println("执行了添加数据");

    }

    public void AddHistoryADTable(ResultSet rs){
        DataAD dataAD = new DataAD();
        try {
            String data;
            while(rs.next()) {
                data = rs.getString("information_data");
                String[] b = data.split(";", 3);

                dataAD.setNode(b[0]);
                dataAD.setAD(b[1]);
                dataAD.setBaseAD(b[2]);
                dataAD.setTime(rs.getString("update_time"));
                HistoryList.add(dataAD);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
