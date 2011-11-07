/**
 *  Program: Chiesa di Bologna
 *  Author: Matteo Fini <mf.calimero@gmail.com>
 *  Year: 2011
 *  
 *	This file is part of "Chiesa di Bologna".
 *	"Chiesa di Bologna" is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  "Chiesa di Bologna" is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  You should have received a copy of the GNU General Public License
 *  along with Foobar.  If not, see <http://www.gnu.org/licenses/>. 
 */
package com.matteofini.chiesabologna;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class ChiesaBO extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        String url = "http://www.bologna.chiesacattolica.it/portale/pagine/";
        try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			URL conn_url = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) conn_url.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoOutput(true);
			conn.setConnectTimeout(5000);
			conn.connect();
			
			InputStream is = conn.getInputStream();
			BufferedInputStream buf = new BufferedInputStream(is, 8192);
			DataInputStream data = new DataInputStream(buf);
			int n=0;
			buf.mark(100000);
			byte[] bytebuf = new byte[8192]; 
			CharBuffer cbuf = ByteBuffer.wrap(bytebuf).asCharBuffer();
			String str="";
			String tot="";
			do{
				str = data.readUTF();
				tot = tot+str;
			}
			while(str.length()>=0);
			
				System.out.println(tot);
			
			Document doc = db.parse(conn.getInputStream());
			
			
			XPathFactory xpf = XPathFactory.newInstance();
			XPath xp =  xpf.newXPath();
			InputSource insource = new InputSource(conn.getInputStream());
			String res = xp.evaluate("//a", insource);
			System.out.println(res);
			
		} catch (ParserConfigurationException e) {
			Log.println(Log.WARN, "chiesaBO", "\t Errore nella creazione del parser");
			e.printStackTrace();
		} catch (IOException e) {
			Log.println(Log.WARN, "chiesaBO", "\t Errore nell'apertura della connessione con l'url "+url);
			e.printStackTrace();
		} catch (SAXException e) {
			Log.println(Log.WARN, "chiesaBO", "\t Errore durante il parsing del documento "+url);
			Log.e("ChiesaBO:parse", e.getMessage()+" "+e.getLocalizedMessage());
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			Log.println(Log.WARN, "chiesaBO", "\t Errore durante la valurazione dell'xpath");
			e.printStackTrace();
		}
		
        
        
    }
}