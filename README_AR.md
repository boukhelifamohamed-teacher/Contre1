# تسيير الاختبارات الفصلية — Android

نسخة Cordova Android بواجهة أفقية شبيهة بواجهة الكمبيوتر.

## الطباعة
تم استبدال `cordova-plugin-printer` القديم بإضافة NativePrint صغيرة متوافقة مع Android الحديث. عند الضغط على أزرار الطباعة، يستخدم التطبيق Android Print Framework مباشرة من WebView، مع احترام تنسيق A4 واتجاه الصفحة.

## البناء عبر GitHub
1. ارفع المشروع إلى مستودع GitHub.
2. افتح **Actions**.
3. اختر **Build Android APK**.
4. اضغط **Run workflow**.
5. حمّل `Tasyir-Alikhtibarat.apk` من Artifacts.

لا تضف `cordova-plugin-printer` القديم إلى المشروع.
