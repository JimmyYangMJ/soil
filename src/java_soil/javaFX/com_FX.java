package javaFX;

import SQL.MySql_operate;
import com_receive.DataAD;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

import Port.PortText;
import jfreechart.JfreeLineChart;
import org.jfree.chart.ChartPanel;

import static javaFX.java_FX_Chart.series1;

/**
 * @author YMj
 * @date 2019/5/4
 */
public class com_FX extends Application { //implements Runnable

    static PortText port =PortText.getInstance(); //实现端口方法
    private BorderPane bp = new BorderPane(); //总体布局
    private SwingNode node=new SwingNode(); //建一个SwingNode组件

    static TextArea textArea = new TextArea(); //文本框
    TextField comSentText = new TextField();

    static Thread ChartThear = new Thread(new JfreeLineChart(JfreeLineChart.getJFreeChart())); //JfreeLineChart
    static TableView<DataAD> tableView; //AD 表

    Button checkPort, choiceCom, startReceive,closeCom; //端口
    Button ChoiceTime, comSent;

    VBox vBox;

    CategoryAxis xAxis;
    NumberAxis yAxis;
    LineChart<String,Number> lineChart;

    static ADTable adTable ;
    ObservableList<DataAD> list;

    public com_FX() {
        super();
    }

    /**
     * 输出文本框消息
     * @param st
     */
    public void log(String st){
        System.out.println(st);
        LogTextArea.log(st);//文本框消息
    }

    /**
     * 接收端口选择 VBComChoose()
     * @return VBox
     */
    public VBox VBComChoose(){
        vBox = new VBox(5);

        Text text = new Text(2,20,"选择接收端口");    //第一行 提示文本

        ChoiceBox<String> com_choice = new ChoiceBox<>();        //第三行 选择接收端口
        /*com选择框初始化*/
        com_choice.getItems().addAll("COM1", "COM2","COM10");
        com_choice.setValue("COM1");                                  //默认值
        com_choice.setTooltip(new Tooltip("选择接收端口"));       //提示框

        choiceCom = new Button("确定端口");                 //第三行 确定端口
        choiceCom.setOnAction(e -> port.selectPort(com_choice));//选择框监听

        vBox.setPadding(new Insets(0, 20, 20, 2));
        vBox.getChildren().addAll(text,com_choice, choiceCom);
        return vBox;
    }//接收端口选择

    /**
     * 相关结点回传数据 VBComBackNode()
     * @return VBox
     */
    public VBox VBComBackNode(){
        vBox = new VBox(5);

        Text text = new Text(2,20,"选择节点回传数据"); //提示文本

        comSent = new Button("回传数据"); //回传数据
        comSent.setOnAction( e -> ComSent(comSentText, comSentText.getText()) );

        vBox.setPadding(new Insets(0, 20, 20, 2));
        vBox.getChildren().addAll(text,comSentText,comSent);
        return vBox;
    }//相关结点回传数据

    /**
     * 端口开启或关闭 VBComONAndOFF()
     * @return VBox
     */
    public VBox VBComONAndOFF(){
        vBox = new VBox(5);

        startReceive = new Button("开始接收端口数据");
        closeCom = new Button("关闭端口");
        startReceive.setOnAction(e -> port.startRead(2000)); //识别端口 时间响应
        closeCom.setOnAction(e -> CloseCom()); //识别端口 时间响应

        vBox.setPadding(new Insets(0, 20, 20, 2));
        vBox.getChildren().addAll(startReceive,closeCom);
        return vBox;
    }//端口开启或关闭

    /**
     * 顶部视图 HBTopView()
     * @return HBox
     */
    public HBox HBTopView(){
        HBox hBox = new HBox(10);

        checkPort = new Button("识别端口");
        checkPort.getStyleClass().add("button-blue"); //css样式
        checkPort.setOnAction(e -> port.StartComClicked()); //识别可用端口 时间响应

        VBox VBoxCom = VBComChoose();  //增加选择端口组件
        VBox VBoxComState = VBComONAndOFF(); //端口状态
        VBox VBoxsent = VBComBackNode(); //增加回传数据组件
        /*事件监听*/
        hBox.getChildren().addAll(checkPort, VBoxCom, VBoxComState, VBoxsent);
        return hBox;
    }//顶部视图

    /**
     * 左部视图 VBoxLeftView()
     * @return VBox
     */
    public VBox VBoxLeftView(){
        VBox vBox = new VBox(10);
        vBox.setStyle("-fx-padding: 10;");
        //日期选择
        GridPane gridPane = GPCalendar() ;
        //模式切换
        HBox TGBox = HBoxRealAndHistory();
        //TextArea
        textArea.setMaxSize(300,300);

        vBox.getChildren().addAll(gridPane);
        vBox.getChildren().addAll(TGBox);
        vBox.getChildren().addAll(textArea);

        return vBox;
    }//左部视图

    /**
     * 右部视图 VBoxRightView()
     * @return VBox
     */
    public VBox VBoxRightView(){
        VBox vBox = new VBox();
        vBox.setStyle("-fx-padding: 10;");

        ADTable(); //AD数据表格显示
        /**********暂停和开始**********/
        HBox hbStartAndSuspend = HBStartAndSuspend();

        vBox.getChildren().addAll(tableView);
        vBox.getChildren().addAll(hbStartAndSuspend);

        return vBox;
    }//右部视图

    /**
     * 水势折线图
     */
    public void JavaFXChart(){
        java_FX_Chart  FXChart = new java_FX_Chart ();
        Thread F = new Thread(FXChart,"JavaFX 折线图");
        F.start(); //开始添加节点线程

        xAxis = new CategoryAxis();
        yAxis = new NumberAxis(0,100,10);
        xAxis.setLabel("时间");
        yAxis.setLabel("水势");

        lineChart = new LineChart(xAxis,yAxis);
        lineChart.setCreateSymbols(false);// 没有具体的点
        lineChart.autosize();
        lineChart.alternativeColumnFillVisibleProperty();
        //lineChart.setMaxWidth(5);
        String node01 = "节点1";
        series1.setName(node01);
        lineChart.getData().addAll(series1);
    }//水势折线图

    /**
     * 选择日历时间
     * @return GridPane
     */
    public GridPane GPCalendar(){
        GridPane gridPane = new GridPane();
        gridPane.setHgap(5.5);
        gridPane.setVgap(5.5);
        Label checkInlabel = new Label("选择观察时间:");//

        DatePicker checkInDatePicker = new DatePicker();
        ChoiceTime= new Button("确定日期");
        ChoiceTime.setOnAction(e -> LogPrint(checkInDatePicker)); //显示日期
        gridPane.add(checkInlabel, 0, 0);
        //GridPane.setHalignment(checkInlabel, HPos.LEFT);
        gridPane.add(checkInDatePicker, 0, 1);
        gridPane.add(ChoiceTime,0,2);

        return gridPane;
    }//选择日历时间

    /**
     * AD数据表格 显示
     */
    public void ADTable(){
        //Name column
        TableColumn nameColumn = new TableColumn<>("节点号");
        nameColumn.setMinWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("node"));

        //Price column
        TableColumn priceColumn = new TableColumn("AD值");
        priceColumn.setMinWidth(100);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("AD"));

        //Quantity column
        TableColumn quantityColumn = new TableColumn("基准电压");
        quantityColumn.setMinWidth(80);
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("baseAD"));

        //time
        TableColumn timeColumn = new TableColumn("时间");
        timeColumn.setMinWidth(150);
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));


        tableView = new TableView<>();
        list = FXCollections.observableArrayList();
        tableView.setItems(list); //表格中插入数据
        tableView.getColumns().addAll(nameColumn, priceColumn, quantityColumn,timeColumn);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //消除多出的，显示出的效果多了一空列
        adTable = new ADTable(list);
    }//AD数据表格 显示

    /**
     * 开启和暂停线程 按钮
     * @return
     */
    public HBox HBStartAndSuspend(){
        HBox hBox = new HBox(20);  //控件之间距离
        ToggleGroup Group2 = new ToggleGroup();
        ToggleButton tbSuspend = new ToggleButton("暂停");
        tbSuspend.setToggleGroup(Group2);
        tbSuspend.setSelected(true); //默认选择
        ToggleButton tbResume = new ToggleButton("开始");
        tbResume.setToggleGroup(Group2);

        tbSuspend.setOnAction(event -> suspend(ChartThear));
        tbResume.setOnAction(event -> resume(ChartThear));

        hBox.getChildren().addAll(tbSuspend,tbResume);
        return hBox;
    }//开启和暂停线程

    /**
     * 实时监控 和 历史数据
     * @return HBox
     */
    public HBox HBoxRealAndHistory(){
        HBox hBox = new HBox(20);

        ToggleGroup Group = new ToggleGroup();
        ToggleButton tb1 = new ToggleButton("实时监控");
        tb1.setToggleGroup(Group);
        tb1.setOnAction(e -> real_time(lineChart));
        tb1.setSelected(true); //默认选择
        ToggleButton tb2 = new ToggleButton("历史纪录");
        tb2.setToggleGroup(Group);
        tb2.setOnAction(e -> history_time()); //切换
//        tb2.setOnAction(e -> history_time());
        //tb2.setSelected(true);
        hBox.getChildren().addAll(tb1,tb2);
        return hBox;
    }//实时监控 和 历史数据

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("土壤水势检测系统-sspu");
        /*************************************/

        HBox hboxTop = HBTopView();      //导航栏 Top

        VBox vboxLeft = VBoxLeftView() ; //左部 Left

        VBox vboxRight = VBoxRightView(); //右部 Right
/*********Jfree图标显示************/
//        try {
//            //从数据库中显示信息
//            JfreeLineChart.Myrun(MySql_operate.JDBC_connect());
//            //从数据库中显示信息
//            ChartPanel cp=new ChartPanel(JfreeLineChart.getJFreeChart());
//            node.setContent(cp); //将jfree表放在组件上
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        //node.setVisible(false);
//折线图 javaFX实现
        JavaFXChart();
//折线图 JFree实现
        ChartThear.start();

        /*************************************/
        bp.setTop(hboxTop);
        bp.setLeft(vboxLeft);
        bp.setCenter(lineChart);
        bp.setRight(vboxRight);
        //bp.setCenter(node); //添加frame（折线图）组件
        /*************************************/

        Scene scene = new Scene(bp, 1500, 800);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("javaFX/Monitor/sspu.png"));
        primaryStage.show();
    }

    /**
     * 相关结点回传数据
     * @param comSentText
     * @param text
     */
    private void ComSent(TextField comSentText, String text) {
        try{
            int age = Integer.parseInt(comSentText.getText());
            System.out.println("选择节点: " + age);
            text = text + ";1";
            port.outputStream.write(text.getBytes());
            port.outputStream.flush();
            port.sendDataToSeriaPort();
        }catch(NumberFormatException e){
            System.out.println("Error: " + text + " is not a number");
            AlertBox.display("回传节点号","输入格式错误，请输出数字");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//相关结点回传数据

    /**
     * 关闭端口
     */
    private void CloseCom() {
        try {
            port.serialPort.close();
            LogTextArea.log("端口<" + port.commPort.getName()+">已经关闭");
        }catch (Exception e){
            AlertBox.display("端口","端口尚未打开");
        }

    }

    private void real_time(LineChart<String, Number> lineChart) {
        if(lineChart.isVisible())
            lineChart.setVisible(false);
        else
            lineChart.setVisible(true);
    }

    private void history_time() {
        if(node.isVisible())
            node.setVisible(false);
        else
            node.setVisible(true);

    }

    public void resume(Thread ChartThear) { //Thread cFX
        ChartThear.resume();
//        cFX.resume();
    }//恢复线程

    public void suspend(Thread ChartThear){ //,Thread cFX
        ChartThear.suspend();
//        cFX.suspend();
    }//暂停线程

    public void LogPrint(DatePicker checkInDatePicker){
        log("选择日期："+checkInDatePicker.getValue());
        //System.out.println("选择日期："+checkInDatePicker.getValue());
    }



    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        launch(args);
    }

//    @Override
//    public void run()
//    {
//        //port接收数据封装到list中
//        Date date ;
//        while(true){
//
//            date = new Date();
//            String date01;
//            date01 = String.format("%tT%n",date);  //t的使用
//
//            DataAD dataAD = new DataAD();
//            dataAD.setNode(port.getNode());
//            dataAD.setNode(port.getMassage());
//            dataAD.setTime(date01);
//
//            try {
//            list.addAll(dataAD);
//            double price = (double)rd.nextInt(100);
//            //String price = port.getMassage();
//            Double test= Double.parseDouble(port.getMassage());
//            //list = FXCollections.observableArrayList(port.getNode(),port.getMassage(),port.getBaseAD());
//
//            log("最近一次接收AD消息:" + test);
//
//            //tableView.getItems().add(list);
//            }catch (NullPointerException e){
//
//            }
//            //log("正在运行AD线程!!（接收massage）");
//            System.out.println("正在运行AD线程（接收massage）" );
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            break;
//        }
//
//    }

    @Override
    public void stop() throws Exception { //停止线程
        super.stop();
        try {
            port.serialPort.close();
            log(port.toString());
        }catch (Exception e){
            log("端口没有打开");
        } //关闭端口
        ChartThear.stop();
    }
}

