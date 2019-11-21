public class sProcess {
  public int cputime;
  public int ioBlockingInitial;
  public int cpudone;
  public int ionext;
  public int numblocked;
  public int ioBlockingNow;
  public boolean up = true;
  public float expectedTime = 0;

  public sProcess (int cputime, int ioBlockingInitial, boolean up, int cpudone, int ionext, int numblocked) {
    this.cputime = cputime;
    this.ioBlockingInitial = ioBlockingInitial;
    this.cpudone = cpudone;
    this.ionext = ionext;
    this.numblocked = numblocked;
    this.up = up;
    this.expectedTime = 0;
    this.ioBlockingNow = this.ioBlockingInitial;
  }
  
  public void updateExpectedTime() {
	  if (this.up)
		  this.ioBlockingNow += (int) this.ioBlockingInitial*0.1;
      else
    	  this.ioBlockingNow -= (int) this.ioBlockingInitial*0.1;
      if (this.ioBlockingNow > this.ioBlockingInitial*1.5)
    	  this.up = false;
      if (this.ioBlockingNow < this.ioBlockingInitial*0.5)
    	  this.up = true;
  }
}
