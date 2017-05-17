/*COMP90020 project assessment
 * 2017
 * Group member :
 * 732355 
 * 732329
 * 776991
 * 756344
 * */
package bankClient;

import java.awt.BorderLayout;
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

import paxosMessage.Message;
import paxosMessage.MessagePacket;
import paxosUtils.RoleType;
@SuppressWarnings("serial")
public class WithdrawGUI extends JFrame {

	@SuppressWarnings("unused")
	private JPanel contentPane;
	private JTextField withdrawAmount;
	String result = "";



	/**
	 * Create the frame.
	 */
	public WithdrawGUI(String host, int port) {
		ClientGUI mainFrame = new ClientGUI(host,port);
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
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
					int withdrawNum = Integer.parseInt(withdrawAmount.getText());
					Gson gson = new Gson();
					Message message = new Message("BankMessage",
							gson.toJson(new BankMessage("jzzzz", "withdraw", withdrawNum)));
					//Message message = new Message("BankMessage",
					//		(new Gson().toJson(new BankMessage("accountName", "withdraw", withdrawNum))));
					mainFrame.sendMessage(new Gson().toJson(new MessagePacket(message, RoleType.CLIENT))+"\n");
					mainFrame.getMessage();
					JOptionPane.showMessageDialog(mainFrame, "Withdraw request has been sent to server!");
					setVisible(false);
					mainFrame.setVisible(true);

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
	
}
