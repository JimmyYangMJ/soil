package Port;

import com_receive.DataAD;
import com_receive.DataBean;
import com_receive.DataDao;
import gnu.io.*;
import javaFX.AlertBox;
import javaFX.LogTextArea;
import javafx.scene.control.ChoiceBox;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.TooManyListenersException;

public class PortText implements  SerialPortEventListener { //Runnable,
    /**
     * 单例 端口
     */
    private static PortText obj = new PortText(); //共享同一个对象
    private PortText(){  //private 确保只能在类内部调用构造函数
        System.out.println("调用了无参构造函数:端口对象实例化");
    }
    public static PortText getInstance()	{
        //静态方法使用静态变量
        //另外可以使用方法内的临时变量，但是不能引用非静态的成员变量
        return obj;
    }
/* -----------------------------------------------------------------------------------*/
    private String appName = "串口调试信息";
    private int threadTime = 0;
    private int timeout = 2000;//open端口时的等待时间

    private InputStream inputStream; //输入流
    public OutputStream outputStream = null; //输出流

    public CommPortIdentifier commPort;  //RXTX com端口检测
    public  SerialPort serialPort;        //RXTX
    static DataAD AD;

    public static String getNode() {
        return node;
    }
    public static void setNode(String node) {
        PortText.node = node;
    }
    public static String getBaseAD() {
        return baseAD;
    }
    public static void setBaseAD(String baseAD) {
        PortText.baseAD = baseAD;
    }

    static String node,baseAD;
    private String massage = null;  //串口消息信息 AD的值
    private boolean massageState = false;
    public void log(String msg) {
        System.out.println("消息" + " --> " + msg);
    } //显示窗口连接信息


    public void StartComClicked(){
        CommPortIdentifier cpid;
        Enumeration en = CommPortIdentifier.getPortIdentifiers();
        System.out.println("下面列出pc中的所有端口:");
        LogTextArea.log("下面列出pc中的所有端口:");
        while (en.hasMoreElements()) {
            cpid = (CommPortIdentifier) en.nextElement();
            if (cpid.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                //打印端口名称和该端口所有者
                System.out.println("端口名称：" + cpid.getName() + "," + cpid.getCurrentOwner());
                LogTextArea.log("端口名称：" + cpid.getName() + "," + cpid.getCurrentOwner());
            }
        }
    }//显示PC中开启的端口
/************************************/
    public void selectPort(ChoiceBox<String> com_choice) {//String portName
        String portName = com_choice.getValue(); //获得选择框的值
        /**
         * 测试
         */
        this.commPort = null;
        CommPortIdentifier cpid;
        Enumeration en = CommPortIdentifier.getPortIdentifiers();
        while (en.hasMoreElements()) {
            cpid = (CommPortIdentifier) en.nextElement();
            if (cpid.getPortType() == CommPortIdentifier.PORT_SERIAL && cpid.getName().equals(portName)) {
                this.commPort = cpid;
                break;
            }
        }
        openPort();
    }//选择接收端口

    public void openPort() {
        if (commPort == null)
            log(String.format("无法找到名字为'%1$s'的串口！", commPort.getName()));
        else {
            log("端口选择成功，当前端口：<" + commPort.getName() + ">,现在实例化 SerialPort:");
            LogTextArea.log("端口选择成功，当前端口：<" + commPort.getName() + "> ");
            try {
                serialPort = (SerialPort) commPort.open(appName, timeout);
                /**
                 * 设置波特率/ 校验位 /数据位 /停止位
                 */
                serialPort.setSerialPortParams(115200,  //波特率
                        SerialPort.DATABITS_8, 			//校验位
                        SerialPort.STOPBITS_1, 			//数据位
                        SerialPort.PARITY_NONE);		//停止位

                log("实例 SerialPort 成功！");
            } catch (PortInUseException e) {
                throw new RuntimeException(String.format("端口'%1$s'正在使用中！",
                        commPort.getName()));
            } catch (UnsupportedCommOperationException e) {
                e.printStackTrace();
            }
        }
    }//打开端口
/************************************/
    private void checkPort() {
        if (commPort == null){
            //throw new RuntimeException("没有选择端口，请使用 " + "selectPort(String portName) 方法选择端口");
            AlertBox.display("端口","没有选择端口");
        }
        if (serialPort == null) {
            throw new RuntimeException("SerialPort 对象无效！");
        }
    }//检测是否有端口
/************************************/
    public void startRead(int time) {
        checkPort(); //检测是否有端口
        try {
            inputStream = new BufferedInputStream(serialPort.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("获取端口的InputStream出错：" + e.getMessage());
        }//打开输入流

        try {
            outputStream = serialPort.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }//打开输出流
        try {
            serialPort.addEventListener(this);
        } catch (TooManyListenersException e) {
            throw new RuntimeException(e.getMessage());
        }
        serialPort.notifyOnDataAvailable(true);
        log(String.format("开始监听来自 '%1$s'端口 的数据--------------", commPort.getName()));
        LogTextArea.log(String.format("开始监听 <%1$s>端口数据", commPort.getName()));
//        if (time > 0) {
//            this.threadTime = time *1000 ;  //线程持续time秒
//            Thread t = new Thread(this);
//            t.start();
//            log(String.format("监听程序将在 %1$d 秒 后关闭！！！！！", time));
//        }//开启线程，定时关闭
    }//开始读取数据

    @Override
    public void serialEvent(SerialPortEvent arg0) {//接收串口信息
        switch (arg0.getEventType()) {
            case SerialPortEvent.BI:/*Break interrupt,通讯中断*/
            case SerialPortEvent.OE:/*Overrun error，溢位错误*/
            case SerialPortEvent.FE:/*Framing error，传帧错误*/
            case SerialPortEvent.PE:/*Parity error，校验错误*/
            case SerialPortEvent.CD:/*Carrier detect，载波检测*/
            case SerialPortEvent.CTS:/*Clear to send，清除发送*/
            case SerialPortEvent.DSR:/*Data set ready，数据设备就绪*/
            case SerialPortEvent.RI:/*Ring indicator，响铃指示*/
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:/*Output buffer is empty，输出缓冲区清空*/
                break;
            case SerialPortEvent.DATA_AVAILABLE:/*Data available at the serial port，端口有可用数据。读到缓冲数组，输出到终端*/
                byte[] readBuffer = new byte[1024];
                String readStr = "";
                String s2 = "";
                try {
                    while (inputStream.available() > 0) {     //\r\n
//                        try {
//                            Thread.sleep(100);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }//线程等待
                        inputStream.read(readBuffer);
                        readStr += new String(readBuffer).trim();
                    }
                    /**
                     * s2为接收到数据 格式： 1;4444;4444
                     */
                    s2 = new String(readBuffer).trim(); //
                    if(s2.length() < 11) //数据传输格式错误
                        break; //数据舍弃
                    /**
                     * 数据库插入AD数据 例：1;1234;1234
                     */
                    DataBean data = new DataBean();
                    data.setData(s2);
                    DataDao dataDao = new DataDao();
                    try {
                        dataDao.insert(data);  //数据库数据插入
                    }catch (SQLException e){
                        e.printStackTrace();
                        System.out.println("数据存储异常");
                    }
                    /******************************/
                    log("接收到端口返回数据" + readStr+"(长度为" + readStr.length() + ")" );
                    setMassageState(true); //标记接收到消息
                    ADHandle(s2);  //数据结构处理
                    log(s2);


                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }//数据接收的监听处理函数

    /**
     * 给串行端口发送数据
     * @since 2019-4-28 上午12:05:00
     */
    public void sendDataToSeriaPort() {
        try {

            outputStream.flush();

        } catch (IOException e) {
            System.out.println("向端口发送数据出错");
        }

        System.out.println("已经发送消息");
    }
    public String getMassage(){
        return this.massage;
    }

    public void setMassageState(boolean state){
        this.massageState = state;
    }

    public boolean getMassageState(){
        return this.massageState;
    }

    public void ADHandle(String com){
//        final String REGEX = "\\d;\\d\\d\\d\\d;\\d\\d\\d\\d\r\n";
//        if(com.matches(REGEX) == false){
//            log("警告： 串口接收数据格式错误");
//            LogTextArea.log("警告： 串口接收数据格式错误");
//            return;
//        }
        String[] b = com.split(";", 3);
        AD = new DataAD();
        AD.setNode(b[0]);   this.node = AD.getNode();
        AD.setAD(b[1]);     this.massage = AD.getAD();
        AD.setBaseAD(b[2]); this.baseAD = AD.getBaseAD();

        LogTextArea.log("接收消息:" + com); //
        log("数据已经处理 AD:"+getMassage());
    }//处理串口接收数据 AD 例如 01;0056;0075 存入数组b[]中
////////////////***端口消息接收消息
//    @Override
//    public void run() {
//        try {
//            Thread.sleep(threadTime);  //持续time后关闭
//            serialPort.close();
//            log(String.format("端口'%s'监听关闭了！", commPort.getName()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }//定时关闭端口监听
}
