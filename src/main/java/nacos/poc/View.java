package nacos.poc;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class View extends Application {

    private Label label_cve = new Label("选择漏洞:");
    private ComboBox<String> comboBox = new ComboBox<>();
    private TabPane tabPane = new TabPane();
    private AnchorPane anchorPane = new AnchorPane();
    private Scene scene = new Scene(anchorPane,100,100);

    private HBox optionButtonHBox = new HBox(15);;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 1、创建 选项卡 并批量添加
        Tab_Cve_2021_29441 tab1 = new Tab_Cve_2021_29441();
        Tab_Cve_2021_29442 tab2 = new Tab_Cve_2021_29442();

        ArrayList<Tab> list = new ArrayList<>();

        list.add(tab1.set());
        list.add(tab2.set());

        setTabPane(list);

        // 2、设置 选项卡 面板
        setAnchorPane();
        // 3、设置 单选框
        setOptionButtonHBox(list);

        primaryStage.setScene(scene);
        primaryStage.setTitle("POC Verify GUI");
        primaryStage.setWidth(900);
        primaryStage.setHeight(650);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * 创建 选项卡面板 和 选项卡
     */
    public TabPane setTabPane(ArrayList<Tab> list){
        // 选项卡面板(3)
        tabPane.setPrefSize(850,550);
        // 监听器 关闭 Tab 时，删除 Tab
        ArrayList<Tab> tabs = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            tabs.add(list.get(i));
        }
        for (Tab tab : tabs) {
            tab.setOnCloseRequest(new EventHandler<Event>() {
                @Override
                public void handle(Event event) {
                    tabPane.getTabs().remove(tab);
                }
            });
        }
        return tabPane;
    }

    /**
     * 设置 定位窗口 元素的位置
     */
    public void setAnchorPane() {
        AnchorPane.setTopAnchor(tabPane,50.0);
        AnchorPane.setLeftAnchor(tabPane,20.0);
        AnchorPane.setTopAnchor(optionButtonHBox,10.0);
        AnchorPane.setLeftAnchor(optionButtonHBox,20.0);
        anchorPane.getChildren().addAll(optionButtonHBox,tabPane);
    }

    /**
     * 创建 CVE 单选框
     * @return
     */
    public void setOptionButtonHBox(ArrayList<Tab> tabs){

        optionButtonHBox.getChildren().addAll(label_cve,comboBox);
        optionButtonHBox.setAlignment(Pos.CENTER_LEFT);

        for (int i = 0; i < tabs.size(); i++) {
            comboBox.getItems().add(tabs.get(i).getText());
        }

        // 监听器 选择后处理
        comboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    int num = 0;
                    for (Tab tabA : tabs) {
                        // 首先判断是否在列表中
                        if (tabA.getText().equals(newValue)) {
                            // 再判断是否在TabPane中
                            for (Tab tabB : tabPane.getTabs()) {
                                if (tabB.getText().equals(newValue)) {
                                    num++;
                                }
                            }
                            // 说明不在TabPane中，那就添加列表中的Tab
                            if (num == 0) {
                                tabPane.getTabs().add(tabA);
                                break;
                            }
                        }
                    }
                }
            }
        });
    }
}
