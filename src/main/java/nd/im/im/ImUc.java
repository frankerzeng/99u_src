package nd.im.im;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import nd.im.util.HttpClient;
import nd.im.util.HttpsClient;
import nd.im.util.Map2Json;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Administrator on 2016/5/19.
 */
public class ImUc {

    private static String host = "aqapi.101.com";
    private static String version = "/v0.93/";

    private static String mobileUrl = "http://mobile.ioa.99.com/ServiceHost/ToDoList/json/";

    public static String access_token = null;
    public static String mac_key = null;
    public static String mac_algorithm = null;

    public static Map<String, String> map = new HashMap<String, String>() {{
        put("Content-Type", "application/json; charset=utf-8");
    }};

    // 祝福
    public void bless(String userId) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {

        String api = "/v0.1/birthday_users/" + userId + "/actions/bless";
        String host = "im-birthday.social.web.sdp.101.com";

        String authorization = sign("POST", api, host);

        if (authorization == null) {
            return;
        }

        map.put("Authorization", authorization);


        String url = "http://" + host + api;

        try {
            String resp = HttpClient.request(url, "", map, "POST");

            if (resp == null) {
                throw new Exception("祝福失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendFlower(String userId) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        String api = "/v0.3/c/flower/send";
        String host = "pack.web.sdp.101.com";

        String authorization = sign("POST", api, host);

        if (authorization == null) {
            return;
        }

        map.put("Authorization", authorization);

        String url = "http://" + host + api;

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("dest_uid", userId);
        paramMap.put("item_type_id", 20000);
        paramMap.put("amount", 1);

        String requestParam = Map2Json.map2json(paramMap);
        System.out.println(requestParam);
        try {
            String resp = HttpClient.request(url, requestParam, map, "POST");

            if (resp == null) {
                throw new Exception("送花失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 获得token
    public void token(String name, String password) throws Exception {

        String api = version + "tokens";

        System.out.println(Charset.defaultCharset());

        System.out.println(password);

        // // TODO: 2016/7/12 加密算法问题
        //  password = encrypt_md5(password);
        switch (name) {
            case "741007":
                password = "a6184949124f01e61edee18f5bea2abf";
                break;
            case "153962":
                password = "c350a822df13a90869a05227a74a5418";
                break;
            default:
                throw new Exception("用户密码未定义");
        }

        String url = "https://" + host + api;

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("login_name", name);
        paramMap.put("password", password);
        paramMap.put("org_name", "ND");

        String requestParam = Map2Json.map2json(paramMap);

        try {
            Map<String, String> map = new HashMap<String, String>();
            String resp = HttpsClient.request(url, requestParam, "POST", map, "application/json");

            if (resp == null) {
                throw new Exception("获得token失败");
            }

            JSONObject jsonObject = JSON.parseObject(resp);
            access_token = jsonObject.getString("access_token");
            mac_key = jsonObject.getString("mac_key");
            mac_algorithm = jsonObject.getString("mac_algorithm");

            if (access_token == null || mac_key == null || mac_algorithm == null) {
                throw new Exception("获得token 字段失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 加密
    private static String encrypt_md5(String password) throws UnsupportedEncodingException {

        String salt = "，。fdjf,jkgfkl";
        char[] charArray = new char[15];
        charArray[0] = (char) 163;
        charArray[1] = (char) 172;
        charArray[2] = (char) 161;
        charArray[3] = (char) 163;
        charArray[4] = (char) 102;
        charArray[5] = (char) 100;
        charArray[6] = (char) 106;
        charArray[7] = (char) 102;
        charArray[8] = (char) 44;
        charArray[9] = (char) 106;
        charArray[10] = (char) 107;
        charArray[11] = (char) 103;
        charArray[12] = (char) 102;
        charArray[13] = (char) 107;
        charArray[14] = (char) 108;
        System.out.println((char) 163);


        String s = "";
        int n = charArray.length;
        System.out.println(Integer.valueOf(charArray[0]));
        for (int i = 0; i < n; i++) {
            s += Integer.valueOf(charArray[i]) > 127 ? String.valueOf(charArray[i]) + String.valueOf(charArray[++i]) : String.valueOf(charArray[i]);
        }
//        a6184949124f01e61edee18f5bea2abf

        System.out.println("-----------------");
        System.out.println(s);
        for (int j = 1; j < 10000000; j++) {

            if ("d527ca074d412d9d0ffc844872c4603c".equals(MD5Util.encode((char) j))) {
                System.out.println("-----------==========");
                System.out.println(j);
            }

        }
        System.out.println("-----------------");

        System.out.println(MD5Util.encode(String.valueOf(charArray[0])));
        System.out.println(MD5Util.encode((char) 163));
        System.out.println(password + s);
        return MD5Util.encode(password + s);
    }

    // uuid
    private static String uuid() {
        return UUID.randomUUID().toString();
    }

    // 签名
    private static String sign(String requstMethod, String api, String host) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {

        Date date = new Date();
        Long mictime = date.getTime();
        String time = mictime.toString();

        String authorization = null;

        if (mac_algorithm != null) {
            String uuid = uuid();
            String nonce = time + ":" + uuid.substring(uuid.length() - 8);
            String hashData = nonce + "\n" + requstMethod + "\n" + api + "\n" + host + "\n";
            String hashKey = mac_key;
            String hashString = hmacSHA512(hashData, hashKey);

            authorization = " MAC id=\"" + access_token + "\",nonce=\"" + nonce + "\",mac=\"" + hashString + "\"";
        }

        return authorization;
    }

    // 生日同学的列表
    public Map<String, String> birthdayUsers() throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        Map<String, String> resultMap = new HashMap<>();

        String api = "/v0.1/birthday_users";
        String host = "im-birthday.social.web.sdp.101.com";

        String authorization = sign("GET", api, host);

        if (authorization == null) {
            return resultMap;
        }

        String url = "http://" + host + api;

        try {
            Map<String, String> map = new HashMap<String, String>();
            map.put("Content-Type", "application/json; charset=utf-8");
            map.put("Authorization", authorization);
            String resp = HttpClient.request(url, "", map, "GET");

            if (resp == null) {
                throw new Exception("获取生日列表失败");
            }

            JSONObject retdata = JSON.parseObject(resp);
            String items = retdata.getString("items");

            JSONArray listArr = (JSONArray) JSON.parse(items);

            for (Object la : listArr) {
                JSONObject laTemp = JSON.parseObject(la.toString());
                String userId = laTemp.getString("user_id");
                String realName = laTemp.getString("real_name");
                resultMap.put(userId, realName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultMap;
    }

    /**
     * 与php中的hash_hmac('sha512', $data, $key，true)+base64_encode功能相同
     *
     * @param data
     * @param key
     * @return
     */
    private static String hmacSHA512(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);

        byte[] toEncode = sha256_HMAC.doFinal(data.getBytes("UTF-8"));

        //        String kk = Hex.encodeHexString(toEncode);

        String mimeEncoded = com.sun.org.apache.xerces.internal.impl.dv.util.Base64.encode(toEncode);

        return mimeEncoded;
    }

}
