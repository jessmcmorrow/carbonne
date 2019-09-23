import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Controller {
    public static Database database = new Database();
    final ShoppingList list = new ShoppingList();
    Calculations calculations = new Calculations();
    String product = "";
    static double carbonTotalForList = 0.0;
    final List<String> productNamesAsStrings = new ArrayList<String>();
    final List<Product> listOfProductObjects = new ArrayList<Product>();
    MealInfo mealDetails = new MealInfo();
    static Map<MealList, List<MealInfo>> recipesMap;

    public Controller(Database database) {
        Controller.database = database;
    }

    public void createShoppingList() throws Exception {
        XMLReader xmlReader = new XMLReader();
        final ArrayList<Product> shoppingList = xmlReader.parseShoppingListXML();
        for (Product product : shoppingList) {
            list.addProduct(product);
        }
        for (Product product : shoppingList) {
            listOfProductObjects.add(product);
        }
        Calculations.createPerson();
        calculations.calculateCarbon();
        database.createNewTable();
        for (int i = 0; i < shoppingList.size(); i++) {
            product = shoppingList.get(i).getName();
            double quantityInKg = shoppingList.get(i).getQuantity();
            String country = shoppingList.get(i).getCountry();
            database.pullCarbonStats(product, quantityInKg, country);
            carbonTotalForList = database.carbonTotalForShop;
            list.setFootPrint(carbonTotalForList);
            productNamesAsStrings.add(product);
        }
        try {
            database.uploadCarbonStats(list.getFootPrint(), database.setListID(), User.getQuantityOfPeople());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String makeMealMap(String mealName) throws Exception {
        String recipeValues = "";
        recipesMap = MealInfo.createMap();
        List<MealInfo> outcomes = recipesMap.get(new MealList(mealName));
        recipeValues = "The calories of the recipe you searched for is" + outcomes.get(0).calories + "and the carbon is"
                + outcomes.get(0).carbon + " grams. The recipe is" + outcomes.get(0).recipe;
        return recipeValues;
    }

    public String transport(double actualCarbon, double carbonLimit) {
        String advice = "";
        double allowedDailyCarbon = Double.parseDouble(calculations.considerDiet());
        String stringCarbon = String.format("%.2f", actualCarbon);
        String carCarbonEquivalent = String.format("%.2f", calculations.considerTransport(actualCarbon));
        advice = "The estimated allowed amount of co2eq for your daily shopping list is " + allowedDailyCarbon
                + "kg.\nThe actual amount of carbon you have used is " + stringCarbon
                + "kg. This estimated allowed amount is judged by examining your"
                + " necessary calories and the quantity of people you are cooking for.\nYour shopping list is roughly the same as driving "
                + carCarbonEquivalent + " miles by car. "
                + "\nTo put this into perspective 1kg of lamb has co2eq the same as driving 91 miles by car.";
        if (actualCarbon <= carbonLimit) {
            advice += "\nConsidering your diet, your shopping list is roughly the right level or below of carbon for today's food intake.";
        } else {
            advice += "\nYour shopping list's carbon is too high. You should attempt to reduce it.\nThis can be done by purchasing locally grown veg or reducing the dairy/meat on your shopping list.";
        }
        return advice;
    }
}
