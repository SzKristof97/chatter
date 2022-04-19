package me.szkristof.chatter.managers;

import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

import me.szkristof.chatter.models.*;

public class XmlManager {

    /**
     * Save the server settings
     * @param settings The {@link ServerSettings} to save
     * @return true if the settings are successfully saved, false otherwise
     */
    public static boolean SaveServerSettings(ServerSettings settings) {
        // Create a document to hold the server settings
        Document doc;
        Element e = null;

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try{
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.newDocument();

            // Create the root element
            Element root = doc.createElement("ServerSettings");

            // Create the server name element
            e = doc.createElement("ServerName");
            e.appendChild(doc.createTextNode(settings.GetServerName()));
            root.appendChild(e);

            // Create the server port element
            e = doc.createElement("ServerPort");
            e.appendChild(doc.createTextNode(settings.GetServerPort()));
            root.appendChild(e);

            // Reset the elemt
            e = null;

            // Add the root element to the document
            doc.appendChild(root);

            // Write the document to the file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            transformer.transform(new DOMSource(doc), new StreamResult(new java.io.File("server_settings.xml")));
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Load the server settings
     * @return The {@link ServerSettings} loaded from the file, otherwise null
     */
    public static ServerSettings LoadServerSettings() {
        ServerSettings settings = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try{
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse("server_settings.xml");

            // Get the root element
            Element root = doc.getDocumentElement();

            // Get the server name element
            Element serverName = (Element)root.getElementsByTagName("ServerName").item(0);
            String serverNameValue = serverName.getTextContent();

            // Get the server port element
            Element serverPort = (Element)root.getElementsByTagName("ServerPort").item(0);
            String serverPortValue = serverPort.getTextContent();

            // Create the server settings
            settings = new ServerSettings(serverNameValue, serverPortValue);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return settings;
    }

    /**
     * Save the user informations
     * @param user The {@link User} to save
     * @return true if the user are successfully saved, otherwise false
     */
    public static boolean SaveUser(User user) {
        // Create a document to hold the user informations
        Document doc;
        Element e = null;

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try{
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.newDocument();

            // Create the root element
            Element root = doc.createElement("UserInformations");

            // Create the user element
            e = doc.createElement("User");
            e.appendChild(doc.createTextNode(user.GetName()));
            root.appendChild(e);

            // Reset the elemt
            e = null;

            // Add the root element to the document
            doc.appendChild(root);

            // Write the document to the file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            transformer.transform(new DOMSource(doc), new StreamResult(new java.io.File("user.xml")));
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Load the user informations
     * @return The {@link User} loaded from the file, otherwise null
     */
    public static User LoadUser() {
        User user = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try{
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse("user.xml");

            // Get the root element
            Element root = doc.getDocumentElement();

            // Get the user element
            Element userName = (Element)root.getElementsByTagName("User").item(0);
            String userNameValue = userName.getTextContent();

            // Create the user
            user = new User(userNameValue);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return user;
    }
    
}
