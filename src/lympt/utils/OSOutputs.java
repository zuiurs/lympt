package lympt.utils;

import java.io.BufferedReader;

public class OSOutputs {

	BufferedReader basic_output;
	BufferedReader error_output;
	
	public OSOutputs() {
		this(null, null);
	}
	
	public OSOutputs(BufferedReader basic, BufferedReader error) {
		basic_output = basic;
		error_output = error;
	}
	
	public String toBasicOutput() {
		String basic = buildString(basic_output);
		return basic;
	}

	public String toErrorOutput() {
		String error = buildString(error_output);
		return error;
	}
	
	private String buildString(BufferedReader reader) {
		String result = "";
		boolean isFirstLine = true;
		
		while (true) {
			try {
				String line = reader.readLine();
				
				if (isFirstLine) {
					result += line;
					isFirstLine = false;
				}
				else if (line != null) {
					result += "\n" + line;
				}
				else {
					break;
				}
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}
		
		return result;
	}
	
	public String toString() {
		String output = "";
		
		// 標準出力をまとめる
		output += "[Basic Output]\n";
		output += toBasicOutput();
		
		// 標準エラー出力をまとめる
		output += "[Error Output]\n";
		output += toErrorOutput();
		
		return output;
	}
}
