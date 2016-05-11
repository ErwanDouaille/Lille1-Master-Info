package restGateWay;

import org.apache.commons.net.ftp.FTPFile;

/**
 * My goal is to provide a default JSON feedback in case of something went wrong
 *
 * @author DOUAILLE ERWAN & DOUYLLIEZ MAXIME
 */
public class FTPFileEncapsulator extends FTPFile {

    String ErrorString = null;

    public FTPFileEncapsulator(String chaine) {
        super();
        ErrorString = "{\n \"Error\":\"" + chaine + "\" }";
    }

}
