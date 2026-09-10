import java.sql.Connection; // Interfeiss savienojuma vadībai
import java.sql.DriverManager; // Rīks, kas piereģistrē draiveri un izveido savienojumu
import java.sql.SQLException; // Izņēmums (kļūda), ko met datubāzes darbības

public class DatabaseConnection {

    // Konstantes (final static) — nemainīgas vērtības piekļuvei datubāzei
    private static final String URL = "jdbc:mysql://localhost:3307/world"; // Adrese, ports (3307) un DB nosaukums
    private static final String USER = "root"; // Datubāzes lietotājs
    private static final String PASSWORD = "tava_parole"; // Lietotāja parole

    // Statiska metode — var izsaukt tieši no klases: DatabaseConnection.getConnection()
    public static Connection getConnection() throws SQLException {
        try {
            // Manuāli ielādē MySQL JDBC draivera klasi atmiņā
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Draiveris netika atrasts!");
        }
        // Izveido un atgriež aktīvu savienojuma objektu
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}