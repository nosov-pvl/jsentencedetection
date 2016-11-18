package ru.spbstu.jsentencedetection.loaders;

import com.pff.*;

import java.io.IOException;
import java.util.*;

public class LoaderPST implements MessagesLoader {

    PSTFolder pstRootFolder = null;
    public boolean load(String fileName) {
        try {
            PSTFile pstFile = new PSTFile(fileName);
            pstRootFolder = pstFile.getRootFolder();
        } catch (PSTException | IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<Message> getMessages() {
        List<Message> result = new ArrayList<>();
        try {
            for(PSTFolder pstFolder : getAllFolders()) {
                PSTMessage pstMessage;
                while((pstMessage = (PSTMessage)pstFolder.getNextChild()) != null) {
                    Message message = new Message();
                    message.setSubject(pstMessage.getSubject());
                    message.setBody(pstMessage.getBody());
                    result.add(message);
                }
            }
        } catch (PSTException | IOException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    private List<PSTFolder> getAllFolders() throws PSTException , IOException{
        List<PSTFolder> result = new ArrayList<>();
        result.add(pstRootFolder);
        for(int i = 0; i < result.size(); ++i) {
            try {
                PSTFolder currentFolder = result.get(i);
                if(currentFolder.getSubFolderCount() > 0) {
                    result.addAll(currentFolder.getSubFolders());
                }
            } catch (PSTException | IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
