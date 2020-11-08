package OATest.APITest;

import OATest.APITest.ConnectToAD;

public class App 
{
    public static void main( String[] args )
    {
    	
    	ConnectToAD connectToAD = new ConnectToAD();
    	 try {
    		 //for jmeter test usage add 100 test user 
    		 /* for(int i =301;i<303;i++) {
    		 connectToAD.addUser("test"+i);
    		 }*/
		     System.out.println( connectToAD.returnADUserList().size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
}
