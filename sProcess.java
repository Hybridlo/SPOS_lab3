public class sProcess {
  public int cputime;
  public int ioblocking;
  public int cpudone;
  public int ionext;
  public int numblocked;
  public int iorandom;
  public int ioBlockingRandomed;
  public float expectedTime = 0;

  public sProcess (int cputime, int ioblocking, int iorandom, int cpudone, int ionext, int numblocked) {
    this.cputime = cputime;
    this.ioblocking = ioblocking;
    this.cpudone = cpudone;
    this.ionext = ionext;
    this.numblocked = numblocked;
    this.iorandom = iorandom;
    this.expectedTime = this.ioblocking;
    if (this.iorandom > this.ioblocking) {
    	this.iorandom = this.ioblocking;
    }
  } 	
}
