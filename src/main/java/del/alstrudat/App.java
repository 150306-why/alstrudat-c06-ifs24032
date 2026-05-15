package del.alstrudat;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        // Mencegah error jika file input kosong
        if (!sc.hasNext()) {
            sc.close();
            return;
        }

        // 1. Baca N (jumlah node) dan M (jumlah edge)
        int N = sc.nextInt();
        int M = sc.nextInt();

        // 2. Baca kode-kode node (misal: X0, X1, X2...)
        String[] codes = new String[N];
        for (int i = 0; i < N; i++) {
            codes[i] = sc.next();
        }

        // 3. Baca edge (u, v, weight)
        int[][] edges = new int[M][3];
        for (int i = 0; i < M; i++) {
            // Cek apakah u dan v diberikan dalam bentuk index (angka) atau nama node (X0, X1)
            if (sc.hasNextInt()) {
                edges[i][0] = sc.nextInt();
                edges[i][1] = sc.nextInt();
            } else {
                edges[i][0] = getIndex(codes, sc.next());
                edges[i][1] = getIndex(codes, sc.next());
            }
            edges[i][2] = sc.nextInt(); // Weight
        }

        // 4. Baca Q (jumlah query)
        int Q = sc.nextInt();
        String[][] queries = new String[Q][2];
        for (int i = 0; i < Q; i++) {
            queries[i][0] = sc.next();
            queries[i][1] = sc.next();
        }
        
        sc.close();

        // 5. Jalankan logika utama di Program.java
        Program program = new Program(N, M, codes, edges, Q, queries);
        program.solve();
    }

    // Fungsi bantuan untuk mencari index node berdasarkan namanya
    private static int getIndex(String[] codes, String node) {
        for (int i = 0; i < codes.length; i++) {
            if (codes[i].equals(node)) {
                return i;
            }
        }
        return -1;
    }
}