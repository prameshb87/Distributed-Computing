This project implements the pagerank algorithm and runs it on either BareMetal nodes or VMs depending on user. For this,
the user specifies the number of processes to which the task will be delegated. Simultaneously, Resource Monitor runs on 
a client machine to display the resource usage statistics of the machines where the processes are running.

README!!!

1. Copy /Source Code/Producer Folder to the respective compute nodes.
2. Copy Scripts folder to the respective nodes.
3. To run on Bare-metal nodes, run the /Scripts/BareMetal/job_bm.sh script.
	- USAGE -
	qsub job_bm.sh
4. To run on VM nodes, run the /Scripts/VM/job_vm.sh script.
	- USAGE -
	qsub job_vm.sh
5. To run the resource Monitor, run the /Source Code/Consumer/consumer.java program.
	- USAGE -
	java consumer < maximum expected number of nodes from 1 to n >
