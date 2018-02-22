import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Vector;

import com.pff.PSTAttachment;
import com.pff.PSTException;
import com.pff.PSTFile;
import com.pff.PSTFolder;
import com.pff.PSTMessage;
import com.pff.PSTObject;

public class Handler {


  public static void handle(String file_path)
      throws FileNotFoundException, PSTException, IOException {
    Path root_dir_path = Fs.rootPath(file_path);
    PSTFolder root = (new PSTFile(file_path)).getRootFolder();

    Folder.handleFolder(root, root_dir_path, 0);
  }

  private static class Folder {
    private static int handleFolder(PSTFolder folder, Path parent_dir_path, int count)
        throws IOException, PSTException {
      count += 1;
      Path dir_path = Fs.appendString2Path(parent_dir_path, "[R"+count+"]"+folder.getDisplayName());
      Fs.mkdir(dir_path);
      count = loopMessages(folder, dir_path, count);
      count = loopSubFolders(folder, dir_path, count);
      return count;
    }

    private static int loopMessages(PSTFolder folder, Path parent_dir_path, int count)
        throws IOException, PSTException {
      if (folder.getEmailCount() > 0) {
        PSTMessage message = (PSTMessage)folder.getNextChild();
        while (message != null) {
          count = Message.handleMessage(message, parent_dir_path, count);
          message = (PSTMessage)folder.getNextChild();
        }
      }
      return count;
    }

    private static int loopSubFolders(PSTFolder folder, Path parent_dir_path, int count)
        throws IOException, PSTException {
      if (folder.hasSubfolders() && folder.getSubFolderCount() > 0) {
        Vector<PSTFolder> children = folder.getSubFolders();
        for (PSTFolder child : children) {
          count = handleFolder(child, parent_dir_path, count);
        }
      }
      return count;
    }
  }


  private static class Message {
    private static int handleMessage(PSTMessage message, Path parent_dir_path, int count)
        throws IOException, PSTException {
      count += 1;
      String message_name = "[M"+count+"]"+message.getSubject();

      Path dir_path = Fs.appendString2Path(parent_dir_path, message_name);
      Fs.mkdir(dir_path);

      Path txt_file_path = Fs.appendString2Path(dir_path, message_name+".txt");
      message2Files(message,txt_file_path);

      count = loopAttachments(message, dir_path, count);
      return count;
    }

    private static void message2Files(PSTMessage message, Path path)
        throws IOException {
      Fs.writeString(path, message.getBody());
    }

    private static int loopAttachments(PSTMessage message, Path parent_dir_path, int count)
        throws IOException, PSTException {
      int num_attach = message.getNumberOfAttachments();
      for (int i = 0; i<num_attach ; i++) {
        PSTAttachment attach = message.getAttachment(i);
        count = Attachment.handleAttachment(attach, parent_dir_path, count);
      }
      return count;
    }
  }

  private static class Attachment {
    private static int handleAttachment(PSTAttachment attach, Path parent_dir_path, int count)
        throws IOException, PSTException {
      count += 1;
      String attach_name = "[P"+count+"]"+attach.getLongFilename();

      Path dir_path = Fs.appendString2Path(parent_dir_path, attach_name);
      Fs.mkdir(dir_path);

      Path file_path = Fs.appendString2Path(dir_path, attach_name);
      attachment2Files(attach, file_path);
      return count;
    }

    private static void attachment2Files(PSTAttachment attach, Path path)
        throws IOException, PSTException {
      Fs.writeStream(path, attach.getFileInputStream());
    }
  }


}

