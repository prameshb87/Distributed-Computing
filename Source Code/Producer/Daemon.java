

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;

import org.hyperic.sigar.*;
import org.hyperic.sigar.ptql.ProcessFinder;

public class Daemon {
	private static String url = "tcp://129.79.49.248:61616";

	private static String subject = "DS_P2_G14";
	private static int name = 0;
	//private static long[] MPIPids;
	private static Session session;
	private static ArrayList<Long> pid;
	private static Sigar sigar= new Sigar();

	public static void main(String[] args)throws JMSException {
		if(args.length < 1){
			System.out.println("usage: java -jar daemon.jar <node_id>");
		}
		name = Integer.parseInt(args[0]);
		Connection connection = null;
		try {
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
			connection = connectionFactory.createConnection();
			connection.start();

			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createTopic(subject);
			MessageProducer producer = session.createProducer(destination);

			Mem mem = null;
			CpuPerc cpu = null;
			ProcCpu procCpu = null;
			ProcMem procMem = null;
			double	SumProcCpu = 0;
			double SumProcMem = 0;
			double procCpuUtil;
			double procCpuMem;
			//pid = new ArrayList<Long>();
			
			
			
//			String query="State.Name.eq=chrome";
//
//			
//				MPIPids = ProcessFinder.find(sigar, query);
//				for(int i =0;i<MPIPids.length;i++){
//					System.out.println("Process ID: "+MPIPids[i]);
//				}
				
			
	        	while(true){
			
			    String s;
			    s = null;
			    pid = new ArrayList<Long>();

			    Process proc = Runtime.getRuntime().exec("sh pid.sh");
			    BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			    s = br.readLine();
				
			    
			    if (s == null){
	//        	if(MPIPids == null){	
				System.out.println("Specified Process not running");
				//pid = null;
			    }
			    else{
				System.out.println("Process Running now");
				}
			    while(s!=null)
				{
				    s = br.readLine();
				    if(s==null)
					break;
				    else{
					   pid.add(Long.parseLong(s));
					}
				}
			    
					mem = sigar.getMem();
					cpu = sigar.getCpuPerc();
					SumProcCpu = 0.0 ;
					SumProcMem = 0.0 ;
					
					if(pid !=null) {
						for(long p:pid){

//					if(MPIPids !=null) {
//						for(long p:MPIPids){
//
					
							procCpu = sigar.getProcCpu(p);
							procMem = sigar.getProcMem(p);
						
							procCpuUtil = (procCpu.getPercent())*100.0;
							procCpuMem = ((double)procMem.getSize()/(double)mem.getTotal())*100.0;
							SumProcCpu += procCpuUtil;
							SumProcMem += procCpuMem;
						    }
						}
					TextMessage message = session.createTextMessage(""+name+" "+SumProcMem+" "+SumProcCpu+" "+mem.getUsedPercent()+" "+100.0*(cpu.getSys()+cpu.getUser())+" "+System.currentTimeMillis());
//					System.out.println(""+name+" "+SumProcMem+" "+SumProcCpu+" "+mem.getUsedPercent()+" "+100.0*(cpu.getSys()+cpu.getUser())+" "+System.currentTimeMillis());
					System.out.println("Sending : "+message.getText());
					producer.send(message);
					Thread.sleep(5000);
				
			}
		
		}	
		catch(Exception ex){
		    System.out.println("Exception in daemon. Stopping");
		    ex.printStackTrace();
		}
		session.close();
		connection.close();	
	
	}
}

