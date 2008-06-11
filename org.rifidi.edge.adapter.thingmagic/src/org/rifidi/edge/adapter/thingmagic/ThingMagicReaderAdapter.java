package org.rifidi.edge.adapter.thingmagic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.rifidi.common.utilities.ByteAndHexConvertingUtility;
import org.rifidi.edge.core.readerAdapter.IReaderAdapter;
import org.rifidi.edge.core.tag.TagRead;

public class ThingMagicReaderAdapter implements IReaderAdapter {
	
	boolean connected = false;
	
	private Socket connection = null;
	private BufferedReader in = null;
	private PrintWriter out = null;
	
	private ThingMagicConnectionInfo tmci;
	public ThingMagicReaderAdapter(ThingMagicConnectionInfo connectionInfo){
		tmci = connectionInfo;
	}
	
	@Override
	public boolean connect() {
		try {
			connection = new Socket(tmci.getIPAddress(), tmci.getPort());
			out = new PrintWriter(connection.getOutputStream());
			in = new BufferedReader(new InputStreamReader(connection
				.getInputStream()));
			
			connected = true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean disconnect() {
		connected=false;
		out.flush();
		try {
			connection.close();
		} catch (IOException e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public List<TagRead> getNextTags() {
		String input = null;
		List<TagRead> tags = new ArrayList<TagRead>();
		if(connected){
			out.write("select id, timestamp from tag_id;/n");
			out.flush();
			try {
				 input = readFromReader(in);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}

			if (input.equals("\n"))
				return tags;
			
			//chew up last new line.
			input = input.substring(0, input.lastIndexOf("\n"));
			
			String[] rawTags = input.split("\n");
			
			
			
			for (String rawTag: rawTags){
				String[] rawTagItems = rawTag.split("|");
				
				TagRead tag = new TagRead();
				
				tag.setId(ByteAndHexConvertingUtility.fromHexString(rawTagItems[0]));
				
				//TODO: correct the time stamps.
				tag.setLastSeenTime(System.nanoTime()); 
				tags.add(tag);
			}
			return tags;
		}
		return null;
	}

	@Override
	public void sendCommand(byte[] command) {
		// TODO This needs to be implemented more fully.
		try {
			out.write(new String(command));
			readFromReader(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean isBlocking() {
		// TODO Auto-generated method stub
		return false;
	}

	
	public static String readFromReader(BufferedReader inBuf) throws IOException{
		StringBuffer buf=new StringBuffer();
		
		while(inBuf.ready()){
			int ch=inBuf.read();
			buf.append((char)ch);
		}
		return buf.toString();
	}
}
