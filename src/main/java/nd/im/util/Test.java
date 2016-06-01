package nd.im.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;


public class Test {

    public static Logger log = Logger.getLogger("Test.class");

    public static SSLSocketFactory init() throws Exception {

        class MyX509TrustManager implements X509TrustManager {

            public MyX509TrustManager() throws Exception {
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }
        }
        TrustManager[] tm = {new MyX509TrustManager()};

        System.setProperty("https.protocols", "TLSv1");
        SSLContext sslContext = SSLContext.getInstance("TLSv1", "SunJSSE");
        sslContext.init(null, tm, new java.security.SecureRandom());
        SSLSocketFactory ssf = sslContext.getSocketFactory();

        return ssf;
    }

    /**
     * @param POST_URL
     * @param token
     * @return
     * @throws IOException
     */
    private static boolean sendHttpsPost(String POST_URL, String token) throws IOException {

        boolean returnVal = false;

        URL myURL = new URL(POST_URL);

        HttpsURLConnection con = (HttpsURLConnection) myURL.openConnection();

        HostnameVerifier hostNameVerify = new HostnameVerifier() {
            /**
             * Always return true
             */
            public boolean verify(String urlHostName, SSLSession session) {
                return true;
            }
        };
        //HttpsURLConnection.setDefaultHostnameVerifier(hostNameVerify);
        con.setHostnameVerifier(hostNameVerify);
        try {
            con.setSSLSocketFactory(init());
        } catch (Exception e1) {
            // throw out the exception
            throw new IOException(e1);
        }

        con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestMethod("POST");
        con.setUseCaches(false);
        con.setInstanceFollowRedirects(true);
        con.setRequestProperty("Content-Type ", " application/x-www-form-urlencoded ");

        con.connect();

        DataOutputStream out = new DataOutputStream(con.getOutputStream());

        String content = "authenticityToken=" + URLEncoder.encode(token, "utf-8");

        out.writeBytes(content);

        out.flush();
        out.close();

        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String line;

        while ((line = reader.readLine()) != null) {

            if (line.equalsIgnoreCase("ok"))
                returnVal = true;
            else
                returnVal = false;
        }
        reader.close();
        con.disconnect();


        return returnVal;
    }

    public static boolean readContentFromPost(String POST_URL, String token) throws IOException {

//        TestLogger.setLog4jLogger(log);

        POST_URL = POST_URL + "validate";

        return sendHttpsPost(POST_URL, token);
    }


}