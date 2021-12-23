package sendfile.client;


import sendfile.client.MainForm;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitorInputStream;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ali
 */
public class ReceivingFileThread implements Runnable {
    
    protected Socket socket;
    protected DataInputStream dis;
    protected DataOutputStream dos;
    protected MainForm main;
    protected StringTokenizer st;
    protected DecimalFormat df = new DecimalFormat("##,#00");
    private final int BUFFER_SIZE = 100;
    
    public ReceivingFileThread(Socket soc, MainForm m){
        this.socket = soc;
        this.main = m;
        try {
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("[ReceivingFileThread]: " +e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            while(!Thread.currentThread().isInterrupted()){
                String data = dis.readUTF();
                st = new StringTokenizer(data);
                String CMD = st.nextToken();
                
                switch(CMD){
                    
                    //   Fungsi ini akan menangani penerimaan file dalam proses latar belakang dari pengguna lain
                    case "CMD_SENDFILE":
                        String consignee = null;
                            try {
                                String filename = st.nextToken();
                                int filesize = Integer.parseInt(st.nextToken());
                                consignee = st.nextToken(); // Get the Sender Username
                                main.setMyTitle("Memuat file ....");
                                System.out.println("sabar sedang di muat filenya ....");
                                System.out.println("From: "+ consignee);
                                String path = main.getMyDownloadFolder() + filename;                                
                                /*  Creat Stream   */
                                FileOutputStream fos = new FileOutputStream(path);
                                InputStream input = socket.getInputStream();                                
                                /*  Monitor Progress   */
                                ProgressMonitorInputStream pmis = new ProgressMonitorInputStream(main, "Downloading file please wait...", input);
                                /*  Buffer   */
                                BufferedInputStream bis = new BufferedInputStream(pmis);
                                /**  Create a temporary file **/
                                byte[] buffer = new byte[BUFFER_SIZE];
                                int count, percent = 0;
                                while((count = bis.read(buffer)) != -1){
                                    percent = percent + count;
                                    int p = (percent / filesize);
                                    main.setMyTitle("Downloading File  "+ p +"%");
                                    fos.write(buffer, 0, count);
                                }
                                fos.flush();
                                fos.close();
                                main.setMyTitle("you are logged in as: " + main.getMyUsername());
                                JOptionPane.showMessageDialog(null, "File telah diunduh ke \n'"+ path +"'");
                                System.out.println("File telah disimpan ke: "+ path);
                            } catch (IOException e) {
                                /*                               
                                Terdapat kesalahan kirim ulang
                                Format: CMD_SENDFILERESPONSE [username] [Message]
                                */
                                DataOutputStream eDos = new DataOutputStream(socket.getOutputStream());
                                eDos.writeUTF("CMD_SENDFILERESPONSE "+ consignee + " Sambungan terputus coba lagi");
                                
                                System.out.println(e.getMessage());
                                main.setMyTitle("Kamu masuk sebagai: " + main.getMyUsername());
                                JOptionPane.showMessageDialog(main, e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
                                socket.close();
                            }
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("[ReceivingFileThread]: " +e.getMessage());
        }
    }
}

