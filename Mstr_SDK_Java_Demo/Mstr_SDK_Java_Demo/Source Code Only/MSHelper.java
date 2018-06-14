package com.helper.pkg;

import com.microstrategy.web.objects.*;
import java.io.*;
import java.net.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import org.xml.sax.InputSource;
import org.w3c.dom.*;
import org.w3c.dom.CharacterData;

public class MSHelper {
	String serverName;
	String projectName;
	String userName;
	String userPassword;
	String baseURL;
	String sessionState;
	String styleName;
	String currentReportURL;
	
	public MSHelper()
	{
		// Default MicroStrategy Connection Info
		serverName = "<YourServerName>";
		projectName = "<YourProjectName>";
		userName = "<YourUserName>";
		userPassword = "<YourPassword>";
		baseURL = "http://<YourServer>/MicroStrategy/asp/taskAdmin.aspx?"; // ASP Version of Mstr Web
		styleName = "CustomXMLReportStyle"; // This is a Mstr plugin
		
		serverName = "aeroms10indev";
		projectName = "EDW Teradata Dev";
		userName = "administrator";
		userPassword = "";
		baseURL = "http://localhost/MicroStrategy/asp/taskAdmin.aspx?"; // ASP Version of Mstr Web
		styleName = "CustomXMLReportStyle"; // This is a Mstr plugin
	}

	private static String getCharacterDataFromElement(Element e) {
	    Node child = e.getFirstChild();
	    if (child instanceof CharacterData) {
	       CharacterData cd = (CharacterData) child;
	       return cd.getData();
	    }
	    return "?";
	  }
	
	private Document GetContentXML(String sUrl1) throws Exception
	{
		URL uURL;
		StringBuffer sResult;
		sResult = new StringBuffer("");
		
		// Get XML report results from MicroStrategy
		try {
			uURL = new URL(sUrl1);
			BufferedReader oReader = new BufferedReader(new InputStreamReader(uURL.openStream()));
			String sInputLine = null;
			
			while((sInputLine = oReader.readLine()) != null)
			{
				// Skip task response start/end tags
				if(!sInputLine.contains("taskResponse"))
				{
					System.out.println("XML Line: " + sInputLine); // DEBUG
					sResult.append(sInputLine);
				}
			}
			
			oReader.close();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Process XML document
		String strXML = sResult.toString();
		Document returnDoc = null;
		try {
			DocumentBuilderFactory dbf =
		            DocumentBuilderFactory.newInstance();
		        DocumentBuilder db = dbf.newDocumentBuilder();
		        InputSource is = new InputSource();
		        is.setCharacterStream(new StringReader(strXML));
		        System.out.println("Got char stream (is)"); // DEBUG
		        System.out.println("strXML: " + strXML); // DEBUG
		        
		        Document doc = db.parse(is);
		        System.out.println("Got parsed document (doc)"); // DEBUG
		        
		        NodeList nodes = doc.getElementsByTagName("mstr-report");
		        System.out.println("Got mstr-report elements"); // DEBUG

		        // Iterate the report
		        for (int i = 0; i < nodes.getLength(); i++) {
		           Element element = (Element) nodes.item(i);

		           NodeList name = element.getElementsByTagName("report-name");
		           Element line = (Element) name.item(0);
		           System.out.println("Report Name: " + getCharacterDataFromElement(line));

		           NodeList title = element.getElementsByTagName("report-id");
		           line = (Element) title.item(0);
		           System.out.println("Report ID: " + getCharacterDataFromElement(line));
		        }
		        returnDoc = doc;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return returnDoc;
	}
	
	public Document GetReportXMLDocument(String reportID) throws TransformerException
	{
		// Construct report URL
		String mstrURL = this.baseURL;
		mstrURL += "taskId=reportDataService&taskEnv=xml&taskContentType=xml";
		mstrURL += "&server=" + this.serverName;
		mstrURL += "&project=" + this.projectName.replace(" ", "+");
		mstrURL += "&userid=" + this.userName;
		mstrURL += "&password=" + this.userPassword;
		mstrURL += "&styleName=" + this.styleName;
		mstrURL += "&reportID=" + reportID;
		
		// Save report URL to property
		this.currentReportURL = mstrURL;
		
		// Execute report and get results as XML document
		Document reportXMLDoc = null;
		try {
			// Get URL as string
			URL uMstrURL = new URL(mstrURL);
			String sMstrURL = uMstrURL.toString();
			
			// Get XML document
			reportXMLDoc = this.GetContentXML(sMstrURL.toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return reportXMLDoc;
	}
	
}
