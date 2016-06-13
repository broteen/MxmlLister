package com.nrifintech;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ClientSideTool {

	public static void main(String[] args) {
		 try {
                
			 File dir = new File("/home/broteen/ClientSideTool/src");
			  File[] directoryListing = dir.listFiles();
			  FileWriter fw=new FileWriter("ListOfEnabledMxmls.txt");
			  BufferedWriter bw = new BufferedWriter(fw);
			  if (directoryListing != null) {
			    for (File child : directoryListing)
			    {
			    	if(child.getName().contains(".mxml"))
			    	{
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(child);
				NodeList nListScript= doc.getElementsByTagName("mx:Script");
				   Node nNodeScript = nListScript.item(1);
				   Element eElementScript = (Element) nNodeScript;
				   String funcName=null;
						
				//optional, but recommended
				//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
				doc.getDocumentElement().normalize();

				System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
						
				NodeList nList = doc.getElementsByTagName("cntrls:XenosButton");
						
				System.out.println("----------------------------");

				for (int temp = 0; temp < nList.getLength(); temp++) {

					Node nNode = nList.item(temp);
							
					System.out.println("\nCurrent Element :" + nNode.getNodeName());
					System.out.println(eElementScript.getAttribute("source"));
					
					
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {

						Element eElement = (Element) nNode;

						System.out.println("XenosButton: " + eElement.getAttribute("click"));
						System.out.println(eElement.getAttribute("id"));
					   if(eElement.getAttribute("click").contains("()"))
					   {
						   
						   System.out.println(eElementScript.getAttribute("source"));
						   funcName=eElement.getAttribute("click").replace(";", "");
						   System.out.println(funcName);
						   
						   if(!(SearchInActionScript(eElement.getAttribute("id"),funcName,eElementScript.getAttribute("source")))){
					          System.out.println(child.getCanonicalPath());
							   bw.write(child.getCanonicalPath() + " "+ eElement.getAttribute("id") + "\n");
							   
						   }
					   }
				}
				}
			    }
			  }
			  }
			  bw.close();
			    } catch (Exception e) {
				e.printStackTrace();
			    }
	}
	
	public static boolean SearchInActionScript(String id, String functionName,String fileName)
	{
		BufferedReader br;
		String line="";
		boolean enabledFlag=true;
		try{
		br = new BufferedReader(new FileReader("/home/broteen/ClientSideTool/src/" +fileName));
		while((line = br.readLine()) != null){
			if(line.contains(functionName)){
				System.out.println(line);
				break;
			}
		}
				while((line=br.readLine() )!=null){
					//System.out.println(line);
					if(line.contains(id + ".enabled")){
						System.out.println("Found It.");
						enabledFlag=false;
						 break;
					}
					else if((line=br.readLine()).contains("function")){
						break;
					}
			}
		
		
		}catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
		return enabledFlag;
	}
}