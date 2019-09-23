public class MealList implements Comparable<MealList> {
    public final String meal;

    public MealList() {
        this("");
    }

    public MealList(String meal) {
        this.meal = meal;

    }

    public int compareTo(MealList mealList) {
        int comparison = meal.compareTo(mealList.meal);
        return comparison;
    }

    public boolean equals(MealList mealList) {
        return meal.equals(mealList.meal);
    }

    public String toString() {
        return meal;
    }
}
