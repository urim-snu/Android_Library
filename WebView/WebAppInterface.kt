
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast
import androidx.fragment.app.findFragment
import com.package.maps.location.LocationHelper

/**  Example of WebAppInterface
 *  We can Access System dependent Action like Calling, Sharing, Opening App, etc in JavaScript by using this.
 *  How to use in JavaScript: Android.functionName()
 * /


/** Instantiate the interface and set the context  */
open class WebAppInterface(private val mContext: Context, private val webView: WebView, private val callback: OnWebInteractionCallback?) {

    constructor(context:Context, webView: WebView): this(context, webView, null)

    // not used anymore
    @JavascriptInterface
    fun requsestCurrentPosition() {
        val myPosition = LocationHelper.INSTANCE.lastKnownLocation
        webView.post(kotlinx.coroutines.Runnable {
            webView.loadUrl("javascript:setCurrentPosition("+myPosition?.latitude+", "+myPosition?.longitude+")")
        })
    }

    // not used anymore
    @JavascriptInterface
    fun launchYoutube(url: String) {
        val launch = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        val chooser = Intent.createChooser(launch, "Youtube")
        mContext.startActivity(chooser)
    }

    /** Show a toast from the web page  */
    @JavascriptInterface
    fun showToast(toast: String) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show()
    }
    @JavascriptInterface
    fun call(phoneNumber: String) {
        val telUri = "tel:".plus(phoneNumber)
        mContext.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse(telUri)))
    }
    @JavascriptInterface
    fun shareUrl(url: String) {
        val share = Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            type = "text/plain"
            putExtra(Intent.EXTRA_TITLE, "share URL")
        }, null)
        mContext.startActivity(share)
    }
    @JavascriptInterface
    fun copyToClipBoard(address: String) {
        val clipboardManager: ClipboardManager = mContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("address", address)
        clipboardManager.primaryClip = clip
    }

    @JavascriptInterface
    fun popFragment(){
        webView.findFragment<BaseWebView>().parentFragmentManager.popBackStack()
    }

    @JavascriptInterface
    fun toggleLike(){
        if (callback != null) {
            callback.onChangePoiLike()
        }
    }

    interface OnWebInteractionCallback{
        fun onChangePoiLike()
    }
}