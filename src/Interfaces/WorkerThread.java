package Interfaces;

import java.util.ArrayDeque;
import java.util.Queue;


public class WorkerThread {
    private Thread thread;
    private Queue<Runnable> queue;
    private volatile boolean running;

    public WorkerThread() {
        thread = new Thread(this::run);

        queue = new ArrayDeque<>();
        running = false;
    }

    public void start() {
        // start the thread going
        running = true;
        thread.start();
    }

    public void stop() {
        // how should we stop this object's thread?
        running = false;
    }

    public void add(Runnable r) {
        // add a runnable to our queue
        synchronized (queue) {
            queue.add(r);
            queue.notifyAll();
        }
    }

    public void run() {
        // run a loop that repeatedly
        //  -- waits for the queue to acquire some contents
        //  -- pops a runnable off the queue, and runs it.
        //  -- goes back to waiting on the queue.
        // figure out how to quit this loop with grace and style.
        try {
            while (running) {
                synchronized (queue) {
                    if (!queue.isEmpty()) {
                        Thread t = new Thread(queue.poll());
                        t.start();
                    } else {
                        queue.wait();
                    }
                }
            }
        } catch (InterruptedException e) {}
    }
}