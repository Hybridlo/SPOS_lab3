// Run() is called from Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.

import java.util.Vector;
import java.io.*;

public class SchedulingAlgorithm {
  static java.util.Random generator = new java.util.Random(System.currentTimeMillis());

  public static Results Run(int runtime, Vector processVector, Results result, float coefficient) {
    int i = 0;
    int comptime = 0;
    int currentProcess = 0;
    int previousProcess = 0;
    int size = processVector.size();
    int completed = 0;
    String resultsFile = "Summary-Processes";

    result.schedulingType = "Interactive (nonpreemptive)";
    result.schedulingName = "Shortest Process Next"; 
    try {
      //BufferedWriter out = new BufferedWriter(new FileWriter(resultsFile));
      //OutputStream out = new FileOutputStream(resultsFile);
      PrintStream out = new PrintStream(new FileOutputStream(resultsFile));
      sProcess process = (sProcess) processVector.elementAt(currentProcess);
      
      process.updateExpectedTime();
      out.println("Process: " + currentProcess + " registered... (" + process.cputime + " " + process.ioBlockingDelayNow + " " + process.cpudone + " " + process.cpudone + ")");
      
      while (comptime < runtime) {
        if (process.cpudone == process.cputime) {
          completed++;
          out.println("Process: " + currentProcess + " completed... (" + process.cputime + " " + process.ioBlockingDelayNow + " " + process.cpudone + " " + process.cpudone + ")");
          
          if (completed == size) {
            result.compuTime = comptime;
            out.close();
            return result;
          }
          
          float minExpectedTime = 1000000;
          for (i = size - 1; i >= 0; i--) {
            process = (sProcess) processVector.elementAt(i);
            
            if (process.expectedTime < minExpectedTime && process.cpudone < process.cputime && !process.isBlocked) {
            	minExpectedTime = process.expectedTime;
            	currentProcess = i;
            }
          }
          
          process = (sProcess) processVector.elementAt(currentProcess);
          process.updateExpectedTime();
          
          out.println("Time elapsed since start: " + comptime);
          
          out.println("Process: " + currentProcess + " registered... (" + process.cputime + " " + process.ioBlockingDelayNow + " " + process.cpudone + " " + process.cpudone + ")");
        }      
        
        if (process.ioBlockingDelayNow == process.ionext) {
          out.println("Process: " + currentProcess + " I/O blocked... (" + process.cputime + " " + process.ioBlockingDelayNow + " " + process.cpudone + " " + process.cpudone + ")");
          process.isBlocked = true;
          out.println("Blocking time: " + process.ioBlockingTime);

          if (process.expectedTime == 0.0)
        	  process.expectedTime = process.ioBlockingDelayNow;
          else
        	  process.expectedTime = process.expectedTime * coefficient + process.ioBlockingDelayNow * (1 - coefficient);
          
          out.println("Process: " + currentProcess + " expected time = " + process.expectedTime + "\n");
          process.numblocked++;
          process.ionext = 0; 
          previousProcess = currentProcess;
          
          float minExpectedTime = 1000000;
          for (i = size - 1; i >= 0; i--) {
            process = (sProcess) processVector.elementAt(i);
            if (process.expectedTime < minExpectedTime && previousProcess != i && process.cpudone < process.cputime && !process.isBlocked) { 
            	minExpectedTime = process.expectedTime;
            	currentProcess = i;
            }
          }
          
          process = (sProcess) processVector.elementAt(currentProcess);
          process.updateExpectedTime();
          
          out.println("Time elapsed since start: " + comptime);
          
          out.println("Process: " + currentProcess + " registered... (" + process.cputime + " " + process.ioBlockingDelayNow + " " + process.cpudone + " " + process.cpudone + ")");
        }   
        
        if (!process.isBlocked) {
        	
	        process.cpudone++; 
     
	        if (process.ioBlockingDelayNow > 0) {
	          process.ionext++;
	        }
	        
        }
        
        comptime++;
        
        for(int k = 0; k < size; k++) {
        	sProcess proc = (sProcess) processVector.elementAt(k);
        	proc.incrementIoBlockedTime();
        }
      }
      out.close();
    } catch (IOException e) { /* Handle exceptions */ }
    result.compuTime = comptime;
    return result;
  }
}
