import java.sql.*;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;

public class Database {
    Connection connection = null;
    double carbonTotalForShop = 0.0;
    private int shopID;
    double carbon = 0.0;
    String strrDouble = "";
    static String averageCarbon = "x";
    static String averagePeople = "x";
    static String x = "";
    static String carbonChange = "";

    // Method to connect to database storing both tables with the general and
    // personal carbon footprint information. WORKS
    public void connectDatabase() {
        // load the JDBC driver
        String username = "m_18_2036367m";
        String password = "2036367m";
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not find JDBC Driver");
            e.printStackTrace();
            return;
        }
        // System.out.println("PostgreSQL JDBC Driver found!");
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://yacata.dcs.gla.ac.uk:5432/", username,
                    password);
        } catch (SQLException e) {
            System.out.println("Connection Failed!");
            e.printStackTrace();
            return;
        }
        if (connection != null) {
            // System.out.println("connect");
        } else {
            System.out.println("failed");
        }
    }

    // 1. Method to pull general carbon footprint stats, multiplying them by the
    // quantity found on the user's shopping list.
    // Method prints product information to the screen. If brand does not exist in
    // SQL DB abbreviated version is printed without brand info. WORKS
    public String pullCarbonStats(String productName, double quantity, String productCountry) {
        connectDatabase();
        String productBreakDown = "";
        try {
            // ? signifies value determined at runtime
            String SQL0 = "SELECT product_name, carbon, description, brand FROM shopping.product WHERE product_name = ? AND country = ?";
            PreparedStatement p = connection.prepareStatement(SQL0);
            p.clearParameters();
            p.setString(1, productName);
            p.setString(2, productCountry);
            ResultSet r = p.executeQuery();
            while (r.next()) {
                String product = r.getString(1);
                Double carbon = r.getDouble(2);
                String description = r.getString(3);
                double carbonTotal = carbon * quantity;
                String strDouble = String.format("%.2f", carbonTotal);
                String brand = r.getString(4);
                carbonTotalForShop += carbonTotal;
                strrDouble = String.format("%.2f", carbonTotalForShop);
                if (brand == null) {
                    productBreakDown = "Item: " + product + "\nDescription of item: " + description + "\n"
                            + "Carbon per kg of product: " + carbon + "kg\nCarbon footprint total from shopping list: "
                            + strDouble + "kg\n";
                } else {
                    productBreakDown = "Item: " + product + "\nBrand : " + brand + "\nDescription of item: "
                            + description + "\n" + "Carbon per kg of product: " + carbon
                            + "kg\nCarbon footprint total from shopping list: " + carbonTotal + "kg\n";
                }
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productBreakDown;
    }

    // 2. Method creates a new table for personal carbon stats if one does not
    // already
    // exist in the database. WORKS
    public void createNewTable() {
        connectDatabase();
        try {
            Statement statement = connection.createStatement();
            String sqlStringCarbonStats = "CREATE TABLE IF NOT EXISTS shopping.carbonStats ( "
                    + "shopID integer PRIMARY KEY NOT NULL," + " carbonTotal float NOT NULL, "
                    + " dateOfShop date NOT NULL," + "quantityOfPeople int NOT NULL" + ");";
            statement.executeUpdate(sqlStringCarbonStats);
        } catch (SQLException e) {
            System.out.println("Connection Failed - create new table");
            e.printStackTrace();
        }
    }

    // 3. Method uploads personal carbon stats, including the overall carbon
    // footprint
    // of your shopping list and the date the shopping list was processed by the
    // program. Status either 0 or 1 to inform user if upload was successful. WORKS
    public String uploadCarbonStats(double carbonFootPrintOfShoppingList, int shopID, int people)
            throws ParseException {
        connectDatabase();
        String shoppingStatus = "";
        int status = 0;
        // create a java calendar instance
        Calendar calendar = Calendar.getInstance();
        // get a java date (java.util.Date) from the Calendar instance.
        // this java date will represent the current date, or "now".
        java.util.Date currentDate = calendar.getTime();
        // now, create a java.sql.Date from the java.util.Date
        java.sql.Date date = new java.sql.Date(currentDate.getTime());
        people = User.getQuantityOfPeople();
        try {
            Statement statement = connection.createStatement();
            String SQLInsert = "INSERT INTO shopping.carbonStats(" + "shopID," + "carbonTotal, " + "dateOfShop,"
                    + "quantityOfPeople) " + "VALUES (" + shopID + ", " + carbonFootPrintOfShoppingList + ", '" + date
                    + "', " + people + "); ";
            status = statement.executeUpdate(SQLInsert);
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (status == 1) {
            shoppingStatus = "\nYour shopping information has been inserted successfully.\n";
        } else {
            shoppingStatus = "Failure to insert occurred";
        }
        return shoppingStatus;
    }

    // 4. Method to set the shopID in SQL database so it increases for every new
    // shopping list processed. WORKS
    public int setListID() {
        connectDatabase();
        try {
            Statement stmt = connection.createStatement();
            ResultSet lastID = stmt.executeQuery("SELECT COUNT(*) shopID FROM shopping.carbonStats");
            if (lastID.next()) {
                shopID = lastID.getInt("shopID");
            }
            lastID.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        shopID++;
        return shopID;
    }

    // 5. Method to print to screen the largest carbon footprint total from all
    // shopping lists processed. RETURNS CARBON BUT WANT DATE
    // Method is currently void but could be edited to return a type double.
    public String largestShopFootPrint() {
        connectDatabase();
        double carbon = 0.0;
        String formattedCarbon = " ";
        try {
            // ? signifies value determined at runtime
            String SQL0 = "SELECT max(carbonTotal) FROM shopping.carbonStats";
            PreparedStatement p = connection.prepareStatement(SQL0);
            p.clearParameters();
            ResultSet r = p.executeQuery();
            while (r.next()) {
                // date = r.getDate(1);
                carbon = r.getDouble(1);
                DecimalFormat df = new DecimalFormat("#.##");
                formattedCarbon = df.format(carbon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        formattedCarbon = "Your shop with the largest footprint has a value of: " + formattedCarbon + "kg.";
        return formattedCarbon;
    }

    // 6. Method to return the carbon footprint for your lowest carbon shop. RETURNS
    // CARBON BUT WANT CORRESPONDING DATE
    public String lowestShopFootPrint() {
        connectDatabase();
        String lowestFootprint = "";
        try {
            // ? signifies value determined at runtime
            String SQL0 = "SELECT min(carbonTotal) FROM shopping.carbonStats";
            PreparedStatement p = connection.prepareStatement(SQL0);
            p.clearParameters();
            ResultSet r = p.executeQuery();
            while (r.next()) {
                // date = r.getDate(1);
                double f = r.getDouble(1);
                lowestFootprint = "Your shop with the lowest footprint has a value of: " + f + "kg.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lowestFootprint;
    }

    // 7. Method to pull carbon value for specific date.
    public void carbonForSpecificDate(Date date) {
        connectDatabase();
        try {
            String SQL0 = "SELECT carbonTotal FROM shopping.carbonStats WHERE dateOfShop = ?";
            PreparedStatement p = connection.prepareStatement(SQL0);
            p.clearParameters();
            p.setDate(1, new java.sql.Date(date.getTime()));
            ResultSet r = p.executeQuery();
            System.out.println("Your carbon foot print results for shops processed on " + date + " are: ");
            while (r.next()) {
                carbon = r.getDouble(1);
                System.out.println(carbon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 8. Method to get average carbon footprint. WORKS
    public String averageCarbonFootPrint() {
        connectDatabase();
        try {
            String SQL0 = "SELECT avg(carbonTotal) FROM shopping.carbonStats";
            PreparedStatement p = connection.prepareStatement(SQL0);
            p.clearParameters();
            ResultSet r = p.executeQuery();
            while (r.next()) {
                carbon = r.getDouble(1);
                DecimalFormat df = new DecimalFormat("#.##");
                averageCarbon = df.format(carbon);
            }
            r.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return averageCarbon;
    }

    public String averageQuantityOfPeople() {
        double people = 0.0;
        connectDatabase();
        try {
            String SQL0 = "SELECT avg(quantityOfPeople) FROM shopping.carbonStats";
            PreparedStatement p = connection.prepareStatement(SQL0);
            p.clearParameters();
            ResultSet r = p.executeQuery();
            while (r.next()) {
                people = r.getDouble(1);
                DecimalFormat df = new DecimalFormat("#.##");
                averagePeople = df.format(people);
            }
            r.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return averagePeople;
    }

    // Methods to calculate the increase or decrease in carbon footprint since the
    // program has started being used.
    public double calculateDisparity() {
        double date = 0.0;
        connectDatabase();
        try {
            String SQL1 = "SELECT carbonTotal FROM shopping.carbonStats ORDER BY shopID ASC LIMIT 1";
            PreparedStatement p = connection.prepareStatement(SQL1);
            p.clearParameters();
            ResultSet r = p.executeQuery();
            while (r.next()) {
                date = r.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return date;
    }

    public double calculateDisparitySecond() {
        double date = 0.0;
        connectDatabase();
        try {
            String SQL1 = "SELECT carbonTotal FROM shopping.carbonStats ORDER BY shopID DESC LIMIT 1";
            PreparedStatement p = connection.prepareStatement(SQL1);
            p.clearParameters();
            ResultSet r = p.executeQuery();
            while (r.next()) {
                date = r.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return date;
    }

    public String calculateDisparityThree() {
        double a = calculateDisparity();
        double b = calculateDisparitySecond();
        double c = a - b;

        if (c < 0) {
            double abs = Math.abs(c);
            double percent = (abs / a) * 100;
            DecimalFormat df = new DecimalFormat("#.##");
            x = df.format(percent);
            carbonChange = "Your carbon has increased by " + x + "% since you started using the program";
        } else {
            double percent = c / a * 100;
            DecimalFormat df = new DecimalFormat("#.##");
            x = df.format(percent);
            carbonChange = "Your carbon has decreased by " + x + "% since you started using the program";
        }
        return carbonChange;
    }
}
