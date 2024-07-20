import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

class BMIApp extends JFrame implements ActionListener
{

    private static final long serialVersionUID = 1L;
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String JDBC_URL = "jdbc:mysql://localhost:3306/BMI";
    static final String USERNAME = "root";
    static final String PASSWORD = "root";

    private JPanel contentPane;
    private JLabel fnameL, lnameL, ageL, weightL, heightL, result,result1;
    private JTextField fnameF, lnameF, ageF, weightF, heightF;
    private JButton calculate, reset;

    public BMIApp() {
        getContentPane().setBackground(new Color(192, 192, 192));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 400);
        setLocationRelativeTo(null);
        setTitle("BMI Calculator");
        getContentPane().setLayout(null);

        fnameL = new JLabel("Enter FirstName ");
        fnameL.setBounds(50, 30, 100, 30); // left-top width-height
        fnameL.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        fnameF = new JTextField(20);
        fnameF.setBounds(200, 30, 140, 30);

        lnameL = new JLabel("Enter LastName ");
        lnameL.setBounds(50, 70, 100, 30);
        lnameL.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        lnameF = new JTextField(20);
        lnameF.setBounds(200, 70, 140, 30);

        ageL = new JLabel("Enter Age");
        ageL.setBounds(50, 110, 100, 30);
        ageL.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        ageF = new JTextField(20);
        ageF.setBounds(200, 110, 140, 30);

        JLabel weightT = new JLabel("Enter weight");
        weightT.setBounds(50, 150, 100, 30);
        weightT.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        weightF = new JTextField(20);
        weightF.setBounds(200, 150, 140, 30);

        JLabel heightT = new JLabel("Enter Height(cm):");
        heightT.setBounds(50, 190, 120, 30);
        heightT.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        heightF = new JTextField(20);
        heightF.setBounds(200, 190, 140, 30);

        getContentPane().add(fnameL);
        getContentPane().add(fnameF);
        getContentPane().add(lnameL);
        getContentPane().add(lnameF);
        getContentPane().add(ageL);
        getContentPane().add(ageF);
        getContentPane().add(weightT);
        getContentPane().add(weightF);
        getContentPane().add(heightT);
        getContentPane().add(heightF);

        result = new JLabel();
        result.setBounds(50, 310, 200, 30);
        getContentPane().add(result);

        result1 = new JLabel();
        result1.setBounds(50, 350, 200, 30);
        getContentPane().add(result1);

        calculate = new JButton("Calculate");
        calculate.setBackground(new Color(51, 204, 255));
        calculate.addActionListener(this);

        calculate.setFont(new Font("MS UI Gothic", Font.BOLD, 14));
        calculate.setBounds(50, 280, 120, 30);
        getContentPane().add(calculate);

        reset = new JButton("Reset");
        reset.setBackground(new Color(51, 204, 255));
        reset.addActionListener(this);

        reset.setFont(new Font("MS UI Gothic", Font.BOLD, 14));
        reset.setBounds(200, 280, 120, 30);
        getContentPane().add(reset);

        setSize(400, 500);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == calculate) {
            String fname = fnameF.getText();
            String lname = lnameF.getText();
            int age = Integer.parseInt(ageF.getText());
            float weight = Float.parseFloat(weightF.getText());
            float height = Float.parseFloat(heightF.getText()) / 100; // Convert cm to meters

            double bmi = weight / (height * height);
            result.setText("Body Mass Index is: " + bmi);

            if(bmi<18.5)
            {
                result1.setText("You are underweight!!");
            }else if(bmi>18.5 && bmi<24.9)
            {
                result1.setText("You are Healthy!!");
            }else if(bmi>24.9 && bmi<29.9)
            {
                result1.setText("You are overweight!!");
            }

            JOptionPane.showMessageDialog(calculate, "Calculated Successfully");

            try {
                Class.forName(JDBC_DRIVER);
                Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

                Statement stmt = conn.createStatement();
                // String sql = "CREATE DATABASE IF NOT EXISTS BMI";
                // stmt.executeUpdate(sql);
                String sql = "CREATE TABLE IF NOT EXISTS visiter " + "(fname VARCHAR(20),"
                        + "lname VARCHAR(20)," + "age INTEGER," + "weight FLOAT," + "height FLOAT)";
                stmt.executeUpdate(sql);

                String sql1 = "INSERT INTO visiter (fname, lname, age, weight, height) VALUES (?,?,?,?,?)";
                PreparedStatement pstmt = conn.prepareStatement(sql1);
                pstmt.setString(1, fname);
                pstmt.setString(2, lname);
                pstmt.setInt(3, age);
                pstmt.setFloat(4, weight);
                pstmt.setFloat(5, height);
                pstmt.executeUpdate();

                JOptionPane.showMessageDialog(calculate, "Data successfully inserted");
            } catch (SQLException | ClassNotFoundException sqle)
            {
                JOptionPane.showMessageDialog(calculate, "Database connectivity declined", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }else if (e.getSource() == reset) {
            fnameF.setText("");
            lnameF.setText("");
            ageF.setText("");
            weightF.setText("");
            heightF.setText("");
            result.setText("");
        }
    }



    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    BMIApp frame = new BMIApp();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}