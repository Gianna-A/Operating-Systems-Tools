package taskA;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

public class TaskA {
	static String filePath = "taskA.csv";

	public static void main(String[] args) {
		System.out.println("Operating Systems Coursework");
		System.out.println("Name: Gianna Addae"); // display your name in here
		System.out.println("Please enter your commands - cat, cut, sort, uniq, wc or |");

		String inputCommand = null;
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		LinuxCommands linuxCommands = new LinuxCommands();

		while (true) {
			System.out.print(">> ");
			try {
				inputCommand = console.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (inputCommand == "") {
				continue;
			}

			if (inputCommand.contains("|") && inputCommand.contains("taskA.csv")) {
				String commandOutput = "";
				ArrayList<String> pipeCommand = new ArrayList<String>();
				for (String each : inputCommand.split("\\|")) {
					pipeCommand.add(each.trim());
				}

				for (String each : pipeCommand) {
					if (each.equals(pipeCommand.get(0))) {
						commandOutput = linuxCommands.pipeCheck(each, filePath);
					} else if (each.equals(pipeCommand.get(pipeCommand.size() - 1))) {
						System.out.print(linuxCommands.pipeCheck(each, commandOutput));
					} else {
						commandOutput = linuxCommands.pipeCheck(each, commandOutput);
					}
				}

			} else {

				ArrayList<String> command = new ArrayList<String>();
				for (String each : inputCommand.split("\\s+")) {
					command.add(each.trim());
				}

				if (command.get(0).equals("cat") && command.get(1).equals("taskA.csv")) {
					System.out.println(linuxCommands.outputCat(filePath));
				} else if (command.get(0).equals("uniq") && command.get(1).equals("taskA.csv")) {
					System.out.println(linuxCommands.outputUniq(filePath));
				} else if (command.get(0).equals("wc") && command.get(1).equals("taskA.csv")) {
					System.out.println(linuxCommands.outputWC(filePath));
				} else if (command.get(0).equals("wc") && command.get(1).equals("-l")
						&& command.get(2).equals("taskA.csv")) {
					System.out.println(linuxCommands.outputWCL(filePath));
				} else if (command.get(0).equals("sort") && command.get(1).equals("taskA.csv")) {
					System.out.println(linuxCommands.outputSort(filePath));
				} else if (command.get(0).equals("cut") && command.contains("taskA.csv") && command.contains("-f")
						&& command.contains("-d")) {
					int fieldIndex = command.indexOf("-f") + 1;
					int delimiterIndex = command.indexOf("-d") + 1;
					String delimiter = command.get(delimiterIndex);
					delimiter = delimiter.replaceAll("[\"']", "");
					delimiter = delimiter.replace("'", "").replace("\"", "");
					String field = command.get(fieldIndex);
					System.out.println(linuxCommands.outputCut(field, delimiter, filePath));
				} else if (command.get(0).equals("cut") && command.contains("-f") && !command.contains("-d")) {
					int fieldIndex = command.indexOf("-f") + 1;
					String field = command.get(fieldIndex);
					System.out.println(linuxCommands.outputCut(field, ",", filePath));
				}
			}
		}

	}

}
