package bank;

import static Client.LoginGUI.out;
import static Client.LoginGUI.in;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

public class DepositGUI extends JFrame {

	private JPanel contentPane;

	//private JFrame depositFrame;
	private JTextField textField;
	private JTextField depositAmount;
	ClientGUI mainFrame = new ClientGUI();
	
	String result = "";
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DepositGUI depositFrame = new DepositGUI();
					depositFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DepositGUI() {
		
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 434, 1);
		panel.setLayout(null);
		getContentPane().add(panel);
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(86, 91, 231, 53);
		panel.add(textField);
		
		
		depositAmount = new JTextField();
		depositAmount.setFont(new Font("Arial", Font.PLAIN, 14));
		depositAmount.setColumns(10);
		depositAmount.setBounds(87, 86, 231, 53);
		getContentPane().add(depositAmount);
		
		JButton btnEnter = new JButton("Enter");
		btnEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				depositOperation(depositAmount.getText());
				if (!result.equals(null)) {
					JOptionPane.showMessageDialog(mainFrame, "Deposite Done!");
					setVisible(false);
					mainFrame.setVisible(true);
					mainFrame.result = result;
								
				}
			}
		});
		btnEnter.setFont(new Font("Arial", Font.PLAIN, 14));
		btnEnter.setBounds(104, 166, 77, 43);
		getContentPane().add(btnEnter);
		
		JLabel label_1 = new JLabel("Please enter the amount:");
		label_1.setFont(new Font("Arial", Font.PLAIN, 16));
		label_1.setBounds(89, 31, 229, 35);
		getContentPane().add(label_1);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);  
                mainFrame.setVisible(true);
			}
		});
		btnBack.setFont(new Font("Arial", Font.PLAIN, 14));
		btnBack.setBounds(216, 165, 83, 44);
		getContentPane().add(btnBack);
	}
	
	//deposit operation: sending deposit amount to server and receive results
	
	public void depositOperation(String depositAmount){
		String message = "1#deposit#"+depositAmount;
		try {
			out.writeUTF(message);
			
			result = in.readUTF();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
