import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class WindowFX extends Application {

    final static String you = "You";
    final static String argentina = "Argentina";
    final static String sweden = "Sweden";
    final static String uk = "UK";
    final static String bangladesh = "Bangladesh";
    final static String usa = "USA";
    Database data = new Database();
    Controller controller = new Controller(Controller.database);
    Calculations calculations = new Calculations();
    BorderPane borderPane = new BorderPane();
    GridPane resultsPane = new GridPane();
    Button largestButton;
    Button smallestButton = new Button();
    Button averageButton = new Button();
    Button carbonLimit = new Button();
    Button decreaseOrIncrease = new Button();
    Button carbonTotal = new Button();
    Button meal = new Button();
    Button transport = new Button();
    Button displayItemsOnShoppingList = new Button();
    Button viewSuggestedMeal = new Button();
    Button graphButton = new Button();
    HBox btnHbox = new HBox(10);
    HBox btn2Hbox = new HBox(10);

    @Override
    public void start(Stage primaryStage) throws Exception {
        transport.setTooltip(new Tooltip("happy now james?"));
        try {

            controller.createShoppingList();
            Calculations.createPerson();
            calculations.calculateCarbon();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        primaryStage.setTitle("Carbon");
        largestButton = new Button("Show largest value of shop:");
        largestButton.setMinSize(100, 50);
        largestButton.setWrapText(true);
        largestButton.setId("button");
        smallestButton.setText("Show smallest value of shop:");
        smallestButton.setWrapText(true);
        smallestButton.setMinSize(100, 50);
        smallestButton.setId("button");
        averageButton.setText("Show average carbon of shops:");
        averageButton.setMinSize(100, 50);
        averageButton.setWrapText(true);
        averageButton.setId("button");
        carbonLimit.setText("Check your carbon limit:");
        carbonLimit.setMinSize(100, 50);
        carbonLimit.setWrapText(true);
        carbonLimit.setId("button");
        decreaseOrIncrease.setText("Show your carbon de/increase:");
        decreaseOrIncrease.setMinSize(100, 50);
        decreaseOrIncrease.setWrapText(true);
        decreaseOrIncrease.setId("button");
        carbonTotal.setText("Show carbon of your shop:");
        carbonTotal.setMinSize(100, 50);
        carbonTotal.setWrapText(true);
        carbonTotal.setId("button");
        meal.setText("Show carbon and calories of meal:");
        meal.setMinSize(100, 50);
        meal.setWrapText(true);
        meal.setId("button");
        transport.setText("Show transport equivalent:");
        transport.setMinSize(100, 50);
        transport.setWrapText(true);
        transport.setId("button");
        displayItemsOnShoppingList.setText("Display items on list:");
        displayItemsOnShoppingList.setMinSize(100, 50);
        displayItemsOnShoppingList.setWrapText(true);
        displayItemsOnShoppingList.setId("button");
        viewSuggestedMeal.setText("See meal suggestion");
        viewSuggestedMeal.setMinSize(100, 50);
        viewSuggestedMeal.setWrapText(true);
        viewSuggestedMeal.setId("button");

        graphButton.setText("See graph");
        graphButton.setMinSize(100, 50);
        graphButton.setWrapText(true);
        graphButton.setId("button");
        btnHbox.getChildren().addAll(largestButton, smallestButton, averageButton, decreaseOrIncrease,
                viewSuggestedMeal, graphButton);
        btn2Hbox.getChildren().addAll(carbonLimit, displayItemsOnShoppingList, carbonTotal, meal, transport);
        TextArea texxt = new TextArea(" ");
        texxt.setPrefSize(400, 210);
        texxt.setWrapText(true);
        resultsPane.add(texxt, 0, 0);
        resultsPane.setAlignment(Pos.CENTER);

        borderPane.setCenter(resultsPane);
        borderPane.setBottom(btnHbox);
        borderPane.setTop(btn2Hbox);

        Scene scene = new Scene(borderPane, 650, 500);
        scene.getStylesheets().add("design.css");
        btnHbox.getStyleClass().add("hbox");
        primaryStage.setScene(scene);
        primaryStage.show();
        largestButton.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                texxt.setText(Controller.database.largestShopFootPrint());

            }
        });
        smallestButton.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                texxt.setText(Controller.database.lowestShopFootPrint());

            }
        });
        averageButton.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                texxt.setText("Your average is " + data.averageCarbonFootPrint() + "kg");

            }
        });

        carbonLimit.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                try {
                    texxt.setText(Calculations.createPerson().toString() + " your limit is "
                            + calculations.considerDiet() + "kg of carbon.");

                } catch (ParserConfigurationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (SAXException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        decreaseOrIncrease.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                texxt.setText(data.calculateDisparityThree());
            }
        });
        carbonTotal.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                double recentCarbon = Controller.database.calculateDisparitySecond();
                DecimalFormat df = new DecimalFormat("#.##");
                texxt.setText("The total amount of carbon used in your shop is " + df.format(recentCarbon) + "kg.");
            }
        });
        meal.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                String userInput = texxt.getText();
                try {
                    texxt.setText(controller.makeMealMap(userInput));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        transport.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {

                double carbonLimit = Double.parseDouble(calculations.considerDiet());
                double actualCarbon = Controller.carbonTotalForList;
                texxt.setText(controller.transport(actualCarbon, carbonLimit));

            }
        });
        displayItemsOnShoppingList.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                String productName = "";
                double quantityInKg = 0.0;
                String countryOfOrigin = "";
                String descriptionForDisplay = "";
                String stringForAppending = "";
                for (Product product : controller.listOfProductObjects) {
                    productName = product.getName();
                    quantityInKg = product.getQuantity();
                    countryOfOrigin = product.getCountry();
                    descriptionForDisplay = Controller.database.pullCarbonStats(productName, quantityInKg,
                            countryOfOrigin);
                    if (product.getQuantity() > 0.00) {
                        stringForAppending += descriptionForDisplay + "\n";
                    }
                    texxt.setText(stringForAppending);
                }
            }
        });
        viewSuggestedMeal.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                String oneDescription = "";
                String appendedDescriptions = "";
                try {
                    Map<MealList, List<MealInfo>> c = MealInfo.createMap();

                    appendedDescriptions = "";
                    Set<MealList> outcomess = c.keySet();
                    MealList[] ml = outcomess.toArray(new MealList[outcomess.size()]);
                    for (int j = 0; j < ml.length; j++) {
                        String mealName = ml[j].toString();
                        List<MealInfo> mll = c.get(new MealList(mealName));
                        for (int k = 0; k < mll.size(); k++) {
                            for (int l = 0; l < controller.productNamesAsStrings.size(); l++) {
                                if (mll.get(k).recipe.contains(controller.productNamesAsStrings.get(l))) {

                                    oneDescription = "Recipes that you could make using some of your ingredients include "
                                            + mealName + ". The recipe is: " + mll.get(k).recipe + "\n\n";
                                    appendedDescriptions += oneDescription;
                                }

                            }

                        }

                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                texxt.setText(appendedDescriptions);
            }
        });
        graphButton.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                double averageOfCarbon = Double.parseDouble(data.averageCarbonFootPrint());
                double averageOfPeople = Double.parseDouble(data.averageQuantityOfPeople());
                double accurate = (averageOfCarbon * 365) / averageOfPeople;
                Stage stage = new Stage();
                stage.setTitle("Graph");

                final CategoryAxis xAxis = new CategoryAxis();
                final NumberAxis yAxis = new NumberAxis();
                final BarChart<String, Number> bc = new BarChart<String, Number>(xAxis, yAxis);
                bc.setTitle("Global Carbon Averages");
                xAxis.setLabel("Country");
                yAxis.setLabel("Carbon Average in KG per person per year");

                XYChart.Series series1 = new XYChart.Series();
                series1.setName("Carnivore");
                series1.getData().add(new XYChart.Data<>(you, accurate));
                series1.getData().add(new XYChart.Data<>(argentina, 2140.65));
                series1.getData().add(new XYChart.Data<>(sweden, 1493.13));
                series1.getData().add(new XYChart.Data<>(uk, 1207.22));
                series1.getData().add(new XYChart.Data<>(usa, 1680.79));
                series1.getData().add(new XYChart.Data<>(bangladesh, 149.55));

                XYChart.Series<String, Number> series2 = new XYChart.Series<>();
                series2.setName("Vegetarian");
                series2.getData().add(new XYChart.Data<>(argentina, 2108.90));
                series2.getData().add(new XYChart.Data<>(sweden, 1459.24));
                series2.getData().add(new XYChart.Data<>(uk, 1172.81));
                series2.getData().add(new XYChart.Data<>(usa, 1642.72));
                series2.getData().add(new XYChart.Data<>(bangladesh, 226.64));

                Scene scenee = new Scene(bc, 800, 600);
                bc.getData().addAll(series1, series2);
                stage.setScene(scenee);
                stage.show();

            }

        });
    }
}
