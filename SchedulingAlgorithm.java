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
      out.println("Process: " + currentProcess + " registered... (" + process.cputime + " " + process.ioBlockingNow + " " + process.cpudone + " " + process.cpudone + ")");
      while (comptime < runtime) {
        if (process.cpudone == process.cputime) {
          completed++;
          out.println("Process: " + currentProcess + " completed... (" + process.cputime + " " + process.ioBlockingNow + " " + process.cpudone + " " + process.cpudone + ")");
          if (completed == size) {
            result.compuTime = comptime;
            out.close();
            return result;
          }
          float minExpectedTime = 1000000;
          for (i = size - 1; i >= 0; i--) {
            process = (sProcess) processVector.elementAt(i);
            if (process.expectedTime < minExpectedTime && process.cpudone < process.cputime) {
            	minExpectedTime = process.expectedTime;
            	currentProcess = i;
            }
          }
          process = (sProcess) processVector.elementAt(currentProcess);
          process.updateExpectedTime();
          out.println("Process: " + currentProcess + " registered... (" + process.cputime + " " + process.ioBlockingNow + " " + process.cpudone + " " + process.cpudone + ")");
        }      
        if (process.ioBlockingNow == process.ionext) {
          out.println("Process: " + currentProcess + " I/O blocked... (" + process.cputime + " " + process.ioBlockingNow + " " + process.cpudone + " " + process.cpudone + ")");

          if (process.expectedTime == 0.0)
        	  process.expectedTime = process.ioBlockingNow;
          else
        	  process.expectedTime = process.expectedTime * coefficient + process.ioBlockingNow * (1 - coefficient);
          out.println("Process: " + currentProcess + " expected time = " + process.expectedTime);
          process.numblocked++;
          process.ionext = 0; 
          previousProcess = currentProcess;
          float minExpectedTime = 1000000;
          for (i = size - 1; i >= 0; i--) {
            process = (sProcess) processVector.elementAt(i);
            if (process.expectedTime < minExpectedTime && previousProcess != i && process.cpudone < process.cputime) { 
            	minExpectedTime = process.expectedTime;
            	currentProcess = i;
            }
          }
          process = (sProcess) processVector.elementAt(currentProcess);
          process.updateExpectedTime();
          out.println("Process: " + currentProcess + " registered... (" + process.cputime + " " + process.ioBlockingNow + " " + process.cpudone + " " + process.cpudone + ")");
        }        
        process.cpudone++;       
        if (process.ioBlockingNow > 0) {
          process.ionext++;
        }
        comptime++;
      }
      out.close();
    } catch (IOException e) { /* Handle exceptions */ }
    result.compuTime = comptime;
    return result;
  }
}
