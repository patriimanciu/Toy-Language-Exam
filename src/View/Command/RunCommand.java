package View.Command;

import Controller.Controller;
import Utils.Exceptions.MyException;

public class RunCommand extends Command {
    private Controller controller;

    public RunCommand(String key, String description, Controller controller) {
        super(key, description);
        this.controller = controller;
    }

    @Override
    public void execute() {
        try {
            controller.allStep();
            System.out.println("All run steps executed");
        } catch (MyException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
