/*COMP90020 project assessment
 * 2017
 * Group member :
 * 732355 
 * 732329
 * 776991
 * 756344
 * */
package bankClient;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;

public class ConnectGUI {

	private JFrame frmLogin;
	private JTextField Host;
	private JTextField Port;
	JLabel lblErrorInfo = new JLabel("");
	


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConnectGUI window = new ConnectGUI();
					window.frmLogin.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ConnectGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLogin = new JFrame();
		frmLogin.setTitle("Login");
		frmLogin.setBounds(100, 100, 450, 300);
		frmLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frmLogin.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblAccountName = new JLabel("Host:");
		lblAccountName.setHorizontalAlignment(SwingConstants.CENTER);
		lblAccountName.setFont(new Font("Arial", Font.PLAIN, 20));
		lblAccountName.setBounds(36, 41, 120, 22);
		panel.add(lblAccountName);
		
		Host = new JTextField();
		Host.setBounds(156, 34, 164, 31);
		panel.add(Host);
		Host.setColumns(10);
		
		JLabel lblPassword = new JLabel("Port:");
		lblPassword.setHorizontalAlignment(SwingConstants.CENTER);
		lblPassword.setFont(new Font("Arial", Font.PLAIN, 20));
		lblPassword.setBounds(45, 84, 110, 31);
		panel.add(lblPassword);
		
		Port = new JTextField();
		Port.setBounds(156, 85, 164, 31);
		panel.add(Port);
		Port.setColumns(10);
		
		JButton btnLogin = new JButton("Connect");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				connectToServer(Host.getText(),Port.getText());
			}

		});
		btnLogin.setFont(new Font("Arial", Font.PLAIN, 18));
		btnLogin.setBounds(159, 162, 145, 33);
		panel.add(btnLogin);
		
		
		lblErrorInfo.setFont(new Font("Arial", Font.BOLD, 18));
		lblErrorInfo.setBounds(133, 203, 203, 22);
		panel.add(lblErrorInfo);
	}

	private void connectToServer(String accName, String psd) {
		frmLogin.setVisible(false);
		ClientGUI mainFrame = new ClientGUI(Host.getText(),Integer.parseInt(Port.getText()));
		mainFrame.setVisible(true);



	}
}
