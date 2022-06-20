import com.sun.jdi.request.DuplicateRequestException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;

public class Client extends Thread {
    private final Socket socket;
    private InputStream is;
    private OutputStream os;
    private final ChartList chats;
    private Chat chat;
    private String nick;
    private final Scanner in;
    private final PrintStream out;

    public Client(Socket socket, ChartList chats) {
        this.socket = socket;
        this.chats = chats;
        try {
            is = socket.getInputStream();
            os = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        in = new Scanner(is);
        out = new PrintStream(os);
    }

    public String getNick() {
        return nick;
    }

    @Override
    public void run() {
        try {
            while (true) {
                out.println("Welcome!!!");
                while (true) {
                    // Вывод списка чатов
                    out.println("List of chats: ");
                    for (Chat i : chats.getChatSet()) {
                        out.println(i.getName());
                    }
                    // Выбор чата
                    out.print("Please choose chat: ");
                    String inputChat = in.nextLine();
                    if (inputChat.equals("exit")) break;
                    out.print("Please choose nick: ");
                    nick = in.nextLine();
                    if (nick.equals("exit")) break;
                    // добавление клиента в чат с проверкой
                    try {
                        // .. удаление из текущего чата
                        chats.findChart(chat).getClients().remove(this);
                        chat = null;
                    } catch (NullPointerException | NoSuchElementException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            // .. добавление в новый чат
                            chat = chats.findChart(new Chat(inputChat));
                            // .. ToDo тут нужна блокировка
                            if (nick.equals("")) throw new NullPointerException();
                            if (chat.getClients().contains(this)) throw new DuplicateRequestException();
                            else chat.getClients().add(this);
                            // .. клиент добавлен успешно
                            out.println("You added to chat " + chat.getName());
                            out.println("Send message: ");
                            while (true) {
                                String message = in.nextLine();
                                if (message.equals("exit")) break;
                                else chat.addMessage(new Chat.Node(this, message));
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            if (nick.equals(""))
                                out.println("nick can't be null");
                            else
                                out.println("not found chat with name \"%s\"".formatted(inputChat));
                            chat = null;
                        } catch (DuplicateRequestException e) {
                            e.printStackTrace();
                            out.println("User %s is already in chat %s".formatted(nick, chat.getName()));
                            chat = null;
                            nick = null;
                        }
                    }
                }
                interrupt();
                sleep(1);
            }
        } catch(InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
            System.out.println("Client disconnected ");
            return;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(chat, client.chat) && Objects.equals(nick, client.nick);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chat, nick);
    }

    public PrintStream getOut() {
        return out;
    }
}