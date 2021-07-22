# JNI(Java-Native Interface)

## How to Call C++ Function in Java/Kotlin

### b.kt (Java/com/package/a/b.kt)

```kotlin
private external fun foo(int a): Int
```

### someCppFile.cpp

```c++
JNIEXPORT jint JNICALL

Java_com_package_a_b_foo(jnit a) {
    //TODO: Implement
    return someInt
}
```

## How to Call Java/Kotlin Function in C++

```c++
JNIEXPORT jobject JNICALL

Java_com_tripaimap_maps_Framework_nativeGetPoiSuggestionList(JNIEnv *env, jclass, jobject poi, jdouble r) {
    PoiReader::Poi p = jni::convertJPoiToCPoi(env, poi);
    vector<PoiReader::Poi> poiList = getNearPois(p, r);

    //Java의 ArrayList Class를 불러온다.
    jclass clsArrayList = (*env).FindClass("java/util/ArrayList");

    //ArrayList의 initialize function 과 add function의 methodID를 불러온다.
    jmethodID mCtor = (*env).GetMethodID(clsArrayList, "<init>", "()V");
    jmethodID mAdd = (*env).GetMethodID(clsArrayList, "add", "(Ljava/lang/Object;)Z");
    jobject resultArray = (*env).NewObject(clsArrayList, mCtor);

    // Java에서 내가 정의한 Poi Class의 constructor ID, class ID는 미리 캐싱해야 에러가 나지 않는다.
    jmethodID constructor = (*env).GetMethodID(g_poiClazz, "<init>",
                                               "(ILjava/lang/String;IZZDDI)V");

    // (*env).NewObject() 로 Object를 생성할 수 있다.
    for_each(poiList.begin(), poiList.end(), [&](PoiReader::Poi& poi){
        jobject newObject = (*env).NewObject(g_poiClazz, constructor, poi.id,
                                             jni::ToJavaString(env, poi.name),
                                             poi.category, poi.isFavorite,
                                             poi.isSearched, (jdouble) poi.lat,
                                             (jdouble) poi.lon, poi.sort);

        // Call[Type]Method(ClassId, MethodId, Parameter)로 Java 함수를 call 한다.
        env->CallBooleanMethod(resultArray, mAdd, newObject);
    });

    return resultArray;
}
```

Class ID 를 캐싱하지 않으면, 오류가 났다... \
그래서 앱 실행 때 미리 static하게 ClassID를 가져오는 게 좋다.

### jni_helper.cpp

```cpp
// Caching is necessary to create class from native threads.
jclass g_poiClazz;


extern "C"
{
JNIEXPORT jint JNICALL
JNI_OnLoad(JavaVM * jvm, void *)
{
  g_jvm = jvm;
  g_poiClazz = jni::GetGlobalClassRef(env, "com/tripaimap/maps/data/Poi");

  return JNI_VERSION_1_6;
}

JNIEXPORT void JNICALL
JNI_OnUnload(JavaVM *, void *)
{
  g_jvm = 0;
  JNIEnv * env = jni::GetEnv();
  env->DeleteGlobalRef(g_poiClazz);
}
} // extern "C"

```
