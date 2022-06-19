import java.util.*;

public class Chat implements Comparable<Chat> {
    private String name;
    private Set<Client> clients = new HashSet<>();

    public static class Node {
        public Client client;
        public String message;

        public Node(Client client, String message) {
            this.client = client;
            this.message = message;
        }
    }

    public Chat(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return name.equals(chat.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public int compareTo(Chat o) {
        return this.name.compareTo(o.name);
    }

    public String getName() {
        return name;
    }

    public Set<Client> getClients() {
        return clients;
    }

    public void addMessage(Node node) {
        for (Client client : clients) {
            if (!node.client.equals(client))
                client.getOut().println("@%s: %s".formatted(node.client.getNick(), node.message));
        }
    }
}
