package net.coderodde.vfs;

public enum VirtualFileSystemNodeType {

    /**
     * Denotes a regular file.
     */
    FILE,
    
    /**
     * Denotes a directory.
     */
    DIRECTORY,
    
    /**
     * Denotes a symbolic link.
     */
    SYMBOLIC_LINK,
}
