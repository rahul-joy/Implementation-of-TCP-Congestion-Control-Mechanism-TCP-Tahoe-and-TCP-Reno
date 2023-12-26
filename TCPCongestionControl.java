package pkg123;
import java.util.*;
public class TCPCongestionControl
{

private int cwnd;
private int ssthresh;
private int rtt;
private boolean congestion;

public TCPCongestionControl(int init_ssthresh)
{
cwnd = 1;
ssthresh = init_ssthresh;
congestion = false;
rtt = 0;
}

public void run()
{
System.out.println("Connected to the Server... ...");
 System.out.println("Enter the length of your data: ");
 Scanner scan = new Scanner(System.in);
 int len = scan.nextInt();
 int dataSeqNum = 0;
 System.out.println("Your data is started to be sent ... ");

 while(dataSeqNum < len)
 {
 this.rtt++;
 System.out.println(); System.out.println();
 System.out.println("Data sending in RTT number "+this.rtt);
 System.out.println("−−−−−−−−−−−−−−−−−−−−−−−−−−−−");
 System.out.println("previous cwnd size: "+ cwnd);
 System.out.println("updated ssthresh value: " + ssthresh);
 if (!congestion)
 {
 if (cwnd < ssthresh)
 {
 // Slow Start Phase
 // Exponentially increase of cwnd
 cwnd = cwnd * 2;
 System.out.println("...SS phase running...");
 }
 else if(cwnd >= ssthresh)
 {
 // Congestion Avoidance Phase
 // Linearly increase of cwnd
 cwnd = cwnd + 1;
 System.out.println("...CA phase running...");
 }
 }
 System.out.println("updated cwnd size: "+ cwnd);

 sendPacket(dataSeqNum);

 dataSeqNum = dataSeqNum + cwnd;
 }
 System.out.println("\n\nYour data sending is completed. No more data to send." + "\nCongestion Control mechanism concludes.\nIt took "+this.rtt −−+" transmission rounds to send the whole data.");

 }

 public void sendPacket(int dataSeqNum) // A function to deal with the congestion part

 {
 System.out.println("Data from " + (dataSeqNum+1) + " − " + (dataSeqNum+ cwnd)+" is being sent now... ...\n\n");

 if(!receiveAcknowledgment())
 {
 congestion = true;
 System.out.println("... but wait ! congestion has been detected !");
 if(timeout())
 {
 handleTimeoutCongestion();
 }
 else
 {
 handle3DupAckCongestion();
 }
 }
 else
 {
 congestion = false;
 }
 }

 public boolean receiveAcknowledgment() // A function to generate congestion randomly

 {
 //returns true if no congestion and data is received by receiver.
 //returns false if congestion occurred and data is not received by receiver.

 Random ack = new Random();
 return ack.nextBoolean();
 }

 public boolean timeout() // A function to decide randonmly if congestion is detected using timeout or duplicateAck

 {
 //returns true if congestion is detected using timeout.
 Random rttRandom = new Random();
 return rttRandom.nextBoolean();
 }

 public void handleTimeoutCongestion() // Function to handle when congestion by timeout

 {
 // severe congestion. cwnd will start from slow start with value 1
 System.out.println("\n\nTimeout occured.Handling Timeout based congestion: cwnd value will become 1.");

 ssthresh = cwnd / 2;
if (ssthresh==0) ssthresh = 1; // making ssthresh 1, if it comes as zero.
cwnd = 1;

 retransmitPacket();
 // we have to make the congestion variable false, since we have recovered using the handling process here.
 }

 public void handle3DupAckCongestion() // Function to handle when congestion by duplicateAck
 {
 // light congestion. cwnd will start from half of the ssthresh value
 // This is the main difference between TCP Reno and TCP Tahoe.

 System.out.println("\n\nHandling Triple Dup Ack based congestion: cwnd value will be halved.");
ssthresh = cwnd / 2;
if (ssthresh==0) ssthresh = 1; // making ssthresh 1, if it comes as zero
 cwnd = ssthresh;

 retransmitPacket();
 // we have to make the congestion variable false, since we ahve recovered using the handling process here.

 }

 public void retransmitPacket() // Function to know that congestion has been recovered.

 {
 congestion = false;
System.out.println("\nRetransmiting the lost packet now after handling.\n");

 }

 public static void main(String[] args)
 {
 Scanner scn = new Scanner(System.in);
 System.out.println("Please input the initial ssthresh value: ");
 int ssthresh = scn.nextInt();

 TCPCongestionControl reno = new TCPCongestionControl(ssthresh);
 reno.run();
 }
 }