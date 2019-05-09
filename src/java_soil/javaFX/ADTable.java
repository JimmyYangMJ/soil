package javaFX;

import Port.PortText;
import com_receive.DataAD;
import javafx.collections.ObservableList;

import java.util.Date;

public class ADTable {
    /**
     * 单例 端口
     */
    private static ObservableList<DataAD> list;
    public ADTable(ObservableList<DataAD> list){  //private 确保只能在类内部调用构造函数
        this.list = list;
        System.out.println("调用了table构造函数:端口对象实例化");
    }

    private String data2; //基准电压
    static PortText port =PortText.getInstance();

    public  void  AddADTable(){
        System.out.println("666666666666666");
        Date date = new Date();
        String date01;
        date01 = date.toString().substring(0,3)+ " "+date.toString().substring(8,10)+ "号"+date.toString().substring(11,16);
        //date01 = String.format("%tT%n",date);  //t的使用


        Double AD = Double.parseDouble(port.getMassage());
        AD = AD/4096*3.3;
        String ad =  String.format("%.3f", AD); //保留三位小数

        Double baseAD = Double.parseDouble(port.getBaseAD());
        baseAD = baseAD/4096*3.3;
        String BAD =  String.format("%.3f", baseAD); //保留三位小数

        DataAD dataAD = new DataAD();
        dataAD.setNode(port.getNode());
        dataAD.setAD(ad);
        dataAD.setBaseAD(BAD);
        dataAD.setTime(date01);

        list.addAll(dataAD);
    }


}
