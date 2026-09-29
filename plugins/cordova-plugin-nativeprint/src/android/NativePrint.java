package dz.boukhelifa.nativeprint;

import android.content.Context;
import android.print.PrintAttributes;
import android.print.PrintManager;
import android.webkit.WebView;
import android.view.View;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

public class NativePrint extends CordovaPlugin {
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (!"print".equals(action)) return false;

        final String jobName = args.optString(0, "تسيير الاختبارات الفصلية");
        final String orientation = args.optString(1, "portrait");

        cordova.getActivity().runOnUiThread(() -> {
            try {
                View view = webView.getEngine().getView();
                if (!(view instanceof WebView)) {
                    callbackContext.error("WebView غير متاح");
                    return;
                }

                WebView printWebView = (WebView) view;
                PrintManager printManager = (PrintManager)
                        cordova.getActivity().getSystemService(Context.PRINT_SERVICE);

                if (printManager == null) {
                    callbackContext.error("خدمة الطباعة غير متاحة");
                    return;
                }

                PrintAttributes.MediaSize mediaSize = PrintAttributes.MediaSize.ISO_A4;
                if ("landscape".equalsIgnoreCase(orientation)) {
                    mediaSize = PrintAttributes.MediaSize.ISO_A4.asLandscape();
                }

                PrintAttributes attributes = new PrintAttributes.Builder()
                        .setMediaSize(mediaSize)
                        .setColorMode(PrintAttributes.COLOR_MODE_COLOR)
                        .build();

                printManager.print(
                        jobName,
                        printWebView.createPrintDocumentAdapter(jobName),
                        attributes
                );

                callbackContext.success();
            } catch (Exception e) {
                callbackContext.error(e.getMessage() == null ? "تعذر فتح الطباعة" : e.getMessage());
            }
        });

        return true;
    }
}
