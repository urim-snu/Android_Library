import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson

@Entity(tableName = "table_name")
data class EntityName(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var poiId: Int = -1
)

@Entity(tableName = "second_table_name")
data class AnotherEntityName(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "timeStamp")
    val timeStamp: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "object_to_json")
    val someObject: CustomClass
)

// @TypeConverter를 통해 DB에 복잡한 오브젝트를 어떻게 저장할 지를 미리 지정해줄 수 있다.
class Converters {
    @TypeConverter
    fun CustomObjectToJson(value: CustomClass): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToCustomObject(value: String): CustomClass = Gson().fromJson(value, CustomClass::class.java)
}
