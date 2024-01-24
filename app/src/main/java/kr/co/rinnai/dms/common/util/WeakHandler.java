package kr.co.rinnai.dms.common.util;

import java.lang.ref.WeakReference;

import android.os.Handler;
import android.os.Message;

public abstract class WeakHandler<T> extends Handler {

    private final WeakReference<T> reference;

    public WeakHandler(T ref) {
        this.reference = new WeakReference<T>(ref);
    }

    @Override
    public void handleMessage(Message msg) {

        T ref = reference.get();
        if (ref != null) {
        	weakHandleMessage(ref, msg);
        }
    }

    protected abstract void weakHandleMessage(T ref, Message msg);

}
