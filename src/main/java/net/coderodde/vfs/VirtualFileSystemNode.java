//package net.coderodde.vfs;
//
//import java.nio.charset.StandardCharsets;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.util.Map;
//import java.util.Objects;
//
///**
// * This class implements a virtual file system node.
// * 
// * @author Rodion "rodde" Efremov
// * @version 1.6 (Aug 4, 2017)
// */
//public class VirtualFileSystemNode {
//    
//    /**
//     * Stores the node type of this node.
//     */
//    private final VirtualFileSystemNodeType nodeType;
//        
//    /**
//     * Points to the parent node that is a directory.
//     */
//    private VirtualFileSystemNode parent;
//    
//    /**
//     * If this node is a directory, this map maps each node name to its node.
//     */
//    private Map<String, VirtualFileSystemNode> childMap;
//    
//    /**
//     * This field contains the name of this node. If this node represents a
//     * file, this is the name of that very file. Otherwise, this object 
//     * represents a directory.
//     */
//    private String name;
//    
//    /**
//     * If this node is a symbolic link, this field points to the linked node.
//     */
//    private VirtualFileSystemNode link;
//    
//    /**
//     * If this node is a file, this field contains the binary data of the file.
//     * Otherwise, it is set to {@code null}.
//     */
//    private byte[] data;
//    
//    /**
//     * If this node is password protected, this field contains the SHA-256 hash
//     * of the password. If no password is set for this node, this field is
//     * {@code null}.
//     */
//    private byte[] passwordHash;
//    
//    /**
//     * If this object is a file, caches the size of the file. Otherwise, this 
//     * object is directory, and this field caches the size of all files in this
//     * subtree.
//     */
//    private int size;
//    
//    private VirtualFileSystemNode(String name, 
//                                  String password,
//                                  VirtualFileSystemNode parent,
//                                  VirtualFileSystemNodeType nodeType,
//                                  VirtualFileSystemNode link) {
//        this.parent = Objects.requireNonNull(parent, 
//                                             "The parent node is null.");
//        this.nodeType = Objects.requireNonNull(nodeType, 
//                                               "The node type is null.");
//        this.name = checkName(name, parent);
//        
//        if (password != null) {
//            try {
//                MessageDigest messageDigest = 
//                        MessageDigest.getInstance("SHA-256");
//                this.passwordHash = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
//            } catch (NoSuchAlgorithmException ex) {
//                System.err.println("No SHA-256 algorithm. Terminating...");
//                System.exit(-1);
//            }
//        }
//        
//        switch (nodeType) {
//            
//        }
//    }
//    
//    public boolean containsFile(String name) {
//        
//    }
//    
//    private String checkName(String name, VirtualFileSystemNode parent) {
//        if (name == null) {
//            throw new IllegalArgumentException("The name of a node is null.");
//        }
//        
//        if (name.isEmpty()) {
//            throw new IllegalArgumentException("The name of a node is empty.");
//        }
//        
//        if (parent.containsFile(name)) {
//            throw new IllegalArgumentException("The name \"" + name + "\"" +
//                    " is already occupied.");
//        }
//        
//        return name;
//    }
//}
