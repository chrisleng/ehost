package gov.va.ehat.model;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DateUtil {
	
	private static SimpleDateFormat annotationAdminSDF = null;	
	private static SimpleDateFormat knowtatorSDF = null;
	
	public static SimpleDateFormat getAnnotationAdminSDF()
	{
		if(annotationAdminSDF==null)
		{
			annotationAdminSDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			annotationAdminSDF.setTimeZone(TimeZone.getTimeZone(""));
		}
		return annotationAdminSDF;
	}
	
	protected static SimpleDateFormat getKnowtatorSDF()
	{
		if(knowtatorSDF==null)
		{
			knowtatorSDF = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
		}
		return knowtatorSDF;
	}
}
