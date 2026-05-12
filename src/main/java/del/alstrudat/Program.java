package del.alstrudat;

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

    /**
     * Jalankan semua fase sesuai deskripsi soal.
     * Output harus dicetak ke System.out.
     *
     * FASE 1 : Deteksi negative-weight cycle dari node sumber (indeks 0).
     *          Cetak "NEGATIVE CYCLE DETECTED" lalu return jika ada.
     *          Jika tidak, cetak "NO NEGATIVE CYCLE".
     *
     * FASE 2 : Jalankan Bellman-Ford dari node 0.
     *          Cetak jarak tiap node: "code: dist" (urut berdasarkan indeks).
     *          Node tidak terjangkau cetak "code: INF".
     *
     * FASE 3a : Hitung diameter SPT (dalam jumlah edge), cetak "DIAMETER: X".
     *
     * FASE 3b : Hitung jumlah centroid SPT, cetak "CENTROIDS: X".
     *
     * FASE 3c : Level-order traversal SPT, cetak node di level genap
     *           dengan jarak Bellman-Ford genap, diurutkan leksikografis per level.
     *           Format: "LEVEL L: code1 code2 ..."
     *           Jika tidak ada sama sekali, cetak "NO EVEN LEVEL NODES".
     *
     * FASE 4  : Untuk setiap query (u_code, v_code):
     *           Hitung LCA di SPT, XOR semua dist pada jalur u->LCA->v.
     *           Cetak "PRIME: X" / "COMPOSITE: X" / "TRIVIAL: X".
     */
    public void solve() {
        // TODO: Implementasikan semua fase di sini
    }
}
