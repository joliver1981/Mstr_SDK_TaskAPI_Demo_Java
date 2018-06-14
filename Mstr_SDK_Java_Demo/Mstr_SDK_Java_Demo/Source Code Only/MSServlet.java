package com.demo.pkg;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import org.w3c.dom.*;
import org.w3c.dom.CharacterData;

import com.helper.pkg.MSHelper;

/**
 * Servlet implementation class MSServlet
 */
public class MSServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MSServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		
		// Create instance of the MS helper class
		MSHelper msHelper = new MSHelper();
		
		// Get report id from user input
		String reportId = request.getParameter("reportId");
		
		// Execute MicroStrategy report and display results to browser
		try
		{
			out.print("<h1>REPORT RESULTS</h1>");
			Document reportXMLDoc = msHelper.GetReportXMLDocument(reportId);
			this.outputReportHTML(reportXMLDoc, response);
		}
		catch(Exception ex)
		{
			System.out.println(ex.getMessage());
			out.print("ERROR: " + ex.getMessage());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private static String getCharacterDataFromElement(Element e) {
	    Node child = e.getFirstChild();
	    if (child instanceof CharacterData) {
	       CharacterData cd = (CharacterData) child;
	       return cd.getData();
	    }
	    return "?";
	  }
	
	private void outputReportHTML(Document doc, HttpServletResponse response) throws IOException
	{
		PrintWriter out = response.getWriter();
        NodeList nodes = doc.getElementsByTagName("mstr-report");

        for (int i = 0; i < nodes.getLength(); i++) {
           Element element = (Element) nodes.item(i);

           // Process report header
           if(i == 0)
           {
               NodeList name = element.getElementsByTagName("report-name");
               Element line = (Element) name.item(0);
               out.print("<h3>Report Name: " + getCharacterDataFromElement(line) + "</h3>");
               
               NodeList title = element.getElementsByTagName("report-id");
               line = (Element) title.item(0);
               out.print("<h3>Report ID: " + getCharacterDataFromElement(line) + "</h3>");
           }
           
           // Display report data
           String reportHTML = "<table border='1' class='report-results'>";
           
           // Get titles
           NodeList titles = element.getElementsByTagName("titles");
    	   Element line = (Element) titles.item(0);
    	   // Get list of "col" nodes
    	   NodeList attTitles = line.getElementsByTagName("col");
    	   reportHTML += "<thead style='BACKGROUND: #cae8ea;'><tr>";
    	   for (int i3=0;i3<attTitles.getLength();i3++)
    	   {
    		   line = (Element) attTitles.item(i3);
    		   String elementValue = getCharacterDataFromElement(line);
    		   reportHTML += "<th>" + elementValue + "</th>";
    	   }
    	   reportHTML += "</tr></thead>";
    	   
    	   // Get rows
           NodeList rows = element.getElementsByTagName("row");
    	   for(int iRow=0;iRow<rows.getLength();iRow++)
    	   {
               reportHTML += "<tr>";
    		   Element rowLine = (Element) rows.item(iRow);
    		   
    		   // Get row columns (tag == "col")
    		   NodeList rowCols = rowLine.getElementsByTagName("col");
    		   
    		   // Iterate row columns
    		   for(int iCol=0;iCol<rowCols.getLength();iCol++)
    		   {
    			   Element colLine = (Element) rowCols.item(iCol);
    			   String colValue = getCharacterDataFromElement(colLine);
    			   reportHTML += "<td>" + colValue + "</td>";
    		   }
        	   reportHTML += "</tr>";
    	   }  
           reportHTML += "</table>";
           
           // Output report table
           out.print(reportHTML);
        }
	}

}
