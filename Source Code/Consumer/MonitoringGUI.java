import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class MonitoringGUI extends ApplicationFrame implements ActionListener {

    private TimeSeries[] cpuSeries;
    private TimeSeries[] memSeries;
    private TimeSeries[] procCpuSeries;
    private TimeSeries[] procMemSeries;
    private double[] lastCPUValues;
    private double[] lastMemValues;
    private double[] lastProcCPUValues;
    private double[] lastProcMemValues;
    public JPanel[] contents;
    int current;
    int length;
    
    final public JPanel mainPanel;
    int numberOfNodes;

    public MonitoringGUI(final String title, int nodes) {

        super(title);
        numberOfNodes = nodes;
        contents = new JPanel[numberOfNodes+1];
        cpuSeries = new TimeSeries[numberOfNodes+1];
        memSeries = new TimeSeries[numberOfNodes+1];
        procCpuSeries = new TimeSeries[numberOfNodes+1];
        procMemSeries = new TimeSeries[numberOfNodes+1];
        TimeSeriesCollection cpuDataset[] = new TimeSeriesCollection[numberOfNodes+1];
        TimeSeriesCollection memDataset[] = new TimeSeriesCollection[numberOfNodes+1];
        TimeSeriesCollection procCpuDataset[] = new TimeSeriesCollection[numberOfNodes+1];
        TimeSeriesCollection procMemDataset[] = new TimeSeriesCollection[numberOfNodes+1];
        JFreeChart cpuChart[] = new JFreeChart[numberOfNodes+1];
        JFreeChart memChart[] = new JFreeChart[numberOfNodes+1];
        JFreeChart procCpuChart[] = new JFreeChart[numberOfNodes+1];
        JFreeChart procMemChart[] = new JFreeChart[numberOfNodes+1];
        ChartPanel cpuChartPanel[] = new ChartPanel[numberOfNodes+1];
        ChartPanel memChartPanel[] = new ChartPanel[numberOfNodes+1];
        ChartPanel procCpuChartPanel[] = new ChartPanel[numberOfNodes+1];
        ChartPanel procMemChartPanel[] = new ChartPanel[numberOfNodes+1];
        lastCPUValues = new double[numberOfNodes];
        for(int i = 0; i<numberOfNodes;i++){
        	lastCPUValues[i]=-1;
        }
        lastMemValues = new double[numberOfNodes];
        for(int i = 0; i<numberOfNodes;i++){
        	lastMemValues[i]=-1;
        }
        lastProcCPUValues = new double[numberOfNodes];
        for(int i = 0; i<numberOfNodes;i++){
        	lastProcCPUValues[i]=-1;
        }
        lastProcMemValues = new double[numberOfNodes];
        for(int i = 0; i<numberOfNodes;i++){
        	lastProcMemValues[i]=-1;
        }
        
        for(int i = 0; i <= numberOfNodes; i++){
        	cpuSeries[i] = new TimeSeries("CPU Usage", Millisecond.class);
        	cpuDataset[i] = new TimeSeriesCollection(cpuSeries[i]);
        	cpuChart[i] = createChart(cpuDataset[i], "CPU Usage");
        	cpuChartPanel[i] =  new ChartPanel(cpuChart[i]);
        	cpuChartPanel[i].setPreferredSize(new java.awt.Dimension(300, 250));
        	cpuChartPanel[i].setBackground(Color.BLACK);
        	System.out.println(" "+cpuDataset[i]);
        	
        	procCpuSeries[i] = new TimeSeries("Process CPU Usage", Millisecond.class);
        	procCpuDataset[i] = new TimeSeriesCollection(procCpuSeries[i]);
        	procCpuChart[i] = createChart(procCpuDataset[i], "Process CPU Usage");
        	procCpuChartPanel[i] =  new ChartPanel(procCpuChart[i]);
        	procCpuChartPanel[i].setPreferredSize(new java.awt.Dimension(300, 250));
        	procCpuChartPanel[i].setBackground(Color.BLACK);
        	
        	memSeries[i] = new TimeSeries("Memory Usage", Millisecond.class);
        	memDataset[i] = new TimeSeriesCollection(memSeries[i]);
        	memChart[i] = createChart(memDataset[i], "Memory Usage");
        	memChartPanel[i] =  new ChartPanel(memChart[i]);
        	memChartPanel[i].setPreferredSize(new java.awt.Dimension(300, 250));
        	memChartPanel[i].setBackground(Color.BLACK);
        	
        	procMemSeries[i] = new TimeSeries("Process Memomory Usage", Millisecond.class);
        	procMemDataset[i] = new TimeSeriesCollection(procMemSeries[i]);
        	procMemChart[i] = createChart(procMemDataset[i], "Process Memory Usage");
        	procMemChartPanel[i] =  new ChartPanel(procMemChart[i]);
        	procMemChartPanel[i].setPreferredSize(new java.awt.Dimension(300, 250));
        	procMemChartPanel[i].setBackground(Color.BLACK);
        	
        	GridLayout gl = new GridLayout(0,2);
        	
        	contents[i] = new JPanel(gl);
            contents[i].add(cpuChartPanel[i]);
            contents[i].add(memChartPanel[i]);
            contents[i].add(procCpuChartPanel[i]);
            contents[i].add(procMemChartPanel[i]);
       
        }
        
        String[] shapeStrings = new String[numberOfNodes+1];
        for(int i = 0; i < numberOfNodes; i++){
        	shapeStrings[i] = "Node "+ (i+1);
        }
        shapeStrings[numberOfNodes] = "Combined";
        
        JComboBox shapeList = new JComboBox(shapeStrings);
        shapeList.setSelectedIndex(0);
        shapeList.addActionListener(this);
        shapeList.setActionCommand("CHANGE_MACHINE");
        
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(contents[0], BorderLayout.SOUTH);
        mainPanel.add(shapeList, BorderLayout.NORTH);
        
        current = 0;
        setContentPane(mainPanel);

    }

    private JFreeChart createChart(final XYDataset dataset, final String title) {
        final JFreeChart result = ChartFactory.createTimeSeriesChart(
            title, 
            "Time", 
            "Percentage",
            dataset, 
            true, 
            true, 
            false
        );
        
        result.setBackgroundPaint((new Color(0x66CC99))); 
        
        final XYPlot plot = result.getXYPlot();
        
        plot.setBackgroundPaint(new Color(0xFFFFFF)); 
        plot.setDomainGridlinePaint(Color.GREEN); 
        plot.setRangeGridlinePaint(Color.GREEN); 

        plot.setDomainCrosshairVisible(true); 
        plot.setRangeCrosshairVisible(true);
       
        ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);
        axis.setFixedAutoRange(30000.0);  
        axis = plot.getRangeAxis();
        axis.setRange(0.0, 100.0); 
        
        return result;
    }
    
    public void actionPerformed(final ActionEvent e) {
        
        if (e.getActionCommand().equals("CHANGE_MACHINE")) {
        	JComboBox comboBox = (JComboBox)e.getSource();
            int shapeList = comboBox.getSelectedIndex();
            
            mainPanel.remove(contents[current]);
            mainPanel.add(contents[shapeList], BorderLayout.SOUTH);
            current = shapeList;	
            
            mainPanel.validate();
        	mainPanel.repaint();
        }
    }
    
    public void addNewPoints(int node, double procCpuValue, double procMemValue, double cpuValue, double memValue, long millisecond){
    	Date date = new Date(millisecond);
    	Millisecond mil = new Millisecond(date);
    	this.cpuSeries[node].add(mil, cpuValue);
    	lastCPUValues[node] = cpuValue;
    	this.memSeries[node].add(mil, memValue);
    	lastMemValues[node] = memValue;
    	this.procCpuSeries[node].add(mil, procCpuValue);
    	lastProcCPUValues[node] = procCpuValue;
    	this.procMemSeries[node].add(mil, procMemValue);
    	lastProcMemValues[node] = procMemValue;
    }
    
    public void summarize(){
    	Millisecond mil = new Millisecond();
    	double cpuValue = 0.0, memValue = 0.0,procCpuValue = 0.0, procMemValue = 0.0;
    	for(int i= 0; i < numberOfNodes; i++){
    		if(lastCPUValues[i]!=-1)
    			cpuValue+=lastCPUValues[i];
    		if(lastMemValues[i]!=-1)
    			memValue+=lastMemValues[i];
    		
    		if(lastProcCPUValues[i]!=-1)
    			procCpuValue+=lastProcCPUValues[i];
    		if(lastProcMemValues[i]!=-1)
    			procMemValue+=lastProcMemValues[i];
    	}
    	//System.out.println("procCpuValue:"+procCpuValue+" procMemValue:"+procMemValue);
    	length = numberOfNodes;
    	for(int i =0;i<numberOfNodes;i++){
    		if(lastCPUValues[i]==-1)
    			length--;
    	}
    	//System.out.println("Length: "+length);
    	cpuValue = cpuValue/length;
    	memValue = memValue/length;
    	procMemValue = procMemValue/length;
    	procMemValue = procCpuValue/length;
    	this.cpuSeries[numberOfNodes].add(mil, cpuValue);
    	this.memSeries[numberOfNodes].add(mil, memValue);
    	this.procCpuSeries[numberOfNodes].add(mil, procCpuValue);
    	this.procMemSeries[numberOfNodes].add(mil, procMemValue);
    	//System.out.println("cpu value"+cpuValue+"mem Value"+memValue);
    	
    	//System.out.println("CPU Value:"+cpuValue+" Memory Value:"+memValue+" last cpu series value:"+cpuSeries[numberOfNodes]+" last memory seies Value:"+memSeries[numberOfNodes]);
    }
    

    /**
     * Starting point for the demonstration application.
     *
     * @param args  ignored.
     */
    public static void main(final String[] args) {
    	if(args.length < 1){
			System.out.println("usage: java -jar monitor.jar <numbuer_of_nodes>");
		}
    	
    	int numberOfNodes = Integer.parseInt(args[0]);
        final MonitoringGUI demo = new MonitoringGUI("Resource Monitor", numberOfNodes);
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
       
        Consumer consumer = new Consumer(demo);
        consumer.start();
        
        Aggregator aggregatro = new Aggregator(demo);
        aggregatro.start();

        try {
        	 aggregatro.join();
        } catch (InterruptedException e) {
			e.printStackTrace();
		}

    }

}

