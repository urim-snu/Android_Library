class Activity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle){
        val dataSource = TripaiDataBase.getInstance(application).likedPoiDatabaseDao
        val viewModelFactory = MainViewModelFactory(dataSource)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        // It updates List Data when the contents change
        viewModel.poiList.observe(this, {
            LOGGER.i(TAG, "poiList Changed ${it}")
            if (supportFragmentManager.findFragmentByTag(DetailFragment::class.java.name) != null) {
                poiListViewAdapter.submitList(null)
            }
            poiListViewAdapter.submitList(it)
            if (it.isNotEmpty()) {
                viewModel.setPoiDescriptionVisibility(View.VISIBLE)
            } else {
                viewModel.setPoiDescriptionVisibility(View.GONE)
            }
        })
    }

}

 
// Fragment에서 ViewModel Instance를 가져올 떄는 부모 Activity로 ViewModelStoreOwner를 보내준다