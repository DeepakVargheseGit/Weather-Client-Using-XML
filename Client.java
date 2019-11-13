package lab3;

import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

/*
 * Author - Deepak Varghese , Student ID - 1001572764, Net ID - dxv2764
 * 
 * The Client Class includes the window frame,main method,the client constructor,
 * the HTTPS URL connection and logic to parse the data retrieved from the URL 
 * 
 * References are as follows
 *  
 * HTTPS URL Connection 
 * 1)https://alvinalexander.com/blog/post/java/simple-https-example
 * 2)https://www.mkyong.com/java/java-https-client-httpsurlconnection-example/
 * 
 * XML Parsing 
 * 1)https://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
 * 2)http://theoryapp.com/parse-xml-using-dom-in-java/
 * 3)https://www.java-forums.org/xml/22674-get-xml-data-url.html
 * 
 * URL - https://graphical.weather.gov/xml/rest.php 
 * 
 */

public class Client extends JFrame 
implements WindowListener{
	
/*
 * The member area definition are as follows -
 * 
 * Latitude_area          - The area where the student inputs the latitude of the location.
 * Longitude_area         - The area where the student inputs the longitude of the location.
 * Weather_Report_Area    - The area where the weather report values are displayed.
 */
	public JTextField Latitude_area;
	public JTextField Longitude_area;
	
	 Client() {
		 
 /*
  * Creates the GUI for the Student process.		 
  */
		setTitle("Weather Report System");
		addWindowListener(this);
		setVisible(true);
		getContentPane().setLayout(null);
		
		JLabel lblLatitude = new JLabel("Latitude");
		lblLatitude.setBounds(10, 14, 65, 14);
		getContentPane().add(lblLatitude);
		
		JLabel lblLongitude = new JLabel("Longitude");
		lblLongitude.setBounds(10, 39, 63, 14);
		getContentPane().add(lblLongitude);
		
		Latitude_area = new JTextField();
		Latitude_area.setBounds(125, 11, 137, 20);
		getContentPane().add(Latitude_area);
		Latitude_area.setColumns(10);
		
		Longitude_area = new JTextField();
		Longitude_area.setBounds(125, 39, 137, 20);
		getContentPane().add(Longitude_area);
		Longitude_area.setColumns(10);
		
		JButton btnNewButton = new JButton("Get Weather");
	
		btnNewButton.setBounds(125, 70, 137, 23);
		getContentPane().add(btnNewButton);
		
		JTextArea Weather_Report_Area = new JTextArea();
		Weather_Report_Area.setBounds(125, 104, 321, 146);
		getContentPane().add(Weather_Report_Area);
		
		JLabel lblWeather_Report = new JLabel("Weather Report");
		lblWeather_Report.setBounds(10, 151, 108, 14);
		getContentPane().add(lblWeather_Report);
		btnNewButton.addActionListener(new ActionListener()
		{
/*
* addActionListener is used to perform actions when Weather Report button is clicked
*/			
			public void actionPerformed(ActionEvent e) 
			{
				
				Weather_Report_Area.setText("");
// httpsURL is the URL that we connect to get the weather report 
				String httpsURL = "https://graphical.weather.gov/xml/sample_products/browser_interface/ndfdXMLclient.php?whichClient=GmlLatLonList&gmlListLatLon="+ Latitude_area.getText() +"%2C" + Longitude_area.getText() + "&featureType=Forecast_Gml2Point&product=time-series&begin=2004-01-01T00%3A00%3A00&end=2021-12-02T00%3A00%3A00&Unit=e&Submit=Submit";
		        URL Weather_Url;
				try {
					Weather_Url = new URL(httpsURL);
// URL_Connection sets up the URL connection					
					HttpsURLConnection URL_Connection = (HttpsURLConnection)Weather_Url.openConnection();			        
			    	DocumentBuilderFactory Doc_Builder_Factory = DocumentBuilderFactory.newInstance(); // Used to parse data given in XML format
			    	DocumentBuilder Doc_Builder; 
					try 
					{
						Doc_Builder = Doc_Builder_Factory.newDocumentBuilder();
						Document Doc = Doc_Builder.parse(Weather_Url.openStream()); // Reads the data from URL in XML format
						Doc.getDocumentElement().normalize();
				    	
				    	NodeList Node_List = Doc.getElementsByTagName("gml:featureMember"); // Retrieves the data using the root node

				    	for (int temp = 0; temp < Node_List.getLength(); temp++)
				    	{

				    		Node N_Node = Node_List.item(temp);

				    		if (N_Node.getNodeType() == Node.ELEMENT_NODE)
				    		{
//Prints all the data retrieved from the URL in the Weather_Report_Area 
				    			Element Node_Element = (Element) N_Node;

				    			Weather_Report_Area.append("Maximum Temperature : " + Node_Element.getElementsByTagName("app:maximumTemperature").item(0).getTextContent()+"\n");
				    			Weather_Report_Area.append("Minimum Temperature : " + Node_Element.getElementsByTagName("app:minimumTemperature").item(0).getTextContent()+"\n");
				    			Weather_Report_Area.append("Dew Point Temperature : " + Node_Element.getElementsByTagName("app:dewpointTemperature").item(0).getTextContent()+"\n");
				    			Weather_Report_Area.append("12 Hour Probability of Precipitation : " + Node_Element.getElementsByTagName("app:probOfPrecip12hourly").item(0).getTextContent()+"\n");
				    			Weather_Report_Area.append("Wind Speed : " + Node_Element.getElementsByTagName("app:windSpeed").item(0).getTextContent()+"\n");
				    			Weather_Report_Area.append("Wind Direction : " + Node_Element.getElementsByTagName("app:windDirection").item(0).getTextContent()+"\n");
				    			Weather_Report_Area.append("Wave Height : " + Node_Element.getElementsByTagName("app:waveHeight").item(0).getTextContent()+"\n");

				    		}
				    	}
				    	
				    	URL_Connection.disconnect(); // Disconnects the URL session 
					} 				
					catch (ParserConfigurationException | SAXException e1)
					{
						e1.printStackTrace();
					}			    	
			    	
				}
				catch (MalformedURLException e1)
				{
					e1.printStackTrace();
				}
				catch (IOException e1) 
				{
					e1.printStackTrace();
				}
		        				
			}
		});
		
		setSize(500,300);
	}
	public static void main(String[] args)
	{
				new Client(); // Invokes the client class
	}
	public void windowActivated(WindowEvent arg0) 
	{
		
	}

	public void windowClosed(WindowEvent arg0) 
	{
		
	}

	public void windowClosing(WindowEvent arg0) 
	{
		
	}

	public void windowDeactivated(WindowEvent arg0) 
	{
		
	}

	public void windowDeiconified(WindowEvent arg0)
	{
		
	}

	public void windowIconified(WindowEvent arg0) 
	{
		
	}

	public void windowOpened(WindowEvent arg0)
	{
		
	}
}
