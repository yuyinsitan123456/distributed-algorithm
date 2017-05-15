package bank;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;

import com.google.gson.Gson;


@SuppressWarnings("serial")
public class ClientGUI extends JFrame {
	
	public static BufferedReader in;
    public static BufferedWriter out;
    public static BankClient client;

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
		try{
			Socket clientSocket = new Socket("localhost", 33333);
			client = new BankClient("JZ",clientSocket);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
			out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
			//create a thread to receive the server's message
			Thread receiveThread = new Thread(new MessageReceiveThread(clientSocket,in));
			receiveThread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

		//JFrame mainframe = new JFrame();
		setFont(new Font("Arial", Font.PLAIN, 16));
		setBounds(100, 100, 453, 336);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(19, 43, 118, 232);
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
		btnDeposit.setBounds(8, 6, 101, 35);
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
		btnWithdraw.setBounds(11, 51, 101, 35);
		panel_1.add(btnWithdraw);
		
		//transfer
		JButton btnTransfer = new JButton("Transfer");
		btnTransfer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		btnTransfer.setFont(new Font("Arial", Font.PLAIN, 14));
		btnTransfer.setBounds(10, 95, 101, 35);
		panel_1.add(btnTransfer);
		
		//check balance
		JButton btnBanlace = new JButton("Banlace");
		btnBanlace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					sendMessage(out,(new Gson().toJson(new BankMessage("accountName", "balance", 0))));
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				/*String message = "4#balance#"+accName;
				try {
					out.writeUTF(message);
					result = in.readUTF();
					resultPane.setText("current balance is: "+ result+ "\n");
				} catch (Exception e1) {
					e1.printStackTrace();
				}*/			
			}
		});
		btnBanlace.setFont(new Font("Arial", Font.PLAIN, 14));
		btnBanlace.setBounds(10, 139, 101, 35);
		panel_1.add(btnBanlace);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setFont(new Font("Arial", Font.PLAIN, 14));
		btnExit.setBounds(11, 179, 101, 35);
		panel_1.add(btnExit);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(155, 43, 231, 222);
		panel.add(scrollPane);
		
		resultPane = new JTextPane();
		resultPane.setFont(new Font("Arial", Font.PLAIN, 16));
		scrollPane.setViewportView(resultPane);
		resultPane.setText(result);
		
		JLabel lblResults = new JLabel("Results:");
		lblResults.setFont(new Font("Arial", Font.PLAIN, 18));
		lblResults.setBounds(155, 10, 72, 18);
		panel.add(lblResults);
	}
//send the message to server
	public static void sendMessage(BufferedWriter out, String msg) throws UnknownHostException, IOException {
		out.write(msg);
		out.flush();
	}
	
//show the messages from server on textPane
	public static void ShowMessage(String message) {
		// TODO Auto-generated method stub
		resultPane.setText(message);
	}
}
