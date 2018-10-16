package io.dama.par.dining.cond;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Philosopher extends Thread implements IPhilosopher {


	private Philosopher left;
	private Philosopher right;
	private ReentrantLock table;
	private Condition condition;
	private boolean isEating = false;
	private boolean isRunning = true;
	

	
	
	@Override
	public void setLeft(final IPhilosopher left) {
		this.left = (Philosopher) left;
	}

	@Override
	public void setRight(final IPhilosopher right) {
		this.right = (Philosopher) right;

	}

	@Override
	public void setTable(final Lock table) {
		this.table = (ReentrantLock) table;
		condition = table.newCondition();

	}

	@Override
	public void stopPhilosopher() {
		this.isRunning = false;
		System.out.println("stopped");

	}

	public void eat() throws InterruptedException {
		table.lock();
		
		try {
			while (left.isEating || right.isEating)
				condition.await();
			System.out.println(getId() + " is eating");
			this.isEating = true;

		} finally {
			table.unlock();
		}
		Thread.sleep(500);

	}
	
	public void think(){
		table.lock();
		try{
			isEating = false;
			left.condition.signal();
			right.condition.signal();
			System.out.println(getId() + " is thinking");
		}finally{
			table.unlock();
		}
	}
	
	public void run(){
		try{
			while(isRunning){
				eat();
				think(); 
			}
		}catch(InterruptedException e){}
	}
	

}
