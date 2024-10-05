package taskB;

/**
 * This class main purpose is to be a linked list for the current blocks of
 * memory that are placed or free for the simulation of First Fit, Best Fit, and
 * Worst Fit memory allocation methods.
 */
public class MainMemory {

	private BlockNode start;
	private BlockNode end;
	private int size;

	/**
	 * Constructor, initialize linked list
	 */
	public MainMemory() {
		start = null;
		end = null;
		size = 0;
	}

	/**
	 * Checks if linked list is empty
	 * 
	 * @return True if empty, false if not
	 */
	public boolean isEmpty() {
		return start == null;
	}

	/**
	 * Gets the size of linked list
	 * 
	 * @return size of linked list
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Inserts Block at start of linked list, best to be used to initialize first
	 * node.
	 * 
	 * @param block Block of memory to insert.
	 */
	public void insertAtStart(Block block) {
		BlockNode nptr = new BlockNode(block, null);
		size++;
		if (start == null) {
			start = nptr;
			end = start;
		} else {
			nptr.setNext(start);
			start = nptr;
		}
	}

	/**
	 * First fit insert, this method goes through the linked list finding the first
	 * place it can insert the block into memory.
	 * 
	 * @param the Process proc to insert into memory
	 * @return True if successfully inserted block of memory, False if failed.
	 */
	public boolean firstFitInsert(Process proc) {
		Block block = new Block(proc);
		BlockNode nptr = new BlockNode(block, null);

		if (start == null) {
			start = nptr;
			end = start;
			return true;
		} else {

			BlockNode curr = start;

			// look at all available slots/holes in memory
			// select the first available position of suitable size for block
			while (curr != null) {

				// enough available space in memory identified
				if (curr.getBlock().canPlace(block.getProcess())) {

					// get the end memory location for available block curr
					int end = curr.getBlock().getHole().getEnd();

					// add the process in memory
					curr.getBlock().setProcess(block.getProcess());

					// take only what we need from memory
					int block_start = curr.getBlock().getHole().getStart();
					int memory_needs = block.getProcess().getArgument();
					curr.getBlock().getHole().setRange(block_start, block_start + memory_needs - 1);

					// create a new block with the rest of memory we don't need
					// notice curr.getBlock().getHole().getEnd() was changed by line 155
					if (curr.getBlock().getHole().getEnd() < end) {
						BlockNode newBlock = new BlockNode(
								new Block(null, new Hole(curr.getBlock().getHole().getEnd() + 1, end)), curr.getNext());

						curr.setNext(newBlock);
					}
					size++;
					return true;
				}
				curr = curr.getNext();
			}
			return false;
		}
	}

	/**
	 * TODO Best fit insert, this method goes through the linked list finding the
	 * best place it can insert the block into memory.
	 * 
	 * @param Process proc to insert into memory
	 * @return True if successfully placed, false if it failed.
	 */
	public boolean bestFitInsert(Process proc) {
		Block block = new Block(proc);
		BlockNode ptr = new BlockNode(block, null);
		if (start == null) {
			start = ptr;
			end = start;
			return true;
		} else {
			BlockNode current = start;
			int index = -1;
			int minimumDif = 6000;
			int j = 0;

			while (current != null) {
				if (current.getBlock().getSize() >= proc.getArgument() && current.getBlock().canPlace(proc)) {
					if (current.getBlock().getSize() < minimumDif) {
						index = j;
						minimumDif = current.getBlock().getSize();
					}
				}
				current = current.getNext();
				j++;
			}

			if (index == -1) {
				return false;
			}

			j = 0;
			current = start;

			while (current != null) {
				if (j == index) {
					if (current.getBlock().canPlace(block.getProcess())) {
						int end = current.getBlock().getHole().getEnd();
						current.getBlock().setProcess(block.getProcess());
						int block_start = current.getBlock().getHole().getStart();
						int memory_needs = block.getProcess().getArgument();
						current.getBlock().getHole().setRange(block_start, block_start + memory_needs - 1);
						if (current.getBlock().getHole().getEnd() < end) {
							BlockNode newBlock = new BlockNode(
									new Block(null, new Hole(current.getBlock().getHole().getEnd() + 1, end)),
									current.getNext());
							current.setNext(newBlock);
						}
						size++;
						return true;
					}
				}
				j++;
				current = current.getNext();
			}
			return false;
		}

	}

	/**
	 * This method goes through current memory blocks. If blocks are next to each
	 * other and free it will join the blocks together making a larger block.
	 */
	public void joinBlocks() {
		BlockNode ptr = start;

		while (ptr != null && ptr.getNext() != null) {

			BlockNode next = ptr.getNext();

			if (ptr.getBlock().getProcess() == null && next.getBlock().getProcess() == null) {
				int start = ptr.getBlock().getHole().getStart();
				int end = next.getBlock().getHole().getEnd();
				ptr.getBlock().getHole().setRange(start, end);
				ptr.setNext(next.getNext());
				size--;
				continue;
			}
			ptr = ptr.getNext();
		}
	}

	/**
	 * TODO This method gets the external fragmentation of the current memory blocks
	 * if a block of memory failed to placed.
	 * 
	 * @return external fragmentation of memory.
	 */
	public int externalFragmentation() {
		BlockNode ptr = start;
		int externalFragmentation = 0;

		while (ptr != null) {
			if (ptr.getBlock().available()) {
				externalFragmentation += ptr.getBlock().getSize();
			}
			ptr = ptr.getNext();
		}

		return externalFragmentation;
	}

	/**
	 * This method goes through the blocks of memory and de-allocates the block for
	 * the provided process_number
	 * 
	 * @param process_number Process to be de-allocated.
	 */
	public void deallocateBlock(int process_number) {

		BlockNode ptr = start;
		while (ptr != null) {

			if (ptr.getBlock().getProcess() != null) {
				if (ptr.getBlock().getProcess().getReference_number() == process_number) {
					ptr.getBlock().setProcess(null);
					joinBlocks();
					return;
				}
			}
			ptr = ptr.getNext();
		}
	}

	public void compaction(Process process) {
		joinBlocks();
		boolean hasSwapped = true;
		while (hasSwapped) {
			hasSwapped = false;
			BlockNode ptr = start;
			while (ptr != null) {
				if (ptr.getNext() != null) {
					if (ptr.getBlock().available() && !ptr.getNext().getBlock().available()) {
						int nextStart = ptr.getBlock().getHole().getStart();
						int nextEnd = ptr.getBlock().getHole().getStart() + ptr.getNext().getBlock().getHole().getSize()
								- 1;
						Block current = ptr.getBlock();
						Block next = ptr.getNext().getBlock();
						ptr.setBlock(next);
						ptr.getNext().setBlock(current);
						ptr.getNext().getBlock().getHole().setRange(nextEnd + 1,
								nextEnd + ptr.getNext().getBlock().getSize());
						ptr.getBlock().getHole().setRange(nextStart, nextEnd);
						hasSwapped = true;

					}
				}
				ptr = ptr.getNext();
			}
		}
		joinBlocks();
		BlockNode current = start;
		if (process.getArgument() <= externalFragmentation()) {
			Block block = new Block(process);
			while (current != null) {
				if (current.getBlock().canPlace(block.getProcess())) {
					int end = current.getBlock().getHole().getEnd();
					current.getBlock().setProcess(block.getProcess());
					int block_start = current.getBlock().getHole().getStart();
					int memory_needs = block.getProcess().getArgument();
					current.getBlock().getHole().setRange(block_start, block_start + memory_needs - 1);

					if (current.getBlock().getHole().getEnd() < end) {
						BlockNode newBlock = new BlockNode(
								new Block(null, new Hole(current.getBlock().getHole().getEnd() + 1, end)),
								current.getNext());

						current.setNext(newBlock);
					}
					break;
				}
				current = current.getNext();
			}
		} else {
			System.out.println("Request " + process.getReference_number() + " failed at allocating "
					+ process.getArgument() + " bytes.");
			System.out.println("External Fragmentation is " + externalFragmentation() + " bytes.");
		}
		joinBlocks();
		printBlocks();
	}

	/**
	 * This method prints the whole list of current memory.
	 */
	public void printBlocks() {
		System.out.println("Current memory display");
		BlockNode ptr = start;
		while (ptr != null) {
			ptr.getBlock().displayBlock();
			ptr = ptr.getNext();
		}

	}

}
