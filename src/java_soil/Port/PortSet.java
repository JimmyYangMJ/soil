package Port;

import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class PortSet {

    public static ChoiceBox<String> getCBdateBit() {
        return CBdateBit;
    }

    public static ChoiceBox<String> getCBparity() {
        return CBparity;
    }

    public static ChoiceBox<String> getCBbaudRate() {
        return CBbaudRate;
    }

    public static ChoiceBox<String> getCBstopBit() {
        return CBstopBit;
    }

    private static ChoiceBox<String> CBdateBit, CBparity, CBbaudRate, CBstopBit;
    /**
     * 串口配置:数据位 校验位 波特率 停止位
     * @return
     */
    public static GridPane Portsets(){
        GridPane gridPane = new GridPane();
        gridPane.setHgap(5.5);
        gridPane.setVgap(5.5);
        Text dateBit = new Text(2,20,"数据位");
        Text parity = new Text(2,20,"校验位");
        Text baudRate = new Text(2,20,"波特率");
        Text stopBit = new Text(2,20,"停止位");

        CBdateBit = new ChoiceBox<>();
        CBdateBit.getItems().addAll("5", "6","7","8");
        CBdateBit.setValue("8");

        CBparity = new ChoiceBox<>();
        CBparity.getItems().addAll("NONE", "ODD","EVEN");
        CBparity.setValue("NONE");

        CBbaudRate = new ChoiceBox<>();
        CBbaudRate.getItems().addAll("2400", "4800","9600","115200","14400","19200");
        CBbaudRate.setValue("115200");

        CBstopBit = new ChoiceBox<>();
        CBstopBit.getItems().addAll("1", "2","1.5");
        CBstopBit.setValue("1");

        gridPane.add(dateBit, 0, 0);gridPane.add(CBdateBit,1,0);
        gridPane.add(parity, 0, 1);gridPane.add(CBparity,1,1);
        gridPane.add(baudRate,2,0);gridPane.add(CBbaudRate,3,0);
        gridPane.add(stopBit,2,1);gridPane.add(CBstopBit,3,1);

        return gridPane;
    }
}
