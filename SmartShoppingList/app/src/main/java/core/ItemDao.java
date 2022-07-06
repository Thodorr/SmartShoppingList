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

    @Query("SELECT * FROM item WHERE id = :id")
    Item getItem(int id);

    @Insert
    void insertItem(Item item);

    @Delete
    void deleteItem(Item item);

    @Update
    void updateItem(Item item);
}
