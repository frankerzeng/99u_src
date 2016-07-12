package nd.im.im;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import nd.im.util.HttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/11.
 */
public class MobileIoa {

    private static String mobileUrl = "http://mobile.ioa.99.com/ServiceHost/ToDoList/json/";

    // 签到和日清
    // 领积分
    public String mobileAction(String userId, String action, HashMap param) {

        String url = mobileUrl + action + "?userID=" + userId + "&sid=";

        if (param != null) {
            for (Object key : param.keySet()) {
                url += "&" + key + "=" + param.get(key);
            }
        }

        try {
            Map<String, String> map = new HashMap<String, String>();
            map.put("Content-Type", "application/json; charset=utf-8");
            map.put("Nd-CompanyOrgId", "481036337156");
            String resp = HttpClient.request(url, "", map, "GET");

            checkReturn(resp, action);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    // 积分列表
    public Map<String, String> mobileActionPoint(String userId, String action) {

        Map<String, String> resultMap = new HashMap<>();

        String url = mobileUrl + action + "?userID=" + userId + "&sid=";

        try {
            Map<String, String> map = new HashMap<String, String>();
            map.put("Content-Type", "application/json; charset=utf-8");
            map.put("Nd-CompanyOrgId", "481036337156");
            String resp = HttpClient.request(url, "", map, "GET");

            checkReturn(resp, action);

            JSONArray jsonArray = (JSONArray) JSON.parse(resp);

            for (Object la : jsonArray) {
                JSONObject laTemp = JSON.parseObject(la.toString());
                String autoCode = laTemp.getString("AutoCode");
                String bAdd = laTemp.getString("bAdd");
                resultMap.put(autoCode, bAdd);
            }

            return resultMap;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<String, String>();
    }

    private void checkReturn(String resp, String action) throws Exception {
        if (resp == null) {
            switch (action) {
                case "signIn":
                    throw new Exception("签到失败");
                case "signOut_new":
                    throw new Exception("日事日清失败");
                case "getReceivePointList":
                    throw new Exception("获取待领积分列表失败");
                case "receivePoint":
                    throw new Exception("领取积分失败");
                default:
                    throw new Exception("未定义错误");
            }
        }

    }

}
