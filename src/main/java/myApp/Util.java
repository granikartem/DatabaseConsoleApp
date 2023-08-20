package myApp;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Random;

public class Util {
    private static final String[] names = {"Abram",
    "Boris",
    "Czar",
    "Dmitr",
    "Egor",
    "Fedor",
    "Grigoriy",
    "Hariton",
    "Ivan",
    "Johan",
    "Konstantin",
    "Lavr",
    "Maksim",
    "Nik",
    "Oleg",
    "Petr",
    "Qasim",
    "Roman",
    "Sergey",
    "Tom",
    "Uliy",
    "Vlad",
    "William",
    "Xavier",
    "Yan",
    "Zoy"};
    static Random random = new Random();
    private static String addQuotationMarks(String s){
        return "'" + s + "'";
    }

    public static String createTableQuery(){
        return "CREATE TABLE IF NOT EXISTS PEOPLE (" +
                "ID SERIAL PRIMARY KEY," +
                " FULL_NAME VARCHAR(100)," +
                " BIRTH_DATE DATE," +
                " GENDER BOOLEAN);";
    }
    public static String dropTableQuery(){
        return "DROP TABLE IF EXISTS PEOPLE;";
    }

    public static String singleInsertQuery(String fullName, String birthDate, String gender) throws IllegalArgumentException{
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO PEOPLE (FULL_NAME, BIRTH_DATE, GENDER) VALUES(");
        sb.append(addQuotationMarks(fullName)).append(", ");
        sb.append(addQuotationMarks(birthDate)).append(", ");
        if(gender.equalsIgnoreCase("male")){
            sb.append("true");
        } else if (gender.equalsIgnoreCase("female")) {
            sb.append("false");
        } else {
            throw new IllegalArgumentException("Wrong gender format. Use 'male' or 'female'.");
        }
        sb.append(" );");
        return sb.toString();
    }
    public static String halfMillionInsertQuery(){
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO PEOPLE (FULL_NAME, BIRTH_DATE, GENDER) VALUES");
        for (int i = 0; i < 500000; i++) {
            boolean gender = random.nextBoolean();
            sb.append("(");
            if(gender){
                sb.append(addQuotationMarks(names[random.nextInt(26)] + "ov_"
                        + names[random.nextInt(26)] +"_"
                        + names[random.nextInt(26)] + "ovich")).append(", ");
                sb.append(addQuotationMarks("1000-01-01")).append(", ").append("true").append(")");
            }else{
                sb.append(addQuotationMarks(names[random.nextInt(26)] + "ova_"
                        + names[random.nextInt(26)] +"a_"
                        + names[random.nextInt(26)] + "ovna")).append(", ");
                sb.append(addQuotationMarks("1000-01-01")).append(", ").append("false").append(")");
            }
            if(i % 100000 == 0){
                System.out.println("\t" + i);
            }
            if(i != 499999){
                sb.append(", ");
            }
        }
        sb.append(";");
        return sb.toString();
    }
    public static String procedureInsertMillion(){
        return "CALL insertMillion();";
    }

    public static String insertHundredFs(){
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO PEOPLE (FULL_NAME, BIRTH_DATE, GENDER) VALUES");
        for (int i = 0; i < 100; i++) {
            sb.append("(");
                sb.append(addQuotationMarks("Fomin-" + names[random.nextInt(26)] + "ov_"
                        + names[random.nextInt(26)] +"_"
                        + names[random.nextInt(26)] + "ovich")).append(", ");
                sb.append(addQuotationMarks("1000-01-01")).append(", ").append("true").append(")");
            if(i != 99){
                sb.append(", ");
            }
        }
        sb.append(";");
        return sb.toString();
    }

    public static String distinctSelectQuery(int limit){
        return "SELECT DISTINCT FULL_NAME, BIRTH_DATE, GENDER FROM PEOPLE ORDER BY FULL_NAME LIMIT " + limit + ";";
    }

    public static String conditionalSelectQuery(){
        return "SELECT FULL_NAME, BIRTH_DATE, GENDER FROM PEOPLE WHERE GENDER AND FULL_NAME LIKE 'F%';";
    }

    public static void printQueryResult(ResultSet rs) {
        System.out.printf("|%-50s|%-20s|%-10s|%-10s|%n", "Full name", "Birth date", "Gender", "Age");
        try {
            while (rs.next()) {
                printQueryRow(rs);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void printQueryRow(ResultSet rs) {
        try {
            String fullName = rs.getString("FULL_NAME");
            Date birthDate = rs.getDate("BIRTH_DATE");
            boolean genderBoolean = rs.getBoolean("GENDER");
            String gender;
            String birthDateString = birthDate.toString();
            LocalDate now = LocalDate.now().plusDays(1);
            Period period = Period.between(birthDate.toLocalDate(), now);
            String age = String.valueOf(period.getYears());
            if(genderBoolean){
                gender = "Male";
            }else{
                gender = "Female";
            }
            System.out.printf("|%-50s|%-20s|%-10s|%-10s|%n", fullName, birthDateString, gender, age);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
