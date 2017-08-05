package net.coderodde.vfs.app.actions;

import net.coderodde.vfs.app.App;

/**
 * This class implements changing the current native directory.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Aug 5, 2017)
 */
public final class ChangedDirectoryAction extends AbstractCommandAction {

    public ChangedDirectoryAction(App app) {
        super(app);
    }
    
    @Override
    void run(String[] tokens) {
        if (tokens.length != 2) {
            throw new BadCommandException("bad \"cd\" command");
        }
    }
}
