package GUI;

import BUSINESS.SimulationManager;

public class main {
    public static void main(String[] args) {
        SimulationFrame frame = new SimulationFrame();
        frame.addStartListener(e -> {
            SimulationManager sim = new SimulationManager(frame);
            new Thread(sim).start();
        });
    }
}
