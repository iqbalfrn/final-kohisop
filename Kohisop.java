import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;
import java.util.Arrays;

public class Kohisop {
    private static final int MAKS_PESAN_PER_KATEGORI = 5;
    private static final int MAKS_PESAN_MINUMAN = 3;
    private static final int MAKS_PESAN_MAKANAN = 2;

    private static final Menu Menu = new Menu();
    private static final Order[] OrderMinuman = new Order[MAKS_PESAN_PER_KATEGORI];
    private static final Order[] OrderMakanan = new Order[MAKS_PESAN_PER_KATEGORI];
    private static int JumlahOrderMinuman = 0;
    private static int JumlahOrderMakanan = 0;

    private static final Scanner scanner = new Scanner(System.in);

    private static final LinkedList<Member> databaseMembership = new LinkedList<>();
    private static final PriorityQueue<Order> antrianMakanan = new PriorityQueue<>(
        (a, b) -> Integer.compare(b.getItem().getHarga(), a.getItem().getHarga()));
    private static final Stack<Order> antrianMinuman = new Stack<>();
    private static int jumlahPelanggan = 0;

    private static Member cariAtauBuatMember(String nama) {
        for (Member member : databaseMembership) {
            if (member.getNama().equalsIgnoreCase(nama))
                return member;
        }
        Member memberBaru = new Member(nama);
        databaseMembership.add(memberBaru);
        return memberBaru;
    }

    public static void main(String[] args) {
        System.out.println("==========================");
        System.out.println("Selamat datang di KohiSop!");
        System.out.println("==========================");

        while (true) {
            System.out.print("\nMasukkan nama pelanggan \n(ketik 'EXIT' untuk keluar): ");
            String namaPelanggan = scanner.nextLine().trim();
            if (namaPelanggan.equalsIgnoreCase("EXIT")) {
                System.out.println("Program selesai.");
                break;
            }
            Member member = cariAtauBuatMember(namaPelanggan);
            JumlahOrderMinuman = 0;
            JumlahOrderMakanan = 0;
            for (int i = 0; i < MAKS_PESAN_PER_KATEGORI; i++) {
                OrderMinuman[i] = null;
                OrderMakanan[i] = null;
            }
            boolean order = ProsesOrder();
            if (order) {
                double totalHarga = hitungTotalHarga(member);
                MetodePembayaran metodePembayaran = piliMetodePembayaran(totalHarga);
                Currency currency = selectCurrency();
                cetakKuitansi(metodePembayaran, currency, member);
                jumlahPelanggan++;
                prosesDapur();
            } else {
                System.out.println("Pesanan dibatalkan. Terima kasih telah mengunjungi KohiSop.");
            }
        }
        scanner.close();
    }

    private static double hitungTotalHarga(Member member) {
        double total = 0;
        for (int i = 0; i < JumlahOrderMinuman; i++) {
            total += OrderMinuman[i].getTotalHarga() + OrderMinuman[i].getJumlahPajak(member);
        }
        for (int i = 0; i < JumlahOrderMakanan; i++) {
            total += OrderMakanan[i].getTotalHarga() + OrderMakanan[i].getJumlahPajak(member);
        }
        return total;
    }

    private static boolean ProsesOrder() {
        boolean Memproses = true;
        DaftarMenu();

        while (Memproses) {
            System.out.println("\nMasukkan kode minuman/makanan (CC untuk membatalkan pesanan):");
            String itemKode = scanner.nextLine().trim();
            if (itemKode.equalsIgnoreCase("CC")) {
                return false;
            }

            Item Orderan = Menu.KodeMenu(itemKode);
            if (Orderan == null) {
                System.out.println("Kode tidak valid! Silakan masukkan kode yang benar.");
                continue;
            }

            if (Orderan instanceof Minuman) {
                if (JumlahOrderMinuman >= MAKS_PESAN_PER_KATEGORI) {
                    System.out.println("Jumlah pesanan minuman telah mencapai batas maksimum! Silakan pilih Makanan.");
                    continue;
                }
            } else {
                if (JumlahOrderMakanan >= MAKS_PESAN_PER_KATEGORI) {
                    System.out.println("Jumlah pesanan makanan telah mencapai batas maksimum! Silakan pilih Minuman.");
                    continue;
                }
            }

            int kuantitas = JumlahMakananAtauMinuman(Orderan);
            if (kuantitas == 0) {
                continue;
            } else if (kuantitas == -1) {
                return false;
            } else if (Orderan instanceof Minuman) {
                OrderMinuman[JumlahOrderMinuman] = new Order(itemKode, Orderan, kuantitas);
                antrianMinuman.push(OrderMinuman[JumlahOrderMinuman]);
                JumlahOrderMinuman++;
            } else {
                OrderMakanan[JumlahOrderMakanan] = new Order(itemKode, Orderan, kuantitas);
                antrianMakanan.offer(OrderMakanan[JumlahOrderMakanan]);
                JumlahOrderMakanan++;
            }

            TampilanOrder();

            if (JumlahOrderMinuman >= MAKS_PESAN_PER_KATEGORI && JumlahOrderMakanan >= MAKS_PESAN_PER_KATEGORI) {
                System.out.println("\nSemua kategori pesanan telah mencapai batas maksimum! Lanjut ke pembayaran");
                return true;
            } else {
                System.out.println("\nApakah anda ingin menambah pesanan? (Y/N)");
                String lanjut = scanner.nextLine().trim();
                if (!lanjut.equalsIgnoreCase("Y")) {
                    Memproses = false;
                }
            }
        }

        return true;
    }

    private static void prosesDapur() {
        if (jumlahPelanggan >= 3) {
            System.out.println("\n=== Proses Dapur ===");
            System.out.println("Pesanan Makanan:");
            if (antrianMakanan.isEmpty()) {
                System.out.println("Tidak ada pesanan makanan.");
            } else {
                while (!antrianMakanan.isEmpty()) {
                    Order order = antrianMakanan.poll();
                    System.out.printf("%s: %s (%d IDR, %d porsi)\n",
                            order.getItemKode(), order.getItem().getNama(),
                            order.getItem().getHarga(), order.getKuantitas());
                }
            }
            System.out.println("\nPesanan Minuman:");
            if (antrianMinuman.isEmpty()) {
                System.out.println("Tidak ada pesanan minuman.");
            } else {
                while (!antrianMinuman.isEmpty()) {
                    Order order = antrianMinuman.pop();
                    System.out.printf("%s: %s (%d IDR, %d porsi)\n",
                            order.getItemKode(), order.getItem().getNama(),
                            order.getItem().getHarga(), order.getKuantitas());
                }
            }
        }
    }

    private static void DaftarMenu() {
        System.out.println("\nMenu Minuman:");
        System.out.println("Kode | Menu Minuman                        | Harga (Rp)");
        System.out.println("--------------------------------------------------");

        Item[] DaftarMinuman = Menu.getDaftarMinuman();
        for (Item Minuman : DaftarMinuman) {
            System.out.printf("%-4s | %-35s | %d\n", Minuman.getKode(), Minuman.getNama(), Minuman.getHarga());
        }

        System.out.println("\nMenu Makanan:");
        System.out.println("Kode | Menu Makanan                        | Harga (Rp)");
        System.out.println("--------------------------------------------------");

        Item[] DaftarMakanan = Menu.getDaftarMakanan();
        for (Item Makanan : DaftarMakanan) {
            System.out.printf("%-4s | %-35s | %d\n", Makanan.getKode(), Makanan.getNama(), Makanan.getHarga());
        }
    }

    private static int JumlahMakananAtauMinuman(Item item) {
        int JumlahMaks = (item instanceof Minuman) ? MAKS_PESAN_MINUMAN : MAKS_PESAN_MAKANAN;

        while (true) {
            System.out.println("Masukkan jumlah " + item.getNama() + " (1-" + JumlahMaks
                    + ", 0/S untuk skip, CC untuk membatalkan pesanan):");
            String inputJumlah = scanner.nextLine().trim();

            if (inputJumlah.equalsIgnoreCase("CC")) {
                return -1;
            } else if (inputJumlah.equalsIgnoreCase("S") || inputJumlah.equals("0")) {
                return 0;
            } else if (inputJumlah.isEmpty()) {
                return 1;
            }

            try {
                int quantity = Integer.parseInt(inputJumlah);
                if (quantity > 0 && quantity <= JumlahMaks) {
                    return quantity;
                } else {
                    System.out.println("Jumlah tidak valid! Harap masukkan jumlah 1-" + JumlahMaks + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("inputJumlah tidak valid! Harap masukkan angka.");
            }
        }
    }

    private static void TampilanOrder() {
        if (JumlahOrderMinuman > 0) {
            System.out.println("\nDaftar Pesanan Minuman:");
            System.out.println("Kode | Minuman                              | Kuantitas");
            System.out.println("-------------------------------------------------------");

            Order[] tempMinuman = new Order[JumlahOrderMinuman];
            System.arraycopy(OrderMinuman, 0, tempMinuman, 0, JumlahOrderMinuman);
            Arrays.sort(tempMinuman, (a, b) -> Integer.compare(b.getItem().getHarga(), a.getItem().getHarga()));

            for (int i = 0; i < JumlahOrderMinuman; i++) {
                Order order = tempMinuman[i];
                System.out.printf("%-4s | %-35s | %d\n", order.getItem().getKode(), order.getItem().getNama(),
                        order.getKuantitas());
            }
        }

        if (JumlahOrderMakanan > 0) {
            System.out.println("\nDaftar Pesanan Makanan:");
            System.out.println("Kode | Makanan                              | Kuantitas");
            System.out.println("-------------------------------------------------------");

            Order[] tempMakanan = new Order[JumlahOrderMakanan];
            System.arraycopy(OrderMakanan, 0, tempMakanan, 0, JumlahOrderMakanan);
            Arrays.sort(tempMakanan, (a, b) -> Integer.compare(b.getItem().getHarga(), a.getItem().getHarga()));

            for (int i = 0; i < JumlahOrderMakanan; i++) {
                Order order = tempMakanan[i];
                System.out.printf("%-4s | %-35s | %d\n", order.getItem().getKode(), order.getItem().getNama(),
                        order.getKuantitas());
            }
        }
    }

    private static MetodePembayaran piliMetodePembayaran(double totalHarga) {
        while (true) {
            System.out.println("\nPilih metode pembayaran:");
            System.out.println("1. Tunai");
            System.out.println("2. QRIS (Diskon 5%)");
            System.out.println("3. eMoney (Diskon 7%, Biaya Admin 20 IDR)");

            System.out.print("Pilihan (1-3): ");
            String input = scanner.nextLine().trim();

            try {
                int choice = Integer.parseInt(input);
                switch (choice) {
                    case 1 -> {
                        return new Tunai();
                    }
                    case 2 -> {
                        System.out.print("Masukkan saldo Anda untuk QRIS: ");
                        double saldo = Double.parseDouble(scanner.nextLine().trim());
                        double totalSetelahDiskon = totalHarga * (1 - new QRIS().getDiskon());
                        if (saldo >= totalSetelahDiskon) {
                            return new QRIS();
                        } else {
                            System.out.println("saldo anda kurang untuk melakukan pembayaran");
                            continue;
                        }
                    }
                    case 3 -> {
                        System.out.print("Masukkan saldo Anda untuk eMoney: ");
                        double saldo = Double.parseDouble(scanner.nextLine().trim());
                        double totalSetelahDiskon = (totalHarga * (1 - new EMoney().getDiskon())) + new EMoney().getAdminFee();
                        if (saldo >= totalSetelahDiskon) {
                            return new EMoney();
                        } else {
                            System.out.println("saldo anda kurang untuk melakukan pembayaran");
                            continue;
                        }
                    }
                    default -> System.out.println("Pilihan tidak valid! Harap masukkan angka 1-3.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid! Harap masukkan angka.");
            }
        }
    }

    private static Currency selectCurrency() {
        System.out.println("\nPilih mata uang pembayaran:");
        System.out.println("1. IDR (Indonesian Rupiah)");
        System.out.println("2. USD (US Dollar) - 1 USD = 15 IDR");
        System.out.println("3. JPY (Japanese Yen) - 10 JPY = 1 IDR");
        System.out.println("4. MYR (Malaysian Ringgit) - 1 MYR = 4 IDR");
        System.out.println("5. EUR (Euro) - 1 EUR = 14 IDR");

        while (true) {
            System.out.print("Pilihan (1-5): ");
            String input = scanner.nextLine().trim();
            try {
                int choice = Integer.parseInt(input);
                switch (choice) {
                    case 1 -> {
                        return PembayaranUangAsing.createCurrency("IDR");
                    }
                    case 2 -> {
                        return PembayaranUangAsing.createCurrency("USD");
                    }
                    case 3 -> {
                        return PembayaranUangAsing.createCurrency("JPY");
                    }
                    case 4 -> {
                        return PembayaranUangAsing.createCurrency("MYR");
                    }
                    case 5 -> {
                        return PembayaranUangAsing.createCurrency("EUR");
                    }
                    default -> System.out.println("Pilihan tidak valid! Harap masukkan angka 1-5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid! Harap masukkan angka.");
            }
        }
    }

    private static void cetakKuitansi(MetodePembayaran metodePembayaran, Currency currency, Member member) {
        System.out.println("\n===================================================");
        System.out.println("               KUITANSI KOHISOP                   ");
        System.out.println("===================================================");
        System.out.printf("Nama Pelanggan: %s\n", member.getNama());
        System.out.printf("Kode Member: %s\n", member.getKodeMember());

        double subtotalMinuman = 0;
        double totalPajakMinuman = 0;
        double subtotalMakanan = 0;
        double totalPajakMakanan = 0;

        if (JumlahOrderMakanan > 0) {
            System.out.println("\nPesanan Makanan:");
            System.out.println("Kode | Makanan                       | Harga  | Jml | Total  | Pajak");
            System.out.println("------------------------------------------------------------------");

            Order[] tempMakanan = new Order[JumlahOrderMakanan];
            System.arraycopy(OrderMakanan, 0, tempMakanan, 0, JumlahOrderMakanan);
            Arrays.sort(tempMakanan, (a, b) -> Integer.compare(b.getItem().getHarga(), a.getItem().getHarga()));

            for (int i = 0; i < JumlahOrderMakanan; i++) {
                Order order = tempMakanan[i];
                double pajakPerItem = order.getItem().Pajak();
                double totalPajakItem = (order.getTotalHarga() * pajakPerItem) * order.getKuantitas();
                System.out.printf("%-4s | %-30s | %6d | %3d | %6d | %5.0f\n",
                        order.getItem().getKode(), order.getItem().getNama(),
                        order.getItem().getHarga(), order.getKuantitas(),
                        order.getTotalHarga(), totalPajakItem);
                subtotalMakanan += order.getTotalHarga();
                totalPajakMakanan += totalPajakItem;
            }
        }

        if (JumlahOrderMinuman > 0) {
            System.out.println("\nPesanan Minuman:");
            System.out.println("Kode | Minuman                       | Harga  | Jml | Total  | Pajak");
            System.out.println("------------------------------------------------------------------");

            Order[] tempMinuman = new Order[JumlahOrderMinuman];
            System.arraycopy(OrderMinuman, 0, tempMinuman, 0, JumlahOrderMinuman);
            Arrays.sort(tempMinuman, (a, b) -> Integer.compare(b.getItem().getHarga(), a.getItem().getHarga()));

            for (int i = 0; i < JumlahOrderMinuman; i++) {
                Order order = tempMinuman[i];
                double pajakPerItem = order.getItem().Pajak();
                double totalPajakItem = (order.getTotalHarga() * pajakPerItem) * order.getKuantitas();
                System.out.printf("%-4s | %-30s | %6d | %3d | %6d | %5.0f\n",
                        order.getItem().getKode(), order.getItem().getNama(),
                        order.getItem().getHarga(), order.getKuantitas(),
                        order.getTotalHarga(), totalPajakItem);
                subtotalMinuman += order.getTotalHarga();
                totalPajakMinuman += totalPajakItem;
            }
        }

        double subtotal = subtotalMinuman + subtotalMakanan;
        double totalPajak = totalPajakMinuman + totalPajakMakanan;
        double lihatTotalDenganPajak = subtotal + totalPajak;

        double JumlahDiskon = lihatTotalDenganPajak * metodePembayaran.getDiskon();
        int adminFee = metodePembayaran.getAdminFee();
        double hasilAkhir = metodePembayaran.totalBiayaAkhir(lihatTotalDenganPajak);

        int poinSebelum = member.getPoin();
        if (currency.getKode().equals("IDR")) {
            hasilAkhir = member.potongDenganPoin(hasilAkhir);
        }
        member.tambahPoin(hasilAkhir);

        double subtotalMataUang = currency.konversiDariIDR(subtotal);
        double totalDenganPajak = currency.konversiDariIDR(lihatTotalDenganPajak);
        double JumlahAkhir = currency.konversiDariIDR(hasilAkhir);

        System.out.println("\n---------------------------------------------------");
        System.out.printf("Subtotal Minuman                : %14.0f IDR\n", subtotalMinuman);
        System.out.printf("Pajak Minuman                   : %14.0f IDR\n", totalPajakMinuman);
        System.out.printf("Subtotal Makanan                : %14.0f IDR\n", subtotalMakanan);
        System.out.printf("Pajak Makanan                   : %14.0f IDR\n", totalPajakMakanan);
        System.out.println("---------------------------------------------------");
        System.out.printf("Total sebelum pajak             : %14.0f IDR\n", subtotal);
        System.out.printf("Total pajak                     : %14.0f IDR\n", totalPajak);
        System.out.printf("Total dengan pajak              : %14.0f IDR\n", lihatTotalDenganPajak);
        System.out.println("---------------------------------------------------");
        System.out.printf("Metode Pembayaran               : %s\n", metodePembayaran.getNama());
        System.out.printf("Diskon                          : %14.0f IDR (%.0f%%)\n",
                JumlahDiskon, metodePembayaran.getDiskon() * 100);
        if (adminFee > 0) {
            System.out.printf("Biaya Admin                     : %14d IDR\n", adminFee);
        }
        System.out.printf("Poin Sebelum Transaksi          : %14d\n", poinSebelum);
        System.out.printf("Poin Setelah Transaksi          : %14d\n", member.getPoin());
        System.out.println("---------------------------------------------------");
        System.out.printf("Mata Uang                       : %s\n", currency.getNama());
        System.out.printf("Total sebelum pajak (%s)         : %s\n",
                currency.getKode(), currency.FormatJumlah(subtotalMataUang));
        System.out.printf("Total dengan pajak (%s)          : %s\n",
                currency.getKode(), currency.FormatJumlah(totalDenganPajak));
        System.out.printf("Total yang harus dibayar (%s)    : %s\n",
                currency.getKode(), currency.FormatJumlah(JumlahAkhir));
        System.out.println("===================================================");
        System.out.println("     Terima kasih dan silakan datang kembali!     ");
        System.out.println("===================================================");
    }
}