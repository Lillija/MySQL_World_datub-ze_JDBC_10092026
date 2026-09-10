package oop_world; // Pakotnes (mapes) nosaukums

public class Country {
    // Private atribūti (laukus redz tikai šī klase) — Iekapsulēšana (Encapsulation)
    private String code;
    private String name;
    private String continent;
    private int population;

    // Noklusētais (tukšais) konstruktors
    public Country() {}

    // Konstruktors ar parametriem — lieto, lai uzreiz izveidotu objektu ar datiem
    public Country(String code, String name, String continent, int population) {
        this.code = code; // "this" norāda uz šīs klases atribūtu
        this.name = name;
        this.continent = continent;
        this.population = population;
    }

    // Getteri (nolasa vērtību) un Setteri (izmaina vērtību)
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContinent() { return continent; }
    public void setContinent(String continent) { this.continent = continent; }

    public int getPopulation() { return population; }
    public void setPopulation(int population) { this.population = population; }

    // Pārrakstītā (Override) toString() metode — ļauj objektu izvadīt teksta veidā
    @Override
    public String toString() {
        return "Country{code='" + code + "', name='" + name + "', continent='" + continent + "', population=" + population + "}";
    }
}