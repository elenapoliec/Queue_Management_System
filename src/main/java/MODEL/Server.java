package MODEL;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;

    public Server(int maxTasksPerServer) {
        this.tasks = new LinkedBlockingQueue<>(maxTasksPerServer);
        this.waitingPeriod = new AtomicInteger(0);

    }

    public void addTask(Task newtask) {
        tasks.add(newtask);
        waitingPeriod.addAndGet(newtask.getServiceTime());
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Task taskInProgress = tasks.peek();

            if (taskInProgress != null) {
                // scad timpul imediat (pt a inceput timpul de procesare)
                int newServiceTime = taskInProgress.getServiceTime() - 1;
                taskInProgress.setServiceTime(newServiceTime);
                waitingPeriod.decrementAndGet();

                if (newServiceTime <= 0) {
                    tasks.poll();
                }

                try {
                    // astept sa treaca unitatea de timp
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            } else {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }


    public BlockingQueue<Task> getTasks() {
        return tasks;
    }

    //cat trebuie sa mai astepte clientii
    public int getWaitingPeriod() {
        return waitingPeriod.get();
    }
}
