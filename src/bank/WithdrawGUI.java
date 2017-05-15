package bank;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.google.gson.Gson;
import static bank.ClientGUI.out;
@SuppressWarnings("serial")
public class WithdrawGUI extends JFrame {

	@SuppressWarnings("unused")
	private JPanel contentPane;
	private JTextField withdrawAmount;
	String result = "";
	ClientGUI mainFrame = new ClientGUI();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WithdrawGUI withdrawframe = new WithdrawGUI();
					withdrawframe.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public WithdrawGUI() {
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		withdrawAmount = new JTextField();
		withdrawAmount.setBounds(86, 91, 231, 53);
		panel.add(withdrawAmount);
		withdrawAmount.setColumns(10);
		
		JButton btnEnter = new JButton("Enter");
		btnEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					withdrawOperation(Integer.parseInt(withdrawAmount.getText()));
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				if (!result.equals(null)) {
					JOptionPane.showMessageDialog(mainFrame, "Withdraw Done!");
					setVisible(false);
					mainFrame.setVisible(true);
				}
			}
		});
		btnEnter.setFont(new Font("Arial", Font.PLAIN, 14));
		btnEnter.setBounds(103, 172, 93, 42);
		panel.add(btnEnter);
		
		JLabel lblPleaseEnterThe = new JLabel("Please enter the amount:");
		lblPleaseEnterThe.setFont(new Font("Arial", Font.PLAIN, 20));
		lblPleaseEnterThe.setBounds(88, 33, 229, 35);
		panel.add(lblPleaseEnterThe);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);  
                mainFrame.setVisible(true);
			}
		});
		btnBack.setFont(new Font("Arial", Font.PLAIN, 14));
		btnBack.setBounds(217, 172, 93, 42);
		panel.add(btnBack);
	}
	
	//withdraw operation: sending withdraw amount to server and receive results
	public void withdrawOperation(int withdrawAmount) throws UnknownHostException, IOException{
		ClientGUI.sendMessage(out,(new Gson().toJson(new BankMessage("accountName", "withdraw", withdrawAmount))));
		/*String message = "2#withdraw#"+withdrawAmount;
		try {
			out.writeUTF(message);
			//result = in.readUTF();
			
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
	}
}
