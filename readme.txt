COMP90020_2017_SM1 Distributed Algorithms source code
Team member:
732355 Zhen Jia
732329 Jinghan Liang
776991 Kun Yu
756344 Hongzhi Zhang
===========================================================
Projece Topic:  implementation  of multi-paxos Algorithms

To run the system, firstly, creat the conf.json file to configure the system, the test.java will read this conf.json file to initialize the server.
Then start the connectGUI.java and input the hostname&port to connect the server.
finally, client can do some operations to the server and the server will run multi-paxos algorithm with every node to process the request.

The source code has serveral packages:
1.The bankClient package is the client of the system. Run ConnectGUI.java to start connecting the server.
2.The server need to be started by running the test.java in paxosBank package.
3.paxosBankAccount package contains a state machine playing a role of account to store info.
4.paxosMessage define the types of message used to communicate among nodes.
5.paxosRole package defines the three role of propocer, accepter and learner in the Paxos algorithm.
6.paxosServer handle the connections for sending/receiving messages.
7.paxosUtils package provides tools classes for the system.