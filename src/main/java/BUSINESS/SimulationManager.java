package BUSINESS;

import GUI.SimulationFrame;
import MODEL.Server;
import MODEL.Task;

import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SimulationManager implements Runnable {
    public int timeLimit = 100;
    public int maxProcessingTime = 10;
    public int minProcessingTime = 2;
    public int minArrivalTime = 2;
    public int maxArrivalTime = 30;
    public int numberOfServers = 3;
    public int numberOfClients = 100;
    public SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_TIME;

    private Scheduler scheduler;
    private SimulationFrame frame;
    private List<Task> generatedTasks;

    private double totalWaitingTime = 0;
    private double totalServiceTime = 0;
    private int peakHour = 0;
    private int maxClientsAtOnce = -1;

    public SimulationManager(SimulationFrame frame) {
        this.frame = frame;
        this.numberOfClients = Integer.parseInt(frame.getN());
        this.numberOfServers = Integer.parseInt(frame.getQ());
        this.timeLimit = Integer.parseInt(frame.getTSim());
        this.minArrivalTime = Integer.parseInt(frame.getMinArrival());
        this.maxArrivalTime = Integer.parseInt(frame.getMaxArrival());
        this.minProcessingTime = Integer.parseInt(frame.getMinService());
        this.maxProcessingTime = Integer.parseInt(frame.getMaxService());

        // initializare Scheduler
        this.scheduler = new Scheduler(numberOfServers, 100);
        this.scheduler.changeStrategy(selectionPolicy);

        generateNRandomTasks();

    }

    private void generateNRandomTasks() {
        generatedTasks= new ArrayList<>();
        Random random = new Random();
        for(int i=0;i<numberOfClients;i++) {
            //generam service time intre min si max
            int serviceTime= random.nextInt(maxProcessingTime-minProcessingTime+1)+minProcessingTime;
            //generam arrival time intre min si max
            int arrivalTime = random.nextInt(maxArrivalTime - minArrivalTime + 1) + minArrivalTime;

            generatedTasks.add(new Task(0, arrivalTime, serviceTime));
            totalServiceTime += serviceTime;
        }

        //sortam lista dupa arrivalTime
        Collections.sort(generatedTasks, Comparator.comparingInt(Task::getArrivalTime));

        //atribui id-urile clientilor
        for (int i = 0; i < generatedTasks.size(); i++) {
            Task t = generatedTasks.get(i);
            generatedTasks.set(i, new Task(i + 1, t.getArrivalTime(), t.getServiceTime()));
        }
    }


    @Override
    public void run() {
        int currentTime = 0;

        try (PrintWriter logWriter = new PrintWriter(new FileWriter("simulare.txt"))) {

            while (currentTime <= timeLimit) {

                //lista de clienti care inca nu au intrat in cozi
                Iterator<Task> it = generatedTasks.iterator();

                while (it.hasNext()) {
                    Task t = it.next();

                    if (t.getArrivalTime() == currentTime) {

                        // alegem serverul optim
                        Server bestServer = scheduler.dispatchTask(t);

                        totalWaitingTime += bestServer.getWaitingPeriod() - t.getServiceTime();

                        it.remove();
                    }
                }

                // peak hour
                int currentClients = 0;
                for (Server s : scheduler.getServers()) {
                    currentClients += s.getTasks().size();
                }

                if (currentClients > maxClientsAtOnce) {
                    maxClientsAtOnce = currentClients;
                    peakHour = currentTime;
                }

                // log
                String state = getSimulationState(currentTime);
                logWriter.println(state);
                frame.updateDisplay(state);

                // conditie de stop
                if ((generatedTasks.isEmpty() && areAllServersEmpty()) || currentTime > timeLimit) {
                    break;
                }

                currentTime++;
                Thread.sleep(500);
            }

            double avgWaitingTime = totalWaitingTime / numberOfClients;
            double avgServiceTime = totalServiceTime / numberOfClients;

            //afisarea de la final
            String results =
                    "\n--- FINAL RESULTS ---" +
                            "\nAverage Waiting Time: " + String.format("%.2f", avgWaitingTime) +
                            "\nAverage Service Time: " + String.format("%.2f", avgServiceTime) +
                            "\nPeak Hour: " + peakHour;

            logWriter.println(results);
            frame.updateDisplay(results);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //verific daca mai am sau nu clienti in coada
    private boolean areAllServersEmpty() {
        for (Server s : scheduler.getServers()) {
            if (!s.getTasks().isEmpty()) return false;
        }
        return true;
    }

    //afisare
    private String getSimulationState(int currentTime) {
        StringBuilder sb = new StringBuilder("Time " + currentTime + "\nWaiting clients: ");
        for (Task t : generatedTasks) sb.append("(").append(t.getId()).append(",")
                .append(t.getArrivalTime()).append(",").append(t.getServiceTime()).append(") ");
        sb.append("\n");
        int i = 1;
        for (Server s : scheduler.getServers()) {
            sb.append("Queue ").append(i++).append(": ").append(s.getTasks().isEmpty() ? "closed" : "");
            for (Task t : s.getTasks()) sb.append("(").append(t.getId()).append(",")
                    .append(t.getArrivalTime()).append(",").append(t.getServiceTime()).append(") ");
            sb.append("\n");
        }
        return sb.toString();
    }
}
