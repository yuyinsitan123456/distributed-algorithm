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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import com.google.gson.Gson;
import paxosMessage.Message;
import paxosMessage.MessagePacket;
import paxosUtils.RoleType;


@SuppressWarnings("serial")
public class ClientGUI extends JFrame {
	
	public  BufferedReader in;
    public  BufferedWriter out;
    public  BankClient client;

	static JTextPane resultPane;
	String result = "";
	String accName= "";
	


	/**
	 * Create the frame.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public ClientGUI(String host, int port) {
		try {
			client = new BankClient(host,port);
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		in = client.getIn();
		out = client.getOut();
		setTitle("Client");
		//JFrame mainframe = new JFrame();
		setFont(new Font("Arial", Font.PLAIN, 16));
		setBounds(100, 100, 877, 388);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 21, 137, 254);
		panel.add(panel_1);
		panel_1.setLayout(null);
		
		// deposit
		JButton btnDeposit = new JButton("Deposit");
		btnDeposit.setFont(new Font("Arial", Font.PLAIN, 14));
		btnDeposit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                setVisible(false);
                DepositGUI depositFrame = new DepositGUI(host,port);
                depositFrame.setVisible(true);
			}
		});
		btnDeposit.setBounds(14, 21, 101, 35);
		panel_1.add(btnDeposit);
		
		//withdraw
		JButton btnWithdraw = new JButton("Withdraw");
		btnWithdraw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
                WithdrawGUI withdrawFrame = new WithdrawGUI(host,port);
                withdrawFrame.setVisible(true);
			}
		});
		btnWithdraw.setFont(new Font("Arial", Font.PLAIN, 14));
		btnWithdraw.setBounds(14, 77, 101, 35);
		panel_1.add(btnWithdraw);
		
		//check balance
		JButton btnBanlace = new JButton("Banlace");
		btnBanlace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ClientGUI mainFrame = new ClientGUI(host,port);
					Message message = new Message("BankMessage",
							(new Gson().toJson(new BankMessage("jzzzz", "balance", 0))));
					mainFrame.sendMessage(new Gson().toJson(new MessagePacket(message, RoleType.CLIENT))+"\n");
					mainFrame.getMessage();
					setVisible(false);
					mainFrame.setVisible(true);
					
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		
			}
		});
		btnBanlace.setFont(new Font("Arial", Font.PLAIN, 14));
		btnBanlace.setBounds(14, 138, 101, 35);
		panel_1.add(btnBanlace);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setFont(new Font("Arial", Font.PLAIN, 14));
		btnExit.setBounds(17, 194, 101, 35);
		panel_1.add(btnExit);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(155, 21, 675, 275);
		panel.add(scrollPane);
		scrollPane.setBorder(BorderFactory.createTitledBorder("Results"));
		
		resultPane = new JTextPane();
		resultPane.setFont(new Font("Arial", Font.PLAIN, 16));
		scrollPane.setViewportView(resultPane);

	}
	//send the message to server
	public void sendMessage( String msg) throws UnknownHostException, IOException {
		out.write(msg);
		out.flush();
	}
	
	//get the message
	public String getMessage() throws IOException{
		String message="";
		message = in.readLine();
		resultPane.setText(message);
		return message;
	}
}
