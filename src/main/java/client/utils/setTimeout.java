package client.utils;

public class setTimeout extends Thread {
    private volatile boolean cleared = false;
    private setTimeoutEvent event;
    private int time;

    public setTimeout(setTimeoutEvent event, int time) {
        this.event = event;
        this.time = time;
        this.start();
    }

    public void clearTimeout() {
        cleared = true;
    }

    @Override
    public void run() {
        try {
            sleep(time);
        } catch (Exception e) {
            System.err.println(e);
        }

        if (!cleared)
            event.performAction();
    }
}
