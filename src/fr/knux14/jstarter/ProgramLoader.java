package fr.knux14.jstarter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class ProgramLoader {
	
	
	public static File progDir = new File(getRoot(), "Programs");

	private static ArrayList<Program> progList = new ArrayList<>();
	private static File configFile = new File(progDir, "config.xml");
	
	public static ArrayList<Program> load() {
		if (!progDir.exists())
			progDir.mkdir();

		progList.clear();
		if (configFile.exists()) {
			SAXBuilder sxb = new SAXBuilder();
			try {
				Document doc = sxb.build(configFile);
				Element root = doc.getRootElement();
				if (Main._VERSION.equalsIgnoreCase(root.getChild("Version").getText())){
					// Ask update
				}
				List<Element> os = root.getChildren("arch");
				for (Element e : os) 
				{
					List<Element> winApps = e.getChildren();
					for (Element app : winApps) {
						String name = app.getAttributeValue("name");
						String path = app.getAttributeValue("path");
						String icon = app.getAttributeValue("icon");
						progList.add(new Program(e.getAttributeValue("type"), name, path, icon));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			saveConfig();
			return load();
		}		
		
		return progList;
	}
	
	public static void saveConfig() {
		try
		   {
		      XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
		      Document doc = new Document();
		      Element root = new Element("CryptoKey");
		      doc.setRootElement(root);
		      
		      Element version = new Element("Version");
		      version.setText(Main._VERSION);
		      root.addContent(version);
		      
		      Element windows = new Element("arch");
		      windows.setAttribute("type", "win");
		      Element mac = new Element("arch");
		      mac.setAttribute("type", "mac");
		      Element linux = new Element("arch");
		      linux.setAttribute("type", "linux");
		      
		      for (Program p : Main.progList) {
		    	  Element e = new Element("Program");
		    	  e.setAttribute("name", p.name);
		    	  e.setAttribute("path", p.path);
		    	  e.setAttribute("icon", p.iconPath);
		    	  if(p.arch.equals("win")) windows.addContent(e);
		    	  else if(p.arch.equals("linux")) linux.addContent(e);
		    	  else if(p.arch.equals("mac")) mac.addContent(e);
		      }
		      
		      root.addContent(windows);
		      root.addContent(mac);
		      root.addContent(linux);
		      sortie.output(doc, new FileOutputStream(configFile));
		   }
		   catch (java.io.IOException e){
			   e.printStackTrace();
		   }
	}
	
	public static File getRoot() {
		return new File(System.getProperty("user.dir"));
	}
	
}
