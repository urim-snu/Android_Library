# How to Deal with Back Button in Fragment

Fragment에서 BackButton을 다루는 것은 자연스럽지 않다. \
Fragment를 Stack에 올려놓는 경우 Back Button이 프래그먼트를 Pop을 하는 Action이 기본으로 적용되는데, WebView 등 Fragment 안에서 다양한 기능으로 Back Button을 사용해야 하는 상황에서 아래처럼 Custom onBackPressed 를 구현할 수 있다.

### Activity.kt

```kotlin
class Activity : AppCompatActivity() {

    // override backpressed
override fun onBackPressed() {
    if (!interceptBackPress()) {
        super.onBackPressed()
    }
}

// Fragment에서 Back Button을 눌렀을 때를 다룬다
private fun interceptBackPress(): Boolean {
    val manager = supportFragmentManager
    for (tag in DOCKED_FRAGMENTS) {
        val fragment = manager.findFragmentByTag(tag)
        if (fragment != null && fragment.isResumed && fragment is OnBackPressListener) return (fragment as OnBackPressListener).onBackPressed()
    }
    return false
}

private val DOCKED_FRAGMENTS = arrayOf(Fragment::class.java.name)
}

```

interceptBackPress 내부에서 supportFragmentManager 를 통해 현재 특정 프래그먼트가 resume 중인지를 확인한다. 실제 해당 프래그먼트가 있으면 프래그먼트 클래스 내부에서 구현된 onBackPressed를 call하게 한다. 그렇지 않으면 false가 return 되어 Activity에서 구현한 액션을 수행하게 되고, 프래그먼트 내부의 함수에서 true가 return 되면 액티비티에서 구현한 액션은 수행되지 않는다.

### Fragment.kt

```kotlin
class Fragment : Fragment(), OnBackPressListener {

    override fun onBackPressed(): Boolean {
        // TODO
    }
}
```

### OnBackPressListener.java

```java
public interface OnBackPressListener
{
  /**
   * Fragment tries to process back button press.
   *
   * @return true, if back was processed & fragment shouldn't be closed. false otherwise.
   */
  boolean onBackPressed();

}
```
