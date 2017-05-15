package bank;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JTabbedPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.border.TitledBorder;
import javax.swing.JList;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class BankServerGUI {

	private JFrame frmBankserver;
	private JTextField txtServerID;
	public static JTextPane NodeInfoPane;
	public static  JList listNode;
	public static  JTextPane clientMsg;
	public static  JTextPane peerMsg;
	
	
	//public static ArrayList<BankAccount> accArr = new ArrayList<BankAccount>();
	String serverID;
	int clientPort = 5555;
	int coordinationPort=6666;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BankServerGUI window = new BankServerGUI();
					window.frmBankserver.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public BankServerGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		//initialize accounts with format(accountNum,accountName,password,initial balance)
		/*BankAccount acc1 = new BankAccount(001,"Molly","123456",1000);
		BankAccount acc2 = new BankAccount(002,"Jason","123456",1200);
		BankAccount acc3 = new BankAccount(003,"Amy","123456",1500);
		BankAccount acc4 = new BankAccount(004,"Bob","123456",2000);
		accArr.add(acc1);
		accArr.add(acc2);
		accArr.add(acc3);
		accArr.add(acc4);*/
		
		frmBankserver = new JFrame();
		frmBankserver.setSize(650, 500);
		frmBankserver.setTitle("BankServer");
		frmBankserver.getContentPane().setLayout(null);
		
		JPanel messagePanel = new JPanel();
		messagePanel.setBorder(new TitledBorder(null, "Clients Messages", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		messagePanel.setBounds(148, 224, 199, 153);
		frmBankserver.getContentPane().add(messagePanel);
		messagePanel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 18, 179, 125);
		messagePanel.add(scrollPane);
		
		clientMsg = new JTextPane();
		scrollPane.setViewportView(clientMsg);
		
		JPanel logPanel = new JPanel();
		logPanel.setLayout(null);
		logPanel.setBorder(new TitledBorder(null, "Peers Message", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		logPanel.setBounds(406, 224, 218, 153);
		frmBankserver.getContentPane().add(logPanel);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(6, 17, 206, 125);
		logPanel.add(scrollPane_1);
		
		peerMsg = new JTextPane();
		scrollPane_1.setViewportView(peerMsg);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Node details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(148, 77, 479, 123);
		frmBankserver.getContentPane().add(panel);
		panel.setLayout(null);
		
		NodeInfoPane = new JTextPane();
		NodeInfoPane.setBounds(6, 17, 463, 99);
		panel.add(NodeInfoPane);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Partner Nodes", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 84, 116, 351);
		frmBankserver.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(6, 17, 100, 324);
		panel_1.add(scrollPane_2);
		
		listNode = new JList();
		scrollPane_2.setViewportView(listNode);
		
		JLabel lblBankSystemServer = new JLabel("Bank System Server Monitor");
		lblBankSystemServer.setHorizontalAlignment(SwingConstants.CENTER);
		lblBankSystemServer.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblBankSystemServer.setBounds(148, 10, 413, 31);
		frmBankserver.getContentPane().add(lblBankSystemServer);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(169, 388, 390, 47);
		frmBankserver.getContentPane().add(panel_2);
		panel_2.setLayout(null);
		
		JButton btnJoin = new JButton("Join ");
		btnJoin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnJoin.setBounds(6, 17, 93, 23);
		panel_2.add(btnJoin);
		
		JButton btnQuit = new JButton("quit");
		btnQuit.setBounds(151, 17, 93, 23);
		panel_2.add(btnQuit);
		
		JButton btnExit = new JButton("exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setBounds(291, 17, 93, 23);
		panel_2.add(btnExit);
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		
		txtServerID = new JTextField();
		txtServerID.setBounds(10, 17, 116, 21);
		frmBankserver.getContentPane().add(txtServerID);
		txtServerID.setColumns(10);
		
		// the button is to start a server with requested ID
		JButton btnServerID = new JButton("Set ServerID");
		btnServerID.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String serverID = txtServerID.getText();
				frmBankserver.setTitle("BankServer"+serverID);
				NodeInfoPane.setText("server "+serverID + " has started");
				ServerStart();
							
			}
		});
		btnServerID.setFont(new Font("SimSun", Font.PLAIN, 12));
		btnServerID.setHorizontalAlignment(SwingConstants.LEFT);
		btnServerID.setBounds(10, 53, 116, 21);
		frmBankserver.getContentPane().add(btnServerID);
		

	}
	
	//create listener thread to listen client connection request
	public void ServerStart(){
		
		/*try {
	            ClientListener clientListener = new ClientListener();
	            clientListener.start();
	    } catch (Exception e) {
	        System.out.println("Error:" + e);
	    }*/
	}
}
