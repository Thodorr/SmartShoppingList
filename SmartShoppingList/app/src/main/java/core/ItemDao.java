package core;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ItemDao {
    @Query("SELECT * FROM item")
    LiveData<List<Item>> getAll();

    @Query("SELECT * FROM item WHERE itemID = :id")
    Item getItem(int id);

    @Query("SELECT * FROM item WHERE name LIKE '%' || :search || '%'")
    List<Item> getBySearch(String search);

    @Query("SELECT * FROM item WHERE ean = :ean")
    Item checkForEan(String ean);

    @Insert
    void insertItem(Item item);

    @Delete
    void deleteItem(Item item);

    @Update
    void updateItem(Item item);
}
