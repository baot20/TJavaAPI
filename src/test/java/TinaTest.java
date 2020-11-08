import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONObject;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.IOException;

/**
 * Unit test for OA related API
 */
public class TinaTest extends TestCase {
    String baseHTTPS = "https://t-gt-api.geekthings.com.cn";
    String baseHTTP = "http://t-gt-api.geekthings.com.cn";

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public TinaTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()

    {
        return new TestSuite( TinaTest.class );
    }


    /**
     * Test  bing search
     *
     */
    public void testBingSearch() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("https://cn.bing.com/search?q=JAVA&PC=U316&FORM=CHROMN")
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        ResponseBody responseBody = response.body();
        System.out.println(responseBody.string());


        /**
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder().url("http://www.baidu.com").build();
        Response response = client.newCall(request).execute();
        ResponseBody responseBody = response.body();**/

    }
}