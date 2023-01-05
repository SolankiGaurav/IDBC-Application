import java.util.Scanner;

public class IDBCBank {
    public static void main(String[] args) throws Exception {
        interfaceOfBank();
        ConnectDatabase.con.close();
    }

    public static void interfaceOfBank(){
        Scanner sc = new Scanner(System.in);
        System.out.println("--------------------------Welcome to Idbc Bank------------------------------");
        System.out.println("Choose the account type you want to proceed with");
        System.out.println("1- Savings Account\n2- Pay Account  ");
        int choice = sc.nextInt();

        if(choice==1) {

            SavingsAccount sa = new SavingsAccount();
            while (choice != 3) {
                System.out.println("Choose the transaction type you want");
                System.out.println("1- Open Account\n2- Show AccountDetails\n3- Remove Account\n4- Deposit money\n5- Withdraw money\n6- Show last 5 transactions\n7- Exit");
                int select = sc.nextInt();
                switch (select) {
                    case 1 -> {
                        System.out.println("Enter your unique ID");
                        int id = sc.nextInt();
                        System.out.println("Enter your name");
                        String name = sc.next();
                        System.out.println("Enter your age");
                        int age = sc.nextInt();
                        if (age < 18) {
                            System.out.println("Age is not valid");
                            return;
                        }
                        System.out.println("Enter your address");
                        String address = sc.next();
                        System.out.println("Enter your mobile no");
                        long mobileNo = sc.nextLong();
                        System.out.println("Enter the opening balance");
                        double openingBalance = sc.nextDouble();
                        sa.addCustomer(id, name, age, address, mobileNo);
                        sa.addAccount(openingBalance, id);
                    }
                    case 2 -> {
                        System.out.println("Enter your accountNo to show details");
                        long no = sc.nextLong();
                        sa.showAccountDetails(no);
                    }
                    case 3 -> {
                        System.out.println("Enter the accountNo to remove account");
                        long no = sc.nextLong();
                        sa.deleteAccount(no);
                    }
                    case 4 -> {
                        System.out.println("Enter the accountNo");
                        long no = sc.nextLong();
                        System.out.println("Enter the amount to deposit");
                        double amount = sc.nextDouble();
                        sa.deposit(no, amount);
                    }
                    case 5 -> {
                        System.out.println("Enter the accountNo");
                        long no = sc.nextLong();
                        System.out.println("Enter the amount to withdraw");
                        double amount = sc.nextDouble();
                        sa.withdraw(no, amount);
                    }
                    case 6 -> {
                        System.out.println("Enter your account no");
                        long no = sc.nextLong();
                        sa.showLastTransactions(no, SavingsAccount.accountType);
                    }
                    case 7 ->choice=3;
                }
                }
            }
            if (choice == 2) {
                PayAccount pa = new PayAccount();
                while (choice != 3) {
                    System.out.println("Choose the transaction type you want");
                    System.out.println("1- Open Account\n2- Show AccountDetails\n3- Remove Account\n4- Deposit money\n5- Show last 5 transactions\n6-Pay to Account\n7- Exit");
                    int select = sc.nextInt();
                    switch (select) {
                        case 1 -> {
                            System.out.println("Enter your unique ID");
                            int id = sc.nextInt();
                            System.out.println("Enter your name");
                            String name = sc.next();
                            System.out.println("Enter your age");
                            int age = sc.nextInt();
                            if (age < 18) {
                                System.out.println("Age is not valid");
                                return;
                            }
                            System.out.println("Enter your address");
                            String address = sc.next();
                            System.out.println("Enter your mobile no");
                            long mobileNo = sc.nextLong();
                            System.out.println("Enter the opening balance");
                            double openingBalance = sc.nextDouble();
                            pa.addCustomer(id, name, age, address, mobileNo);
                            pa.addAccount(openingBalance, id);
                        }
                        case 2 -> {
                            System.out.println("Enter your accountNo to show details");
                            long no = sc.nextLong();
                            pa.showAccountDetails(no);
                        }
                        case 3 -> {
                            System.out.println("Enter the accountNo to remove account");
                            long no = sc.nextLong();
                            pa.deleteAccount(no);
                        }
                        case 4 -> {
                            System.out.println("Enter the accountNo");
                            long no = sc.nextLong();
                            System.out.println("Enter the amount to deposit");
                            double amount = sc.nextDouble();
                            pa.deposit(no, amount);
                        }
                        case 5 -> {
                            System.out.println("Enter your account no");
                            long no = sc.nextLong();
                            pa.showLastTransactions(no, PayAccount.accountType);
                        }
                        case 6->{
                            System.out.println("Enter your account no");
                            long fromAccount=sc.nextLong();
                            System.out.println("Enter the amount to transfer");
                            double amount = sc.nextDouble();
                            System.out.println("Enter the type of account\n1-Savings\n2-Pay Account");
                            int sel=sc.nextInt();
                            System.out.println("Enter the accountNo of person to pay");
                            long toAccount=sc.nextLong();
                            if(sel==1)
                                pa.payToAccount(fromAccount,amount,toAccount,SavingsAccount.accountType);
                            else
                                pa.payToAccount(fromAccount,amount,toAccount,PayAccount.accountType);
                        }
                        case 7 -> choice=3;
                    }
                }
            }

    }
}
