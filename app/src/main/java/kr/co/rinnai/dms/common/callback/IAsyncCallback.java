package kr.co.rinnai.dms.common.callback;

public interface IAsyncCallback<T> {

	/**
	 * task가 정상적으로 끝났을 때의 메소드 선언
	 * @param result  제네릭 형태의 결과 변수
	 */
	public void onResult(T result);
	
	/**
	 * task 진행중 error 및 exception 발생시 메소드 선언
	 * @param e  exception 변수
	 */
	public void exceptionOccured(Exception e);
	
	/**
	 * task 진행중 취소 발생시 메소드 선언
	 */
	public void cancelled();

}
