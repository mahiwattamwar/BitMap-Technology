package com.example.anti_theft;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class send extends AsyncTask<Void,Void,Void> {

    private Context context;
    private String email;
    private String subject;
    private  String message;


    public send(Context context, String email, String subject, String message)
    {
        this.context = context;
        this.email = email;
        this.subject = subject;
        this.message = message;


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();


        Toast.makeText(context,"Mail Send To" + email.toString(),Toast.LENGTH_LONG).show();

//        Toast.makeText(context,subject.toString(),Toast.LENGTH_LONG).show();
//        Toast.makeText(context,message.toString(),Toast.LENGTH_LONG).show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        final String username = "madhuri.sct@gmail.com";
        String password = "qbfpnsybximtxbgp";
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                        new Authenticator(){
                            @Override
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(username,password);

                            }
                        });

        try {
                    Message mm = new MimeMessage(session);
                    mm.setFrom(new InternetAddress("madhuri.sct@gmail.com"));
                    mm.setRecipients(Message.RecipientType.TO,
                            InternetAddress.parse(email));
                   mm.setSubject("Call log");
                    mm.setText("image from gallery");
            MimeBodyPart messageBodyPart1 = new MimeBodyPart();
            messageBodyPart1.setText("image");

            MimeBodyPart messageBodyPart2 = new MimeBodyPart();
            DataSource source = new FileDataSource(message);
            messageBodyPart2.setDataHandler(new DataHandler(source));

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart1);
            multipart.addBodyPart(messageBodyPart2);
            mm.setContent(multipart);
                    Transport.send(mm);

                    //Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_LONG).show();
                    System.out.println("Done");

                }catch (MessagingException e)

                {
                    e.printStackTrace();
                }
        return null;
    }
}
