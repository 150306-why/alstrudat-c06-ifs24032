# alstrudat-c06-ifs24032

## Description

Diberikan sebuah **kota virtual** yang terdiri dari `N` node (persimpangan) dan `M` edge (jalan) berbobot dan berarah (*weighted directed graph*). Setiap persimpangan memiliki sebuah **kode unik** berupa string.

Tugasmu adalah membangun sistem analisis kota dengan melakukan serangkaian operasi berikut **secara berurutan**:

### Fase 1 — Konstruksi Graf & Deteksi Siklus Negatif
Bangun graf berarah berbobot. Kemudian, tentukan apakah terdapat **negative-weight cycle** yang dapat dijangkau dari node sumber (node dengan indeks 0). Jika ada, cetak `NEGATIVE CYCLE DETECTED` dan hentikan program.

### Fase 2 — Shortest Path Tree (SPT)
Jalankan algoritma **Bellman-Ford** dari node sumber (indeks 0). Bangun **Shortest Path Tree (SPT)**: sebuah pohon berakar di node sumber di mana setiap node terhubung ke parent-nya melalui edge yang digunakan dalam jalur terpendek. Jika ada beberapa jalur terpendek dengan bobot sama, pilih parent dengan **indeks lebih kecil**.

### Fase 3 — Analisis SPT
Pada SPT yang terbentuk, lakukan:

**3a. Diameter SPT**
Temukan **diameter** dari SPT (jalur terpendek terpanjang antara dua node manapun dalam pohon, dihitung berdasarkan **jumlah edge**, bukan bobot).

**3b. Centroid Decomposition Count**
Hitung jumlah node yang merupakan **centroid** dari SPT. Sebuah node `v` disebut centroid jika ketika `v` dihapus dari pohon, semua komponen yang tersisa memiliki ukuran ≤ `N/2`.

**3c. Level-Order Traversal dengan Filter Genap**
Lakukan level-order traversal pada SPT. Cetak hanya node-node yang berada di **level genap** (root = level 0) dan memiliki **jarak terpendek (dari Bellman-Ford) yang merupakan bilangan genap**. Cetak kode string dari node-node tersebut diurutkan **secara leksikografis** per level.

### Fase 4 — Query Lintasan Terkritis
Diberikan `Q` query. Setiap query berisi dua kode string node `(u, v)`. Untuk setiap query:
1. Temukan **LCA (Lowest Common Ancestor)** dari `u` dan `v` pada SPT.
2. Hitung **XOR** dari semua bobot Bellman-Ford (jarak terpendek dari sumber) pada jalur `u → LCA → v` di SPT.
3. Jika hasil XOR adalah bilangan **prima**, cetak `PRIME: [nilai XOR]`. Jika tidak, cetak `COMPOSITE: [nilai XOR]`. Jika nilai XOR adalah 0 atau 1, cetak `TRIVIAL: [nilai XOR]`.

---

## Input Format

```
N M
code_0 code_1 ... code_{N-1}
u_1 v_1 w_1
u_2 v_2 w_2
...
u_M v_M w_M
Q
u_1 v_1
u_2 v_2
...
u_Q v_Q
```

- `1 ≤ N ≤ 300`
- `1 ≤ M ≤ 1000`
- `-100 ≤ w ≤ 100` (bobot edge bisa negatif)
- `1 ≤ Q ≤ 50`
- Kode node adalah string alfanumerik unik, panjang 1–10 karakter.
- Node sumber selalu indeks 0.

## Output Format

```
[Fase 1]
NO NEGATIVE CYCLE
atau
NEGATIVE CYCLE DETECTED

[Jika tidak ada negative cycle, lanjutkan:]

[Fase 2 — cetak jarak Bellman-Ford tiap node, format: "code: dist"]
(diurutkan berdasarkan indeks node)

[Fase 3a]
DIAMETER: [nilai]

[Fase 3b]
CENTROIDS: [jumlah centroid]

[Fase 3c — cetak per level genap yang memenuhi syarat]
LEVEL [L]: code1 code2 ...
(jika tidak ada node yang memenuhi di level genap manapun, cetak "NO EVEN LEVEL NODES")

[Fase 4 — satu baris per query]
PRIME: [nilai] / COMPOSITE: [nilai] / TRIVIAL: [nilai]
```

---

## Source Codes

| No | File | Deskripsi |
|----|------|-----------|
| 1 | App.java | Entry point: membaca input dan memanggil semua fase |
| 2 | Program.java | Implementasi semua logika (diisi oleh penjawab) |

---

## Test Cases

| No | Input | Output |
|----|-------|--------|
| 1  | Lihat `testcases/input1.txt` | Lihat `testcases/expected1.txt` |
| 2  | Lihat `testcases/input2.txt` | Lihat `testcases/expected2.txt` |
| 3  | Lihat `testcases/input3.txt` | Lihat `testcases/expected3.txt` |
| 4  | Lihat `testcases/input4.txt` | Lihat `testcases/expected4.txt` |
| 5  | Lihat `testcases/input5.txt` | Lihat `testcases/expected5.txt` |
| 6  | Lihat `testcases/input6.txt` | Lihat `testcases/expected6.txt` |
| 7  | Lihat `testcases/input7.txt` | Lihat `testcases/expected7.txt` |
| 8  | Lihat `testcases/input8.txt` | Lihat `testcases/expected8.txt` |
| 9  | Lihat `testcases/input9.txt` | Lihat `testcases/expected9.txt` |
| 10 | Lihat `testcases/input10.txt` | Lihat `testcases/expected10.txt` |

---

## Compile

```
mvn clean package
```

## Run

```
java -jar target/alstrudat-cXX-USERNAME-1.0-SNAPSHOT.jar
```
