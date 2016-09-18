package nd.im.control;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/5/24.
 */
public class Test {

    public static void main(String[] args) {
        System.out.println("Default Charset=" + Charset.defaultCharset());
        System.out.println("file.encoding=" + System.getProperty("file.encoding"));
        System.out.println("Default Charset=" + Charset.defaultCharset());
        System.out.println("Default Charset in Use=" + getDefaultCharSet());
        System.out.println("Default Charset=" + Charset.defaultCharset());
        getDate();
    }

    {
        System.out.println("chushihua");
    }

    Test() {
        System.out.println("chushihua static");
    }

    private static String getDefaultCharSet() {
        OutputStreamWriter writer = new OutputStreamWriter(new ByteArrayOutputStream());
        String enc = writer.getEncoding();

        String n[] = {"sd","sdsd"};
        String[] dn = {"dsfs", "s"};

        System.out.println("Default Charset in Use="+n[0]);
        System.out.println("Default Charset in Use="+dn[0]);


        return enc;
    }

    private static void getDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = new Date();

        String pri = simpleDateFormat.format(date);

        System.out.println(pri);
    }
}
