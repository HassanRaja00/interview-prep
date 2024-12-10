package src;
// package lc_premium;

/*
 * Given two nodes of a binary tree p and q, return their lowest common ancestor (LCA).

    Each node will have a reference to its parent node. The definition for Node is below:

    class Node {
        public int val;
        public Node left;
        public Node right;
        public Node parent;
    }

    According to the definition of LCA on Wikipedia: "The lowest common ancestor of two nodes 
    p and q in a tree T is the lowest node that has both p and q as descendants 
    (where we allow a node to be a descendant of itself)."

    Assuming there are at minimum 2 nodes in the tree and the nodes p and q actually exist.
    Also, a node can be an ancestor if the other is a child
 */
public class LCA_III {

    public static void main(String... args) {
        System.out.println("Strting...");
        Node root1 = buildTreeEx1();
        System.out.println("Answer for 1 is: " + lowestCommonAncestor(root1.left, root1.right).val);

        System.out.println("Answer for 2 is: "  + lowestCommonAncestor(root1.left, root1.left.right.right).val);

        Node root2 = buildTreeEx2();
        System.out.println("Answer for 3 is: " + lowestCommonAncestor(root2, root2.left).val);
    }

    /**
     * Using the unique properties of this tree where we note the parent node,
     * that means that if we traverse upwards from node p and node q we will end up
     * at the lowest common ancestor (which may be the root). This is a sort of reverse
     * DFS by using pointers to go up the tree.
     * 
     * Time: O(n)
     * Space: O(1), if we used a hash set for p to record all parents including p and
     *              set q to q.parent until q is a node in the set, then the space is O(n)
     */
    static Node lowestCommonAncestor(Node p, Node q) {
        Node ptr1 = p, ptr2 = q;
        while (ptr1 != ptr2) {
            // if we are at the root which has no parent, set ptr1 to ptr2
            ptr1 = ptr1.parent != null ? ptr1.parent : q;
            // same with ptr2
            ptr2 = ptr2.parent != null ? ptr2.parent : p;
        }
        // now that they are the same, just return
        return ptr1;
    }


    static Node buildTreeEx1() {
        Node root = new Node(3);
        root.parent = null;
        root.left = new Node(5);
        root.left.parent = root;
        root.right = new Node(1);
        root.right.parent = root;
        root.left.left = new Node(6);
        root.left.left.parent = root.left;
        root.left.right = new Node(2);
        root.left.right.parent = root.left;
        root.left.right.left = new Node(7);
        root.left.right.left.parent = root.left.right;
        root.left.right.right = new Node(4);
        root.left.right.right.parent = root.left.right;
        root.right.left = new Node(0);
        root.right.left.parent = root.right;
        root.right.right = new Node(8);
        root.right.right.parent = root.right;
        return root;
    }

    static Node buildTreeEx2() {
        Node root = new Node(1);
        root.parent = null;
        root.left = new Node(2);
        root.left.parent = root;
        return root;
    }
    
}

class Node {
    public int val;
    public Node left;
    public Node right;
    public Node parent;

    public Node(int val) {
        this.val = val;
    }
}