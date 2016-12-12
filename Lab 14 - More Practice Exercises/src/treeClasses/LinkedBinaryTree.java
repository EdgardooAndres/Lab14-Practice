package treeClasses;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

import positionalStructures.Position;

/**
 * Implementation of the Binary Tree using a linked structure. 
 * 
 * @author pedroirivera-vega
 *
 * @param <E> Generic data type of elements in the tree. 
 */
public class LinkedBinaryTree<E> extends AbstractBinaryTree<E> {
	// class Node<E> is included at the end of this class

	protected Node<E> root;   // the root of the tree
	private int size;               // the size of the tree

	public LinkedBinaryTree() { 
		root = null; 
		size = 0; 
	}

	/**
	 * Validation method for a position. 
	 * @param p the position to validate
	 * @return if successful, it returns the position casted to type Node.
	 * @throws IllegalArgumentException If the position does not passes the validation.
	 */
	private Node<E> validate(Position<E> p) throws IllegalArgumentException { 
		if (p == null) throw new IllegalArgumentException("Position p is null."); 
		if (!(p instanceof Node<?>)) 
			throw new IllegalArgumentException("Position is not of righ data type."); 

		Node<E> ptn = (Node<E>) p; 
		if (ptn.getParent() == p) 
			throw new IllegalArgumentException("Invalid position --- not a tree position."); 

		return ptn; 
	}

	@Override
	public Position<E> left(Position<E> p) throws IllegalArgumentException {
		Node<E> ptn = validate(p); 
		return ptn.getLeft();
	}

	@Override
	public Position<E> right(Position<E> p) throws IllegalArgumentException {
		Node<E> ptn = validate(p); 
		return ptn.getRight();
	}

	@Override
	public Position<E> root() {
		return root;
	}

	@Override
	public Position<E> parent(Position<E> p) throws IllegalArgumentException {
		Node<E> ptn = validate(p); 
		return ptn.getParent();
	}

	@Override
	public int size() {
		return size;
	}


	////////////////////////////////////////////////////////////////////////////////
	// OTHER methods as in textbook: addRoot, addLeft, addRight, attach, and remove
	////////////////////////////////////////////////////////////////////////////////

	public Position<E> addRoot(E e) throws IllegalStateException { 
		if (!this.isEmpty()) 
			throw new IllegalStateException("Non-empty tree: Can not add a root to a non-empty tree."); 
		root = createNode(e, null, null, null); 
		size = 1; 
		return root; 
	}

	public Position<E> addLeft(Position<E> p, E e) throws IllegalArgumentException { 
		Node<E> np = validate(p); 
		if (np.getLeft() != null) 
			throw new IllegalArgumentException("Given position already has left child."); 
		Node<E> newNode = createNode(e, np, null, null); 
		np.setLeft(newNode); 
		size++; 
		return newNode;
	}

	public Position<E> addRight(Position<E> p, E e) throws IllegalArgumentException { 
		Node<E> np = validate(p); 
		if (np.getRight() != null) 
			throw new IllegalArgumentException("Given position already has right child."); 
		Node<E> newNode = createNode(e, np, null, null); 
		np.setRight(newNode); 
		size++; 
		return newNode;
	}

	public void Attach(Position<E> p, LinkedBinaryTree<E> t1, LinkedBinaryTree<E> t2) 
			throws IllegalArgumentException
	{ 
		Node<E> np = validate(p); 
		if (isInternal(np))
			throw new IllegalArgumentException("Position is not an external node in the tree."); 
		size += t1.size + t2.size;   // determine new size of this tree
		if (!t1.isEmpty()) { 
			np.setLeft(t1.root);
			t1.root.setParent(np); 

			// make t1 empty
			t1.root = null; 
			t1.size = 0; 
		}
		if (!t2.isEmpty()) { 
			np.setRight(t2.root);
			t2.root.setParent(np); 

			// make t2 empty
			t2.root = null; 
			t2.size = 0; 
		}
	}

	/**
	 * Removes a position from the tree. The operation is valid only if the
	 * specified position has at most one child. 
	 * 
	 * @param p the position that will attempt to remove from the tree
	 * @return if successful, the element at the position (p) that is removed. 
	 * @throws IllegalArgumentException if the position is not a valid position
	 *         for the current instance of the tree or if it has two children.
	 */
	public E remove(Position<E> p) throws IllegalArgumentException { 
		Node<E> ntd = validate(p); 
		if (numChildren(ntd) == 2)
			throw new IllegalArgumentException("Position to delete has two children.");
		E etr = ntd.getElement(); 
		Node<E> child = (ntd.getLeft() == null ? ntd.getRight() : ntd.getLeft()); 
		Node<E> parent = ntd.getParent(); 
		if (parent == null) 
			root = child; 
		else if (parent.getLeft() == ntd)
			parent.setLeft(child); 
		else 
			parent.setRight(child);
		if (child != null)
			child.setParent(parent);
		size--; 

		// discard deleted node
		ntd.discard();

		return etr; 
	}

	//----------------------------------------------------------------------------
	/////////////////////////////////////////////////////////
	// METHODS CREATED TO PRACTICE FOR FINAL EXAM (LAB 14) //
	/////////////////////////////////////////////////////////

	/**
	 * Returns true or false, depending on whether e is in the tree.
	 * @param e
	 * @return
	 */
	public boolean find(E e){
		//create empty queue
		Queue<Node<E>> que = new LinkedList<>();
		//base case
		if (root == null)
			return false;

		//add the first node to begin the search
		//in this case its the root of the tree.
		que.add(root);

		//search the rest of the tree.
		while(!que.isEmpty())
		{
			Node<E> curr = que.remove(); //compare the first node in the queue

			if(curr.getElement().equals(e)) //if equals then found.
				return true;

			if(curr.getLeft() != null) //if has left child
				que.add(curr.getLeft());
			if(curr.getRight() != null) //if has right child
				que.add(curr.getRight());
		}
		//queue is empty and did not find the element.
		return false;
	}

	/**
	 * Returns a new LinkedBinaryTree object that is an identical copy.
	 * @return
	 */
	public LinkedBinaryTree<E> copyOf(){//TODO
		//use PreOrder to copy root and its children.

		//create tree
		LinkedBinaryTree<E> copy = new LinkedBinaryTree<E>();

		//Base Case
		if(this.root == null) //tree is empty
			return null;

		Queue<Node<E>> q1 = new LinkedList<>();
		q1.add(this.root);
		Node<E> n;

		Queue<Node<E>> q2 = new LinkedList<>();
		Node<E> fresh;
		Node<E> newRoot = new Node<>(this.root.getElement(), null, this.root.getLeft(), this.root.getRight());
		q2.add(newRoot);

		while(!q1.isEmpty())
		{
			n = q1.remove();
			fresh = q2.remove();
			if(null != n.left) 
			{
				q1.add(n.left);
				fresh.left = new Node(n.left.getElement());
				q2.add(fresh.left);
			}
			if(null != n.right) {
				q1.add(n.right);
				fresh.right= new Node(n.right.data);
				q2.add(fresh.right);
			}           
		}
		//make root of three == to the other root.
		copy.root = this.root;
		copy.size = this.size;

		//set left and right children
		if(this.root.getLeft() != null)//if original has left child.
			copy.root.setLeft(this.root.getLeft());
		if(this.root.getRight() != null) //if original has right child.
			copy.root.setRight(this.root.getRight());

		return null;
	}

	/**
	 * Returns true or false, depending on whether the instance object and t are identical (tree structure and node values).
	 * @param t
	 * @return
	 */
	public boolean equals(LinkedBinaryTree<E> t){
		//any traversal

		Queue<Node<E>> q1 = new LinkedList<>();
		Queue<Node<E>> q2 = new LinkedList<>();

		if(!this.root.equals(t.root) || this.size != t.size) //if roots or sizes are not equal.
			return false;

		q1.add(this.root);
		q2.add(t.root);

		while(!q1.isEmpty())
		{
			Node<E> curr1 = q1.remove();
			Node<E> curr2 = q2.remove();

			if(!curr1.getLeft().getElement().equals(curr2.getLeft().getElement()))
				return false;
			if(!curr1.getRight().getElement().equals(curr2.getRight().getElement()))
				return false;

			if(curr1.getLeft() != null && curr2.getLeft() != null)
			{
				q1.add(curr1.getLeft());
				q2.add(curr2.getLeft());
			}
			else
				return false;

			if(curr1.getRight() != null && curr2.getRight() != null)
			{
				q1.add(curr1.getLeft());
				q2.add(curr2.getLeft());
			}
			else
				return false;
		}
		return true;
	}

	/**
	 * Returns the height of the tree.
	 * @return
	 */
	public int height(){
		return height(root);
	}
	public int height(Node<E> r){ 
		if (r == null)
			return 0;

		return Math.max(height(r.getLeft()), height(r.getRight())) + 1;
	}

	/**
	 * Returns true or false, depending on whether the tree is complete.
	 * Hint: Consider using a queue in a way similar to BFS.
	 * @return
	 */
	public boolean isComplete(){
		//base case
		if(root == null)
			return false;

		Queue<Node<E>> que = new LinkedList<>();
		que.add(root);
		
		//flag es para saber si es el ultimo nivel. 
		//de ser true entonces no debe haber mas ningun nodo en el resto de los elementos del queue.
		boolean flag = false; 

		//traverse the tree
		while(!que.isEmpty())
		{
			Node<E> curr = que.remove();

			if(curr.getLeft() == null) //if there is left child
			{
				if(flag) //not complete
					return false;
				que.add(curr.getLeft());
			}
			else //there is a left node.
				flag = true;
			
			if(curr.getRight() != null) //if there is right child.
			{
				if(flag) //is complete.
					return false;
				que.add(curr.getRight());
			}
			else
				flag = true;
		}
		//there was no node with children at the last level filled.
		return true;
	}

	/**
	 * Returns true or false, depending on whether the tree is a min-heap.
	 * @return
	 */
	public boolean isMinHeap(){
		//base case
		if(root == null)
			return true;
		
		if(!this.isComplete()) // if its not a complete tree it can't be a heap.
			return false;
		
		Queue<Node<E>> que = new LinkedList<>();
		que.add(root);
		
		while(!que.isEmpty())
		{
			Node<E> curr = que.remove();
			
			if(curr.getLeft() != null) //has left
			{
				if(curr.getLeft().getElement() < curr.getElement()) //left element has lower priority.
					return false;
				que.add(curr.getLeft());
			}
			if(curr.getLeft() != null) //has right
			{
				if(curr.getRight().getElement() < curr.getElement()) //right element has lower priority.
					return false;
				que.add(curr.getRight());
			}
		}

		return true;
	}

	/**
	 * Returns true or false, depending on whether the tree is a binary search tree.
	 * @return
	 */
	public boolean isBST(){
		//base case
		if(root == null)
			return false;
		
		Queue<Node<E>> que = new LinkedList<>();
		que.add(root);
		
		while(!que.isEmpty())
		{
			Node<E> curr = que.remove();
			
			if(curr.getLeft() != null)
			{
				if(curr.getLeft().getElement() > curr.getElement())
					return false;
				que.add(curr.getLeft());
			}
			if(curr.getRight() != null)
			{
				if(curr.getRight().getElement() < curr.getElement())
					return false;
				que.add(curr.getRight());
			}
		}
		
		return true;
	}

	/**
	 * Returns true or false, depending on whether the tree is an AVL tree.
	 * @return
	 */
	public boolean isAVL(){
		
		return isAVL(root);
	}
	public boolean isAVL(Node<E> r){
		//base case
		if(root == null)
			return true;
		
		Queue<Node<E>> que = new LinkedList<>();
		que.add(root);
		
		while(!que.isEmpty())
		{
			Node<E> curr = que.remove();
			
			if(curr.getLeft() != null)
			{
				isAVL()
			}
			if(curr.getRight() != null)
			{
				if(curr.getRight().getElement() < curr.getElement())
					return false;
				que.add(curr.getRight());
			}
		}
		
		return true;
	}

	////////////////////////////////////////////////////////
	// Inner class Node<E> and method to create new node  //
	////////////////////////////////////////////////////////	
	/**
	 * Inner class Node<E>
	 * @author pedroirivera-vega
	 *
	 * @param <E> Data type of element in Node
	 */
	protected static class Node<E> implements Position<E> { 
		private E element; 
		private Node<E> parent, left, right; 
		public Node() {}
		public Node(E element, Node<E> parent, Node<E> left, Node<E> right) { 
			this.element = element; 
			this.parent = parent; 
			this.left = left; 
			this.right = right; 
		}
		public E getElement() { 
			return element; 
		}
		public Node<E> getParent() {
			return parent;
		}
		public void setParent(Node<E> parent) {
			this.parent = parent;
		}
		public Node<E> getLeft() {
			return left;
		}
		public void setLeft(Node<E> left) {
			this.left = left;
		}
		public Node<E> getRight() {
			return right;
		}
		public void setRight(Node<E> right) {
			this.right = right;
		}
		public void setElement(E element) {
			this.element = element;
		}

		public void discard() { 
			element = null; 
			left = right = null;
			parent = this; 
		}

	} // end Node<E>

	/**
	 * Method to create a new Node
	 * @param e the element in the new node
	 * @param p the parent of the new node
	 * @param l the left child of the new node
	 * @param r the right child of the new node
	 * @return
	 */
	protected Node<E> createNode(E e, Node<E> p, Node<E> l, Node<E> r) { 
		return new Node<E>(e, p, l, r); 
	}
}
