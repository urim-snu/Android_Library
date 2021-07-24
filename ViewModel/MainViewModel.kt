import android.view.View
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody

class MainViewModel(val database: LikedObjectDao) : ViewModel() {

    // How to load Data from Database
    private val _likedObjectList = Transformations.map(database.getAllLikedObject()) { it.map(LikedObject::ObjectId
    ) }
    val likedObjectList: LiveData<List<Int>>
        get() = _likedObjectList

    private val _searchHisToryList = database.getAllSearchHistory()
    val searchHisToryList: LiveData<List<SearchHistory>>
        get() = _searchHisToryList

    // Object List at Quick Menu
    private val _overlapObjectList = MutableLiveData<List<Object>>()
    val overlapObjectList: LiveData<List<Object>>
        get() = _overlapObjectList
    fun setOverlapObjectList(Objects: ArrayList<Object>) {
        _overlapObjectList.value = Objects
    }
    fun clearOverlapObjectList() {
        _overlapObjectList.value = listOf()
    }

    /**
     * Communicate with Server
     */

    // 받은 Json을 parsing 해서 SearchResult Type으로 넣어준다.
    fun getSearchResult(searchText: String?) {
        if (searchText != null) {
            if (searchText == "") {
                clearSearchResultList()
            } else {
                viewModelScope.launch {
                    val requestBody = RequestBody.create(MediaType.parse("text/plain"), searchText)
                    try {
                        val SearchResult = Api.retrofitService.search(requestBody).searched_list
                        _searchResultList.value = SearchResult
                    } catch (e: Exception) {
                        Log.e("search", "Failure: ${e.message}")
                    }
                }
            }
        }
    }

    /**
     * Communicate with SQLite
     */


    fun clearSearchHistoryList(){
        viewModelScope.launch {
            database.clearSearchHistory()
        }
    }

    fun deleteSearchHistory(id: Long){
        viewModelScope.launch {
            database.deleteSearchHistory(id)
        }
    }

}