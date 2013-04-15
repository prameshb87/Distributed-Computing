
public class Aggregator extends Thread{
	private MonitoringGUI myGUI;
	public Aggregator(MonitoringGUI gui){
		myGUI = gui;
	}
	public void run(){
		while(true){
			myGUI.summarize();
			try {
				sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
