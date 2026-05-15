package del.alstrudat;
import java.util.*;

public class Program {
    // Gunakan nilai besar untuk Infinity, dibagi 2 agar tidak overflow saat penjumlahan
    static final int INF = Integer.MAX_VALUE / 2;

    static class Edge {
        int u, v, w;
        Edge(int u, int v, int w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }
    }

    // Ubah nama method ini menjadi run() jika App.java memanggil Program.run()
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) return;

        int N = sc.nextInt();
        int M = sc.nextInt();

        String[] codes = new String[N];
        Map<String, Integer> codeToIndex = new HashMap<>();
        for (int i = 0; i < N; i++) {
            codes[i] = sc.next();
            codeToIndex.put(codes[i], i);
        }

        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < M; i++) {
            String uStr = sc.next();
            String vStr = sc.next();
            int w = sc.nextInt();
            edges.add(new Edge(codeToIndex.get(uStr), codeToIndex.get(vStr), w));
        }

        // ==========================================
        // FASE 1 & 2: Bellman-Ford & Negative Cycle
        // ==========================================
        int[] dist = new int[N];
        int[] parent = new int[N];
        Arrays.fill(dist, INF);
        Arrays.fill(parent, -1);
        dist[0] = 0; // Source selalu node 0

        // Relax edges N-1 kali
        for (int i = 0; i < N - 1; i++) {
            for (Edge e : edges) {
                if (dist[e.u] == INF) continue;
                
                if (dist[e.u] + e.w < dist[e.v]) {
                    dist[e.v] = dist[e.u] + e.w;
                    parent[e.v] = e.u;
                } 
                // Tie-breaker: pilih parent dengan indeks lebih kecil
                else if (dist[e.u] + e.w == dist[e.v]) {
                    if (e.u < parent[e.v] || parent[e.v] == -1) {
                        parent[e.v] = e.u;
                    }
                }
            }
        }

        // Deteksi Siklus Negatif (hanya yang bisa dijangkau dari source)
        boolean hasNegativeCycle = false;
        for (Edge e : edges) {
            if (dist[e.u] == INF) continue;
            if (dist[e.u] + e.w < dist[e.v]) {
                hasNegativeCycle = true;
                break;
            }
        }

        if (hasNegativeCycle) {
            System.out.println("NEGATIVE CYCLE DETECTED");
            return; // Hentikan program
        } else {
            System.out.println("NO NEGATIVE CYCLE");
        }

        // Cetak jarak (Fase 2)
        for (int i = 0; i < N; i++) {
            // Asumsi format jarak dari source ke i
            System.out.println(codes[i] + ": " + (dist[i] == INF ? "INF" : dist[i]));
        }

        // ==========================================
        // FASE 3: Konstruksi Shortest Path Tree (SPT)
        // ==========================================
        List<Integer>[] treeAdj = new ArrayList[N]; // Undirected untuk Diameter & Centroid
        List<Integer>[] directedTree = new ArrayList[N]; // Directed untuk Level-Order
        for (int i = 0; i < N; i++) {
            treeAdj[i] = new ArrayList<>();
            directedTree[i] = new ArrayList<>();
        }

        int treeSize = 0;
        boolean[] inTree = new boolean[N];
        for (int i = 0; i < N; i++) {
            if (dist[i] != INF) {
                inTree[i] = true;
                treeSize++;
                if (parent[i] != -1) {
                    treeAdj[parent[i]].add(i);
                    treeAdj[i].add(parent[i]);
                    directedTree[parent[i]].add(i);
                }
            }
        }

        // Fase 3a: Diameter SPT (Menggunakan 2x BFS)
        int[] pass1 = bfs(0, N, treeAdj, inTree);
        int[] pass2 = bfs(pass1[0], N, treeAdj, inTree);
        System.out.println("DIAMETER: " + pass2[1]);

        // Fase 3b: Centroid Decomposition Count
        int[] subtreeSize = new int[N];
        int[] centroidCount = new int[1]; // Gunakan array sebagai referensi counter
        dfsCentroid(0, -1, treeSize, treeAdj, subtreeSize, centroidCount);
        System.out.println("CENTROIDS: " + centroidCount[0]);

        // Fase 3c: Level-Order Traversal dengan Filter
        int[] level = new int[N];
        Arrays.fill(level, -1);
        Queue<Integer> q = new LinkedList<>();
        q.add(0);
        level[0] = 0;
        
        while (!q.isEmpty()) {
            int u = q.poll();
            for (int v : directedTree[u]) {
                level[v] = level[u] + 1;
                q.add(v);
            }
        }

        boolean printedAnyLevel = false;
        for (int l = 0; l < N; l += 2) { // Hanya level genap (0, 2, 4...)
            List<String> validNodes = new ArrayList<>();
            for (int i = 0; i < N; i++) {
                // Filter: level genap DAN dist genap
                if (level[i] == l && dist[i] % 2 == 0 && inTree[i]) {
                    validNodes.add(codes[i]);
                }
            }
            if (!validNodes.isEmpty()) {
                Collections.sort(validNodes); // Urutkan secara leksikografis
                System.out.print("LEVEL " + l + ":");
                for (String c : validNodes) System.out.print(" " + c);
                System.out.println();
                printedAnyLevel = true;
            }
        }
        
        if (!printedAnyLevel) {
            System.out.println("NO EVEN LEVEL NODES");
        }

        // ==========================================
        // FASE 4: Query Lintasan Terkritis (LCA & XOR)
        // ==========================================
        int Q = sc.nextInt();
        for (int i = 0; i < Q; i++) {
            String uStr = sc.next();
            String vStr = sc.next();
            int uIdx = codeToIndex.get(uStr);
            int vIdx = codeToIndex.get(vStr);

            // Cari ancestor dari u
            Set<Integer> uAncestors = new HashSet<>();
            int curr = uIdx;
            while (curr != -1) {
                uAncestors.add(curr);
                curr = parent[curr];
            }

            // Cari LCA dengan menelusuri dari v ke atas
            curr = vIdx;
            int lca = -1;
            while (curr != -1) {
                if (uAncestors.contains(curr)) {
                    lca = curr;
                    break;
                }
                curr = parent[curr];
            }

            // Kumpulkan semua node dalam path (u -> lca -> v)
            List<Integer> pathNodes = new ArrayList<>();
            curr = uIdx;
            while (curr != lca) {
                pathNodes.add(curr);
                curr = parent[curr];
            }
            curr = vIdx;
            while (curr != lca) {
                pathNodes.add(curr);
                curr = parent[curr];
            }
            pathNodes.add(lca); // Tambahkan LCA sekali

            // Hitung XOR dari jarak
            int xorVal = 0;
            for (int node : pathNodes) {
                xorVal ^= dist[node];
            }

            // Evaluasi keprimaan hasil XOR
            if (xorVal == 0 || xorVal == 1) {
                System.out.println("TRIVIAL: " + xorVal);
            } else if (isPrime(xorVal)) {
                System.out.println("PRIME: " + xorVal);
            } else {
                System.out.println("COMPOSITE: " + xorVal);
            }
        }
        sc.close();
    }

    // ==========================================
    // UTILITY METHODS
    // ==========================================

    // BFS untuk mencari diameter pohon (menghitung jumlah edge)
    static int[] bfs(int start, int N, List<Integer>[] adj, boolean[] inTree) {
        int[] edgeCount = new int[N];
        Arrays.fill(edgeCount, -1);
        Queue<Integer> q = new LinkedList<>();
        
        q.add(start);
        edgeCount[start] = 0;
        
        int farthestNode = start;
        int maxDist = 0;
        
        while (!q.isEmpty()) {
            int curr = q.poll();
            for (int nxt : adj[curr]) {
                if (edgeCount[nxt] == -1 && inTree[nxt]) {
                    edgeCount[nxt] = edgeCount[curr] + 1;
                    if (edgeCount[nxt] > maxDist) {
                        maxDist = edgeCount[nxt];
                        farthestNode = nxt;
                    }
                    q.add(nxt);
                }
            }
        }
        return new int[]{farthestNode, maxDist};
    }

    // DFS untuk mencari Centroid Decomposition Count
    static void dfsCentroid(int u, int p, int totalNodes, List<Integer>[] adj, int[] subtreeSize, int[] centroidCount) {
        subtreeSize[u] = 1;
        int maxSubtree = 0;
        for (int v : adj[u]) {
            if (v == p) continue;
            dfsCentroid(v, u, totalNodes, adj, subtreeSize, centroidCount);
            subtreeSize[u] += subtreeSize[v];
            maxSubtree = Math.max(maxSubtree, subtreeSize[v]);
        }
        // Bandingkan component terbesar termasuk parent component
        maxSubtree = Math.max(maxSubtree, totalNodes - subtreeSize[u]);
        
        // Aturan centroid: semua komponen <= N/2
        if (maxSubtree <= totalNodes / 2) {
            centroidCount[0]++;
        }
    }

    // Cek bilangan prima (bilangan <= 1 atau negatif otomatis direturn false)
    static boolean isPrime(int n) {
        if (n <= 1) return false;
        if (n == 2 || n == 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;
        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) return false;
        }
        return true;
    }
}
