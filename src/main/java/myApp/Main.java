package myApp;

import java.sql.*;

import com.mysql.cj.jdbc.Driver;

public class Main {
    private static final String DB_URL = "jdbc:mysql://db4free.net:3306/task_database";
    private static final String USER = "task_user";
    private static final String PASS = "task_321";

    private static Connection createConnection() throws SQLException {
        DriverManager.registerDriver(new Driver());
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public static void main(String[] args){
        if(args.length > 0) {
            try (Connection connection = createConnection()) {
                Statement statement = connection.createStatement();
                if(args[0].equals("1")){
                    statement.execute(Util.createTableQuery());
                    System.out.println("Table was created successfully or already exists.");
                    return;
                } else if (args[0].equals("2") && args.length == 4){
                    statement.execute(Util.singleInsertQuery(args[1], args[2], args[3]));
                    System.out.println("Person added to the table successfully.");
                    return;
                } else if (args[0].equals("3")){
                    ResultSet rs;
                    if(args.length == 1) {
                        rs = statement.executeQuery(Util.distinctSelectQuery(100));
                    }else{
                        rs = statement.executeQuery(Util.distinctSelectQuery(Integer.parseInt(args[1])));
                    }
                    Util.printQueryResult(rs);
                    return;
                } else if (args[0].equals("4")){
                    System.out.println("This will most likely take around 10 minutes.");
                    long start  = System.currentTimeMillis();
                    connection.setAutoCommit(false);
                    statement.execute(Util.procedureInsertMillion());
                    statement.execute(Util.insertHundredFs());
                    connection.commit();
                    connection.setAutoCommit(true);
                    long time = System.currentTimeMillis() - start;
                    System.out.println("Insert finished successfully in " + (time / 1000) + " seconds.");
                    return;
                } else if (args[0].equals("5")){
                    long start  = System.currentTimeMillis();
                    ResultSet rs = statement.executeQuery(Util.conditionalSelectQuery());
                    long time = System.currentTimeMillis() - start;
                    if(args.length > 1){
                        int limit = Integer.parseInt(args[1]);
                        Util.printQueryResult(rs, limit);
                    }else {
                        Util.printQueryResult(rs, 100);
                    }
                    System.out.println("Select took " + (time) + " milliseconds.");
                    return;
                }else if(args[0].equals("6")){
                    statement.execute(Util.dropTableQuery());
                    System.out.println("Table deleted.");
                    return;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("""
                Wrong format of arguments. Please use one of these options:
                \t1 - create table
                \t2 + full_name separated by '_' + birth_date formatted as 'YYYY-MM-DD' + gender as 'female'/'male' - insert person
                \t3 + LIMIT (optional) - select LIMIT rows distinct full_names and birth_dates (default value of LIMIT is 100)
                \t4 - insert million random rows and 100 starting with f
                \t5 + LIMIT (optional) - select males starting with 'F' and print LIMIT of them\t6 - drop table""");
    }
}