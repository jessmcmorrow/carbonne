public class User {

    private static int quantityOfPeople;
    private int calories;
    private String transport;
    private String diet;
    static int users;

    public static int getQuantityOfPeople() {
        return User.quantityOfPeople;
    }

    public void setPeople(int people) {
        User.quantityOfPeople = people;
    }

    public int getCalories() {
        return this.calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public String getTransport() {
        return this.transport;
    }

    public void setTransport(String transportType) throws IllegalStateException {
        if (!transportType.equals("walk") && !transportType.equals("car") && !transportType.equals("bus")
                && !transportType.equals("bike"))
            throw new IllegalStateException("this method requires a transport of type walk, car, bus or bike");
        this.transport = transportType;
    }

    public String getDiet() {
        return this.diet;
    }

    public void setDiet(String dietChoice) throws IllegalStateException {
        if (!dietChoice.equals("vegan") && !dietChoice.equals("vegetarian") && !dietChoice.equals("carnivore")
                && !dietChoice.equals("pescetarian"))
            throw new IllegalStateException(
                    "this method requires a diet of type vegetarian, vegan, carnivore or pescetarian");

        this.diet = dietChoice;
    }

    @Override
    public String toString() {
        return "Because you are cooking for " + quantityOfPeople + " and your diet type is logged as " + diet;
    }
}
