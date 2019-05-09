package test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;



public class DynamicLineChart extends Application implements Runnable {

    static  int i = 0;
    static XYChart.Series<String,Number> series2 = new XYChart.Series<String,Number>();

    @Override
    public void run() {
        double temp;
        while(true){
            try {

                Date date = new Date();
                String ab ;
                ab = String.format("%tT%n",date);
                System.out.println(ab);
                temp = Math.random()*100;
                series2.getData().add(new XYChart.Data(ab,temp )); //RandomNumber()数据库函数
                if(i == 8){
                    series2.getData().remove(0);
                    System.out.println("节点信息"+series2.getNode());
                    //i = 0;
                }
                if(i!= 8)i++;
                Thread.sleep(2*1000);
            }catch (InterruptedException e) {

            }catch (NullPointerException npe) {
                System.out.println("lineChart="+series2);
                System.out.println("lineChart.getData()="+series2.getData());
                throw npe ;
            }
        }
    }
    @Override
    public void start(Stage stage) {

        new Thread(new DynamicLineChart()).start();
        stage.setTitle("Line Chart Sample");

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis(0,100,10);
        xAxis.setLabel("时间");
        yAxis.setLabel("水势");
        LineChart<String,Number> lineChart = new LineChart(xAxis,yAxis);
//LineChart<String,Number> lineChart = new LineChart<String,Number>(xAxis,yAxis);
        lineChart.autosize();
        lineChart.alternativeColumnFillVisibleProperty();
        lineChart.setMaxWidth(5);
        String node01 = "节点1";
        series2.setName(node01);

        Scene scene  = new Scene(lineChart,800,600);
        lineChart.getData().addAll(series2);

        stage.setScene(scene);
        stage.show();

    }



    public static void main(String[] args) throws InterruptedException {
        //jdbc();
        launch(args);//

    }
}

