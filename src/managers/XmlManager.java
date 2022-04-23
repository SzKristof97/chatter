package managers;

import java.io.File;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import gui.MainGUI;
import models.User;
import server.ClientHandler;
import enums.Permission;

/**
 * This class is used to manage the XML files
 */
public class XmlManager {

    /**
     * This method is used to save the list of users
     * @param users The list of users
     * @return True if the operation was successful, false otherwise
     * @throws Exception If an error occurs
     */
    public static boolean SaveUsers(List<ClientHandler> users) {
        try{
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();

            Element root = doc.createElement("users");
            for (ClientHandler client : users) {
                root.appendChild(CreateElement(doc, client.user, root));
            }

            doc.appendChild(root);
            Save(doc, "users.xml");
        }catch(Exception e){
            return false;
        }
        return true;
    }

    /**
     * This method is used to search for a specific user
     * @param username The username
     * @return The user if found, null otherwise
     */
    public static User SearchUser(String username) {
        try{
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse("users.xml");

            Element root = doc.getDocumentElement();
            NodeList users = root.getElementsByTagName("user");
            for (int i = 0; i < users.getLength(); i++) {
                Element user = (Element) users.item(i);
                if (user.getAttribute("username").equals(username)) {
                    return new User(-1, user.getAttribute("username"), Permission.valueOf(user.getAttribute("permission")));
                }
            }
        }catch(Exception e){
            return null;
        }
        return null;
    }


    /**
     * This method create an element from a user
     * @param doc The document
     * @param user The user
     * @param root The root element
     * @return The element
     */
    private static Element CreateElement(Document doc, User user, Element root) {
        Element element = null;

        try {
            element = doc.createElement("user");
            element.setAttribute("username", user.getUsername());
            element.setAttribute("permission", user.getPermission().toString());
            root.appendChild(element);
        } catch (Exception e) {
            MainGUI.getInstance().Message("Error creating the element: " + e.getMessage());
        }

        return element;
    }

    /**
     * This method is used to save the document
     * @param doc The document
     * @return True if the operation was successful, false otherwise
     */
    private static boolean Save(Document doc, String name){
        try{
            doc.getDocumentElement().normalize();

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "1");

            transformer.transform(new DOMSource(doc), new StreamResult(new File(name)));
        }catch(Exception e){
            return false;
        }
        return true;
    }
}
