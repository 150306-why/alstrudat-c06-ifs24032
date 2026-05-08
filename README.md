# alstrudat-c06-ifs24032

## Description

**Sistem Reorganisasi Memori Terenkripsi**

Sebuah sistem memori menyimpan data dalam bentuk **Binary Search Tree (BST)**. Karena terjadi fragmentasi, sistem harus melakukan reorganisasi data dengan aturan yang sangat spesifik menggunakan struktur **Stack** dan **Queue**.

**Aturan Reorganisasi:**
1. **Membangun Pohon:** Masukkan N buah data angka ke dalam BST sesuai urutan input.
2. **Pemrosesan Per Level:** Telusuri pohon tersebut secara level-order (dari atas ke bawah) menggunakan bantuan **Queue**.
3. **Logika Stack & Queue per Level:**
   - **Level Genap (0, 2, ...):** Node di level ini dimasukkan ke **Stack** (dibalik), lalu lakukan operasi pengurangan beruntun: `(angka1 - angka2 - ...)`.
   - **Level Ganjil (1, 3, ...):** Node di level ini dimasukkan ke **Queue** (tetap), lalu lakukan operasi penjumlahan beruntun: `(angka1 + angka2 + ...)`.
4. **Hasil Akhir:** Jumlahkan nilai mutlak (Absolute) dari hasil setiap level untuk mendapatkan Skor Akhir.

## Source Codes

| No | File | Deskripsi |
|---|---|---|
| 1 | App.java | Program utama untuk membaca input (Driver). |
| 2 | Program.java | Logika utama (BST, Stack, Queue, Level Order). |
| 3 | TestScript.sh | Script otomatis untuk pengujian (Bawaan). |

## Test Cases

| No | Input | Output |
|---|---|---|
| 1 | `5`<br>`10 5 15 3 7` | `34` |
| 2 | `3`<br>`10 20 30` | `40` |
| 3 | `1`<br>`50` | `50` |

## Compile

Gunakan perintah berikut untuk menjalankan program secara manual:

```bash
mvn clean package
java -cp target/nama-project-anda-1.0-SNAPSHOT.jar del.alstrudat.App