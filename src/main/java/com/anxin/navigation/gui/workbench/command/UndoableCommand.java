package com.anxin.navigation.gui.workbench.command;

public interface UndoableCommand<C> {
    void execute(C context);

    void undo(C context);

    String successMessage();
}
