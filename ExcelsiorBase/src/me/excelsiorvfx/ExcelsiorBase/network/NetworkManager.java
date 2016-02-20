package me.excelsiorvfx.ExcelsiorBase.network;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;

import me.excelsiorvfx.ExcelsiorBase.ExcelsiorBase;
import me.excelsiorvfx.ExcelsiorBase.API.ExcelsiorAPI;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class NetworkManager implements ExcelsiorAPI {

	public static String checkMOTD(String host) {
		try {
			Socket socket = new Socket();
			OutputStream out;
			DataOutputStream dataOut;
			InputStream in;
			InputStreamReader reader;
			
			socket.setSoTimeout(2500);
			socket.connect(new InetSocketAddress(host, 25565), 2500);
			
			out = socket.getOutputStream();
			dataOut = new DataOutputStream(out);
			
			in = socket.getInputStream();
			reader = new InputStreamReader(in, Charset.forName("UTF-16BE"));
			
			dataOut.write(new byte[] {(byte) 0xFE, (byte) 0x01});
			
			int packetID = in.read();
			if (packetID == -1) {
				return null;
			} if (packetID != 0xFF) {
				return null;
			}
			
			int length = reader.read();
			if (length == -1 || length == 0) {
				return null;
			}
			
			char[] chars = new char[length];
			
			if (reader.read(chars, 0, length) != length) 
				return null;
			
			String str = new String(chars);
			String[] data = str.split("\0");
			
			return data[3];
			 
		} catch(Exception e) {
			return null;
		}
	}
	
	public static String checkMOTD(int port, String host) {
		try {
			Socket socket = new Socket();
			OutputStream out;
			DataOutputStream dataOut;
			InputStream in;
			InputStreamReader reader;
			
			socket.setSoTimeout(2500);
			socket.connect(new InetSocketAddress(host, port), 2500);
			
			out = socket.getOutputStream();
			dataOut = new DataOutputStream(out);
			
			in = socket.getInputStream();
			reader = new InputStreamReader(in, Charset.forName("UTF-16BE"));
			
			dataOut.write(new byte[] {(byte) 0xFE, (byte) 0x01});
			
			int packetID = in.read();
			if (packetID == -1) {
				return null;
			} if (packetID != 0xFF) {
				return null;
			}
			
			int length = reader.read();
			if (length == -1 || length == 0) {
				return null;
			}
			
			char[] chars = new char[length];
			
			if (reader.read(chars, 0, length) != length) 
				return null;
			
			String str = new String(chars);
			String[] data = str.split("\0");
			
			return data[3];
			 
		} catch(Exception e) {
			return null;
		}
	}
	
	public static boolean transfer(Player p, String server) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		try {
			out.writeUTF("Connect");
			out.writeUTF(server);
		} catch (Exception e) {
			return false;
		}
		p.sendPluginMessage(ExcelsiorBase.instance, "BungeeCord", out.toByteArray());
		return true;
	}

	@Override
	public void enable(JavaPlugin p) {
		try {
		p.getServer().getMessenger().registerOutgoingPluginChannel(p, "BungeeCord");
		p.getLogger().info("Connected into BungeeCord, ready to transfer players.");
		} catch (Exception e) {
			p.getLogger().info("Unable to connect to BungeeCord.");
		}
	}

}
