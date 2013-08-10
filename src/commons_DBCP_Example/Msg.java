package commons_DBCP_Example;

/**
 * 这个类支持所有代码段中的输出 可以通过property.xml里面的参数来控制这些输出到底是否显示
 * 
 * @author Administrator
 * 
 */
public class Msg {
	public static void debugMsg(Class<?> c, String msg) {
		System.out.printf("DEBUG: [%-20s]  %s\n", c, msg);
	}

	public static void debugSep() {
		System.out.println("DEBUG: *********************************************");

	}

	public static void userMsgLn(String msg) {
		System.out.println(msg);
	}

	public static void userMsgF(String format, Object... args) {
		System.out.printf(format, args);
	}

	public static void userSep(int numberOfColumns, char chr) {
		for (int i = 0; i < 21 * numberOfColumns; i++) {
			System.out.print(chr);
		}

		System.out.println();
	}

}
