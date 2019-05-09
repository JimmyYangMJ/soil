//package test;
//
//import org.jfree.chart.ChartFactory;
//import org.jfree.chart.ChartPanel;
//import org.jfree.chart.JFreeChart;
//import org.jfree.chart.StandardChartTheme;
//import org.jfree.chart.plot.PlotOrientation;
//import org.jfree.chart.ui.ApplicationFrame;
//import org.jfree.data.category.DefaultCategoryDataset;
//import org.jfree.ui.ApplicationFrame;
//import org.jfree.ui.RefineryUtilities;
//
//    public class SaastolaskuriTest extends ApplicationFrame
//    {
//        public LineChart_AWT( String applicationTitle , String chartTitle )
//        {
//            super(applicationTitle);
//            StandardChartTheme chartTheme = new StandardChartTheme("CN");
//            JFreeChart lineChart = ChartFactory.createLineChart(
//                    chartTitle,
//                    "Sr","&&%r",
//                    createDataset(),
//                    PlotOrientation.VERTICAL,
//                    true,true,false);
//
//            ChartPanel chartPanel = new ChartPanel( lineChart );
//            chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
//            setContentPane( chartPanel );
//        }
//
//        public SaastolaskuriTest(String title) {
//            super(title);
//        }
//
//        private DefaultCategoryDataset createDataset( )
//        {
//            DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
//            dataset.addValue( 0.1 , "Sr" , "0.5" );
//            dataset.addValue( 0.2 , "Sr" , "0.55" );
//            dataset.addValue( 0.3 , "Sr" ,  "0.6" );
//            dataset.addValue( 0.4 , "Sr" , "0.67" );
//            dataset.addValue( 0.5 , "danger" , "0.7" );
//            dataset.addValue( 0.6 , "danger" , "0.74" );
//            return dataset;
//        }
//        public static void main( String[ ] args )
//        {
//            LineChart_AWT chart = new LineChart_AWT(
//                    "Swing Line Chart" ,
//                    "平面缺陷简化评定的失效评定图");
//
//            chart.pack( );
//            RefineryUtilities.centerFrameOnScreen( chart );
//            chart.setVisible( true );
//        }
//    }