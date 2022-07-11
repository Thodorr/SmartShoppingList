package core;

import android.content.Context;
import android.os.AsyncTask;


public class DatabaseFiller {
    public static void fillDatabase(Context context) {
        Supermarket market = new Supermarket();

        Item[] items = {
                new Item("Banana", market.getPosition(0), "2123441000700", "gram", "Bread"),
                new Item("Bread", market.getPosition(2), "4100550010101", "gram", "Bread"),
                new Item("Cheese", market.getPosition(3), "4041071009444", "gram", "Egg"),
                new Item("Egg", market.getPosition(3), "4100550010101", "", "Egg"),
                new Item("Ground beef", market.getPosition(4), "4006784293092", "gram", "Burger"),
                new Item("Milk", market.getPosition(3), "4388844254763", "Liter", "Egg"),
                new Item("Pizza", market.getPosition(5), "7613035384422", "Pies", "Pizza"),
                new Item("Canned Beans", market.getPosition(6), "29062645", "gram", "Meat"),
                new Item("Skewer Meat", market.getPosition(4), "4023012150521", "Skewers", "Burger"),
                new Item("Chips", market.getPosition(7), "4003586000569", "Bags", "Burger"),
                new Item("Croissant", market.getPosition(2), "8001720445876", "gram", "Croissant"),
                new Item("Ice Cream", market.getPosition(8), "4260227195460", "Milliliter", "Ice Cream"),
                new Item("Coffee", market.getPosition(2), "9100100126653", "Kilogram", "Coffee"),
        };

        ItemDao dao = AppDatabase.getDb(context).itemDao();
        AsyncTask.execute(() -> dao.insertItems(items));
    }
}
