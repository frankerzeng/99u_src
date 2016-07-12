package nd.im.control;

import nd.im.im.ImUc;
import nd.im.im.MobileIoa;
import nd.im.im.MysqlConnect;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/18.
 */
@Controller
@RestController
@RequestMapping(value = "/99u")
public class Index {


    static String sql = null;
    static MysqlConnect mysqlConnect = null;
    static MysqlConnect mysqlConnectSendFlower = null;
    static ResultSet resultSet = null;
    static ResultSet resultSetSendFlower = null;

    @RequestMapping(value = "/index", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    String index(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        System.out.println("Default Charset=" + Charset.defaultCharset());
        System.out.println("file.encoding=" + System.getProperty("file.encoding"));
        System.out.println("Default Charset=" + Charset.defaultCharset());
        System.out.println("Default Charset in Use=" + getDefaultCharSet());

        // 用户数据
        sql = "select * from user";
        mysqlConnect = new MysqlConnect(sql);

        sql = "select * from send where status = 1";
        mysqlConnectSendFlower = new MysqlConnect(sql);

        try {
            resultSet = mysqlConnect.preparedStatement.executeQuery();
            resultSetSendFlower = mysqlConnectSendFlower.preparedStatement.executeQuery();

            while (resultSet.next()) {

                String password = resultSet.getString(2);
                String name = resultSet.getString(4);

                ImUc imUc = new ImUc();

                // 获取token
                String ret = imUc.token(name, password);

                // 送花
                if ("741007".equals(name)) {
                    System.out.println("送花开始");
                    while (resultSetSendFlower.next()) {
                        String userId = resultSetSendFlower.getString(1);
                        imUc.sendFlower(userId);
                        System.out.println("送花--->" + userId);
                    }
                    System.out.println("送完成");
                }

                // 生日列表
                System.out.println("祝福开始");
                Map<String, String> birthdayUsers = imUc.birthdayUsers();

                for (String key : birthdayUsers.keySet()) {
                    //祝福
                    imUc.bless(key);
                    System.out.println("祝福" + birthdayUsers.get(key));
                    //送花
                    imUc.sendFlower(key);
                    System.out.println("送花--->" + key);
                }
                System.out.println("祝福完成");


                MobileIoa mobileIoa = new MobileIoa();

                // 签到
                String retSign = mobileIoa.mobileAction(name, "signIn", null);

                // 日清
                String retClean = mobileIoa.mobileAction(name, "signOut_new", null);

                // 待领积分列表
                Map<String, String> pointList = mobileIoa.mobileActionPoint(name, "getReceivePointList");
                for (String k : pointList.keySet()) {
                    if (Integer.parseInt(pointList.get(k)) == 0) {
                        //领积分
                        HashMap autoCode = new HashMap();
                        autoCode.put("auto", k);
                        mobileIoa.mobileAction(name, "receivePoint", autoCode);
                        System.out.println("领积分" + k);
                    }
                }
                System.out.println("自动化结束");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "送花完成~~";
    }

    private static String getDefaultCharSet() {
        OutputStreamWriter writer = new OutputStreamWriter(new ByteArrayOutputStream());
        String enc = writer.getEncoding();
        return enc;
    }
}