package net.coderodde.vfs.app.actions;

import java.util.Objects;
import net.coderodde.vfs.app.App;

/**
 * This abstract class defines the API for command actions.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Aug 5, 2017)
 */
public abstract class AbstractCommandAction {

    private final App app;
    
    protected AbstractCommandAction(App app) {
        this.app = Objects.requireNonNull(app, "The input App is null.");
    }
    
    abstract void run(String[] tokens);
}
