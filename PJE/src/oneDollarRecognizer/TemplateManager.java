package oneDollarRecognizer;

/**
 * @author <a href="mailto:gery.casiez@lifl.fr">Gery Casiez</a>
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import mygeom.Tuple2;

import org.jdom.*;
import org.jdom.input.*;

public class TemplateManager
{
	private Vector<Template> theTemplates;
	private org.jdom.Document document;
	private Element racine;
	private String fileName;
	
	public TemplateManager(String filename) {
		theTemplates = new Vector<Template>();
		this.fileName = filename;
		loadFile(this.fileName);
	}	

	void loadFile(String filename) {
		SAXBuilder sxb = new SAXBuilder();
		
		// Open xml file
	    try {
	    	document = sxb.build(new File(filename));
	    }
	    catch(Exception e){
	    	System.out.println("Problem loading file");
	    	System.exit(-1);
	    }
	    racine = document.getRootElement();
	    
	    List<?> listTemplates = racine.getChildren("template");

	    Iterator<?> i = listTemplates.iterator();
	    // Go through each template
	    while(i.hasNext())
	    {
	    	// Get template name
	    	Element courant = (Element)i.next();
	    	String name = courant.getAttributeValue("name").toString();

	    	// Get all points
	    	Vector<Tuple2> pts = new Vector<Tuple2>();
	    	Iterator<?> c = courant.getChildren().iterator();
	    	while (c.hasNext()) {
	    	   Element point = (Element)c.next();
	    	   pts.add(new Tuple2(Integer.parseInt(point.getAttributeValue("x").toString()),
	    			   Integer.parseInt(point.getAttributeValue("y").toString())));
	       }
	    	theTemplates.add(new Template(name, pts));
	    }		
	}
	
	
	
	public void AddTemplate( String name, Tuple2 [] points)
	{
		Vector<Tuple2> pts = new Vector<Tuple2>();
		for (int i = 0; i < points.length; i++) {
			pts.add(points[i]);
		}
		theTemplates.add(new Template(name, pts));
	}
	
	public Vector<Template> getTemplates() {
		return theTemplates;
	}
	
	public void writeRawPoints2XMLFile(String name, Vector<Tuple2> points) {
		try {
		FileWriter fstream = new FileWriter("out.xml", true);
		BufferedWriter out = new BufferedWriter(fstream);
		out.write("	<template name=\"" + name + "\" nbPts=\"" + points.size() +"\">\n");
		for (int i=0; i<points.size();i++) {
			out.write("		<Point x=\"" + (int)points.get(i).getX() + "\" y=\"" +
					(int)points.get(i).getY()  +"\"/>\n");
		}
		
		out.write("	</template>\n");
		out.close();
		} catch (Exception e){//Catch exception if any
		      System.err.println("Error: " + e.getMessage());
	    }

	}	
	
}