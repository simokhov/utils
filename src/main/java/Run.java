import com.sstd.utils.ftp.SstdFtpConnection;
import com.sstd.utils.ftp.SstdFtpManager;

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
            List<String> out = ftpManager.getValidFileList("out/nsi", true);
            for (String f : out) {
                System.out.println(f);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
