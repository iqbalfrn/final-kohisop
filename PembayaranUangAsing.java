public class PembayaranUangAsing {
    public static Currency createCurrency(String Kode)  {
        return switch (Kode.toUpperCase()) {
            case "IDR" -> new IDRCurrency();
            case "USD" -> new USDCurrency();
            case "JPY" -> new JPYCurrency();
            case "MYR" -> new MYRCurrency();
            case "EUR" -> new EURCurrency();
            default -> new IDRCurrency();
        };
    }

    static class IDRCurrency extends UangAsing {
        @Override
        public String getKode() {
            return "IDR";
        }

        @Override
        public String getNama() {
            return "Indonesian Rupiah";
        }

        @Override
        public double getKurs() {
            return 1.0;
        }
    }

    static class USDCurrency extends UangAsing {
        @Override
        public String getKode() {
            return "USD";
        }

        @Override
        public String getNama() {
            return "US Dollar";
        }

        @Override
        public double getKurs() {
            return 15.0;
        }
    }

    static class JPYCurrency extends UangAsing {
        @Override
        public String getKode() {
            return "JPY";
        }
        @Override
        public String getNama() {
            return "Japanese Yen";
        }

        @Override
        public double getKurs() {
            return 0.1;
        }
    }

    static class MYRCurrency extends UangAsing {
        @Override
        public String getKode() {
            return "MYR";
        }
        @Override
        public String getNama() {
            return "Malaysian Ringgit";
        }

        @Override
        public double getKurs() {
            return 4.0;
        }
    }

    static class EURCurrency extends UangAsing {
        @Override
        public String getKode() {
            return "EUR";
        }

        @Override
        public String getNama() {
            return "EURO";
        }

        @Override
        public double getKurs() {
            return 14.0;
        }
    }
}