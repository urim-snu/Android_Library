import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

//Sample Dao Implementation

@Dao
interface LikedPoiDao {
    /**
     * Defines methods for using the SearchHistory class with Room.
     * Insert, Delete, GetAll, ClearAll, Get?
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchHistory(searchHistory: SearchHistory)

    @Query("DELETE FROM search_history_table WHERE id = :id")
    suspend fun deleteSearchHistory(id: Long)

    @Query("DELETE FROM search_history_table")
    suspend fun clearSearchHistory()

    @Query("SELECT * FROM search_history_table")
    fun getAllSearchHistory(): LiveData<List<SearchHistory>>

    @Query("SELECT * FROM search_history_table WHERE search_result = :conent")
    suspend fun getSearchHistory(conent: SearchResultItem): SearchHistory?
}