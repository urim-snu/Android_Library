
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import com.package.maps.R
import com.package.maps.base.OnBackPressListener

open class BaseWebView : Fragment(), OnBackPressListener {

    private lateinit var webView:WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Construct WebView
        webView = WebView(requireContext())

        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            useWideViewPort = true
            cacheMode = WebSettings.LOAD_NO_CACHE
            setAppCacheEnabled(false)
        }
        webView.apply {
            loadUrl(getPageUrl())
            overScrollMode = View.OVER_SCROLL_NEVER
            scrollBarSize = 0

            webViewClient = WebViewClient()
            webChromeClient = object : WebChromeClient() {

                // File Upload
                override fun onShowFileChooser(
                    webView: WebView?,
                    filePathCallback: ValueCallback<Array<Uri>>?,
                    fileChooserParams: FileChooserParams?
                ): Boolean {
                    mFilePathCallback = filePathCallback
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.addCategory(Intent.CATEGORY_OPENABLE)
                    intent.type = "image/*"
                    startActivityForResult(intent, 0)
                    return true
                }
            }

            // 텍스트 선택을 막기 위한 코드
            setOnLongClickListener { return@setOnLongClickListener true }
        }

        // JavaScript에서 Kotlin Function을 Call하기 위해 WebAppInterface를 구현한다.
        if (getWebAppInterfaceCallback() != null){
            webView.addJavascriptInterface(WebAppInterface(requireContext(), webView, getWebAppInterfaceCallback()), "Android")
        }else {
            webView.addJavascriptInterface(WebAppInterface(requireContext(), webView), "Android")
        }
        return root
    }

    open fun getWebAppInterfaceCallback():  WebAppInterface.OnWebInteractionCallback? {
        return null
    }

    open fun getPageUrl(): String? {
        return BASE_URL
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.e("resultCode:: ", resultCode.toString());
        if(requestCode == 0 && resultCode == Activity.RESULT_OK){
            mFilePathCallback?.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
            mFilePathCallback = null;
        }else{
            mFilePathCallback?.onReceiveValue(null);
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        var mFilePathCallback: ValueCallback<Array<Uri>>? = null
        val BASE_URL = ""
    }

    // WebView에서 Back Button을 뒤로 가기로 수행하기 위해서
    override fun onBackPressed(): Boolean {
        val goBackAllowed = (webView.canGoBack())
        if(webView.canGoBack()){
            webView.goBack()
        }
        return goBackAllowed
    }

}