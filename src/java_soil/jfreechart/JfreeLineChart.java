//package jfreechart;
//
//
//import java.awt.*;
//import java.sql.*;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import Port.PortText;
//import com_receive.SoilData;
//
//import org.jfree.chart.*;
//import org.jfree.chart.axis.DateAxis;
//import org.jfree.chart.axis.DateTickUnit;
//import org.jfree.chart.axis.DateTickUnitType;
//import org.jfree.chart.axis.ValueAxis;
//import org.jfree.chart.labels.StandardXYItemLabelGenerator;
//import org.jfree.chart.plot.XYPlot;
//import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
//import org.jfree.data.time.Second;
//import org.jfree.data.time.TimeSeries;
//import org.jfree.data.time.TimeSeriesCollection;
//
///**
// * @author ymj
// * @version 创建时间：2019年5月3日 上午10:52:00 类说明
// * 用来练习将MyTimeSeries类改变成多线程，用来实现折线图的动态刷新
// */
//public class JfreeLineChart  extends ChartPanel implements Runnable
//{
//    static Connection con = null;
//    static boolean  flag = true;
//    static PortText pt = PortText.getInstance();
//    static  double end = 0;
//    static  int count = 1; //计数
//
//    /**
//     * 从数据库读取数据
//     * @return double
//     * @throws SQLException
//     */
//    public static  double DB() throws SQLException {
//        try {
//            Statement statement = con.createStatement();
//            String sql = "select * from soilprojectdata";
//            ResultSet rs = statement.executeQuery(sql);
//
//            SoilData Data = new SoilData();
//            for(int i = 0; i < count;i++){
//                rs.next();
//            }
//
//            while (rs.next()) {
//
//                //获取information_id这列数据
//                Data.setId(rs.getInt("information_id")) ;
//
//                //获取information_data这列数据
//                Double d = Double.parseDouble(rs.getString("information_data"));
//
//                Data.setData(d.doubleValue())  ;
//
//                //输出结果
//                System.out.println(Data.getId() + "\t" + Data.getData());
//                count++;
//                end = Data.getData();
//                return Data.getData();
//            }
//        }catch ( NullPointerException e){
//
//        }
//        return end;
//    }
//
//    public static double DB_AD(){ //实时AD信息
//        Double test= Double.parseDouble(pt.getMassage());
//        return  test;
//    }
//
//    public static TimeSeries timeseries = new TimeSeries("土壤水势图", Second.class);
//    private static final long serialVersionUID = 1L;
//    //构造函数
//    public JfreeLineChart(JFreeChart chart) {
//        super(chart);
//    }
//
//
//    @SuppressWarnings("deprecation")
//    public static JFreeChart getJFreeChart()
//    {
//        // 首先要解决中文乱码问题
//        // 创建主题样式
//        StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
//        // 设置标题字体
//        standardChartTheme.setExtraLargeFont(new Font("隶书", Font.BOLD, 20));
//        // 设置图例的字体
//        standardChartTheme.setRegularFont(new Font("宋书", Font.PLAIN, 15));
//        // 设置轴向的字体
//        standardChartTheme.setLargeFont(new Font("宋书", Font.ITALIC, 15));
//        // 应用主题样式
//        ChartFactory.setChartTheme(standardChartTheme);
//
//        // 第一步：创建timeseries实例,并将有效数据加入到该timeseries中
//        @SuppressWarnings("deprecation")
//        //timeseries = new TimeSeries("土壤水势", Second.class);
//
//
//        // 第二步：将timeseries加入到数据集中
//         TimeSeriesCollection dataset = new TimeSeriesCollection(timeseries);
//
//        // 第三步：创建时序图相关的JFreeChart对象
//        JFreeChart seriesChart = ChartFactory.createTimeSeriesChart(
//                "土壤水势", "时间(每 1 秒更新一次)", "水势值(pa)",
//                    dataset, true,
//                true, true);
//
//        // 第四步：对表的绘图区域进行设定
//        XYPlot xyPlot = seriesChart.getXYPlot(); // 时序图的plot为XYPlot
//        //对DomainAxis()进行设定
//
//        ValueAxis valueAxis = xyPlot.getDomainAxis();
//
//        //System.out.println(xyPlot.getDomainAxis());
//
//        valueAxis.setAutoRange(true);
//        valueAxis.setFixedAutoRange(10000D);  //设置整个X轴的总的长度(*****)======>这个最重要了
//
//        // 先对X轴设定
//        DateAxis axis = (DateAxis) xyPlot.getDomainAxis(); // 将ValueAxis强转成DateAxis
//        axis.setDateFormatOverride(new SimpleDateFormat("HH点mm分ss秒"));
//        axis.setAxisLineVisible(true); // 设定x轴可见
//        axis.setAutoRange(true); // 设定x轴的刻度自动调整
//        axis.setAutoTickUnitSelection(true); // x轴的刻度可见
//        axis.setVerticalTickLabels(true); // x轴的标签垂直分布
//        axis.setTickUnit(new DateTickUnit(DateTickUnitType.MILLISECOND,1000)); // 这里设置X轴的刻度间隔
////        axis.setMinorTickCount(15);//
//
//        // 在对Y轴设定
//        ValueAxis axis2 = xyPlot.getRangeAxis();
//        axis2.setAutoRange(true); // Y轴可以随着图像的伸缩做数值的变化
//        axis2.setLowerBound(0); // 保证Y轴能够从0开始显示数据
//        axis2.setUpperBound(100); // 设置Y轴显示的最大值
//        axis2.setAutoRange(false);
//
//        // 对折线本身对象进行编辑,折线对象是XYItemRenderer,XYItemRenderer是一个interface,需要将XYItemRenderer转换为实现了它的XYLineAndShapeRenderer
//        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) xyPlot.getRenderer();
//        renderer.setBaseShapesVisible(true); // 使折线图上面带有图例
//        renderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator()); // 是数字显示在折线上
//        renderer.setBaseItemLabelsVisible(true); // 是数字显示在折线上
//        renderer.setAutoPopulateSeriesFillPaint(true);
//        renderer.setAutoPopulateSeriesOutlineStroke(true);
//
//        return seriesChart;
//    }
//
//    @Override
//    public void run() //动态刷新
//    {
//
//        while(true)
//        {
//
//            Date date = new Date();
//            try
//            {
//                timeseries.addOrUpdate(new Second(),DB()); //每1秒刷新一次 从数据库读取信息
//                //timeseries.addOrUpdate(new Second(),DB_AD()); //每1秒刷新一次 c
//                flag = false;
//                //System.out.println("6666");
//                Thread.sleep(1000);
//            } catch (InterruptedException  e) {
//                e.printStackTrace();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }
//
//    public static void Myrun(Connection conFX ) throws SQLException{
//        con = conFX;//连接数据库
////        JfreeLineChart MTS = new JfreeLineChart(JfreeLineChart.getJFreeChart());  //创建jfreechart图
////        Thread ChartThear = new Thread(MTS);
////        //ChartThear = new Thread(MTS).start();  //动态插入值
////        ChartThear.start();
////
////        ChartThear.suspend();
//       // ChartThear.resume();
//    }
//}