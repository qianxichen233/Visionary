package client;

public class client {
    public static void main(String[] args) {
        new ClientInstance("localhost", 23333).start();
    }
}