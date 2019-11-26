public class sProcess {
  public int cputime;
  public int ioBlockingDelayInitial;
  public int cpudone;
  public int ionext;
  public int numblocked;
  public int ioBlockingDelayNow;
  public boolean up = true;
  public float expectedTime = 0;
  public int ioBlockingTime;
  public int ioBlockedFor = 0;
  public boolean isBlocked = false;

  public sProcess (int cputime, int ioBlockingInitial, int ioBlockingTime, boolean up, int cpudone, int ionext, int numblocked) {
    this.cputime = cputime;
    this.ioBlockingDelayInitial = ioBlockingInitial;
    this.cpudone = cpudone;
    this.ionext = ionext;
    this.numblocked = numblocked;
    this.up = up;
    this.expectedTime = 0;
    this.ioBlockingDelayNow = this.ioBlockingDelayInitial;
    this.ioBlockingTime = ioBlockingTime;
  }
  
  public void updateExpectedTime() {
	  if (this.up)
		  this.ioBlockingDelayNow += (int) this.ioBlockingDelayInitial*0.1;
      else
    	  this.ioBlockingDelayNow -= (int) this.ioBlockingDelayInitial*0.1;
      if (this.ioBlockingDelayNow > this.ioBlockingDelayInitial*1.5)
    	  this.up = false;
      if (this.ioBlockingDelayNow < this.ioBlockingDelayInitial*0.5)
    	  this.up = true;
  }
  
  public void incrementIoBlockedTime() {
	  if (this.isBlocked) {
		  this.ioBlockedFor++;
	  }
	  
	  if (this.ioBlockingTime == this.ioBlockedFor) {
		  this.isBlocked = false;
		  this.ioBlockedFor = 0;
	  }
  }
}
