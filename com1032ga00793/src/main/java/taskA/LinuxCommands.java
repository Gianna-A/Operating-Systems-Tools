package taskA;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class LinuxCommands {

	public LinuxCommands() {
		super();
	}

	public String pipeCheck(String command, String commandInput) {
		String result = "";
		ArrayList<String> sepCommand = new ArrayList<String>();
		for (String each : command.split("\\s+")) {
			sepCommand.add(each.trim());
		}
		if (sepCommand.get(0).equals("cat")) {
			result = outputCat(commandInput);
		} else if (sepCommand.get(0).equals("uniq")) {
			result = outputUniq(commandInput);
		} else if (sepCommand.get(0).equals("wc")) {
			if (sepCommand.size() == 2) {
				result = outputWCL(commandInput);
			} else {
				result = outputWC(commandInput);
			}
		} else if (sepCommand.get(0).equals("sort")) {
			result = outputSort(commandInput);
		} else if (sepCommand.get(0).equals("cut") && sepCommand.contains("-f") && sepCommand.contains("-d")) {
			int fieldIndex = sepCommand.indexOf("-f") + 1;
			int delimiterIndex = sepCommand.indexOf("-d") + 1;
			String delimiter = sepCommand.get(delimiterIndex);
			delimiter = delimiter.replace("'", "").replace("\"", "");
			String field = sepCommand.get(fieldIndex);
			result = outputCut(field, delimiter, commandInput);
		} else if (sepCommand.get(0).equals("cut") && sepCommand.contains("-f") && !sepCommand.contains("-d")) {
			int fieldIndex = sepCommand.indexOf("-f") + 1;
			String field = sepCommand.get(fieldIndex);
			result = outputCut(field, ",", commandInput);
		}

		return result;
	}

	public String outputCut(String fieldStr, String delim, String commandInput) {
		boolean containsCom = false;
		int field = 0;
		int lowBound = 0;
		int upBound = 0;
		String totalOutput = "";
		ArrayList<Integer> trimComma = new ArrayList<Integer>();
		if (commandInput.equals(TaskA.filePath)) {
			if (fieldStr.length() == 1) {
				field = Integer.parseInt(fieldStr) - 1;
			} else if (fieldStr.contains(",")) {
				String[] trimmedComma = fieldStr.split(",");
				for (String each : trimmedComma) {
					trimComma.add(Integer.parseInt(each.trim()) - 1);
				}
				containsCom = true;
			} else if (fieldStr.length() > 1) {
				String[] trimmedField = fieldStr.split("-");
				for (String each : trimmedField) {
					each.trim();
				}
				lowBound = Integer.parseInt(trimmedField[0]) - 1;
				upBound = Integer.parseInt(trimmedField[1]) - 1;

			}
			try {
				BufferedReader bReader = new BufferedReader(new FileReader(TaskA.filePath));
				String line;
				if (fieldStr.length() == 1) {
					while ((line = bReader.readLine()) != null) {
						String[] words = line.split(delim);
						totalOutput += words[field];
						if (bReader.ready()) {
							totalOutput += "\n";
						}

					}
					bReader.close();
				} else if (containsCom) {
					///
					while ((line = bReader.readLine()) != null) {
						String result = "";
						String[] words = line.split(delim);
						for (int each : trimComma) {
							result += words[each];
							if (each != trimComma.get(trimComma.size() - 1)) {
								result += delim;
							}
						}
						totalOutput += result;
						if (bReader.ready()) {
							totalOutput += "\n";
						}
					}
					bReader.close();
				} else {
					while ((line = bReader.readLine()) != null) {
						String result = "";
						String[] words = line.split(delim);
						for (int i = lowBound; i <= upBound; i++) {
							result += words[i];
							if (i < upBound) {
								result += delim;
							}
						}
						totalOutput += result;

						if (bReader.ready()) {
							totalOutput += "\n";
						}

					}
					bReader.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			String[] lines = commandInput.split("\n");
			if (fieldStr.length() == 1) {
				field = Integer.parseInt(fieldStr) - 1;
				for (String line : lines) {
					String[] words = line.split(delim);
					totalOutput += words[field] + "\n";
				}
			} else if (fieldStr.contains(",")) {
				///
				String[] trimmedComma = fieldStr.split(",");
				for (String each : trimmedComma) {
					trimComma.add(Integer.parseInt(each.trim()) - 1);
				}
				for (String line : lines) {
					String result = "";
					String[] words = line.split(delim);
					for (int each : trimComma) {
						result += words[each];
						if (each != trimComma.get(trimComma.size() - 1)) {
							result += delim;
						}
					}
					totalOutput += result + "\n";
				}

			} else {
				String[] trimmedField = fieldStr.split("-");
				for (String each : trimmedField) {
					each.trim();
				}
				lowBound = Integer.parseInt(trimmedField[0]) - 1;
				upBound = Integer.parseInt(trimmedField[1]) - 1;
				for (String line : lines) {
					String result = "";
					String[] words = line.split(delim);
					for (int i = lowBound; i <= upBound; i++) {
						result += words[i];
						if (i < upBound) {
							result += delim;
						}
					}
					totalOutput += result + "\n";
				}
			}
		}
		return totalOutput;
	}

	public String outputSort(String commandInput) {
		ArrayList<String> sortList = new ArrayList<String>();
		String outputTotal = "";
		if (commandInput.equals(TaskA.filePath)) {
			try {
				BufferedReader bReader = new BufferedReader(new FileReader(commandInput));
				String line;
				sortList = new ArrayList<String>();
				while ((line = bReader.readLine()) != null) {
					sortList.add(line);
				}
				bReader.close();
				Collections.sort(sortList);

				for (String each : sortList) {
					if (!each.equals(sortList.get(sortList.size() - 1))) {
						outputTotal += each + "\n";
					} else {
						outputTotal += each;
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			String[] lines1 = commandInput.split("\n");
			for (String line : lines1) {
				sortList.add(line);
			}
			Collections.sort(sortList);
			for (String each : sortList) {
				outputTotal += each + "\n";
			}

		}
		return outputTotal;
	}

	public String outputCat(String commandInput) {
		String outputTotal = "";
		boolean isFirstLine = true;
		if (commandInput.equals(TaskA.filePath)) {
			try {
				BufferedReader bReader = new BufferedReader(new FileReader(commandInput));
				String line;
				while ((line = bReader.readLine()) != null) {
					if (!isFirstLine) {
						outputTotal += "\n";
					} else {
						isFirstLine = false;
					}
					outputTotal += line;
				}
				bReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			String[] lines = commandInput.split("\n");
			for (String line : lines) {
				if (!isFirstLine) {
					outputTotal += "\n";
				} else {
					isFirstLine = false;
				}
				outputTotal += line;
			}
		}
		return outputTotal;
	}

	public String outputUniq(String commandInput) {
		String outputTotal = "";
		ArrayList<String> lines = new ArrayList<String>();
		if (commandInput.equals(TaskA.filePath)) {
			try {
				BufferedReader bReader = new BufferedReader(new FileReader(commandInput));
				String line;
				while ((line = bReader.readLine()) != null) {
					lines.add(line);
				}
				bReader.close();
				for (int i = lines.size() - 1; i > 0; i--) {
					if (lines.get(i).equals(lines.get(i - 1))) {
						lines.remove(i);
					}
				}
				for (String each : lines) {
					if (!each.equals(lines.get(lines.size() - 1))) {
						outputTotal += each + "\n";
					} else {
						outputTotal += each;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			String[] lines1 = commandInput.split("\n");
			for (String line : lines1) {
				lines.add(line);
			}
			for (int i = lines.size() - 1; i > 0; i--) {
				if (lines.get(i).equals(lines.get(i - 1))) {
					lines.remove(i);
				}
			}
			for (String line : lines) {
				outputTotal += line + "\n";
			}
		}

		return outputTotal;
	}

	public String outputWCL(String commandInput) {
		int numberOfLines = 0;
		String aLine;
		boolean ifPiped = false;
		String addNewLine = "";
		if (commandInput.equals(TaskA.filePath)) {
			try {
				BufferedReader buReader = new BufferedReader(new FileReader(commandInput));
				while ((aLine = buReader.readLine()) != null) {
					numberOfLines++;
				}
				buReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			String[] lines1 = commandInput.split("\n");
			numberOfLines = lines1.length;
			ifPiped = true;
		}
		if (ifPiped) {
			addNewLine = "\n";
		}
		return numberOfLines + " " + TaskA.filePath + addNewLine;
	}

	public String outputWC(String commandInput) {
		int numberOfBytes = 0;
		int numberOfNewLine = 0;
		int totalWords = 0;
		String aLine;
		int character;
		boolean ifPiped = false;
		String addNewLine = "";
		if (commandInput.equals(TaskA.filePath)) {
			try {
				BufferedReader buReader = new BufferedReader(new FileReader(commandInput));
				while ((character = buReader.read()) != -1) {
					numberOfBytes++;
					if ((char) character == '\n') {
						numberOfNewLine++;
					}
				}
				buReader.close();
				buReader = new BufferedReader(new FileReader(commandInput));
				while ((aLine = buReader.readLine()) != null) {
					String[] words = aLine.split("\\s+");
					totalWords += words.length;
				}
				buReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			String[] lines1 = commandInput.split("\n");
			numberOfNewLine = lines1.length - 1;
			for (String line : lines1) {
				String[] words = line.split("\\s+");
				totalWords += words.length;
				numberOfBytes += line.length();
			}
			ifPiped = true;

		}
		if (ifPiped) {
			addNewLine = "\n";
		}
		return numberOfNewLine + " " + totalWords + " " + numberOfBytes + " " + TaskA.filePath + addNewLine;
	}
}
