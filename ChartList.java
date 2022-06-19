import java.util.*;

public class ChartList {
    private TreeSet<Chat> chatSet = new TreeSet<>();

    public Set<Chat> getChatSet() {
        return chatSet;
    }

    public void newChat(String name){
        Chat chat = new Chat(name);
        chatSet.add(chat);
    }
    public Chat findChart(Chat chat){
        for (Chat c : chatSet){
            if (c.equals(chat)) return c;
        }
        return null;
    }
}
