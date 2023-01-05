import java.sql.*;

public class SavingsAccount extends Account{
    final  static String accountType="Savings";
    final static double rateOfInterest=2.5;

    public double calculateInterest(double amount){
        return amount*rateOfInterest/100;
    }
    @Override
    public void addAccount(double openingBalance,int personId){
        try {
            long accountNo = generateAccountNo();
            double interest = calculateInterest(openingBalance);
            Date openingDate = new Date(new java.util.Date().getTime());
            String query = "insert into savingAccount values(?,?,?,?,?,?)";
            PreparedStatement ps = ConnectDatabase.con.prepareStatement(query);
            ps.setLong(1, accountNo);
            ps.setDouble(2, openingBalance);
            ps.setDouble(3, interest);
            ps.setDate(4, openingDate);
            ps.setLong(5, personId);
            ps.setString(6, accountType);
            int count = ps.executeUpdate();
            System.out.println("Added account successfully, " + count + " rows effected");
            showAccountDetails(accountNo);
            ps.close();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public void updateInterest(long accountNo) throws Exception{
        String query="select balance from savingAccount where accountNo=?";
        PreparedStatement ps = ConnectDatabase.con.prepareStatement(query);
        ps.setLong(1,accountNo);
        ResultSet resultSet= ps.executeQuery();
        resultSet.next();
        double balance = resultSet.getDouble("balance");
        double interest= calculateInterest(balance);
        query="update savingAccount set interest=? where accountNo=?";
        PreparedStatement ps1 = ConnectDatabase.con.prepareStatement(query);
        ps1.setDouble(1,interest);
        ps1.setLong(2,accountNo);
        int count =ps1.executeUpdate();
        System.out.println("update interest successful,"+count+" rows effected");
        ps1.close();
        ps.close();
    }

    @Override
    public void deposit(long accountNo,double amount){
        try {
            String query="select balance from savingAccount where accountNo=?";
            PreparedStatement ps = ConnectDatabase.con.prepareStatement(query);
            ps.setLong(1,accountNo);
            ResultSet resultSet= ps.executeQuery();
            resultSet.next();
            double balance = resultSet.getDouble("balance");
            query="update savingAccount set balance=? where accountNo=?";
            PreparedStatement  ps1 = ConnectDatabase.con.prepareStatement(query);
            ps1.setDouble(1,balance+amount);
            ps1.setLong(2,accountNo);
            int count = ps1.executeUpdate();
            System.out.println("deposit successful, "+count+"rows effected");
            ps.close();
            ps1.close();
            updateInterest(accountNo);
            updateTransaction(accountNo,amount,accountType,"Credit");
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void withdraw(long accountNo,double amount){
        try{
            String query="select balance from savingAccount where accountNo=?";
            PreparedStatement ps = ConnectDatabase.con.prepareStatement(query);
            ps.setLong(1,accountNo);
            ResultSet resultSet= ps.executeQuery();
            if (resultSet.next()) {
                double balance = resultSet.getDouble("balance");
                if (balance < amount)
                   throw new CustomException("Not having sufficient balance");

                query = "update savingAccount set balance=? where accountNo=?";
                PreparedStatement ps1 = ConnectDatabase.con.prepareStatement(query);
                ps1.setDouble(1, balance - amount);
                ps1.setLong(2, accountNo);
                int count = ps1.executeUpdate();
                System.out.println("Withdraw successful, " + count + "rows effected");
                ps1.close();
                updateInterest(accountNo);
                updateTransaction(accountNo, amount, accountType, "Debit");
            }
            else
                throw new CustomException("No records found");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    @Override
    public void showAccountDetails(long accountNo){
        try {
            String query = "select * from savingAccount s inner join customer c on s.personId=c.personid where accountNo=?";
            PreparedStatement ps = ConnectDatabase.con.prepareStatement(query);
            ps.setLong(1, accountNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.printf("%15s %15s %15s %15s %15s\n", "Account No", "Name", "Opening Date", "Balance", "Interest");
                System.out.printf("%15s %15s %15s %15s %15s\n", rs.getLong("accountNo"), rs.getString("name"), rs.getDate("openingDate"), rs.getDouble("balance"), rs.getDouble("interest"));
            }
            else
                throw new CustomException("No records found with the provided account number");
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
    @Override
    public void deleteAccount(long accountNo){
        try {
            String query = "delete from savingAccount where accountNo=?";
            PreparedStatement ps = ConnectDatabase.con.prepareStatement(query);
            ps.setLong(1, accountNo);
            int count = ps.executeUpdate();
            if (count > 0) {
                System.out.println("Account removed successfully, " + count + "rows effected");
            }
            else
                throw new CustomException("No record found");
            ps.close();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
}
