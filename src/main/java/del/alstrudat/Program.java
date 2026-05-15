package del.alstrudat;

import java.util.*;

public class Program {

    private int N, M, Q;
    private String[] codes;
    private int[][] edges;
    private String[][] queries;

    public Program(int N, int M, String[] codes, int[][] edges, int Q, String[][] queries) {
        this.N = N;
        this.M = M;
        this.codes = codes;
        this.edges = edges;
        this.Q = Q;
        this.queries = queries;
    }

    // -------------------------------------------------------------------------
    // ENTRY POINT
    // -------------------------------------------------------------------------
    public void solve() {
        final long INF = Long.MAX_VALUE / 2;

        // =====================================================================
        // FASE 1 & 2 : Bellman-Ford dari node 0
        // =====================================================================
        long[] dist = new long[N];
        Arrays.fill(dist, INF);
        dist[0] = 0;

        // N-1 iterasi relaksasi
        for (int iter = 0; iter < N - 1; iter++) {
            boolean updated = false;
            for (int[] edge : edges) {
                int u = edge[0], v = edge[1];
                long w = edge[2];
                if (dist[u] != INF && dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    updated = true;
                }
            }
            if (!updated) break;
        }

        // --- FASE 1 : Deteksi negative cycle ---
        boolean negCycle = false;
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1];
            long w = edge[2];
            if (dist[u] != INF && dist[u] + w < dist[v]) {
                negCycle = true;
                break;
            }
        }

        if (negCycle) {
            System.out.println("NEGATIVE CYCLE DETECTED");
            return;
        }
        System.out.println("NO NEGATIVE CYCLE");

        // --- FASE 2 : Cetak jarak Bellman-Ford ---
        for (int i = 0; i < N; i++) {
            System.out.println(codes[i] + ": " + (dist[i] == INF ? "INF" : dist[i]));
        }

        // =====================================================================
        // Bangun SPT
        // =====================================================================
        // Untuk setiap node v, cari parent dengan indeks terkecil
        // di antara semua edge (u,v) yang merealisasikan jarak terpendek.
        int[] parent = new int[N];
        Arrays.fill(parent, -1);
        for (int v = 1; v < N; v++) {
            if (dist[v] == INF) continue;
            for (int[] edge : edges) {
                int u = edge[0], ev = edge[1];
                long w = edge[2];
                if (ev == v && dist[u] != INF && dist[u] + w == dist[v]) {
                    if (parent[v] == -1 || u < parent[v]) {
                        parent[v] = u;
                    }
                }
            }
        }

        // children list (directed: parent -> child)
        List<List<Integer>> children = new ArrayList<>();
        for (int i = 0; i < N; i++) children.add(new ArrayList<>());

        List<Integer> sptNodes = new ArrayList<>();
        sptNodes.add(0);
        for (int i = 1; i < N; i++) {
            if (dist[i] != INF) {
                sptNodes.add(i);
                if (parent[i] != -1) {
                    children.get(parent[i]).add(i);
                }
            }
        }
        int sptSize = sptNodes.size();

        // Undirected adjacency untuk diameter
        List<List<Integer>> undir = new ArrayList<>();
        for (int i = 0; i < N; i++) undir.add(new ArrayList<>());
        for (int p = 0; p < N; p++) {
            for (int c : children.get(p)) {
                undir.get(p).add(c);
                undir.get(c).add(p);
            }
        }

        // =====================================================================
        // FASE 3a : Diameter SPT
        // =====================================================================
        int diameter = 0;
        if (sptSize > 1) {
            // BFS dari root → temukan ujung terjauh
            int[] d1 = bfsUndir(0, undir);
            int far1 = 0;
            for (int node : sptNodes) {
                if (d1[node] > d1[far1]) far1 = node;
            }
            // BFS dari ujung terjauh → diameter
            int[] d2 = bfsUndir(far1, undir);
            for (int node : sptNodes) {
                if (d2[node] > diameter) diameter = d2[node];
            }
        }
        System.out.println("DIAMETER: " + diameter);

        // =====================================================================
        // FASE 3b : Centroid count
        // =====================================================================
        int[] subtreeSize = new int[N];
        computeSubtreeSize(0, children, subtreeSize);

        int centroidCount = 0;
        for (int v : sptNodes) {
            int maxComp = sptSize - subtreeSize[v]; // komponen "atas"
            for (int c : children.get(v)) {
                if (subtreeSize[c] > maxComp) maxComp = subtreeSize[c];
            }
            if (maxComp <= N / 2) centroidCount++;
        }
        System.out.println("CENTROIDS: " + centroidCount);

        // =====================================================================
        // FASE 3c : Level-order traversal, level genap, dist genap
        // =====================================================================
        int[] level = new int[N];
        Arrays.fill(level, -1);
        level[0] = 0;
        Queue<Integer> bfsQ = new LinkedList<>();
        bfsQ.add(0);

        // Pakai TreeMap agar level terurut ascending otomatis
        Map<Integer, List<String>> evenLevels = new TreeMap<>();

        while (!bfsQ.isEmpty()) {
            int node = bfsQ.poll();
            int lvl = level[node];

            // Level genap DAN dist genap (termasuk bilangan negatif genap)
            if (lvl % 2 == 0 && dist[node] % 2 == 0) {
                evenLevels.computeIfAbsent(lvl, k -> new ArrayList<>()).add(codes[node]);
            }

            for (int child : children.get(node)) {
                level[child] = lvl + 1;
                bfsQ.add(child);
            }
        }

        if (evenLevels.isEmpty()) {
            System.out.println("NO EVEN LEVEL NODES");
        } else {
            for (Map.Entry<Integer, List<String>> entry : evenLevels.entrySet()) {
                List<String> list = entry.getValue();
                Collections.sort(list);
                System.out.println("LEVEL " + entry.getKey() + ": " + String.join(" ", list));
            }
        }

        // =====================================================================
        // FASE 4 : LCA queries
        // =====================================================================
        // Precompute depth dan ancestor untuk LCA naif
        int[] depth = new int[N];
        int[] lcaPar = new int[N];
        Arrays.fill(depth, -1);
        Arrays.fill(lcaPar, -1);
        depth[0] = 0;
        Queue<Integer> bfsQ2 = new LinkedList<>();
        bfsQ2.add(0);
        while (!bfsQ2.isEmpty()) {
            int node = bfsQ2.poll();
            for (int child : children.get(node)) {
                depth[child] = depth[node] + 1;
                lcaPar[child] = node;
                bfsQ2.add(child);
            }
        }

        // Map kode → indeks
        Map<String, Integer> codeMap = new HashMap<>();
        for (int i = 0; i < N; i++) codeMap.put(codes[i], i);

        for (String[] query : queries) {
            int u = codeMap.get(query[0]);
            int v = codeMap.get(query[1]);

            // --- Cari LCA ---
            int a = u, b = v;
            while (depth[a] > depth[b]) a = lcaPar[a];
            while (depth[b] > depth[a]) b = lcaPar[b];
            while (a != b) { a = lcaPar[a]; b = lcaPar[b]; }
            int lca = a;

            // --- Kumpulkan node pada jalur u → LCA → v ---
            List<Integer> path = new ArrayList<>();
            // u → LCA (termasuk kedua ujung)
            int cur = u;
            while (cur != lca) { path.add(cur); cur = lcaPar[cur]; }
            path.add(lca);
            // LCA → v  (LCA sudah ada, tambah sisa menuju v)
            List<Integer> vSide = new ArrayList<>();
            cur = v;
            while (cur != lca) { vSide.add(cur); cur = lcaPar[cur]; }
            Collections.reverse(vSide);
            path.addAll(vSide);

            // --- XOR semua dist pada jalur ---
            long xorVal = 0;
            for (int node : path) xorVal ^= dist[node];

            // --- Klasifikasi ---
            if (xorVal == 0 || xorVal == 1) {
                System.out.println("TRIVIAL: " + xorVal);
            } else if (xorVal > 1 && isPrime(xorVal)) {
                System.out.println("PRIME: " + xorVal);
            } else {
                System.out.println("COMPOSITE: " + xorVal);
            }
        }
    }

    // =========================================================================
    // HELPER : BFS undirected (kembalikan array jarak dari start)
    // =========================================================================
    private int[] bfsUndir(int start, List<List<Integer>> adj) {
        int n = adj.size();
        int[] d = new int[n];
        Arrays.fill(d, -1);
        d[start] = 0;
        Queue<Integer> q = new LinkedList<>();
        q.add(start);
        while (!q.isEmpty()) {
            int cur = q.poll();
            for (int nb : adj.get(cur)) {
                if (d[nb] == -1) { d[nb] = d[cur] + 1; q.add(nb); }
            }
        }
        return d;
    }

    // =========================================================================
    // HELPER : DFS untuk hitung ukuran subtree
    // =========================================================================
    private void computeSubtreeSize(int node, List<List<Integer>> ch, int[] sz) {
        sz[node] = 1;
        for (int child : ch.get(node)) {
            computeSubtreeSize(child, ch, sz);
            sz[node] += sz[child];
        }
    }

    // =========================================================================
    // HELPER : Cek bilangan prima
    // =========================================================================
    private boolean isPrime(long n) {
        if (n < 2) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        for (long i = 3; i * i <= n; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }
}