import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class Calculations {
    // Creating new XMLReader object and initialising variables.
    static XMLReader XMLReader = new XMLReader();
    static int calories;
    static String diet;
    static int flights;
    static int quantity;
    static String transport;
    int allowedCarbonPerYear;
    double allowedCarbonPerDay;
    double printedDailyCarbon;

    // Method to create Person object from parsed XML file. Throwing exceptions
    // instead of using try/catch blocks at the top.
    public static User createPerson() throws ParserConfigurationException, SAXException, IOException {
        User[] users = XMLReader.parsePersonXML();
        User user = users[0];
        calories = user.getCalories();
        diet = user.getDiet();
        quantity = user.getQuantityOfPeople();
        transport = user.getTransport();
        return user;
    }

    // Method to calculate carbon. This look at the calories the person is aiming to
    // consume that day.
    // Average carbon is 2.2 tons per year on food per person. If calories is for
    // women instead (2000) carbon decreases accordingly.
    // Sets allowedCarbon by looking at quantity of people.
    // For allowedCarbonPerDay cast both allowedCarbon and days per year as double
    // to avoid integer divison. Method assumes it is not a leap year.
    public final double calculateCarbon() {

        if (calories > 2000) {
            allowedCarbonPerYear = 2200 * quantity;

        } else {
            allowedCarbonPerYear = 2000 * quantity;
        }
        allowedCarbonPerDay = (double) allowedCarbonPerYear / (double) 365;
        // String strDouble = String.format("%.2f", allowedCarbonPerDay);
        // System.out.println(strDouble);
        return allowedCarbonPerDay;

    }

    // Method to consider the diet of the user.
    // Carnivores have their carbon set to the full allowed amount. Going vegetarian
    // reduces carbon by half, while Veganism reduces carbon by another 1/3.
    // if diet.equals("vegetarian") multiply allowedCarbon by 0.5. of
    // diet.equals("vegan") cast to double so integer division does not occur,
    // divide amount by 6 and then multiply by 2 for vegan carbon estimate.
    // return double allowed carbon amount.
    public String considerDiet() {
        if (diet.equals("carnivore")) {
            printedDailyCarbon = allowedCarbonPerDay * 1;
        } else if (diet.equals("pescetarian")) {
            printedDailyCarbon = allowedCarbonPerDay * 0.65;
        } else if (diet.equals("vegetarian")) {
            printedDailyCarbon = allowedCarbonPerDay * 0.5;
        } else if (diet.equals("vegan")) {
            printedDailyCarbon = ((double) allowedCarbonPerDay / (double) 6) * (double) 2;
        }
        String formattedDailyCarbonFigure = String.format("%.2f", printedDailyCarbon);
        return formattedDailyCarbonFigure;
    }

    // Method to utilise transport input by user.
    // Method takes in carbon total of shopping list as a double. To figure out the
    // car mile equivalent
    // double can be multiplied by 2.314. This is an accurate estimate taken from
    // green eatz.
    // return double, can be inserted into System out statements. Method needs some
    // work as it performs the same function regardless of user String input.
    public double considerTransport(double convertFoodCarbonToTransportCarbon) {
        double transportMiles = convertFoodCarbonToTransportCarbon * 2.314;
        return transportMiles;
    }

}
