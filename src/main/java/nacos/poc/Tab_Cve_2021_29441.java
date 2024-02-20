package nacos.poc;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Tab_Cve_2021_29441 {

    private Tab tab_CVE_2021_29441 = new Tab("CVE-2021-29441 Nacos 未授权访问 + 任意用户注册 漏洞");
    private Button buttonRun = new Button("Run");
    private Button buttonClear = new Button("Clear");
    private TextField urlTextField = new TextField();
    private TextField userNameTextField = new TextField();
    private TextField passWordTextField = new TextField();
    private TextArea textArea = new TextArea();
    private Label label_url = new Label("URL:");
    private Label label_UserName = new Label("UserName:");
    private Label label_PassWord = new Label("PassWord:");

    public Button getButtonRun() {
        return buttonRun;
    }

    public Label getLabel_url() {
        return label_url;
    }

    public Label getLabel_UserName() {
        return label_UserName;
    }

    public Label getLabel_PassWord() {
        return label_PassWord;
    }

    public Button getButtonClear() {
        return buttonClear;
    }

    public TextField getUrlTextField() {return urlTextField;}

    public TextField getUserNameTextField() {
        return userNameTextField;
    }

    public TextField getPassWordTextField() {
        return passWordTextField;
    }

    public TextArea getTextArea() {
        return textArea;
    }

    public Tab set(){
        setUPLabel();
        setField();
        setArea();
        setButton();
        setTab();
        return tab_CVE_2021_29441;
    }

    /**
     * 设置 Tab
     * @return
     */
    public void setTab() {
        tab_CVE_2021_29441.setContent(setBox());
    }

    /**
     * 文本 位置 设置
     */
    public void setUPLabel(){
        label_UserName.setPrefWidth(65);
        label_PassWord.setPrefWidth(65);
    }

    /**
     * 单行输入框 设置
     */
    public void setField(){
        urlTextField.setPromptText("http://ip:port");
        urlTextField.setPrefWidth(450);
        userNameTextField.setPromptText("user");
        userNameTextField.setPrefWidth(150);
        passWordTextField.setPromptText("pass");
        passWordTextField.setPrefWidth(150);
    }

    /**
     * 多行输入框 设置(作为信息输出窗口)
     */
    public void setArea(){
        textArea.setPrefRowCount(25);
        textArea.setWrapText(true);
        textArea.setFocusTraversable(false); // 取消焦点
        textArea.setPromptText("Info:输入需要注册的账户密码");
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
        hBox_u.getChildren().addAll(label_UserName,userNameTextField);

        HBox hBox_p = new HBox(15);
        hBox_p.setAlignment(Pos.CENTER_LEFT);
        hBox_p.getChildren().addAll(label_PassWord,passWordTextField);

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(hBox,hBox_u,hBox_p,textArea);
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
                    // 执行POC
                    Code_Cve_2021_29441 code_cve_2021_29441 = new Code_Cve_2021_29441();

                    code_cve_2021_29441.run(url,userNameTextField.getText(),passWordTextField.getText(),textArea);
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
