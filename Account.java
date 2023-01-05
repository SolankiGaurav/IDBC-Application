import java.sql.*;
import java.util.Random;

public abstract class Account {

    public long generateAccountNo(){
        Random rand = new Random();
        return rand.nextLong(100000000000L,999999999999L);
    }

    public abstract void addAccount(double openingBalance,int personId);
    public abstract void deposit(long accountNo,double amount);

    public abstract void showAccountDetails(long accountNo);
    public abstract void deleteAccount(long accountNo);

    public void updateTransaction(long accountNo,double amount,String accountType,String transactionType)throws Exception{
        String query;
        if(accountType.equalsIgnoreCase("Savings"))
            query="insert into savingTransactions values(?,?,?,?)";
        else
            query="insert into payTransactions values(?,?,?,?)";
        PreparedStatement ps = ConnectDatabase.con.prepareStatement(query);
        ps.setLong(1,accountNo);
        ps.setString(2,transactionType);
        ps.setDate(3,new Date(new java.util.Date().getTime()));
        ps.setDouble(4,amount);
        int count = ps.executeUpdate();
        System.out.println("Transaction stored successfully, "+count+" rows effected");
        ps.close();

    }

    public void addCustomer(int personId,String name,int age,String address,long mobileNo){
        try {
            String query = "insert into customer values(?,?,?,?,?)";
            PreparedStatement ps = ConnectDatabase.con.prepareStatement(query);
            ps.setInt(1, personId);
            ps.setString(2, name);
            ps.setInt(3, age);
            ps.setString(4, address);
            ps.setLong(5, mobileNo);
            int count = ps.executeUpdate();
            System.out.println("Added customer successfully, " + count + " rows effected");
            ps.close();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
    public void showLastTransactions(long accountNo,String type){
        try {
            String query;
            if (type.equalsIgnoreCase("Savings")) {
                query = "select * from savingTransactions where accountNo=? order by transactionDate desc limit 5";
            } else {
                query = "select * from payTransactions where accountNo=? order by transactionDate desc limit 5";
            }
            PreparedStatement ps = ConnectDatabase.con.prepareStatement(query);
            ps.setLong(1, accountNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.printf("%15s %15s %15s %15s\n", "Account No", "Type", "Date", "Amount");
                System.out.printf("%15s %15s %15s %15s\n", rs.getLong("accountNo"), rs.getString("transactionType"), rs.getDate("transactionDate"), rs.getDouble("amount"));
                while (rs.next())
                    System.out.printf("%15s %15s %15s %15s\n", rs.getLong("accountNo"), rs.getString("transactionType"), rs.getDate("transactionDate"), rs.getDouble("amount"));
            } else
                throw new CustomException("No records found");
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
}
