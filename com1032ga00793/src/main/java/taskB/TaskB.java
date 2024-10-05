package taskB;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * Main function for memory management.
 */
public class TaskB {

	public static final int TOTAL_BYTES = 1024;
	private static String filePath = "taskB.csv";
	private static int[][] alloc = new int[TOTAL_BYTES][3];
	public static ArrayList<Process> allocatedProcesses;
	public static ArrayList<Process> unallocatedProcesses;

	public static void readFileSplit() {
		int i = 0;
		try {
			BufferedReader bReader = new BufferedReader(new FileReader(filePath));
			String line;
			while ((line = bReader.readLine()) != null) {
				String[] trimmedField = line.split(",");
				alloc[i][0] = Integer.parseInt(trimmedField[0].trim());
				alloc[i][1] = Integer.parseInt(trimmedField[1].trim());
				alloc[i][2] = Integer.parseInt(trimmedField[2].trim());
				i++;
			}
			bReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static ArrayList<Process> listof_processes;

	public static void createProcesses() {
		readFileSplit();
		Process proc;

		listof_processes = new ArrayList<>();
		for (int i = 0; i < alloc.length; i++) {
			proc = new Process(alloc[i][0], alloc[i][1], alloc[i][2]);
			listof_processes.add(proc);
		}
	}

	/**
	 * This method runs the First Fit Memory Allocation simulation using a linked
	 * list. Loops through the Processes in the Process list and allocates
	 * appropriately. If it cannot allocate, it will fail and print why accordingly.
	 * If it succeeds it will print 'Success'.
	 */
	private static void firstFit() {

		MainMemory manager = new MainMemory();
		manager.insertAtStart(new Block());

		for (Process proc : listof_processes) {

			if (proc.isAllocating()) {
				boolean placed = manager.firstFitInsert(proc);
				if (!placed) {
					System.out.println("Request " + proc.getReference_number() + " failed at allocating "
							+ proc.getArgument() + " bytes.");
					System.out.println("External Fragmentation is " + manager.externalFragmentation() + " bytes.");
					manager.printBlocks();
					return;
				}

			} else if (proc.isDeallocating()) {
				manager.deallocateBlock(proc.getArgument());
			}
		}

		System.out.println("Success");
		manager.printBlocks();
	}

	private static void bestFit() {

		MainMemory manager = new MainMemory();
		manager.insertAtStart(new Block());

		for (Process proc : listof_processes) {

			if (proc.isAllocating()) {
				boolean placed = manager.bestFitInsert(proc);
				if (!placed) {
					System.out.println("Request " + proc.getReference_number() + " failed at allocating "
							+ proc.getArgument() + " bytes.");
					System.out.println("External Fragmentation is " + manager.externalFragmentation() + " bytes.");
					manager.printBlocks();
					System.out.println("-------After Compaction ------");
					manager.compaction(proc);
				}
			} else if (proc.isDeallocating()) {
				manager.deallocateBlock(proc.getArgument());
			}
		}
		System.out.println("Success");
	}

	public static void main(String[] args) {

		createProcesses();

		System.out.println("----------Best Fit - to be implemented ---------");
		bestFit();

	}

}
