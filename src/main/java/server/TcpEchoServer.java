package server;

import org.jetbrains.annotations.Contract;
import services.UserActivityService;
import services.UserService;
import services.impl.UserActivityServiceImpl;
import services.impl.UserServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class TcpEchoServer {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private final int PORT = 3345;
    private String user;
    private long userActivityId = 0;

    private UserActivityService userActivityService = new UserActivityServiceImpl();
    private UserService userService = new UserServiceImpl();

    public TcpEchoServer() {
        try {
            setServer(new ServerSocket(PORT));
        } catch (Exception ex) {
            System.err.println("Could not listen on port: " + PORT + ".");
        }
    }

    public void writeError() {
        try {
            if (clientSocket != null) {
                OutputStream out = clientSocket.getOutputStream();
                out.write(("\r\n\r\nTimeout expired.\r\n").getBytes());
            }
        } catch (IOException ignored) {
        }
    }

    public boolean disconnect() {
        try {
            OutputStream out = clientSocket.getOutputStream();
            out.write(("\r\nConnection completed.").getBytes());
            if (userActivityId != 0) {
                String time = new Date().toString();
                userActivityService.disconnected(userActivityId, time);
            }
            serverSocket.close();
            clientSocket.close();
            System.out.println("TCP Echo Server Stopped on port " + PORT + ".");
            System.exit(0);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public void initialize() {
        System.out.println("TCP Echo Server Started on port " + PORT + ".\nWaiting for connection.....");

    }

    public boolean connect() {
        try {
            clientSocket = serverSocket.accept();
            System.out.println("Client Connection successful.\nWaiting for input.....");
            return true;
        } catch (IOException e) {
            disconnect();
            return false;
        }
    }

    public boolean send() throws IOException {
        OutputStream out = clientSocket.getOutputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out.write(("\r\n" + user + ": ").getBytes());
        String command;
        command = in.readLine();
        if (command.equals("show running-config")) {
            out.write(("\r\nBuilding configuration...\r\n").getBytes());
            /**/
            out.write(("\r\ntrack 100 ip route 217.168.38.254 255.255.255.255 reachability\r\n" +
                    " ip vrf VPN-INT2\r\n" +
                    " delay up 20\r\n" +
                    "ip subnet-zero\r\n" +
                    "no ip source-route\r\n" +
                    "ip routing\r\n" +
                    "ip domain-lookup source-interface Loopback1\r\n" +
                    "ip domain-name cablecom.net\r\n" +
                    "ip host tftp 62.2.21.8\r\n" +
                    "ip name-server 62.2.22.61\r\n" +
                    "ip name-server 62.2.22.62\r\n" +
                    "ip name-server 62.2.32.5\r\n" +
                    "ip vrf VPN-INT1\r\n" +
                    " rd 65300:1\r\n" +
                    "ip vrf VPN-INT2\r\n" +
                    " rd 65300:2\r\n" +
                    "no ip igmp snooping vlan 404\r\n" +
                    "no errdisable detect cause gbic-invalid\r\n" +
                    "errdisable recovery cause link-flap\r\n" +
                    "!\r\n" +
                    "spanning-tree mode rapid-pvst\r\n" +
                    "spanning-tree extend system-id\r\n" +
                    "no spanning-tree vlan 1168-1169,1175\r\n" +
                    "vlan internal allocation policy ascending\r\n" +
                    "vlan 404\r\n" +
                    " name EoMPLS_2775\r\n" +
                    "vlan 1072\r\n" +
                    " name VPN-MGT_CE_10.157.216.152_/30\r\n" +
                    "vlan 1086\r\n" +
                    " name VPN-MGT_CE_10.157.228.196_/30\r\n" +
                    "vlan 1168\r\n" +
                    " name VPN-INT1_213.193.70.168_/30\r\n" +
                    "vlan 1169\r\n" +
                    " name VPN-INT2_213.193.70.172_/30\r\n" +
                    "vlan 1175\r\n" +
                    " name jrq:21244_10.194.42.236_/30\r\n" +
                    "ip tftp source-interface Loopback1\r\n" +
                    "class-map match-all cos-map-...-vlan\r\n" +
                    " match vlan  1168\r\n" +
                    "class-map match-all cos-map-...-vlan\r\n" +
                    " match vlan  1169\r\n" +
                    "class-map match-all cos-map-...-vlan\r\n" +
                    " match vlan  1175\r\n" +
                    "class-map match-any cos-map-...\r\n" +
                    " match qos-group 4\r\n" +
                    "class-map match-all cos-map-...\r\n" +
                    " match qos-group 5\r\n" +
                    "class-map match-all cos-map-...\r\n" +
                    " match qos-group 2\r\n" +
                    "class-map match-all cos-map-...\r\n" +
                    " match qos-group 6\r\n" +
                    "policy-map cos-po2-cos-...\r\n" +
                    " class cos-map-pre-nm-1\r\n" +
                    "    bandwidth remaining percent 1\r\n" +
                    " class class-default\r\n" +
                    "    bandwidth remaining percent 99\r\n" +
                    "policy-map cos-pol-vlan-1\r\n" +
                    " class cos-map-...-vlan\r\n" +
                    "    shape average 106000000\r\n" +
                    "  service-policy cos-po2-cos-...\r\n" +
                    " class cos-map-...-vlan\r\n" +
                    "    shape average 10600000\r\n" +
                    " class cos-map-...-vlan\r\n" +
                    "    shape average 106000000\r\n" +
                    " class class-default\r\n" +
                    "    bandwidth 200000\r\n" +
                    "interface Loopback1\r\n" +
                    " description Management Loopback Interface\r\n" +
                    " ip address 10.230.20.24 255.255.255.255\r\n" +
                    "interface GigabitEthernet0\r\n" +
                    " no ip address\r\n" +
                    " shutdown\r\n" +
                    " negotiation auto\r\n" +
                    "interface GigabitEthernet0/1\r\n" +
                    " description jrq:21244; MLE VPN; ...: 100000kbps; ...; 2012-08-15; ...\r\n" +
                    " port-type nni\r\n" +
                    " no switchport\r\n" +
                    " ip address 192.168.200.4 255.255.255.0\r\n" +
                    "interface GigabitEthernet0/2\r\n" +
                    " description VPN-INT1; 10000kbps; ...; 2012-08-15; ...\r\n" +
                    " port-type nni\r\n" +
                    " no switchport\r\n" +
                    " ip vrf forwarding VPN-INT1\r\n" +
                    " ip address 46.140.131.41 255.255.255.252\r\n" +
                    "interface GigabitEthernet0/3\r\n" +
                    " description VPN-INT2; 100000kbps; SAP@320.0171007; 2012-08-15; ...\r\n" +
                    " port-type nni\r\n" +
                    " no switchport\r\n" +
                    " ip vrf forwarding VPN-INT2\r\n" +
                    " ip address 46.140.128.34 255.255.255.224\r\n" +
                    " standby 100 ip 46.140.128.33\r\n" +
                    " standby 100 priority 150\r\n" +
                    " standby 100 preempt\r\n" +
                    " standby 100 track 100 decrement 70\r\n" +
                    "interface GigabitEthernet0/4\r\n" +
                    " description swaBIL008; swaBIL009; VLAN1072; VPN-MGT_CE; 2012-08-28; ...\r\n" +
                    " port-type nni\r\n" +
                    " switchport access vlan 1072\r\n" +
                    " no cdp enable\r\n" +
                    "interface GigabitEthernet0/5\r\n" +
                    " description EoMPLS_2775; SAP@354.0191248; 100Mbps; 2013-04-19; ...\r\n" +
                    " port-type nni\r\n" +
                    " switchport trunk allowed vlan none\r\n" +
                    " switchport mode trunk\r\n" +
                    " mtu 9800\r\n" +
                    " load-interval 30\r\n" +
                    " service instance 1 ethernet\r\n" +
                    "  encapsulation default\r\n" +
                    "  l2protocol tunnel cdp stp vtp udld\r\n" +
                    "  bridge-domain 404\r\n" +
                    "interface GigabitEthernet0/6\r\n" +
                    " description swaBIL010; VLAN1086; VPN-MGT_CE; 2014-08-07; ...\r\n" +
                    " port-type nni\r\n" +
                    " switchport access vlan 1086\r\n" +
                    " no cdp enable\r\n" +
                    "interface GigabitEthernet0/7\r\n" +
                    " port-type nni\r\n" +
                    " shutdown\r\n" +
                    "interface GigabitEthernet0/8\r\n" +
                    " port-type nni\r\n" +
                    " shutdown\r\n" +
                    "interface GigabitEthernet0/9\r\n" +
                    " port-type nni\r\n" +
                    " shutdown\r\n" +
                    "interface GigabitEthernet0/10\r\n" +
                    " port-type nni\r\n" +
                    " shutdown\r\n" +
                    "interface GigabitEthernet0/11\r\n" +
                    " port-type nni\r\n" +
                    " shutdown\r\n" +
                    "interface GigabitEthernet0/12\r\n" +
                    " port-type nni\r\n" +
                    " shutdown\r\n" +
                    "interface GigabitEthernet0/13\r\n" +
                    " port-type nni\r\n" +
                    " shutdown\r\n" +
                    "interface GigabitEthernet0/14\r\n" +
                    " port-type nni\r\n" +
                    " shutdown\r\n" +
                    "interface GigabitEthernet0/15\r\n" +
                    " port-type nni\r\n" +
                    " shutdown\r\n" +
                    "interface GigabitEthernet0/16\r\n" +
                    " port-type nni\r\n" +
                    " shutdown\r\n" +
                    "interface GigabitEthernet0/17\r\n" +
                    " port-type nni\r\n" +
                    " shutdown\r\n" +
                    "interface GigabitEthernet0/18\r\n" +
                    " port-type nni\r\n" +
                    " shutdown\r\n" +
                    "interface GigabitEthernet0/19\r\n" +
                    " port-type nni\r\n" +
                    " shutdown\r\n" +
                    "interface GigabitEthernet0/20\r\n" +
                    " port-type nni\r\n" +
                    " shutdown\r\n" +
                    "interface GigabitEthernet0/21\r\n" +
                    " port-type nni\r\n" +
                    " shutdown\r\n" +
                    "interface GigabitEthernet0/22\r\n" +
                    " port-type nni\r\n" +
                    " shutdown\r\n" +
                    "interface GigabitEthernet0/23\r\n" +
                    " port-type nni\r\n" +
                    " shutdown\r\n" +
                    "interface GigabitEthernet0/24\r\n" +
                    " port-type nni\r\n" +
                    " shutdown\r\n" +
                    "interface TenGigabitEthernet0/1\r\n" +
                    " description mlrZUC002 gi3/19;; 2012-08-15; ...\r\n" +
                    " port-type nni\r\n" +
                    " switchport mode trunk\r\n" +
                    " mtu 9800\r\n" +
                    " load-interval 30\r\n" +
                    " service-policy output cos-pol-vlan-1\r\n" +
                    "interface TenGigabitEthernet0/2\r\n" +
                    " port-type nni\r\n" +
                    " shutdown\r\n" +
                    "interface Vlan1\r\n" +
                    " no ip address\r\n" +
                    " shutdown\r\n" +
                    "interface Vlan1168\r\n" +
                    " description mlrZUC002 gi3/19.1168; VPN-INT1; 10000kbps; ...; 2012-08-15; ...\r\n" +
                    " ip vrf forwarding VPN-INT1\r\n" +
                    " ip address 213.193.70.170 255.255.255.252\r\n" +
                    "interface Vlan1169\r\n" +
                    " description mlrZUC002 gi3/19.1168; VPN-INT2; 100000kbps; ...; 2012-08-15; ...\r\n" +
                    " ip vrf forwarding VPN-INT2\r\n" +
                    " ip address 213.193.70.174 255.255.255.252\r\n" +
                    "interface Vlan1175\r\n" +
                    " description mlrZUC002 gi3/19.1175; jrq:21244; MLE VPN; ...; 100000kbps; ...; 2012-08-15; abullet\r\n" +
                    " bandwidth 106000\r\n" +
                    " ip address 10.194.42.238 255.255.255.252\r\n" +
                    "router bgp 65300\r\n" +
                    " bgp log-neighbor-changes\r\n" +
                    " neighbor 10.194.42.237 remote-as 8404\r\n" +
                    " neighbor 10.194.42.237 password 7 ...\r\n" +
                    " neighbor 10.194.42.237 timers 5 15\r\n" +
                    " address-family ipv4\r\n" +
                    "  neighbor 10.194.42.237 activate\r\n" +
                    "  neighbor 10.194.42.237 default-originate\r\n" +
                    "  no auto-summary\r\n" +
                    "  no synchronization\r\n" +
                    "  network 10.194.42.236 mask 255.255.255.252\r\n" +
                    "  network 10.230.20.24 mask 255.255.255.255\r\n" +
                    "  network 192.168.200.0\r\n" +
                    " exit-address-family\r\n" +
                    " address-family ipv4 vrf VPN-INT1\r\n" +
                    "  neighbor 213.193.70.169 remote-as 8404\r\n" +
                    "  neighbor 213.193.70.169 password 7 ...\r\n" +
                    "  neighbor 213.193.70.169 timers 5 15\r\n" +
                    "  neighbor 213.193.70.169 activate\r\n" +
                    "  no synchronization\r\n" +
                    "  network 46.140.131.40 mask 255.255.255.252\r\n" +
                    "  network 213.193.70.168 mask 255.255.255.252\r\n" +
                    " exit-address-family\r\n" +
                    "ip classless\r\n" +
                    "ip route 0.0.0.0 0.0.0.0 192.168.200.254\r\n" +
                    "no ip http server\r\n" +
                    "no ip http secure-server\r\n" +
                    "ip tacacs source-interface Loopback1\r\n" +
                    "ip sla enable reaction-alerts\r\n" +
                    "logging history size 100\r\n" +
                    "logging history debugging\r\n" +
                    "logging trap notifications\r\n" +
                    "logging facility local6\r\n" +
                    "logging source-interface Loopback1\r\n" +
                    "logging 62.2.22.122\r\n" +
                    "access-list 60 remark *** Block-Access-All default SNMP RO string ***\r\n" +
                    "access-list 60 deny   any\r\n" +
                    "access-list 61 remark *** Permitted SMC/NMC Hosts for SNMP RO access ***\r\n" +
                    "access-list 61 permit 217.168.32.0 0.0.0.255\r\n" +
                    "access-list 61 permit 217.168.33.0 0.0.0.255\r\n" +
                    "access-list 61 permit 62.2.19.0 0.0.0.255\r\n" +
                    "access-list 61 permit 62.2.20.0 0.0.3.255\r\n" +
                    "access-list 62 remark 'Customer SNMP Hosts'\r\n" +
                    "access-list 62 deny   any\r\n" +
                    "access-list 63 remark *** Permitted SMC/NMC Hosts for SNMP RW access ***\r\n" +
                    "access-list 63 permit 217.168.33.20\r\n" +
                    "access-list 63 permit 217.168.32.20\r\n" +
                    "access-list 63 permit 217.168.33.30\r\n" +
                    "access-list 63 permit 217.168.33.10\r\n" +
                    "access-list 63 permit 217.168.32.10\r\n" +
                    "access-list 88 remark 'telnet'\r\n" +
                    "access-list 88 permit 46.140.128.35\r\n" +
                    "access-list 88 permit 62.2.19.0 0.0.0.255\r\n" +
                    "access-list 88 permit 62.2.20.0 0.0.3.255\r\n" +
                    "access-list 88 permit 217.168.32.0 0.0.0.255\r\n" +
                    "access-list 88 permit 217.168.33.0 0.0.0.255\r\n" +
                    "access-list 88 permit 10.194.42.236 0.0.0.3\r\n" +
                    "access-list 88 deny   any\r\n" +
                    "access-list 95 remark 'ntp'\r\n" +
                    "access-list 95 permit 62.2.22.62\r\n" +
                    "access-list 95 permit 62.2.22.61\r\n" +
                    "access-list 95 deny   any\r\n" +
                    "route-map VPN-INT2-MAIN-LINK-IN permit 10\r\n" +
                    " set local-preference 150\r\n" +
                    "route-map VPN-INT2-MAIN-LINK-OUT permit 10\r\n" +
                    " set metric 0\r\n" +
                    "snmp-server community public RO 60\r\n" +
                    "snmp-server community ... RO 61\r\n" +
                    "snmp-server community ... RW 63\r\n" +
                    "snmp-server community ... RW 63\r\n" +
                    "snmp-server trap-source Loopback1\r\n" +
                    "snmp-server location ...; ...; .........\r\n" +
                    "snmp-server contact ...\r\n" +
                    "snmp-server enable traps snmp authentication linkdown linkup coldstart warmstart\r\n" +
                    "snmp-server enable traps tty\r\n" +
                    "snmp-server host 62.2.20.106 ...  snmp\r\n" +
                    "snmp-server host 62.2.20.107 ...  snmp\r\n" +
                    "snmp-server host 62.2.20.108 ...  snmp\r\n" +
                    "snmp-server host 62.2.20.109 ...  snmp\r\n" +
                    "snmp ifmib ifindex persist\r\n" +
                    "tacacs-server host 62.2.22.70\r\n" +
                    "tacacs-server host 62.2.22.72\r\n" +
                    "tacacs-server directed-request\r\n" +
                    "tacacs-server key 7 ...\r\n" +
                    "end\r\n").getBytes());
        } else {
            System.err.println("\nIncorrect command. Access denied.\n");
            out.write(("\r\nIncorrect command. Access denied.\r\n").getBytes());
            disconnect();
            return false;
        }
        return true;
    }

    public boolean login() throws IOException {
        OutputStream out = clientSocket.getOutputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        assert out != null;
        out.write("login: ".getBytes());
        String login = in.readLine();
        if (isLoginCorrect(login)) {
            user = login;
            return true;
        } else {
            System.err.println("\nIncorrect login. Access denied.\n");
            out.write(("\r\nIncorrect login. Access denied.\r\n").getBytes());
            disconnect();
            return false;
        }
    }

    public boolean password() throws IOException {
        OutputStream out = clientSocket.getOutputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out.write("password: ".getBytes());
        String password = in.readLine();
        if (isPasswordCorrect(user, password)) {
            String time = new Date().toString();
            out.write(("Successful authorization for user " + user + " in " + time + ".\r\n").getBytes());
            userActivityId = userActivityService.connected(user, time);
        } else {
            out.write("Incorrect password. Please, try again.\r\npassword: ".getBytes());
            password = in.readLine();
            if (isPasswordCorrect(user, password)) {
                String time = new Date().toString();
                out.write(("Successful authorization for user " + user + " in " + time + ".\r\n").getBytes());
                userActivityId = userActivityService.connected(user, time);
            } else {
                out.write("Incorrect password. Please, try again.\r\npassword: ".getBytes());
                password = in.readLine();
                if (isPasswordCorrect(user, password)) {
                    String time = new Date().toString();
                    out.write(("Successful authorization for user " + user + " in " + time + ".\r\n").getBytes());
                    userActivityId = userActivityService.connected(user, time);
                } else {
                    System.err.println("\nIncorrect password. Access denied.\n");
                    out.write(("\r\nIncorrect password. Access denied.\r\n").getBytes());
                    disconnect();
                    return false;
                }
            }
        }
        return true;
    }

    @Contract(pure = true)
    private ServerSocket getServer() {
        return serverSocket;
    }

    private boolean isPasswordCorrect(String login, String password) {
        String realPassword = userService.getPassword(login);
        return realPassword.equals(password);
    }

    private boolean isLoginCorrect(String login) {
        return userService.isLogin(login);
    }

    private void setServer(ServerSocket server) {
        this.serverSocket = server;
    }
}