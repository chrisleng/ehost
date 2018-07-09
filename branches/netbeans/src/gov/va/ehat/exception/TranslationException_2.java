package gov.va.ehat.exception;

public class TranslationException_2 extends Exception {

	private static final long serialVersionUID = 4002518942503134665L;
	
	private String errorMsg;
	public String getErrorMsg() {
		return errorMsg;
	}
	public TranslationException_2(String err)
	{
		super(err);
		errorMsg = err;
	}
}
