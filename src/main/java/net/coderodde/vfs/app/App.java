package net.coderodde.vfs.app;

import java.io.File;
import java.io.PrintStream;
import java.util.Objects;
import java.util.Scanner;
import net.coderodde.command.line.AbstractCommandProvider;
import net.coderodde.command.line.support.BufferedCommandProvider;

public class App {
    
    private static final String PROMPT = "> ";

    private final Scanner scanner;
    private final PrintStream out;
    private final PrintStream err;
    private final AbstractCommandProvider commandProvider;
    private boolean done = false;
    private String currentNativeDirectory;
    
    public App(Scanner scanner, PrintStream out, PrintStream err) {
        this.scanner = Objects.requireNonNull(scanner, 
                                              "The input scanner is null.");
        
        this.out = Objects.requireNonNull(out, 
                                          "The output print writer is null.");
        
        this.err = Objects.requireNonNull(err,
                                          "The error print writer is null.");
        
        this.commandProvider = new BufferedCommandProvider();
        commandProvider.addCommandListener(new AppCommandListener(this));
    }
    
    public void run() {
        while (!done) {
            System.out.print(PROMPT);
            String line = scanner.nextLine();
            commandProvider.processLine(line);
        }
        
        System.out.println("Bye!");
    }
    
    public void execute(String command) {
        if (command.endsWith(commandProvider.getCommandSeparator())) {
            System.err.println("Fuck yeah!");
            command = command.substring(0, command.length() - 2); // Remove the command separator.
        } else {
            System.err.println("Fuck no!");
        }
        
        String[] words = command.split("\\s+");
        
        
    }
    
    public void changeCurrentNativeDirectory(String newCurrentNativeDirectory) {
        File file = new File(newCurrentNativeDirectory);
        
        if (!file.exists()) {
            error("directory \"" + newCurrentNativeDirectory + "\" does not " +
                  "exist.");
            return;
        }
        
        if (!file.isDirectory()) {
            error("\"" + newCurrentNativeDirectory + "\" is not a directory " +
                  "name.");
            return;
        }
        
        this.currentNativeDirectory = newCurrentNativeDirectory;
    }
    
    public void requestExit() {
        done = true;
    }
    
    public static void main(String[] args) {
        App app = new App(new Scanner(System.in), System.out, System.err);
        app.run();
    }
    
    private void error(String message) {
        err.println("Error: " + message);
    }
}
