package javaFX;

import javafx.scene.chart.XYChart;

import java.util.Date;

import static javaFX.com_FX.adTable;


public class java_FX_Chart implements Runnable {
    /**
     * 实时接收端口数据，用折线图表示
     *
     */
    private int i = 0;
    static XYChart.Series<String,Number> series1 = new XYChart.Series<String,Number>();
    private String firstMassage =null ;
    @Override
    public void run() {

        series1.getData().add(new XYChart.Data( " ", 50));//第一个点
        while(true){
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e) {

            }
            if(com_FX.port.getMassageState() == false){
                continue;
            }
            Date date = new Date();
            String time ;
            time = String.format("%tT%n",date);

            firstMassage = com_FX.port.getMassage(); // firstMassage为AD的值
            Double ADtest= Double.parseDouble(firstMassage);
            Double soiltest = 0.0;

            ADtest = ADtest/4096*3.3;


            Double soil = Double.parseDouble(com_FX.port.getBaseAD());
            soiltest = ADtest + 1;

            adTable.AddADTable();
            System.out.println("最近一次接收 AD:" + ADtest + " soil:" + soil);


            try {
                series1.getData().add(new XYChart.Data(time, soiltest));//实时接收
            }catch (Exception e){

            }
            if(i == 10){
                series1.getData().remove(0);
                System.out.println("节点信息"+ series1.getNode());
            }
            if(i!= 10)i++;

            com_FX.port.setMassageState(false); //
        }
    }

    public XYChart.Series<String,Number> getSeries2(){
        return this.series1;
    }

}