package del.alstrudat;

import java.util.*;

public class Program {
    // Kamu boleh memberikan struktur Node agar temanmu tidak bingung
    static class Node {
        int val;
        Node left, right;
        Node(int v) { val = v; }
    }

    public static void solve(int n, int[] data) {
        // TULIS JAWABANMU DI SINI
        // Gunakan struktur BST, Stack, dan Queue sesuai instruksi di README.md
        
        // Contoh: System.out.println("Skor Akhir");
    }

    // Kamu boleh membiarkan fungsi helper ini atau menghapusnya jika ingin lebih susah
    static Node insert(Node root, int val) {
        if (root == null) return new Node(val);
        if (val < root.val) root.left = insert(root.left, val);
        else if (val > root.val) root.right = insert(root.right, val);
        return root;
    }
}