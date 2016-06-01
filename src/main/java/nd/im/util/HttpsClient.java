package nd.im.util;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mir
 */
public class HttpsClient {
    private static final String DEFAULT_CHARSET = "UTF-8";

    public static String request(String url, String request, String method, Map<String, String> headerMap) throws IOException {
        String regEx = "^((https)://).+";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(url);
        if (!matcher.matches()) {
            url = "https://" + url;
        }

        HttpsURLConnection con = null;
        try {
            SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            URL u = new URL(url);
            con = (HttpsURLConnection) u.openConnection();
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setSSLSocketFactory(sslSocketFactory);
            // if (!StringUtil.isEmpty(request)) {
            con.setDoOutput(true);
            if (!method.equals("PATCH")) {
                con.setRequestMethod(method);
            } else {
                con.setRequestProperty("x-http-method-override", method);
            }
            con.setRequestProperty("Charset", DEFAULT_CHARSET);
            System.out.println(url);
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                con.setRequestProperty(entry.getKey(), entry.getValue().trim());
                //System.out.println(entry.getValue().trim());
                //con.addRequestProperty(entry.getKey(), entry.getValue().trim());
            }
            // con.setRequestProperty("Content-Type", "application/json");
            if (method.equals("POST")) {
                OutputStream os = con.getOutputStream();
                os.write(request.getBytes());
                os.flush();
                os.close();
            } else {
                con.connect();
            }
            // }
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK || con.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                StringBuilder sb = new StringBuilder(con.getContentLength() == -1 ? 1024 * 5 : con.getContentLength());
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                for (String line = br.readLine(); line != null; line = br.readLine()) {
                    sb.append(line);
                }
                System.out.println(sb.toString());
                return sb.toString();
            }
            // 获取错误信息
            StringBuilder sb = new StringBuilder(con.getContentLength() == -1 ? 1024 * 5 : con.getContentLength());
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                sb.append(line);
            }
            System.out.println(sb.toString());
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        return null;
    }


    public static String request(String url, String request, String method, Map<String, String> headerMap, String contentType) throws IOException {
        String regEx = "^((https)://).+";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(url);
        if (!matcher.matches()) {
            url = "https://" + url;
        }

        HttpsURLConnection con = null;
        try {
            SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            URL u = new URL(url);
            con = (HttpsURLConnection) u.openConnection();
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setSSLSocketFactory(sslSocketFactory);
            con.setDoOutput(true);
            if (!method.equals("PATCH")) {
                con.setRequestMethod(method);
            } else {
                con.setRequestProperty("x-http-method-override", method);
            }
            con.setRequestProperty("Charset", DEFAULT_CHARSET);
            System.out.println(url);
            System.out.println(request);
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                con.setRequestProperty(entry.getKey(), entry.getValue().trim());
            }
            System.out.println(contentType);

            con.setRequestProperty("Content-Type", contentType);
            if (method.equals("POST")) {
                OutputStream os = con.getOutputStream();
                os.write(request.getBytes());
                os.flush();
                os.close();
            } else {
                con.connect();
            }
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK || con.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                StringBuilder sb = new StringBuilder(con.getContentLength() == -1 ? 1024 * 5 : con.getContentLength());
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                for (String line = br.readLine(); line != null; line = br.readLine()) {
                    sb.append(line);
                }
                System.out.println(sb.toString());
                return sb.toString();
            }
            // 获取错误信息
            StringBuilder sb = new StringBuilder(con.getContentLength() == -1 ? 1024 * 5 : con.getContentLength());
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                sb.append(line);
            }
            System.out.println(sb.toString());

        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        return null;
    }

    public static String post(String url, String xmlStr) {
        HttpsURLConnection httpsURLConnection = null;
        try {
            httpsURLConnection = (HttpsURLConnection) (new URL(url)).openConnection();
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setRequestProperty("Content-Length", String.valueOf(xmlStr.getBytes().length));
            httpsURLConnection.setUseCaches(false);

            httpsURLConnection.getOutputStream().write(xmlStr.getBytes("GBK"));
            httpsURLConnection.getOutputStream().flush();
            httpsURLConnection.getOutputStream().close();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}