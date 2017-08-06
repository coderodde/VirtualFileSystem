package net.coderodde.vfs;

public enum VirtualFileSystemNodeType {

    /**
     * Denotes a regular file.
     */
    REGULAR_FILE,
    
    /**
     * Denotes a directory.
     */
    DIRECTORY,
    
    /**
     * Denotes a symbolic link.
     */
    SYMBOLIC_LINK,
}
