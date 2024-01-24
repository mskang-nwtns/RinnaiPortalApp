package kr.co.rinnai.dms.common.task;

public interface IAsyncExecutorAware<T> {

	public void setAsyncExecutor(AsyncExecutor<T> asyncExecutor);
}
