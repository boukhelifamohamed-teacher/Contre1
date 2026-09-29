package dz.boukhelifa.nativeprint;

import android.content.Context;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
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
                final WebView source = (WebView) webView.getView();
                WebView printWebView = new WebView(cordova.getActivity());
                printWebView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        PrintManager pm = (PrintManager) cordova.getActivity().getSystemService(Context.PRINT_SERVICE);
                        PrintDocumentAdapter adapter = view.createPrintDocumentAdapter(jobName);
                        PrintAttributes.MediaSize size = "landscape".equalsIgnoreCase(orientation)
                                ? PrintAttributes.MediaSize.ISO_A4.asLandscape()
                                : PrintAttributes.MediaSize.ISO_A4.asPortrait();
                        PrintAttributes attrs = new PrintAttributes.Builder()
                                .setMediaSize(size)
                                .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                                .build();
                        pm.print(jobName, adapter, attrs);
                        callbackContext.success();
                    }
                });
                String js = "javascript:(function(){document.title=" +
                        org.json.JSONObject.quote(jobName) + ";})()";
                source.evaluateJavascript(js, value -> {
                    source.post(() -> {
                        String html = "<!doctype html><html><head><meta charset='utf-8'><style>" +
                                "@page{size:A4 " + ("landscape".equalsIgnoreCase(orientation) ? "landscape" : "portrait") + ";margin:10mm}" +
                                "body{margin:0}" +
                                "</style></head><body>" +
                                "<script>document.write(window.document.getElementById('printArea')?window.document.getElementById('printArea').innerHTML:window.document.body.innerHTML);</script>" +
                                "</body></html>";
                        printWebView.loadDataWithBaseURL("file:///android_asset/www/", html, "text/html", "UTF-8", null);
                    });
                });
            } catch (Exception e) {
                callbackContext.error(e.getMessage() == null ? "Printing failed" : e.getMessage());
            }
        });
        return true;
    }
}
