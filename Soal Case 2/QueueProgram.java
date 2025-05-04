import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class QueueProgram {

    // setup queue dan scanner utk digunakan dalam program
    private static final Queue<String> queue = new LinkedList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;

        do {
            System.out.println("\n===== MENU ANTRIAN =====");
            System.out.println("1. Tambah item ke antrian");
            System.out.println("2. Hapus item dari antrian");
            System.out.println("3. Lihat jumlah item");
            System.out.println("4. Tampilkan semua item");
            System.out.println("5. Keluar");
            System.out.print("Pilih menu: ");
            choice = scanner.nextInt();
            scanner.nextLine(); //clear buffer

            // switch controller utk mengarahkan menu
            switch (choice) {
                case 1 -> addItem();
                case 2 -> removeItem();
                case 3 -> showItemCount();
                case 4 -> displayQueue();
                case 5 -> System.out.println("Keluar dari program.");
                default -> System.out.println("Pilihan tidak valid.");
            }
        } while (choice != 5);

        scanner.close();
    }

    private static void addItem() {
        System.out.print("Masukkan item (string/number): ");
        String item = scanner.nextLine();
        queue.add(item); // gunakan method .add() utk menambahkan item ke antrian
        System.out.println(item + " ditambahkan ke antrian.");
    }

    private static void removeItem() {
        if (!queue.isEmpty()) {
            String removed = queue.poll(); //gunakan method .poll() untuk menghapus item
            System.out.println(removed + " dihapus dari antrian.");
        } else {
            System.out.println("Antrian kosong.");
        }
    }

    private static void showItemCount() {
        System.out.println("Jumlah item dalam antrian: " + queue.size()); //size() untuk mengetahui jumlah antrian
    }

    private static void displayQueue() {
        if (!queue.isEmpty()) {
            System.out.println("Isi antrian: " + queue);
        } else {
            System.out.println("Antrian kosong.");
        }
    }
}
