package info.androidhive.loginandregistration.app;

public class AppConfig {
	public static String IP_Address = "http://192.168.43.72/";
	// Server user login url
	public static String URL_LOGIN = IP_Address + "userDB/login.php";
	public static String URL_LOAD_DATA = IP_Address + "userDB/loadDB.php";
	public static String URL_LOAD_REVIEWS = IP_Address +"userDB/loadReviews.php";
	public static String URL_UPLOAD_REVIEW = IP_Address +"userDB/uploadReview.php";
	// Server user register url
	public static String URL_REGISTER = IP_Address + "userDB/register.php";
}
