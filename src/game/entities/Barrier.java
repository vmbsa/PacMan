package game.entities;

public class Barrier {

	private int numberWaiters;
	private int currentWaiters = 0;
	private int passedWaiters = 0;
	
	public Barrier(int numberWaiters) {
		this.numberWaiters = numberWaiters;
	}

	public synchronized void barrierWait() throws InterruptedException {
		currentWaiters++;
		while (currentWaiters < numberWaiters) {
			wait();
		}
		if (passedWaiters == 0)
			notifyAll();
		passedWaiters++;
		if (passedWaiters == numberWaiters) {
			passedWaiters = 0;
			currentWaiters = 0;
		}
	}
}