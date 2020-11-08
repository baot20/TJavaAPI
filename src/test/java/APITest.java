

import java.util.*;
import java.util.Map.Entry;

import org.json.JSONObject;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * Unit test for OA related API
 */
public class APITest 
    extends TestCase
    
{
	String baseHTTPS= "https://t-gt-api.geekthings.com.cn";
	String baseHTTP= "http://t-gt-api.geekthings.com.cn";
	
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public APITest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    
    {
        return new TestSuite( APITest.class );
    }
    
  
    /**
     * Test login pass
     * **/

    public void testLoginPass()
    {
    	String userName ="zhangl";
    	
    	JSONObject jsonObj = new JSONObject()
                .put("account",userName)
                .put("password","abcd@1234")
    			.put("device_type","iOS")
    			.put("app_type","1001");
    	
        RestAssured.given()
        .contentType("application/json")  //another way to specify content type
        .body(jsonObj.toString())   // use jsonObj toString method
        .when()
        .post(baseHTTPS+"/geek/login")
     .then()
        .assertThat()
        	.body("body.userid", org.hamcrest.Matchers.equalTo(userName))
        	.body("body.token.length()", org.hamcrest.Matchers.greaterThan(0))
        	.body("body.finochat.access_token.length()", org.hamcrest.Matchers.greaterThan(0));
        
        
        jsonObj = new JSONObject()
                .put("account",userName)
                .put("password","abcd@1234")
    			.put("device_type","Android")
    			.put("app_type","1002");
    	
        RestAssured.given()
        .contentType("application/json")  //another way to specify content type
        .body(jsonObj.toString())   // use jsonObj toString method
        .when()
        .post(baseHTTPS+"/geek/login")
     .then()
        .assertThat()
        	.body("body.userid", org.hamcrest.Matchers.equalTo(userName))
        	.body("body.token.length()", org.hamcrest.Matchers.greaterThan(0))
        	.body("body.finochat.access_token.length()", org.hamcrest.Matchers.greaterThan(0));
    }
    
    /**
     * Test login fail
     * 
     */

    public void testLoginFail()
    {
    	Map<String,String> errMap = new HashMap<String,String>();
    	errMap.put("", "abcd@1234");
    	//errMap.put(null, "abcd@1234");
    	//errMap.put("zhangl", null);
    	errMap.put("zhangl", "");
    	errMap.put("zhangl", "errorPwd");
    	
    	Iterator iterator = errMap.entrySet().iterator();
    	int i = 0; 

    	while(iterator.hasNext()) {
    	Map.Entry entry = (Entry) iterator.next();
    	String device_type ="iOS";
    	String app_type="1001";
    	
    	if(i%2==0 ) {
    		device_type ="Android";
        	app_type="1002";
    	}	
    	JSONObject jsonObj = new JSONObject()
                .put("account",entry.getKey())
                .put("password",entry.getValue())
    			.put("device_type",device_type)
    			.put("app_type",app_type);
    	
    	//System.out.println(entry.getValue());
        RestAssured.given()
        .contentType("application/json")  //another way to specify content type
        .body(jsonObj.toString())   // use jsonObj toString method
        .when()
        .post(baseHTTPS+"/geek/login")
     .then()
        .assertThat()
        	.body("errCode", org.hamcrest.Matchers.equalTo("4003"));
    	i++;
    }

    }
    


    /**
     * Test Update App version
   **/
    public void testAppUpdate()
    {
    	//ios
        RestAssured.get(baseHTTP+"/oa-update?platform=1")
        .then()
        	.body("body.platform", org.hamcrest.Matchers.equalTo(1))
            .body("body.url.length()", org.hamcrest.Matchers.greaterThan(0));
        
        //android
        RestAssured.get(baseHTTP+"/oa-update?platform=2")
        .then()
        	.body("body.platform", org.hamcrest.Matchers.equalTo(2))
            .body("body.url.length()", org.hamcrest.Matchers.greaterThan(0));



}
