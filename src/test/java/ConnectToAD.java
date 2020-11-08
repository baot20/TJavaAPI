package OATest.APITest;

import  OATest.APITest.ADHostInfo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

public class ConnectToAD {
	
	Hashtable env = new Hashtable(); 
	LdapContext ctx; 
	
	ConnectToAD(){
		String url = new String("ldap://" +ADHostInfo.host + ":" + ADHostInfo.port); 
		//不带邮箱后缀名的话，会报错
		String user = ADHostInfo.userName.indexOf(ADHostInfo.domain) > 0 ?ADHostInfo. userName : ADHostInfo.userName +ADHostInfo.domain; 

		env.put(Context.SECURITY_AUTHENTICATION, "simple"); 
		env.put(Context.SECURITY_PRINCIPAL, user);
		env.put(Context.SECURITY_CREDENTIALS, ADHostInfo.password); 
		env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory"); 
		env.put(Context.PROVIDER_URL, url); 

		try {  
	    ctx = new InitialLdapContext(env, null); 
		System.out.println("验证成功!"); 
		} 
		catch (NamingException err){ 
		err.printStackTrace(); 
		System.out.println("验证失败!"); 
		}
	}
	
	public ArrayList<String>  returnADUserList() throws Exception{
		ArrayList<String> nameList = new ArrayList<String>();
		nameList=query(ctx);
        ctx.close();  
		writeTotxt( ADHostInfo.saveFilePath,nameList);
		return nameList;
	}
	
	private ArrayList<String>  query(LdapContext ctx) throws NamingException {
		//search all the users 
				SearchControls searchCtls = new SearchControls(); //Create the search controls 
				searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE); //Specify the search scope 
				String searchFilter = "objectClass=User"; //specify the LDAP search filter 
				String searchBase = "OU=geekthings,"+ADHostInfo.base; //Specify the Base for the search//搜索域节点 
			

				//Specify the attributes to return 
				String returnedAtts[] = { "sAMAccountName"}; //定制返回属性 
				searchCtls.setReturningAttributes(returnedAtts); //设置返回属性集 
				NamingEnumeration answer = null;
				try {
					answer = ctx.search(searchBase, searchFilter,searchCtls);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
				
				int totalResults = 0; 
				ArrayList<String> nameList = new ArrayList();
				while (answer.hasMoreElements()) { 
					totalResults++;
					SearchResult sr=null;
					try {
						sr = (SearchResult) answer.next();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
					System.out.println("************************************************"); 
					System.out.println(sr.getName()); 
					Attributes Attrs = sr.getAttributes(); 
					if (Attrs != null) { 
						try { 
							for (NamingEnumeration ne = Attrs.getAll(); ne.hasMore(); ) { 
								Attribute Attr = (Attribute) ne.next(); 
				                System.out.println(" ---------------"); 
					            System.out.println(" AttributeID=" + Attr.getID().toString()); 
					            //System.out.println(" AttributeValues=" + Attr.toString()); 
					            //读取属性值 
					            Enumeration values = Attr.getAll();  
					            if (values != null) { // 迭代 
					            	while (values.hasMoreElements()) { 
					            		String value = values.nextElement().toString();
					            		System.out.println(" AttributeValues=" + value); 
					            		nameList.add(value+",");
					            	} 
					            } 
					            System.out.println(" ---------------");} 
					     }
					    catch (NamingException e) { 
					    	System.err.println("Throw Exception : " + e); 
					    	
					    }
					}
				}
				System.out.println("Number: " + totalResults); 
				return nameList;
	}

	
	public void writeTotxt(String filename,ArrayList<String> content) {
		  try {
			   File file = new File(filename);
			   //file.createNewFile();

			   FileWriter fw = new FileWriter(file.getAbsoluteFile());
			   BufferedWriter  bw=new BufferedWriter(fw);
			   
			   Iterator it1 = content.iterator();
		        while(it1.hasNext()){
		        	bw.write(it1.next().toString());
		        	bw.newLine();  
		        }
		        bw.close();
		        fw.close();
			   System.out.println(" write to txt Done");

			  } catch (IOException e) {
			   e.printStackTrace();
			  }
		
	}
	
	
	public boolean addUser(String userName) throws NamingException {

	        Attributes container = new BasicAttributes();

	        Attribute objClasses = new BasicAttribute("objectClass");
	        objClasses.add("top");
	        objClasses.add("person");
	        objClasses.add("organizationalPerson");
	        objClasses.add("user");


	        String cnValue = userName;
	        Attribute cn = new BasicAttribute("cn", cnValue);
	        Attribute sAMAccountName = new BasicAttribute("sAMAccountName", userName);
	        Attribute principalName = new BasicAttribute("userPrincipalName", userName
	                +  ADHostInfo.domain);
	        Attribute uid = new BasicAttribute("uid", userName);
	        //Attribute pwd = new BasicAttribute("unicodePwd","abcd@1234");

	        container.put(objClasses);
	        container.put(sAMAccountName);
	        container.put(principalName);
	        container.put(cn);
	        container.put(uid);
	        //container.put(pwd);

	        try {
	            ctx.createSubcontext(getUserDN(cnValue, "Testing"), container);
	            return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

	private static String getUserDN(String aUsername, String aOU) {
	        return "CN=" + aUsername + ",OU=" + aOU + ",OU=Geekthings,"+ ADHostInfo.base;
	    }
}
