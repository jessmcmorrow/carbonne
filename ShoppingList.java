import java.util.ArrayList;
import java.util.Date;

public class ShoppingList {
    private ArrayList<Product> shoppingList = new ArrayList<>();
    double carbonFootPrint;
    Date date;
    int id;

    public double getFootPrint() {
        return this.carbonFootPrint;
    }

    public Date getDate() {
        return this.date;
    }

    public void setFootPrint(double footprint) throws IllegalStateException {
        if (footprint < 0)
            throw new IllegalStateException("footprint can't be negative");
        this.carbonFootPrint = footprint;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void addProduct(Product nextProduct) {
        shoppingList.add(nextProduct);
    }

    public ArrayList<Product> getList() {
        return shoppingList;
    }

    public String toString() {
        String result = "";
        for (Product product : this.shoppingList) {
            result += product.toString();
        }
        return result;
    }
}
