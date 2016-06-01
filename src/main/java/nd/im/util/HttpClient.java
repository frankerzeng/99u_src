package nd.im.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * @author Mir
 */
public class HttpClient {
    public static final String DEFAULT_CHARSET = "UTF-8";

    public static String request(String url, String request) throws IOException {
        HttpURLConnection con = null;
        try {
            URL u = new URL(url);
            con = (HttpURLConnection) u.openConnection();
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setRequestProperty("Connection", "Keep-Alive");
            if (request != null && !"".equals(request)) {
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                con.setRequestProperty("Charset", DEFAULT_CHARSET);
                OutputStream os = con.getOutputStream();
                os.write(request.getBytes());
                os.flush();
                os.close();
            }

            if (con.getResponseCode() == HttpURLConnection.HTTP_OK || con.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                StringBuilder sb = new StringBuilder(con.getContentLength() == -1 ? 1024 * 5 : con.getContentLength());
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                for (String line = br.readLine(); line != null; line = br.readLine()) {
                    sb.append(line);
                }
                return sb.toString();
            }
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        return null;
    }

    public static String request(String url, String request, String contentType) throws IOException {
        HttpURLConnection con = null;
        try {
            URL u = new URL(url);
            con = (HttpURLConnection) u.openConnection();
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setRequestProperty("Connection", "Keep-Alive");
            if (request != null && !"".equals(request)) {
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                con.setRequestProperty("Charset", DEFAULT_CHARSET);
                con.setRequestProperty("Content-Type", contentType);
                //con.setRequestProperty("Content-Type", "application/json");
                OutputStream os = con.getOutputStream();
                os.write(request.getBytes());
                os.flush();
                os.close();
            }

            if (con.getResponseCode() == HttpURLConnection.HTTP_OK || con.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                StringBuilder sb = new StringBuilder(con.getContentLength() == -1 ? 1024 * 5 : con.getContentLength());
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                for (String line = br.readLine(); line != null; line = br.readLine()) {
                    sb.append(line);
                }
                return sb.toString();
            }
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        return null;
    }

    public static String request(String url, String request, String contentType, String authorization, String requestMethod) throws IOException {
        HttpURLConnection con = null;
        try {
            URL u = new URL(url);
            con = (HttpURLConnection) u.openConnection();
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setRequestProperty("Connection", "Keep-Alive");

            con.setDoOutput(true);
            con.setRequestMethod(requestMethod.toUpperCase());
            con.setRequestProperty("Charset", DEFAULT_CHARSET);
            con.setRequestProperty("Content-Type", contentType);
            con.setRequestProperty("Authorization", authorization);

            if (!request.isEmpty()) {
                OutputStream os = con.getOutputStream();
                os.write(request.getBytes());
                os.flush();
                os.close();
            }

            if (con.getResponseCode() == HttpURLConnection.HTTP_OK || con.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                StringBuilder sb = new StringBuilder(con.getContentLength() == -1 ? 1024 * 5 : con.getContentLength());
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                for (String line = br.readLine(); line != null; line = br.readLine()) {
                    sb.append(line);
                }
                return sb.toString();
            }
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        return null;
    }

    public static String request(String url, String request, Map<String, String> headerMap, String requestMethod) throws IOException {
        HttpURLConnection con = null;
        try {
            URL u = new URL(url);
            con = (HttpURLConnection) u.openConnection();
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setRequestProperty("Connection", "Keep-Alive");

            con.setDoOutput(true);
            con.setRequestMethod(requestMethod.toUpperCase());
            con.setRequestProperty("Charset", DEFAULT_CHARSET);
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                con.setRequestProperty(entry.getKey(), entry.getValue().trim());
            }
            if (!request.isEmpty()) {
                OutputStream os = con.getOutputStream();
                os.write(request.getBytes());
                os.flush();
                os.close();
            }

            if (con.getResponseCode() == HttpURLConnection.HTTP_OK || con.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
                StringBuilder sb = new StringBuilder(con.getContentLength() == -1 ? 1024 * 5 : con.getContentLength());
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                for (String line = br.readLine(); line != null; line = br.readLine()) {
                    sb.append(line);
                }
                System.out.println(sb.toString());
                return sb.toString();
            }
            // 获取错误信息
            StringBuilder sb = new StringBuilder(con.getContentLength() == -1 ? 1024 * 5 : con.getContentLength());
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream(),"UTF-8"));
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

    /**
     * 下载文件
     *
     * @param requestUrl
     * @param requestMethod
     * @param savePath
     * @return
     */
    public static String requestFile(String requestUrl, String requestMethod, String savePath, String fileName) {

        String filePath = null;
        try {
            URL url = new URL(requestUrl);
            System.out.println(requestUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setRequestMethod(requestMethod.toUpperCase());
            conn.setRequestProperty("Charset", DEFAULT_CHARSET);

            String content_type = conn.getContentType();
            System.out.println("content_type========>" + content_type);

            // 文件拓展名
            String fileExt = "";

            switch (content_type) {
                case "text/plain":
                    throw new Exception("下载文件错误");
//                case "image/jpeg":
//                    fileExt = ".jpg";
//                    break;
//                case "audio/amr":
//                    fileExt = ".amr";
//                    break;
                default:
                    fileExt = "." + content_type.split("/")[1];
            }

            filePath = savePath + fileName + fileExt;

            File dir = new File(savePath);
            if (!dir.exists() && !dir.isDirectory()) {
                dir.mkdirs();
            }

            File file = new File(filePath);

            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            FileOutputStream fos = new FileOutputStream(file);

            byte[] buf = new byte[8096];
            int size = 0;

            while ((size = bis.read(buf)) != -1) {
                fos.write(buf, 0, size);
            }
            fos.close();
            bis.close();

            conn.disconnect();

            System.out.println("下载成功==" + filePath);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("下载失败");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filePath;

    }
}