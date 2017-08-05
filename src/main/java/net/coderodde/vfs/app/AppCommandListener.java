package net.coderodde.vfs.app;

import net.coderodde.vfs.app.actions.AbstractCommandAction;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import net.coderodde.command.line.CommandListener;

public class AppCommandListener implements CommandListener {

    private static final Map<String, AbstractCommandAction> actionDispatchTable =
            new HashMap<>();
    
    static {
        actionDispatchTable.put("pwd", null);
        actionDispatchTable.put("vpwd", null);
        actionDispatchTable.put("cd", null);
        actionDispatchTable.put("vcd", null);
        actionDispatchTable.put("v2n", null);
        actionDispatchTable.put("n2v", null);
        actionDispatchTable.put("link", null);
        actionDispatchTable.put("help", null);
        actionDispatchTable.put("psswd", null);
    }
    
    private final App app;
    
    public AppCommandListener(App app) {
        this.app = Objects.requireNonNull(app, "The input app is null.");
    }
    
    @Override
    public void onCommand(String command) {
        app.execute(command);
    }
}
