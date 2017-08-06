package net.coderodde.vfs;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This class implements a virtual file system node.
 * 
 * @author Rodion "rodde" Efremov
 * @version 1.6 (Aug 4, 2017)
 */
public class VirtualFileSystemNode {
    
    /**
     * Maximum number of {@code char}s in a node name regardless of the type of
     * the node.
     */
    private static final int MAXIMUM_NODE_NAME_LENGTH = 64;
    
    /**
     * Stores the node type of this node.
     */
    private final VirtualFileSystemNodeType nodeType;
        
    /**
     * Points to the parent node that is a directory. For all nodes except the
     * root node "/" this field is not null.
     */
    private VirtualFileSystemNode parent;
    
    /**
     * If this node is a file and was accessed, stores the binary data of the 
     * file.
     */
    private byte[] fileData;
    
    /**
     * If this object is a file, caches the size of the file. Otherwise, this 
     * object is directory, and this field caches the size of all files in this
     * subtree. If this node is a link, caches the size of the linked node.
     */
    private int fileSize;
    
    /**
     * Stores the time at which this node was created. The number of 
     * milliseconds since the Unix Epoch (00:00:00 UTC Thursday January 1, 
     * 1970).
     */
    private long creationTime;
    
    /**
     * The time stamp of the most recent access to the node. This time stamp 
     * handles only read operations to the node.
     */
    private long lastAccessedTime;
    
    /**
     * The time stamp of the most recent modification to the node. For a file,
     * modification are modifying the data or renaming the file. For a
     * directory, these are adding/removing a file to the directory or renaming
     * it.
     */
    private long lastModificationTime;
    
    /**
     * If this node is a directory, this map maps each node name to its node.
     */
    private Map<String, VirtualFileSystemNode> childMap;
    
    /**
     * This field contains the name of this node. If this node represents a
     * file, this is the name of that very file. Otherwise, this object 
     * represents a directory.
     */
    private String nodeName;
    
    /**
     * If this node is a symbolic link, this field points to the linked node.
     */
    private VirtualFileSystemNode link;
    
    /**
     * If this node is password protected, this field contains the SHA-256 hash
     * of the password. If no password is set for this node, this field is
     * {@code null}.
     */
    private byte[] passwordHash;
    
    /**
     * Set to {@code true} whenever this node represents a regular file whose
     * data was modified but not committed.
     */
    private boolean dirty;
    
    private final List<VirtualFileBlock> dataBlockList;
    
    public static VirtualFileSystemNode 
        createRegularFile(String fileName, 
                          String password, 
                          VirtualFileSystemNode parentDirectory) {
        return null;
    }
        
    public static VirtualFileSystemNode
        createDirectory(String directoryName,
                        String password,
                        VirtualFileSystemNode parentDirectory) {
        return null;       
    }
        
    public static VirtualFileSystemNode
        createSymbolicLink(String linkName,
                           VirtualFileSystemNode parentDirectory,
                           VirtualFileSystemNode linkedNode) {
        return null;
    }
    
    private VirtualFileSystemNode(String nodeName,
                                  String password,
                                  VirtualFileSystemNode parentDirectory,
                                  VirtualFileSystemNodeType nodeType,
                                  VirtualFileSystemNode linked) {
        Objects.requireNonNull(nodeName, "The node name is null.");
        Objects.requireNonNull(parentDirectory,     
                               "The parent directory is null.");
        Objects.requireNonNull(nodeType, "The node type is null.");
        
        this.nodeName = checkNodeName(nodeName, parentDirectory);
        this.nodeType = nodeType;
        
        switch (nodeType) {
            case REGULAR_FILE:
                
            case DIRECTORY:
                
            case SYMBOLIC_LINK:
                
        }
        
        if (password != null) {
            try {
                byte[] passwordHash = 
                        MessageDigest
                            .getInstance("SHA-256")
                            .digest(password.getBytes(StandardCharsets.UTF_8));
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException("Cannot hash the password.", ex);
            }
        }
        
        long currentTime = System.currentTimeMillis();
        
        this.creationTime         = currentTime;
        this.lastAccessedTime     = currentTime;
        this.lastModificationTime = currentTime;        
    }
        
    public boolean containsNodeName(String name) {
        switch (nodeType) {
            case DIRECTORY:
                return childMap.containsKey(name);
                
            case REGULAR_FILE:
                throw new IllegalStateException("This node is a regular file.");
                
            case SYMBOLIC_LINK:
                return link.containsNodeName(name);
                
            default:
                throw new IllegalStateException("This should not be thrown.");
        }
    }
    
    private String checkNodeName(String name, 
                                 VirtualFileSystemNode parentDirectory) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("The name of a node is empty.");
        }
        
        if (name.length() > MAXIMUM_NODE_NAME_LENGTH) {
            throw new IllegalArgumentException("The node name is " +
                    (name.length() - MAXIMUM_NODE_NAME_LENGTH) +
                    " character(s) too long.");
        }
        
        if (parentDirectory.containsNodeName(name)) {
            throw new IllegalArgumentException("The name \"" + name + "\"" +
                    " is already occupied.");
        }
        
        return name;
    }
}
