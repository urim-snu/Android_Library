# Connecting to Network (Retrofit, Coroutine)

## How to Use

### get Json

```kotlin
// In ViewModel
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
```

### get File and Save it to Local Storage

```kotlin
// in Some Activity

val fileName = "bin/sample.txt"
val storagePath = StorageUtils.getSettingsPath() + "sample.txt"

GlobalScope.launch {
    val response = Api.retrofitService.downloadFile(fileName)
    val responseBody= response.body()
    saveFile(responseBody,storagePath)
}

fun saveFile(body: ResponseBody?, path: String):String{
    if (body==null)
        return ""
    var input: InputStream? = null
    try {
        input = body.byteStream()
        val fos = FileOutputStream(path)
        fos.use { output ->
            val buffer = ByteArray(4 * 1024) // or other buffer size
            var read: Int
            while (input.read(buffer).also { read = it } != -1) {
                output.write(buffer, 0, read)
            }
            output.flush()
        }
        return path
    }catch (e:Exception){
        LOGGER.e(TAG,e.toString())
    }
    finally {
        input?.close()
    }
    return ""
}
```
