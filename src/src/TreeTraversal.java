package src;
public class TreeTraversal {
    public static void main(String... args) {
        TreeNode ex = buildTree();
        System.out.print("[");
        preorder(ex);
        System.out.println("]");
        System.out.print("[");
        inorder(ex);
        System.out.println("]");
        System.out.print("[");
        postorder(ex);
        System.out.println("]");
    }

    static TreeNode buildTree() {
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(9);
        root.left.left = new TreeNode(8);
        root.right = new TreeNode(20);
        root.right.left = new TreeNode(15);
        root.right.right = new TreeNode(7);
        return root;
    }

    static void preorder(TreeNode root) {
        if (root == null)
            return;
        System.out.print(root.val + ",");
        preorder(root.left);
        preorder(root.right);
    }

    static void inorder(TreeNode root) {
        if (root == null)
            return;
        inorder(root.left);
        System.out.print(root.val + ",");
        inorder(root.right);
    }

    static void postorder(TreeNode root) {
        if (root == null)
            return;
        postorder(root.left);
        postorder(root.right);
        System.out.print(root.val + ",");
    }
}

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    public TreeNode() {
        val = 0;
        left = null;
        right = null;
    }

    public TreeNode(int val) {
        this.val = val;
        left = null;
        right = null;
    }
}
