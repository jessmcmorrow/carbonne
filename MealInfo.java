import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MealInfo {
    String recipe;
    String calories;
    String carbon;
    Map<MealList, List<MealInfo>> dataStructure;

    public MealInfo() {
        this("", "", "");
    }

    public MealInfo(String recipe, String calories, String carbon) {
        this.recipe = recipe;
        this.calories = calories;
        this.carbon = carbon;
    }

    public boolean equals(MealInfo c) {
        return recipe.equals(c.recipe) && calories.equals(c.calories) && carbon.equals(c.carbon);
    }

    public String toString() {
        return recipe + " , " + calories + " , " + carbon;
    }

    public static Map<MealList, List<MealInfo>> createMap() throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader("\\My Documents\\\\Recipes.txt"))) {
            br.readLine();
            Map<MealList, List<MealInfo>> recipeMap = new TreeMap<>();

            String line = null;
            while ((line = br.readLine()) != null && !line.equals(";;")) {
                String[] split = line.split("=");
                MealList mealList = new MealList(split[0]);
                MealInfo mealInfo = new MealInfo(split[1], split[2], split[3]);

                if (recipeMap.containsKey(mealList)) {
                    recipeMap.get(mealList).add(mealInfo);
                } else {
                    List<MealInfo> carbonCalories = new ArrayList<>();
                    carbonCalories.add(mealInfo);

                    recipeMap.put(mealList, carbonCalories);
                }
            }

            return recipeMap;

        }
    }
}
