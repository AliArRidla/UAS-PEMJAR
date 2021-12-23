/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package uaspemjar;

import sendfile.client.LoginForm;
import sendfile.server.MainForm;

/**
 *
 * @author bee
 */
public class UASPEMJAR {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        MainForm mainForm = new MainForm();
        LoginForm loginForm = new LoginForm();
        
         /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                mainForm.setVisible(true);
            }
        });
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                loginForm.setVisible(true);
            }
        });
        
    }
    
}
 