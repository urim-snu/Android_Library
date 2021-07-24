private const val BASE_URL = ""


private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface ApiService {
    // 받아오는 response 에서 Return Type의 Property에 해당하는 값들만 선별하여 가져오게 된다.
    @Multipart
    @POST("search")
    suspend fun search(@Part("searchText") searchText: RequestBody): SearchedList

    @Streaming
    @GET
    suspend fun downloadFile(@Url fileUrl:String): Response<ResponseBody>
}

object Api {
    val retrofitService : ApiService by lazy { retrofit.create(ApiService::class.java) }
}