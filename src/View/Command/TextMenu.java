package View.Command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TextMenu {
    private Map<String, Command> commands;
    public TextMenu() {
        commands = new HashMap<>();
    }

    public void addCommand(Command c) {
        commands.put(c.getKey(), c);
    }

    private void print() {
        for (Command com : commands.values()) {
            String line = String.format("%4s : %s", com.getKey(), com.getDescription());
            System.out.println(line);
        }
    }

    public void show() {
        ArrayList<String> logs = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            print();
            System.out.print("> ");
            String key = scanner.nextLine();
            Command com = commands.get(key);
            if (com == null) {
                System.out.println("Unknown command");
                continue;
            }
            if (logs.contains(com.getKey())) {
                System.out.println("Command already executed");
                continue;
            }
            com.execute();
            logs.add(com.getKey());
        }
    }
}
