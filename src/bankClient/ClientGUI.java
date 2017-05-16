package bankClient;

import java.awt.BorderLayout;
import java.awt.EventQueue;
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
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientGUI mainframe = new ClientGUI();
					mainframe.setVisible(true);

					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public ClientGUI() {
		try {
			client = new BankClient();
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		in = client.getIn();
		out = client.getOut();
		setTitle("Client");
		//JFrame mainframe = new JFrame();
		setFont(new Font("Arial", Font.PLAIN, 16));
		setBounds(100, 100, 455, 359);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
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
                DepositGUI depositFrame = new DepositGUI();
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
                WithdrawGUI withdrawFrame = new WithdrawGUI();
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
					Message message = new Message("BankMessage",
							(new Gson().toJson(new BankMessage("accountName", "balance", 0))));
					sendMessage(new Gson().toJson((new MessagePacket(message, RoleType.CLIENT))+"\n"));
					
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				/*try {

					result = in.readLine();
					resultPane.setText("current balance is: "+ result+ "\n");
				} catch (Exception e1) {
					e1.printStackTrace();
				}*/		
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
		scrollPane.setBounds(155, 21, 253, 244);
		panel.add(scrollPane);
		scrollPane.setBorder(BorderFactory.createTitledBorder("Results"));
		
		resultPane = new JTextPane();
		resultPane.setFont(new Font("Arial", Font.PLAIN, 16));
		scrollPane.setViewportView(resultPane);
		//resultPane.setText("123");
		//resultPane.setText("456");
	}
//send the message to server
	public void sendMessage( String msg) throws UnknownHostException, IOException {
		out.write(msg);
		out.flush();
	}
	
//show the messages from server on textPane
	public static void ShowMessage(ArrayList<String> messageList) {
		
		resultPane.setText(messageList.toString());
	}
//get the message
	public String getMessage() throws IOException{
		String message="";
		message = in.readLine();
		resultPane.setText(message);

		return message;
	}
}
