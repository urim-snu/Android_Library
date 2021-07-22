
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.content.Intent.ACTION_DIAL
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider


class CustomWebView : BaseWebView() {

    private lateinit var webView: WebView
    private lateinit var currentPoi: Poi
    private var isFavorite = false
    private var mPageUrl: String = BASE_URL

    val webInteractionCallback = object: WebAppInterface.OnWebInteractionCallback {
        override fun onChangePoiLike() {
        }
    }

    override fun getWebAppInterfaceCallback(): WebAppInterface.OnWebInteractionCallback? {
        return webInteractionCallback
    }

    override fun getPageUrl(): String? {
        return BaseWebView.BASE_URL.plus("/someUri")
    }
}