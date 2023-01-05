import java.sql.*;

public class PayAccount extends Account{
    final  static String accountType="PayAccount";

    @Override
    public void deposit(long accountNo,double amount){
        try {
            String query="select balance from payAccount where accountNo=?";
            PreparedStatement ps = ConnectDatabase.con.prepareStatement(query);
            ps.setLong(1,accountNo);
            ResultSet resultSet= ps.executeQuery();
            if(resultSet.next()) {
                double balance = resultSet.getDouble("balance");
                query = "update payAccount set balance=? where accountNo=?";
                PreparedStatement ps1 = ConnectDatabase.con.prepareStatement(query);
                ps1.setDouble(1, amount + balance);
                ps1.setLong(2, accountNo);
                int count = ps1.executeUpdate();
                System.out.println("deposit successful, " + count + "rows effected");
                ps.close();
                ps1.close();
                updateTransaction(accountNo, amount, accountType, "Credit");
            }
            else
                throw new CustomException("No records for the account no found");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void addAccount(double openingBalance,int personId){
        try {
            long accountNo = generateAccountNo();
            java.sql.Date openingDate = new java.sql.Date(new java.util.Date().getTime());
            String query = "insert into payAccount values(?,?,?,?,?)";
            PreparedStatement ps = ConnectDatabase.con.prepareStatement(query);
            ps.setLong(1, accountNo);
            ps.setDouble(2, openingBalance);
            ps.setDate(3, openingDate);
            ps.setLong(4, personId);
            ps.setString(5, accountType);
            int count = ps.executeUpdate();
            System.out.println("Added account successfully, " + count + " rows effected");
            ps.close();
            showAccountDetails(accountNo);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
    @Override
    public void showAccountDetails(long accountNo){
        try {
            String query = "select * from payAccount s inner join customer c on s.customerId=c.personId where accountNo=?";
            PreparedStatement ps = ConnectDatabase.con.prepareStatement(query);
            ps.setLong(1, accountNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.printf("%15s %15s %15s %15s %15s\n", "Account No", "Name", "OpeningDate", "Balance", "AccountType");
                System.out.printf("%15s %15s %15s %15s %15s\n", rs.getLong("accountNo"), rs.getString("name"), rs.getDate("openingDate"), rs.getDouble("balance"), rs.getString("accountType"));
            }
            else
                throw new CustomException("No records found with the provided account number");
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
    @Override
    public void deleteAccount(long accountNo){
        try {
            String query = "delete from payAccount where accountNo=?";
            PreparedStatement ps = ConnectDatabase.con.prepareStatement(query);
            ps.setLong(1, accountNo);
            int count = ps.executeUpdate();
            if (count > 0) {
                System.out.println("Account removed successfully, " + count + "rows effected");
            } else
                throw new CustomException("No account found");
            ps.close();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public void payToAccount(long accountNo,double amount,long toAccountNo,String type){
        try{
            String query="select balance from payAccount where accountNo=?";
            PreparedStatement ps = ConnectDatabase.con.prepareStatement(query);
            ps.setLong(1,accountNo);
            ResultSet resultSet= ps.executeQuery();
            if (resultSet.next()) {
                double balance = resultSet.getDouble("balance");
                if (balance < amount) {
                    throw new CustomException("Not having sufficient balance");
                }
                query = "update payAccount set balance=? where accountNo=?";
                PreparedStatement ps1 = ConnectDatabase.con.prepareStatement(query);
                ps1.setDouble(1, balance - amount);
                ps1.setLong(2, accountNo);
                int count = ps1.executeUpdate();
                System.out.println("Amount Deducted, " + count + "rows effected");
                ps1.close();
                updateTransaction(accountNo, amount, accountType, "Debit");
                if(type.equalsIgnoreCase(SavingsAccount.accountType))
                    new SavingsAccount().deposit(toAccountNo,amount);
                else
                    deposit(toAccountNo,amount);
            }
            else
                throw new CustomException("No records found");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
