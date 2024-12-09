import java.sql.*;
import java.util.Scanner;

public class Main {
    public static Scanner scanner = new Scanner(System.in);

    private static Connection connect(){

        String url = "jdbc:sqlite:C:\\Users\\Tova\\SQLiteLabb.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    private static void showMenu() {
        System.out.println("\nMeny:" +
                "\n1. Visa alla fiskar" +
                "\n2. Lägg till fisk" +
                "\n3. Uppdatera fisk" +
                "\n4. Ta bort fisk" +
                "\n5. Sök efter fisk baserat på namn" +
                "\n6. Uppdatera favoritstatus" +
                "\n7. Se favoritfiskar" +
                "\n8. Antal fiskar sammanlagt" +
                "\n9. Fiskar sorterade baserat på ålder" +
                "\n10. Visa menyn" +
                "\n11. Avsluta");
    }

    public static void main(String[] args) {
        boolean quit = false;
        showMenu();
        while (!quit) {
            int select = scanner.nextInt();
            scanner.nextLine();

            switch (select) {
                case 1: showAllFish(); break;
                case 2: addFish(); break;
                case 3: updateFish(); break;
                case 4: deleteFish(scanner.nextInt()); break;
                case 5: fishNameSearch(); break;
                case 6: showMenu(); break;
                case 7: updateToFavourite(); break;
                case 8: showFavourite(); break;
                case 9: showFishSum(); break;
                case 10: showAgeIncreasing(); break;
                case 11: quit = true;

            }
        }
    }


    private static void fishNameSearch(){
        String sql = "SELECT * FROM fisk WHERE fiskNamn = ?";
        try {
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            String inputFiskNamn = scanner.nextLine();

            pstmt.setString(1,inputFiskNamn);

            ResultSet rs  = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("Id: " + rs.getInt("fiskId") +  "\t" +
                        "Namn: " + rs.getString("fiskNamn") + "\t" +
                        "Ålder: " + rs.getString("fiskAlder"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }

    private static void showAllFish(){
        String sql = "SELECT * FROM fisk";

        try {
            Connection conn = connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);


            while (rs.next()) {
                System.out.println("Id: " + rs.getInt("fiskId") +  "\t" +
                        "Namn: " + rs.getString("fiskNamn") + "\t" +
                        "Ålder: " + rs.getString("fiskAlder") + "\t" +
                        "Art-id: " + rs.getString("fiskArtId") + "\t" +
                        "Mat-id: " + rs.getString("fiskMatId"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    private static void addFish() {
        System.out.println("Ange namn på fisken:");
        String name = scanner.nextLine();

        System.out.println("Ange ålder på fisken:");
        int age = Integer.parseInt(scanner.nextLine());

        System.out.println("Ange artens ID för fisken:");
        int species = Integer.parseInt(scanner.nextLine());

        System.out.println("Ange matens ID för fisken:");
        int food = Integer.parseInt(scanner.nextLine());

        String sql = "INSERT INTO fisk(fiskNamn, fiskAlder, fiskArtId, fiskMatId) VALUES(?,?,?,?)";

        try {
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setInt(3, species);
            pstmt.setInt(4, food);
            pstmt.executeUpdate();
            System.out.println("Du har lagt till en ny fisk!");
        } catch (SQLException e) {
            System.out.println("Fel: " + e.getMessage());
        }
    }

    private static void updateFish(){
        System.out.println("Ange namn på fisken:");
        String name = scanner.nextLine();

        System.out.println("Ange ålder på fisken:");
        int age = Integer.parseInt(scanner.nextLine());

        System.out.println("Ange artens ID för fisken:");
        int species = Integer.parseInt(scanner.nextLine());

        System.out.println("Ange matens ID för fisken:");
        int food = Integer.parseInt(scanner.nextLine());

        System.out.println("Ange ID för fisken du vill uppdatera:");
        int id = Integer.parseInt(scanner.nextLine());



        String sql = "UPDATE fisk SET fiskNamn = ? , "
                + "fiskAlder = ? , "
                + "fiskArtId = ? ,"
                + "fiskMatId = ? "
                + "WHERE fiskId = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setInt(3, species);
            pstmt.setInt(4, food);
            pstmt.setInt(5, id);
            // update
            pstmt.executeUpdate();
            System.out.println("Du har uppdaterat vald fisk");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void deleteFish(int id){
        String sql = "DELETE FROM fisk WHERE fiskId = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setInt(1, id);
            // execute the delete statement
            pstmt.executeUpdate();
            System.out.println("Du har tagit bort fisken");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void updateToFavourite(){
        System.out.println("Ange namn på fisken:");
        String name = scanner.nextLine();

        System.out.println("Vill du markera denna fisk som favorit? (ja/nej)");

        String response = scanner.nextLine().trim().toLowerCase();

        int isFavorite = response.equals("ja") ? 1 : 0;

        String sql = "UPDATE fisk SET isFavorite = ? WHERE fiskNamn = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, isFavorite);
            pstmt.setString(2, name);
            pstmt.executeUpdate();
            System.out.println("Fiskens favoritstatus har uppdaterats.");
        } catch (SQLException e) {
            System.out.println("Fel vid uppdatering: " + e.getMessage());
        }
    }

    private static void showFavourite(){
        String sql = "SELECT * FROM fisk WHERE isFavorite = 1";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            boolean foundFavorites = false;

            while (rs.next()) {
                foundFavorites = true;
                System.out.println("Id: " + rs.getInt("fiskId") + "\t" +
                        "Namn: " + rs.getString("fiskNamn") + "\t" +
                        "Ålder: " + rs.getInt("fiskAlder"));
            }

            if (!foundFavorites) {
                System.out.println("Inga favoriter hittades.");
            }
        } catch (SQLException e) {
            System.out.println("Fel vid hämtning av favoriter: " + e.getMessage());
        }
    }

    private static void showFishSum() {
        String sql = "SELECT COUNT(*) AS totalFiskar FROM fisk";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Hämta resultatet från frågan
            if (rs.next()) {
                int totalFish = rs.getInt("totalFiskar");  // Hämta antalet från resultatset
                System.out.println("Totalt antal fiskar: " + totalFish);
            }
        } catch (SQLException e) {
            System.out.println("Fel vid hämtning av antal fiskar: " + e.getMessage());
        }

    }

    private static void showAgeIncreasing() {
        String sql = "SELECT * FROM fisk ORDEr BY fiskAlder";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.println(
                        "Namn: " + rs.getString("fiskNamn") + "\t" +
                        "Ålder: " + rs.getInt("fiskAlder"));
            }
        } catch (SQLException e) {
            System.out.println("Fel vid hämtning av antal fiskar: " + e.getMessage());
        }

    }


}