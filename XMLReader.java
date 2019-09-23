import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLReader {

    public User[] parsePersonXML() throws ParserConfigurationException, SAXException, IOException {
        User[] people = new User[1];
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder build = factory.newDocumentBuilder();
        Document doc = build.parse(new File("User.xml"));
        NodeList nodeList = doc.getElementsByTagName("person");

        for (int i = 0; i < nodeList.getLength(); i++) {
            User user = getPerson(nodeList.item(i));
            people[0] = user;
        }

        return people;
    }

    public ArrayList<Product> parseShoppingListXML() throws ParserConfigurationException, SAXException, IOException {
        ArrayList<Product> products = new ArrayList<Product>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File("ShoppingList.xml"));
        document.getDocumentElement().normalize();

        NodeList nList = document.getElementsByTagName("item");

        for (int i = 0; i < nList.getLength(); i++) {
            products.add(getProduct(nList.item(i)));
        }

        return (products);
    }

    private static User getPerson(Node node) {
        User user = new User();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element el = (Element) node;
            user.setPeople(Integer.parseInt(getTagValue("howManyFor", el)));
            user.setCalories(Integer.parseInt(getTagValue("calorieIntake", el)));
            user.setTransport(getTagValue("dailyTransport", el));
            user.setDiet(getTagValue("diet", el));

        }
        return user;
    }

    private static Product getProduct(Node node) {
        Product product = new Product();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            product.setName(getTagValue("name", element));
            product.setBrand(getTagValue("brand", element));
            product.setCountry(getTagValue("country", element));
            product.setQuantity(Double.parseDouble(getTagValue("quantity", element)));
        }
        return product;
    }

    private static String getTagValue(String tag, Element element) {
        NodeList nList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nList.item(0);
        return node.getNodeValue();

    }
}
