package del.alstrudat;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 1. Membaca jumlah data (N)
        // Ini penting agar sistem otomatis bisa mulai membaca input
        if (!sc.hasNextInt()) {
            sc.close();
            return;
        }
        int n = sc.nextInt();

        // 2. Membaca N buah angka memori
        int[] data = new int[n];
        for (int i = 0; i < n; i++) {
            if (sc.hasNextInt()) {
                data[i] = sc.nextInt();
            }
        }

        // 3. Mengirim data ke Program.java
        // Di sinilah App.java memanggil fungsi 'solve' yang akan dikerjakan penjawab
        Program.solve(n, data);

        sc.close();
    }
}