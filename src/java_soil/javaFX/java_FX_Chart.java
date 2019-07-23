package javaFX;

import javafx.scene.chart.XYChart;

import java.util.Date;

import static javaFX.com_FX.RealADTable;

/**
 * 实时接收端口数据，用折线图表示 javaFX
 */
public class java_FX_Chart implements Runnable {

    private int i = 0;

//    static XYChart.Series<String,Number> series1 = new XYChart.Series<String,Number>();
//    static XYChart.Series<String,Number> series2 = new XYChart.Series<String,Number>();
    static XYChart.Series[] seriesArray = {new XYChart.Series<String,Number>(),
        new XYChart.Series<String,Number>(),new XYChart.Series<String,Number>()};

    private String series = "";

    private String firstMassage = null ;

    private String node = null;

    private int SHOW_NUMBER = 15; //一张图显示 SHOW_NUMBER 个节点

    @Override
    public void run() {

        //series1.getData().add(new XYChart.Data( " ", 50));//第一个点 初始化
        while(true){
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e) {

            }
            if(com_FX.port.getMassageState() == false){
                continue;
            }
            Date date = new Date();
            String time = String.format("%tT%n",date);;

            firstMassage = com_FX.port.getMassage(); // firstMassage为AD的值

            node = com_FX.port.getNode();


            Double ADtest = Double.parseDouble(firstMassage);
            Double soiltest = 0.0;

            ADtest = ADtest/4096*3.3;

            series = "series" + node;  //节点值
            System.out.println(series);

            Double soil = Double.parseDouble(com_FX.port.getBaseAD());
            soiltest = ADtest + 1;

            RealADTable.AddRealADTable(); //向表格添加数据
            System.out.println("最近一次接收 AD:" + ADtest + " soil:" + soil);

            int nodes = Integer.parseInt(node);

            try {
                 seriesArray[nodes].getData().add(new XYChart.Data(time, soiltest));//实时接收
            }catch (Exception e){

            }

            if(i == SHOW_NUMBER){
                    seriesArray[nodes].getData().remove(0);
                System.out.println("节点信息"+  seriesArray[nodes].getNode());
            }
            if(i!= SHOW_NUMBER)i++;

            com_FX.port.setMassageState(false); //
        }
    }

//    public XYChart.Series<String,Number> getSeries2(){
//        return this. seriesArray[nodes];
//    }

}