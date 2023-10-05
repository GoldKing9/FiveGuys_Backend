package fiveguys.webide.api.project.dto.response;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class FileTreeResponse {

    private String name;
    private String type;
    private String path;
    private List<FileTreeResponse> tree = new ArrayList<>();

    public FileTreeResponse(String name, String type, String path) {
        this.path = path;
        this.name = name;
        this.type = type;
    }

    public void insert(String[] fileParts, int index, String path) {
        if (index == fileParts.length) return;

        String filePart = fileParts[index];

        String fileType;
        if(filePart.contains(".")) {
            fileType = "file";
        } else {
            fileType = "folder";
        }
        FileTreeResponse newTreeNode = new FileTreeResponse(filePart, fileType, path);
        if(!tree.contains(newTreeNode)) {
            tree.add(newTreeNode);
        }

        tree.get(tree.indexOf(newTreeNode)).insert(fileParts, index+1, path);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileTreeResponse treeNode = (FileTreeResponse) o;
        return Objects.equals(name, treeNode.name) && Objects.equals(type, treeNode.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }
}