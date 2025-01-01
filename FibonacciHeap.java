


// username - maibendayan
// id1      - 319071148
// name1    - Mai Ben Dayan
// id2      - 208278945
// name2    - Arbel Klein

/**
 * FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */
public class FibonacciHeap
{
	private HeapNode first;
	private HeapNode min;
	private int size;
	private int nonMarked;
	private int numTrees;
	private static int totalLinks = 0;
	private static int totalCuts = 0;
	private static final double LogPhi = Math.log((1+Math.sqrt(5))/2);
	
	/*
	 * Constructor for the heap with given first, min and size
	 * 
	 * 
	 * Time Complexity - O(1)
	 */
	public FibonacciHeap(HeapNode first, HeapNode min, int size, int trees)
	{
		this.first = first;
		this.min = min;
		this.size = size;
		this.nonMarked = size;
		this.numTrees = trees;
	}
	
	/*
	 * Constructor for empty heap
	 * 
	 * 
	 * Time Complexity - O(1)
	 */
	public FibonacciHeap()
	{
		this.first = null;
		this.min = null;
		this.size = 0;
		this.nonMarked = 0;
		this.numTrees = 0;
	}
	
	public HeapNode getFirst()
	{
		return this.first;
	}

   /**
    * public boolean isEmpty()
    *
    * Returns true if and only if the heap is empty.
    * 
    * 
    * Time Complexity - O(1)
    */
    public boolean isEmpty()
    {
    	return (this.size == 0);
    }
		
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
    * The added key is assumed not to already belong to the heap.  
    * 
    * Returns the newly created node.
    * 
    * 
    * Time Complexity - O(1)
    */
    public HeapNode insert(int key)
    {
    	HeapNode node = new HeapNode(key);
    	
    	//updating the pointers
    	if(this.isEmpty()) //inserting to empty heap
    	{
    		node.next = node;
    		node.prev = node;
    		
    		//updating the pointer to the min
    		this.min = node;
    	}
    	else
    	{
	    	node.next = this.first;
	    	node.prev = this.first.prev;
	    	this.first.prev = node;
	    	node.prev.next = node;
	    	
	    	if(key < this.min.key) //need to update the min
	    		this.min = node;
    	}
    	
    	//updating the pointer to the first
    	this.first = node;
    	
    	//updating the fields
    	this.size++;
    	this.nonMarked++;
    	this.numTrees++;
    	
    	return node;
    }

   /**
    * public void deleteMin()
    *
    * Deletes the node containing the minimum key.
    *
    * 
    * Time Complexity - w.c. O(n), amortized O(logn)
    */
    public void deleteMin()
    {
    	//first we check if the heap is empty-> cannot delete
    	if (this.isEmpty() == true) {
    		return;
    	}
    	
    	//make sure after deleting min, we wont lose our pointer to first
    	if (this.first == this.min) {
    		if (this.min.child != null) {
    			this.first = this.min.child;
    		}
    		else {
    			this.first = this.min.next;
    		}
    	}
    	//knowing the min-node children are gonna be roots, we make them nonMarked
    	// plus disconnecting them from the min-node to be deleted (min still points at his children)
    	
    	HeapNode child1 = this.min.child;
    	if (child1 != null) {
	    	do {
	    		if(child1.mark == 1)
	    		{
	    			child1.mark = 0;
	    			this.nonMarked += 1;
	    		}
	    		child1.parent = null;
	    		child1 = child1.next;
	    	}while (this.min.child != child1);
    	}
	    	
    	//inserting the children between all "roots"
    	if (this.min.child == null) {
    		this.min.prev.next = this.min.next;
    		this.min.next.prev = this.min.prev;
    	}
    	else {
    		if(this.min.next == this.min) //min is only tree
    		{
    			this.first = this.min.child;
    		}
    		else
    		{
		    	this.min.prev.next = child1;
		    	this.min.next.prev = child1.prev;
		    	child1.prev.next = this.min.next;
		    	child1.prev = this.min.prev;
    		}
    	}
    	
    	//updating fields
    	this.numTrees += (this.min.rank - 1);
    	this.size -= 1;
    	if (this.min.mark == 0) {
    		this.nonMarked -=1;
    	}
    	
    	//here the min is not updated, will be updated in this call:
    	SuccessiveLink(this); 
    	
     	return;
    }
    
   /**
    * public HeapNode findMin()
    *
    * Returns the node of the heap whose key is minimal, or null if the heap is empty.
    * 
    * 
    * Time Complexity - O(1)
    */
    public HeapNode findMin()
    {
    	return this.min;
    } 
    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Melds heap2 with the current heap.
    *
    *
    * Time Complexity - O(1)
    */
    public void meld (FibonacciHeap heap2)
    {
    	if(this.isEmpty()) //heap is empty
    	{
    		//updating all fields of this heap to the fields of other heap
    		this.size = heap2.size;
    		this.nonMarked = heap2.size;
    		this.numTrees = heap2.numTrees;
    		
    		//updating all pointers of this heap to the pointers of other heap
    		this.min = heap2.min;
    		this.first = heap2.first;
    		
    		return;
    	}
    	
    	if(heap2.isEmpty()) //heap2 is empty
    	{
    		//no need to update anything
    		return;
    	}
    	
    	HeapNode first1 = this.first, first2 = heap2.first;
    	HeapNode last1 = first1.prev, last2 = first2.prev;
    	
    	//linking the end of this heap to the start of the other heap
    	last1.next = first2;
    	first2.prev = last1;
    	
    	
    	//linking the start of this heap to the end of the other heap
    	last2.next = first1;
    	first1.prev = last2;
    	
    	//updating the fields
    	this.size = this.size + heap2.size;
    	this.nonMarked = this.nonMarked + heap2.nonMarked;
    	this.numTrees = this.numTrees + heap2.numTrees;
    	
    	
    	//updating the pointers
    	if(heap2.min.key < this.min.key) //need to update the min
    		this.min = heap2.min;
    }

   /**
    * public int size()
    *
    * Returns the number of elements in the heap.
    * 
    *   
    * Time Complexity - O(1)
    */
    public int size()
    {
    	return this.size;
    }
    	
    /**
    * public int[] countersRep()
    *
    * Return an array of counters. The i-th entry contains the number of trees of order i in the heap.
    * (Note: The size of of the array depends on the maximum order of a tree.)  
    * 
    * 
    * Time Complexity - O(n)
    */
    public int[] countersRep()
    {
    	if (this.size == 0){
    		return new int[0];
    	}
    	
    	//checking what is the maximal rank to create an array in the right length.
    	int maxRank = 0;
    	HeapNode node = this.first;
    	do {
    		if (maxRank < node.rank)
    			maxRank = node.rank;
    		node = node.next;
    	}while (node != this.first);
    	
    	int[] arr = new int[maxRank + 1];
    	
    	//counting the trees and updating the counters in the array
    	node = this.first;
    	do {
    		arr[node.rank] +=1;
    		node = node.next;
    	}while (node != this.first);
    	
        return arr;
    }
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap.
	* It is assumed that x indeed belongs to the heap.
    *
    * 
    * Time Complexity - w.c. O(n), amortized O(logn)
    */
    public void delete(HeapNode x) 
    {    
    	//make our node to be deleted our new min
    	int delta = x.key - (this.min.key) + 1;
    	this.decreaseKey(x, delta);
    	
    	this.deleteMin();
    	return; 
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
    * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
    * 
    * 
    * Time Complexity - O(1) (amortize)
    * Time Complexity - O(n) (WC)
    */
    public void decreaseKey(HeapNode x, int delta)
    {
    	x.key = x.key - delta;
    	
    	if(x.parent == null) //x is root
    	{
    		if(x.key < this.min.key) //need to update the min
    			this.min = x;
    		x.mark = 0;
    		return;
    	}
    	
    	if(x.parent.key < x.key) //the decrease didn't ruined the heap condition
    		return;
    	
    	cascadingCuts(x, x.parent);
    }
    
    /*
     * cuts x from its parent (y) going up towards the root of the tree
     * 
     * assuming that both x and y are not null
     * 
     * 
     * Time Complexity - O(1) (amortize)
     * Time Complexity - O(n) (WC)
     */
    private void cascadingCuts(HeapNode x, HeapNode y)
    {
    	
    	cut(x,y);
    	
    	if(y.parent != null)
    	{
    		if(y.mark == 0)
    		{
    			y.mark = 1;
    			this.nonMarked--;
    		}
    		else
    			cascadingCuts(y, y.parent);
    	}
    }
    
    /*
     * cut x from its parent (y)
     * assuming both x and y are not null
     * 
     * 
     * Time Complexity - O(1)
     */
    private void cut(HeapNode x, HeapNode y)
    {
    	totalCuts++;
    	x.parent = null; //x became root
    	if(x.key < this.min.key) //need to update min
    		this.min = x;
    	if(x.mark == 1) //was marked
    		this.nonMarked++;
    	x.mark = 0;
    	y.rank = y.rank - 1;
    	
    	if(x.next == x)
    		y.child = null;
    	
    	else
    	{
    		if(y.child == x)
    			y.child = x.next;
    		
    		x.prev.next = x.next;
    		x.next.prev = x.prev;
    	}
    	
    	//inserting x to the heap
    	HeapNode last = this.first.prev;
    	last.next = x;
    	this.first.prev = x;
    	x.prev = last;
    	x.next = this.first;
    	this.first = x;
    	
    	this.numTrees++;
    }

   /**
    * public int nonMarked() 
    *
    * This function returns the current number of non-marked items in the heap
    * 
    * 
    * Time Complexity - O(1)
    */
    public int nonMarked() 
    {    
        return this.nonMarked;
    }

   /**
    * public int potential() 
    *
    * This function returns the current potential of the heap, which is:
    * Potential = #trees + 2*#marked
    * 
    * In words: The potential equals to the number of trees in the heap
    * plus twice the number of marked nodes in the heap.
    * 
    * 
    * Time Complexity - O(1)
    */
    public int potential() 
    {
    	int marked = this.size - this.nonMarked;
    	
        return (this.numTrees + 2*marked);
    }

   /**
    * public static int totalLinks() 
    *
    * This static function returns the total number of link operations made during the
    * run-time of the program. A link operation is the operation which gets as input two
    * trees of the same rank, and generates a tree of rank bigger by one, by hanging the
    * tree which has larger value in its root under the other tree.
    * 
    * 
    * Time Complexity - O(1)
    */
    public static int totalLinks()
    {    
    	return totalLinks;
    }

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the
    * run-time of the program. A cut operation is the operation which disconnects a subtree
    * from its parent (during decreaseKey/delete methods).
    * 
    * 
    * Time Complexity - O(1)
    */
    public static int totalCuts()
    {    
    	return totalCuts;
    }

     /**
    * public static int[] kMin(FibonacciHeap H, int k) 
    *
    * This static function returns the k smallest elements in a Fibonacci heap that contains a single tree.
    * The function should run in O(k*deg(H)). (deg(H) is the degree of the only tree in H.)
    *  
    * ###CRITICAL### : you are NOT allowed to change H. 
    * 
    * 
    * Time Complexity - O(k*deg(H))
    */
    public static int[] kMin(FibonacciHeap H, int k)
    {
        int[] arr = new int[k];
        if(k==0)
        	return arr;
        
        
        if(k == 1)
        {
        	arr[0] = H.min.key;
        	return arr;
        }
        
        //building helping heap
        FibonacciHeap helpHeap = new FibonacciHeap();
        helpHeap.insert(H.min.key);
    	helpHeap.first.info = H.min;
    	
    	//node in H that point to the current min value in helpHeap
        HeapNode helpMin = H.min;
        
        for(int i=0; i<k; i++)
        {
        	//inserting the min value to the array
        	arr[i] = helpMin.key;
        	
        	//inserting children to helpHeap
        	helpMin = helpMin.child;
        	HeapNode child = helpMin;
        	if(child != null)
        	{
	        	do
	        	{
	        		helpHeap.insert(helpMin.key);
	        		helpHeap.first.info = helpMin;
	        		helpMin = helpMin.next;
	        	}
	        	while(helpMin != child);
        	}
        	
        	//deleting the min from helpHeap and updating the new min in helpMin
        	helpHeap.deleteMin();
        	
        	if(!helpHeap.isEmpty())
	        	//updating the pointer to node in H to the new min value in helpHeap
	        	helpMin = helpHeap.min.info;
        }
        
        return arr;
    }
    
    
    /**
     * public void SuccesiveLink(FibonacciHeap H)
     *
     * This function implements Successive Linking, called after deletion
     * 
    * 
    * Time Complexity - w.c. O(n), amortized O(logn)
     */
    public void SuccessiveLink(FibonacciHeap H) 
    {
    	if (this.size == 0) {  //no need to link
    		this.min = null;
    		this.first = null;
    		this.numTrees = 0;
    		return;
    	}
    	
    	if (this.size == 1) {  //no need to link
    		this.min = first;
    		this.numTrees = 1;
    		return;
    	}
    	
    	HeapNode[] arr = new HeapNode[(int) Math.floor(Math.log(this.size)/LogPhi) + 1];
    	HeapNode node = this.first;
    	node.prev.next = null;  //make our heap linear
    	
    	arr[node.rank] = node;
    	first = node.next;
    	
    	//linking!
    	while (first != null) {
    		node = first;
    		first = node.next;  //first is the next.next of original node
    		
    		if (arr[node.rank] == null) {  // if cell is empty - we "place the tree in the cell"
    			arr[node.rank] = node;
    		}
    		else {
    			linkRec(node, arr);
    		}		
    	}
    	
    	boolean weDontKnowWhosFirst = true;  //put our heap in order
    	HeapNode left = arr[0];
    	HeapNode curr;
    	
    	int num = 0;  //to update the num of trees
    	for (int i=0; i<arr.length; i++) {
    		if (arr[i] != null) {
    			this.min = arr[i];  //just to initialize
    			break;
    		}
    	}
    	
    	//reconnect the list, trees in acceding order
    	for (int i=0; i<arr.length; i++) {
    		if (arr[i] != null) {
    			left = arr[i];
    			num += 1;
    			if (this.min.key > left.key) {
    				this.min = left;
    			}
    			if (weDontKnowWhosFirst) {
    				this.first = left;
    				weDontKnowWhosFirst = false;
    			}
    			if (i != arr.length -1) {
	    			for (int j=i+1; j< arr.length; j++) {
	    				if (arr[j] != null) {
		    				curr = arr[j];
		    				
		    				//now we can connect
		    				left.next = curr;
		    				curr.prev = left;
		    				break;
	    				}
	    			}
    			}
    		}
    	}
    	//now left points to the last tree, lets connect first and last
    	left.next = this.first;
    	this.first.prev = left;
    	
    	this.numTrees = num;
    	
    	
    	return;
    }
    
    /**
     * public void linkRec(HeapNode node, HeapNode[] arr)
     *
     * This function is recursively linking the trees in the heap.
     * 
    * 
    * Time Complexity - O(logn)
     */
    //we get here cuz the cell is not empty, need to link
    public void linkRec(HeapNode node, HeapNode[] arr) {
    	//first - connect the root as a child to the tree "already in the cell"
		HeapNode father = arr[node.rank];
		
		if (father.key > node.key)
			{
			HeapNode tmp = node;
			node = father;
			father = tmp;
		}//take care of case there are no children
		if (father.child != null) {
			node.next = father.child;
			node.prev = father.child.prev;
			father.child.prev.next = node;
			father.child.prev = node;
			
		}
		else { //only child
			node.next = node;
			node.prev = node;
		}
		
		father.child = node;
		node.parent = father;
		father.rank += 1;
		totalLinks++;
		
		//moving the tree to the next cell
		arr[father.rank - 1] = null;
		if (arr[father.rank] == null) {
			arr[father.rank] = father;
		}
		else {   //cell is occupied
			linkRec(father, arr);
		}
    }
    
   /**
    * public class HeapNode
    * 
    * If you wish to implement classes other than FibonacciHeap
    * (for example HeapNode), do it in this file, not in another file. 
    *  
    */
    public static class HeapNode{

    	public int key; //check if can change to private
    	private int rank;
    	private int mark;
    	private HeapNode child;
    	private HeapNode next;
    	private HeapNode prev;
    	private HeapNode parent;
    	private HeapNode info;
    	

    	/*
    	 * Constructor for node
    	 * 
    	 * 
    	 * Time Complexity - O(1)
    	 */
    	public HeapNode(int key) {
    		this.key = key;
    		this.rank = 0;
    		this.mark = 0;
    		this.child = null;
    		this.next = this;
    		this.prev = this;
    		this.parent = null;
    		this.info = null;
    		
    	}

    	/*
    	 * returns the key of node
    	 * 
    	 * 
    	 * Time Complexity - O(1)
    	 */
    	public int getKey() {
    		return this.key;
    	}
    	
    	/*
    	 * return the rank of node
    	 * 
    	 * 
    	 * Time Complexity - O(1)
    	 */
    	public int getRank() {
    		return this.rank;
    	}
    	
    	/*
    	 * return the mark of node
    	 * 
    	 * 
    	 * Time Complexity - O(1)
    	 */
    	public int getMark() {
    		return this.mark;
    	}
    	
    	/*
    	 * return true if the node is marked, false otherwise
    	 * 
    	 * 
    	 * Time Complexity - O(1)
    	 */
    	public boolean getMarked() {
    		if(this.mark == 1)
    			return true;
    		return false;
    	}
    	
    	/*
    	 * returns pointer to the node child
    	 * 
    	 * 
    	 * Time Complexity - O(1)
    	 */
    	public HeapNode getChild() {
    		return this.child;
    	}
    	
    	/*
    	 * returns pointer to the node next
    	 * 
    	 * 
    	 * Time Complexity - O(1)
    	 */
    	public HeapNode getNext() {
    		return this.next;
    	}
    	
    	/*
    	 * returns pointer to the node prev
    	 * 
    	 * 
    	 * Time Complexity - O(1)
    	 */
    	public HeapNode getPrev() {
    		return this.prev;
    	}
    	
    	/*
    	 * returns pointer to the node parent
    	 * 
    	 * 
    	 * Time Complexity - O(1)
    	 */
    	public HeapNode getParent() {
    		return this.parent;
    	}
    	
    	/*
    	 * returns the info of node
    	 * 
    	 * 
    	 * Time Complexity - O(1)
    	 */
    	public HeapNode getInfo() {
    		return this.info;
    	}
    }
    
}
