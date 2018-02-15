import com.sstd.utils.ftp.SstdFtpConnection;
import com.sstd.utils.ftp.SstdFtpManager;
import com.sstd.utils.zip.SstdZipManager;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Run {
    public static void main(String[] args) {
        SstdFtpConnection sstdFtpConnection;
        try {
            sstdFtpConnection = new SstdFtpConnection(
                    "ftp.zakupki.gov.ru",
                    21,
                    "fz223free",
                    "fz223free"
            );

            SstdFtpManager ftpManager = new SstdFtpManager(sstdFtpConnection);

            List<String> out = ftpManager.getValidFileList("out/nsi/nsiOkv/nsiOkv_all_20131027_010753_001.xml.zip", true);
            for (String f : out) {
                System.out.println(f);
            }
            FTPFile ftpFileInfoByRemotePath = ftpManager.getFtpFileInfoByRemotePath("out/nsi/nsiOkv/nsiOkv_all_20131027_010753_001.xml.zip");
            System.out.println(ftpFileInfoByRemotePath.toString());

            List<String> fileList = ftpManager.getRemoteFileList(out, new File("/tmp"));
            for (String s : fileList) {
                System.out.println(s);
                SstdZipManager.unpack(s, "/tmp");
            }



        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
