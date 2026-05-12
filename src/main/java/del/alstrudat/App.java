package del.alstrudat;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Baca N dan M
        int N = scanner.nextInt();
        int M = scanner.nextInt();

        // Baca kode node
        String[] codes = new String[N];
        for (int i = 0; i < N; i++) {
            codes[i] = scanner.next();
        }

        // Baca edges: u v w (0-indexed)
        int[][] edges = new int[M][3];
        for (int i = 0; i < M; i++) {
            edges[i][0] = scanner.nextInt();
            edges[i][1] = scanner.nextInt();
            edges[i][2] = scanner.nextInt();
        }

        // Baca query
        int Q = scanner.nextInt();
        String[][] queries = new String[Q][2];
        for (int i = 0; i < Q; i++) {
            queries[i][0] = scanner.next();
            queries[i][1] = scanner.next();
        }

        scanner.close();

        // Jalankan semua fase melalui Program
        Program program = new Program(N, M, codes, edges, Q, queries);
        program.solve();
    }
}
