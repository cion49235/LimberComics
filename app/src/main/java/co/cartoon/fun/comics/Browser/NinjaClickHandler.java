package co.cartoon.fun.comics.Browser;

import android.os.Handler;
import android.os.Message;

import co.cartoon.fun.comics.View.NinjaWebView;

public class NinjaClickHandler extends Handler {
    private final NinjaWebView webView;

    public NinjaClickHandler(NinjaWebView webView) {
        super();
        this.webView = webView;
    }

    @Override
    public void handleMessage(Message message) {
        super.handleMessage(message);
        webView.getBrowserController().onLongPress(message.getData().getString("url"));
    }
}
