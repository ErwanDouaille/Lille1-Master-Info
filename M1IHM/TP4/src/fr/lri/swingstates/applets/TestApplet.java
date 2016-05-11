package fr.lri.swingstates.applets;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.TextArea;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class TestApplet extends Applet {

	String nomFichier;
	TextArea zoneTexte=new TextArea();

	public void init() {
		nomFichier=getParameter("fichier");
		if (nomFichier==null) nomFichier="textapplet.java";
		setLayout(new BorderLayout());
		add(zoneTexte,BorderLayout.CENTER);
	}

	public String openURL(URL u) throws UnsupportedEncodingException {
		// it is VERY important to read the entire response
		// if you want to connect to the same server again
		// this is because closing the inputstream does not close the socket
		// and response data from a previous request could be mixed up with the current
		InputStream is;
		byte[] buf = new byte[1024];
		URLConnection urlc = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			urlc = u.openConnection();
			is = urlc.getInputStream();
			int len = 0;
			while ((len = is.read(buf)) > 0) {
				bos.write(buf, 0, len);
			}
			// close the inputstream
			is.close();
		} catch (IOException e) {
			try {
				// now failing to read the inputstream does not mean the server did not send
				// any data, here is how you can read that data, this is needed for the same
				// reason mentioned above.
				((HttpURLConnection) urlc).getResponseCode();
				InputStream es = ((HttpURLConnection) urlc).getErrorStream();
				int ret = 0;
				// read the response body
				while ((ret = es.read(buf)) > 0) {
				}
				// close the errorstream
				es.close();
			} catch (IOException ex) {
				// deal with the exception
			}
		}
		return new String(bos.toByteArray(),"UTF-8"); // UTF=8 is a guess, http servers give theire encoding in
		// the http header, to do this correctly read that header and convert to the correct encoding
		// this will give you something to do after you make this method work
	}

	public void start() {
		if(this.getCodeBase().getProtocol().compareTo("file")==0){
			zoneTexte.append("Useless to try and open an URL to the local file system\n");
		}else{
			zoneTexte.append("Useless to try and open a File to URL\n");
		}
		String testTxt;
		try {
			testTxt = openURL(new URL(this.getCodeBase(),"classifier/classifierNCCC.cl"));
			zoneTexte.append(testTxt);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
