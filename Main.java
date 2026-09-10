import java.sql.CallableStatement;  // Izmanto MySQL procedūru izsaukšanai
import java.sql.Connection;         // Pārvalda savienojumu
import java.sql.PreparedStatement;  // Izpilda SQL vaicājumus ar parametriem (?)
import java.sql.ResultSet;          // Glabā no datubāzes saņemtos datus (tabulu)
import java.sql.SQLException;       // Ķer datubāzes kļūdas
import oop_world.Country;          // Importē mūsu izveidoto Country klasi
import java.util.ArrayList;         // Dinamiskais masīvs
import java.util.List;              // Saraksta interfeiss

public class Main {

    public static void main(String[] args) {
        // Šeit tiek secīgi izsauktas visas metodes darba pārbaudei
        showCountries();
        findCities("LVA", 50000);
        addCity("Ventspils", "LVA", "Kurzeme", 33000);
        updateCityPopulation(2434, 605000);
        showCitiesWithCountry("Europe");
        callCitiesProcedure("LVA");
        callCountryStatsProcedure("LVA");
        testOOPModel();
    }

    // 1. uzdevums: Parāda 20 lielākās valstis
    public static void showCountries() {
        String sql = "SELECT Code, Name, Continent, Population FROM country ORDER BY Population DESC LIMIT 20";
        
        // Try-with-resources — automātiski aizver resursus (connection, statement, result) beigās
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery() // executeQuery izmanto SELECT vaicājumiem
        ) {
            while (result.next()) { // result.next() pārvieto kursoru uz nākamo rindu
                System.out.println(
                    result.getString("Code") + " | " +      // getString nolasa teksta kolonnu
                    result.getString("Name") + " | " +
                    result.getString("Continent") + " | " +
                    result.getInt("Population")            // getInt nolasa skaitļa kolonnu
                );
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Izsniedz kļūdas ziņojumu, ja kaut kas noiet greizi
        }
    }

    // 2. uzdevums: Meklē pilsētas pēc valsts koda un iedzīvotāju skaita
    public static void findCities(String countryCode, int minimumPopulation) {
        // Jautājuma zīmes (?) ir aizstājējzīmes, ko aizpildām drošā veidā
        String sql = "SELECT ID, Name, District, Population FROM city WHERE CountryCode = ? AND Population >= ? ORDER BY Population DESC";
        
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, countryCode);       // Ievieto 1. jautājuma zīmē tekstu
            statement.setInt(2, minimumPopulation);   // Ievieto 2. jautājuma zīmē skaitli

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    System.out.println(
                        result.getInt("ID") + " | " +
                        result.getString("Name") + " | " +
                        result.getString("District") + " | " +
                        result.getInt("Population")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 3. uzdevums: Pievieno jaunu pilsētu (INSERT)
    public static void addCity(String name, String countryCode, String district, int population) {
        String sql = "INSERT INTO city(Name, CountryCode, District, Population) VALUES (?, ?, ?, ?)";
        
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, name);
            statement.setString(2, countryCode);
            statement.setString(3, district);
            statement.setInt(4, population);

            // executeUpdate izmanto INSERT/UPDATE/DELETE — atgriež izmainīto rindu skaitu (int)
            int rows = statement.executeUpdate();
            System.out.println("Pievienotas rindas: " + rows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 4. uzdevums: Atjauno pilsētas iedzīvotāju skaitu (UPDATE)
    public static void updateCityPopulation(int cityId, int newPopulation) {
        String sql = "UPDATE city SET Population = ? WHERE ID = ?";
        
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, newPopulation);
            statement.setInt(2, cityId);

            int rows = statement.executeUpdate();
            System.out.println("Mainītas rindas: " + rows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 5. uzdevums: Savieno pilsētas ar valstīm (JOIN)
    public static void showCitiesWithCountry(String continent) {
        String sql = "SELECT city.Name AS City, country.Name AS Country, country.Continent, city.Population " +
                     "FROM city JOIN country ON city.CountryCode = country.Code " +
                     "WHERE country.Continent = ? ORDER BY city.Population DESC LIMIT 20";
        
        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, continent);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    System.out.println(
                        result.getString("City") + " | " +
                        result.getString("Country") + " | " +
                        result.getString("Continent") + " | " +
                        result.getInt("Population")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 6. uzdevums: Izsauc MySQL procedūru Nr. 1
    public static void callCitiesProcedure(String countryCode) {
        String sql = "{CALL GetCitiesByCountry(?)}"; // Sintakse procedūras izsaukšanai
        
        try (
            Connection connection = DatabaseConnection.getConnection();
            CallableStatement statement = connection.prepareCall(sql) // prepareCall izmanto procedūrām
        ) {
            statement.setString(1, countryCode);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    System.out.println(
                        result.getString("Name") + " | " +
                        result.getString("District") + " | " +
                        result.getInt("Population")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 7. uzdevums: Izsauc MySQL procedūru Nr. 2 (Statistika)
    public static void callCountryStatsProcedure(String countryCode) {
        String sql = "{CALL GetCountryStatistics(?)}";
        
        try (
            Connection connection = DatabaseConnection.getConnection();
            CallableStatement statement = connection.prepareCall(sql)
        ) {
            statement.setString(1, countryCode);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) { // if, jo sagaidām tikai vienu rezultātu rindu
                    System.out.println("Valsts: " + result.getString("Country"));
                    System.out.println("Pilsētu skaits: " + result.getInt("CityCount"));
                    System.out.println("Lielākā pilsēta: " + result.getString("LargestCity"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 8. uzdevums: OOP modeļa pārbaude (Nolasa datus un pārvērš objektos)
    public static void testOOPModel() {
        List<Country> countryList = new ArrayList<>(); // Izveido tukšu sarakstu
        String sql = "SELECT Code, Name, Continent, Population FROM country LIMIT 5";

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery()
        ) {
            while (result.next()) {
                // Katrai rindai izveido jaunu Country objektu
                Country country = new Country(
                    result.getString("Code"),
                    result.getString("Name"),
                    result.getString("Continent"),
                    result.getInt("Population")
                );
                countryList.add(country); // Pievieno objektu sarakstam
            }

            // Izvelk un izdrukā visus objektus no saraksta
            for (Country c : countryList) {
                System.out.println(c.toString());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}