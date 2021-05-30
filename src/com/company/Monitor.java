package com.company;
import jssc.SerialPortException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Monitor {
    double min, max;
    double temperatur;
    double urgent_alarm;
    double urgent_min, urgent_max;

    Sensor sensorobj_1 = new Sensor();
    Brugergraenseflade Brugergraeseflade = new Brugergraenseflade();
    Traadloesnetvaerksystem alarm_server = new Traadloesnetvaerksystem();
    FileWriter Database;
    private final PrintWriter skrivDatabase;

    public Monitor() throws IOException {
        double databaseVersion = (Math.random() * 10);
        Database = new FileWriter("Database_" + databaseVersion); // generering af ny fyl hver gang.
        skrivDatabase = new PrintWriter(Database);
        System.out.println("Patients værdier gemmes under: " + databaseVersion);

    }

    public static void main(String[] args) throws InterruptedException, SerialPortException, IOException {
        Monitor m = new Monitor();

        //Kald af get metoderne, til indtasting af
        m.tryGetmax();
        m.tryGetmin();
        m.tryGeturgent();

        m.Run();

    }

    void Run() throws InterruptedException, SerialPortException {
        //kald af intial sensor værdi
        int counter = 0;

        while (counter<2) {
            Thread.sleep(15000);
            temperatur = sensorobj_1.rigtigSensor();
            if (temperatur < 10 || temperatur > 50) {
                temperatur = sensorobj_1.konverttemperatur();
                System.out.println("Ingen værdi fra sensor, taget værdi fra generator");
            }
            skrivDatabase.println(temperatur);
            skrivDatabase.flush();

            //Print af temp hver 30. sec
            if ((counter % 2) == 0) {
                System.out.println(String.format("%.2f", temperatur) + " celcius.");
            }
            counter++;

            // kontrol af urgent / alarm
            if (temperatur > urgent_max || temperatur < urgent_min) {
                //kalder metoden urgent og slår alarm
                alarm_server.Urgent();
            }

            //kontrol om temperaturer overskrider min eller max
            if (temperatur > max || temperatur < min) {
                // print pop up til sygeplejersken
                Brugergraeseflade.notifikation(temperatur);
            }
        }
    }

    //methoder til intialisering max og min værdier
    void Getmax() {
        max = Brugergraeseflade.setmax();
        while (max < 20 || max > 40) {
            System.out.println("Du skal desværre indsætte en maksimal værdi i intervallet 20-40.");
            max = Brugergraeseflade.setmax();
        }
        System.out.println("Maksimal temperatur i celcius :" + max);
    }

    void Getmin() {
        min = Brugergraeseflade.setmin();
        while (min < 20 || min > 40) {
            System.out.println("Du skal desværre indsætte en maksimal værdi i intervallet 20-40.");
            min = Brugergraeseflade.setmin();
        }
        System.out.println("Minimal temperatur i celcius :" + min);


    }

    //metode til intialisering af urgent værdien
    void Geturgent() {
        urgent_alarm = Brugergraeseflade.SetUrgent();
        while (urgent_alarm < 0 || urgent_alarm > 5) {
            System.out.println("Der må desværre ikke være en højere tollerance end 5% eller mindre tollerance end 0%, prøv igen.");
            urgent_alarm = Brugergraeseflade.SetUrgent();
        }
        urgent_min = min / 100 * (100 - urgent_alarm);
        urgent_max = max / 100 * (urgent_alarm + 100);
        System.out.println("tollerancen er " + urgent_alarm + "%");
        System.out.println("Minimums afvigelse " + String.format("%.2f", urgent_min) + "   " +
                "maximum afviglese " + String.format("%.2f", urgent_max));
    }

    //metode til at finde syntaks fejl i brugers indtastning
    void tryGeturgent() {
        int p = 0;
        while (p == 0) {
            try {
                Geturgent();
                p++;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("du skrev et ikke læsbart tegn i feltet, prøv igen. Her må du gerne skrive komma tal");
            }
        }
    }

    //metode til at finde syntaks fejl i brugers indtastning
    void tryGetmin() {
        int p = 0;
        while (p == 0) {
            try {
                Getmin();
                p++;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("du skrev et ikke læsbart tegn i tekstfeltet, prøv igen med et reelt tal");
            }
        }
    }

    //metode til at finde syntaks fejl i brugers indtastning
    void tryGetmax() {
        int p = 0;
        while (p == 0) {
            try {
                Getmax();
                p++;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("du skrev et ikke læsbart tegn i tekstfeltet, prøv igen med et reelt tal");
            }
        }
    }
}
