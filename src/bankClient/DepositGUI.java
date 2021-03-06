
/*COMP90020 project assessment
 * 2017
 * Group member :
 * 732355 
 * 732329
 * 776991
 * 756344
 * */
package bankClient;



import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.google.gson.Gson;
import paxosMessage.Message;
import paxosMessage.MessagePacket;
import paxosUtils.RoleType;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class DepositGUI extends JFrame {

	@SuppressWarnings("unused")
	private JPanel contentPane;

	//private JFrame depositFrame;
	private JTextField textField;
	private JTextField depositAmount;


	
	String result = "";


	/**
	 * Create the frame.
	 */
	public DepositGUI(String host, int port) {
		ClientGUI mainFrame = new ClientGUI(host,port);
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		setLocationRelativeTo(null);
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
				try {
					int depositNum = Integer.parseInt(depositAmount.getText());
					Gson gson = new Gson();
					Message message = new Message("BankMessage",
							gson.toJson(new BankMessage("jzzzz", "deposit", depositNum)));
					mainFrame.sendMessage(gson.toJson(new MessagePacket(message, RoleType.CLIENT))+"\n");
					mainFrame.getMessage();
					JOptionPane.showMessageDialog(mainFrame, "request has been sent to server!");
					setVisible(false);
					mainFrame.setVisible(true);

				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
	


}
