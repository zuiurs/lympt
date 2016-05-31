package lympt.utils;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

public class OSCommand {

	public static OSOutputs exec(String cmd) {
		BufferedReader basic_output = null;
		BufferedReader error_output = null;
		
		try {
			Runtime runtime = Runtime.getRuntime();
			String[] cmds = {"/bin/sh", "-c", cmd};
			Process p = runtime.exec(cmds);
			p.waitFor();
			basic_output = new BufferedReader(new InputStreamReader(p.getInputStream()));
			error_output = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		}
		catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return new OSOutputs(basic_output, error_output);
	}
	
	/**
	 * @param hasPrompt whether to show prompt
	 */
	public static String inputConsole(boolean hasPrompt) {
		return inputConsole(hasPrompt, "> ", false);
	}
	
	/**
	 * @param prompt character of prompt
	 */
	public static String inputConsole(String prompt) {
		return inputConsole(true, prompt, false);
	}
	
	/**
	 * @param prompt character of prompt
	 */
	public static String inputPassword(String prompt) {
		return inputConsole(true, prompt, true);
	}
	
	/**
	 * @param hasPrompt	whether to show prompt
	 * @param prompt	character of prompt
	 * @param isSecret	whether to input password mode
	 */
	private static String inputConsole(boolean hasPrompt, String prompt, boolean isSecret) {
		Console console = System.console();
		String input = null;
		if (!isSecret) {
			input = console.readLine(prompt + " ");
		} else {
			char[] str = console.readPassword(prompt +  " ");
	        input = String.valueOf(str);
		}
        return input;
		
//		/* Before Java SE 6 */
//		String input = null;
//		try {
//			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//			if (hasPrompt) {
//				System.out.print(prompt + " ");
//			}
//			input = reader.readLine();
//		} catch (Exception e) {
//			System.out.println(e.toString());
//		}
//		return input;
	}
}
