package nacos.poc;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Tab_Cve_2021_29442 {
    private Tab tab_CVE_2021_29442 = new Tab("CVE-2021-29442 Nacos 未授权访问 + SQL代码执行 漏洞");
    private Button buttonRun = new Button("Run");
    private Button buttonClear = new Button("Clear");
    private TextField urlTextField = new TextField();
    private TextField sqlTextField = new TextField();
    private TextArea textArea = new TextArea();
    private Label label_url = new Label("URL:");
    private Label label_sql = new Label("SQL:");

    public Tab set(){
        setField();
        setArea();
        setButton();
        setTab();
        return tab_CVE_2021_29442;
    }

    /**
     * 设置 Tab
     * @return
     */
    public void setTab() {
        tab_CVE_2021_29442.setContent(setBox());
    }

    /**
     * 单行输入框 设置
     */
    public void setField(){
        urlTextField.setPromptText("http://ip:port");
        urlTextField.setPrefWidth(450);
        sqlTextField.setPromptText("select * from users");
        sqlTextField.setPrefWidth(300);
    }

    /**
     * 多行输入框 设置(作为信息输出窗口)
     */
    public void setArea(){
        textArea.setPrefRowCount(25);
        textArea.setFocusTraversable(false); // 取消焦点
        textArea.setWrapText(true);
        textArea.setPromptText("[+] Tips: select * from [permissions users app_list config_info]");
    }

    /**
     * 设置 嵌套 Box
     */
    public VBox setBox(){
        HBox hBox = new HBox(15);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.getChildren().addAll(label_url,urlTextField,buttonRun,buttonClear);

        HBox hBox_u = new HBox(15);
        hBox_u.setAlignment(Pos.CENTER_LEFT);
        hBox_u.getChildren().addAll(label_sql,sqlTextField);

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(hBox,hBox_u,textArea);
        vBox.setAlignment(Pos.CENTER_LEFT);

        return vBox;
    }

    /**
     * 设置按钮 监听行为
     */
    public void setButton(){
        // 设置 run 按钮监听
        buttonRun.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String url = urlTextField.getText();
                if (url != null) {
                    try {
                        // 执行POC
                        Code_Cve_2021_29442 code_cve_2021_29442 = new Code_Cve_2021_29442();
                        // 有空格需要 RUL 编码
                        String encodedString = URLEncoder.encode(sqlTextField.getText(), "UTF-8");
                        code_cve_2021_29442.run(url,encodedString,textArea);
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        });

        buttonClear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                textArea.clear();
            }
        });
    }
}
