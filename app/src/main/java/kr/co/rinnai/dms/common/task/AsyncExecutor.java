package kr.co.rinnai.dms.common.task;

import android.os.AsyncTask;

import java.util.concurrent.Callable;

import kr.co.rinnai.dms.common.callback.IAsyncCallback;

public class AsyncExecutor<T> extends AsyncTask<Void, Void, T> {
	
	private IAsyncCallback<T> callback;
	private Callable<T> callable;
	private Exception occuredException;

	/**
	 * AyncTask가 할 작업을 지정한다. 
	 * @param callable  할 작업
	 * @return  작업 결과
	 */
	public AsyncExecutor<T> setCallable(Callable<T> callable) {
		this.callable = callable;
		return this;
	}

	/**
	 * AsyncTask가 작업 도중, 및 작업 후에 결과로 받을 콜백을 지정한다.
	 * @param callback  결과로 받을 콜백
	 * @return  각 콜백에 대한 결과
	 */
	public AsyncExecutor<T> setCallback(IAsyncCallback<T> callback) {
		this.callback = callback;
		processAsyncExecutorAware(callback);
		return this;
	}

	@SuppressWarnings("unchecked")
	private void processAsyncExecutorAware(IAsyncCallback<T> callback) {
		if (callback instanceof IAsyncExecutorAware) {
			((IAsyncExecutorAware<T>) callback).setAsyncExecutor(this);
		}
	}

	/**
	 * AsyncTask의 수행분
	 */
	@Override
	protected T doInBackground(Void... params) {
		try {

			return callable.call();
		} catch (Exception ex) {
			this.occuredException = ex;
			return null;
		}
	}

	/**
	 * AsyncTask의 작업 수행 후
	 */
	@Override
	protected void onPostExecute(T result) {
		if (isCancelled()) {
			notifyCanceled();
		}
		if (isExceptionOccured()) {
			notifyException();
			return;
		}
		notifyResult(result);
	}

	/**
	 * AsyncTask의 작업 취소시 수행부
	 */
	private void notifyCanceled() {
		if (callback != null)
			callback.cancelled();
	}

	/**
	 * AsyncTask의 작업 도중 에러 여부를 반환한다.
	 * @return Error가 발생했다면 true, 발생하지 않았다면 false
	 */
	private boolean isExceptionOccured() {
		return occuredException != null;
	}

	/**
	 * AsyncTask의 에러 발생시 수행부
	 */
	private void notifyException() {
		if (callback != null)
			callback.exceptionOccured(occuredException);
	}

	/**
	 * AsyncTask의 작업 정상 처리 수행부
	 * @param result
	 */
	private void notifyResult(T result) {
		if (callback != null)
			callback.onResult(result);
	}

}