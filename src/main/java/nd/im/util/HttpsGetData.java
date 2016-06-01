package nd.im.util;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

public class HttpsGetData {
    private static class TrustAnyTrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    String _url = "";

    Map<String, String> _params;

    public HttpsGetData(String url, Map<String, String> keyValueParams) {
        this._url = url;
        this._params = keyValueParams;
    }

    public String Do() throws Exception {
        String result = "";
        BufferedReader in = null;
        try {

            String urlStr = this._url + "?" + getParamStr();
            System.out.println("GET请求的URL为：" + urlStr);
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[]{new TrustAnyTrustManager()},
                    new java.security.SecureRandom());
            URL realUrl = new URL(urlStr);
            // 打开和URL之间的连接
            HttpsURLConnection connection = (HttpsURLConnection) realUrl.openConnection();
            //设置https相关属性
            connection.setSSLSocketFactory(sc.getSocketFactory());
            connection.setHostnameVerifier(new TrustAnyHostnameVerifier());
//            connection.setDoOutput(true);

            // 发送POST请求必须设置如下两行
            connection.setDoOutput(true);
            connection.setDoInput(true);

            // 设置通用的请求属性
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();

            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            System.out.println("获取的结果为：" + result);
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            //e.printStackTrace();
            throw e;
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                //e2.printStackTrace();
                throw e2;
            }
        }
        return result;

    }

    public String DoPostUrl() {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(this._url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Length", String.valueOf(getParamStr().getBytes().length));
            conn.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 5.1; rv:19.0) Gecko/20100101 Firefox/19.0");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            System.out.println(getParamStr());
            out.print(getParamStr());
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应


            Map<String, List<String>> field = conn.getHeaderFields();
            String code = conn.getHeaderField(0);
            System.out.println(code);
            System.out.println("------------");
            code = code.substring(9, 12);


            InputStream inputStream = conn.getInputStream();
            Reader reader = new InputStreamReader(inputStream);
            in = new BufferedReader(reader);
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public String DoPostHttps() {

        HttpsURLConnection httpsURLConnection = null;
        try {
            httpsURLConnection = (HttpsURLConnection) (new URL(this._url)).openConnection();
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            httpsURLConnection.setRequestProperty("Content-Length", String.valueOf(getParamStr().getBytes().length));
            httpsURLConnection.setRequestProperty("accept", "*/*");
            httpsURLConnection.setRequestProperty("connection", "Keep-Alive");
            httpsURLConnection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 5.1; rv:19.0) Gecko/20100101 Firefox/19.0");
            httpsURLConnection.setUseCaches(false);
            httpsURLConnection.connect();

            PrintWriter pw = new PrintWriter(httpsURLConnection.getOutputStream());
            String content = getParamStr();
            pw.print(content);
            pw.flush();
            pw.close();

            System.out.println("===========+++++++++++++++++++");
            System.out.println(httpsURLConnection.getResponseCode());
            System.out.println(httpsURLConnection.getResponseMessage());

            InputStream in1 = httpsURLConnection.getInputStream();
            ByteArrayOutputStream oss = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            int len = 0;
            while ((len = in1.read(data)) != -1) {
                oss.write(data, 0, len);
            }
            in1.close();
            System.out.println("-ddddddddddddd---------=======-----------");
            System.out.println(new String(oss.toByteArray()));

            BufferedReader in = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println("----------=======-----------");
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getParamStr() {
        String paramStr = "";
        // 获取所有响应头字段
        Map<String, String> params = this._params;
        // 获取参数列表组成参数字符串
        for (String key : params.keySet()) {
            paramStr += key + "=" + params.get(key) + "&";
        }
        //去除最后一个"&"
        paramStr = paramStr.substring(0, paramStr.length() - 1);

        return paramStr;
    }
}