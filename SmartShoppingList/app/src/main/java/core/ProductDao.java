package core;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProductDao
{
	@Query("SELECT * FROM product")
	LiveData<List<Product>> getAll();

	@Insert
	void insertProduct(Product product);

	@Delete
	void deleteProduct(Product product);

	@Update
	void updateProduct(Product product);
}
